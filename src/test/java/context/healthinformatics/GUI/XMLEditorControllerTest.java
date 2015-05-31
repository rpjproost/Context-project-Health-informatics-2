package context.healthinformatics.GUI;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import context.healthinformatics.Parser.Column;
import context.healthinformatics.Writer.XMLDocument;

public class XMLEditorControllerTest {
	
	private XMLDocument documentOne;
	private XMLDocument documentTwo;
	private XMLEditorController controller;

	@Before
	public void setup() {
		Column column = new Column(1, "comment", "String");
		ArrayList<Column> columns = new ArrayList<Column>();
		columns.add(column);
		documentOne = new XMLDocument("excel", "main", ";", "src/main", 1, 2, columns);
		documentTwo = new XMLDocument("text", "test", ",", "src/test", 2, 1, columns);
		ArrayList<XMLDocument> docs = new ArrayList<XMLDocument>();
		docs.add(documentOne);
		controller = new XMLEditorController(docs);
	}

	@Test
	public void testGetSelectedDocs() {
		XMLEditorController controller = new XMLEditorController();
		assertEquals(0, controller.getSelectedDocs().size());
	}

	@Test
	public void testSelectDocumentCorrect() {
		controller.selectDocument("src/main");
		assertEquals(1, controller.getSelectedDocs().size());
	}
	
	@Test
	public void testSelectDocumentDuplicate() {
		XMLEditorController controller = new XMLEditorController();
		controller.addDocument(documentOne);
		controller.addDocument(documentTwo);
		controller.selectDocument("src/main");
		controller.selectDocument("src/test");
		assertEquals(2, controller.getSelectedDocs().size());
		controller.selectDocument("src/main");
		assertEquals(2, controller.getSelectedDocs().size());
	}
	
	@Test
	public void testSelectDocumentMissing() {
		controller.selectDocument("src/test");
		assertEquals(0, controller.getSelectedDocs().size());
	}
	
	@Test
	public void testDeselectDocumentCorrect() {
		testSelectDocumentCorrect();
		controller.deselectDocument("src/main");
		assertEquals(0, controller.getSelectedDocs().size());
	}
	
	@Test
	public void testDeselectDocumentMissing() {
		testSelectDocumentCorrect();
		controller.deselectDocument("src/test");
		assertEquals(1, controller.getSelectedDocs().size());
	}
	
	@Test
	public void testDeselectDocumentNotInSelected() {
		testSelectDocumentCorrect();
		controller.addDocument(documentTwo);
		controller.deselectDocument("src/test");
		assertEquals(1, controller.getSelectedDocs().size());
	}

}
