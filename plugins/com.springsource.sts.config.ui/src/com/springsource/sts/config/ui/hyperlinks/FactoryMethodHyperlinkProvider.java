/******************************************************************************************
 * Copyright (c) 2008 - 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.ui.hyperlinks;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.springframework.ide.eclipse.beans.ui.editor.hyperlink.IHyperlinkCalculator;
import org.springframework.ide.eclipse.beans.ui.editor.hyperlink.bean.FactoryMethodHyperlinkCalculator;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.springsource.sts.config.core.schemas.BeansSchemaConstants;

/**
 * An {@link XmlBackedHyperlinkProvider} that uses
 * {@link FactoryMethodHyperlinkCalculator} as its hyperlink calculator.
 * @author Leo Dos Santos
 * @author Christian Dupuis
 * @since 2.0.0
 */
@SuppressWarnings("restriction")
public class FactoryMethodHyperlinkProvider extends XmlBackedHyperlinkProvider {

	private final String referenceNode;

	/**
	 * Constructs a hyperlink provider for an XML attribute.
	 * 
	 * @param textViewer the text viewer containing the XML source
	 * @param input the XML element to serve as the model for this hyperlink
	 * provider
	 * @param attrName the name of the attribute to compute a hyperlink action
	 * for
	 */
	public FactoryMethodHyperlinkProvider(ITextViewer textViewer, IDOMElement input, String attrName) {
		this(textViewer, input, attrName, BeansSchemaConstants.ATTR_FACTORY_BEAN);
	}

	/**
	 * Constructs a hyperlink provider for an XML attribute.
	 * 
	 * @param textViewer the text viewer containing the XML source
	 * @param input the XML element to serve as the model for this hyperlink
	 * provider
	 * @param attrName the name of the attribute to compute a hyperlink action
	 * for
	 * @param referenceNode the name of the factory bean reference node
	 */
	public FactoryMethodHyperlinkProvider(ITextViewer textViewer, IDOMElement input, String attrName,
			String referenceNode) {
		super(textViewer, input, attrName);
		this.referenceNode = referenceNode;
	}

	@Override
	protected IHyperlinkCalculator createHyperlinkCalculator() {
		return new FactoryMethodHyperlinkCalculator() {
			@Override
			protected Node getFactoryBeanReferenceNode(NamedNodeMap attributes) {
				return attributes.getNamedItem(referenceNode);
			}
		};
	}

}
