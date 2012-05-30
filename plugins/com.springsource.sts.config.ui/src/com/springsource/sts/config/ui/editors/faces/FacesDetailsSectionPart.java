/******************************************************************************************
 * Copyright (c) 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.ui.editors.faces;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;

import com.springsource.sts.config.core.contentassist.providers.BeanReferenceContentProposalProvider;
import com.springsource.sts.config.core.schemas.FacesSchemaConstants;
import com.springsource.sts.config.ui.editors.AbstractConfigEditor;
import com.springsource.sts.config.ui.editors.SpringConfigDetailsSectionPart;
import com.springsource.sts.config.ui.widgets.TextAttribute;
import com.springsource.sts.config.ui.widgets.TextAttributeProposalAdapter;

/**
 * @author Leo Dos Santos
 */
@SuppressWarnings("restriction")
public class FacesDetailsSectionPart extends SpringConfigDetailsSectionPart {

	public FacesDetailsSectionPart(AbstractConfigEditor editor, IDOMElement input, Composite parent, FormToolkit toolkit) {
		super(editor, input, parent, toolkit);
	}

	@Override
	protected boolean addCustomAttribute(Composite client, String attr, boolean required) {
		// FacesConfigContentAssistProcessor and FacesConfigHyperLinkDetector
		String elem = getInput().getLocalName();
		if (FacesSchemaConstants.ELEM_FLOW_BUILDER_SERVICES.equals(elem)
				&& (FacesSchemaConstants.ATTR_EXPRESSION_PARSER.equals(attr)
						|| FacesSchemaConstants.ATTR_FORMATTER_REGISTRY.equals(attr)
						|| FacesSchemaConstants.ATTR_VIEW_FACTORY_CREATOR.equals(attr) || FacesSchemaConstants.ATTR_CONVERSION_SERVICE
						.equals(attr))) {
			TextAttribute attrControl = createBeanAttribute(client, attr, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new BeanReferenceContentProposalProvider(
					getInput(), attr, true)));
			return true;
		}
		return super.addCustomAttribute(client, attr, required);
	}

}
