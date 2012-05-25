package org.trektrace;

import java.util.Date;

import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;
import javax.microedition.location.QualifiedCoordinates;

import org.trektrace.dao.BaseDao;
import org.trektrace.dao.RouteDao;
import org.trektrace.db.DatabaseException;
import org.trektrace.db.DatabaseManager;
import org.trektrace.entities.Point;
import org.trektrace.entities.Route;
import org.trektrace.gps.GPSListener;

import net.rim.device.api.gps.BlackBerryCriteria;
import net.rim.device.api.gps.BlackBerryLocationProvider;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class TreckTraceScreen extends MainScreen implements LocationListener {
	private RouteDao routeDao;
	private Route route = new Route();
	
	/**
	 * Creates a new TreckTraceScreen object
	 */
	public TreckTraceScreen() {
		// Set the displayed title of the screen
		setTitle("TrekTrace - dbtest");
		route.setName("hard");
		route.setDescription("hardcoded one!");

		FieldChangeListener listener = new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				try {
					initDatabase();

					Dialog.inform("Database created!");
					routeDao = new RouteDao();
					
					BlackBerryCriteria criteria = new BlackBerryCriteria();
					BlackBerryLocationProvider provider = (BlackBerryLocationProvider) LocationProvider
							.getInstance(criteria);
					provider.setLocationListener(TreckTraceScreen.this, 10, -1, -1);

				} catch (Exception e) {
					Dialog.alert(e.getMessage());
				}

			}
		};

		ButtonField dbButton = new ButtonField("Start DB",
				ButtonField.CONSUME_CLICK | Field.FIELD_HCENTER);
		dbButton.setChangeListener(listener);
		add(dbButton);
	}

	private void initDatabase() throws DatabaseException {
		if (!DatabaseManager.databaseExists()) {
			DatabaseManager.createDatabase();
		}
		BaseDao.setDatabase(DatabaseManager.getDatabase());

		if (BaseDao.getDatabase() == null) {
			throw new DatabaseException("Database error!");
		}

		Route r = new Route();
		r.setName("test route");
		r.setDescription("some description");
		long base = new Date().getTime();
		for (int i = 0; i < 10; ++i) {
			Point p = new Point();
			p.setAltitude(1000 + 10 * i);
			p.setLatitude(10 * i);
			p.setLongitude(5 * i);
			p.setDate(new Date(base + 60 * i));

			r.addPoint(p);
		}

		RouteDao rd = new RouteDao();
		rd.saveOrUpdate(r);

		Route fromDB = (Route) rd.read(r.getId());
		Dialog.inform("points count: " + fromDB.getPoints().size());
	}
	
	public void locationUpdated(LocationProvider provider, Location location) {
		// TODO Auto-generated method stub
		if (location.isValid()) {
			Point p = new Point();
			QualifiedCoordinates coords = location.getQualifiedCoordinates();
			p.setAltitude(coords.getAltitude());
			p.setLatitude(coords.getLatitude());
			p.setLongitude(coords.getLongitude());
			p.setDate(new Date());

			route.addPoint(p);
			try {
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
