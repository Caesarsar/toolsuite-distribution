/******************************************************************************************
 * Copyright (c) 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.ui.editors.beans;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;

import com.springsource.sts.config.core.contentassist.providers.BeanIdContentProposalProvider;
import com.springsource.sts.config.core.contentassist.providers.BeanReferenceContentProposalProvider;
import com.springsource.sts.config.core.contentassist.providers.ClassContentProposalProvider;
import com.springsource.sts.config.core.contentassist.providers.FactoryMethodContentProposalProvider;
import com.springsource.sts.config.core.contentassist.providers.InitDestroyMethodContentProposalProvider;
import com.springsource.sts.config.core.contentassist.providers.LookupMethodContentProposalProvider;
import com.springsource.sts.config.core.contentassist.providers.PropertyBeanReferenceContentProposalProvider;
import com.springsource.sts.config.core.contentassist.providers.PropertyNameContentProposalProvider;
import com.springsource.sts.config.core.contentassist.providers.PropertyValueContentProposalProvider;
import com.springsource.sts.config.core.contentassist.providers.ReplaceMethodContentProposalProvider;
import com.springsource.sts.config.core.contentassist.providers.ScopeContentProposalProvider;
import com.springsource.sts.config.core.schemas.BeansSchemaConstants;
import com.springsource.sts.config.ui.editors.AbstractConfigEditor;
import com.springsource.sts.config.ui.editors.SpringConfigDetailsSectionPart;
import com.springsource.sts.config.ui.widgets.ComboAttribute;
import com.springsource.sts.config.ui.widgets.ComboAttributeProposalAdapter;
import com.springsource.sts.config.ui.widgets.TextAttribute;
import com.springsource.sts.config.ui.widgets.TextAttributeProposalAdapter;

/**
 * @author Leo Dos Santos
 */
@SuppressWarnings("restriction")
public class BeansDetailsSectionPart extends SpringConfigDetailsSectionPart {

	public BeansDetailsSectionPart(AbstractConfigEditor editor, IDOMElement input, Composite parent, FormToolkit toolkit) {
		super(editor, input, parent, toolkit);
	}

