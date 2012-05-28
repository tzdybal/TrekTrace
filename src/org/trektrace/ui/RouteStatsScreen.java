package org.trektrace.ui;

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
		
		add(new LabelField("Minimal altitude [m]: " + stats.getMinAltitude()));
		add(new LabelField("Maximal altitude [m]: " + stats.getMaxAltitude()));
		add(new LabelField("Average altitude [m]: " + stats.getAvgAltitude()));
		add(new LabelField("Distance [km]: " + stats.getDistance() / 1000));
		add(new LabelField("Time [h]: " + stats.getTime() / 1000.0 / 3600.0));
		add(new LabelField("Average speed [km/h]: " + stats.getAvgSpeed()));
		add(new LabelField("Distance ascending [km]: " + stats.getAscending() / 1000));
		add(new LabelField("Distance descending [km]: " + stats.getDescending() / 1000));
		
	}

}
