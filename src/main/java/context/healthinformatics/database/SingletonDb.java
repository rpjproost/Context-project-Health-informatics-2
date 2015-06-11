package context.healthinformatics.database;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

/** Class for setting up database.
 * 
 * 
 *
 */
public final class SingletonDb {

	private static Db data;
	private static String path = "C:/db/";
	private static String dbName = "analyze";
	private static Logger log = Logger.getLogger(SingletonDb.class.getName());
	private static HashMap<String , Db> databases = new HashMap<String, Db>();

	/**
	 * 
	 */
	private SingletonDb() {
		
	}

	/**
	 * getter for the current database.
	 * @return database.
	 */
	public static Db getDb() {
		if (data != null) {
			return data;
		}
		if (databases.get(dbName) != null) {
			data = databases.get(dbName);
		} else {
			data = setDb();
			databases.put(dbName, data);
		}
		return data;
	}

	/** Sets up a database at default path for now.
	 * 
	 * @return database
	 */
	protected static Db setDb() {
		try {
			data = new Db(dbName, path);
		} catch (NullPointerException | SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * drop everything from the given database.
	 * @param data database to drop from.
	 */
	public static void dropAll(Db data) {
		boolean ignore = false;
		if (data == null) {
			return;
		}
		MergeTable mt = new MergeTable();
		if (data.isMerged()) {
			try {
				mt.dropView("workspace");
			} catch (SQLException e) {
				ignore = true;
			}
		}
		try {
		dropTables();
		} catch (SQLException e) {
			ignore = true;
		}
		if (ignore) {
			log.info("Exceptions where ignored in dropAll()");
		}
	}
	
	private static void dropTables() throws SQLException {
		boolean ignore = false;
		try {
			data.dropTable("result");
		} catch (SQLException e) {
			ignore = true;
		}
		Set<String> tables = new TreeSet<String>();
		tables.addAll(data.getTables().keySet());
		for (String s : tables) {
			try {
				data.dropTable(s);
			} catch (SQLException e) {
				ignore = true;
			}
		}
		if (ignore) {
			throw new SQLException("Exceptions where ignored.");
		}
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

	/**
	 * Update dbName for new project.
	 * @param param dbName.
	 */
	public static void update(String param) {
		dbName = param;
		data = null;
	}


}
