/******************************************************************************************
 * Copyright (c) 2008 - 2012 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.wizard.template;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;
import org.springsource.ide.eclipse.commons.content.core.ContentItem;
import org.springsource.ide.eclipse.commons.content.core.ContentManager;
import org.springsource.ide.eclipse.commons.content.core.ContentPlugin;
import org.springsource.ide.eclipse.commons.content.core.util.ContentUtil;
import org.springsource.ide.eclipse.commons.content.core.util.Descriptor;
import org.springsource.ide.eclipse.commons.core.StatusHandler;
import org.springsource.ide.eclipse.commons.ui.StsUiImages;
import org.springsource.ide.eclipse.commons.ui.UiStatusHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.springsource.sts.wizard.WizardImages;
import com.springsource.sts.wizard.WizardPlugin;
import com.springsource.sts.wizard.template.infrastructure.ITemplateElement;
import com.springsource.sts.wizard.template.infrastructure.ITemplateProjectData;
import com.springsource.sts.wizard.template.infrastructure.RuntimeTemplateProjectData;
import com.springsource.sts.wizard.template.infrastructure.Template;
import com.springsource.sts.wizard.template.infrastructure.TemplateCategory;
import com.springsource.sts.wizard.template.infrastructure.ui.WizardUIInfo;
import com.springsource.sts.wizard.template.infrastructure.ui.WizardUIInfoLoader;
import com.thoughtworks.xstream.XStreamException;

/**
 * Wizard page in template wizard for selecting which template to create project
 * from
 * @author Terry Denney
 * @author Leo Dos Santos
 * @author Christian Dupuis
 * @author Kaitlin Duck Sherwood
 */
public class TemplateSelectionWizardPage extends WizardPage {

	private Template selectedTemplate;

	private NewTemplateWizardPage firstPage;

	private final List<Template> templates;

	private final TemplateWizard wizard;

	private String[] topLevelPackageTokens;

	private Label descriptionLabel;

	private Label descriptionText;

	private String projectNameToken;

	private TreeViewer treeViewer;

	private Label legendImage;

	private Label legendText;

	private PropertyChangeListener contentManagerListener;

	private Button refreshButton;

	protected TemplateSelectionWizardPage(TemplateWizard wizard) {
		super("Template Selection Wizard Page"); //$NON-NLS-1$

		setTitle(Messages.getString("TemplateSelectionWizardPage.PAGE_TITLE")); //$NON-NLS-1$
		setDescription(Messages.getString("TemplateSelectionWizardPage.PAGE_DESCRIPTION")); //$NON-NLS-1$

		templates = new ArrayList<Template>();
		this.wizard = wizard;

		initializeTemplates();
	}

