package context.healthinformatics.interfacecomponents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import context.healthinformatics.gui.InputPage;
import context.healthinformatics.parser.Column;
import context.healthinformatics.writer.XMLDocument;

/**
 * Test the XMLDocumentFormController class.
 */
@RunWith(MockitoJUnitRunner.class)
public class XMLDocumentFormControllerTest {

	private DocumentFieldsContainer fullDoc;
	private DocumentFieldsContainer nullDoc;
	private XMLDocument xmlDocFull;
	private XMLDocument xmlDocNull;
	private ArrayList<DocumentFieldsContainer> listOfDocuments;
	private XMLDocument tempXMLDocument;

	private XMLEditor edit = new XMLEditor(mock(InputPage.class));
	private DocumentFieldsContainer mockedDocField;
	private ArrayList<DocumentFieldsContainer> mockedList;

	private ColumnFieldContainer mockedColField;
	private ArrayList<ColumnFieldContainer> mockedListColField;

	/**
	 * Set up some documents to work with.
	 */
	@Before
	public void setUp() {
		ArrayList<Column> cols = new ArrayList<Column>();
		Column col = new Column(2, "date", "Date");
		col.setColumnType("dd-MM-yyyy");
		cols.add(col);
		xmlDocFull = new XMLDocument("excel", "admire", ";", "src/main", 1, 1,
				cols);
		xmlDocNull = new XMLDocument("excel", "admire", ";", "src/main", 1, 1,
				new ArrayList<Column>());
		fullDoc = new DocumentFieldsContainer(xmlDocFull, edit);
		nullDoc = new DocumentFieldsContainer(xmlDocNull, edit);
		listOfDocuments = new ArrayList<DocumentFieldsContainer>();
		setupMockedDocumentContainer();
		setupMockedColumnContainer();
	}

	/**
	 * Setup the mocked document container to test the different exceptions.
	 */
	public void setupMockedDocumentContainer() {
		mockedList = new ArrayList<DocumentFieldsContainer>();
		mockedDocField = mock(DocumentFieldsContainer.class);
		when(mockedDocField.getDocumentNameValue()).thenReturn("name");
		when(mockedDocField.getDocumentPathValue()).thenReturn("path");
		when(mockedDocField.getDocumentTypeValue()).thenReturn("excel");
		when(mockedDocField.getDocumentStartLineValue()).thenReturn("1");
		when(mockedDocField.getDelimiterValue()).thenReturn("");
		when(mockedDocField.getSheetValue()).thenReturn("1");
		when(mockedDocField.getColumnFields()).thenReturn(
				new ArrayList<ColumnFieldContainer>());
		mockedList.add(mockedDocField);
	}

	/**
	 * Setup the mocked column container to test the different exceptions.
	 */
	public void setupMockedColumnContainer() {
		mockedColField = mock(ColumnFieldContainer.class);
		mockedListColField = new ArrayList<ColumnFieldContainer>();
		mockedListColField.add(mockedColField);
		when(mockedColField.getColumnIDValue()).thenReturn("1");
		when(mockedColField.getColumnNameValue()).thenReturn("colName");
		when(mockedColField.getColumnTypeValue()).thenReturn("int");
		when(mockedColField.getColumnDateTypeValue()).thenReturn("yymmdd");
	}

	/**
	 * Test a correct document.
	 * 
	 * @throws Exception
	 *             shouldn't throw an exception
	 */
	@Test
	public void testCorrectListOfDocuments() throws Exception {
		listOfDocuments.add(fullDoc);
		XMLDocumentFormController controller = new XMLDocumentFormController(
				listOfDocuments);
		assertFalse(controller.checkIfHasEmptyFields());
	}

	/**
	 * Test a correct list of documents which is empty.
	 * 
	 * @throws Exception
	 *             shouldn't throw an exception
	 */
	@Test
	public void testCorrectEmptyListOfDocuments() throws Exception {
		XMLDocumentFormController controller = new XMLDocumentFormController(
				listOfDocuments);
		assertFalse(controller.checkIfHasEmptyFields());
	}

