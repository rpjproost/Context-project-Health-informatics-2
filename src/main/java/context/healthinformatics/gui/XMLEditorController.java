package context.healthinformatics.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import context.healthinformatics.writer.XMLDocument;;

/**
 * The controller of the XMLEditor Panel.
 */
public class XMLEditorController {

	private HashMap<String, ArrayList<XMLDocument>> allDocs;
	private ArrayList<XMLDocument> selectedDocs;
	private String project;

	/**
	 * Constructor for the XMLEditorController.
	 */
	public XMLEditorController() {
		allDocs = new HashMap<String, ArrayList<XMLDocument>>();
		setSelectedDocs(new ArrayList<XMLDocument>());
	}
	
	/**
	 * Constructor created a list with all document already loaded.
	 * @param project the name of project you are working in.
	 * @param projectDocs the list of documents already made.
	 */
	public XMLEditorController(String project, ArrayList<XMLDocument> projectDocs) {
		allDocs = new HashMap<String, ArrayList<XMLDocument>>();
		setSelectedDocs(new ArrayList<XMLDocument>());
		allDocs.put(project, projectDocs);
		setProject(project);
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
		ArrayList<XMLDocument> projectDocs;
		if (allDocs.containsKey(project)) {
			projectDocs = allDocs.get(project);
		} else {
			projectDocs = new ArrayList<XMLDocument>();
		}
		projectDocs.add(doc);
		allDocs.put(project, projectDocs);
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
		ArrayList<XMLDocument> projectDocs = allDocs.get(project);
		if (projectDocs != null) {
			for (int i = 0; i < projectDocs.size(); i++) {
				if (projectDocs.get(i).getPath().equals(filePath)) {
					return projectDocs.get(i);
				}
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
		ArrayList<XMLDocument> projectDocs = allDocs.get(project);
		if (projectDocs != null) {
			for (int i = 0; i < projectDocs.size(); i++) {
				String path = projectDocs.get(i).getPath();
				if (obtainFileName(path).equals(filePath)) {
					return projectDocs.get(i);
				}
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
		File file = new File(path);
		return file.getName();
	}
	
	/**
	 * Set the directory of the files to another project.
	 * @param project the new project you want to work with.
	 */
	public void setProject(String project) {
		this.project = project;
	}
	
	/**
	 * @return a list of all documents that are in a project.
	 */
	public ArrayList<XMLDocument> getProjectDocuments() {
		return allDocs.get(project);
	}

	/**
	 * Empties the editor and add the new related files into the editor.
	 * @param editor the panel that should be cleared etc.
	 */
	public void loadProject(XMLEditor editor) {
		editor.emptyEditor();
		if (getProjectDocuments() != null) {
			for (XMLDocument doc : getProjectDocuments()) {
				editor.addXMLDocumentToContainerScrollPanel(doc);
			}
		}
	}
}
