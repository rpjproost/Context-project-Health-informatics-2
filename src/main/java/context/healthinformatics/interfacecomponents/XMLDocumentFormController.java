package context.healthinformatics.interfacecomponents;

import java.util.ArrayList;

/**
 * XMlDocumnetFormController checks all XML Editor fields for wrong input.
 */
public class XMLDocumentFormController {
	private ArrayList<DocumentFieldsContainer> documentsFromForm;

	/**
	 * The constructor for the form controller gets the list with document
	 * fields.
	 * 
	 * @param documentsFromForm
	 *            the list of document fields
	 */
	public XMLDocumentFormController(
			ArrayList<DocumentFieldsContainer> documentsFromForm) {
		this.documentsFromForm = documentsFromForm;
	}

	/**
	 * Check if there are any empty fields or fields with wrong input.
	 * 
	 * @return false if there are no errors.
	 * @throws Exception
	 *             if there are errors with specific message where it goes
	 *             wrong.
	 */
	public boolean checkIfHasEmptyFields() throws Exception {
		boolean hasEmptyFields = false;
		for (int i = 0; i < documentsFromForm.size(); i++) {
			if (!hasEmptyFields) {
				DocumentFieldsContainer docFieldContainer = documentsFromForm
						.get(i);
				try {
					hasEmptyFields = checkIfExcelDocHasEmptyFields(docFieldContainer)
							|| checkEmptyColumns(docFieldContainer
									.getColumnFields());
				} catch (Exception e) {
					throw new Exception("Error in document:" + (i + 1) + " "
							+ e.getMessage());
				}
			}
		}
		return hasEmptyFields;
	}

	/**
	 * Check if the document has any empty fields or wrong input.
	 * 
	 * @param docFieldContainer
	 *            the document field container.
	 * @return false if no errors
	 * @throws Exception
	 *             with message where it goes wrong
	 */
	private boolean checkIfExcelDocHasEmptyFields(
			DocumentFieldsContainer docFieldContainer) throws Exception {
		if (isEmptyTextField(docFieldContainer.getDocumentNameValue())) {
			throw new Exception("Document name is empty!");
		} else if (isEmptyTextField(docFieldContainer.getDocumentPathValue())) {
			throw new Exception("Document path is empty!");
		} else if (isEmptyTextFieldInteger(docFieldContainer
				.getDocumentStartLineValue())) {
			throw new Exception("Document start line is empty or not a number!");
		} else {
			return checkSpecificFields(docFieldContainer);
		}
	}

	/**
	 * Check for excel and for txt/csv relevant fields.
	 * 
	 * @param docFieldContainer
	 *            the document field container
	 * @return false if there are no errors
	 * @throws Exception
	 *             with message where it goes wrong
	 */
	private boolean checkSpecificFields(
			DocumentFieldsContainer docFieldContainer) throws Exception {
		if (docFieldContainer.getDocumentTypeValue().toLowerCase()
				.equals("excel")) {
			if (isEmptyTextFieldInteger(docFieldContainer.getSheetValue())) {
				throw new Exception(
						"Document sheet value is empty or not a number!");
			} else {
				return false;
			}
		} else {
			if (isEmptyTextField(docFieldContainer.getDelimiterValue())) {
				throw new Exception("Document delimiter value is empty!");
			} else {
				return false;
			}
		}
	}

	/**
	 * Check if there are empty or wrong formatted columns.
	 * 
	 * @param columnFieldContainer
	 *            the container of the column fields
	 * @return false if there are no errors
	 * @throws Exception
	 *             exception with specific message of the error
	 */
	private boolean checkEmptyColumns(
			ArrayList<ColumnFieldContainer> columnFieldContainer)
			throws Exception {
		boolean hasEmptyColumns = false;
		for (int i = 0; i < columnFieldContainer.size(); i++) {
			if (!hasEmptyColumns) {
				try {
					hasEmptyColumns = checkIfHasEmptyFields(columnFieldContainer
							.get(i));
				} catch (Exception e) {
					throw new Exception("Error in column: " + (i + 1) + " "
							+ e.getMessage());
				}
			}
		}
		return hasEmptyColumns;
	}

	/**
	 * Check if a ColumnContainer has empty or wrong formatted fields.
	 * 
	 * @param columnField
	 *            the column field
	 * @return false if there are no errors
	 * @throws Exception
	 *             exception with specific message of the error
	 */
	private boolean checkIfHasEmptyFields(ColumnFieldContainer columnField)
			throws Exception {
		if (isEmptyTextFieldInteger(columnField.getColumnIDValue())) {
			throw new Exception("Column id is empty or not a number!");
		} else if (isEmptyTextField(columnField.getColumnNameValue())) {
			throw new Exception("Column value is empty!");
		} else {
			if (columnField.hasDateType()
					&& isEmptyTextField(columnField.getColumnDateTypeValue())) {
				throw new Exception("Column date type is empty!");
			} else {
				return false;
			}
		}
	}

	/**
	 * Check if a text field is empty.
	 * 
	 * @param textFieldInput
	 *            the TextField input
	 * @return if is empty
	 */
	private boolean isEmptyTextField(String textFieldInput) {
		return textFieldInput.equals("");
	}

	/**
	 * Check if text field is empty and is parse able to integer.
	 * 
	 * @param textFieldInput
	 *            the TextField input
	 * @return if is empty and parse able
	 */
	private boolean isEmptyTextFieldInteger(String textFieldInput) {
		if (textFieldInput.equals("")) {
			return true;
		} else {
			return !isInteger(textFieldInput);
		}
	}

	/**
	 * Check if string can be parsed to an integer.
	 * 
	 * @param s
	 *            the input string
	 * @return true if can be parsed else false
	 */
	private boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException | NullPointerException e) {
			return false;
		}
		return true;
	}
}
