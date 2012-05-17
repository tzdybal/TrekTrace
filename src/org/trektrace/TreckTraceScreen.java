package org.trektrace;

import org.trektrace.dao.BaseDao;
import org.trektrace.db.DatabaseException;
import org.trektrace.db.DatabaseManager;

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
		System.out.println("Database initialised! ("
				+ DatabaseManager.getDatabase() + ")");
	}
}
