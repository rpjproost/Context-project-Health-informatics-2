package context.healthinformatics.interfacecomponents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

import context.healthinformatics.parser.Column;

/**
 * Tests the column field container.
 */
public class ColumnFieldContainerTest {

	private ColumnFieldContainer correctContainer;
	private ColumnFieldContainer dateContainerNoType;
	private ColumnFieldContainer dateContainerWithType;
	private ColumnFieldContainer emptyContainer;

	/**
	 * Set up some columns to work with.
	 */
	@Before
	public void setUp()  {
		correctContainer = new ColumnFieldContainer(new Column(2, "value", "String"));
		dateContainerNoType = new ColumnFieldContainer(new Column(1, "date", "Date"));
		Column c = new Column(2, "date", "Date");
		c.setDateType("ddMMyy");
		dateContainerWithType =  new ColumnFieldContainer(c);
		emptyContainer = new ColumnFieldContainer(new Column(-1, "", ""));
	}

	/**
	 * Test method for has date type.
	 */
	@Test
	public void testHasDateType() {
		assertTrue(dateContainerWithType.hasDateType());
		assertFalse(emptyContainer.hasDateType());
	}

	/**
	 * Test method for get the column.
	 */
	@Test
	public void testGetColumn() {
		Column actual = correctContainer.getColumn();
		assertTrue(actual.equals(new Column(2, "value", "String")));
		actual = dateContainerNoType.getColumn();
		assertTrue(actual.equals(new Column(1, "date", "Date")));
		assertFalse(actual.equals(new Column(1, "date", "String")));
	}

	/**
	 * Test method for checking for empty fields.
	 */
	@Test
	public void testCheckIfHasEmptyFields() {
/*		ColumnFieldContainer dateContainerNoID = new ColumnFieldContainer(
				new Column(-1, "datum", "Date"));
		ColumnFieldContainer dateContainerNoName = new ColumnFieldContainer(
				new Column(2, "", "Date"));*/
/*		assertTrue(emptyContainer.checkIfHasEmptyFields());
		assertFalse(dateContainerWithType.checkIfHasEmptyFields());
		assertTrue(dateContainerNoType.checkIfHasEmptyFields());
		assertFalse(correctContainer.checkIfHasEmptyFields());
		assertTrue(dateContainerNoID.checkIfHasEmptyFields());
		assertTrue(dateContainerNoName.checkIfHasEmptyFields());*/
	}

	/**
	 * Test method for set the panels.
	 */
	@Test
	public void testSetPanel() {
		assertFalse(dateContainerWithType.hasPanel());
		JPanel actual = new JPanel();
		dateContainerWithType.setPanel(actual);
		assertTrue(dateContainerWithType.hasPanel());
		assertEquals(actual, dateContainerWithType.getPanel());
	}
	
	/**
	 * Test the sel column method.
	 */
	@Test
	public void testSetColumnType() {
		testSetPanel();
		JPanel actual = new JPanel();
		dateContainerWithType.setDateTypePanel(actual);
		dateContainerWithType.setColumnType(2);
		assertTrue(dateContainerWithType.getDateTypePanel().isVisible());
		dateContainerWithType.setColumnType(0);
		assertFalse(dateContainerWithType.getDateTypePanel().isVisible());
		dateContainerWithType.setColumnType(2);
		assertTrue(dateContainerWithType.getDateTypePanel().isVisible());
	}

	/**
	 * Test method for is column id empty.
	 */
	@Test
	public void testIsColumnIDEmpty() {
/*		assertTrue(dateContainerNoType.isColumnDateTypeEmpty());
		assertFalse(dateContainerWithType.isColumnDateTypeEmpty());
		assertFalse(correctContainer.isColumnDateTypeEmpty());*/
	}

}
