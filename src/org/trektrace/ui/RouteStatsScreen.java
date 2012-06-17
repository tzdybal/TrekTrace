package org.trektrace.ui;

import javax.microedition.global.Formatter;

import org.trektrace.RouteStatsGenerator;
import org.trektrace.entities.Route;
import org.trektrace.entities.RouteStats;

import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;

public class RouteStatsScreen extends MainScreen {
	private RouteStats stats;

	/**
	 * Create screen that shows statistics for a route.
	 */
	public RouteStatsScreen(Route route) {
		super(MainScreen.VERTICAL_SCROLL | MainScreen.VERTICAL_SCROLLBAR);
		setTitle(route.getName() + " - statistics");
		stats = RouteStatsGenerator.generateStats(route);

		double dist = stats.getDistance() / 1000.0;
		double time = stats.getTime() / 1000.0 / 3600.0;

		Formatter f = new Formatter();
		add(new LabelField("Minimal altitude [m]: " + f.formatNumber(stats.getMinAltitude(), 2)));
		add(new LabelField("Maximal altitude [m]: " + f.formatNumber(stats.getMaxAltitude(), 2)));
		add(new LabelField("Average altitude [m]: " + f.formatNumber(stats.getAvgAltitude(), 2)));
		add(new LabelField("Distance [km]: " + f.formatNumber(dist, 2)));
		add(new LabelField("Time [h]: " + f.formatNumber(time, 2)));
		add(new LabelField("Average speed [km/h]: " + f.formatNumber(stats.getAvgSpeed(), 2)));
	}

}
