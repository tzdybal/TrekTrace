package org.trektrace;

import java.util.Date;

import org.trektrace.dao.BaseDao;
import org.trektrace.dao.RouteDao;
import org.trektrace.db.DatabaseException;
import org.trektrace.db.DatabaseManager;
import org.trektrace.entities.Point;
import org.trektrace.entities.Route;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class TreckTraceScreen extends MainScreen {
	/**
	 * Creates a new TreckTraceScreen object
	 */
	public TreckTraceScreen() {
		// Set the displayed title of the screen
		setTitle("TrekTrace - dbtest");
		
		FieldChangeListener listener = new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				try {
					initDatabase();
				} catch (DatabaseException e) {
					Dialog.alert(e.getMessage());
				}
				
			}
		};
		
		ButtonField dbButton = new ButtonField("Start DB", ButtonField.CONSUME_CLICK | Field.FIELD_HCENTER);
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
			p.setAltitude(1000 + 10*i);
			p.setLatitude(10*i);
			p.setLongitude(5*i);
			p.setDate(new Date(base + 60*i));
			
			r.addPoint(p);
		}
		
		RouteDao rd = new RouteDao();
		rd.saveOrUpdate(r);
		
		Route fromDB = (Route) rd.read(r.getId());
		Dialog.inform("points count: " + fromDB.getPoints().size());
	}
}
