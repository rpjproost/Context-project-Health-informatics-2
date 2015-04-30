package context.healthinformatics;

import java.sql.SQLException;

import context.healthinformatics.Database.Db;
import context.healthinformatics.Parser.*;

public class Main {

	public static void main(String[] args) {
		//XMLParser xmlp = new XMLParser("src/main/data/ExampleStatSensor.xml");
		//xmlp.parse();
		Db database = new Db();
		database.setupConn();
	}
}
