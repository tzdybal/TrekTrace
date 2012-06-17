package org.trektrace.entities;

import java.util.Vector;

public class Route extends Entity {
	private String name;
	private String description;
	private Vector points = new Vector();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void addPoint(Point point) {
		points.addElement(point);
	}

	public Vector getPoints() {
		return points;
	}
}
