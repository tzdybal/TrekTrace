package org.trektrace.dao;

import java.util.Vector;

import org.trektrace.db.DatabaseException;
import org.trektrace.entities.Point;
import org.trektrace.entities.Route;

import com.ianywhere.ultralitej12.PreparedStatement;
import com.ianywhere.ultralitej12.ResultSet;
import com.ianywhere.ultralitej12.UUIDValue;

public class RouteDao extends BaseDao {
	private PointDao pointDao = new PointDao();

	public Object read(UUIDValue objectId) throws DatabaseException {
		if (objectId == null)
			return null;
		try {
			PreparedStatement stmt = da.getConnection().prepareStatement(
					"SELECT name, description FROM routes WHERE id = ?");
			stmt.set(1, objectId);
			ResultSet rs = stmt.executeQuery();
			rs.next();

			Route route = new Route();
			route.setId(objectId);
			route.setName(rs.getString("name"));
			route.setDescription(rs.getString("description"));

			rs.close();
			stmt.close();

			PreparedStatement pointsStmt = da
					.getConnection()
					.prepareStatement(
							"SELECT id, route_id, altitude, latitude, longitude, time_stmp FROM points WHERE route_id = ?");
			pointsStmt.set(1, objectId);
			ResultSet points = pointsStmt.executeQuery();

			while (points.next()) {
				final Point p = PointDao.createPoint(points);
				route.addPoint(p);
			}
			points.close();
			pointsStmt.close();

			return route;
		} catch (Exception e) {
			throw new DatabaseException(getClass().getName() + ".read() "
					+ e.getMessage());
		} finally {
		}
	}

	public void saveOrUpdate(Object o) throws DatabaseException {
		Route r = (Route) o;

		try {
			PreparedStatement stmt;
			if (r.getId() == null) {
				stmt = da
						.getConnection()
						.prepareStatement(
								"INSERT INTO routes (name, description, id) VALUES (?, ?, ?)");
				r.setId(da.getConnection().createUUIDValue());
			} else {
				stmt = da
						.getConnection()
						.prepareStatement(
								"UPDATE routes SET name = ?, description = ? WHERE id = ?");

			}

			stmt.set(1, r.getName());
			stmt.set(2, r.getDescription());
			stmt.set(3, r.getId());

			stmt.execute();
			Vector v = r.getPoints();

			for (int i = 0; i < v.size(); ++i) {
				Point p = (Point) v.elementAt(i);
				p.setRouteId(r.getId());
				pointDao.saveOrUpdate(p);
			}

			da.getConnection().commit();

			stmt.close();
		} catch (Exception e) {
			throw new DatabaseException(getClass().getName()
					+ ".saveOrUpdate() " + e.getMessage());
		}
	}

	public void saveWithoutPoints(Route o) throws DatabaseException {
		Route r = (Route) o;

		try {
			PreparedStatement stmt;
			if (r.getId() == null) {
				stmt = da
						.getConnection()
						.prepareStatement(
								"INSERT INTO routes (name, description, id) VALUES (?, ?, ?)");
				r.setId(da.getConnection().createUUIDValue());
			} else {
				stmt = da
						.getConnection()
						.prepareStatement(
								"UPDATE routes SET name = ?, description = ? WHERE id = ?");

			}
			stmt.set(1, r.getName());
			stmt.set(2, r.getDescription());
			stmt.set(3, r.getId());

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
			PreparedStatement points = da.getConnection().prepareStatement(
					"DELETE FROM points WHERE route_id = ?");
			PreparedStatement route = da.getConnection().prepareStatement(
					"DELETE FROM routes WHERE id = ?");

			points.set(1, objectId);
			route.set(1, objectId);

			points.execute();
			route.execute();
			da.getConnection().commit();

			points.close();
			route.close();
		} catch (Exception e) {
			throw new DatabaseException(getClass().getName() + ".remove() "
					+ e.getMessage());
		}
	}

	public Vector getAllRoutes() throws DatabaseException {
		try {
			PreparedStatement routesStmt = da.getConnection().prepareStatement(
					"SELECT id FROM routes");

			ResultSet rs = routesStmt.executeQuery();

			Vector routes = new Vector();
			while (rs.next()) {
				routes.addElement(read(rs.getUUIDValue("id")));
			}

			return routes;
		} catch (Exception ex) {
			throw new DatabaseException(ex);
		}
	}

	public PointDao getPointDao() {
		return pointDao;
	}

}
