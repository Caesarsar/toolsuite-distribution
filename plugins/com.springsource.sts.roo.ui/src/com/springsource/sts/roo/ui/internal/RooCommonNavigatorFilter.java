/******************************************************************************************
 * Copyright (c) 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.roo.ui.internal;

import java.util.regex.Pattern;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * {@link ViewerFilter} responsible for filtering Roo ITDs from the project source tree.
 * @author Christian Dupuis
 * @since 2.5.0.
 */
public class RooCommonNavigatorFilter extends ViewerFilter {

	private static final Pattern ROO_AJ_PATTERN = Pattern.compile(".*_Roo_.*.aj");

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof ICompilationUnit) {
			try {
				ICompilationUnit cu = (ICompilationUnit) element;
				if (cu.getUnderlyingResource() != null) {
					return !ROO_AJ_PATTERN.matcher(cu.getUnderlyingResource().getName()).matches();
				}
			}
			catch (JavaModelException e) {
				// just ignore this here
			}
		}
		return true;
	}

}
