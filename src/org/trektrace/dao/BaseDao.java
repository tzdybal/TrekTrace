package org.trektrace.dao;

import org.trektrace.db.DatabaseException;
import org.trektrace.db.DatabaseManager;

import net.rim.device.api.database.Database;

public abstract class BaseDao {
	protected static Database db;

	public static void setDatabase(Database database) {
		db = database;
	}

	public Database getDatabase() {
		return db;
	}

	public abstract Object read(Long objectId) throws DatabaseException;

	public abstract void saveOrUpdate(Object o) throws DatabaseException;

	public abstract void remove(Long objectId) throws DatabaseException;
}
