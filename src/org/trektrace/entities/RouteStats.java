package org.trektrace.entities;

public class RouteStats {
	private double minAltitude;
	private double maxAltitude;
	private double avgAltitude;
	private double ascending;
	private double descending;
	private double distance;
	private long time;
	private double avgSpeed;
	
	public double getMinAltitude() {
		return minAltitude;
	}
	public void setMinAltitude(double minAltitude) {
		this.minAltitude = minAltitude;
	}
	public double getMaxAltitude() {
		return maxAltitude;
	}
	public void setMaxAltitude(double maxAltitude) {
		this.maxAltitude = maxAltitude;
	}
	public double getAvgAltitude() {
		return avgAltitude;
	}
	public void setAvgAltitude(double avgAltitude) {
		this.avgAltitude = avgAltitude;
	}
	public double getAscending() {
		return ascending;
	}
	public void setAscending(double ascending) {
		this.ascending = ascending;
	}
	public double getDescending() {
		return descending;
	}
	public void setDescending(double descending) {
		this.descending = descending;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public void setAvgSpeed(double avgSpeed) {
		this.avgSpeed = avgSpeed;
	}
	public double getAvgSpeed() {
		return avgSpeed;
	}
	
	
}