	/**
	 * Test a null document.
	 * 
	 * @throws Exception
	 *             shouldnt throw an exception
	 */
	@Test
	public void testCorrectListOfNullDocuments() throws Exception {
		listOfDocuments.add(nullDoc);
		XMLDocumentFormController controller = new XMLDocumentFormController(
				listOfDocuments);
		assertFalse(controller.checkIfHasEmptyFields());
	}

	/**
	 * Test doc type is empty.
	 * 
	 * @throws Exception
	 *             should throw exception with doc type is empty
	 */
	@Test
	public void testEmptyDocType() throws Exception {
		tempXMLDocument = new XMLDocument("", "admire", ";", "src/main", 1, 1,
				new ArrayList<Column>());
		DocumentFieldsContainer emptyTypeDoc = new DocumentFieldsContainer(
				tempXMLDocument, edit);
		listOfDocuments.add(emptyTypeDoc);
		XMLDocumentFormController controller = new XMLDocumentFormController(
				listOfDocuments);
		assertFalse(controller.checkIfHasEmptyFields());
	}

	/**
	 * Test doc name is empty.
	 * 
	 * @throws Exception
	 *             should throw exception with doc name is empty
	 */
	@Test(expected = Exception.class)
	public void testEmptyDocName() throws Exception {
		when(mockedDocField.getDocumentNameValue()).thenReturn("");
		XMLDocumentFormController controller = new XMLDocumentFormController(
				mockedList);
		try {
			controller.checkIfHasEmptyFields();
		} catch (Exception e) {
			assertEquals("Error in document:1 Document name is empty!",
					e.getMessage());
			throw new Exception("Test Passes!");
		}
	}

	/**
	 * Test Doc path is empty.
	 * 
	 * @throws Exception
	 *             should throw exception with doc path is empty
	 */
	@Test(expected = Exception.class)
	public void testEmptyDocPath() throws Exception {
		when(mockedDocField.getDocumentPathValue()).thenReturn("");
		XMLDocumentFormController controller = new XMLDocumentFormController(
				mockedList);
		try {
			controller.checkIfHasEmptyFields();
		} catch (Exception e) {
			assertEquals("Error in document:1 Document path is empty!",
					e.getMessage());
			throw new Exception("Test Passes!");
		}
	}

	/**
	 * Test doc start line is empty.
	 * 
	 * @throws Exception
	 *             should throw exception wih doc path is empty
	 */
	@Test(expected = Exception.class)
	public void testEmptyDocStartLine() throws Exception {
		when(mockedDocField.getDocumentStartLineValue()).thenReturn("");
		XMLDocumentFormController controller = new XMLDocumentFormController(
				mockedList);
		try {
			controller.checkIfHasEmptyFields();
		} catch (Exception e) {
			assertEquals(
					"Error in document:1 Document start line is empty or not a number!",
					e.getMessage());
			throw new Exception("Test Passes!");
		}
	}

	/**
	 * Test Empty Doc sheet.
	 * 
	 * @throws Exception
	 *             should throw exception with empty doc sheet
	 */
	@Test(expected = Exception.class)
	public void testEmptyDocSheet() throws Exception {
		when(mockedDocField.getDocumentTypeValue()).thenReturn("excel");
		when(mockedDocField.getSheetValue()).thenReturn("");
		XMLDocumentFormController controller = new XMLDocumentFormController(
				mockedList);
		try {
			controller.checkIfHasEmptyFields();
		} catch (Exception e) {
			assertEquals(
					"Error in document:1 Document sheet value is empty or not a number!",
					e.getMessage());
			throw new Exception("Test Passes!");
		}
	}

