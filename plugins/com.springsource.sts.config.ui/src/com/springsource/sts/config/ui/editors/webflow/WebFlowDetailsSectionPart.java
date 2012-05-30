/******************************************************************************************
 * Copyright (c) 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.ui.editors.webflow;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;

import com.springsource.sts.config.core.contentassist.providers.BeanActionMethodContentProposalProvider;
import com.springsource.sts.config.core.contentassist.providers.BeanMethodContentProposalProvider;
import com.springsource.sts.config.core.contentassist.providers.ClassContentProposalProvider;
import com.springsource.sts.config.core.contentassist.providers.ClassHierarchyContentProposalProvider;
import com.springsource.sts.config.core.contentassist.providers.StateReferenceContentProposalProvider;
import com.springsource.sts.config.core.contentassist.providers.SubflowReferenceContentProposalProvider;
import com.springsource.sts.config.core.contentassist.providers.WebFlowBeanReferenceContentProposalProvider;
import com.springsource.sts.config.core.schemas.WebFlowSchemaConstants;
import com.springsource.sts.config.ui.editors.AbstractConfigEditor;
import com.springsource.sts.config.ui.editors.SpringConfigDetailsSectionPart;
import com.springsource.sts.config.ui.widgets.TextAttribute;
import com.springsource.sts.config.ui.widgets.TextAttributeProposalAdapter;

/**
 * @author Leo Dos Santos
 */
@SuppressWarnings("restriction")
public class WebFlowDetailsSectionPart extends SpringConfigDetailsSectionPart {

	public WebFlowDetailsSectionPart(AbstractConfigEditor editor, IDOMElement input, Composite parent,
			FormToolkit toolkit) {
		super(editor, input, parent, toolkit);
	}

	@Override
	protected boolean addCustomAttribute(Composite client, String attr, boolean required) {
		// WebflowContentAssistProcessor and WebflowHyperlinkDetector
		String elem = getInput().getLocalName();
		if ((WebFlowSchemaConstants.ELEM_ARGUMENT.equals(elem) && WebFlowSchemaConstants.ATTR_PARAMETER_TYPE
				.equals(attr))
				|| (WebFlowSchemaConstants.ELEM_MAPPING.equals(elem) && (WebFlowSchemaConstants.ATTR_TO.equals(attr) || WebFlowSchemaConstants.ATTR_FROM
						.equals(attr)))
				|| (WebFlowSchemaConstants.ELEM_EVALUATE.equals(elem) && WebFlowSchemaConstants.ATTR_RESULT_TYPE
						.equals(attr))
				|| WebFlowSchemaConstants.ATTR_CLASS.equals(attr)
				|| WebFlowSchemaConstants.ATTR_TYPE.equals(attr)) {
			TextAttribute attrControl = createClassAttribute(client, attr, false, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new ClassContentProposalProvider(getInput(), attr,
					false)));
			return true;
		}
		if ((WebFlowSchemaConstants.ELEM_VAR.equals(elem) && WebFlowSchemaConstants.ATTR_NAME.equals(attr))
				|| (WebFlowSchemaConstants.ELEM_SUBFLOW_STATE.equals(elem) && WebFlowSchemaConstants.ATTR_SUBFLOW_ATTRIBUTE_MAPPER
						.equals(attr)) || WebFlowSchemaConstants.ATTR_BEAN.equals(attr)) {
			TextAttribute attrControl = createWebFlowBeanAttribute(client, attr, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new WebFlowBeanReferenceContentProposalProvider(
					getInput(), attr)));
			return true;
		}
		if ((WebFlowSchemaConstants.ELEM_TRANSITION.equals(elem) && WebFlowSchemaConstants.ATTR_TO.equals(attr))
				|| (WebFlowSchemaConstants.ELEM_START_STATE.equals(elem) && WebFlowSchemaConstants.ATTR_IDREF
						.equals(attr))
				|| (WebFlowSchemaConstants.ELEM_IF.equals(elem) && (WebFlowSchemaConstants.ATTR_THEN.equals(attr) || WebFlowSchemaConstants.ATTR_ELSE
						.equals(attr)))) {
			TextAttribute attrControl = createStateAttribute(client, attr, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new StateReferenceContentProposalProvider(
					getInput(), attr)));
			return true;
		}
		if (WebFlowSchemaConstants.ELEM_TRANSITION.equals(elem)
				&& WebFlowSchemaConstants.ATTR_ON_EXCEPTION.equals(attr)) {
			TextAttribute attrControl = createClassAttribute(client, attr, false, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new ClassHierarchyContentProposalProvider(
					getInput(), attr, Throwable.class.getName())));
			return true;
		}
		if ((WebFlowSchemaConstants.ELEM_SUBFLOW_STATE.equals(elem) && (WebFlowSchemaConstants.ATTR_FLOW.equals(attr) || WebFlowSchemaConstants.ATTR_SUBFLOW
				.equals(attr)))
				|| (WebFlowSchemaConstants.ELEM_FLOW.equals(elem) && WebFlowSchemaConstants.ATTR_PARENT.equals(attr))) {
			TextAttribute attrControl = createSubflowAttribute(client, attr, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new SubflowReferenceContentProposalProvider(
					getInput(), attr)));
			return true;
		}
		if (WebFlowSchemaConstants.ELEM_ACTION.equals(elem) && WebFlowSchemaConstants.ATTR_METHOD.equals(attr)) {
			TextAttribute attrControl = createWebFlowMethodAttribute(client, attr, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new BeanMethodContentProposalProvider(getInput(),
					attr)));
			return true;
		}
		if (WebFlowSchemaConstants.ELEM_BEAN_ACTION.equals(elem) && WebFlowSchemaConstants.ATTR_METHOD.equals(attr)) {
			TextAttribute attrControl = createWebFlowMethodAttribute(client, attr, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new BeanActionMethodContentProposalProvider(
					getInput(), attr)));
			return true;
		}
		return super.addCustomAttribute(client, attr, required);
	}

}
