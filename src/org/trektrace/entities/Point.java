package org.trektrace.entities;

import java.util.Date;

public class Point extends Entity {
	private Long routeId;
	private float altitude;
	private double latitude;
	private double longitude;
	private Date date;
	
	public Long getRouteId() {
		return routeId;
	}
	public void setRouteId(Long routeId) {
		this.routeId = routeId;
	}
	public float getAltitude() {
		return altitude;
	}
	public void setAltitude(float altitude) {
		this.altitude = altitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getDate() {
		return date;
	}
}
