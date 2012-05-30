/******************************************************************************************
 * Copyright (c) 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.ui.editors.batch;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;

import com.springsource.sts.config.core.contentassist.providers.BeanReferenceContentProposalProvider;
import com.springsource.sts.config.core.contentassist.providers.ClassContentProposalProvider;
import com.springsource.sts.config.core.contentassist.providers.StepReferenceContentProposalProvider;
import com.springsource.sts.config.core.schemas.BatchSchemaConstants;
import com.springsource.sts.config.ui.editors.AbstractConfigEditor;
import com.springsource.sts.config.ui.editors.SpringConfigDetailsSectionPart;
import com.springsource.sts.config.ui.widgets.TextAttribute;
import com.springsource.sts.config.ui.widgets.TextAttributeProposalAdapter;

/**
 * @author Leo Dos Santos
 */
@SuppressWarnings("restriction")
public class BatchDetailsSectionPart extends SpringConfigDetailsSectionPart {

	public BatchDetailsSectionPart(AbstractConfigEditor editor, IDOMElement input, Composite parent, FormToolkit toolkit) {
		super(editor, input, parent, toolkit);
	}

	@Override
	protected boolean addCustomAttribute(Composite client, String attr, boolean required) {
		// BatchContentAssistProcessor and BatchHyperlinkDetector
		String elem = getInput().getLocalName();
		if ((BatchSchemaConstants.ELEM_NEXT.equals(elem) && BatchSchemaConstants.ATTR_TO.equals(attr))
				|| (BatchSchemaConstants.ELEM_STOP.equals(elem) && BatchSchemaConstants.ATTR_RESTART.equals(attr))
				|| (BatchSchemaConstants.ELEM_SPLIT.equals(elem) && BatchSchemaConstants.ATTR_NEXT.equals(attr))
				|| (BatchSchemaConstants.ELEM_STEP.equals(elem) && BatchSchemaConstants.ATTR_NEXT.equals(attr))) {
			TextAttribute attrControl = createStepAttribute(client, attr, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new StepReferenceContentProposalProvider(
					getInput(), attr)));
			return true;
		}
		if ((BatchSchemaConstants.ELEM_JOB.equals(elem) && BatchSchemaConstants.ATTR_PARENT.equals(attr))
				|| (BatchSchemaConstants.ELEM_STEP.equals(elem) && BatchSchemaConstants.ATTR_PARENT.equals(attr))
				|| (BatchSchemaConstants.ELEM_CHUNK.equals(elem) && (BatchSchemaConstants.ATTR_PROCESSOR.equals(attr)
						|| BatchSchemaConstants.ATTR_READER.equals(attr) || BatchSchemaConstants.ATTR_WRITER
						.equals(attr)))) {
			TextAttribute attrControl = createBeanAttribute(client, attr, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new BeanReferenceContentProposalProvider(
					getInput(), attr)));
			return true;
		}
		if (BatchSchemaConstants.ATTR_CLASS.equals(attr)) {
			TextAttribute attrControl = createClassAttribute(client, attr, true, required);
			addWidget(attrControl);
			addAdapter(new TextAttributeProposalAdapter(attrControl, new ClassContentProposalProvider(getInput(), attr)));
			return true;
		}
		return super.addCustomAttribute(client, attr, required);
	}

}
