package org.trektrace.ui;

import javax.microedition.location.Coordinates;

import net.rim.device.api.lbs.MapField;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYPoint;

public class RouteMapField extends MapField {
	private Coordinates[] route;

	public RouteMapField(Coordinates[] coordinates) {
		route = coordinates;
	}

	public void paint(Graphics graphics) {
		super.paint(graphics);

		graphics.setDrawingStyle(Graphics.DRAWSTYLE_ANTIALIASED, true);
		graphics.setColor(0x000000FF);

		XYPoint current = new XYPoint();
		XYPoint last = new XYPoint();
		convertWorldToField(route[0], last);
		for (int i = 1; i < route.length; ++i) {
			convertWorldToField(route[i], current);

			graphics.drawLine(last.x, last.y, current.x, current.y);

			last.set(current);
		}
	}
}
