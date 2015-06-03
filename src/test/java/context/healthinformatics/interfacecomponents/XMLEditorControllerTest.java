package context.healthinformatics.interfacecomponents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import context.healthinformatics.parser.Column;
import context.healthinformatics.writer.XMLDocument;

/**
 * Tests the xml editor controller.
 */
public class XMLEditorControllerTest {

	private XMLDocument documentOne;
	private XMLDocument documentTwo;
	private XMLEditorController controller;
	private String project = "ADMIRE";
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() {
		Column column = new Column(1, "comment", "String");
		ArrayList<Column> columns = new ArrayList<Column>();
		columns.add(column);
		documentOne = new XMLDocument("excel", "main", ";", "src/main", 1, 2, columns);
		documentTwo = new XMLDocument("text", "test", ",", "src/test", 2, 1, columns);
		ArrayList<XMLDocument> docs = new ArrayList<XMLDocument>();
		docs.add(documentOne);
		controller = new XMLEditorController(project, docs);
	}

	/**
	 * Tests the selected documents method.
	 */
	@Test
	public void testGetSelectedDocs() {
		XMLEditorController controller = new XMLEditorController();
		assertEquals(0, controller.getSelectedDocs().size());
	}

	/**
	 * Tests the select document when everything is used normally.
	 */
	@Test
	public void testSelectDocumentCorrect() {
		controller.selectDocument("src/main");
		assertEquals(1, controller.getSelectedDocs().size());
	}
	
	/**
	 * Tests the select document method when there is already that document.
	 */
	@Test
	public void testSelectDocumentDuplicate() {
		XMLEditorController controller = new XMLEditorController();
		String project = "controllerTest";
		controller.setProject(project);
		controller.addDocument(documentOne);
		controller.addDocument(documentTwo);
		controller.selectDocument("src/main");
		controller.selectDocument("src/test");
		assertEquals(2, controller.getSelectedDocs().get(project).size());
		controller.selectDocument("src/main");
		assertEquals(2, controller.getSelectedDocs().get(project).size());
	}
	
	/**
	 * Tests the select document when this document doesn't exist.
	 */
	@Test
	public void testSelectDocumentMissing() {
		controller.selectDocument("src/main");
		controller.selectDocument("src/test");
		assertEquals(1, controller.getSelectedDocs().get(project).size());
	}
	
	/**
	 * Test the deselect document correctly.
	 */
	@Test
	public void testDeselectDocumentCorrect() {
		testSelectDocumentCorrect();
		controller.deselectDocument("src/main");
		assertEquals(0, controller.getSelectedDocs().get(project).size());
	}
	
	/**
	 * Tests the deselect method when the document doesn't exist.
	 */
	@Test
	public void testDeselectDocumentMissing() {
		testSelectDocumentCorrect();
		controller.deselectDocument("src/test");
		assertEquals(1, controller.getSelectedDocs().get(project).size());
	}
	
	/**
	 * Tests deselect method when the file exists but not yet in the selected files list.
	 */
	@Test
	public void testDeselectDocumentNotInSelected() {
		testSelectDocumentCorrect();
		controller.addDocument(documentTwo);
		controller.deselectDocument("src/test");
		assertEquals(1, controller.getSelectedDocs().get(project).size());
	}
	
	/**
	 * Tests the document with only the end of the path string.
	 */
	@Test
	public void testGetDocumentWithPartofPath() {
		XMLDocument test = controller.getDocumentWithPartofPath("main");
		assertEquals("main", test.getDocName());
	}
	
	/**
	 * Tests when it doesn't exists with only the end of the path string.
	 */
	@Test
	public void testGetDocumentWithPartofPathWrong() {
		assertNull(controller.getDocumentWithPartofPath("test"));
	}

}
