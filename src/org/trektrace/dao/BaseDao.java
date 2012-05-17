package org.trektrace.dao;

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

	public abstract Object read(long objectId);

	public abstract boolean saveOrUpdate(Object o);

	public abstract boolean remove(long objectId);
}
