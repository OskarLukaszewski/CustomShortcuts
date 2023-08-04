package custom_shortcuts.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlController {

	private final Connection conn;

	public SqlController(String url) {

		try {
			this.conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		String sql1 =
				"CREATE TABLE IF NOT EXISTS shortcuts (" +
				"name text PRIMARY KEY," +
				"parameters text," +
				"body text NOT NULL" +
				");";
		String sql2 =
				"CREATE TABLE IF NOT EXISTS mouse_position (" +
				"x_position double," +
				"y_position double" +
				");";
		String sql3 =
				"INSERT INTO mouse_position " +
				"SELECT 0, 0 " +
				"WHERE NOT EXISTS (SELECT * FROM mouse_position);";

		Statement stmt;
		try {
			stmt = this.conn.createStatement();
			stmt.addBatch(sql1);
			stmt.addBatch(sql2);
			stmt.addBatch(sql3);
			stmt.executeBatch();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public double[] getMousePosition() throws Exception {
		double[] result = new double[2];
		String sql = "SELECT x_position, y_position FROM mouse_position";
		Statement stmt;
		try {
			stmt = this.conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				result[0] = rs.getDouble("x_position");
				result[1] = rs.getDouble("y_position");
			}
		} catch (SQLException e) {
			throw new Exception("Couldn't get mouse position from database.");
		}
		return result;
	}

	public void updateMousePosition(double[] newMousePosition) throws Exception {
		String sql = "UPDATE mouse_position SET x_position = ?, y_position = ?;";
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement(sql);
			stmt.setDouble(1, newMousePosition[0]);
			stmt.setDouble(2, newMousePosition[1]);
			stmt.execute();
		} catch (SQLException e) {
			throw new Exception("Couldn't access database to update mouse position.");
		}
	}

	public void insertShortcut(String[] shortcut) throws Exception {
		String sql = "INSERT INTO shortcuts (" +
				"name," +
				"parameters," +
				"body" +
				")" +
				"VALUES (" +
				"?," +
				"?," +
				"?" +
				");";
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement(sql);
			stmt.setString(1, shortcut[0]);
			stmt.setString(2, shortcut[1]);
			stmt.setString(3, shortcut[2]);
			stmt.execute();
		} catch (SQLException e) {
			if (e.getMessage().contains("A PRIMARY KEY constraint failed")) {
				throw new Exception("This shortcut already exists.");
			}
			throw new RuntimeException(e);
		}
	}

	public String[] getShortcut(String name) throws Exception {
		String[] result = new String[3];
		String sql = "SELECT name, parameters, body FROM shortcuts WHERE name = ?;";
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement(sql);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result[0] = rs.getString("name");
				result[1] = rs.getString("parameters");
				result[2] = rs.getString("body");
			}
		} catch (SQLException e) {
			throw new Exception("Couldn't retrieve shortcut from database.");
		}
		return result;
	}

	public List<String[]> getAllShortcuts() throws Exception {
		List<String[]> result = new ArrayList<>();
		String sql = "SELECT * FROM shortcuts;";
		Statement stmt;
		try {
			stmt = this.conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				result.add(new String[] {
						rs.getString("name"),
						rs.getString("parameters"),
						rs.getString("body")});
			}
		} catch (SQLException e) {
			throw new Exception("Couldn't load all shortcuts.");
		}
		return result;
	}

	public List<String> getSuggestedShortcuts(String prompt) {
		List<String> result = new ArrayList<>();
		String sql = "SELECT * FROM shortcuts WHERE name LIKE ?;";
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement(sql);
			stmt.setString(1, "%"+prompt+"%");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				result.add(rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
		return result;
	}

	public void deleteShortcut(String name) throws Exception {
		String sql = "DELETE FROM shortcuts WHERE name = ?;";
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement(sql);
			stmt.setString(1, name);
			stmt.execute();
		} catch (SQLException e) {
			throw new Exception("Couldn't remove shortcut from database.");
		}
	}

	public void updateShortcut(String oldShortcutName, String[] newShortcut) throws Exception {
		String sql = "UPDATE shortcuts SET name = ?, parameters = ?, body = ? WHERE name = ?;";
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement(sql);
			stmt.setString(1, newShortcut[0]);
			stmt.setString(2, newShortcut[1]);
			stmt.setString(3, newShortcut[2]);
			stmt.setString(4, oldShortcutName);
			stmt.execute();
		} catch (SQLException e) {
			throw new Exception("Couldn't update shortcut in database.");
		}
	}

	public void close() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}