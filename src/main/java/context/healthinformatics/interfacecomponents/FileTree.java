package context.healthinformatics.interfacecomponents;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import context.healthinformatics.gui.InputPage;
import context.healthinformatics.gui.MainFrame;
import context.healthinformatics.writer.XMLDocument;

/**
 * Class which represents the FileTree on the InputPage.
 */
public class FileTree implements TreeSelectionListener, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private InputPage ip;
	private JPanel folderPanel;
	private JTree tree;
	private JScrollPane treePane;
	private DefaultMutableTreeNode root;
	private DefaultTreeModel model;
	
	public static final int FOLDERSECTIONHEIGHT = 400;
	public static final int TREEPANEWIDTH = 700;
	public static final int TREEPANEHEIGHT = 300;
	public static final int BUTTONINSETS = 10;
	public static final int FLAGLENGTH = 13;
	public static final int EIGHT = 8;
	
	/**
	 * Constructor.
	 * @param p is the InptPage object.
	 */
	public FileTree(InputPage p) {
		ip = p;
		initTree();
	}

	/**
	 * @return The tree folder structure Panel.
	 */
	public JPanel loadFolder() {
		folderPanel = MainFrame.createPanel(MainFrame.INPUTTABCOLOR,
				ip.getScreenWidth(), FOLDERSECTIONHEIGHT);
		folderPanel.setLayout(new  GridBagLayout());
		loadFileSelectionTitle(folderPanel);
		addTreePane(folderPanel);
		return folderPanel;
	}
	
	/**
	 * Loads a title for the file selection part.
	 * @param titlePanel where the title for file selection must come.
	 */
	public void loadFileSelectionTitle(JPanel titlePanel) {
		JLabel title = ip.createTitle("File Selection:");
		GridBagConstraints c = ip.setGrids(0, 0);
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS,
				BUTTONINSETS);
		c.anchor = GridBagConstraints.LAST_LINE_START;
		titlePanel.add(title, c);
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
			project = new DefaultMutableTreeNode(ip.getFolder().get(i).get(0));
			for (int j = 1; j < ip.getFolder().get(i).size(); j++) {
				String node = ip.getFolder().get(i).get(j);
				file = new DefaultMutableTreeNode(node);
				project.add(file);
			}
			root.add(project);
		}
	}
	
	/**
	 * @param panel to which the treePane will be added.
	 */
	private void addTreePane(JPanel panel) {
		treePane = new JScrollPane(tree);
        treePane.setPreferredSize(new Dimension(TREEPANEWIDTH, TREEPANEHEIGHT));
        GridBagConstraints c = ip.setGrids(0, 1);
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
		if (node != null && node.isLeaf() && !node.equals(getRoot())
				&& !node.getParent().equals(getRoot())) {
			String selected = node.getUserObject().toString();
			selectNode(selected, node);
		} else if (node != null && !node.equals(getRoot())
				&& node.getParent().equals(getRoot())) {
			String selected = node.getUserObject().toString();
			ip.getInputPageComponent().getComboBox().setSelectedItem(selected);
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
		DefaultMutableTreeNode project = (DefaultMutableTreeNode) node.getParent();
		XMLDocument doc = ip.getXMLController().getDocumentWithPartofPath(
				project.getUserObject().toString(), selected);
		if (doc != null) {
			ip.getXMLController().selectDocument(doc.getPath());
			node.setUserObject(node.getUserObject().toString()
					+ "   [SELECTED]");
			model.nodeStructureChanged(node.getParent());
		}
	}
	
	/**
	 * Set the selected TreeNode to NOT selected.
	 * @param selected is the string of the selected TreeNode.
	 * @param node is the selected TreeNode.
	 */
	private void setNodeToNotSelected(String selected, DefaultMutableTreeNode node) {
		String origin = selected.substring(0, selected.length() - FLAGLENGTH);
		DefaultMutableTreeNode project = (DefaultMutableTreeNode) node.getParent();
		XMLDocument doc = ip.getXMLController().getDocumentWithPartofPath(
				project.getUserObject().toString(), origin);
		if (doc != null) {
			ip.getXMLController().deselectDocument(doc.getPath());
			node.setUserObject(origin);
			model.nodeStructureChanged(node.getParent());
		}
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
		expandTree();
	}
	
	/**
	 * add project to the tree.
	 * @param project is the name of the project.
	 */
	public void addProjectToTree(String project) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(project);
		model.insertNodeInto(node, root, root.getChildCount());
		expandTree();
	}
	
	/**
	 * Method which remove the project selected in the combobox from the tree.
	 */
	public void reloadProjects() {
		folderPanel.remove(treePane);
		initTree();
		addTreePane(folderPanel);
		folderPanel.revalidate();
		expandTree();
	}
	
	/**
	 * Expand all paths of the tree.
	 */
	public void expandTree() {
		tree.expandRow(0);
		int count = 1;
		for (int i = 0; i < ip.getFolder().size(); i++) {
			tree.expandRow(count);
			count++;
			for (int j = 1; j < ip.getFolder().get(i).size(); j++) {
				count++;
			}
		}
	}
	
	/**
	 * @return root of the project tree.
	 */
	private DefaultMutableTreeNode getRoot() {
		return root;
	}
}
