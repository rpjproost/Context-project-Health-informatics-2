package context.healthinformatics.gui;

import java.awt.GridBagConstraints;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.interfacecomponents.FileTree;
import context.healthinformatics.interfacecomponents.InputPageComponents;
import context.healthinformatics.interfacecomponents.XMLEditor;
import context.healthinformatics.interfacecomponents.XMLEditorController;
import context.healthinformatics.parser.XMLParser;
import context.healthinformatics.writer.XMLDocument;

/**
 * Class which represents one of the states for the variable panel in the
 * mainFrame.
 */
public class InputPage extends InterfaceHelper implements PanelState,
		Serializable {

	private static final long serialVersionUID = 1L;

	private MainFrame mf;
	private FileTree ft;
	private InputPageComponents ipc;
	private ArrayList<ArrayList<String>> folder;

	private JPanel leftPanel;
	private JFileChooser selecter;

	public static final int BUTTONFONTSIZE = 15;
	public static final int THREE = 3;

	private XMLEditor xmledit;
	private XMLEditorController xmlController;

	/**
	 * Constructor.
	 * @param m is the mainframe object
	 */
	public InputPage(MainFrame m) {
		mf = m;
		folder = new ArrayList<ArrayList<String>>();
		xmledit = new XMLEditor(this);
		xmlController = new XMLEditorController();
		xmlController.subscribe(SingletonInterpreter.getInterpreter());
		xmlController.subscribe(SingletonDb.getDb());
		ipc = new InputPageComponents(mf, this);
		checkOnFiles();
	}

	/**
	 * Check whether there are existing projects.
	 */
	private void checkOnFiles() {
		File directory = new File("src/main/data/savedXML/");
		File[] listOfFiles = directory.listFiles();
			if (listOfFiles != null && listOfFiles.length > 0) {
				foundFiles(listOfFiles);
			} else {
				runClearedProject();
			}
	}

	/**
	 * Founded files will be parsed and will be added to the interface.
	 * @param listOfFiles list of files that are found by java.
	 */
	private void foundFiles(File[] listOfFiles) {
		for (int i = 0; i < listOfFiles.length; i++) {
			String project = listOfFiles[i].getName().replace(".xml", "");
			XMLParser parser = new XMLParser(listOfFiles[i].getPath());
			try {
				parser.parse();
			} catch (IOException e) {
				JOptionPane.showConfirmDialog(null,
						"Something went wrong with reading the files.",
						"Error!", JOptionPane.OK_OPTION);
				runClearedProject();
			}
			runOldProject(project, parser.getDocuments());
		}
	}

	/**
	 * Sets all saved values in all components that are involved.
	 * @param project the project which should be added.
	 * @param docsOfFile all files that are belongs to the project.
	 */
	private void runOldProject(String project, ArrayList<XMLDocument> docsOfFile) {
		xmlController.setProject(project);
		xmlController.setDocumentsInProject(docsOfFile);
		folder.add(xmlController.breakDownXMLDocumentsIntoNames(docsOfFile));
		ft = new FileTree(this);
		ipc.getComboBox().addItem(project);
		ipc.getComboBox().setSelectedItem(project);
		xmlController.loadProject(xmledit);
		ft.expandTree();
	}

	/**
	 * Creates a clear project with a standard project default.
	 */
	protected void runClearedProject() {
		ft = new FileTree(this);
		xmlController.setProject("(default)");
		addComboItem("(default)");
	}

	/**
	 * @return Panel of the InputPage state.
	 */
	public JPanel loadPanel() {
		JPanel containerPanel = createPanel(MainFrame.INPUTTABCOLOR,
				getScreenWidth(), getStatePanelSize());
		leftPanel = createLeftPanel();
		JPanel rightPanel = createRightPanel();
		containerPanel.add(leftPanel);
		containerPanel.add(rightPanel);
		return containerPanel;
	}

	/**
	 * Create the left panel of the input page.
	 * @return the left panel
	 */
	private JPanel createLeftPanel() {
		JPanel leftPanel = createPanel(MainFrame.INPUTTABCOLOR,
				getScreenWidth() / 2, getStatePanelSize());
		leftPanel.add(ipc.loadProjectSelection(), setGrids(0, 0));
		leftPanel.add(ft.loadFolder(), setGrids(0, 1));
		GridBagConstraints c = setGrids(0, 2);
		c.weighty = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		leftPanel.add(ipc.loadButtonSection(), c);
		return leftPanel;
	}

	/**
	 * Create the right panel of the input page.
	 * @return the right panel
	 */
	private JPanel createRightPanel() {
		JPanel rightPanel = createPanel(MainFrame.INPUTTABCOLOR,
				getScreenWidth() / 2, getStatePanelSize());
		rightPanel.add(xmledit.loadPanel(), setGrids(0, 0));
		rightPanel.add(ipc.loadAnalyzeButtonSection(), setGrids(0, 1));
		return rightPanel;
	}

	/**
	 * Method which creates the list of projects.
	 * @return list of projects.
	 */
	public String[] getProjects() {
		String[] res = new String[folder.size()];
		for (int i = 0; i < folder.size(); i++) {
			res[i] = folder.get(i).get(0);
		}
		return res;
	}

	/**
	 * Method which asks the user to enter a new project and inserts it in
	 * the comboBox.
	 */
	public void createProject() {
		String newProject = (String) JOptionPane.showInputDialog(leftPanel,
				"New Project : ");
		if (findFolderProject(newProject) != -1) {
			JOptionPane.showMessageDialog(null, "Project name already exists!");
			return;
		}
		if (newProject != null && !newProject.isEmpty()) {
			addComboItem(newProject);
		}
	}

	/**
	 * Method which asks the user to enter a new PROJECTS :, and inserts it in
	 * the ComboBox.
	 * @param project is the name of the project.
	 */
	protected void addComboItem(String project) {
		ArrayList<String> list = new ArrayList<String>();
		list.add(project);
		folder.add(list);
		ipc.getComboBox().addItem(project);
		ipc.getComboBox().setSelectedItem(project);
		xmlController.setProject(project);
		ft.addProjectToTree(project);
	}

	/**
	 * @return the anwser of the filechooser.
	 */
	public JFileChooser openFileChooser() {
		selecter = new JFileChooser();
		selecter = setFilePopUp(selecter, "Only text, excel and xml", 
				new String[]{"txt", "csv", "xlsx", "xls", "xml"});
		return selecter;
	}

	/**
	 * Method which finds the project in the folder.
	 * @return index of project.
	 * @param s is string of project.
	 */
	public int findFolderProject(String s) {
		for (int i = 0; i < folder.size(); i++) {
			if (folder.get(i).get(0).equals(s)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * @return the file tree object.
	 */
	public FileTree getFileTree() {
		return ft;
	}

	/**
	 * @return the InputPageComponents object.
	 */
	public InputPageComponents getInputPageComponent() {
		return ipc;
	}

	/**
	 * @return the file selecter object.
	 */
	public JFileChooser getFileSelecter() {
		return selecter;
	}

	/**
	 * @return the xml editor controller.
	 */
	public XMLEditorController getXMLController() {
		return xmlController;
	}

	/**
	 * @return folder of projects and files.
	 */
	public ArrayList<ArrayList<String>> getFolder() {
		return folder;
	}

	/**
	 * @return the XML editor.
	 */
	public XMLEditor getEditor() {
		return xmledit;
	}

	/**
	 * Method which adds a project to the folder and FileTree.
	 * @param path is the path of the added file.
	 */
	protected void addFile(String path) {
		int project = findFolderProject((String) ipc.getComboBox()
				.getSelectedItem());
		if (folder.size() > 0 && project >= 0 && !path.equals("")
				&& !folder.get(project).contains(path)) {
			folder.get(project).add(path);
			ft.addFileToTree(project, path);
		}
	}

	/**
	 * Open files and adds them to the tree.
	 * @param files all files that should be added.
	 */
	public void openFiles(File[] files) {
		for (int i = 0; i < files.length; i++) {
			String path = files[i].getPath();
			XMLDocument currentDoc = makeDocument(path);
			if (!path.endsWith("xml")) {
				checkForXmlDocument(files, i, path, currentDoc);
			} else {
				addXmlFile(path);
			}
		}
	}
	
	/**
	 * Method which check if there is a document in the xmlController.
	 * @param files the array of files.
	 * @param index, the index in the loop.
	 * @param path, the path from the file.
	 * @param currentDoc the current document.
	 */
	private void checkForXmlDocument(File[] files, int index, String path, XMLDocument currentDoc) {
		if (xmlController.getDocument(path) == null) {
			addDocumentAndShowInEditor(currentDoc);
			addFile(files[index].getName());
		} else {
			JOptionPane.showMessageDialog(null,
					"No project created yet, or no file specified, "
							+ "or file already in project!");
		}
	}

	/**
	 * Adds a XML file to the file tree and to the editor.
	 * @param path the source of the XML file.
	 */
	protected void addXmlFile(String path) {
		XMLParser parser = new XMLParser(path);
		try {
			parser.parse();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<XMLDocument> internDocs = parser.getDocuments();
		for (int i = 0; i < internDocs.size(); i++) {
			proccesXmlFile(internDocs, i);
		}
	}
	
	/**
	 * Method which processes the XML file.
	 * @param internDocs
	 * @param index
	 */
	private void proccesXmlFile(ArrayList<XMLDocument> internDocs, int index) {
		if (xmlController.getDocument(internDocs.get(index).getPath()) == null) {
			XMLDocument current = internDocs.get(index);
			addDocumentAndShowInEditor(current);
			addFile(xmlController.obtainFileName(current.getPath()));
		} else {
			JOptionPane.showMessageDialog(null,
					"No project created yet, or no file specified, "
							+ "or file already in project!");
		}
	}

	/**
	 * Adds the file with a path to all documents.
	 * @param path the path to a file.
	 * @return the current xml document.
	 */
	protected XMLDocument makeDocument(String path) {
		XMLDocument current = new XMLDocument();
		current.setPath(path);
		return current;
	}

	/**
	 * Adds document to all documents and show it, if it isn't already loaded.
	 * @param doc the xml document to be added.
	 */
	private void addDocumentAndShowInEditor(XMLDocument doc) {
		xmlController.addDocument(doc);
		xmledit.addXMLDocumentToContainerScrollPanel(doc);
	}

	/**
	 * Removes a project from all devices.
	 * @param project the project that must be removed.
	 */
	public void removeProject(String project) {
		int index = findFolderProject(project);
		if (index != -1) {
			folder.remove(index);
			ft.reloadProjects();
			xmlController.removeProject(project);
		}
	}

	/**
	 * Load the database.
	 */
	public void loadDatabase() {
		ArrayList<XMLDocument> xmlDocuments = xmledit.getAllXMLDocuments();
		xmlController.setDocumentsInProject(xmlDocuments);
		xmlController.save();
		XMLParser parser = new XMLParser(xmlController.getFileLocation());
		try {
			parser.parse();
			ArrayList<Integer> indexesOfSelectedFiles = xmlController
					.getIndexesOfSelectedFiles(parser.getDocuments());
			parser.createTables(indexesOfSelectedFiles);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Something went wrong, please check all fields!");
		}
		xmlController.updateDocuments(this, xmlDocuments);
	}
}
