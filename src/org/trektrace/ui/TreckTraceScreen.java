package org.trektrace.ui;

import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.container.MainScreen;

import org.trektrace.RouteListCallback;
import org.trektrace.dao.BaseDao;
import org.trektrace.db.DatabaseException;
import org.trektrace.db.DatabaseManager;
import org.trektrace.entities.Route;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class TreckTraceScreen extends MainScreen {
	private ListField routeList;

	/**
	 * Creates a new TreckTraceScreen object
	 */
	public TreckTraceScreen() {
		// Set the displayed title of the screen
		setTitle("TrekTrace");
		routeList = new ListField();
		add(routeList);
		MenuItem altitudeItem = new MenuItem("Alitude plot", 10, 1) {
			public void run() {
				Route selected = getSelectedRoute();
				((UiApplication) getApplication())
						.pushScreen(new AltitudePlotScreen(selected));
			}
		};
		MenuItem mapItem = new MenuItem("Show on map", 10, 2) {
			public void run() {
				Route selected = getSelectedRoute();
				((UiApplication) getApplication())
						.pushScreen(new MapScreen(selected));
			}
		};
		MenuItem statItem = new MenuItem("Show statistics", 10, 3) {
			public void run() {
				Route selected = getSelectedRoute();
				((UiApplication) getApplication())
						.pushScreen(new RouteStatsScreen(selected));
			}
		};

		addMenuItem(altitudeItem);
		addMenuItem(mapItem);
		addMenuItem(statItem);
	}

	private void initUI() throws DatabaseException {
		RouteListCallback callback = new RouteListCallback();
		routeList.setCallback(callback);
		routeList.setSize(callback.getSize());
	}

	private void initDatabase() throws DatabaseException {
		if (!DatabaseManager.databaseExists()) {
			DatabaseManager.createDatabase();
		}
		BaseDao.setDatabase(DatabaseManager.getDatabase());

		if (BaseDao.getDatabase() == null) {
			throw new DatabaseException("Database error!");
		}

		/*
		 * Route r = new Route(); r.setName("test route");
		 * r.setDescription("some description"); long base = new
		 * Date().getTime(); for (int i = 0; i < 10; ++i) { Point p = new
		 * Point(); p.setAltitude(1000 + 10 * i); p.setLatitude(10 * i);
		 * p.setLongitude(5 * i); p.setDate(new Date(base + 60 * i));
		 * 
		 * r.addPoint(p); }
		 * 
		 * RouteDao rd = new RouteDao(); rd.saveOrUpdate(r);
		 * 
		 * Route fromDB = (Route) rd.read(r.getId());
		 * Dialog.inform("points count: " + fromDB.getPoints().size());
		 */
	}

	public void init() throws Exception {
		initDatabase();
		initUI();

		//BlackBerryCriteria criteria = new BlackBerryCriteria();
		//BlackBerryLocationProvider provider = (BlackBerryLocationProvider) LocationProvider
		//		.getInstance(criteria);
	}

	private Route getSelectedRoute() {
		Route selected = (Route) routeList.getCallback().get(routeList,
				routeList.getSelectedIndex());
		return selected;
	}
}
