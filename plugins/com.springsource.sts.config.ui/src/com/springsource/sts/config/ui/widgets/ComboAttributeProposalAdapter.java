/******************************************************************************************
 * Copyright (c) 2009 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.fieldassist.ComboContentAdapter;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;

import com.springsource.sts.config.core.contentassist.XmlBackedContentProposalAdapter;
import com.springsource.sts.config.core.contentassist.XmlBackedContentProposalProvider;

/**
 * A pre-configured content proposal adapter for a combo control that is set to
 * use an {@link XmlBackedContentProposalProvider} as its proposal provider.
 * @author Leo Dos Santos
 * @see ContentProposalAdapter
 * @sicne 2.1.0
 */
@SuppressWarnings("restriction")
public class ComboAttributeProposalAdapter extends XmlBackedContentProposalAdapter {

	private final ComboAttribute comboAttr;

	/**
	 * Constructs a content proposal adapter that can assist the user with
	 * choosing content for the control.
	 * 
	 * @param attrControl the {@link ComboAttribute} widget set for which the
	 * adapter is providing content
	 * @param proposalProvider the {@link XmlBackedContentProposalProvider} used
	 * to obtain content proposals for this control, or null if no content
	 */
	public ComboAttributeProposalAdapter(ComboAttribute attrControl, XmlBackedContentProposalProvider proposalProvider) {
		super(attrControl.getComboControl(), new ComboContentAdapter(), proposalProvider, null);
		comboAttr = attrControl;
	}

	private void populateCombo() {
		IContentProposal[] proposals = getContentProposalProvider().getProposals("", 0); //$NON-NLS-1$
		List<String> items = new ArrayList<String>();
		items.add(""); //$NON-NLS-1$

		for (IContentProposal proposal : proposals) {
			items.add(proposal.getContent());
		}
		comboAttr.getComboControl().setItems(items.toArray(new String[items.size()]));
	}

	@Override
	public void update(IDOMElement input) {
		super.update(input);
		if (input != null) {
			populateCombo();
			comboAttr.update();
		}
	}

}
