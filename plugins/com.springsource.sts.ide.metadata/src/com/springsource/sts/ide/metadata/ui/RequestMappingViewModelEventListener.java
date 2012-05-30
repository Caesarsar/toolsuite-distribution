/******************************************************************************************
 * Copyright (c) 2009 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.ide.metadata.ui;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.springframework.ide.eclipse.core.internal.model.validation.ValidatorDefinition;
import org.springframework.ide.eclipse.core.project.IProjectContributorState;
import org.springframework.ide.eclipse.core.project.ProjectBuilderDefinition;
import org.springframework.ide.eclipse.core.project.ProjectContributionEventListenerAdapter;

/**
 * @author Leo Dos Santos
 */
public class RequestMappingViewModelEventListener extends
		ProjectContributionEventListenerAdapter {

	@Override
	public void finish(int kind, IResourceDelta delta,
			List<ProjectBuilderDefinition> builderDefinitions,
			List<ValidatorDefinition> validatorDefinitions,
			IProjectContributorState state, IProject project) {
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench()
				.getWorkbenchWindows();
		for (IWorkbenchWindow window : windows) {
			IWorkbenchPage[] pages = window.getPages();
			for (IWorkbenchPage page : pages) {
				IViewReference viewRef = page
						.findViewReference(RequestMappingView.ID_VIEW);
				if (viewRef != null
						&& viewRef.getView(false) instanceof RequestMappingView) {
					((RequestMappingView) viewRef.getView(false)).finish(kind,
							delta, builderDefinitions, validatorDefinitions,
							state, project);
				}
			}

		}
	}

}
