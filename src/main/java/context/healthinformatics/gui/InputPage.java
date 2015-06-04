package context.healthinformatics.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import context.healthinformatics.interfacecomponents.FileTree;
import context.healthinformatics.interfacecomponents.InputPageComponents;
import context.healthinformatics.interfacecomponents.XMLEditor;
import context.healthinformatics.interfacecomponents.XMLEditorController;
import context.healthinformatics.parser.XMLParser;
import context.healthinformatics.writer.XMLDocument;

/**
 * Class which represents one of the states for the variabel panel in the
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

	private static final int SELECTERHEIGHT = 500;
	private static final int SELECTERWIDTH = 600;

	private XMLEditor xmledit;
	private XMLEditorController xmlController;

	/**
	 * Constructor.
	 * 
	 * @param m
	 *            is the mainframe object
	 */
	public InputPage(MainFrame m) {
		mf = m;
		folder = new ArrayList<ArrayList<String>>();
		xmledit = new XMLEditor(this);
		xmlController = new XMLEditorController();
		ipc = new InputPageComponents(mf, this);
		checkOnFiles();
	}

	/**
	 * 
	 */
	private void checkOnFiles() {
		File directory = new File("src/main/data/savedXML/");
		File[] listOfFiles = directory.listFiles();
		if (listOfFiles.length > 0) {
			foundFiles(listOfFiles);
		} else {
			runClearedProject();
		}
	}

	/**
	 * Founded files will be parsed and will be added to the interface.
	 * 
	 * @param listOfFiles
	 *            list of files that are found by java.
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
	 * 
	 * @param project
	 *            the project which should be added.
	 * @param docsOfFile
	 *            all files that are belongs to the project.
	 */
	private void runOldProject(String project, ArrayList<XMLDocument> docsOfFile) {
		xmlController.setProject(project);
		xmlController.setDocumentsInProject(docsOfFile);
		folder.add(xmlController.breakDownXMLDocumentsIntoNames(docsOfFile));
		ft = new FileTree(mf, this);
		ipc.getComboBox().addItem(project);
		ipc.getComboBox().setSelectedItem(project);
		xmlController.loadProject(xmledit);
		ft.expandTree();
	}

	/**
	 * Creates a clear project with a standard project default.
	 */
	private void runClearedProject() {
		ft = new FileTree(mf, this);
		xmlController.setProject("(default)");
		addComboItem("(default)");
	}

	/**
	 * @return Panel of the InputPage state.
	 */
	public JPanel loadPanel() {
		JPanel containerPanel = createPanel(MainFrame.INPUTTABCOLOR,
				mf.getScreenWidth(), mf.getStatePanelSize());
		leftPanel = createLeftPanel();
		JPanel rightPanel = createRightPanel();
		containerPanel.add(leftPanel);
		containerPanel.add(rightPanel);
		return containerPanel;
	}

	/**
	 * Create the left panel of the input page.
	 * 
	 * @return the left panel
	 */
	public JPanel createLeftPanel() {
		JPanel leftPanel = createPanel(MainFrame.INPUTTABCOLOR,
				mf.getScreenWidth() / 2, mf.getStatePanelSize());
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
	 * 
	 * @return the right panel
	 */
	public JPanel createRightPanel() {
		JPanel rightPanel = createPanel(MainFrame.INPUTTABCOLOR,
				mf.getScreenWidth() / 2, mf.getStatePanelSize());
		rightPanel.add(xmledit.loadPanel(), setGrids(0, 0));

		rightPanel.add(ipc.loadAnalyzeButtonSection(), setGrids(0, 1));
		return rightPanel;
	}

	/**
	 * Method which creates the list of projects.
	 * 
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
	 * Method which asks the user to enter a new PROJECTS :, and inserts it in
	 * the combobox.
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
	 * 
	 * @param project
	 *            is the name of the project.
	 */
	public void addComboItem(String project) {
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
	public int openFileChooser() {
		selecter = new JFileChooser();
		selecter.setMultiSelectionEnabled(true);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Only txt, excel and xml", "txt", "csv", "xlsx", "xls", "xml");
		selecter.setFileFilter(filter);
		selecter.setPreferredSize(new Dimension(SELECTERWIDTH, SELECTERHEIGHT));
		selecter.setFileSelectionMode(JFileChooser.FILES_ONLY);
		Action details = selecter.getActionMap().get("viewTypeDetails");
		details.actionPerformed(null);
		return selecter.showOpenDialog(leftPanel);
	}

	/**
	 * Method which finds the project in the folder.
	 * 
	 * @return index of project.
	 * @param s
	 *            is string of project.
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
	 * @return the left panel.
	 */
	public JPanel getLeftPanel() {
		return leftPanel;
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
	 * @return selecter.
	 */
	public JFileChooser getSelecter() {
		return selecter;
	}

	/**
	 * @return the XML editor.
	 */
	public XMLEditor getEditor() {
		return xmledit;
	}

	/**
	 * Method which adds a project to the folder and FileTree.
	 * 
	 * @param path
	 *            is the path of the added file.
	 */
	public void addFile(String path) {
		int project = findFolderProject((String) ipc.getComboBox()
				.getSelectedItem());
		if (folder.size() > 0 && project >= 0 && !path.equals("")
				&& !folder.get(project).contains(path)) {
			folder.get(project).add(path);
			ft.addFileToTree(project, path);
		} else {
			JOptionPane.showMessageDialog(null,
					"No project created yet, or no file specified, "
							+ "or file already in project!");
		}
	}

	/**
	 * Open files and adds them to the tree.
	 * 
	 * @param files
	 *            all files that should be added.
	 */
	public void openFiles(File[] files) {
		for (int i = 0; i < files.length; i++) {
			String path = files[i].getPath();
			XMLDocument currentDoc = makeDocument(path);
			if (!path.endsWith("xml")) {
				if (xmlController.getDocument(path) == null) {
					addDocumentAndShowInEditor(currentDoc);
					addFile(files[i].getName());
				}
			} else {
				addXmlFile(path);
			}
		}
	}

	/**
	 * Adds a xml file to the file tree and to the editor.
	 * 
	 * @param path
	 *            the source of the xml file.
	 */
	private void addXmlFile(String path) {
		XMLParser parser = new XMLParser(path);
		try {
			parser.parse();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<XMLDocument> internDocs = parser.getDocuments();
		for (int i = 0; i < internDocs.size(); i++) {
			XMLDocument current = internDocs.get(i);
			addDocumentAndShowInEditor(current);
			addFile(xmlController.obtainFileName(current.getPath()));
		}
	}

	/**
	 * Adds the file with a path to all documents.
	 * 
	 * @param path
	 *            the path to a file.
	 * @return
	 */
	private XMLDocument makeDocument(String path) {
		XMLDocument current = new XMLDocument();
		current.setPath(path);
		return current;
	}

	/**
	 * Adds document to all documents and show it, if it isn't already loaded.
	 * 
	 * @param doc
	 *            the xml document to be added.
	 */
	private void addDocumentAndShowInEditor(XMLDocument doc) {
		xmlController.addDocument(doc);
		xmledit.addXMLDocumentToContainerScrollPanel(doc);
	}

	/**
	 * Removes a project from all devices.
	 * 
	 * @param project
	 *            the project that must be removed.
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
		if (!xmledit.checkAllXMLDocumentsOnError()) {
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

}
