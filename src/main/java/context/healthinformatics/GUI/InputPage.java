package context.healthinformatics.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import context.healthinformatics.Database.SingletonDb;
import context.healthinformatics.Parser.XMLParser;

/**
 * Class which represents one of the states for the variabel panel in the mainFrame.
 */
public class InputPage extends InterfaceHelper implements PanelState,
	Serializable, TreeSelectionListener {
	
	private static final long serialVersionUID = 1L;

	private MainFrame mf;
	private ArrayList<ArrayList<String>> folder;
	private ArrayList<String> xmlList;
	private ArrayList<String> selectedFiles;
	
	private JTextArea txt;
	private JButton projectButton;
	private JButton fileButton;
	private JButton selectButton;
	private JButton helpButton;
	private JButton analyseButton;
	private JPanel panel;
	private JComboBox<String> box;
	private JTree tree;
	private DefaultMutableTreeNode root;
	private DefaultTreeModel model;
	private JScrollPane treePane;
	private JFileChooser selecter;
	
	public static final int TXTFIELDWIDTH = 500;
	public static final int TXTFIELDHEIGHT = 30;
	public static final int BUTTONFONTSIZE = 15;
	public static final int DIMESIONHEIGHT = 150;
	public static final int DIMESIONWIDTH = 40;
	public static final int SECTION1HEIGHT = 100;
	public static final int PROJECTLABELFONTSIZE = 20;
	public static final int BUTTONINSETS = 10;
	public static final int THREE = 3;
	public static final int EIGHT = 8;
	public static final int FLAGLENGTH = 13;
	public static final int TREEPANEWIDTH = 700;
	public static final int TREEPANEHEIGHT = 300;
	public static final int SECTION3HEIGHT = 400;
	public static final int HELPBUTTONHEIGHT = 100;
	public static final int HELPBUTTONWIDTH = 300;
	public static final String COLOR = "#81DAF5";
	
	/**
	 * Constructor.
	 * @param m is the mainframe object
	 */
	public InputPage(MainFrame m) {
		mf = m;
		selectedFiles = new ArrayList<String>();
		folder = new ArrayList<ArrayList<String>>();
		xmlList = new ArrayList<String>();
		initXmlList();
	}

	/**
	 * @return Panel of this state.//////////////////////////////////////////////////////////////////
	 */
	public JPanel loadPanel() {
		panel = createSection(mf.getStatePanelSize());
		panel.add(loadProjectSelection(), setGrids(0, 0));
		panel.add(loadFileSelection(), setGrids(0, 1));
		panel.add(loadFolder(), setGrids(0, 2));
		GridBagConstraints c = setGrids(0, THREE);
		c.weighty = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		panel.add(loadButtonSection(), c);
		return panel;
	}
	
	/**
	 * @return section of the inputpage.
	 * @param height of the section
	 */
	public JPanel createSection(int height) {
		JPanel section = MainFrame.createPanel(Color.decode(COLOR),
				mf.getScreenWidth(), height);
		section.setLayout(new GridBagLayout());	
		return section;
	}
	
	/**
	 * @return section1 Panel.////////////////////////////////////////////////////////////////////////
	 */
	public JPanel loadProjectSelection() {
		JPanel section1 = createSection(SECTION1HEIGHT);
		addProjectLabel(section1);
		addCombobox(section1);
		addProjectButton(section1);
		return section1;
	}
	
	/**
	 * @param panel to which the projectlabel will be added.
	 */
	public void addProjectLabel(JPanel panel) {
		JLabel projectLabel = new JLabel("      Project :   ");
		projectLabel.setFont(new Font("Arial", Font.PLAIN, PROJECTLABELFONTSIZE));
		projectLabel.setSize(new Dimension(DIMESIONWIDTH, DIMESIONHEIGHT));
		panel.add(projectLabel, setGrids(0, 0));
	}
	
	/**
	 * @param panel to which the combobox will be added.
	 */
	public void addCombobox(JPanel panel) {
		box = new JComboBox<String>(getProjects());
		panel.add(box, setGrids(1, 0));
	}
	
	/**
	 * @param panel to which the projectButton will be added.
	 */
	public void addProjectButton(JPanel panel) {
		GridBagConstraints c = setGrids(2, 0);
		projectButton = createButton("ADD new Project", DIMESIONWIDTH, DIMESIONHEIGHT);
		projectButton.addActionListener(new ActionHandler());
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS,
				BUTTONINSETS, BUTTONINSETS);
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(projectButton, c);
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
	 * Method which asks the user to enter a new PROJECTS :, and inserts it in the combobox.
	 */
	public void addComboItem() {
		String newProject =  (String) JOptionPane.showInputDialog(panel,
				"New Project : ");
		if (findFolderProject(newProject) != -1) {
			JOptionPane.showMessageDialog(null, "Project name already exists!");
			return;
		}
		if (newProject != null) {
			ArrayList<String> list = new ArrayList<String>();
			list.add(newProject);
			folder.add(list);
			box.addItem(newProject);
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(newProject);
			DefaultMutableTreeNode xml = new DefaultMutableTreeNode("SET XML FILE");
			node.add(xml);
			model.insertNodeInto(node, root, root.getChildCount());
			xmlList.add(null);
			tree.expandRow(0);
			tree.expandRow(root.getChildCount() + root.getLeafCount() - 1);
		}
		else {
			JOptionPane.showMessageDialog(null, "No projects specified");
		}
	}
	
	/**
	 * @return section2 Panel.//////////////////////////////////////////////////////////////////////////
	 */
	public JPanel loadFileSelection() {
		JPanel section2 = createSection(SECTION1HEIGHT);
		addFileLabel(section2);
		addTextArea(section2);
		addSelectButton(section2);
		addFileButton(section2);
		return section2;
	}
	
	/**
	 * @param panel to which the fileLabel will be added.
	 */
	public void addFileLabel(JPanel panel) {
		JLabel fileLabel = new JLabel("      File :   ");
		fileLabel.setFont(new Font("Arial", Font.PLAIN, PROJECTLABELFONTSIZE));
		fileLabel.setSize(new Dimension(DIMESIONWIDTH, DIMESIONHEIGHT));
		panel.add(fileLabel, setGrids(0, 0));
	}
	
	/**
	 * @param panel to which the addTextArea will be added.
	 */
	public void addTextArea(JPanel panel) {
		txt = new JTextArea();
		txt.setMinimumSize(new Dimension(TXTFIELDWIDTH, TXTFIELDHEIGHT));
		panel.add(txt, setGrids(1, 0));
	}
	
	/**
	 * @param panel to which the addSelectButton will be added.
	 */
	public void addSelectButton(JPanel panel) {
		GridBagConstraints c = setGrids(2, 0);
		selectButton = createButton("SELECT", DIMESIONWIDTH, DIMESIONHEIGHT);
		selectButton.addActionListener(new ActionHandler());
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS, BUTTONINSETS);
		panel.add(selectButton, c);
	}
	
	/**
	 * @param panel to which the addFileButton will be added.
	 */
	public void addFileButton(JPanel panel) {
		GridBagConstraints c = setGrids(THREE, 0);
		fileButton = createButton("ADD new File", DIMESIONWIDTH, DIMESIONHEIGHT);
		fileButton.addActionListener(new ActionHandler());
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS, BUTTONINSETS);
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(fileButton, c);
	}
	
	/**
	 * @return The tree folder structure Panel./////////////////////////////////////////////////////////////////////////////
	 */
	public JPanel loadFolder() {
		JPanel section3 = MainFrame.createPanel(Color.decode(COLOR),
				mf.getScreenWidth(), SECTION3HEIGHT);
		section3.setLayout(new  GridBagLayout());
		initTree();
		addTreePane(section3);
		return section3;
	}
	
	/**
	 * @param panel to which the treePane will be added.
	 */
	public void addTreePane(JPanel panel) {
		treePane = new JScrollPane(tree);
        treePane.setPreferredSize(new Dimension(TREEPANEWIDTH, TREEPANEHEIGHT));
        GridBagConstraints c = setGrids(0, 0);
        c.weightx = 1;
        c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS, BUTTONINSETS);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(treePane, c);
	}
	
	/**
	 * Method which creates the tree and fills it with all the nodes currently in the folder.
	 */
	public void fillTree() {
		DefaultMutableTreeNode project = null;
		DefaultMutableTreeNode file = null;
		for (int i = 0; i < folder.size(); i++) {
			project = new DefaultMutableTreeNode(folder.get(i).get(0) + isXmlSet(i));
			for (int j = 1; j < folder.get(i).size(); j++) {
				String node = folder.get(i).get(j);
				file = new DefaultMutableTreeNode(node + isSelected(node));
				project.add(file);
			}
			DefaultMutableTreeNode xml = new DefaultMutableTreeNode("SET XML FILE");
			project.add(xml);
			root.add(project);
		}
	}
	
	/**
	 * Method which checks if the SML file for this project is set.
	 * @param i is the index of the project.
	 * @return empty String if the XML is not yet set, and return "   [SET]" otherwise.
	 */
	public String isXmlSet(int i) {
		if (getXmlList().get(i) == null) {
			return "";
		}
		else {
			return "   [SET]";
		}
	}
	
	/**
	 * Method which checks if the TreeNode is selected.
	 * @param node is the current node.
	 * @return empty String if the Node is not selcted, and return "   [SELECTED]" otherwise.
	 */
	public String isSelected(String node) {
		if (getSelectedFiles().contains(node)) {
			return "   [SELECTED]";
		}
		else {
			return "";
		}
	}
	
	/**
	 * Method which initialises the tree.
	 */
	public void initTree() {
		root = new DefaultMutableTreeNode("PROJECTS :");		
        fillTree();
        model = new DefaultTreeModel(root);
        tree = new JTree(model);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        tree.setVisible(true);
        tree.setLayout(new GridBagLayout());
        tree.addTreeSelectionListener(this);
	}
	
	/**
	 * Method which initialises the XML list.
	 */
	public void initXmlList() {
		for (int i = 0; i < folder.size(); i++) {
			xmlList.add(null);
		}
	}
	
	/**
	 * @return section4 Panel.
	 */
	public JPanel loadButtonSection() {
		JPanel section4 = createSection(SECTION3HEIGHT);
		addHelpButton(section4);
		addAnalyseButton(section4);
		return section4;
	}
	
	/**
	 * @param panel to which the addHelpButton will be added.
	 */
	public void addHelpButton(JPanel panel) {
		helpButton = createButton("HELP", HELPBUTTONWIDTH, HELPBUTTONHEIGHT);
		helpButton.addActionListener(new ActionHandler());
		GridBagConstraints c = setGrids(0, 0);
		c.weightx = 1;
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS, BUTTONINSETS);
		c.anchor = GridBagConstraints.WEST;
		panel.add(helpButton, c);
	}
	
	/**
	 * @param panel to which the addAnalyseButton will be added.
	 */
	public void addAnalyseButton(JPanel panel) {
		analyseButton = createButton("ANALYSE", HELPBUTTONWIDTH, HELPBUTTONHEIGHT);
		analyseButton.addActionListener(new ActionHandler());
		GridBagConstraints c = setGrids(0, 0);
		c.weightx = 1;
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS, BUTTONINSETS);
		c.anchor = GridBagConstraints.EAST;
		panel.add(analyseButton, c);
	}
	
	/**
	 * @return Arraylist of slected files.
	 */
	public ArrayList<String> getSelectedFiles() {
		return selectedFiles;
	}
	
	/**
	 * @return folder of projects and files.
	 */
	public ArrayList<ArrayList<String>> getFolder() {
		return folder;
	}
	
	/**
	 * @param  f a 2D array of projects and files.
	 */
	public void setFolder(ArrayList<ArrayList<String>> f) {
		folder = f;
	}
	
	/**
	 * @return root of the project tree.
	 */
	public DefaultMutableTreeNode getRoot() {
		return root;
	}
	
	/**
	 * @return model of the project tree.
	 */
	public DefaultTreeModel getModel() {
		return model;
	}
	
	/**
	 * @return the xmlList.
	 */
	public ArrayList<String> getXmlList() {
		return xmlList;
	}
	
	/**
	 * @return the analyseButton.
	 */
	public JButton getAnalyseButton() {
		return analyseButton;
	}
	
	/**
	 * @return the anwser of the filechooser.
	 */
	public int openFileChooser() {
		selecter = new JFileChooser();
		selecter.setDialogType(JFileChooser.SAVE_DIALOG);
		return selecter.showSaveDialog(panel);
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
	 * Class which handles the actions when buttons are clicked.
	 */
	class ActionHandler implements ActionListener {

		/**
		 * Method which is fired after a certain event.
		 * @param e event
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == projectButton) {
				addComboItem();
			}
			if (e.getSource() == fileButton) {
				if (folder.size() != 0 && !txt.getText().equals("")) {
					String text = txt.getText();
					int project = findFolderProject((String) box.getSelectedItem());
					folder.get(project).add(text);
					DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(text);
					model.insertNodeInto(newNode, (MutableTreeNode) root.getChildAt(project)
							, root.getChildAt(project).getChildCount());
				} else {
					JOptionPane.showMessageDialog(null,
							"No project created yet, or no file specified!");
				}
			}
			if (e.getSource() == selectButton) {
				if (openFileChooser() == JFileChooser.APPROVE_OPTION) {
				    String path = selecter.getSelectedFile().toString();
				    txt.setText(path);
				}
				selecter.setVisible(false);
			}
			if (e.getSource() == helpButton) {
				return; //TODO
			}
			if (e.getSource() == analyseButton) {
				loadDatabase();
				mf.setState(mf.getCodePage());
				mf.reloadStatePanel();
			}
		}
	}

	/**
	 * Method which is fired after a node selection.
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		if (node != null && node.isLeaf()
				&& !node.getUserObject().toString().equals("PROJECTS :")) {
			String selected = node.getUserObject().toString();
			useSelectedTreeNode(selected, node);
		}
	}
	
	/**
	 * Do something with the selected TreeNode.
	 * @param selected is the string of the selected TreeNode.
	 * @param node is the selected TreeNode.
	 */
	public void useSelectedTreeNode(String selected, DefaultMutableTreeNode node) {
		if (selected.equals("SET XML FILE")) {
			addXmlFile(node);
		} else {
			selectNode(selected, node);
		}
	}
	
	/**
	 * Method which asks the user for an xml file and updates the tree.
	 * @param node is the selected treeNode.
	 */
	public void addXmlFile(DefaultMutableTreeNode node) {
		askUserForXml(node);
		model.nodeStructureChanged(node.getParent());
	}
	
	/**
	 * Method which asks the user.
	 * @param node is the node that is selected.
	 */
	public void askUserForXml(DefaultMutableTreeNode node) {
		if (openFileChooser() == JFileChooser.APPROVE_OPTION) {
		    String path = selecter.getSelectedFile().toString();
			if (!path.equals("")) {
				setProjectNodeToSelected(path, node);
			}
		}
		selecter.setVisible(false);
	}
	
	/**
	 * Set the selected projectNode to Set.
	 * @param path is the entered XML file path.
	 * @param node is the selected TreeNode.
	 */
	public void setProjectNodeToSelected(String path, DefaultMutableTreeNode node) {
		DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) node
	    		.getParent();
		String temp = projectNode.getUserObject().toString();
		String project = checkProjectNodeString(temp);
	    int c = findFolderProject(project);
		if (xmlList.get(c) == null) {
			projectNode.setUserObject(projectNode.getUserObject()
					.toString() + "   [SET]");
		}
		xmlList.set(c, path);
	}
	
	/**
	 * Check the string of the projectNode.
	 * @param string is the string of the selected TreeNode.
	 * @return the string of the projectNode.
	 */
	public String checkProjectNodeString(String string) {
		if (string.length() > EIGHT && string.substring(
				string.length() - EIGHT).equals("   [SET]")) {
			return string.substring(0, string.length() - EIGHT);
		} else {
			return string;
		}
	}
	
	/**
	 * Set the selected TreeNode to selected.
	 * @param selected is the string of the selected TreeNode.
	 * @param node is the selected TreeNode.
	 */
	public void selectNode(String selected, DefaultMutableTreeNode node) {
		String flag = checkLengthOfNodeString(selected);
		if (flag != "" && flag.equals("   [SELECTED]")) {
			setNodeToNotSelected(selected, node);
		} else {
			setNodeToSelected(selected, node);
		}
	}
	
	/**
	 * Do something with the selected TreeNode.
	 * @param selected is the string of the selected TreeNode.
	 * @return the flag String.
	 */
	public String checkLengthOfNodeString(String selected) {
		if (selected.length() > FLAGLENGTH) {
			return selected.substring(selected.length() - FLAGLENGTH);
		} else {
			return "";
		}
	}
	
	/**
	 * Set the selected TreeNode to selected.
	 * @param selected is the string of the selected TreeNode.
	 * @param node is the selected TreeNode.
	 */
	public void setNodeToSelected(String selected, DefaultMutableTreeNode node) {
		selectedFiles.add(selected);
		node.setUserObject(node.getUserObject().toString()
				+ "   [SELECTED]");
		model.nodeStructureChanged(node.getParent());
	}
	
	/**
	 * Set the selected TreeNode to NOT selected.
	 * @param selected is the string of the selected TreeNode.
	 * @param node is the selected TreeNode.
	 */
	public void setNodeToNotSelected(String selected, DefaultMutableTreeNode node) {
		selectedFiles.remove(selected);
		String s = node.getUserObject().toString();
		node.setUserObject(s.substring(0, s.length() - FLAGLENGTH));
		model.nodeStructureChanged(node.getParent());
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
				e1.printStackTrace(); //TODO exception handling
			}
		}
	}
}
