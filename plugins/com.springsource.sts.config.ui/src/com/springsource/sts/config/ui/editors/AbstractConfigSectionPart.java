/******************************************************************************************
 * Copyright (c) 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.ui.editors;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;

/**
 * @author Leo Dos Santos
 * @since 2.3.4
 */
@SuppressWarnings("restriction")
public abstract class AbstractConfigSectionPart extends SectionPart {

	private final AbstractConfigEditor editor;

	private IDOMElement input;

	public AbstractConfigSectionPart(AbstractConfigEditor editor, IDOMElement input, Composite parent,
			FormToolkit toolkit, int style) {
		super(parent, toolkit, style);
		this.editor = editor;
		this.input = input;
	}

	public AbstractConfigSectionPart(AbstractConfigEditor editor, IDOMElement input, Section section) {
		super(section);
		this.editor = editor;
		this.input = input;
	}

	public abstract void createContent();

	public AbstractConfigEditor getConfigEditor() {
		return editor;
	}

	public IDOMElement getInput() {
		return input;
	}

	@Override
	public boolean setFormInput(Object input) {
		if (input instanceof IDOMElement) {
			this.input = (IDOMElement) input;
			return true;
		}
		return false;
	}

}
