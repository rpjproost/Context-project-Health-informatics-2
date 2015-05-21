package context.healthinformatics.Parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

/**
 * Test all functions of the Column class.
 */
public class ColumnTest {
	
	private Column col;
	private static final int TWELVE = 12;
	private static final int FITYFIVE = 55;
	
	/**
	 * Create for each test a new Column to test with.
	 */
	@Before
	public void createColumn() {
		col = new Column(TWELVE, "Column", "List");
	}

	/**
	 * Test the getter of column id/number.
	 */
	@Test
	public void testGetColumnNumber() {
		assertEquals(col.getColumnNumber(), TWELVE);
	}

	/**
	 * Test the setter of column id/number.
	 */
	@Test
	public void testSetColumnNumber() {
		col.setColumnNumber(FITYFIVE);
		assertEquals(col.getColumnNumber(), FITYFIVE);
	}

	/**
	 * Test the getter of column name.
	 */
	@Test
	public void testGetColumnName() {
		assertEquals(col.getColumnName(), "Column");
	}

	/**
	 * Test the setter of column name.
	 */
	@Test
	public void testSetColumnName() {
		col.setColumnName("Row");
		assertEquals(col.getColumnName(), "Row");
	}

	/**
	 * Test the getter of the column type.
	 */
	@Test
	public void testGetColumnType() {
		assertEquals(col.getColumnType(), "List");
	}

	/**
	 * Check if the set of type works.
	 */
	@Test
	public void testSetColumnType() {
		col.setColumnType("String");
		assertEquals(col.getColumnType(), "varchar(150)");
	}

	/**
	 * Test all possibilities of the equals method.
	 */
	@Test
	public void testEqualsObject() {
		Column noName = new Column(TWELVE, null, "List");
		Column noType = new Column(TWELVE, "Column", null);
		Column good = new Column(TWELVE, "Column", "List");
		String wrongObject = "";
		
		assertFalse(col.equals(null));
		assertFalse(col.equals(noType));
		assertFalse(col.equals(noName));
		assertFalse(col.equals(wrongObject));
		
		assertTrue(col.equals(good));
	}

	/**
	 * Test the toString method of Column.
	 */
	@Test
	public void testToString() {
		assertEquals(col.toString(), "Column<12 Column List>");
	}
	
	/**
	 * Sets and gets dateType.
	 */
	@Test
	public void testSetDateType() {
		col.setDateType("dd-MM-yyyy");
		assertEquals(col.getDateType(), "dd-MM-yyyy");
	}
	
	/**
	 * tests the if statement = date.
	 */
	@Test
	public void setColumnTypeDate() {
		col.setColumnType("date");
		assertEquals(col.getColumnType(), "DATE");
	}
	
	/**
	 * Tests hashcode if columnName is null.
	 */
	@Test
	public void testHashCodeNull() {
		Column col = new Column(1, null, null);
		final int hash = 924482;
		assertEquals(col.hashCode(), hash);
	}
	
	/**
	 * Tests every if statement in hashcode.
	 */
	@Test
	public void testHashCodeName() {
		Column col = new Column(1, "test", "date");
		col.setDateType("dd-MM-yyyy");
		final int prime = 31;
		int name = "test".hashCode();
		int type = "DATE".hashCode();
		int date = "dd-MM-yyyy".hashCode();
		int res = 1;
		res = prime * res;
		res += name;
		res = prime * res + 1;
		res = prime * res;
		res += type;
		res = prime * res;
		res += date;
		assertEquals(col.hashCode(), res);
	}

}