package org.trektrace;

import java.util.Vector;

import org.trektrace.dao.RouteDao;
import org.trektrace.db.DatabaseException;
import org.trektrace.entities.Route;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;

public class RouteListCallback implements ListFieldCallback {
	private RouteDao routeDao;
	private Vector routes;
	
	public RouteListCallback() throws DatabaseException {
		routeDao = new RouteDao();
		routes = routeDao.getAllRoutes();
	}
	
	public int indexOfList(ListField listField, String prefix, int start) {
		return routes.indexOf(prefix, start);
	}
	
	public int getPreferredWidth(ListField listField) {
		return Display.getWidth();
	}
	
	public Object get(ListField listField, int index) {
		return routes.elementAt(index);
	}
	
	public void drawListRow(ListField listField, Graphics graphics, int index,
			int y, int width) {
		Route route = (Route) routes.elementAt(index);
		
		graphics.drawText(route.getName(), 0, y, 0, width);
	}

	public int getSize() {
		return routes.size();
	}

}
