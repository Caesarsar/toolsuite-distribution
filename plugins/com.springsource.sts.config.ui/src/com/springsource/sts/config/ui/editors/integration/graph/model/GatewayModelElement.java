/******************************************************************************************
 * Copyright (c) 2009 - 2012 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.ui.editors.integration.graph.model;

import java.util.Arrays;
import java.util.List;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.springsource.sts.config.core.schemas.IntegrationSchemaConstants;
import com.springsource.sts.config.flow.model.AbstractConfigFlowDiagram;
import com.springsource.sts.config.flow.model.Activity;
import com.springsource.sts.config.flow.model.ParallelActivity;
import com.springsource.sts.config.flow.model.Transition;

/**
 * @author Leo Dos Santos
 */
@SuppressWarnings("restriction")
public class GatewayModelElement extends AbstractInboundGatewayModelElement {

	public GatewayModelElement() {
		super();
	}

	public GatewayModelElement(IDOMElement input, AbstractConfigFlowDiagram diagram) {
		super(input, diagram);
	}

	@Override
	public String getInputName() {
		return IntegrationSchemaConstants.ELEM_GATEWAY;
	}

	@Override
	public List<String> getPrimaryIncomingAttributes() {
		return Arrays.asList(IntegrationSchemaConstants.ATTR_DEFAULT_REPLY_CHANNEL);
	}

	@Override
	public List<String> getPrimaryOutgoingAttributes() {
		return Arrays.asList(IntegrationSchemaConstants.ATTR_DEFAULT_REQUEST_CHANNEL);
	}

	@Override
	public List<String> getSecondaryOutgoingAttributes() {
		return Arrays.asList(IntegrationSchemaConstants.ATTR_ERROR_CHANNEL);
	}

	@Override
	protected List<Transition> getIncomingTransitionsFromXml() {
		List<Transition> list = super.getIncomingTransitionsFromXml();
		List<Activity> registry = getDiagram().getModelRegistry();
		NodeList methods = getInput().getChildNodes();
		for (int i = 0; i < methods.getLength(); i++) {
			Node node = methods.item(i);
			if (node instanceof IDOMElement && node.getLocalName().equals(IntegrationSchemaConstants.ELEM_METHOD)) {
				IDOMElement method = (IDOMElement) node;
				String channel = method.getAttribute(IntegrationSchemaConstants.ATTR_REPLY_CHANNEL);
				if (channel != null && channel.trim().length() > 0) {
					Node channelRef = getDiagram().getReferencedNode(channel);
					if (channelRef instanceof IDOMElement) {
						for (Activity activity : registry) {
							if (!(activity instanceof ParallelActivity) && activity.getInput().equals(channelRef)) {
								Transition trans = new Transition(activity, this, method);
								list.add(trans);
							}
						}
					}
				}
			}
		}
		return list;
	}

	@Override
	protected List<Transition> getOutgoingTransitionsFromXml() {
		List<Transition> list = super.getOutgoingTransitionsFromXml();
		List<Activity> registry = getDiagram().getModelRegistry();
		NodeList methods = getInput().getChildNodes();
		for (int i = 0; i < methods.getLength(); i++) {
			Node node = methods.item(i);
			if (node instanceof IDOMElement && node.getLocalName().equals(IntegrationSchemaConstants.ELEM_METHOD)) {
				IDOMElement method = (IDOMElement) node;
				String channel = method.getAttribute(IntegrationSchemaConstants.ATTR_REQUEST_CHANNEL);
				if (channel != null && channel.trim().length() > 0) {
					Node channelRef = getDiagram().getReferencedNode(channel);
					if (channelRef instanceof IDOMElement) {
						for (Activity activity : registry) {
							if (!(activity instanceof ParallelActivity) && activity.getInput().equals(channelRef)) {
								Transition trans = new Transition(this, activity, method);
								list.add(trans);
							}
						}
					}
				}
			}
		}
		return list;
	}

}