	/**
	 * | Test empty delimiter.
	 * 
	 * @throws Exception
	 *             should throw exception with empty delimiter
	 */
	@Test(expected = Exception.class)
	public void testEmptyDelimiter() throws Exception {
		when(mockedDocField.getDocumentTypeValue()).thenReturn("txt");
		when(mockedDocField.getDelimiterValue()).thenReturn("");
		XMLDocumentFormController controller = new XMLDocumentFormController(
				mockedList);
		try {
			controller.checkIfHasEmptyFields();
		} catch (Exception e) {
			assertEquals(
					"Error in document:1 Document delimiter value is empty!",
					e.getMessage());
			throw new Exception("Test Passes!");
		}
	}

	/**
	 * Test with not number for Start line.
	 * 
	 * @throws Exception
	 *             throws exception for wrong start line
	 */
	@Test(expected = Exception.class)
	public void testWrongFormattedStartLine() throws Exception {
		when(mockedDocField.getDocumentStartLineValue()).thenReturn(
				"wrongFormattedLine");
		XMLDocumentFormController controller = new XMLDocumentFormController(
				mockedList);
		try {
			controller.checkIfHasEmptyFields();
		} catch (Exception e) {
			assertEquals(
					"Error in document:1 Document start line is empty or not a number!",
					e.getMessage());
			throw new Exception("Test Passes!");
		}
	}

	/**
	 * Test sheet with wrong formatted number.
	 * 
	 * @throws Exception
	 *             should throw error
	 */
	@Test(expected = Exception.class)
	public void testWrongFormattedSheet() throws Exception {
		when(mockedDocField.getSheetValue()).thenReturn("wrongFormattedLine");
		XMLDocumentFormController controller = new XMLDocumentFormController(
				mockedList);
		try {
			controller.checkIfHasEmptyFields();
		} catch (Exception e) {
			assertEquals(
					"Error in document:1 Document sheet value is empty or not a number!",
					e.getMessage());
			throw new Exception("Test Passes!");
		}
	}

	/**
	 * Test empty column id.
	 * 
	 * @throws Exception
	 *             should throw exception for empty column id
	 */
	@Test(expected = Exception.class)
	public void testEmptyColumnID() throws Exception {
		when(mockedDocField.getColumnFields()).thenReturn(mockedListColField);
		when(mockedColField.getColumnIDValue()).thenReturn("");
		XMLDocumentFormController controller = new XMLDocumentFormController(
				mockedList);
		try {
			controller.checkIfHasEmptyFields();
		} catch (Exception e) {
			assertEquals(
					"Error in document:1 Error in column: 1 Column id is empty or not a number!",
					e.getMessage());
			throw new Exception("Test passes!");
		}
	}

	/**
	 * Test a column with empty name.
	 * 
	 * @throws Exception
	 *             should throw exception for empty column name
	 */
	@Test(expected = Exception.class)
	public void testEmptyColumnName() throws Exception {
		when(mockedDocField.getColumnFields()).thenReturn(mockedListColField);
		when(mockedColField.getColumnNameValue()).thenReturn("");
		XMLDocumentFormController controller = new XMLDocumentFormController(
				mockedList);
		try {
			controller.checkIfHasEmptyFields();
		} catch (Exception e) {
			assertEquals(
					"Error in document:1 Error in column: 1 Column name is empty!",
					e.getMessage());
			throw new Exception("Test passes!");
		}
	}

	/**
	 * Test a column with empty column date type.
	 * 
	 * @throws Exception
	 *             should throw exception for empty column date type
	 */
	@Test(expected = Exception.class)
	public void testEmptyColumnDateType() throws Exception {
		when(mockedDocField.getColumnFields()).thenReturn(mockedListColField);
		when(mockedColField.hasDateType()).thenReturn(true);
		when(mockedColField.getColumnDateTypeValue()).thenReturn("");
		XMLDocumentFormController controller = new XMLDocumentFormController(
				mockedList);
		try {
			controller.checkIfHasEmptyFields();
		} catch (Exception e) {
			assertEquals(
					"Error in document:1 Error in column: 1 Column date type is empty!",
					e.getMessage());
			throw new Exception("Test passes!");
		}
	}
}
