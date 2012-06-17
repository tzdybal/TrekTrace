package org.trektrace.dao;

import java.util.Vector;

import net.rim.device.api.database.Cursor;
import net.rim.device.api.database.Row;
import net.rim.device.api.database.Statement;

import org.trektrace.db.DBUtil;
import org.trektrace.db.DatabaseException;
import org.trektrace.entities.Point;
import org.trektrace.entities.Route;

public class RouteDao extends BaseDao {
	private PointDao pointDao = new PointDao();

	public Object read(Long objectId) throws DatabaseException {
		if (objectId == null)
			return null;
		try {
			Statement stmt = db.createStatement("SELECT name, description FROM routes WHERE id = ?");
			stmt.prepare();
			stmt.bind(1, objectId.longValue());
			Row row = DBUtil.getFirstRow(stmt);

			Route route = new Route();
			route.setId(objectId);
			route.setName(row.getString(0));
			route.setDescription(row.getString(1));

			stmt.close();

			Statement pointsStmt = db
					.createStatement("SELECT id, route_id, altitude, latitude, longitude, date FROM points WHERE route_id = ?");
			pointsStmt.prepare();
			pointsStmt.bind(1, objectId.longValue());
			Cursor c = pointsStmt.getCursor();

			PointDao pointDao = new PointDao();
			while (c.next()) {
				final Point p = pointDao.createPoint(c.getRow());
				route.addPoint(p);
			}
			c.close();
			pointsStmt.close();

			return route;
		} catch (Exception e) {
			throw new DatabaseException(getClass().getName() + ".read() " + e.getMessage());
		} finally {
		}
	}

	public void saveOrUpdate(Object o) throws DatabaseException {
		Route r = (Route) o;

		try {
			Statement stmt;
			if (r.getId() == null) {
				stmt = db.createStatement("INSERT INTO routes (name, description) VALUES (?1, ?2)");
			} else {
				stmt = db.createStatement("UPDATE routes SET name = ?1, description = ?2 WHERE id = ?3");

			}
			stmt.prepare();
			stmt.bind(1, r.getName());
			stmt.bind(2, r.getDescription());
			if (r.getId() != null) {
				stmt.bind(3, r.getId().longValue());
			}

			db.beginTransaction();

			stmt.execute();
			if (r.getId() == null) {
				r.setId(new Long(db.lastInsertedRowID()));
			}

			Vector v = r.getPoints();

			for (int i = 0; i < v.size(); ++i) {
				Point p = (Point) v.elementAt(i);
				p.setRouteId(r.getId());
				pointDao.saveOrUpdate(p);
			}

			db.commitTransaction();

			stmt.close();
		} catch (Exception e) {
			throw new DatabaseException(getClass().getName() + ".saveOrUpdate() " + e.getMessage());
		}
	}

	public void saveWithoutPoints(Route o) throws DatabaseException {
		Route r = (Route) o;

		try {
			Statement stmt;
			if (r.getId() == null) {
				stmt = db.createStatement("INSERT INTO routes (name, description) VALUES (?1, ?2)");
			} else {
				stmt = db.createStatement("UPDATE routes SET name = ?1, description = ?2 WHERE id = ?3");

			}
			stmt.prepare();
			stmt.bind(1, r.getName());
			stmt.bind(2, r.getDescription());
			if (r.getId() != null) {
				stmt.bind(3, r.getId().longValue());
			}
			stmt.execute();
			if (r.getId() == null) {
				r.setId(new Long(db.lastInsertedRowID()));
			}
			stmt.close();
		} catch (Exception e) {
			throw new DatabaseException(getClass().getName() + ".saveOrUpdate() " + e.getMessage());
		}
	}

	public void remove(Long objectId) throws DatabaseException {
		try {
			Statement points = db.createStatement("DELETE FROM points WHERE route_id = ?");
			Statement route = db.createStatement("DELETE FROM routes WHERE id = ?");
			points.prepare();
			route.prepare();

			points.bind(1, objectId.longValue());
			route.bind(1, objectId.longValue());

			db.beginTransaction();
			points.execute();
			route.execute();
			db.commitTransaction();

			points.close();
			route.close();
		} catch (Exception e) {
			throw new DatabaseException(getClass().getName() + ".remove() " + e.getMessage());
		}
	}

	public Vector getAllRoutes() throws DatabaseException {
		try {
			Statement routesStmt = db.createStatement("SELECT id FROM routes");
			routesStmt.prepare();

			db.beginTransaction();

			Cursor c = routesStmt.getCursor();

			Vector routes = new Vector();
			while (c.next()) {
				routes.addElement(read(new Long(c.getRow().getLong(0))));
			}

			db.commitTransaction();

			return routes;
		} catch (Exception ex) {
			throw new DatabaseException(ex);
		}
	}

	public PointDao getPointDao() {
		return pointDao;
	}

}
