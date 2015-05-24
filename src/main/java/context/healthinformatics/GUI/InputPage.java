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
	Serializable {
	
	private static final long serialVersionUID = 1L;

	private MainFrame mf;
	private FileTree ft;
	private ArrayList<ArrayList<String>> folder;
	
	private JTextArea txt;
	private JButton projectButton;
	private JButton fileButton;
	private JButton selectButton;
	private JButton helpButton;
	private JButton analyseButton;
	private JPanel panel;
	private JComboBox<String> box;
	private JFileChooser selecter;
	
	public static final int FOLDERSECTIONHEIGHT = 400;
	public static final int TXTFIELDWIDTH = 500;
	public static final int TXTFIELDHEIGHT = 30;
	public static final int BUTTONFONTSIZE = 15;
	public static final int DIMESIONHEIGHT = 150;
	public static final int DIMESIONWIDTH = 40;
	public static final int SECTION1HEIGHT = 100;
	public static final int PROJECTLABELFONTSIZE = 20;
	public static final int BUTTONINSETS = 10;
	public static final int THREE = 3;
	public static final int HELPBUTTONHEIGHT = 100;
	public static final int HELPBUTTONWIDTH = 300;
	public static final String COLOR = "#81DAF5";
	
	/**
	 * Constructor.
	 * @param m is the mainframe object
	 */
	public InputPage(MainFrame m) {
		mf = m;
		folder = new ArrayList<ArrayList<String>>();
		ft = new FileTree(m, this);
	}

	/**
	 * @return Panel of this state.//////////////////////////////////////////////////////////////////
	 */
	public JPanel loadPanel() {
		panel = createSection(mf.getStatePanelSize());
		panel.add(loadProjectSelection(), setGrids(0, 0));
		panel.add(loadFileSelection(), setGrids(0, 1));
		panel.add(ft.loadFolder(), setGrids(0, 2));
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
			ft.getModel().insertNodeInto(node, ft.getRoot(), ft.getRoot().getChildCount());
			ft.getXmlList().add(null);
			ft.getTree().expandRow(0);
			ft.getTree().expandRow(ft.getRoot().getChildCount() 
					+ ft.getRoot().getLeafCount() - 1);
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
	 * @return section4 Panel.
	 */
	public JPanel loadButtonSection() {
		JPanel section4 = createSection(FOLDERSECTIONHEIGHT);
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
	 * @return the file tree object.
	 */
	public FileTree getFileTree() {
		return ft;
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
	 * @param  f a 2D array of projects and files.
	 */
	public void setFolder(ArrayList<ArrayList<String>> f) {
		folder = f;
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
					ft.getModel().insertNodeInto(newNode,
							(MutableTreeNode) ft.getRoot().getChildAt(project)
							, ft.getRoot().getChildAt(project).getChildCount());
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
