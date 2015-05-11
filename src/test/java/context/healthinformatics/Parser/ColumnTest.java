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
	private static final int COL_ID = 12;
	
	/**
	 * Create for each test a new Column to test with.
	 */
	@Before
	public void createColumn() {
		col = new Column(COL_ID, "Column", "List");
	}

	/**
	 * Test the getter of column id/number.
	 */
	@Test
	public void testGetColumnNumber() {
		assertEquals(col.getColumnNumber(), COL_ID);
	}

	/**
	 * Test the setter of column id/number.
	 */
	@Test
	public void testSetColumnNumber() {
		final int wrongID = 55;
		col.setColumnNumber(wrongID);
		assertEquals(col.getColumnNumber(), wrongID);
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
		assertEquals(col.getColumnType(), "String");
	}

	/**
	 * Test all possibilities of the equals method.
	 */
	@Test
	public void testEqualsObject() {
		Column wrongNumber = new Column(-1, "Column", "List");
		Column noName = new Column(COL_ID, null, "List");
		Column noType = new Column(COL_ID, "Column", null);
		Column good = new Column(COL_ID, "Column", "List");
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

}
