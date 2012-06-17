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
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;

public class HomeScreen extends MainScreen {
	private BlackBerryLocationProvider provider = null;
	
	private Formatter formatter = new Formatter();

	private TextField latitudeLabel;

	private TextField longitudeLabel;

	private TextField altitudeLabel;

	public HomeScreen() {
		super(MainScreen.VERTICAL_SCROLL | MainScreen.VERTICAL_SCROLLBAR);
		setTitle("TreckTrace");
	
		latitudeLabel = new TextField();
		latitudeLabel.setLabel(" latitude:");
		longitudeLabel = new TextField();
		longitudeLabel.setLabel(" longitude:");
		altitudeLabel = new TextField();
		altitudeLabel.setLabel(" altitude:");
		
		add(new LabelField("Current Position:"));
		
		add(latitudeLabel);
		add(longitudeLabel);
		add(altitudeLabel);
		
		add(new SeparatorField());
		
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
							provider.setLocationListener(new GPSListener(route, HomeScreen.this), 10, -1, -1);
							stopTracingButton.setEnabled(true);
							stopTracingButton.setFocus();
							startTracingButton.setEnabled(false);
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
				startTracingButton.setEnabled(true);
			}
		});

		hfm.add(startTracingButton);
		hfm.add(stopTracingButton);

		add(hfm);
		add(routeListButton);
		
		startTracingButton.setFocus();
	}

	public boolean isDirty() {
		return false;
	}

	public void setCurrentAltitude(float altitude) {
		altitudeLabel.setText(formatter.formatNumber(altitude, 2));
		//doPaint();
	}
	
	public void setCurrentLatitude(double latitude) {
		latitudeLabel.setText(decimalToDegree(latitude));
		doPaint();
	}
	
	public void setCurrentLongitude(double longitude) {
		longitudeLabel.setText(decimalToDegree(longitude));
		//doPaint();
	}
	
	private String decimalToDegree(double value) {		
		int deg = (int) Math.abs(value);
		double min = (Math.abs(value) - deg) * 60 % 60;
		double sec = min * 60 % 60;
		
		return deg + "° " + (int)min + "\" " + formatter.formatNumber(sec, 3) + "'";
	}
	
}
