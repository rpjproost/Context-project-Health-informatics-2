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
		Column wrongNumber = new Column(-1, "Column", "List");
		Column noName = new Column(TWELVE, null, "List");
		Column noType = new Column(TWELVE, "Column", null);
		Column good = new Column(TWELVE, "Column", "List");
		String wrongObject = "";
		
		assertFalse(col.equals(null));
		assertFalse(col.equals(wrongNumber));
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
	 * Test to convert date.
	 */
	@Test
	public void testDateConversion() {
		col.setDateType("dd-MM-yyyy");
		assertEquals(col.convertDate("28-10-1992"), "1992-10-28");
	}

}