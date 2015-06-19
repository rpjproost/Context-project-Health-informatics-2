package context.healthinformatics.interfacecomponents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import context.healthinformatics.parser.Column;
import context.healthinformatics.writer.XMLDocument;

/**
 * Test the analyse pop up class.
 */
public class GoToAnalysePopupTest {
	
	private XMLDocument xmlDocFull;
	private GoToAnalyzePopup popup;
	private GoToAnalyzePopupController control;

	/**
	 * Creates files and docs to work with.
	 */
	@Before
	public void setUp() {
		control = mock(GoToAnalyzePopupController.class);
		ArrayList<Column> cols = new ArrayList<Column>();
		Column col = new Column(2, "date", "Date");
		col.setColumnType("dd-MM-yyyy");
		cols.add(col);
		xmlDocFull = new XMLDocument("excel", "admire", ";", "src/main", 1, 1,
				cols);
		ArrayList<XMLDocument> list = new ArrayList<XMLDocument>();
		list.add(xmlDocFull);
		popup = new GoToAnalyzePopup(list, control);
	}

	/**
	 * Test go to front method.
	 */
	@Test
	public void testGetToFront() {
		popup.getToFront();
		assertTrue(popup.isVisible());
	}

	/**
	 * Set the filters and verify if methods are called.
	 */
	@Test
	public void testSetFilters() {
		String[] test = new String[] {"admire", "date", "2014"};
		ArrayList<String[]> filter = new ArrayList<String[]>();
		filter.add(test);
		popup.setFilters(filter);
		popup.addEmptyFilter();
		assertEquals(2, popup.getAllFilterValues().length);
		verify(control).handleSpecifiedFilter(popup.getAllFilterValues());
	}
	
	/**
	 * Close the pop up.
	 */
	@After
	public void closePopUp() {
		popup.closePopUp();
	}

}
