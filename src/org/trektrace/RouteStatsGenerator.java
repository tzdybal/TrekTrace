package org.trektrace;

import java.util.Vector;

import javax.microedition.location.QualifiedCoordinates;

import org.trektrace.entities.Point;
import org.trektrace.entities.Route;
import org.trektrace.entities.RouteStats;

public class RouteStatsGenerator {
	public static RouteStats generateStats(Route route) {
		RouteStats stats = new RouteStats();

		double minA = Double.MAX_VALUE;
		double maxA = Double.MIN_VALUE;
		double sumA = 0;
		double asc = 0;
		double desc = 0;
		double distance = 0;
		long time = 0;

		Vector points = route.getPoints();

		QualifiedCoordinates lastCoords = new QualifiedCoordinates(0, 0, 0, Float.NaN, Float.NaN);
		QualifiedCoordinates coords = new QualifiedCoordinates(0, 0, 0, Float.NaN, Float.NaN);

		for (int i = 0; i < points.size(); ++i) {
			Point p = (Point) points.elementAt(i);
			double alt = p.getAltitude();
			sumA += alt;
			if (alt < minA) {
				minA = alt;
			}
			if (alt > maxA) {
				maxA = alt;
			}

			coords.setAltitude(p.getAltitude());
			coords.setLongitude(p.getLongitude());
			coords.setLatitude(p.getLatitude());

			if (i > 0) {
				double dist = coords.distance(lastCoords);
				distance += dist;
				/*
				 * if (alt > lastCoords.getAltitude()) { asc += dist; } if (alt
				 * < lastCoords.getAltitude()) { desc += dist; }
				 */
			}

			lastCoords.setAltitude(coords.getAltitude());
			lastCoords.setLongitude(coords.getLongitude());
			lastCoords.setLatitude(coords.getLatitude());
		}

		time = ((Point) points.lastElement()).getDate().getTime() - ((Point) points.firstElement()).getDate().getTime();

		stats.setMaxAltitude(maxA);
		stats.setMinAltitude(minA);
		stats.setAvgAltitude(sumA / points.size());
		// stats.setAscending(asc);
		// stats.setDescending(desc);
		stats.setDistance(distance);
		stats.setTime(time);
		// distance is in meters, time in miliseconds, speed in km/h
		stats.setAvgSpeed((distance / 1000.0) / (time / 1000.0 / 3600.0));

		return stats;
	}
}
