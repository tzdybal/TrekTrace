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

		QualifiedCoordinates lastCoords = null;
		for (int i = 0; i < points.size(); ++i) {
			Point p = (Point) points.elementAt(i);
			double alt = p.getAltitude();
			sumA += alt;
			if (alt < minA) {
				minA = alt;
			} else if (alt > maxA) {
				maxA = alt;
			}

			QualifiedCoordinates coords = new QualifiedCoordinates(
					p.getLatitude(), p.getLongitude(), (float) p.getAltitude(),
					Float.NaN, Float.NaN);

			if (lastCoords != null) {
				double dist = coords.distance(lastCoords);
				distance += dist;
				if (alt > lastCoords.getAltitude()) {
					asc += dist;
				} else if (alt < lastCoords.getAltitude()) {
					desc += dist;
				}
			}
			lastCoords = coords;
		}
		
		time = ((Point)points.lastElement()).getDate().getTime() - ((Point)points.firstElement()).getDate().getTime();
		
		stats.setMaxAltitude(maxA);
		stats.setMinAltitude(minA);
		stats.setAvgAltitude(sumA / points.size());
		stats.setAscending(asc);
		stats.setDescending(desc);
		stats.setDistance(distance);
		stats.setTime(time);
		// distance is in meters, time in miliseconds, speed in km/h
		stats.setAvgSpeed((distance / 1000) / (time / 1000 /  3600));

		return stats;
	}
}
