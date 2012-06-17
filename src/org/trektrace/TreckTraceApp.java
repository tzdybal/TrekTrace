package org.trektrace;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import org.trektrace.dao.BaseDao;
import org.trektrace.db.DatabaseException;
import org.trektrace.db.DatabaseManager;
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
					if (!DatabaseManager.databaseExists()) {
						DatabaseManager.createDatabase();
					}
					BaseDao.setDatabase(DatabaseManager.getDatabase());

					if (BaseDao.getDatabase() == null) {
						throw new DatabaseException("Database error!");
					}
				} catch (Exception ex) {
					Dialog.alert(ex.getMessage());
				}
			}
		});
	}

}
