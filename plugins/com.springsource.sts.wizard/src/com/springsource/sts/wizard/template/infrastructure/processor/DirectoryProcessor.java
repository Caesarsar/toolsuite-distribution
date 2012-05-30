/******************************************************************************************
 * Copyright (c) 2008 - 2009 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.wizard.template.infrastructure.processor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Terry Denney
 * @author Leo Dos Santos
 * @author Christian Dupuis
 */
public class DirectoryProcessor {

	private final ProcessingInfo processingInfo;

	private final TemplateProcessor templateProcessor;

	private final TemplateProcessor fileNameProcessor;

	private final IProject project;

	// private final IProjectDescription projectDescription;

	private final IPath projectPath;

	public DirectoryProcessor(ProcessingInfo processingInfo, IProject project, IPath projectPath) {
		this.project = project;
		this.projectPath = projectPath;
		// this.projectDescription = projectDescription;
		templateProcessor = new TemplateProcessor(processingInfo.getTemplateReplacementContext());
		fileNameProcessor = new TemplateProcessor(processingInfo.getResourceReplacementContext());

		this.processingInfo = processingInfo;
	}

	public IProject process(URL zipPath, Shell shell, IProgressMonitor monitor) throws IOException, URISyntaxException,
			CoreException {
		TemplateProjectCreator projectCreator = new TemplateProjectCreator(project, projectPath, zipPath, shell,
				templateProcessor, fileNameProcessor, processingInfo);
		return projectCreator.createProject(monitor);
	}
}
