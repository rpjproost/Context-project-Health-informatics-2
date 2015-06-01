package context.healthinformatics.gui;

import java.util.ArrayList;

import context.healthinformatics.writer.XMLDocument;;

/**
 * The controller of the XMLEditor Panel.
 */
public class XMLEditorController {

	private ArrayList<XMLDocument> allDocs;
	private ArrayList<XMLDocument> selectedDocs;

	/**
	 * Constructor for the XMLEditorController.
	 */
	public XMLEditorController() {
		allDocs = new ArrayList<XMLDocument>();
		setSelectedDocs(new ArrayList<XMLDocument>());
	}
	
	/**
	 * Constructor created a list with all document already loaded.
	 * @param allDocs the list of documents already made.
	 */
	public XMLEditorController(ArrayList<XMLDocument> allDocs) {
		this.allDocs = allDocs;
		setSelectedDocs(new ArrayList<XMLDocument>());
	}

	/**
	 * @return the selected documents of the input.
	 */
	public ArrayList<XMLDocument> getSelectedDocs() {
		return selectedDocs;
	}

	/**
	 * Set the selectDos to a specific list of documents.
	 * @param selectedDocs the new list of selected documents.
	 */
	private void setSelectedDocs(ArrayList<XMLDocument> selectedDocs) {
		this.selectedDocs = selectedDocs;
	}
	
	/**
	 * Adds a document to all documents off the program.
	 * @param doc the document that should be added.
	 */
	public void addDocument(XMLDocument doc) {
		allDocs.add(doc);
	}
	
	/**
	 * Adds a document to the selected documents list.
	 * Only if it isn't already selected and it must exist.
	 * @param filePath the path of the document.
	 */
	public void selectDocument(String filePath) {
		XMLDocument select = getDocument(filePath);
		if (select != null) {
			addToSelectedFiles(select);
		}
	}
	
	/**
	 * Deletes a document to the selected documents list.
	 * It must exist, else it will do nothing.
	 * @param filePath the path of the document.
	 */
	public void deselectDocument(String filePath) {
		XMLDocument deselect = getDocument(filePath);
		if (deselect != null) {
			deleteFromSelectedFiles(deselect);
		}
	}

	/**
	 * Adds a XML document to the list of selected files.
	 * @param select, the file that could be added.
	 */
	private void addToSelectedFiles(XMLDocument select) {
		if (!selectedDocs.contains(select)) {
			selectedDocs.add(select);
		}
	}
	
	/**
	 * Deletes a XML document from the list of selected files.
	 * @param deselect, the file that must be removed.
	 */
	private void deleteFromSelectedFiles(XMLDocument deselect) {
		if (selectedDocs.contains(deselect)) {
			selectedDocs.remove(deselect);
		}
	}

	/**
	 * Search in all documents if a document with the file path exists
	 * if not return a null Object.
	 * @param filePath String with the path to the file.
	 * @return the document out of all documents if it exists.
	 */
	public XMLDocument getDocument(String filePath) {
		for (int i = 0; i < allDocs.size(); i++) {
			if (allDocs.get(i).getPath().equals(filePath)) {
				return allDocs.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Search in all documents if a document with only a piece of the file path exists
	 * if not return a null Object.
	 * @param filePath the part of a entire path.
	 * @return Document that corresponds to that little piece of path.	
	 */
	public XMLDocument getDocumentWithPartofPath(String filePath) {
		for (int i = 0; i < allDocs.size(); i++) {
			String path = allDocs.get(i).getPath();
			if (obtainFileName(path).equals(filePath)) {
				return allDocs.get(i);
			}
		}
		return null;
	}
	

	/**
	 * Splits a path string and obtain only the file name.
	 * @param path the source path of a file.
	 * @return only the file name.
	 */
	public String obtainFileName(String path) {
		String[] split = path.split("/");
		return split[split.length - 1];
	}
}
