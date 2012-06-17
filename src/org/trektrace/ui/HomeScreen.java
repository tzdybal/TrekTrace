package org.trektrace.ui;

import java.util.Calendar;

import javax.microedition.global.Formatter;
import javax.microedition.location.LocationProvider;

import org.trektrace.entities.Route;
import org.trektrace.gps.GPSListener;

import net.rim.device.api.gps.BlackBerryCriteria;
import net.rim.device.api.gps.BlackBerryLocationProvider;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;

public class HomeScreen extends MainScreen {
	private BlackBerryLocationProvider provider = null;

	public HomeScreen() {
		super(MainScreen.VERTICAL_SCROLL | MainScreen.VERTICAL_SCROLLBAR);
		setTitle("TreckTrace");

		final ButtonField routeListButton = new ButtonField("Saved routes", Field.FIELD_HCENTER);
		final ButtonField startTracingButton = new ButtonField("Start tracing");
		final ButtonField stopTracingButton = new ButtonField("Stop tracing");
		stopTracingButton.setEnabled(false);
		HorizontalFieldManager hfm = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER);

		routeListButton.setRunnable(new Thread() {
			public void run() {
				TreckTraceScreen screen = new TreckTraceScreen();
				try {
					screen.init();
				} catch (Exception e) {
					Dialog.alert(e.getMessage());
				}
				((UiApplication) getApplication()).pushScreen(screen);
			}
		});

		startTracingButton.setRunnable(new Thread() {
			public void run() {
				try {
					Formatter f = new Formatter();
					Route route = new Route();
					route.setName(f.formatDateTime(Calendar.getInstance(), Formatter.DATETIME_SHORT));
					RouteNameDialog d = new RouteNameDialog(route, Dialog.D_OK_CANCEL);
					if (d.doModal() == Dialog.OK) {
						route.setName(d.getText());

						if (provider == null) {
							BlackBerryCriteria criteria = new BlackBerryCriteria();
							provider = (BlackBerryLocationProvider) LocationProvider.getInstance(criteria);
							provider.setLocationListener(new GPSListener(route), 10, -1, -1);
							stopTracingButton.setEnabled(true);
						}
					}
				} catch (Exception e) {
					Dialog.alert(e.getMessage());
				}
			}

		});

		stopTracingButton.setRunnable(new Thread() {
			public void run() {
				provider.setLocationListener(null, -1, -1, -1);
				routeListButton.setFocus();
				stopTracingButton.setEnabled(false);
			}
		});

		hfm.add(startTracingButton);
		hfm.add(stopTracingButton);

		add(hfm);
		add(routeListButton);
	}

	public boolean isDirty() {
		return false;
	}
}
