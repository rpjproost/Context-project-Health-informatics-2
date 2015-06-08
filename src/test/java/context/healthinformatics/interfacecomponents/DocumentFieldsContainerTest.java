package context.healthinformatics.interfacecomponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import context.healthinformatics.gui.InputPage;
import context.healthinformatics.parser.Column;
import context.healthinformatics.writer.XMLDocument;

public class DocumentFieldsContainerTest {
	
	private DocumentFieldsContainer emptyDoc;
	private DocumentFieldsContainer fullDoc;
	private DocumentFieldsContainer nullDoc;
	private XMLDocument xmlDoc;
	private XMLDocument xmlDocFull;
	private XMLDocument xmlDocNull;

	@Before
	public void setUp() {
		xmlDoc = new XMLDocument();
		ArrayList<Column> cols = new ArrayList<Column>();
		cols.add(new Column(2, "date", "Date"));
		xmlDocFull = new XMLDocument("excel", "admire", ";", "src/main", 1, 1, cols);
		xmlDocNull = new XMLDocument(
				"excel", "admire", ";", "src/main", 1, 1, new ArrayList<Column>());
		XMLEditor edit = new XMLEditor(mock(InputPage.class));
		emptyDoc = new DocumentFieldsContainer(xmlDoc, edit);
		fullDoc = new DocumentFieldsContainer(xmlDocFull, edit);
		nullDoc = new DocumentFieldsContainer(xmlDocNull, edit);
	}

	@Test
	public void testGetComboBoxIndex() {
		assertEquals(emptyDoc.getComboBoxIndex("excEl"), 0);
		assertEquals(emptyDoc.getComboBoxIndex("CSV"), 2);
		assertEquals(emptyDoc.getComboBoxIndex("txt"), 1);
	}
	
	@Test
	public void testCheckIfHasEmptyFields() {
		assertTrue(emptyDoc.checkIfHasEmptyFields());
		assertTrue(fullDoc.checkIfHasEmptyFields());
		assertFalse(nullDoc.checkIfHasEmptyFields());
	}
	
	@Test
	public void testGetDocument() {
		XMLDocument temp = nullDoc.getXMLDocument();
		assertEquals(temp.getDocName(), xmlDocNull.getDocName());
		temp = emptyDoc.getXMLDocument();
		assertEquals(emptyDoc.getDocumentNameValue(), xmlDoc.getDocName());
		temp = fullDoc.getXMLDocument();
		assertEquals(temp.getDocName(), xmlDocFull.getDocName());
	}
	
	@Test
	public void testIsInteger() {
		assertFalse(fullDoc.isInteger(null));
		assertFalse(fullDoc.isInteger("doc"));
		assertTrue(fullDoc.isInteger("9"));
	}
	
	@Test
	public void testGetAllData() {
		assertEquals("Excel", fullDoc.getDocumentTypeValue());
		assertEquals("admire", fullDoc.getDocumentNameValue());
		assertEquals(",", fullDoc.getDelimiterValue());
		assertEquals("src/main", fullDoc.getDocumentPathValue());
		assertEquals(1, fullDoc.getSheetValue());
		assertEquals(1, fullDoc.getDocumentStartLineValue());
		ArrayList<ColumnFieldContainer> columns = fullDoc.getColumnFields();
		for (int i = 0; i < columns.size(); i++) {
			ColumnFieldContainer c = columns.get(i);
			assertEquals(2, c.getColumnIDValue());
			assertEquals("date", c.getColumnNameValue());
			assertEquals("Date", c.getColumnTypeValue());
		}
	}
	
	@Test
	public void testIsEmptyMethods() {
		assertFalse(fullDoc.isDelimiterEmpty());
		assertTrue(emptyDoc.isDelimiterEmpty());
		assertFalse(emptyDoc.isSheetEmpty());
		assertFalse(emptyDoc.isDocumentPathEmpty());
	}
	
	@Test
	public void testActionListeners() {
		ActionListener[] listeners = fullDoc.getAddColumnButton().getActionListeners();
		ActionEvent event = new ActionEvent(fullDoc.getAddColumnButton(), 0, null);
		listeners[0].actionPerformed(event);
		listeners = fullDoc.getRemoveColumnButton().getActionListeners();
		event = new ActionEvent(fullDoc.getRemoveColumnButton(), 0, null);
		listeners[0].actionPerformed(event);
	}

}
