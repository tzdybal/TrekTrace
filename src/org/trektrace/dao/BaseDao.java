package org.trektrace.dao;

import org.trektrace.db.DatabaseException;
import net.rim.device.api.database.Database;

public abstract class BaseDao {
	protected static Database db;

	public static void setDatabase(Database database) {
		db = database;
	}

	public static Database getDatabase() {
		return db;
	}

	public abstract Object read(Long objectId) throws DatabaseException;

	public abstract void saveOrUpdate(Object o) throws DatabaseException;

	public abstract void remove(Long objectId) throws DatabaseException;
}
