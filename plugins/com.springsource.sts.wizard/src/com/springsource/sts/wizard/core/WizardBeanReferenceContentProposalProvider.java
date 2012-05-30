/******************************************************************************************
 * Copyright (c) 2008 - 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.wizard.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.springframework.ide.eclipse.beans.ui.editor.contentassist.IContentAssistContext;
import org.w3c.dom.Document;

import com.springsource.sts.config.core.contentassist.providers.BeanReferenceContentProposalProvider;

/**
 * Wrapper class for BeanReferenceContentProposalProvider to work with bean
 * wizard.
 * @author Terry Denney
 * @author Leo Dos Santos
 * @author Christian Dupuis
 * @since 2.0
 */
@SuppressWarnings("restriction")
public class WizardBeanReferenceContentProposalProvider extends BeanReferenceContentProposalProvider {

	private IFile file;

	private Document document;

	public WizardBeanReferenceContentProposalProvider(IDOMElement node, String attrName, IFile file, Document document) {
		super(node, attrName);
		this.file = file;
		this.document = document;
	}

	public WizardBeanReferenceContentProposalProvider(IDOMElement node, String attrName, IFile file, Document document,
			boolean showExternal) {
		super(node, attrName, showExternal);
		this.file = file;
		this.document = document;
	}

	@Override
	protected IContentAssistContext createContentAssistContext(String contents) {
		return new WizardContentAssistContext(getInput(), getAttributeName(), file, contents, document);
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public void setFile(IFile file) {
		this.file = file;
	}
}
