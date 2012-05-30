/******************************************************************************************
 * Copyright (c) 2008 - 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.quickfix.proposals;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IMarkerResolution2;

import com.springsource.sts.quickfix.QuickfixUtils;

/**
 * Abstract class for quick fix proposal for a bean attribute
 * @author Terry Denney
 * @author Leo Dos Santos
 * @author Christian Dupuis
 * @since 2.0
 */
public abstract class BeanAttributeQuickFixProposal implements ICompletionProposal, IMarkerResolution2 {

	protected int offset, length;

	private final boolean missingEndQuote;

	public BeanAttributeQuickFixProposal(int offset, int length, boolean missingEndQuote) {
		this.offset = offset;
		this.length = length;
		this.missingEndQuote = missingEndQuote;
	}

	public void apply(IDocument document) {
		try {
			if (missingEndQuote) {
				document.replace(offset + length, 0, "\"");
			}

			applyQuickFix(document);
		}
		catch (BadLocationException e) {

		}
	}

	public abstract void applyQuickFix(IDocument document);

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BeanAttributeQuickFixProposal) {
			BeanAttributeQuickFixProposal otherProposal = (BeanAttributeQuickFixProposal) obj;
			return otherProposal.offset == offset && otherProposal.length == length
					&& otherProposal.getDisplayString().equals(getDisplayString());
		}
		return super.equals(obj);
	}

	public String getAdditionalProposalInfo() {
		return null;
	}

	public IContextInformation getContextInformation() {
		return null;
	}

	public String getDescription() {
		return null;
	}

	public String getLabel() {
		return getDisplayString();
	}

	public int getLength() {
		return length;
	}

	public int getOffset() {
		return offset;
	}

	public Point getSelection(IDocument document) {
		return null;
	}

	@Override
	public int hashCode() {
		return (getDisplayString() + offset + length).hashCode();
	}

	public void run(IMarker marker) {
		IDocument document = QuickfixUtils.getDocument(marker);
		if (document != null) {
			applyQuickFix(document);
		}
	}
}
