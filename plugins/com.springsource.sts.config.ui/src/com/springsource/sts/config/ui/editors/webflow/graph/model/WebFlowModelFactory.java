/******************************************************************************************
 * Copyright (c) 2010 - 2011 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.ui.editors.webflow.graph.model;

import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;

import com.springsource.sts.config.core.preferences.SpringConfigPreferenceConstants;
import com.springsource.sts.config.core.schemas.WebFlowSchemaConstants;
import com.springsource.sts.config.flow.model.AbstractConfigFlowDiagram;
import com.springsource.sts.config.flow.model.Activity;
import com.springsource.sts.config.flow.model.IDiagramModelFactory;
import com.springsource.sts.config.ui.ConfigUiPlugin;

/**
 * @author Leo Dos Santos
 */
@SuppressWarnings("restriction")
public class WebFlowModelFactory implements IDiagramModelFactory {

	public void getChildrenFromXml(List<Activity> list, IDOMElement input, Activity parent) {
		IPreferenceStore prefStore = ConfigUiPlugin.getDefault().getPreferenceStore();
		if (input.getLocalName().equals(WebFlowSchemaConstants.ELEM_ACTION_STATE)
				&& prefStore.getBoolean(SpringConfigPreferenceConstants.PREF_DISPLAY_ACTION_STATE)) {
			ActionStateModelElement state = new ActionStateModelElement(input, parent.getDiagram());
			list.add(state);
		}
		else if (input.getLocalName().equals(WebFlowSchemaConstants.ELEM_DECISION_STATE)
				&& prefStore.getBoolean(SpringConfigPreferenceConstants.PREF_DISPLAY_DECISION_STATE)) {
			DecisionStateModelElement state = new DecisionStateModelElement(input, parent.getDiagram());
			list.add(state);
		}
		else if (input.getLocalName().equals(WebFlowSchemaConstants.ELEM_END_STATE)
				&& prefStore.getBoolean(SpringConfigPreferenceConstants.PREF_DISPLAY_END_STATE)) {
			EndStateModelElement state = new EndStateModelElement(input, parent.getDiagram());
			list.add(state);
		}
		else if (input.getLocalName().equals(WebFlowSchemaConstants.ELEM_SUBFLOW_STATE)
				&& prefStore.getBoolean(SpringConfigPreferenceConstants.PREF_DISPLAY_SUBFLOW_STATE)) {
			SubflowStateModelElement state = new SubflowStateModelElement(input, parent.getDiagram());
			list.add(state);
		}
		else if (input.getLocalName().equals(WebFlowSchemaConstants.ELEM_VIEW_STATE)
				&& prefStore.getBoolean(SpringConfigPreferenceConstants.PREF_DISPLAY_VIEW_STATE)) {
			ViewStateModelElement state = new ViewStateModelElement(input, parent.getDiagram());
			list.add(state);
		}
	}

	public void getGenericChildrenFromXml(List<Activity> list, IDOMElement input, Activity parent) {
		// TODO Auto-generated method stub

	}

	public void getNestedChildrenFromXml(List<Activity> list, IDOMElement input, AbstractConfigFlowDiagram diagram) {
		// TODO Auto-generated method stub

	}

}
