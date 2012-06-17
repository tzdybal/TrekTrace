package org.trektrace.ui;

import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.SeparatorField;
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
		MenuItem altitudeItem = new MenuItem("Show alitude plot", 0, 1) {
			public void run() {
				Route selected = getSelectedRoute();
				((UiApplication) getApplication()).pushScreen(new AltitudePlotScreen(selected));
			}
		};
		MenuItem mapItem = new MenuItem("Show on map", 0, 2) {
			public void run() {
				Route selected = getSelectedRoute();
				((UiApplication) getApplication()).pushScreen(new MapScreen(selected));
			}
		};
		MenuItem statItem = new MenuItem("Show statistics", 0, 3) {
			public void run() {
				Route selected = getSelectedRoute();
				((UiApplication) getApplication()).pushScreen(new RouteStatsScreen(selected));
			}
		};
		MenuItem renameItem = new MenuItem("Rename route", 0, 10) {
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
		MenuItem removeItem = new MenuItem("Delete route", 0, 11) {
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
		addMenuItem(MenuItem.separator(0));
		addMenuItem(renameItem);
		addMenuItem(removeItem);
		addMenuItem(MenuItem.separator(0));
	}

	private void initUI() throws DatabaseException {
		callback = new RouteListCallback();
		routeList.setCallback(callback);
		routeList.setSize(callback.getSize());
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
