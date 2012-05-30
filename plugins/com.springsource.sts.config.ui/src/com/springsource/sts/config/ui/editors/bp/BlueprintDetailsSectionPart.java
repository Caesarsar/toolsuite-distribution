/******************************************************************************************
 * Copyright (c) 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.ui.editors.bp;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;

import com.springsource.sts.config.core.contentassist.providers.BeanReferenceContentProposalProvider;
import com.springsource.sts.config.core.contentassist.providers.ClassContentProposalProvider;
import com.springsource.sts.config.core.contentassist.providers.FactoryMethodContentProposalProvider;
import com.springsource.sts.config.core.schemas.BlueprintSchemaConstants;
import com.springsource.sts.config.ui.editors.AbstractConfigEditor;
import com.springsource.sts.config.ui.editors.SpringConfigDetailsSectionPart;
import com.springsource.sts.config.ui.widgets.TextAttribute;
import com.springsource.sts.config.ui.widgets.TextAttributeProposalAdapter;

/**
 * @author Leo Dos Santos
 */
@SuppressWarnings("restriction")
public class BlueprintDetailsSectionPart extends SpringConfigDetailsSectionPart {

	public BlueprintDetailsSectionPart(AbstractConfigEditor editor, IDOMElement input, Composite parent,
			FormToolkit toolkit) {
		super(editor, input, parent, toolkit);
	}

	@Override
	protected boolean addCustomAttribute(Composite client, String attr, boolean required) {
		// BlueprintContentAssistProcessor and BlueprintHyperlinkDetector
		String elem = getInput().getLocalName();
		if ((BlueprintSchemaConstants.ELEM_BEAN.equals(elem) && BlueprintSchemaConstants.ATTR_FACTORY_REF.equals(attr))
				|| (BlueprintSchemaConstants.ELEM_REFERENCE.equals(elem) && BlueprintSchemaConstants.ATTR_DEPENDS_ON
						.equals(attr))
				|| (BlueprintSchemaConstants.ELEM_SERVICE.equals(elem) && BlueprintSchemaConstants.ATTR_DEPENDS_ON
						.equals(attr))
				|| (BlueprintSchemaConstants.ELEM_REFERENCE_LISTENER.equals(elem) && BlueprintSchemaConstants.ATTR_REF
						.equals(attr))
				|| (BlueprintSchemaConstants.ELEM_REF.equals(elem) && BlueprintSchemaConstants.ATTR_COMPONENT_ID
						.equals(attr))
				|| (BlueprintSchemaConstants.ELEM_IDREF.equals(elem) && BlueprintSchemaConstants.ATTR_COMPONENT_ID
						.equals(attr))
				|| (BlueprintSchemaConstants.ELEM_ARGUMENT.equals(elem) && BlueprintSchemaConstants.ATTR_REF
						.equals(attr))) {
			TextAttribute attrControl = createBeanAttribute(client, attr, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new BeanReferenceContentProposalProvider(
					getInput(), attr)));
			return true;
		}
		if (BlueprintSchemaConstants.ATTR_INTERFACE.equals(attr)) {
			TextAttribute attrControl = createClassAttribute(client, attr, true, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new ClassContentProposalProvider(getInput(), attr,
					true)));
			return true;
		}
		if (BlueprintSchemaConstants.ELEM_ARGUMENT.equals(elem) && BlueprintSchemaConstants.ATTR_TYPE.equals(attr)) {
			TextAttribute attrControl = createClassAttribute(client, attr, true, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new ClassContentProposalProvider(getInput(), attr,
					false)));
			return true;
		}
		if (BlueprintSchemaConstants.ELEM_BEAN.equals(elem)
				&& BlueprintSchemaConstants.ATTR_FACTORY_METHOD.equals(attr)) {
			TextAttribute attrControl = createFactoryMethodAttribute(client, attr,
					BlueprintSchemaConstants.ATTR_FACTORY_REF, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new FactoryMethodContentProposalProvider(
					getInput(), attr, BlueprintSchemaConstants.ATTR_FACTORY_REF)));
			return true;
		}
		// Inherit from BeansContentAssistProcessor and BeansHyperlinkDetector
		return super.addCustomAttribute(client, attr, required);
	}

}
