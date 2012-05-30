/******************************************************************************************
 * Copyright (c) 2009 - 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.ui.hyperlinks;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.springframework.ide.eclipse.batch.ui.editor.hyperlink.batch.StepReferenceHyperlinkCalculator;
import org.springframework.ide.eclipse.beans.ui.editor.hyperlink.HyperlinkUtils;
import org.springframework.ide.eclipse.beans.ui.editor.hyperlink.IHyperlinkCalculator;
import org.springframework.ide.eclipse.beans.ui.editor.util.BeansEditorUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.springsource.sts.config.core.schemas.BatchSchemaConstants;

/**
 * @author Leo Dos Santos
 * @deprecated Use {@link StepReferenceHyperlinkCalculator} instead.
 */
@Deprecated
public class ExtendedStepReferenceHyperlinkCalculator implements IHyperlinkCalculator {

	public IHyperlink createHyperlink(String name, String target, Node node, Node parentNode, IDocument document,
			ITextViewer textViewer, IRegion hyperlinkRegion, IRegion cursor) {
		if (node.getOwnerDocument() != null && node.getOwnerDocument().getDocumentElement() != null) {
			return searchStepElements(target, node.getOwnerDocument().getDocumentElement(), textViewer, hyperlinkRegion);
		}
		return null;
	}

	private IHyperlink searchStepElements(String name, Node node, ITextViewer textViewer, IRegion hyperlinkRegion) {
		NodeList children = node.getChildNodes();
		IHyperlink link = null;
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (BatchSchemaConstants.ELEM_STEP.equals(child.getLocalName())) {
				if (name.equals(BeansEditorUtils.getAttribute(child, BatchSchemaConstants.ATTR_ID))) {
					IRegion region = HyperlinkUtils.getHyperlinkRegion(child);
					link = new ExtendedNodeElementHyperlink(child, hyperlinkRegion, region, textViewer);
				}
			}
			if (link == null && child.hasChildNodes()) {
				link = searchStepElements(name, child, textViewer, hyperlinkRegion);
			}
		}
		return link;
	}

}
