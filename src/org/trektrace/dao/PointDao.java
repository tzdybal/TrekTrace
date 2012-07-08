package org.trektrace.dao;

import org.trektrace.db.DatabaseException;
import org.trektrace.entities.Point;

import com.ianywhere.ultralitej12.PreparedStatement;
import com.ianywhere.ultralitej12.ResultSet;
import com.ianywhere.ultralitej12.ULjException;
import com.ianywhere.ultralitej12.UUIDValue;

public class PointDao extends BaseDao {

	public Object read(UUIDValue objectId) throws DatabaseException {
		if (objectId == null)
			return null;
		try {
			PreparedStatement stmt = da
					.getConnection()
					.prepareStatement(
							"SELECT id, route_id, altitude, latitude, longitude, time_stmp FROM points WHERE id = ?");
			stmt.set(1, objectId);

			ResultSet rs = stmt.executeQuery();
			rs.next();
			Point p = createPoint(rs);
			rs.close();
			stmt.close();
			return p;
		} catch (Exception e) {
			throw new DatabaseException(getClass().getName() + ".read() "
					+ e.getMessage());
		}
	}

	public void saveOrUpdate(Object o) throws DatabaseException {
		Point p = (Point) o;
		try {
			PreparedStatement stmt;
			if (p.getId() == null) {
				stmt = da
						.getConnection()
						.prepareStatement(
								"INSERT INTO points (route_id, altitude, latitude, longitude, time_stmp, id) VALUES (?, ?, ?, ?, ?, ?)");
				p.setId(da.getConnection().createUUIDValue());
			} else {
				stmt = da
						.getConnection()
						.prepareStatement(
								"UPDATE points SET route_id = ?, altitude = ?, latitude = ?, longitude = ?, time_stmp = ? WHERE id = ?");
			}

			stmt.set(1, p.getRouteId());
			stmt.set(2, p.getAltitude());
			stmt.set(3, p.getLatitude());
			stmt.set(4, p.getLongitude());
			stmt.set(5, p.getDate());
			stmt.set(6, p.getId());

			stmt.execute();
			da.getConnection().commit();
			stmt.close();
		} catch (Exception e) {
			throw new DatabaseException(getClass().getName()
					+ ".saveOrUpdate() " + e.getMessage());
		}
	}

	public void remove(UUIDValue objectId) throws DatabaseException {
		try {
			PreparedStatement stmt = da.getConnection().prepareStatement(
					"DELETE FROM points WHERE id = ?");
			stmt.set(1, objectId);
			stmt.execute();
			da.getConnection().commit();
			stmt.close();
		} catch (Exception e) {
			throw new DatabaseException(getClass().getName() + ".remove() "
					+ e.getMessage());
		}
	}

	static Point createPoint(ResultSet rs) throws ULjException {
		Point p = new Point();
		p.setId(rs.getUUIDValue("id"));
		p.setRouteId(rs.getUUIDValue("route_id"));
		p.setAltitude(rs.getFloat("altitude"));
		p.setLatitude(rs.getDouble("latitude"));
		p.setLongitude(rs.getDouble("longitude"));
		p.setDate(rs.getDate("time_stmp"));
		return p;
	}
}
