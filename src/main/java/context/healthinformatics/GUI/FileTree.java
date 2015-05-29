package context.healthinformatics.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

/**
 * Class which represents the FileTree on the InputPage.
 */
public class FileTree implements TreeSelectionListener, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private MainFrame mf;
	private InputPage ip;
	private JTree tree;
	private DefaultMutableTreeNode root;
	private DefaultTreeModel model;
	private ArrayList<String> xmlList;
	private ArrayList<String> selectedFiles;
	
	public static final int FOLDERSECTIONHEIGHT = 400;
	public static final int TREEPANEWIDTH = 700;
	public static final int TREEPANEHEIGHT = 300;
	public static final int FLAGLENGTH = 13;
	public static final int EIGHT = 8;
	
	/**
	 * Constructor.
	 * @param m is the mainframe object.
	 * @param p is the InptPage object.
	 */
	public FileTree(MainFrame m, InputPage p) {
		mf = m;
		ip = p;
		selectedFiles = new ArrayList<String>();
		xmlList = new ArrayList<String>();
		initXmlList();
	}
	
	/**
	 * Method which initialises the XML list.
	 */
	public void initXmlList() {
		for (int i = 0; i < ip.getFolder().size(); i++) {
			xmlList.add(null);
		}
	}

	/**
	 * @return The tree folder structure Panel.
	 */
	public JPanel loadFolder() {
		JPanel section3 = MainFrame.createPanel(Color.decode(InputPage.COLOR),
				mf.getScreenWidth(), FOLDERSECTIONHEIGHT);
		section3.setLayout(new  GridBagLayout());
		initTree();
		addTreePane(section3);
		return section3;
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
	 * Method which creates the tree and fills it with all the nodes currently in the folder.
	 */
	public void fillTree() {
		DefaultMutableTreeNode project = null;
		DefaultMutableTreeNode file = null;
		for (int i = 0; i < ip.getFolder().size(); i++) {
			project = new DefaultMutableTreeNode(ip.getFolder().get(i).get(0) + isXmlSet(i));
			for (int j = 1; j < ip.getFolder().get(i).size(); j++) {
				String node = ip.getFolder().get(i).get(j);
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
	private String isXmlSet(int i) {
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
	private String isSelected(String node) {
		if (getSelectedFiles().contains(node)) {
			return "   [SELECTED]";
		}
		else {
			return "";
		}
	}
	
	/**
	 * @param panel to which the treePane will be added.
	 */
	private void addTreePane(JPanel panel) {
		JScrollPane treePane = new JScrollPane(tree);
        treePane.setPreferredSize(new Dimension(TREEPANEWIDTH, TREEPANEHEIGHT));
        GridBagConstraints c = ip.setGrids(0, 0);
        c.weightx = 1;
        c.insets = new Insets(InputPageComponents.BUTTONINSETS, InputPageComponents.BUTTONINSETS
        		, InputPageComponents.BUTTONINSETS, InputPageComponents.BUTTONINSETS);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(treePane, c);
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
	private void useSelectedTreeNode(String selected, DefaultMutableTreeNode node) {
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
	private void askUserForXml(DefaultMutableTreeNode node) {
		if (ip.openFileChooser() == JFileChooser.APPROVE_OPTION) {
		    String path = ip.getFileSelecter().getSelectedFile().toString();
			if (!path.equals("")) {
				setProjectNodeToSelected(path, node);
			}
		}
		ip.getFileSelecter().setVisible(false);
	}
	
	/**
	 * Set the selected projectNode to Set.
	 * @param path is the entered XML file path.
	 * @param node is the selected TreeNode.
	 */
	private void setProjectNodeToSelected(String path, DefaultMutableTreeNode node) {
		DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) node
	    		.getParent();
		String temp = projectNode.getUserObject().toString();
		String project = checkProjectNodeString(temp);
	    int c = ip.findFolderProject(project);
		if (getXmlList().get(c) == null) {
			projectNode.setUserObject(projectNode.getUserObject()
					.toString() + "   [SET]");
		}
		getXmlList().set(c, path);
	}
	
	/**
	 * Check the string of the projectNode.
	 * @param string is the string of the selected TreeNode.
	 * @return the string of the projectNode.
	 */
	private String checkProjectNodeString(String string) {
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
	private void selectNode(String selected, DefaultMutableTreeNode node) {
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
	private String checkLengthOfNodeString(String selected) {
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
	private void setNodeToSelected(String selected, DefaultMutableTreeNode node) {
		getSelectedFiles().add(selected);
		node.setUserObject(node.getUserObject().toString()
				+ "   [SELECTED]");
		model.nodeStructureChanged(node.getParent());
	}
	
	/**
	 * Set the selected TreeNode to NOT selected.
	 * @param selected is the string of the selected TreeNode.
	 * @param node is the selected TreeNode.
	 */
	private void setNodeToNotSelected(String selected, DefaultMutableTreeNode node) {
		getSelectedFiles().remove(selected);
		String s = node.getUserObject().toString();
		node.setUserObject(s.substring(0, s.length() - FLAGLENGTH));
		model.nodeStructureChanged(node.getParent());
	}
	
	/**
	 * add file to the tree.
	 * @param project is the index of the project.
	 * @param text is the name of the file.
	 */
	public void addFileToTree(int project, String text) {
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(text);
		model.insertNodeInto(newNode,
				(MutableTreeNode) root.getChildAt(project)
				, root.getChildAt(project).getChildCount());
	}
	
	/**
	 * add project to the tree.
	 * @param project is the name of the project.
	 */
	public void addProjectToTree(String project) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(project);
		DefaultMutableTreeNode xml = new DefaultMutableTreeNode("SET XML FILE");
		node.add(xml);
		model.insertNodeInto(node, root, root.getChildCount());
		xmlList.add(null);
		tree.expandRow(0);
		tree.expandRow(root.getChildCount() + root.getLeafCount() - 1);
	}
	
	/**
	 * @return Arraylist of slected files.
	 */
	public ArrayList<String> getSelectedFiles() {
		return selectedFiles;
	}
	
	/**
	 * @return the xmlList.
	 */
	public ArrayList<String> getXmlList() {
		return xmlList;
	}
	
	/**
	 * @return the tree.
	 */
	public JTree getTree() {
		return tree;
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
}
