/******************************************************************************************
 * Copyright (c) 2009 - 2011 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.ide.metadata.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.springframework.ide.eclipse.beans.core.BeansCorePlugin;
import org.springframework.ide.eclipse.beans.core.model.IBeansConfig;
import org.springframework.ide.eclipse.beans.core.model.IBeansConfigSet;
import org.springframework.ide.eclipse.beans.core.model.IBeansModelElement;
import org.springframework.ide.eclipse.beans.core.model.IBeansProject;
import org.springframework.ide.eclipse.ui.navigator.actions.AbstractNavigatorAction;
import org.springsource.ide.eclipse.commons.core.StatusHandler;

import com.springsource.sts.ide.metadata.MetadataPlugin;
import com.springsource.sts.ide.metadata.MetadataUIImages;
import com.springsource.sts.ide.metadata.ui.RequestMappingBeanMetadataReference;
import com.springsource.sts.ide.metadata.ui.RequestMappingView;

/**
 * @author Leo Dos Santos
 * @author Christian Dupuis
 */
public class ShowRequestMappingsAction extends AbstractNavigatorAction
		implements IActionDelegate {

	private IBeansModelElement element;

	public ShowRequestMappingsAction() {
		this(null);
	}

	public ShowRequestMappingsAction(ICommonActionExtensionSite actionSite) {
		super(actionSite);
		setText(Messages.ShowRequestMappingsAction_TITLE);
		setImageDescriptor(MetadataUIImages.DESC_OBJS_REQUEST_MAPPING);
	}

	@Override
	protected boolean isEnabled(IStructuredSelection selection) {
		Object treeElement = selection.getFirstElement();
		IBeansModelElement modelElement = null;
		if (treeElement instanceof IBeansProject) {
			modelElement = (IBeansProject) treeElement;
		} else if (treeElement instanceof IBeansConfig) {
			modelElement = (IBeansConfig) treeElement;
		} else if (treeElement instanceof IBeansConfigSet) {
			modelElement = (IBeansConfigSet) treeElement;
		} else if (treeElement instanceof IProject) {
			modelElement = BeansCorePlugin.getModel().getProject(
					(IProject) treeElement);
		} else if (treeElement instanceof IFile) {
			modelElement = BeansCorePlugin.getModel().getConfig(
					(IFile) treeElement);
		} else if (treeElement instanceof RequestMappingBeanMetadataReference) {
			modelElement = ((RequestMappingBeanMetadataReference) treeElement)
					.getBeansProject();
		}
		if (modelElement != null) {
			element = modelElement;
			return true;
		}
		return false;
	}

	@Override
	public void run() {
		displayView();
	}

	private void displayView() {
		try {
			RequestMappingView view = (RequestMappingView) PlatformUI
					.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.showView(RequestMappingView.ID_VIEW);
			view.setInput(element);
		} catch (PartInitException e) {
			StatusHandler.log(new Status(IStatus.ERROR,
					MetadataPlugin.PLUGIN_ID,
					Messages.ShowRequestMappingsAction_ERROR_OPENING_VIEW
							+ RequestMappingView.ID_VIEW, e));
		}
	}

	public void run(IAction action) {
		run();
	}

	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			isEnabled((IStructuredSelection) selection);
		}
	}

}
