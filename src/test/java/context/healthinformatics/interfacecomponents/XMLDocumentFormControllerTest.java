package context.healthinformatics.interfacecomponents;

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

	}

	/**
	 * Test a correct document.
	 * @throws Exception shouldnt throw an exception
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
	 * @throws Exception shouldnt throw an exception
	 */
	@Test
	public void testCorrectEmptyListOfDocuments() throws Exception {
		XMLDocumentFormController controller = new XMLDocumentFormController(
				listOfDocuments);
		assertFalse(controller.checkIfHasEmptyFields());
	}

	/**
	 * Test a null document.
	 * @throws Exception shouldnt throw an exception
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
	 * @throws Exception should throw exception with doc type is empty
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
	 * @throws Exception should throw exception with doc name is empty
	 */
	@Test(expected = Exception.class)
	public void testEmptyDocName() throws Exception {
		ArrayList<DocumentFieldsContainer> mockedList = new ArrayList<DocumentFieldsContainer>();
		DocumentFieldsContainer mockedDocField = mock(DocumentFieldsContainer.class);
		when(mockedDocField.getDocumentNameValue()).thenReturn("");
		when(mockedDocField.getDocumentPathValue()).thenReturn("path");
		when(mockedDocField.getDocumentTypeValue()).thenReturn("excel");
		when(mockedDocField.getDocumentStartLineValue()).thenReturn("1");
		when(mockedDocField.getDelimiterValue()).thenReturn("");
		when(mockedDocField.getSheetValue()).thenReturn("1");
		when(mockedDocField.getColumnFields()).thenReturn(
				new ArrayList<ColumnFieldContainer>());
		mockedList.add(mockedDocField);
		XMLDocumentFormController controller = new XMLDocumentFormController(
				mockedList);
		controller.checkIfHasEmptyFields();
	}

	/**
	 * Test Doc path is empty.
	 * @throws Exception should throw exception with doc path is empty
	 */
	@Test(expected = Exception.class)
	public void testEmptyDocPath() throws Exception {
		ArrayList<DocumentFieldsContainer> mockedList = new ArrayList<DocumentFieldsContainer>();
		DocumentFieldsContainer mockedDocField = mock(DocumentFieldsContainer.class);
		when(mockedDocField.getDocumentNameValue()).thenReturn("docName");
		when(mockedDocField.getDocumentPathValue()).thenReturn("");
		when(mockedDocField.getDocumentTypeValue()).thenReturn("excel");
		when(mockedDocField.getDocumentStartLineValue()).thenReturn("1");
		when(mockedDocField.getDelimiterValue()).thenReturn("");
		when(mockedDocField.getSheetValue()).thenReturn("1");
		when(mockedDocField.getColumnFields()).thenReturn(
				new ArrayList<ColumnFieldContainer>());
		mockedList.add(mockedDocField);
		XMLDocumentFormController controller = new XMLDocumentFormController(
				mockedList);
		controller.checkIfHasEmptyFields();
	}

	/**
	 * Test doc start line is empty.
	 * @throws Exception should throw exception wih doc path is empty
	 */
	@Test(expected = Exception.class)
	public void testEmptyDocStartLine() throws Exception {
		ArrayList<DocumentFieldsContainer> mockedList = new ArrayList<DocumentFieldsContainer>();
		DocumentFieldsContainer mockedDocField = mock(DocumentFieldsContainer.class);
		when(mockedDocField.getDocumentNameValue()).thenReturn("docName");
		when(mockedDocField.getDocumentPathValue()).thenReturn("path");
		when(mockedDocField.getDocumentTypeValue()).thenReturn("excel");
		when(mockedDocField.getDocumentStartLineValue()).thenReturn("");
		when(mockedDocField.getDelimiterValue()).thenReturn(",");
		when(mockedDocField.getSheetValue()).thenReturn("1");
		when(mockedDocField.getColumnFields()).thenReturn(
				new ArrayList<ColumnFieldContainer>());
		mockedList.add(mockedDocField);
		XMLDocumentFormController controller = new XMLDocumentFormController(
				mockedList);
		controller.checkIfHasEmptyFields();
	}

	/**
	 * Test Empty Doc sheet.
	 * @throws Exception should throw exception with empty doc sheet
	 */
	@Test(expected = Exception.class)
	public void testEmptyDocSheet() throws Exception {
		ArrayList<DocumentFieldsContainer> mockedList = new ArrayList<DocumentFieldsContainer>();
		DocumentFieldsContainer mockedDocField = mock(DocumentFieldsContainer.class);
		when(mockedDocField.getDocumentNameValue()).thenReturn("docName");
		when(mockedDocField.getDocumentPathValue()).thenReturn("path");
		when(mockedDocField.getDocumentTypeValue()).thenReturn("excel");
		when(mockedDocField.getDocumentStartLineValue()).thenReturn("1");
		when(mockedDocField.getDelimiterValue()).thenReturn("");
		when(mockedDocField.getSheetValue()).thenReturn("");
		when(mockedDocField.getColumnFields()).thenReturn(
				new ArrayList<ColumnFieldContainer>());
		mockedList.add(mockedDocField);
		XMLDocumentFormController controller = new XMLDocumentFormController(
				mockedList);
		controller.checkIfHasEmptyFields();
	}

	/**|
	 * Test empty delimiter.
	 * @throws Exception should throw exception with empty delimiter
	 */
	@Test(expected = Exception.class)
	public void testEmptyDelimiter() throws Exception {
		ArrayList<DocumentFieldsContainer> mockedList = new ArrayList<DocumentFieldsContainer>();
		DocumentFieldsContainer mockedDocField = mock(DocumentFieldsContainer.class);
		when(mockedDocField.getDocumentNameValue()).thenReturn("docName");
		when(mockedDocField.getDocumentPathValue()).thenReturn("path");
		when(mockedDocField.getDocumentTypeValue()).thenReturn("txt");
		when(mockedDocField.getDocumentStartLineValue()).thenReturn("1");
		when(mockedDocField.getDelimiterValue()).thenReturn("");
		when(mockedDocField.getSheetValue()).thenReturn("");
		when(mockedDocField.getColumnFields()).thenReturn(
				new ArrayList<ColumnFieldContainer>());
		mockedList.add(mockedDocField);
		XMLDocumentFormController controller = new XMLDocumentFormController(
				mockedList);
		controller.checkIfHasEmptyFields();
	}

	/**
	 * Test with not number for Start line.
	 * @throws Exception throws exception for wrong start line
	 */
	@Test(expected = Exception.class)
	public void testWrongFormattedStartLine() throws Exception {
		ArrayList<DocumentFieldsContainer> mockedList = new ArrayList<DocumentFieldsContainer>();
		DocumentFieldsContainer mockedDocField = mock(DocumentFieldsContainer.class);
		when(mockedDocField.getDocumentNameValue()).thenReturn("docName");
		when(mockedDocField.getDocumentPathValue()).thenReturn("path");
		when(mockedDocField.getDocumentTypeValue()).thenReturn("txt");
		when(mockedDocField.getDocumentStartLineValue()).thenReturn("wrongFormattedLine");
		when(mockedDocField.getDelimiterValue()).thenReturn(",");
		when(mockedDocField.getSheetValue()).thenReturn("1");
		when(mockedDocField.getColumnFields()).thenReturn(
				new ArrayList<ColumnFieldContainer>());
		mockedList.add(mockedDocField);
		XMLDocumentFormController controller = new XMLDocumentFormController(
				mockedList);
		controller.checkIfHasEmptyFields();
	}

	/**
	 * Test sheet with wrong formatted number.
	 * @throws Exception should throw error 
	 */
	@Test(expected = Exception.class)
	public void testWrongFormattedSheet() throws Exception {
		ArrayList<DocumentFieldsContainer> mockedList = new ArrayList<DocumentFieldsContainer>();
		DocumentFieldsContainer mockedDocField = mock(DocumentFieldsContainer.class);
		when(mockedDocField.getDocumentNameValue()).thenReturn("docName");
		when(mockedDocField.getDocumentPathValue()).thenReturn("path");
		when(mockedDocField.getDocumentTypeValue()).thenReturn("excel");
		when(mockedDocField.getDocumentStartLineValue()).thenReturn("1");
		when(mockedDocField.getDelimiterValue()).thenReturn("");
		when(mockedDocField.getSheetValue()).thenReturn("wrongFormattedLine");
		when(mockedDocField.getColumnFields()).thenReturn(
				new ArrayList<ColumnFieldContainer>());
		mockedList.add(mockedDocField);
		XMLDocumentFormController controller = new XMLDocumentFormController(
				mockedList);
		controller.checkIfHasEmptyFields();
	}

