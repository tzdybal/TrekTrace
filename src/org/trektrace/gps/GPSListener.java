package org.trektrace.gps;

import java.util.Date;

import javax.microedition.location.Location;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;
import javax.microedition.location.QualifiedCoordinates;

import net.rim.device.api.ui.component.Dialog;

import org.trektrace.dao.RouteDao;
import org.trektrace.entities.Point;
import org.trektrace.entities.Route;

public class GPSListener implements LocationListener {

	private RouteDao routeDao;
	private Route route;

	public GPSListener(Route route) {
		this.route = route;
		routeDao = new RouteDao();
	}

	public void locationUpdated(LocationProvider provider, Location location) {
		if (location.isValid()) {
			Point p = new Point();
			QualifiedCoordinates coords = location.getQualifiedCoordinates();
			p.setAltitude(coords.getAltitude());
			p.setLatitude(coords.getLatitude());
			p.setLongitude(coords.getLongitude());
			p.setDate(new Date());

			route.addPoint(p);
			try {
				// TODO optimize this
				routeDao.saveOrUpdate(route);
			} catch (Exception e) {
				Dialog.alert(e.getMessage());
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
