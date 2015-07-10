package context.healthinformatics.database;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

/** 
 * Class for setting up database.
 */
public final class SingletonDb {

	private static Db data;
	private static String path = "src";
	private static String dbName = "analyze";
	private static HashMap<String , Db> databases = new HashMap<String, Db>();

	/**
	 * Constructor for database.
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

	/** 
	 * Sets up a database at default path for now. 
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
	 * Drop everything from the given database.
	 * @param data database to drop from.
	 */
	public static void dropAll(Db data) {
		if (data == null) {
			return;
		}
		if (data.isMerged()) {
			try {
				data.dropView("workspace");
			} catch (SQLException e) {
				errorsThrown(true);
			}
		}
		dropTables();
	}
	
	/**
	 * @param b boolean the will be given back.
	 * @return the inserted boolean.
	 */
	private static boolean errorsThrown(boolean b) {
		return b;
	}
	
	/**
	 * Drops all tables that aren't needed anymore.
	 */
	private static void dropTables() {
		try {
			data.dropTable("result");
		} catch (SQLException e) {
			errorsThrown(true);
		}
		Set<String> tables = new TreeSet<String>();
		tables.addAll(data.getTables().keySet());
		for (String s : tables) {
			try {
				data.dropTable(s);
			} catch (SQLException e) {
				errorsThrown(true);
			}
		}
	}

	/**
	 * Sets path where database will be stored.
	 * @param p path directory.
	 */
	public static void setPath(String p) {
		path = p;
	}

	/** 
	 * Sets name of database.
	 * @param d name of database.
	 */
	public static void setDbName(String d) {
		dbName = d;
	}

	/** 
	 * Returns directory where database is stored.
	 * @return directory where database is stored.
	 */
	public static String getPath() {
		return path;
	}

	/** 
	 * Returns database name.
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
