package org.trektrace.ui;

import java.util.Vector;

import javax.microedition.location.Coordinates;

import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.container.FullScreen;

import org.trektrace.entities.Point;
import org.trektrace.entities.Route;

public class MapScreen extends FullScreen {
	private RouteMapField mapField;

	public MapScreen(Route route) {
		super(Screen.DEFAULT_CLOSE | Screen.DEFAULT_MENU);

		double latitude = 0;
		double longitude = 0;

		Vector points = route.getPoints();
		Coordinates[] coords = new Coordinates[points.size()];
		for (int i = 0; i < points.size(); ++i) {
			Point p = (Point) points.elementAt(i);
			latitude += p.getLatitude();
			longitude += p.getLongitude();

			coords[i] = new Coordinates(p.getLatitude(), p.getLongitude(), p.getAltitude());
		}

		Coordinates center = new Coordinates(latitude / points.size(), longitude / points.size(), 0);

		mapField = new RouteMapField(coords);
		add(mapField);

		mapField.moveTo(center);
		mapField.setZoom(mapField.getMaxZoom() / 2);
	}

	protected boolean keyChar(char c, int status, int time) {
		if (c == 'i' || c == 'I') {
			mapField.setZoom(Math.max(mapField.getZoom() - 1, mapField.getMinZoom()));
		} else if (c == 'o' || c == 'O') {
			mapField.setZoom(Math.min(mapField.getZoom() + 1, mapField.getMaxZoom()));
		}
		return super.keyChar(c, status, time);
	}

	public boolean navigationMovement(int dx, int dy, int status, int time) {

		int mx = 0;
		int my = 0;
		int skip = 25;
		if (dx > 0)
			mx = skip;
		else if (dx < 0)
			mx = -skip;
		if (dy > 0)
			my = skip;
		else if (dy < 0)
			my = -skip;

		mapField.move(mx, my);
		return true;
	}

	public boolean onClose() {
		setDirty(false);
		return super.onClose();
	}

}
