/******************************************************************************************
 * Copyright (c) 2009 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.ui.editors;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.springframework.ide.eclipse.core.internal.model.validation.ValidatorDefinition;
import org.springframework.ide.eclipse.core.project.IProjectContributorState;
import org.springframework.ide.eclipse.core.project.ProjectBuilderDefinition;
import org.springframework.ide.eclipse.core.project.ProjectContributionEventListenerAdapter;

/**
 * @author Leo Dos Santos
 */
public class BeansModelLoadListener extends ProjectContributionEventListenerAdapter {

	@Override
	public void finish(int kind, IResourceDelta delta, List<ProjectBuilderDefinition> builderDefinitions,
			List<ValidatorDefinition> validatorDefinitions, IProjectContributorState state, IProject project) {
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
		for (IWorkbenchWindow window : windows) {
			IWorkbenchPage[] pages = window.getPages();
			for (IWorkbenchPage page : pages) {
				IEditorReference[] refs = page.getEditorReferences();
				for (IEditorReference reference : refs) {
					IWorkbenchPart part = reference.getPart(false);
					if (part instanceof SpringConfigEditor) {
						SpringConfigEditor cEditor = (SpringConfigEditor) part;
						cEditor.createDeferredBeanGraphPage();
					}
				}
			}
		}
	}

}
