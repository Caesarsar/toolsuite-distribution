/******************************************************************************************
 * Copyright (c) 2008 - 2009 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.ui.editors;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.springframework.ide.eclipse.beans.ui.editor.outline.DelegatingLabelProvider;

/**
 * Label provider for displaying elements from the Spring namespaces.
 * @author Leo Dos Santos
 * @author Christian Dupuis
 * @since 2.0.0
 */
public class SpringConfigLabelProvider extends AbstractConfigLabelProvider {

	private final DelegatingLabelProvider provider = new DelegatingLabelProvider();

	public void addListener(ILabelProviderListener listener) {
		provider.addListener(listener);
	}

	public void dispose() {
		provider.dispose();
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return getImage(element);
	}

	public String getColumnText(Object element, int columnIndex) {
		return getText(element);
	}

	public Image getImage(Object element) {
		return provider.getImage(element);
	}

	public String getText(Object element) {
		return provider.getText(element);
	}

	public boolean isLabelProperty(Object element, String property) {
		return provider.isLabelProperty(element, property);
	}

	public void removeListener(ILabelProviderListener listener) {
		provider.removeListener(listener);
	}

}
