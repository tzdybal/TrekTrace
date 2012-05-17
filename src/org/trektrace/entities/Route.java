package org.trektrace.entities;

import java.util.Vector;

public class Route extends Entity  {
	private Vector points = new Vector();
	
	public void addPoint(Point point) {
		points.addElement(point);
	}
	
	public Vector getPoints() {
		return points;
	}
}
