package context.healthinformatics.interfacecomponents;

import java.util.ArrayList;

public class XMLDocumentFormController {
	ArrayList<DocumentFieldsContainer> documentsFromForm;

	public XMLDocumentFormController(
			ArrayList<DocumentFieldsContainer> documentsFromForm) {
		this.documentsFromForm = documentsFromForm;
	}

	public boolean checkIfHasEmptyFields() {
		boolean hasEmptyFields = false;
		for (int i = 0; i < documentsFromForm.size(); i++) {
			if (hasEmptyFields != true) {
				DocumentFieldsContainer docFieldContainer = documentsFromForm
						.get(i);
				hasEmptyFields = checkIfExcelDocHasEmptyFields(docFieldContainer)
						|| checkEmptyColumns(docFieldContainer
								.getColumnFields());
			}
		}
		return hasEmptyFields;

	}

	private boolean checkIfExcelDocHasEmptyFields(
			DocumentFieldsContainer docFieldContainer) {
		if (isEmptyTextField(docFieldContainer.getDocumentNameValue())) {
			return true;
		} else if (isEmptyTextField(docFieldContainer.getDocumentPathValue())) {
			return true;
		} else if (isEmptyTextFieldInteger(docFieldContainer
				.getDocumentStartLineValue())) {
			return true;
		} else {
			if (docFieldContainer.getDocumentTypeValue().toLowerCase()
					.equals("excel")) {
				return isEmptyTextFieldInteger(docFieldContainer
						.getSheetValue());
			} else {
				return isEmptyTextField(docFieldContainer.getDelimiterValue());
			}
		}
	}

	private boolean isEmptyTextField(String textFieldInput) {
		return textFieldInput.equals("");
	}

	private boolean isEmptyTextFieldInteger(int textFieldInput) {
		return textFieldInput == -1;
	}

	public int getIntegerValue(String stringIntegerValue) {
		if (stringIntegerValue.equals("") || !isInteger(stringIntegerValue)) {
			return -1;
		} else {
			return Integer.parseInt(stringIntegerValue);
		}
	}

	protected boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}

	private boolean checkEmptyColumns(
			ArrayList<ColumnFieldContainer> columnFieldContainer) {
		boolean emptyColumns = false;
		for (int i = 0; i < columnFieldContainer.size(); i++) {
			if (emptyColumns != true) {
				emptyColumns = checkIfHasEmptyFields(columnFieldContainer
						.get(i));
			}
		}
		return emptyColumns;
	}

	private boolean checkIfHasEmptyFields(ColumnFieldContainer columnField) {
		return checkIfDateColumnHasEmptyFields(columnField);
	}

	private boolean checkIfDateColumnHasEmptyFields(
			ColumnFieldContainer columnField) {
		if (isEmptyTextFieldInteger(columnField.getColumnIDValue())) {
			return true;
		} else if (isEmptyTextField(columnField.getColumnNameValue())) {
			return true;
		} else {
			if (columnField.hasDateType()) {
				return isEmptyTextField(columnField.getColumnDateTypeValue());
			} else {
				return false;
			}
		}
	}

}
