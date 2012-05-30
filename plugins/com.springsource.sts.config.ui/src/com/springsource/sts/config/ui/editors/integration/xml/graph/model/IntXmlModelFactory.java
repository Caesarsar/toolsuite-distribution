/******************************************************************************************
 * Copyright (c) 2010 - 2011 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.ui.editors.integration.xml.graph.model;

import java.util.List;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;

import com.springsource.sts.config.core.schemas.IntXmlSchemaConstants;
import com.springsource.sts.config.flow.model.Activity;
import com.springsource.sts.config.ui.editors.integration.graph.model.AbstractIntegrationModelFactory;

/**
 * @author Leo Dos Santos
 */
@SuppressWarnings("restriction")
public class IntXmlModelFactory extends AbstractIntegrationModelFactory {

	public void getChildrenFromXml(List<Activity> list, IDOMElement input, Activity parent) {
		if (input.getLocalName().equals(IntXmlSchemaConstants.ELEM_MARSHALLING_TRANSFORMER)) {
			MarshallingTransformerModelElement transformer = new MarshallingTransformerModelElement(input,
					parent.getDiagram());
			list.add(transformer);
		}
		else if (input.getLocalName().equals(IntXmlSchemaConstants.ELEM_UNMARSHALLING_TRANSFORMER)) {
			UnmarshallingTransformerModelElement transformer = new UnmarshallingTransformerModelElement(input,
					parent.getDiagram());
			list.add(transformer);
		}
		else if (input.getLocalName().equals(IntXmlSchemaConstants.ELEM_VALIDATING_FILTER)) {
			ValidatingFilterModelElement router = new ValidatingFilterModelElement(input, parent.getDiagram());
			list.add(router);
		}
		else if (input.getLocalName().equals(IntXmlSchemaConstants.ELEM_XPATH_FILTER)) {
			XpathFilterModelElement filter = new XpathFilterModelElement(input, parent.getDiagram());
			list.add(filter);
		}
		else if (input.getLocalName().equals(IntXmlSchemaConstants.ELEM_XPATH_HEADER_ENRICHER)) {
			XpathHeaderEnricherModelElement enricher = new XpathHeaderEnricherModelElement(input, parent.getDiagram());
			list.add(enricher);
		}
		else if (input.getLocalName().equals(IntXmlSchemaConstants.ELEM_XPATH_ROUTER)) {
			XpathRouterModelElement router = new XpathRouterModelElement(input, parent.getDiagram());
			list.add(router);
		}
		else if (input.getLocalName().equals(IntXmlSchemaConstants.ELEM_XPATH_SPLITTER)) {
			XpathSplitterModelElement splitter = new XpathSplitterModelElement(input, parent.getDiagram());
			list.add(splitter);
		}
		else if (input.getLocalName().equals(IntXmlSchemaConstants.ELEM_XPATH_TRANSFORMER)) {
			XpathTransformerModelElement transformer = new XpathTransformerModelElement(input, parent.getDiagram());
			list.add(transformer);
		}
		else if (input.getLocalName().equals(IntXmlSchemaConstants.ELEM_XSLT_TRANSFORMER)) {
			XslTransformerModelElement transformer = new XslTransformerModelElement(input, parent.getDiagram());
			list.add(transformer);
		}
	}

}
