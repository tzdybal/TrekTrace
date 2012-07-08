package org.trektrace.db;

import net.rim.device.api.ui.component.Dialog;

import com.ianywhere.ultralitej12.ConfigObjectStore;
import com.ianywhere.ultralitej12.Connection;
import com.ianywhere.ultralitej12.DatabaseManager;
import com.ianywhere.ultralitej12.PreparedStatement;
import com.ianywhere.ultralitej12.SyncParms;
import com.ianywhere.ultralitej12.ULjException;

public class DataAccess {
	private static Connection conn;
	private static DataAccess instance;
	private static SyncParms syncParms;

	public static synchronized DataAccess getDataAccess(boolean reset)
			throws Exception {
		if (instance == null) {
			instance = new DataAccess();
			ConfigObjectStore config = DatabaseManager
					.createConfigurationObjectStore("TreckTraceDB");
			if (reset) {
				conn = DatabaseManager.createDatabase(config);
				instance.createDatabaseSchema();
			} else {
				try {
					conn = DatabaseManager.connect(config);
				} catch (ULjException uex1) {
					if (uex1.getErrorCode() != ULjException.SQLE_ULTRALITE_DATABASE_NOT_FOUND) {
						Dialog.alert("Exception: " + uex1.toString()
								+ ". Recreating database...");
					}
					conn = DatabaseManager.createDatabase(config);
					instance.createDatabaseSchema();
				}
			}
		}
		return instance;
	}
	
	public Connection getConnection() {
		return conn;
	}

	private void createDatabaseSchema() throws DatabaseException {
		try {
			PreparedStatement pointsStmt = conn.prepareStatement(
					"CREATE TABLE points"
					+ "("
					+ "id UNIQUEIDENTIFIER DEFAULT NEWID()"
					+ ", route_id UNIQUEIDENTIFIER NOT NULL"
					+ ", altitude DOUBLE NOT NULL"
					+ ", latitude DOUBLE NOT NULL"
					+ ", longitude DOUBLE NOT NULL"
					+ ", time_stmp DATE NOT NULL"
					+ ", PRIMARY KEY (id)"
					+ ")");

			PreparedStatement routesStmt = conn.prepareStatement(
					"CREATE TABLE routes "
					+ "("
					+ "id UNIQUEIDENTIFIER DEFAULT NEWID()"
					+ ", name VARCHAR(256) NOT NULL"
					+ ", description VARCHAR(256)"
					+ ", PRIMARY KEY (id)"
					+ ")");
		
			pointsStmt.execute();
			routesStmt.execute();

			pointsStmt.close();
			routesStmt.close();

		} catch (Exception e) {
			throw new DatabaseException(e);
		}
	}

	public boolean sync() {
		try {
			if (syncParms == null) {
				syncParms = conn.createSyncParms(SyncParms.HTTP_STREAM,
						"mluser", "HelloBlackBerrySyncModel");
				syncParms.setPassword("mlpassword");
				syncParms.getStreamParms().setHost("your-host-name"); // USE
																		// YOUR
																		// OWN
				syncParms.getStreamParms().setPort(8081); // USE YOUR OWN
			}
			conn.synchronize(syncParms);
			return true;
		} catch (ULjException uex) {
			Dialog.alert("Exception: " + uex.toString());
			return false;
		}
	}
}
