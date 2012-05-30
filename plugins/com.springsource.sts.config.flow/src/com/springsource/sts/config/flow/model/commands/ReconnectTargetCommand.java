/******************************************************************************************
 * Copyright (c) 2009 - 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.flow.model.commands;

import java.util.List;

import org.eclipse.ui.texteditor.ITextEditor;

import com.springsource.sts.config.flow.model.Activity;
import com.springsource.sts.config.flow.model.Transition;

/**
 * Command that handles the reconnection of target Activities.
 * @author Leo Dos Santos
 * @author Christian Dupuis
 */
public class ReconnectTargetCommand extends AbstractTextCommand {

	/** Source Activity **/
	protected Activity source;

	/** Target Activity **/
	protected Activity target;

	/** Transition between source and target **/
	protected Transition transition;

	/** Previous target prior to command execution **/
	protected Activity oldTarget;

	public ReconnectTargetCommand(ITextEditor textEditor) {
		super(textEditor);
	}

	/**
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		if (transition.source.equals(target)) {
			return false;
		}

		List<Transition> transitions = source.getOutgoingTransitions();
		for (int i = 0; i < transitions.size(); i++) {
			Transition trans = ((transitions.get(i)));
			if (trans.target.equals(target) && !trans.target.equals(oldTarget)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		if (target != null) {
			oldTarget.removeIncoming(transition);
			transition.target = target;
			target.addIncoming(transition);
		}
	}

	/**
	 * Returns the source Activity associated with this command
	 * @return the source Activity
	 */
	public Activity getSource() {
		return source;
	}

	/**
	 * Returns the target Activity associated with this command
	 * @return the target Activity
	 */
	public Activity getTarget() {
		return target;
	}

	/**
	 * Returns the Transition associated with this command
	 * @return the Transition
	 */
	public Transition getTransition() {
		return transition;
	}

	/**
	 * Sets the source Activity associated with this command
	 * @param activity the source Activity
	 */
	public void setSource(Activity activity) {
		source = activity;
	}

	/**
	 * Sets the target Activity assoicated with this command
	 * @param activity the target Activity
	 */
	public void setTarget(Activity activity) {
		target = activity;
	}

	/**
	 * Sets the transition associated with this
	 * @param trans the transition
	 */
	public void setTransition(Transition trans) {
		transition = trans;
		source = trans.source;
		oldTarget = trans.target;
	}

}
