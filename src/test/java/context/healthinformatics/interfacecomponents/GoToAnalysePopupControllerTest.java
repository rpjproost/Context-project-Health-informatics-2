package context.healthinformatics.interfacecomponents;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import context.healthinformatics.parser.Column;
import context.healthinformatics.writer.XMLDocument;

/**
 * Test the pop up controller.
 */
public class GoToAnalysePopupControllerTest {

	private InputPageComponents components;
	private GoToAnalyzePopupController controller;
	private String projectName = "TestGoToAnalyseTest";

	/**
	 * Set up the objects to work with.
	 */
	@Before
	public void setUp() {
		components = mock(InputPageComponents.class);
		controller = new GoToAnalyzePopupController(components);
	}

	/**
	 * Tests the entire classes.
	 */
	@Test
	public void testCreatePopup() {
		ArrayList<Column> cols = new ArrayList<Column>();
		Column col = new Column(2, "date", "Date");
		col.setColumnType("dd-MM-yyyy");
		cols.add(col);
		XMLDocument xmlDocFull = new XMLDocument("excel", "admire", ";", "src/main", 1, 1,
				cols);
		ArrayList<XMLDocument> list = new ArrayList<XMLDocument>();
		list.add(xmlDocFull);
		controller.createPopup(list, projectName);
		String[] filter = new String[]{"admire", "date", "2014"};
		controller.handleSpecifiedFilter(filter);
		verify(components).handleSpecifiedFilter(filter);
	}
	
	/**
	 * Remove the file that is made by this class.
	 */
	@After
	public void clear() {
		File file = new File("src/main/data/savedFilters/" + projectName + ".txt");
		file.delete();
		controller.close();
	}
}
