/******************************************************************************************
 * Copyright (c) 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.ui.navigator;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.part.FileEditorInput;
import org.springframework.ide.eclipse.beans.core.BeansCorePlugin;
import org.springframework.ide.eclipse.beans.core.internal.model.BeansModelUtils;
import org.springframework.ide.eclipse.beans.core.model.IBean;
import org.springframework.ide.eclipse.beans.core.model.IBeansComponent;
import org.springframework.ide.eclipse.beans.core.model.IBeansConfig;
import org.springframework.ide.eclipse.beans.core.model.IBeansConfigSet;
import org.springframework.ide.eclipse.beans.ui.BeansUIUtils;
import org.springframework.ide.eclipse.beans.ui.graph.BeansGraphImages;
import org.springframework.ide.eclipse.beans.ui.graph.editor.GraphEditor;
import org.springframework.ide.eclipse.beans.ui.graph.editor.GraphEditorInput;
import org.springframework.ide.eclipse.core.io.ZipEntryStorage;
import org.springframework.ide.eclipse.core.model.IModelElement;
import org.springframework.ide.eclipse.core.model.IResourceModelElement;
import org.springframework.ide.eclipse.ui.SpringUIUtils;
import org.springframework.ide.eclipse.ui.navigator.actions.AbstractNavigatorAction;

import com.springsource.sts.config.ui.editors.SpringConfigEditor;
import com.springsource.sts.config.ui.editors.SpringConfigGraphPage;

/**
 * @author Leo Dos Santos
 * @since 2.5.2
 */
public class StsShowBeansGraphAction extends AbstractNavigatorAction {

	private IResourceModelElement element;

	private IModelElement context;

	public StsShowBeansGraphAction(ICommonActionExtensionSite site) {
		super(site);
		setText("Open Dependency &Graph"); // TODO externalize text
		setToolTipText("Open the Bean Dependency Graph");
		setImageDescriptor(BeansGraphImages.DESC_OBJS_BEANS_GPRAH);
	}

	@Override
	public boolean isEnabled(IStructuredSelection selection) {
		if (selection instanceof ITreeSelection) {
			ITreeSelection tSelection = (ITreeSelection) selection;
			if (tSelection.size() == 1) {
				Object tElement = tSelection.getFirstElement();
				IResourceModelElement rElement = null;
				if (tElement instanceof IResourceModelElement) {
					if (tElement instanceof IBeansConfig || tElement instanceof IBeansConfigSet
							|| tElement instanceof IBeansComponent || tElement instanceof IBean) {
						rElement = (IResourceModelElement) tElement;
					}
				}
				else if (tElement instanceof IFile) {
					rElement = BeansCorePlugin.getModel().getConfig((IFile) tElement);
				}
				else if (tElement instanceof ZipEntryStorage) {
					rElement = BeansModelUtils.getConfig((ZipEntryStorage) tElement);
				}
				if (rElement != null) {
					element = rElement;
					context = BeansUIUtils.getContext(tSelection);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void run() {
		IEditorInput input;
		if (element.getElementResource() instanceof IFile) {
			IFile file = (IFile) element.getElementResource();
			input = new FileEditorInput(file);
			IEditorPart part = SpringUIUtils.openInEditor(input, SpringConfigEditor.ID_EDITOR);
			if (part instanceof SpringConfigEditor) {
				SpringConfigEditor cEditor = (SpringConfigEditor) part;
				SpringConfigGraphPage graph = cEditor.getBeansGraphPage();
				if (graph != null) {
					cEditor.setActiveEditor(graph);
				}
			}
		}
		else {
			if (element instanceof IBeansConfig || element instanceof IBeansConfigSet) {
				input = new GraphEditorInput(element.getElementID());
			}
			else {
				input = new GraphEditorInput(element.getElementID(), context.getElementID());
			}
			SpringUIUtils.openInEditor(input, GraphEditor.EDITOR_ID);
		}
	}

}
