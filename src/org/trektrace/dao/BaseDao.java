package org.trektrace.dao;

import org.trektrace.db.DataAccess;
import org.trektrace.db.DatabaseException;

import com.ianywhere.ultralitej12.UUIDValue;

public abstract class BaseDao {
	protected DataAccess da;
	
	public BaseDao() {
		try {
			da = DataAccess.getDataAccess(false);
		} catch (Exception e) {
			da = null;
		}
	}

	public abstract Object read(UUIDValue objectId) throws DatabaseException;

	public abstract void saveOrUpdate(Object o) throws DatabaseException;

	public abstract void remove(UUIDValue objectId) throws DatabaseException;
}
