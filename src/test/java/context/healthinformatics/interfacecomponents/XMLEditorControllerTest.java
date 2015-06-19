package context.healthinformatics.interfacecomponents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import context.healthinformatics.gui.InputPage;
import context.healthinformatics.parser.Column;
import context.healthinformatics.writer.XMLDocument;

/**
 * Tests the xml editor controller.
 */
public class XMLEditorControllerTest {

	private XMLDocument documentOne;
	private XMLDocument documentTwo;
	private XMLEditorController controller;
	private String project = "testingXMLController";
	private ArrayList<XMLDocument> docs;
	private XMLEditor edit;
	private InputPage ip;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() {
		edit = mock(XMLEditor.class);
		ip = mock(InputPage.class);
		Column column = new Column(1, "comment", "String");
		ArrayList<Column> columns = new ArrayList<Column>();
		columns.add(column);
		documentOne = new XMLDocument("excel", "main", ";", "src/main", 1, 2, columns);
		documentTwo = new XMLDocument("text", "test", ",", "src/test", 2, 1, columns);
		docs = new ArrayList<XMLDocument>();
		docs.add(documentOne);
		controller = new XMLEditorController();
		controller.setProject(project);
		controller.setDocumentsInProject(docs);
		when(ip.findFolderProject(project)).thenReturn(-1);
	}

	/**
	 * Tests the selected documents method.
	 */
	@Test
	public void testGetSelectedDocs() {
		XMLEditorController controller = new XMLEditorController();
		assertNull(controller.getSelectedDocs());
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
		controller.addDocument(documentTwo);
		controller.selectDocument("src/main");
		controller.selectDocument("src/test");
		assertEquals(2, controller.getSelectedDocs().size());
		controller.selectDocument("src/main");
		assertEquals(2, controller.getSelectedDocs().size());
	}
	
	/**
	 * Tests the select document when this document doesn't exist.
	 */
	@Test
	public void testSelectDocumentMissing() {
		controller.selectDocument("src/main");
		controller.selectDocument("src/test");
		assertEquals(1, controller.getSelectedDocs().size());
	}
	
	/**
	 * Test the deselect document correctly.
	 */
	@Test
	public void testDeselectDocumentCorrect() {
		testSelectDocumentCorrect();
		controller.deselectDocument("src/main");
		assertEquals(0, controller.getSelectedDocs().size());
	}
	
	/**
	 * Tests the deselect method when the document doesn't exist.
	 */
	@Test
	public void testDeselectDocumentMissing() {
		testSelectDocumentCorrect();
		controller.deselectDocument("src/test");
		assertEquals(1, controller.getSelectedDocs().size());
	}
	
	/**
	 * Tests deselect method when the file exists but not yet in the selected files list.
	 */
	@Test
	public void testDeselectDocumentNotInSelected() {
		testSelectDocumentCorrect();
		controller.addDocument(documentTwo);
		controller.deselectDocument("src/test");
		assertEquals(1, controller.getSelectedDocs().size());
	}
	
	/**
	 * Tests the document with only the end of the path string.
	 */
	@Test
	public void testGetDocumentWithPartofPath() {
		XMLDocument test = controller.getDocumentWithPartofPath(project, "main");
		assertEquals("main", test.getDocName());
	}
	
	/**
	 * Tests when it doesn't exists with only the end of the path string.
	 */
	@Test
	public void testGetDocumentWithPartofPathWrong() {
		assertNull(controller.getDocumentWithPartofPath(project, "test"));
	}
	
	/**
	 * Tests the get file location if it is correct.
	 */
	@Test
	public void testGetFileLocation() {
		String actual = controller.getFileLocation();
		String expected = "src/main/data/savedXML/" + project + ".xml";
		assertEquals(expected, actual);
	}
	
	/**
	 * Tests the get project documents method.
	 */
	@Test
	public void testGetProjectDocuments() {
		ArrayList<XMLDocument> actual = controller.getProjectDocuments();
		ArrayList<XMLDocument> expected = new ArrayList<XMLDocument>();
		expected.add(documentOne);
		assertEquals(expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++) {
			assertEquals(expected.get(i).getPath(), actual.get(i).getPath());
			assertEquals(expected.get(i).getDocName(), actual.get(i).getDocName());
		}
	}
	
	/**
	 * Tests the break down xml docs into names.
	 */
	@Test
	public void testBreakDownXMLDocumentsIntoNames() {
		ArrayList<String> actual = controller.breakDownXMLDocumentsIntoNames(docs);
		ArrayList<String> expected = new ArrayList<String>();
		expected.add(project);
		expected.add("main");
		for (int i = 0; i < actual.size(); i++) {
			assertEquals(expected.get(i), actual.get(i));
		}
	}
	
	/**
	 * Tests save and remove methods.
	 */
	@Test
	public void testSaveAndRemoveCorrect() {
		controller.save();
		File test = new File(controller.getFileLocation());
		assertTrue(test.exists());
		controller.removeProject(project);
		assertFalse(test.exists());
	}
	
	/**
	 * Tests if the project will be not deleted if the file can't be deleted.
	 */
	@Test
	public void testRemoveIfNoFileExists() {
		assertEquals(1, controller.getProjectDocuments().size());
		controller.removeProject(project);
		assertEquals(1, controller.getProjectDocuments().size());
	}
	
	/**
	 * Tests the get indexes of selected files correctly.
	 */
	@Test
	public void testGetIndexesOfSelectedFiles() {
		testSelectDocumentCorrect();
		ArrayList<Integer> actual = controller.getIndexesOfSelectedFiles(docs);
		assertEquals(1, actual.size());
	}
	
	/**
	 * Tests if there is no equality between the project file and selected files.
	 */
	@Test
	public void testGetIndexesOfSelectedFilesNull() {
		testSelectDocumentCorrect();
		ArrayList<XMLDocument> test = new ArrayList<XMLDocument>();
		test.add(documentTwo);
		ArrayList<Integer> actual = controller.getIndexesOfSelectedFiles(test);
		assertEquals(0, actual.size());
	}
	
	/**
	 * Tests some null catches.
	 */
	@Test
	public void testSomeNullCatches() {
		controller.save();
		controller.removeProject(project);
		controller.addDocument(documentTwo);
		assertEquals(1, controller.getProjectDocuments().size());
		controller.setProject("ADMIRE");
		assertNull(controller.getDocument("src/main"));
		assertNull(controller.getDocumentWithPartofPath(project, "main"));
	}
	
	/**
	 * Verifies if some methods will be run when you call load project.
	 */
	@Test
	public void testLoadProject() {
		controller.loadProject(edit);
		verify(edit).emptyEditor();
		verify(edit).addXMLDocumentToContainerScrollPanel(documentOne);
	}
	
	/**
	 * Tests if the find folder project will be called.
	 */
	@Test
	public void testUpdateProject() {
		controller.updateDocuments(ip, docs);
		verify(ip).findFolderProject(project);
	}
	
}
