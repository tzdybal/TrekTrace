package org.trektrace.db;

import org.trektrace.Configuration;

import net.rim.device.api.database.Database;
import net.rim.device.api.database.DatabaseFactory;
import net.rim.device.api.database.Statement;
import net.rim.device.api.io.URI;

public class DatabaseManager {
	private static final URI DB_URI = getURI();
	private static Database db = null;

	private static URI getURI() {
		try {
			return URI.create(Configuration.DB_FILE);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean createDatabase() throws DatabaseException {
		if (databaseExists()) {
			openDatabase();
			return false;
		} else {
			try {
				db = DatabaseFactory.create(DB_URI);
				return true;
			} catch (Exception e) {
				throw new DatabaseException(e);
			}
		}
	}

	public static Database getDatabase() throws DatabaseException {
		if (db == null) {
			openDatabase();
		}
		return db;
	}

	public static boolean databaseExists() {
		try {
			if (DB_URI == null)
				return false;

			return DatabaseFactory.exists(DB_URI);
		} catch (Exception e) {
			return false;
		}
	}

	private static void openDatabase() throws DatabaseException {
		try {
			URI uri = URI.create(Configuration.DB_FILE);
			db = DatabaseFactory.open(uri);
			createTables();
		} catch (Exception e) {
			throw new DatabaseException(e);
		}
	}

	private static void createTables() throws DatabaseException {
		try {
			Statement pointsStmt = db.createStatement("CREATE TABLE IF NOT EXISTS points" + "("
					+ "id INTEGER PRIMARY KEY" + ", route_id INTEGER NOT NULL" + ", altitude DOUBLE NOT NULL"
					+ ", latitude DOUBLE NOT NULL" + ", longitude DOUBLE NOT NULL" + ", date INTEGER NOT NULL" + ")");

			Statement routesStmt = db.createStatement("CREATE TABLE IF NOT EXISTS routes " + "("
					+ "id INTEGER PRIMARY KEY" + ", name TEXT NOT NULL" + ", description TEXT" + ")");

			Statement settingsStmt = db.createStatement("CREATE TABLE IF NOT EXISTS settings " + "("
					+ "key TEXT NOT NULL" + ", value TEXT" + ")");

			pointsStmt.prepare();
			routesStmt.prepare();
			settingsStmt.prepare();

			db.beginTransaction();
			pointsStmt.execute();
			routesStmt.execute();
			settingsStmt.execute();
			db.commitTransaction();

			pointsStmt.close();
			routesStmt.close();
			settingsStmt.close();
		} catch (Exception e) {
			throw new DatabaseException(e);
		}

	}
}