	@Override
	public boolean canFlipToNextPage() {
		return selectedTemplate != null;
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label label = new Label(container, SWT.NONE);
		label.setText("Templates:"); //$NON-NLS-1$
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		String[] names = new String[templates.size()];
		for (int i = 0; i < templates.size(); i++) {
			names[i] = templates.get(i).getName();
		}

		Tree tree = new Tree(container, SWT.FULL_SELECTION | SWT.BORDER);
		tree.setLinesVisible(false);
		tree.setHeaderVisible(false);
		tree.setEnabled(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		treeViewer = new TreeViewer(tree);

		treeViewer.setLabelProvider(new ILabelProvider() {

			public void removeListener(ILabelProviderListener listener) {
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void dispose() {
			}

			public void addListener(ILabelProviderListener listener) {
			}

			public String getText(Object element) {
				if (element instanceof ITemplateElement) {
					return ((ITemplateElement) element).getName();
				}
				return null;
			}

			public Image getImage(Object element) {
				if (element instanceof Template) {
					Template template = (Template) element;
					Image templateImage = WizardImages.getImage(WizardImages.TEMPLATE_ICON);
					if (template.getItem().isLocal() && !template.getItem().isNewerVersionAvailable()) {
						return templateImage;
					}
					return WizardImages.getImage(new DecorationOverlayIcon(templateImage, new ImageDescriptor[] {
							StsUiImages.DOWNLOAD_OVERLAY, null, null, null, null }));
				}

				if (element instanceof TemplateCategory) {
					return WizardImages.getImage(WizardImages.TEMPLATE_CATEGORY_ICON);
				}
				return null;
			}
		});

		treeViewer.setSorter(new ViewerSorter() {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				if (e1 instanceof TemplateCategory && e2 instanceof Template) {
					return -1;
				}
				if (e1 instanceof Template && e2 instanceof TemplateCategory) {
					return 1;
				}

				if (e1 instanceof ITemplateElement && e2 instanceof ITemplateElement) {
					ITemplateElement t1 = (ITemplateElement) e1;
					ITemplateElement t2 = (ITemplateElement) e2;
					return t1.getName().compareTo(t2.getName());
				}
				return super.compare(viewer, e1, e2);
			}
		});

		treeViewer.setContentProvider(new TemplateContentProvider());

		treeViewer.setInput(templates);

		Composite legendContainer = new Composite(container, SWT.NONE);
		legendContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout headerLayout = new GridLayout(2, false);
		headerLayout.marginWidth = 0;
		headerLayout.marginHeight = 0;
		legendContainer.setLayout(headerLayout);

		Composite legendComposite = new Composite(legendContainer, SWT.NONE);
		GridLayout legendLayout = new GridLayout(2, false);
		legendLayout.verticalSpacing = 0;
		legendLayout.marginHeight = 0;
		legendLayout.marginBottom = 5;

		legendComposite.setLayout(legendLayout);
		legendComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		legendImage = new Label(legendComposite, SWT.NONE);
		legendImage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		legendImage.setImage(WizardImages.getImage(StsUiImages.DOWNLOAD_OVERLAY));

		legendText = new Label(legendComposite, SWT.NONE);
		legendText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		legendText.setText("requires downloading");

		refreshButton = new Button(legendContainer, SWT.PUSH);
		refreshButton.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false));
		refreshButton.setText("Refresh");

		// refreshButton.setImage(WizardImages.getImage(WizardImages.REFRESH_ICON));

		refreshButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final ContentManager manager = ContentPlugin.getDefault().getManager();
				try {

					getWizard().getContainer().run(true, true, new IRunnableWithProgress() {

						public void run(IProgressMonitor monitor) throws InvocationTargetException,
								InterruptedException {
							try {
								if (isRefreshing()) {
									while (isRefreshing()) {
										Thread.sleep(300);
										if (monitor.isCanceled()) {
											break;
										}
									}
								}
								else {
									manager.refresh(monitor);
								}
							}
							catch (Exception e) {
								System.err.println("manager.refresh threw an exception");
								// ignore, just terminate the thread
							}

						}
					});
				}
				catch (InvocationTargetException e1) {
					System.err.println("InvocationTargetException " + e1);
					// ignore, just terminate the thread
				}
				catch (InterruptedException e1) {
					System.err.println("InterruptedException " + e1);
					// ignore, just terminate the thread
				}
			}

		});

		Composite descriptionComposite = new Composite(container, SWT.NONE);
		descriptionComposite.setLayout(new GridLayout());
		descriptionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		descriptionLabel = new Label(descriptionComposite, SWT.NONE);
		descriptionLabel.setText("Description:"); //$NON-NLS-1$
		descriptionLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		descriptionLabel.setVisible(false);

		descriptionText = new Label(descriptionComposite, SWT.WRAP);

		GridData descriptionData = new GridData(SWT.FILL, SWT.FILL, false, false);
		descriptionData.widthHint = 200;
		descriptionData.heightHint = 40;
		descriptionText.setLayoutData(descriptionData);

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = treeViewer.getSelection();
				selectedTemplate = null;
				if (selection instanceof IStructuredSelection) {
					Object element = ((IStructuredSelection) selection).getFirstElement();
					if (element instanceof Template) {
						selectedTemplate = ((Template) element);
					}
				}
				firstPage = null;

				setDescription(selectedTemplate);
				if (TemplateSelectionWizardPage.this.equals(wizard.getContainer().getCurrentPage())) {
					wizard.getContainer().updateButtons();
				}
			}
		});

		this.contentManagerListener = new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent arg0) {

				initializeTemplates();
				// switch to UI thread
				PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
					public void run() {
						try {
							refreshPage();
						}
						finally {
							// ignore
						}
					}

				});
			}
		};

		ContentPlugin.getDefault().getManager().addListener(contentManagerListener);

		refreshPage();
		setControl(container);

	}

	@Override
	public IWizardPage getNextPage() {
		if (firstPage != null) {
			return firstPage;
		}

		if (selectedTemplate == null) {
			return null;
		}

		try {
			getContainer().run(true, true, new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						monitor.beginTask("Download template " + selectedTemplate.getName(), 100);
						ITemplateProjectData data;
						if (selectedTemplate.getItem().isRuntimeDefined()) {
							data = new RuntimeTemplateProjectData(selectedTemplate.getItem().getRuntimeProject());
						}
						else {
							data = TemplateUtils.importTemplate(selectedTemplate, getContainer().getShell(),
									new SubProgressMonitor(monitor, 1));
						}
						selectedTemplate.setTemplateData(data);
						monitor.done();
					}
					catch (CoreException e) {
						throw new InvocationTargetException(e);
					}
					catch (OperationCanceledException e) {
						throw new InterruptedException();
					}
					finally {
						monitor.done();
					}
				}
			});
		}
		catch (InterruptedException e) {
			throw new OperationCanceledException();
		}
		catch (InvocationTargetException e) {
			UiStatusHandler.logAndDisplay(new Status(IStatus.ERROR, WizardPlugin.PLUGIN_ID, e.getTargetException()
					.getLocalizedMessage(), e));
			return null;
		}

		if (selectedTemplate.getTemplateData() == null) {
			return null;
		}

		URL jsonWizardUIDescriptor;
		try {
			jsonWizardUIDescriptor = selectedTemplate.getTemplateLocation();
		}
		catch (CoreException e) {
			StatusHandler.log(e.getStatus());
			return null;
		}

		WizardUIInfo info;
		try {
			WizardUIInfoLoader infoLoader = new WizardUIInfoLoader();
			InputStream jsonDescriptionInputStream = jsonWizardUIDescriptor.openStream();
			info = infoLoader.load(jsonDescriptionInputStream);
		}
		catch (IOException ex) {
			UiStatusHandler.logAndDisplay(new Status(Status.ERROR, WizardPlugin.PLUGIN_ID,
					"Failed to load json descriptor for wizard page"));
			return null;
		}
		catch (XStreamException ex) {
			UiStatusHandler.logAndDisplay(new Status(Status.ERROR, WizardPlugin.PLUGIN_ID,
					"Failed to load json descriptor for wizard page"));
			return null;
		}

		topLevelPackageTokens = info.getTopLevelPackageTokens();

		projectNameToken = info.getProjectNameToken();

		firstPage = null;
		ITemplateWizardPage previousPage = null;
		ITemplateWizardPage page = null;

		try {
			for (int i = 0; i < info.getPageCount(); i++) {
				if (firstPage == null) {
					firstPage = new NewTemplateWizardPage(info.getPage(i).getDescription(), info.getElementsForPage(i),
							selectedTemplate.getName(), wizard, selectedTemplate.getIcon());
					page = firstPage;
				}
				else {
					page = new TemplateWizardPage(info.getPage(i).getDescription(), info.getElementsForPage(i),
							selectedTemplate.getName(), wizard, selectedTemplate.getIcon());
				}
				page.setWizard(getWizard());
				if (previousPage != null) {
					previousPage.setNextPage(page);
				}

				previousPage = page;
			}
		}
		catch (Exception e) {
			UiStatusHandler.logAndDisplay(new Status(Status.ERROR, WizardPlugin.PLUGIN_ID,
					"Failed to read json descriptor for wizard page"));
			return null;
		}

		wizard.setFirstPage(firstPage);
		return firstPage;
	}

	public URL getProjectLocation() throws CoreException {
		if (selectedTemplate == null) {
			throw new CoreException(new Status(Status.ERROR, WizardPlugin.PLUGIN_ID,
					"Unable to find template project location"));
		}
		return selectedTemplate.getZippedLocation();
	}

	public String getProjectNameToken() {
		return projectNameToken;
	}

	public String[] getTopLevelPackage() {
		return topLevelPackageTokens;
	}

	private void initializeTemplates() {
		// FIXME keep old templates; only replace if there wasn't an error
		templates.clear();

		Collection<ContentItem> items = ContentPlugin.getDefault().getManager()
				.getItemsByKind(ContentManager.KIND_TEMPLATE);

		List<ContentItem> sortedItems = new ArrayList<ContentItem>();
		sortedItems.addAll(items);
		Collections.sort(sortedItems, new Comparator<ContentItem>() {
			public int compare(ContentItem o1, ContentItem o2) {
				Descriptor descriptor1 = o1.getRemoteDescriptor();
				if (descriptor1 == null) {
					descriptor1 = o1.getLocalDescriptor();
				}

				Descriptor descriptor2 = o2.getRemoteDescriptor();
				if (descriptor2 == null) {
					descriptor2 = o2.getLocalDescriptor();
				}
				return descriptor1.getVersion().compareTo(descriptor2.getVersion()) * -1;
			}
		});

		Set<String> templateIds = new HashSet<String>();
		for (ContentItem item : sortedItems) {
			String templateId = item.getId();
			if (!templateIds.contains(templateId)) {
				templates.add(new Template(item, null));
				templateIds.add(templateId);
			}
		}

		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject project : projects) {
			IFile templateFile = project.getFile("template.xml");
			if (templateFile.exists()) {
				File file = templateFile.getLocation().toFile();
				try {
					DocumentBuilder documentBuilder = ContentUtil.createDocumentBuilder();
					Document document = documentBuilder.parse(file);
					Element rootNode = document.getDocumentElement();
					if (rootNode != null) {
						NodeList children = rootNode.getChildNodes();
						for (int i = 0; i < children.getLength(); i++) {
							Node childNode = children.item(i);
							if (childNode.getNodeType() == Node.ELEMENT_NODE) {
								if ("descriptor".equals(childNode.getNodeName())) {
									Descriptor descriptor = Descriptor.read(childNode);
									ContentItem item = new ContentItem(descriptor.getId(), project);
									item.setLocalDescriptor(descriptor);
									descriptor.setUrl(project.getName());
									ImageDescriptor icon = null;
									Template template = new Template(item, icon);
									templates.add(template);
								}
							}
						}
					}
				}
				catch (CoreException e) {
					// FIXME better error handling
					System.err.println("Error getting and parsing descriptors file " + e + ", ignoring");
					e.printStackTrace();
				}
				catch (SAXException e) {
					// FIXME better error handling
					System.err.println("Error parsing tmp descriptors file at " + file + ", ignoring");
					e.printStackTrace();
				}
				catch (IOException e) {
					// FIXME better error handling
					System.err.println("IO error on file at " + file + ", ignoring");
					e.printStackTrace();
				}

				// create template and add to templates
			}
		}
		Collections.sort(templates, new Comparator<Template>() {
			public int compare(Template t1, Template t2) {
				return t1.getName().compareTo(t2.getName());
			}
		});
	}

	@Override
	public boolean isPageComplete() {
		return false;
	}

	private void refreshPage() {
		treeViewer.refresh(true);
		treeViewer.setSelection(new StructuredSelection());

		boolean needsDownload = false;
		for (Template template : templates) {
			if (!template.getItem().isLocal()) {
				needsDownload = true;
				break;
			}
		}

		legendImage.setVisible(needsDownload);
		legendText.setVisible(needsDownload);
		descriptionText.setText(""); //$NON-NLS-1$
	}

	private void setDescription(Template template) {
		String description = null;

		if (template != null) {
			description = template.getDescription();
		}

		if (description != null) {
			descriptionText.setText(description);
			descriptionLabel.setVisible(true);
		}
		else {
			descriptionText.setText(""); //$NON-NLS-1$
			descriptionLabel.setVisible(false);
		}
		descriptionText.redraw();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		ContentManager manager = ContentPlugin.getDefault().getManager();
		if (visible && manager.getItemsByKind(ContentManager.KIND_TEMPLATE).size() == 0 && !isRefreshing()) {
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					try {
						getContainer().run(true, true, new IRunnableWithProgress() {

							public void run(IProgressMonitor monitor) throws InvocationTargetException,
									InterruptedException {
								ContentPlugin.getDefault().getManager().refresh(monitor);
							}
						});
					}
					catch (InvocationTargetException e) {
						// ignore

					}
					catch (InterruptedException e) {
						// ignore

					}

				}
			});
		}
	}

	@Override
	public void dispose() {

		if (contentManagerListener != null) {
			ContentPlugin.getDefault().getManager().removeListener(contentManagerListener);
		}
		else {
			// This should never happen.
			System.err.println("ERROR: contentManagerListener was null!");
		}
		super.dispose();
	}

	private boolean isRefreshing() {
		return ContentPlugin.getDefault().getManager().isRefreshing();
	}
}
