/******************************************************************************************
 * Copyright (c) 2009 - 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.flow.model.commands;

import java.util.List;

import org.eclipse.ui.texteditor.ITextEditor;

import com.springsource.sts.config.flow.model.Activity;
import com.springsource.sts.config.flow.model.Transition;

/**
 * Command that handles the reconnection of source Activities.
 * @author Leo Dos Santos
 * @author Christian Dupuis
 */
public class ReconnectSourceCommand extends AbstractTextCommand {

	/** source Activity **/
	protected Activity source;

	/** target Activity **/
	protected Activity target;

	/** transition between source and target **/
	protected Transition transition;

	/** previous source prior to command execution **/
	protected Activity oldSource;

	public ReconnectSourceCommand(ITextEditor textEditor) {
		super(textEditor);
	}

	/**
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		if (transition.target.equals(source)) {
			return false;
		}

		List<Transition> transitions = source.getOutgoingTransitions();
		for (int i = 0; i < transitions.size(); i++) {
			Transition trans = ((transitions.get(i)));
			if (trans.target.equals(target) && !trans.source.equals(oldSource)) {
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
		if (source != null) {
			oldSource.removeOutgoing(transition);
			transition.source = source;
			source.addOutgoing(transition);
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
		target = trans.target;
		oldSource = trans.source;
	}

}
