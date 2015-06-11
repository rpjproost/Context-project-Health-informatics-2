package context.healthinformatics.database;

import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

/** Class for setting up database.
 * 
 * 
 *
 */
public final class SingletonDb {

	private static Db data = setDb();
	private static String path;
	private static String dbName;

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
	public static Db setDb() {
		if (data == null) {
			try {
				data = new Db("analyze", "C:/db/");
			} catch (NullPointerException | SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			try {
				dropAll(data);
				data.closeConnection();
				data = new Db(dbName, path);
			} catch (NullPointerException | SQLException e) {
				e.printStackTrace();
			}
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
			mt.dropView("workspace");
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
		
		
	}


}
