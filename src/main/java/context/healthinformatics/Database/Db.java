package context.healthinformatics.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Rick, 30 apr 2015.
 *
 */
public class Db {
	
	private String db;
	
	/**
	 * Sets up connection.
	 * @throws SQLException 
	 */
	public void setupConn() throws SQLException {
		//setDb("jdbc:derby:C:/db/analyze;create=true");
		Connection conn = DriverManager.getConnection(db);
		//Check connection
		if (conn != null) {
			System.out.println("Connected to database");
		}
		else {
			System.out.println("Could not connect to database");
		}
	}

	/**
	 * 
	 * @return db string.
	 */
	public String getDb() {
		return db;
	}

	/**
	 * 
	 * @param path specifies path to db.
	 */
	public void setDb(String path) {
		String prefix = "jdbc:derby:";
		String suffix = ";create=true";
		String temp = prefix + path + suffix;
		this.db = temp;
	}

}
