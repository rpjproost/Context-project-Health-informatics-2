package context.healthinformatics.Database;

import java.sql.SQLException;

/** Class for setting up database.
 * 
 * 
 *
 */
public final class SingletonDb {
	
	private static Db data = setDb();
	private static String path = "C:/db/";
	private static String dbName = "analyze";
	
	/**
	 * 
	 */
	private SingletonDb() {
		
	}
	
	/**
	 * 
	 * @return database.
	 */
	public static Db getDb() {
		return data;
	}
	
	/** Sets up a database at default path for now.
	 * 
	 * @return database
	 */
	private static Db setDb() {
		try {
			return new Db("analyze", "C:/db/");
		} catch (NullPointerException | SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/**Sets path where database will be stored.
	 * 
	 * @param p path directory.
	 */
	public static void setPath(String p) {
		path = p;
	}
	
	/** Sets name of database.
	 * 
	 * @param d name of database.
	 */
	public static void setDbName(String d) {
		dbName = d;
	}
	
	/** Returns directory where database is stored.
	 * 
	 * @return directory where database is stored.
	 */
	public static String getPath() {
		return path;
	}
	
	/** Returns database name.
	 * 
	 * @return database name.
	 */
	public static String getDbName() {
		return dbName;
	}

}
