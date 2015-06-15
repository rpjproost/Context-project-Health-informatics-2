package context.healthinformatics.interfacecomponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import context.healthinformatics.gui.InputPage;
import context.healthinformatics.parser.Column;
import context.healthinformatics.writer.XMLDocument;

/**
 * Tests the entire document field container class.
 */
public class DocumentFieldsContainerTest {

	private DocumentFieldsContainer emptyDoc;
	private DocumentFieldsContainer fullDoc;
	private DocumentFieldsContainer nullDoc;
	private XMLDocument xmlDoc;
	private XMLDocument xmlDocFull;
	private XMLDocument xmlDocNull;

	/**
	 * Set up some documents to work with.
	 */
	@Before
	public void setUp() {
		xmlDoc = new XMLDocument();
		ArrayList<Column> cols = new ArrayList<Column>();
		cols.add(new Column(2, "date", "Date"));
		xmlDocFull = new XMLDocument("excel", "admire", ";", "src/main", 1, 1,
				cols);
		xmlDocNull = new XMLDocument("excel", "admire", ";", "src/main", 1, 1,
				new ArrayList<Column>());
		XMLEditor edit = new XMLEditor(mock(InputPage.class));
		emptyDoc = new DocumentFieldsContainer(xmlDoc, edit);
		fullDoc = new DocumentFieldsContainer(xmlDocFull, edit);
		nullDoc = new DocumentFieldsContainer(xmlDocNull, edit);
	}

	/**
	 * Tests if the combobox index will give the right index.
	 */
	@Test
	public void testGetComboBoxIndex() {
		assertEquals(emptyDoc.getComboBoxIndex("excEl"), 0);
		assertEquals(emptyDoc.getComboBoxIndex("CSV"), 2);
		assertEquals(emptyDoc.getComboBoxIndex("txt"), 1);
	}

	/**
	 * Tests if the correct boolean is thrown for the empty field check.
	 */
	@Test
	public void testCheckIfHasEmptyFields() {
/*		assertTrue(emptyDoc.checkIfHasEmptyFields());
		assertTrue(fullDoc.checkIfHasEmptyFields());
		assertFalse(nullDoc.checkIfHasEmptyFields());*/
	}

	/**
	 * test if the correct document is returned.
	 */
	@Test
	public void testGetDocument() {
		XMLDocument temp = nullDoc.getXMLDocument();
		assertEquals(temp.getDocName(), xmlDocNull.getDocName());
		temp = emptyDoc.getXMLDocument();
		assertEquals(emptyDoc.getDocumentNameValue(), xmlDoc.getDocName());
		temp = fullDoc.getXMLDocument();
		assertEquals(temp.getDocName(), xmlDocFull.getDocName());
	}

	/**
	 * Tests if the is integer method works properly.
	 */
	@Test
	public void testIsInteger() {
		assertFalse(fullDoc.isInteger(null));
		assertFalse(fullDoc.isInteger("doc"));
		assertTrue(fullDoc.isInteger("9"));
	}

	/**
	 * Tests if all get data methods are working.
	 */
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

	/**
	 * Test the is empty methods.
	 */
	@Test
	public void testIsEmptyMethods() {
		//TODO 
/*		assertFalse(fullDoc.isDelimiterEmpty());
		assertTrue(emptyDoc.isDelimiterEmpty());
		assertFalse(emptyDoc.isSheetEmpty());
		assertFalse(emptyDoc.isDocumentPathEmpty());*/
	}

	/**
	 * Test the add column button.
	 */
	@Test
	public void testAddColumnButton() {
		int last = fullDoc.getColumnFields().size();
		ActionListener[] listeners = fullDoc.getAddColumnButton().getActionListeners();
		ActionEvent event = new ActionEvent(fullDoc.getAddColumnButton(), 0, null);
		listeners[0].actionPerformed(event);
		assertEquals(last + 1, fullDoc.getColumnFields().size());
	}

	/**
	 * Test the remove column button.
	 */
	@Test
	public void testRemoveColumnButton() {
		testAddColumnButton();
		int last = fullDoc.getColumnFields().size();
		ActionListener[] listeners = fullDoc.getRemoveColumnButton().getActionListeners();
		ActionEvent event = new ActionEvent(fullDoc.getRemoveColumnButton(), 0, null);
		listeners[0].actionPerformed(event);
		assertEquals(last - 1, fullDoc.getColumnFields().size());
	}

	/**
	 * Test the combo box changes if there is something changed.
	 */
	@Test
	public void testComboxEvent() {
		JPanel actual = new JPanel();
		fullDoc.setPanelForDocTypeSpecificInput(actual);
		ActionListener[] listeners = fullDoc.getDocumentType().getActionListeners();
		ActionEvent event = new ActionEvent(fullDoc.getDocumentType(), 0,	null);
		listeners[0].actionPerformed(event);
		assertNotEquals(actual, fullDoc.getPanelForDocTypeSpecificInput());
	}
}
