package org.trektrace.db;

import net.rim.device.api.database.Cursor;
import net.rim.device.api.database.DatabaseException;
import net.rim.device.api.database.Row;
import net.rim.device.api.database.Statement;

public class DBUtil {
	public static Row getFirstRow(Statement stmt) throws DatabaseException {
		Cursor c = stmt.getCursor();
		c.first();
		Row r = c.getRow();
		c.close();
		stmt.close();
		return r;
	}
}