//	@Test(expected = Exception.class)
//	public void testEmptyColumnID() throws Exception {
//		
//		ColumnFieldContainer mockedColField = mock(ColumnFieldContainer.class);
//		ArrayList<ColumnFieldContainer> mockedListColField = new ArrayList<ColumnFieldContainer>();
//		mockedListColField.add(mockedColField);
//		when(mockedColField.getColumnIDValue()).thenReturn("");
//		when(mockedColField.getColumnNameValue()).thenReturn("colName");
//		when(mockedColField.getColumnTypeValue()).thenReturn("int");
//		when(mockedColField.getColumnDateTypeValue()).thenReturn("");
//		
//		ArrayList<DocumentFieldsContainer> mockedList = new ArrayList<DocumentFieldsContainer>();
//		DocumentFieldsContainer mockedDocField = mock(DocumentFieldsContainer.class);
//		when(mockedDocField.getDocumentNameValue()).thenReturn("docName");
//		when(mockedDocField.getDocumentPathValue()).thenReturn("path");
//		when(mockedDocField.getDocumentTypeValue()).thenReturn("excel");
//		when(mockedDocField.getDocumentStartLineValue()).thenReturn("1");
//		when(mockedDocField.getDelimiterValue()).thenReturn("");
//		when(mockedDocField.getSheetValue()).thenReturn("wrongFormattedLine");
//		when(mockedDocField.getColumnFields()).thenReturn(
//				mockedListColField);
//		
//		XMLDocumentFormController controller = new XMLDocumentFormController(
//				mockedList);
//		controller.checkIfHasEmptyFields();
//	}

//	@Test(expected = Exception.class)
//	public void testEmptyColumnName() {
//
//	}
//
//	@Test(expected = Exception.class)
//	public void testEmptyColumnDateType() {
//
//	}
}
