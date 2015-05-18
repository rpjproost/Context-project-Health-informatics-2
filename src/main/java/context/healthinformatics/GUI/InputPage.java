package context.healthinformatics.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.tree.TreeSelectionModel;

/**
 * Class which represents one of the states for the variabel panel in the mainFrame.
 */
public class InputPage extends InterfaceHelper implements PanelState,
	Serializable, TreeSelectionListener {
	
	private static final long serialVersionUID = 1L;

	private MainFrame mf;
	private ArrayList<ArrayList<String>> folder;
	private ArrayList<String> xmlList;
	
	private JTextArea txt;
	private JButton projectButton;
	private JButton fileButton;
	private JButton selectButton;
	private JButton helpButton;
	private JButton analyseButton;
	private ArrayList<String> selectedFiles;
	private Dimension dim;
	private GridBagConstraints c;
	private GridBagLayout l;
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
	public static final int HELPBUTTONWIDTH = 200;
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
		
		//////////test
		folder.add(new ArrayList<String>());
		folder.get(0).add("1");
		folder.get(0).add("2");
		folder.get(0).add("3");
		folder.add(new ArrayList<String>());
		folder.get(1).add("4");
		folder.get(1).add("5");
		folder.get(1).add("6");
		////////////////////
		
		initXmlList();
	}

	/**
	 * @return Panel of this state.
	 */
	public JPanel loadPanel() {
		panel = createSection(mf.getStatePanelSize());
		
		JPanel section1 = makeSection1();
		panel.add(section1, setGrids(0, 0));
		
		JPanel section2 = makeSection2();
		panel.add(section2, setGrids(0, 1));
		
		JPanel section3 = makeSection3();
		panel.add(section3, setGrids(0, 2));
		
		JPanel section4 = makeSection4();
		c = setGrids(0, THREE);
		c.weighty = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		panel.add(section4, c);
		
		return panel;
	}
	
	/**
	 * @return section of the inputpage.
	 * @param height of the section
	 */
	public JPanel createSection(int height) {
		JPanel section = MainFrame.createPanel(Color.decode(COLOR),
				mf.getScreenWidth(), height);
		c = new GridBagConstraints();
		l = new GridBagLayout();
		section.setLayout(l);
		dim = new Dimension(DIMESIONHEIGHT, DIMESIONWIDTH);		
		return section;
	}
	
	/**
	 * @return section1 Panel.
	 */
	public JPanel makeSection1() {
		JPanel section1 = createSection(SECTION1HEIGHT);
		
		JLabel projectLabel = new JLabel("      Project :   ");
		projectLabel.setFont(new Font("Arial", Font.PLAIN, PROJECTLABELFONTSIZE));
		projectLabel.setSize(dim);
		section1.add(projectLabel, setGrids(0, 0));
		
		box = new JComboBox<String>(getProjects());
		section1.add(box, setGrids(1, 0));
		
		projectButton = makeButton("ADD new Project", 2, 0);
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS,
				BUTTONINSETS, BUTTONINSETS);
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		section1.add(projectButton, c);
		
		return section1;
	}
	
	/**
	 * @return section2 Panel.
	 */
	public JPanel makeSection2() {
		JPanel section2 = createSection(SECTION1HEIGHT);
		
		JLabel fileLabel = new JLabel("      File :   ");
		fileLabel.setFont(new Font("Arial", Font.PLAIN, PROJECTLABELFONTSIZE));
		fileLabel.setSize(dim);
		section2.add(fileLabel, setGrids(0, 0));
		
		txt = new JTextArea();
		txt.setMinimumSize(new Dimension(TXTFIELDWIDTH, TXTFIELDHEIGHT));
		section2.add(txt, setGrids(1, 0));
		
		selectButton = makeButton("SELECT", 2, 0);
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS, BUTTONINSETS);
		section2.add(selectButton, c);
		
		fileButton = makeButton("ADD new File", THREE, 0);
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS, BUTTONINSETS);
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		section2.add(fileButton, c);
		return section2;
	}
	
	/**
	 * @return section3 Panel.
	 */
	public JPanel makeSection3() {
		JPanel section3 = MainFrame.createPanel(Color.decode(COLOR),
				mf.getScreenWidth(), SECTION3HEIGHT);
		c = new GridBagConstraints();
		section3.setLayout(l);

		initTree();

        treePane = new JScrollPane(tree);
        dim.width = TREEPANEWIDTH;
        dim.height = TREEPANEHEIGHT;
        treePane.setPreferredSize(dim);
		
        c = setGrids(0, 0);
        c.weightx = 1;
        c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS, BUTTONINSETS);
		c.anchor = GridBagConstraints.LINE_START;
		section3.add(treePane, c);
		return section3;
	}
	
	/**
	 * @return section4 Panel.
	 */
	public JPanel makeSection4() {
		JPanel section4 = createSection(SECTION3HEIGHT);
		
		helpButton = createButton("HELP", HELPBUTTONHEIGHT, HELPBUTTONWIDTH);
		helpButton.addActionListener(new ActionHandler());
		c = setGrids(0, 0);
		c.weightx = 1;
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS, BUTTONINSETS);
		c.anchor = GridBagConstraints.WEST;
		section4.add(helpButton, c);
		
		analyseButton = createButton("ANALYSE", HELPBUTTONHEIGHT, HELPBUTTONWIDTH);
		analyseButton.addActionListener(new ActionHandler());
		c = setGrids(0, 1);
		c.weightx = 1;
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS, BUTTONINSETS);
		c.anchor = GridBagConstraints.EAST;
		section4.add(analyseButton, c);

		return section4;
	}
	
	/**
	 * Method which creates the tree and fills it with all the nodes currently in the folder.
	 */
	public void fillTree() {
		DefaultMutableTreeNode project = null;
		DefaultMutableTreeNode file = null;
		for (int i = 0; i < folder.size(); i++) {
			project = new DefaultMutableTreeNode(folder.get(i).get(0));
			for (int j = 1; j < folder.get(i).size(); j++) {
				file = new DefaultMutableTreeNode(folder.get(i).get(j));
				project.add(file);
			}
			DefaultMutableTreeNode xml = new DefaultMutableTreeNode("SET XML FILE");
			project.add(xml);
			root.add(project);
		}
	}
	
	/**
	 * Method which initialises the tree.
	 */
	public void initTree() {
		root = new DefaultMutableTreeNode("PROJECT NAME");		
        fillTree();
        model = new DefaultTreeModel(root);
        tree = new JTree(model);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        tree.setVisible(true);
        tree.setLayout(l);
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
	 * Method which reloades the tree.
	 */
	public void reloadTree() {
		root.removeAllChildren();
		fillTree();
		model.reload();
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
	 * @return the project button.
	 */
	public JButton getProjectButton() {
		return projectButton;
	}
	
	/**
	 * @return the file button.
	 */
	public JButton getFileButton() {
		return fileButton;
	}
	
	/**
	 * @return the select button.
	 */
	public JButton getselectButton() {
		return selectButton;
	}
	
	/**
	 * @return the help button.
	 */
	public JButton gethelpButton() {
		return helpButton;
	}
	
	/**
	 * @return the analyse button.
	 */
	public JButton getanalyseButton() {
		return analyseButton;
	}
	
	/**
	 * @return the xmlList.
	 */
	public ArrayList<String> getXmlList() {
		return xmlList;
	}
	
	/**
	 * Method which asks the user to enter a new Project name, and inserts it in the combobox.
	 */
	public void addComboItem() {
		String newProject =  (String) JOptionPane.showInputDialog(panel,
				"New Project Name : ");
		ArrayList<String> list = new ArrayList<String>();
		list.add(newProject);
		folder.add(list);
		box.addItem(newProject);
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(newProject);
		model.insertNodeInto(node, root, root.getChildCount());
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
		System.out.println("no project selected.");
		return -1;
	}
	
	/**
	 * Method which creates a button.
	 * @param txt text on the button
	 * @param c1 gridx coordinate.
	 * @param c2 gridy coordinate.
	 * @return button.
	 */
	public JButton makeButton(String txt, int c1, int c2) {
		JButton button = new JButton(txt);
		
		c.gridx = c1;
		c.gridy = c2;
		button.setPreferredSize(dim);
		button.addActionListener(new ActionHandler());
		button.setFont(new Font("Arial", Font.PLAIN, BUTTONFONTSIZE));
		return button;
	}
	
	/**
	 * Method which asks the user.
	 * @param node is the node that is selected.
	 */
	public void askUser(DefaultMutableTreeNode node) {
		if (openFileChooser() == JFileChooser.APPROVE_OPTION) {
		    String path = selecter.getSelectedFile().toString();
			if (!path.equals("")) {
				DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) node
			    		.getParent();
				String temp = projectNode.getUserObject().toString();
				String project = "";
				if (temp.length() > EIGHT && temp.substring(
						temp.length() - EIGHT).equals("   [SET]")) {
					project = temp.substring(0, temp.length() - EIGHT);
				} else {
					project = temp;
				}
			    int c = findFolderProject(project);
				if (xmlList.get(c) == null) {
					projectNode.setUserObject(projectNode.getUserObject()
							.toString() + "   [SET]");
				}
				xmlList.set(c, path);
			}
		}
		selecter.setVisible(false);
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
				xmlList.add(null);
			}
			if (e.getSource() == fileButton) {
				folder.get(findFolderProject((String) box.getSelectedItem())).add(txt.getText());
				reloadTree();
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
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if (node != null) {
			if (node.isLeaf()) {
				String selected = node.getUserObject().toString();
				if (selected.equals("SET XML FILE")) {
					askUser(node);
					model.nodeStructureChanged(node.getParent());
				} else {
					String flag;
					if (selected.length() > FLAGLENGTH) {
						flag = selected.substring(selected.length() - FLAGLENGTH);
					} else {
						flag = null;
					}
					if (flag != null && flag.equals("   [SELECTED]")) {
						selectedFiles.remove(selected);
						String s = node.getUserObject().toString();
						node.setUserObject(s.substring(0, s.length() - FLAGLENGTH));
						model.nodeStructureChanged(node.getParent());
					} else {
						selectedFiles.add(selected);
						node.setUserObject(node.getUserObject().toString() + "   [SELECTED]");
						model.nodeStructureChanged(node.getParent());
					}
				}
			}
		}
	}
}