	@Override
	protected boolean addCustomAttribute(Composite client, String attr, boolean required) {
		// BeansContentAssistProcessor and BeansHyperlinkDetector
		String elem = getInput().getLocalName();
		if (BeansSchemaConstants.ELEM_BEAN.equals(elem) && BeansSchemaConstants.ATTR_CLASS.equals(attr)) {
			TextAttribute attrControl = createClassAttribute(client, attr, false, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new ClassContentProposalProvider(getInput(), attr,
					false)));
			return true;
		}
		if ((BeansSchemaConstants.ELEM_CONSTRUCTOR_ARG.equals(elem) && BeansSchemaConstants.ATTR_TYPE.equals(attr))
				|| (BeansSchemaConstants.ELEM_ARG_TYPE.equals(elem) && BeansSchemaConstants.ATTR_MATCH.equals(attr))
				|| (BeansSchemaConstants.ELEM_VALUE.equals(elem) && BeansSchemaConstants.ATTR_TYPE.equals(attr))
				|| BeansSchemaConstants.ATTR_VALUE_TYPE.equals(attr) || BeansSchemaConstants.ATTR_KEY_TYPE.equals(attr)) {
			TextAttribute attrControl = createClassAttribute(client, attr, true, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new ClassContentProposalProvider(getInput(), attr)));
			return true;
		}
		if (BeansSchemaConstants.ELEM_PROPERTY.equals(elem) && BeansSchemaConstants.ATTR_REF.equals(attr)) {
			TextAttribute attrControl = createBeanAttribute(client, attr, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new PropertyBeanReferenceContentProposalProvider(
					getInput(), attr)));
			return true;
		}
		if ((BeansSchemaConstants.ELEM_BEAN.equals(elem) && (BeansSchemaConstants.ATTR_PARENT.equals(attr)
				|| BeansSchemaConstants.ATTR_DEPENDS_ON.equals(attr) || BeansSchemaConstants.ATTR_FACTORY_BEAN
				.equals(attr)))
				|| (BeansSchemaConstants.ELEM_REF.equals(elem) && BeansSchemaConstants.ATTR_BEAN.equals(attr))
				|| (BeansSchemaConstants.ELEM_IDREF.equals(elem) && BeansSchemaConstants.ATTR_BEAN.equals(attr))
				|| (BeansSchemaConstants.ELEM_CONSTRUCTOR_ARG.equals(elem) && BeansSchemaConstants.ATTR_REF
						.equals(attr))
				|| (BeansSchemaConstants.ELEM_ALIAS.equals(elem) && BeansSchemaConstants.ATTR_NAME.equals(attr))
				|| (BeansSchemaConstants.ELEM_REPLACED_METHOD.equals(elem) && BeansSchemaConstants.ATTR_REPLACER
						.equals(attr))
				|| (BeansSchemaConstants.ELEM_ENTRY.equals(elem) && (BeansSchemaConstants.ATTR_VALUE_REF.equals(attr) || BeansSchemaConstants.ATTR_KEY_REF
						.equals(attr)))
				|| (BeansSchemaConstants.ELEM_LOOKUP_METHOD.equals(elem) && BeansSchemaConstants.ATTR_BEAN.equals(attr))) {
			TextAttribute attrControl = createBeanAttribute(client, attr, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new BeanReferenceContentProposalProvider(
					getInput(), attr)));
			return true;
		}
		if ((BeansSchemaConstants.ELEM_REF.equals(elem) && BeansSchemaConstants.ATTR_LOCAL.equals(attr))
				|| (BeansSchemaConstants.ELEM_IDREF.equals(elem) && BeansSchemaConstants.ATTR_LOCAL.equals(attr))) {
			TextAttribute attrControl = createBeanAttribute(client, attr, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new BeanReferenceContentProposalProvider(
					getInput(), attr, false)));
			return true;
		}
		if (BeansSchemaConstants.ELEM_BEAN.equals(elem)
				&& (BeansSchemaConstants.ATTR_INIT_METHOD.equals(attr) || BeansSchemaConstants.ATTR_DESTROY_METHOD
						.equals(attr))) {
			TextAttribute attrControl = createInitDestroyMethodAttribute(client, attr, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new InitDestroyMethodContentProposalProvider(
					getInput(), attr)));
			return true;
		}
		if (BeansSchemaConstants.ELEM_BEAN.equals(elem) && BeansSchemaConstants.ATTR_ID.equals(attr)) {
			TextAttribute attrControl = createTextAttribute(client, attr, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl,
					new BeanIdContentProposalProvider(getInput(), attr)));
			return true;
		}
		if (BeansSchemaConstants.ELEM_REPLACED_METHOD.equals(elem) && BeansSchemaConstants.ATTR_NAME.equals(attr)) {
			TextAttribute attrControl = createLookupReplaceMethodAttribute(client, attr, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new ReplaceMethodContentProposalProvider(
					getInput(), attr)));
			return true;
		}
		if (BeansSchemaConstants.ELEM_LOOKUP_METHOD.equals(elem) && BeansSchemaConstants.ATTR_NAME.equals(attr)) {
			TextAttribute attrControl = createLookupReplaceMethodAttribute(client, attr, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new LookupMethodContentProposalProvider(
					getInput(), attr)));
			return true;
		}
		if (BeansSchemaConstants.ELEM_PROPERTY.equals(elem) && BeansSchemaConstants.ATTR_NAME.equals(attr)) {
			TextAttribute attrControl = createPropertyNameAttribute(client, attr, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new PropertyNameContentProposalProvider(
					getInput(), attr)));
			return true;
		}
		if (BeansSchemaConstants.ELEM_PROPERTY.equals(elem) && BeansSchemaConstants.ATTR_VALUE.equals(attr)) {
			TextAttribute attrControl = createClassAttribute(client, attr, false, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new PropertyValueContentProposalProvider(
					getInput(), attr)));
			return true;
		}
		if (BeansSchemaConstants.ELEM_BEAN.equals(elem) && BeansSchemaConstants.ATTR_FACTORY_METHOD.equals(attr)) {
			TextAttribute attrControl = createFactoryMethodAttribute(client, attr, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new FactoryMethodContentProposalProvider(
					getInput(), attr)));
			return true;
		}
		if (BeansSchemaConstants.ELEM_BEAN.equals(elem) && BeansSchemaConstants.ATTR_SCOPE.equals(attr)) {
			ComboAttribute attrControl = createComboAttribute(client, attr, new String[] {}, required);
			addWidget(attrControl);
			addAdapter(new ComboAttributeProposalAdapter(attrControl,
					new ScopeContentProposalProvider(getInput(), attr)));
			return true;
		}
		if (BeansSchemaConstants.ELEM_IMPORT.equals(elem) && BeansSchemaConstants.ATTR_RESOURCE.equals(attr)) {
			TextAttribute attrControl = createImportAttribute(client, attr, required);
			addWidget(attrControl);
			return true;
		}
		return super.addCustomAttribute(client, attr, required);
	}

}
