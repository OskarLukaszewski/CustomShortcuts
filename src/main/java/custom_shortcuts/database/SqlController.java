package custom_shortcuts.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static custom_shortcuts.gui.main_window.CustomShortcuts.getDataFolder;

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
				"body text," +
				"includes_picture text," +
				"path_to_picture text" +
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
		String sql4 =
				"CREATE TABLE IF NOT EXISTS version (" +
				"current_version text" +
				");";
		Statement stmt;
		try {
			stmt = this.conn.createStatement();
			stmt.addBatch(sql1);
			stmt.addBatch(sql2);
			stmt.addBatch(sql3);
			stmt.addBatch(sql4);
			stmt.executeBatch();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		if (!isCurrentVersion()) {
			updateToCurrentVersion();
		}
	}

	private boolean isCurrentVersion() {
		String sql =
				"SELECT EXISTS (SELECT 1 FROM version);";
		Statement stmt;
		try {
			stmt = this.conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			return rs.getInt(1) == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void updateToCurrentVersion() {
		String sql1 = "ALTER TABLE shortcuts ADD COLUMN includes_picture NOT NULL DEFAULT 'false';";
		String sql2 = "ALTER TABLE shortcuts ADD COLUMN path_to_picture text;";
		String sql3 = "INSERT INTO version SELECT '2.0' WHERE NOT EXISTS (SELECT * FROM version);";
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

	public double[] getMousePosition() throws SqlControllerException {
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
			throw new SqlControllerException("Couldn't get mouse position from database.");
		}
		return result;
	}

	public void updateMousePosition(double[] newMousePosition) throws SqlControllerException {
		String sql = "UPDATE mouse_position SET x_position = ?, y_position = ?;";
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement(sql);
			stmt.setDouble(1, newMousePosition[0]);
			stmt.setDouble(2, newMousePosition[1]);
			stmt.execute();
		} catch (SQLException e) {
			throw new SqlControllerException("Couldn't access database to update mouse position.");
		}
	}

	public void insertShortcut(String[] shortcut) throws SqlControllerException {
		String sql = "INSERT INTO shortcuts (" +
				"name," +
				"parameters," +
				"body," +
				"includes_picture" +
				")" +
				"VALUES (" +
				"?," +
				"?," +
				"?," +
				"'false'" +
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
				throw new SqlControllerException("This shortcut already exists.");
			}
			throw new RuntimeException(e);
		}
	}

	public void addPictureToShortcut(String name, String path) throws SqlControllerException {
		String sql = "UPDATE shortcuts " +
				"SET includes_picture = 'true', path_to_picture = ? " +
				"WHERE name = ?;";
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement(sql);
			stmt.setString(1, path);
			stmt.setString(2, name);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SqlControllerException("The provided picture couldn't be added to the database.");
		}
	}

	public void removePictureFromShortcut(String name) throws SqlControllerException {
		String sql = "UPDATE shortcuts SET includes_picture = 'false', path_to_picture = NULL WHERE name = ?;";
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement(sql);
			stmt.setString(1, name);
			stmt.execute();
		} catch (SQLException e) {
			throw  new SqlControllerException("The picture couldn't be deleted from the database.");
		}
	}

	public void changePictureInShortcut(String name, String path) throws SqlControllerException {
		String sql = "UPDATE shortcuts " +
				"SET path_to_picture = ? " +
				"WHERE name = ?;";
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement(sql);
			stmt.setString(1, path);
			stmt.setString(2, name);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SqlControllerException("The picture couldn't be changed.");
		}
	}

	public String[] getShortcut(String name) throws SqlControllerException {
		String[] result = new String[5];
		String sql = "SELECT name, parameters, body, includes_picture, path_to_picture " +
				"FROM shortcuts " +
				"WHERE name = ?;";
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement(sql);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result[0] = rs.getString("name");
				result[1] = rs.getString("parameters");
				result[2] = rs.getString("body");
				result[3] = rs.getString("includes_picture");
				result[4] = rs.getString("path_to_picture");
			}
		} catch (SQLException e) {
			throw new SqlControllerException("Couldn't retrieve the shortcut from the database.");
		}
		if (result[0] == null) {
			throw new SqlControllerException("Couldn't retrieve the shortcut from the database.");
		}
		return result;
	}

	public List<String[]> getAllShortcuts() throws SqlControllerException {
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
						rs.getString("body"),
						rs.getString("includes_picture"),
						rs.getString("path_to_picture")});
			}
		} catch (SQLException e) {
			throw new SqlControllerException("Couldn't load all shortcuts.");
		}
		return result;
	}

	public List<String> getSuggestedShortcuts(String prompt) {
		List<String> result = new ArrayList<>();
		String sql = "SELECT name FROM shortcuts WHERE name LIKE ?;";
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement(sql);
			stmt.setString(1, "%"+prompt+"%");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				result.add(rs.getString("name"));
			}
		} catch (SQLException e) {
			return new ArrayList<>();
		}
		return result;
	}

	public void deleteShortcut(String name) throws SqlControllerException, DataFolderException {
		String sql1 = "SELECT includes_picture, path_to_picture FROM shortcuts WHERE name = ?;";
		PreparedStatement stmt1;
		String[] result = new String[2];
		try {
			stmt1 = this.conn.prepareStatement(sql1);
			stmt1.setString(1, name);
			ResultSet rs = stmt1.executeQuery();
			while(rs.next()) {
				result[0] = rs.getString("includes_picture");
				result[1] = rs.getString("path_to_picture");
			}
		} catch (SQLException e) {
			throw new SqlControllerException("Couldn't remove shortcut from database.");
		}

		String sql2 = "DELETE FROM shortcuts WHERE name = ?;";
		PreparedStatement stmt2;
		try {
			stmt2 = this.conn.prepareStatement(sql2);
			stmt2.setString(1, name);
			stmt2.execute();
			if (result[0].equals("true")) {
				getDataFolder().deletePicture(result[1]);
			}
		} catch (SQLException e) {
			throw new SqlControllerException("Couldn't remove shortcut from database.");
		}
	}

	public void updateShortcut(String oldShortcutName, String[] newShortcut) throws SqlControllerException {
		String sql = "UPDATE shortcuts " +
				"SET name = ?, parameters = ?, body = ? " +
				"WHERE name = ?;";
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement(sql);
			stmt.setString(1, newShortcut[0]);
			stmt.setString(2, newShortcut[1]);
			stmt.setString(3, newShortcut[2]);
			stmt.setString(4, oldShortcutName);
			stmt.execute();
		} catch (SQLException e) {
			throw new SqlControllerException("Couldn't update shortcut in database.");
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