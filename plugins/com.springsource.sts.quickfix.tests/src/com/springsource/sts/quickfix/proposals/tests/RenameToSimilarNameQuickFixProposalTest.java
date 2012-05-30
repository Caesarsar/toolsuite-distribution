/******************************************************************************************
 * Copyright (c) 2009, 2011 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.quickfix.proposals.tests;

import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.xml.core.internal.document.AttrImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;

import com.springsource.sts.config.core.schemas.BeansSchemaConstants;
import com.springsource.sts.quickfix.proposals.RenameToSimilarNameQuickFixProposal;
import com.springsource.sts.quickfix.tests.QuickfixTestUtil;

/**
 * Test case for RenameToSimlarNameQuickFixProposl
 * @author Terry Denney
 * @author Leo Dos Santos
 * @author Christian Dupuis
 * @author Martin Lippert
 */
@SuppressWarnings("restriction")
public class RenameToSimilarNameQuickFixProposalTest extends AbstractBeanFileQuickfixTestCase {

	private AttrImpl getAttrAndApplyClassRenamingProposal(String beanName, String newClassName,
			boolean isMissingEndQuote) {
		IDOMNode beanNode = QuickfixTestUtil.getNode(BeansSchemaConstants.ELEM_BEAN, beanName,
				beansNode.getChildNodes());
		AttrImpl classAttr = (AttrImpl) beanNode.getAttributes().getNamedItem(BeansSchemaConstants.ATTR_CLASS);
		ITextRegion valueRegion = classAttr.getValueRegion();

		int offset = getOffset(valueRegion, beanNode);
		int length = getLength(valueRegion, isMissingEndQuote);

		RenameToSimilarNameQuickFixProposal proposal = new RenameToSimilarNameQuickFixProposal(newClassName, offset,
				length, isMissingEndQuote);
		proposal.apply(document);

		beanNode = QuickfixTestUtil.getNode(BeansSchemaConstants.ELEM_BEAN, beanName, beansNode.getChildNodes());
		classAttr = (AttrImpl) beanNode.getAttributes().getNamedItem(BeansSchemaConstants.ATTR_CLASS);
		return classAttr;
	}

	private AttrImpl getAttrAndApplyPropertyNameRenamingProposal(String beanName, String newPropertyName,
			boolean isMissingEndQuote) {
		IDOMNode beanNode = QuickfixTestUtil.getNode(BeansSchemaConstants.ELEM_BEAN, beanName,
				beansNode.getChildNodes());
		IDOMNode propertyNode = QuickfixTestUtil.getFirstNode(BeansSchemaConstants.ELEM_PROPERTY,
				beanNode.getChildNodes());
		AttrImpl nameAttr = (AttrImpl) propertyNode.getAttributes().getNamedItem(BeansSchemaConstants.ATTR_NAME);
		ITextRegion valueRegion = nameAttr.getValueRegion();

		int offset = getOffset(valueRegion, propertyNode);
		int length = getLength(valueRegion, isMissingEndQuote);

		RenameToSimilarNameQuickFixProposal proposal = new RenameToSimilarNameQuickFixProposal("account", offset,
				length, isMissingEndQuote);
		proposal.apply(document);

		beanNode = QuickfixTestUtil.getNode(BeansSchemaConstants.ELEM_BEAN, beanName, beansNode.getChildNodes());
		propertyNode = QuickfixTestUtil.getFirstNode(BeansSchemaConstants.ELEM_PROPERTY, beanNode.getChildNodes());
		nameAttr = (AttrImpl) propertyNode.getAttributes().getNamedItem(BeansSchemaConstants.ATTR_NAME);
		return nameAttr;
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testRenameClassAttribute() throws Exception {
		openBeanEditor("src/rename-proposals-class.xml");
		AttrImpl classAttr = getAttrAndApplyClassRenamingProposal("renameTest1", "com.test.Account", false);
		assertEquals("Expects class attribute = com.test.Account", "com.test.Account", classAttr.getValue());
	}

	public void testRenameClassAttributeWithMissingEndQuote() throws Exception {
		openBeanEditor("src/rename-proposals-class.xml");
		AttrImpl classAttr = getAttrAndApplyClassRenamingProposal("renameTest2", "com.test.Account", true);
		assertEquals("Expects class attribute = com.test.Account", "com.test.Account", classAttr.getValue());
	}

	public void testRenamePropertyName() throws Exception {
		openBeanEditor("src/rename-proposals-attribute.xml");
		AttrImpl nameAttr = getAttrAndApplyPropertyNameRenamingProposal("renameTest3", "account", false);
		assertEquals("Expects property name = account", "account", nameAttr.getValue());
	}

	public void testRenamePropertyNameWithMissingEndQuote() throws Exception {
		openBeanEditor("src/rename-proposals-attribute.xml");
		AttrImpl nameAttr = getAttrAndApplyPropertyNameRenamingProposal("renameTest4", "account", true);
		assertEquals("Expects property name = account", "account", nameAttr.getValue());
	}
}
