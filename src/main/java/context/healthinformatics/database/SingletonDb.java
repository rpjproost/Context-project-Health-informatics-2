package context.healthinformatics.database;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

/** Class for setting up database.
 * 
 * 
 *
 */
public final class SingletonDb {

	private static Db data;
	private static String path;
	private static String dbName;
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
		if (!databases.containsKey(dbName)) {
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
		if (data == null) {
			return;
		}
		MergeTable mt = new MergeTable();
		if (data.isMerged()) {
			try {
				mt.dropView("workspace");
			} catch (SQLException e) {
			//do nothing for tests.
			}
		}
		try {
			data.dropTable("result");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		Set<String> tables = new TreeSet<String>();
		tables.addAll(data.getTables().keySet());
		for (String s : tables) {
			try {
				data.dropTable(s);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
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

	public static void update(String param) {
		dbName = param;
		data = null;
	}


}
