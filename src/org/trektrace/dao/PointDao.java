package org.trektrace.dao;

import java.util.Date;

import net.rim.device.api.database.DataTypeException;
import net.rim.device.api.database.Row;
import net.rim.device.api.database.Statement;

import org.trektrace.db.DBUtil;
import org.trektrace.db.DatabaseException;
import org.trektrace.entities.Point;

public class PointDao extends BaseDao {

	public Object read(Long objectId) throws DatabaseException {
		if (objectId == null) return null;
		try {
			Statement stmt = db
					.createStatement("SELECT id, route_id, altitude, latitude, longitude, date FROM points WHERE id = ?");
			stmt.bind(1, objectId.longValue());
			Row row = DBUtil.getFirstRow(stmt);
			return createPoint(row);
		} catch (Exception e) {
			throw new DatabaseException(getClass().getName() + ".read() " + e.getMessage());
		}
	}

	public void saveOrUpdate(Object o) throws DatabaseException {
		Point p = (Point) o;
		try {
			Statement stmt;
			if (p.getId() == null) {
				stmt = db
						.createStatement("INSERT INTO points (route_id, altitude, latitude, longitude, date) VALUES (?1, ?2, ?3, ?4, ?5)");
			} else {
				stmt = db
						.createStatement("UPDATE points SET route_id = ?1, altitude = ?2, latitude = ?3, longitude = ?4, date = ?5 WHERE id = ?6");
			}
			stmt.prepare();

			stmt.bind(1, p.getRouteId().longValue());
			stmt.bind(2, p.getAltitude());
			stmt.bind(3, p.getLatitude());
			stmt.bind(4, p.getLongitude());
			stmt.bind(5, p.getDate().getTime());
			if (p.getId() != null) {
				stmt.bind(6, p.getId().longValue());
			}
			
			stmt.execute();
			stmt.close();

			if (p.getId() == null) {
				p.setId(new Long(db.lastInsertedRowID()));
			}
		} catch (Exception e) {
			throw new DatabaseException(getClass().getName() + ".saveOrUpdate() " + e.getMessage());
		}
	}

	public void remove(Long objectId) throws DatabaseException {
		try {
			Statement stmt = db.createStatement("DELETE FROM points WHERE id = ?");
			stmt.prepare();
			stmt.bind(1, objectId.longValue());
			stmt.execute();
			stmt.close();
		} catch (net.rim.device.api.database.DatabaseException e) {
			throw new DatabaseException(getClass().getName() + ".remove() " + e.getMessage());
		}
	}

	
	Point createPoint(Row row) throws DataTypeException {
		Point p = new Point();
		p.setId(new Long(row.getLong(0)));
		p.setRouteId(new Long(row.getLong(1)));
		p.setAltitude(row.getFloat(2));
		p.setLatitude(row.getDouble(3));
		p.setLongitude(row.getDouble(4));
		p.setDate(new Date(row.getInteger(5)));
		return p;
	}
}
