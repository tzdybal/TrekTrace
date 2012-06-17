package org.trektrace.gps;

import java.util.Date;

import javax.microedition.location.Location;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;
import javax.microedition.location.QualifiedCoordinates;

import net.rim.device.api.ui.component.Dialog;

import org.trektrace.dao.PointDao;
import org.trektrace.dao.RouteDao;
import org.trektrace.entities.Point;
import org.trektrace.entities.Route;
import org.trektrace.ui.HomeScreen;

public class GPSListener implements LocationListener {
	private RouteDao routeDao;
	private PointDao pointDao;
	private HomeScreen screen;
	private Route route;

	public GPSListener(Route route, HomeScreen screen) {
		this.route = route;
		this.screen = screen;
		routeDao = new RouteDao();
		pointDao = routeDao.getPointDao();
		try {
			routeDao.saveOrUpdate(route);
		} catch (Exception e) {
			Dialog.alert(e.getMessage());
		}
	}

	public void locationUpdated(LocationProvider provider, Location location) {
		if (location.isValid()) {
			QualifiedCoordinates coords = location.getQualifiedCoordinates();

			screen.setCurrentAltitude(coords.getAltitude());
			screen.setCurrentLatitude(coords.getLatitude());
			screen.setCurrentLongitude(coords.getLongitude());

			Point p = new Point();

			p.setAltitude(coords.getAltitude());
			p.setLatitude(coords.getLatitude());
			p.setLongitude(coords.getLongitude());
			p.setDate(new Date());

			if (route != null) {
				route.addPoint(p);
				try {
					// routeDao.saveOrUpdate(route);
					pointDao.saveOrUpdate(p);
				} catch (Exception e) {
					Dialog.alert(e.getMessage());
				}
			}
		}
	}

	public void providerStateChanged(LocationProvider provider, int newState) {
		switch (newState) {
		case LocationProvider.AVAILABLE:
			Dialog.alert("Provider available!");
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			Dialog.alert("Provider temporairly unavailable!");
			break;
		case LocationProvider.OUT_OF_SERVICE:
			Dialog.alert("Provider out of service!");
			break;
		}
	}
}
