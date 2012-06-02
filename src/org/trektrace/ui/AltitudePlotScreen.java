package org.trektrace.ui;

import java.util.Vector;

import javax.microedition.location.QualifiedCoordinates;

import org.trektrace.RouteStatsGenerator;
import org.trektrace.entities.Point;
import org.trektrace.entities.Route;
import org.trektrace.entities.RouteStats;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.container.MainScreen;

public class AltitudePlotScreen extends MainScreen {
	RouteStats stats;
	Vector points;

	public AltitudePlotScreen(Route route) {
		super(MainScreen.VERTICAL_SCROLL | MainScreen.VERTICAL_SCROLLBAR);
		setTitle(route.getName() + " - alititude plot");

		stats = RouteStatsGenerator.generateStats(route);
		points = route.getPoints();
	}

	protected void paint(Graphics graphics) {
		super.paint(graphics);

		int[] ptx = new int[points.size() + 2];
		int[] pty = new int[points.size() + 2];

		int dw = Display.getWidth();
		int dh1 = Display.getHeight();
		int dh2 = dh1 - 30;

		int pw = (int) stats.getDistance();
		int ph = (int) Math.floor(stats.getMaxAltitude()
				- stats.getMinAltitude() + 0.5);
		
		graphics.setColor(0x000000FF);
		QualifiedCoordinates lastCoords = new QualifiedCoordinates(0, 0, 0,
				Float.NaN, Float.NaN);
		QualifiedCoordinates coords = new QualifiedCoordinates(0, 0, 0,
				Float.NaN, Float.NaN);
		double dist = 0;
	
		for (int i = 0; i < points.size(); ++i) {
			Point p = (Point) points.elementAt(i);
			
			float alt = p.getAltitude();
			int cnt = 1;
			if (i - 2 >= 0) {
				alt += ((Point) points.elementAt(i-2)).getAltitude();
				++cnt;
			}
			if (i > 0) {
				alt += ((Point) points.elementAt(i-1)).getAltitude();
				++cnt;
			}
			if (i + 1 < points.size()) {
				alt += ((Point) points.elementAt(i+1)).getAltitude();
				++cnt;
			}
			if (i + 2 < points.size()) {
				alt += ((Point) points.elementAt(i+2)).getAltitude();
				++cnt;
			}
			alt /= cnt;
			pty[i] = (int) (dh1 - (alt - stats
					.getMinAltitude()) / ph * dh2);

			coords.setAltitude(p.getAltitude());
			coords.setLongitude(p.getLongitude());
			coords.setLatitude(p.getLatitude());

			if (i > 0) {
				dist += coords.distance(lastCoords);
			}
			ptx[i] = (int) (dist / pw * dw);

			lastCoords.setAltitude(coords.getAltitude());
			lastCoords.setLongitude(coords.getLongitude());
			lastCoords.setLatitude(coords.getLatitude());
		}
		ptx[ptx.length - 2] = dw;
		pty[ptx.length - 2] = dh1;

		ptx[ptx.length - 1] = 0;
		pty[ptx.length - 1] = dh1;

		graphics.setColor(0x0000FF00);
		graphics.drawFilledPath(ptx, pty, null, null);
	}

}
