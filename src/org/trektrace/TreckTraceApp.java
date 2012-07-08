package org.trektrace;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import org.trektrace.db.DataAccess;
import org.trektrace.ui.HomeScreen;

/**
 * This class extends the UiApplication class, providing a graphical user
 * interface.
 */
public class TreckTraceApp extends UiApplication {
	private HomeScreen mainScreen;

	/**
	 * Entry point for application
	 * 
	 * @param args
	 *            Command line arguments (not used)
	 */
	public static void main(String[] args) {
		// Create a new instance of the application and make the currently
		// running thread the application's event dispatch thread.
		TreckTraceApp theApp = new TreckTraceApp();
		theApp.enterEventDispatcher();
	}

	/**
	 * Creates a new TreckTraceApp object
	 */
	public TreckTraceApp() {
		mainScreen = new HomeScreen();
		pushScreen(mainScreen);
	}

	public void activate() {
		invokeLater(new Runnable() {
			public void run() {
				try {
					DataAccess.getDataAccess(false);
					/*
					Route r = new Route();
					r.setName("LOL");
					Point p = new Point();
					p.setAltitude(123);
					p.setLongitude(0.456);
					p.setLatitude(0.789);
					p.setDate(new Date());
					r.addPoint(p);
					RouteDao rd = new RouteDao();
					rd.saveOrUpdate(r);
					rd.saveOrUpdate(r);
					p.setId(null);
					p.setRouteId(null);
					r.addPoint(p);
					
					PointDao pd = new PointDao();
					pd.saveOrUpdate(p);
					
					RouteStatsGenerator.generateStats(r);
					*/
				} catch (Exception ex) {
					Dialog.alert(ex.getMessage());
				}
			}
		});

		Thread th = new Thread() {
			public void run() {
				mainScreen.updateCurrentLocation();
			}
		};
		th.start();
	}

}
