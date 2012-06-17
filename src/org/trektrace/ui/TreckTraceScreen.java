package org.trektrace.ui;

import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.container.MainScreen;

import org.trektrace.RouteListCallback;
import org.trektrace.dao.BaseDao;
import org.trektrace.dao.RouteDao;
import org.trektrace.db.DatabaseException;
import org.trektrace.db.DatabaseManager;
import org.trektrace.entities.Route;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class TreckTraceScreen extends MainScreen {
	private ListField routeList;
	private RouteListCallback callback;

	/**
	 * Creates a new TreckTraceScreen object
	 */
	public TreckTraceScreen() {
		// Set the displayed title of the screen
		setTitle("TrekTrace");
		routeList = new ListField();
		add(routeList);
		MenuItem altitudeItem = new MenuItem("Show alitude plot", 10, 1) {
			public void run() {
				Route selected = getSelectedRoute();
				((UiApplication) getApplication()).pushScreen(new AltitudePlotScreen(selected));
			}
		};
		MenuItem mapItem = new MenuItem("Show on map", 10, 2) {
			public void run() {
				Route selected = getSelectedRoute();
				((UiApplication) getApplication()).pushScreen(new MapScreen(selected));
			}
		};
		MenuItem statItem = new MenuItem("Show statistics", 10, 3) {
			public void run() {
				Route selected = getSelectedRoute();
				((UiApplication) getApplication()).pushScreen(new RouteStatsScreen(selected));
			}
		};
		MenuItem renameItem = new MenuItem("Rename route", 20000, 1) {
			public void run() {
				Route selected = getSelectedRoute();
				RouteNameDialog d = new RouteNameDialog(selected);
				if (d.doModal() == Dialog.OK) {
					selected.setName(d.getText());
					RouteDao rd = new RouteDao();
					try {
						rd.saveWithoutPoints(selected);
						callback.refresh();
						routeList.setSize(callback.getSize());
					} catch (DatabaseException e) {
						Dialog.alert("Cannot update route information!");
					}
				}
			}
		};
		MenuItem removeItem = new MenuItem("Delete route", 20000, 2) {
			public void run() {
				Route selected = getSelectedRoute();
				if (Dialog.ask(Dialog.D_DELETE) == Dialog.DELETE) {
					RouteDao rd = new RouteDao();
					try {
						rd.remove(selected.getId());
						callback.refresh();
						routeList.setSize(callback.getSize());
					} catch (DatabaseException e) {
						Dialog.alert("Cannot delete route!");
					}
				}
			}
		};

		addMenuItem(altitudeItem);
		addMenuItem(mapItem);
		addMenuItem(statItem);
		addMenuItem(renameItem);
		addMenuItem(removeItem);
	}

	private void initUI() throws DatabaseException {
		callback = new RouteListCallback();
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
	}

	public void init() throws Exception {
		// initDatabase();
		initUI();
	}

	private Route getSelectedRoute() {
		Route selected = (Route) routeList.getCallback().get(routeList, routeList.getSelectedIndex());
		return selected;
	}
}
