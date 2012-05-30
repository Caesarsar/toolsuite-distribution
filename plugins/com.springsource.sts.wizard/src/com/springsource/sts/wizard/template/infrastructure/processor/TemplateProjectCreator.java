/******************************************************************************************
 * Copyright (c) 2009 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.wizard.template.infrastructure.processor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.springframework.util.AntPathMatcher;
import org.springsource.ide.eclipse.commons.core.Policy;
import org.springsource.ide.eclipse.commons.core.ZipFileUtil;
import org.springsource.ide.eclipse.commons.ui.UiStatusHandler;

import com.springsource.sts.wizard.WizardPlugin;

/**
 * @author Terry Denney
 */
public class TemplateProjectCreator {

	private final Shell shell;

	private volatile IProject project;

	private final URL archiveFile;

	private final ProcessingInfo processingInfo;

	private final TemplateProcessor templateProcessor;

	private final TemplateProcessor fileNameProcessor;

	private final IPath projectPath;

	public TemplateProjectCreator(IProject project, IPath projectPath, URL archiveFile, Shell shell,
			TemplateProcessor templateProcessor, TemplateProcessor fileNameProcessor, ProcessingInfo processingInfo) {
		this.project = project;
		this.projectPath = projectPath;
		this.archiveFile = archiveFile;
		this.shell = shell;
		this.templateProcessor = templateProcessor;
		this.fileNameProcessor = fileNameProcessor;
		this.processingInfo = processingInfo;
	}

	public IProject createProject(IProgressMonitor monitor) throws CoreException {
		final String projectName = project.getName();
		final File unzipFolder = unzipProject(monitor);

		final WorkspaceModifyOperation op = new WorkspaceModifyOperation() {

			@Override
			protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException,
					InterruptedException {
				monitor.subTask("Creating project " + projectName);

				Policy.checkCancelled(monitor);
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				IProjectDescription projectDescription = workspace.newProjectDescription(projectName);
				projectDescription.setLocation(projectPath);
				project.create(projectDescription, new SubProgressMonitor(monitor, 10));
				project.open(new SubProgressMonitor(monitor, 20));

				monitor.subTask("Importing project " + projectName);
				Policy.checkCancelled(monitor);
				try {
					importFiles(project, unzipFolder.listFiles()[0], new SubProgressMonitor(monitor, 70));
				}
				catch (IOException e) {
					final Status status = new Status(Status.ERROR, WizardPlugin.PLUGIN_ID,
							"Unable to create template project", e);
					throw new CoreException(status);
				}
			}
		};

		try {
			monitor.beginTask("Importing project", 100);

			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			project = workspace.getRoot().getProject(projectName);

			if (project.exists()) {
				final boolean[] result = new boolean[1];
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						result[0] = MessageDialog.openQuestion(shell, "Create Template Project", "A project named "
								+ project.getName()
								+ " already exists in the workspace. Overwrite the existing project?");
					}
				});

				if (!result[0]) {
					return null;
				}

				project.delete(true, true, monitor);
			}

			op.run(monitor);
			monitor.done();
		}
		catch (InterruptedException e) {
			throw new OperationCanceledException();
		}
		catch (InvocationTargetException e) {
			Status status = new Status(IStatus.ERROR, WizardPlugin.PLUGIN_ID, "Error during template project creation",
					e);
			UiStatusHandler.logAndDisplay(status);
			throw new CoreException(status);
		}
		finally {
			if (unzipFolder != null) {
				deleteFile(unzipFolder);
			}
			monitor.done();
		}

		return project;
	}

	private void deleteFile(File file) {
		if (file.isDirectory()) {
			File[] children = file.listFiles();
			for (File child : children) {
				deleteFile(child);
			}
		}

		file.delete();
	}

	private File getMappedFile(File source, File destination, String sourceBasePath, String destinationBasePath) {
		if (source.isDirectory()) {
			String path = source.getPath();
			String mappedPath = fileNameProcessor.replacePathForDirectory(path, File.separatorChar);
			if (mappedPath != null) {
				mappedPath = destinationBasePath + mappedPath.substring(sourceBasePath.length());
				return new File(mappedPath);
			}
		}

		String contentName = source.getName();
		String mappedContentName = fileNameProcessor.replaceTokens(contentName);
		return new File(destination, mappedContentName);
	}

	private void importFiles(IProject project, File unzipFolder, IProgressMonitor monitor) throws IOException,
			InvocationTargetException, InterruptedException {
		FileSystemStructureProvider provider = FileSystemStructureProvider.INSTANCE;
		ImportOperation operation = new ImportOperation(project.getFullPath(), unzipFolder, provider,
				new IOverwriteQuery() {
					// always overwrite
					public String queryOverwrite(String pathString) {
						return IOverwriteQuery.YES;
					}
				});
		operation.setContext(shell);

		// need to overwrite .project file
		operation.setOverwriteResources(true);
		operation.setCreateContainerStructure(false);
		operation.run(monitor);
	}

	private void processDirectory(File source, File destination, String sourceBasePath, String destinationBasePath)
			throws IOException {
		for (File content : source.listFiles()) {
			File subFileOrDir = getMappedFile(content, destination, sourceBasePath, destinationBasePath);

			if (shouldExclude(subFileOrDir)) {
				continue;
			}
			if (content.isDirectory()) {
				subFileOrDir.mkdirs();
				processDirectory(content, subFileOrDir, sourceBasePath, destinationBasePath);
			}
			else {
				templateProcessor.process(content, subFileOrDir);
			}
		}
	}

	private boolean shouldExclude(File file) {
		AntPathMatcher pathMatcher = new AntPathMatcher();
		for (String exclusionPattern : processingInfo.getExclusionPatterns()) {
			if (pathMatcher.match(exclusionPattern, file.getAbsolutePath().replace('\\', '/'))) {
				return true;
			}
		}
		return false;
	}

	private File unzipProject(IProgressMonitor monitor) throws CoreException {
		monitor.beginTask("Unzip project", 10);
		File unzipFolder = null;
		try {
			File tempFile = File.createTempFile("tempFile", null);
			String parentPath = tempFile.getParent();
			tempFile.delete();

			unzipFolder = new File(parentPath + "/unzipFolder");
			if (unzipFolder.exists()) {
				deleteFile(unzipFolder);
			}

			unzipFolder.mkdir();

			ZipFileUtil.unzip(archiveFile, unzipFolder, monitor);

			File tempFolder = new File(parentPath + "/tempFolder");
			if (tempFolder.exists()) {
				deleteFile(tempFolder);
			}

			tempFolder.mkdir();

			processDirectory(unzipFolder, tempFolder, unzipFolder.getPath(), tempFolder.getPath());

			deleteFile(unzipFolder);
			monitor.done();
			return tempFolder;
		}
		catch (IOException e) {
			Status status = new Status(IStatus.ERROR, WizardPlugin.PLUGIN_ID, "Could not create template project", e);
			UiStatusHandler.logAndDisplay(status);
			throw new CoreException(status);
		}
	}

}
