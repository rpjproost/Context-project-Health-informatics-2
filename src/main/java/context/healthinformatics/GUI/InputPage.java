package context.healthinformatics.GUI;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import context.healthinformatics.Database.SingletonDb;
import context.healthinformatics.Parser.XMLParser;
import context.healthinformatics.Writer.XMLDocument;

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
	private ArrayList<XMLDocument> xmlDocs;

	private JPanel leftPanel;
	private JFileChooser selecter;

	public static final int BUTTONFONTSIZE = 15;
	public static final int THREE = 3;
	public static final String COLOR = "#81DAF5";

	private XMLEditor xmledit;

	/**
	 * Constructor.
	 * 
	 * @param m
	 *            is the mainframe object
	 * @throws IOException 
	 */
	public InputPage(MainFrame m) {
		mf = m;
		folder = new ArrayList<ArrayList<String>>();
		ft = new FileTree(m, this);
		ipc = new InputPageComponents(m, this);
		xmledit = new XMLEditor();
		try {
			XMLParser parser = new XMLParser("src/main/data/demo/demo.xml");
			parser.parse();
			xmlDocs = parser.getXMLDocs();
			addComboItem("ADMIRE");
			for (int i = 0; i < xmlDocs.size(); i++) {
				int project = findFolderProject((String) ipc.getComboBox()
						.getSelectedItem());
				folder.get(project).add(xmlDocs.get(i).getPath());
				ft.addFileToTree(project, xmlDocs.get(i).getPath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return Panel of the InputPage state.
	 */
	public JPanel loadPanel() {
		JPanel containerPanel = createPanel(Color.decode(COLOR),
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
		JPanel leftPanel = createPanel(Color.decode(COLOR),
				mf.getScreenWidth() / 2, mf.getStatePanelSize());
		leftPanel.add(ipc.loadProjectSelection(), setGrids(0, 0));
		leftPanel.add(ipc.loadFileSelection(), setGrids(0, 1));
		leftPanel.add(ft.loadFolder(), setGrids(0, 2));
		GridBagConstraints c = setGrids(1, 0);
		c = setGrids(0, THREE);
		c.weighty = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		leftPanel.add(ipc.loadHelpButtonSection(), c);
		return leftPanel;
	}

	/**
	 * Create the right panel of the input page.
	 * 
	 * @return the right panel
	 */
	public JPanel createRightPanel() {
		JPanel rightPanel = createPanel(Color.decode(COLOR),
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
		if (newProject != null) {
			addComboItem(newProject);
		} else {
			JOptionPane.showMessageDialog(null, "No projects specified");
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
		ft.addProjectToTree(project);
	}

	/**
	 * @return the anwser of the filechooser.
	 */
	public int openFileChooser() {
		selecter = new JFileChooser();
		selecter.setDialogType(JFileChooser.SAVE_DIALOG);
		return selecter.showSaveDialog(leftPanel);
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
	 * @param f
	 *            a 2D array of projects and files.
	 */
	public void setFolder(ArrayList<ArrayList<String>> f) {
		folder = f;
	}

	/**
	 * Method which adds a project to the folder and FileTree.
	 */
	public void addFile() {
		if (folder.size() != 0 && !ipc.getTextArea().getText().equals("")) {
			String text = ipc.getTextArea().getText();
			int project = findFolderProject((String) ipc.getComboBox()
					.getSelectedItem());
			folder.get(project).add(text);
			ft.addFileToTree(project, text);
		} else {
			JOptionPane.showMessageDialog(null,
					"No project created yet, or no file specified!");
		}
	}

	/**
	 * Load the database from a xml file when there isn't already one.
	 */
	protected void loadDatabase() {
		if (SingletonDb.getDb().getTables().isEmpty()) {
			XMLParser xmlp = new XMLParser("src/main/data/demo/demo.xml");
			try {
				xmlp.parse();
			} catch (IOException e1) {
				e1.printStackTrace(); // TODO exception handling
			}
		}
	}
}
