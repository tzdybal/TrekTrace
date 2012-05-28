package org.trektrace.ui;

import java.util.Vector;

import org.trektrace.RouteStatsGenerator;
import org.trektrace.entities.Point;
import org.trektrace.entities.Route;
import org.trektrace.entities.RouteStats;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.Dialog;
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
		// TODO Auto-generated method stub
		super.paint(graphics);

		int[] ptx = new int[points.size()+2];
		int[] pty = new int[points.size()+2];

		int dw = Display.getWidth();
		int dh = Display.getHeight();

		int pw = (int) stats.getTime();
		int ph = (int) Math.floor(stats.getMaxAltitude()
				- stats.getMinAltitude() + 0.5);

		long xStart = ((Point) points.firstElement()).getDate().getTime();

		graphics.setColor(0x000000FF);
		for (int i = 0; i < points.size(); ++i) {
			Point p = (Point) points.elementAt(i);
			ptx[i] = (int) ((double) (p.getDate().getTime() - xStart) / pw * dw);
			pty[i] = (int) ((double) dh - (p.getAltitude() - stats.getMinAltitude()) / ph * dh);
			//graphics.drawText(ptx[i] + " " + pty[i], 50, 15 * i);
		}
		ptx[ptx.length-2] = dw;
		pty[ptx.length-2] = dh;
		
		ptx[ptx.length-1] = 0;
		pty[ptx.length-1] = dh;

		graphics.setColor(0x0000FF00);
		graphics.drawFilledPath(ptx, pty, null, null);
	}

}
