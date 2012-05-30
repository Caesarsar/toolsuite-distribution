/******************************************************************************************
 * Copyright (c) 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.ui.hyperlinks;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.springframework.ide.eclipse.beans.ui.editor.hyperlink.IHyperlinkCalculator;
import org.springframework.ide.eclipse.webflow.ui.editor.hyperlink.webflow.SubflowReferenceHyperlinkCalculator;

/**
 * An {@link XmlBackedHyperlinkProvider} that uses
 * {@link SubflowReferenceHyperlinkCalculator} as its hyperlink calculator.
 * @author Leo Dos Santos
 * @since 2.3.4
 */
@SuppressWarnings("restriction")
public class SubflowReferenceHyperlinkProvider extends XmlBackedHyperlinkProvider {

	/**
	 * Constructs a hyperlink provider for an XML attribute.
	 * 
	 * @param textViewer the text viewer containing the XML source
	 * @param input the XML element to serve as the model for this hyperlink
	 * provider
	 * @param attrName the name of the attribute to compute a hyperlink action
	 * for
	 */
	public SubflowReferenceHyperlinkProvider(ITextViewer textViewer, IDOMElement input, String attrName) {
		super(textViewer, input, attrName);
	}

	@Override
	protected IHyperlinkCalculator createHyperlinkCalculator() {
		return new SubflowReferenceHyperlinkCalculator();
	}

}
