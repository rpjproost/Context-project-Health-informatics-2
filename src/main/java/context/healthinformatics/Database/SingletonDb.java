package context.healthinformatics.Database;

import java.sql.SQLException;

/** Class for setting up database.
 * 
 * 
 *
 */
public final class SingletonDb {
	
	private static Db data = setDb();
	
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
	
	private static Db setDb() {
		try {
			return new Db("analyze", "C:/db/");
		} catch (NullPointerException | SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

}
