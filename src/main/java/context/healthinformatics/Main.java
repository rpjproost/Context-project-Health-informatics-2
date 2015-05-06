package context.healthinformatics;

import java.io.IOException;

import context.healthinformatics.Database.Db;
import context.healthinformatics.Parser.*;

public class Main {

	public static void main(String[] args) throws IOException {
		Db test = new Db("anal", null);
		String[] col = new String[2];
		String[] t = new String[2];
		col[0] = "testC1";
		col[1] = "testC2";
		t[0] = "int";
		t[1] = "int";
		//test.createTable("test", col, t);
		String[] v = new String[2];
		v[0] = "7";
		v[1] = "4";
		//test.insert("test", v);
		//test.select("test", "testC2");
		//test.dropTable("test");
		test.dropDatabase("anal");
	}
}