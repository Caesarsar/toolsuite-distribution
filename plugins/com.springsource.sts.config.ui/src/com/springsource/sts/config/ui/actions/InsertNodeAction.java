/******************************************************************************************
 * Copyright (c) 2008 - 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.ui.actions;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Document;

import com.springsource.sts.config.core.contentassist.SpringConfigContentAssistProcessor;

/**
 * @author Leo Dos Santos
 * @author Christian Dupuis
 */
@SuppressWarnings("restriction")
public class InsertNodeAction extends TreeViewerNodeAction {

	private final StructuredTextViewer textView;

	private final String childName;

	public InsertNodeAction(TreeViewer treeViewer, SpringConfigContentAssistProcessor processor,
			StructuredTextViewer textView, String childName) {
		super(treeViewer, processor);
		this.textView = textView;
		this.childName = childName;
		setText(Messages.getString("InsertNodeAction.INSERT_ELEMENT_PREFIX") + childName + Messages.getString("InsertNodeAction.INSERT_ELEMENT_SUFFIX")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public void run() {
		super.run();
		IDOMElement selection = getElementFromTreeItem(getSelectedTreeItem());
		if (textView != null && selection != null) {
			IDOMModel model = selection.getModel();
			Document doc = null;
			if (selection instanceof Document) {
				doc = (Document) selection;
			}
			else if (selection.getOwnerDocument() != null) {
				doc = selection.getOwnerDocument();
			}

			if (model != null && doc != null) {
				model.beginRecording(textView);
				IDOMElement child = (IDOMElement) doc.createElement(childName);
				selection.appendChild(child);
				processor.insertDefaultAttributes(child);
				formatter.formatNode(child);
				formatter.formatNode(child.getParentNode());
				model.endRecording(textView);
				if (treeViewer != null) {
					treeViewer.setSelection(new StructuredSelection(child));
				}
			}
		}
	}

}
