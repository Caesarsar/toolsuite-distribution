/******************************************************************************************
 * Copyright (c) 2011 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.flow.model.commands;

import java.util.List;

import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Leo Dos Santos
 */
@SuppressWarnings("restriction")
public abstract class RefactoringRenameCommand extends RenameActivityCommand {

	private IDOMDocument doc;

	public RefactoringRenameCommand(ITextEditor textEditor) {
		super(textEditor);
	}

	@Override
	public boolean canExecute() {
		if (super.canExecute()) {
			if (oldName == null || oldName.trim().length() == 0 || oldName.equals(name)) {
				return false;
			}

			if (input.getOwnerDocument() instanceof IDOMDocument) {
				doc = (IDOMDocument) input.getOwnerDocument();
				return true;
			}
		}
		return false;
	}

	@Override
	public void execute() {
		IDOMModel model = doc.getModel();
		if (model != null) {
			model.beginRecording(this);
			super.execute();
			searchElements(doc.getDocumentElement());
			model.endRecording(this);
		}
	}

	protected abstract List<String> getAttributesToCheck();

	private void searchAttributes(Node parent) {
		NamedNodeMap attributes = parent.getAttributes();
		if (attributes != null) {
			for (int i = 0; i < attributes.getLength(); i++) {
				Node attr = attributes.item(i);
				if (getAttributesToCheck().contains(attr.getNodeName()) && oldName.equals(attr.getNodeValue())) {
					attr.setNodeValue(name);
				}
			}
		}
	}

	private void searchElements(Node parent) {
		NodeList elements = parent.getChildNodes();
		for (int i = 0; i < elements.getLength(); i++) {
			Node elementNode = elements.item(i);
			searchAttributes(elementNode);
			searchElements(elementNode);
		}
	}

}
