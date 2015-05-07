package context.healthinformatics.Parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ColumnTest {
	
	Column col;
	
	@Before
	public void createColumn() {
		col = new Column(12, "Column", "List");
	}

	@Test
	public void testGetColumnNumber() {
		assertEquals(col.getColumnNumber(), 12);
	}

	@Test
	public void testSetColumnNumber() {
		col.setColumnNumber(55);
		assertEquals(col.getColumnNumber(), 55);
	}

	@Test
	public void testGetColumnName() {
		assertEquals(col.getColumnName(), "Column");
	}

	@Test
	public void testSetColumnName() {
		col.setColumnName("Row");
		assertEquals(col.getColumnName(), "Row");
	}

	@Test
	public void testGetColumnType() {
		assertEquals(col.getColumnType(), "List");
	}

	@Test
	public void testSetColumnType() {
		col.setColumnType("String");
		assertEquals(col.getColumnType(), "String");
	}

	@Test
	public void testEqualsObject() {
		Column wrongNumber = new Column(-1, "Column", "List");
		Column noName = new Column(12, null, "List");
		Column noType = new Column(12, "Column", null);
		Column good = new Column(12, "Column", "List");
		String wrongObject = "";
		
		assertFalse(col.equals(null));
		assertFalse(col.equals(wrongNumber));
		assertFalse(col.equals(noType));
		assertFalse(col.equals(noName));
		assertFalse(col.equals(wrongObject));
		
		assertTrue(col.equals(good));
	}

	@Test
	public void testToString() {
		assertEquals(col.toString(), "<12 Column List>");
	}

}
