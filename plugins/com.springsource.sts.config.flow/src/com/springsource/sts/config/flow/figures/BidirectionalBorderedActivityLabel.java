/******************************************************************************************
 * Copyright (c) 2010 SpringSource, a division of VMware, Inc. All rights reserved.
 ******************************************************************************************/
package com.springsource.sts.config.flow.figures;

import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;

import com.springsource.sts.config.flow.parts.FixedConnectionAnchor;

/**
 * @author Leo Dos Santos
 */
public class BidirectionalBorderedActivityLabel extends BorderedActivityLabel {

	private final boolean leftSide;

	private final boolean incomingOnTop;

	public BidirectionalBorderedActivityLabel(int direction, List<String> inputs, List<String> outputs, boolean leftSide) {
		this(direction, inputs, outputs, leftSide, true);
	}

	public BidirectionalBorderedActivityLabel(int direction, List<String> inputs, List<String> outputs,
			boolean leftSide, boolean incomingOnTop) {
		super(direction, inputs, outputs);
		this.leftSide = leftSide;
		this.incomingOnTop = incomingOnTop;
		border = createConnectorBorder();
		setBorder(border);
		createConnectionAnchors();
	}

	@Override
	protected ConnectorBorder createConnectorBorder() {
		return new BidirectionalConnectorBorder(direction, inputs.size(), outputs.size(), leftSide);
	}

	@Override
	protected void layoutConnectionAnchors() {
		FixedConnectionAnchor in;
		FixedConnectionAnchor out;
		List<String> topItems;
		List<String> bottomItems;
		Rectangle rect = getBounds();
		int capacity = inputs.size() + outputs.size();

		if (incomingOnTop) {
			topItems = inputs;
			bottomItems = outputs;
		}
		else {
			topItems = outputs;
			bottomItems = inputs;
		}

		if (direction == PositionConstants.EAST) {
			int height = rect.height;
			for (int i = 0; i < topItems.size(); i++) {
				in = (FixedConnectionAnchor) getConnectionAnchor(topItems.get(i));
				in.offsetV = (2 * i + 1) * height / (capacity * 2);
				in.leftToRight = leftSide;
			}
			for (int i = 0; i < bottomItems.size(); i++) {
				int j = topItems.size() + i;
				out = (FixedConnectionAnchor) getConnectionAnchor(bottomItems.get(i));
				out.offsetV = (2 * j + 1) * height / (capacity * 2);
				out.leftToRight = leftSide;
			}
		}
		else {
			int width = rect.width;
			for (int i = 0; i < topItems.size(); i++) {
				in = (FixedConnectionAnchor) getConnectionAnchor(topItems.get(i));
				in.offsetV = (2 * i + 1) * width / (capacity * 2);
				in.topDown = leftSide;
			}
			for (int i = 0; i < bottomItems.size(); i++) {
				int j = topItems.size() + i;
				out = (FixedConnectionAnchor) getConnectionAnchor(bottomItems.get(i));
				out.offsetV = (2 * j + 1) * width / (capacity * 2);
				out.topDown = leftSide;
			}
		}
	}

}
