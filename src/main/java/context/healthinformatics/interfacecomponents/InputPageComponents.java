package context.healthinformatics.interfacecomponents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
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

import context.healthinformatics.gui.InputPage;
import context.healthinformatics.gui.MainFrame;
import context.healthinformatics.parser.ReadHelpInfoFromTXTFile;
import context.healthinformatics.writer.XMLDocument;

/**
 * Class which can load components of the InputPage.
 */
public class InputPageComponents implements Serializable, ActionListener {

	private static final long serialVersionUID = 1L;

	private MainFrame mf;
	private InputPage ip;

	private JComboBox<String> box;
	private JButton projectButton;
	private JButton openFileButton;
	private JButton helpButton;
	private JButton analyseButton;
	private int screenWidth;

	private JButton removeProjectButton;

	public static final int PROJECTSELECTIONPANELHEIGHT = 100;
	public static final int TITLEHEIGHT = 50;
	public static final int PROJECTLABELFONTSIZE = 20;
	public static final int DIMESIONHEIGHT = 150;
	public static final int DIMESIONWIDTH = 40;
	public static final int TXTFIELDWIDTH = 500;
	public static final int TXTFIELDHEIGHT = 30;
	public static final int BUTTONINSETS = 10;
	public static final int THREE = 3;
	public static final int HELPBUTTONHEIGHT = 100;
	public static final int HELPBUTTONWIDTH = 300;
	public static final int FOLDERSECTIONHEIGHT = 100;

	private boolean helpFrameOpen = false;

	/**
	 * Constructor.
	 * 
	 * @param m
	 *            is the mainframe object.
	 * @param p
	 *            is the InptPage object.
	 */
	public InputPageComponents(MainFrame m, InputPage p) {
		mf = m;
		ip = p;
		screenWidth = mf.getScreenWidth() / 2;
		box = new JComboBox<String>(ip.getProjects());
	}

	/**
	 * Loads a title for the project selection part.
	 * 
	 * @param panel
	 *            where the title belongs.
	 */
	public void loadProjectTitle(JPanel panel) {
		JLabel title = ip.createTitle("Project Selection:");
		GridBagConstraints c = ip.setGrids(0, 0);
		c.gridwidth = THREE;
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS,
				BUTTONINSETS);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(title, c);
	}

	/**
	 * @return section1 Panel.
	 */
	public JPanel loadProjectSelection() {
		JPanel projectSelectionPanel = MainFrame.createPanel(
				Color.decode(InputPage.COLOR), screenWidth,
				PROJECTSELECTIONPANELHEIGHT);
		loadProjectTitle(projectSelectionPanel);
		addProjectLabel(projectSelectionPanel);
		projectSelectionPanel.add(box, ip.setGrids(1, 1));
		addProjectButton(projectSelectionPanel);
		removeProjectButton(projectSelectionPanel);
		box.addActionListener(this);
		return projectSelectionPanel;
	}

	/**
	 * @param panel
	 *            to which the projectlabel will be added.
	 */
	public void addProjectLabel(JPanel panel) {
		JLabel projectLabel = new JLabel("   Project :   ");
		projectLabel
				.setFont(new Font("Arial", Font.PLAIN, PROJECTLABELFONTSIZE));
		projectLabel.setSize(new Dimension(DIMESIONWIDTH, DIMESIONHEIGHT));
		panel.add(projectLabel, ip.setGrids(0, 1));
	}

	/**
	 * @param panel
	 *            to which the projectButton will be added.
	 */
	public void addProjectButton(JPanel panel) {
		GridBagConstraints c = ip.setGrids(2, 1);
		projectButton = ip.createButton("ADD new Project", DIMESIONWIDTH,
				DIMESIONHEIGHT);
		projectButton.addActionListener(this);
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS,
				BUTTONINSETS);
		panel.add(projectButton, c);
	}

	/**
	 * @param panel
	 *            to which a remove project button will be added.
	 */
	public void removeProjectButton(JPanel panel) {
		GridBagConstraints c = ip.setGrids(THREE, 1);
		removeProjectButton = ip.createButton("Remove Project", DIMESIONWIDTH,
				DIMESIONHEIGHT);
		removeProjectButton.addActionListener(this);
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(removeProjectButton, c);
	}

	/**
	 * @return Button Panel.
	 */
	public JPanel loadButtonSection() {
		JPanel buttonSection = MainFrame
				.createPanel(Color.decode(InputPage.COLOR), screenWidth,
						FOLDERSECTIONHEIGHT);
		addHelpButton(buttonSection);
		addOpenFileButton(buttonSection);
		return buttonSection;
	}

	/**
	 * @param panel
	 *            to which the addFileButton will be added.
	 */
	public void addOpenFileButton(JPanel panel) {
		GridBagConstraints c = ip.setGrids(0, 0);
		openFileButton = ip.createButton("Open File", DIMESIONWIDTH,
				DIMESIONHEIGHT);
		openFileButton.addActionListener(this);
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS,
				BUTTONINSETS);
		panel.add(openFileButton, c);
	}

	/**
	 * @param panel
	 *            to which the addHelpButton will be added.
	 */
	public void addHelpButton(JPanel panel) {
		helpButton = ip.createButton("HELP", HELPBUTTONWIDTH, HELPBUTTONHEIGHT);
		helpButton.addActionListener(this);
		GridBagConstraints c = ip.setGrids(0, 1);
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS,
				BUTTONINSETS);
		c.weightx = 1;
		c.anchor = GridBagConstraints.WEST;
		panel.add(helpButton, c);
	}

	/**
	 * The function to load the analyze button.
	 * 
	 * @return the analyze button
	 */
	public JPanel loadAnalyzeButtonSection() {
		JPanel analyseButton = MainFrame
				.createPanel(Color.decode(InputPage.COLOR), screenWidth,
						FOLDERSECTIONHEIGHT);
		addAnalyseButton(analyseButton);
		return analyseButton;
	}

	/**
	 * @param panel
	 *            to which the addAnalyseButton will be added.
	 */
	public void addAnalyseButton(JPanel panel) {
		analyseButton = ip.createButton("Save & Load", HELPBUTTONWIDTH,
				HELPBUTTONHEIGHT);
		analyseButton.addActionListener(this);
		GridBagConstraints c = ip.setGrids(0, 0);
		c.weightx = 1;
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS,
				BUTTONINSETS);
		c.anchor = GridBagConstraints.EAST;
		panel.add(analyseButton, c);
	}

	/**
	 * @return the analyseButton.
	 */
	public JButton getAnalyseButton() {
		return analyseButton;
	}

	/**
	 * @return the analyseButton.
	 */
	public JButton getProjectButton() {
		return projectButton;
	}

	/**
	 * @return the analyseButton.
	 */
	public JButton getHelpButton() {
		return helpButton;
	}

	/**
	 * @return the analyseButton.
	 */
	public JButton getOpenFileButton() {
		return openFileButton;
	}

	/**
	 * @return the ComboBox.
	 */
	public JComboBox<String> getComboBox() {
		return box;
	}

	/**
	 * Method which is fired after an ActionEvent.
	 * 
	 * @param e
	 *            event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getProjectButton()) {
			ip.createProject();
		}
		if (e.getSource() == removeProjectButton && ip.getFolder().size() > 1) {
			int reply = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to remove the project?",
					"Remove Project", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				remove();
			}
		}
		if (e.getSource() == getOpenFileButton()
				&& ip.openFileChooser() == JFileChooser.APPROVE_OPTION) {
			ip.openFiles(ip.getFileSelecter().getSelectedFiles());
		}
		if (e.getSource() == getHelpButton()) {
			handleHelpButton();
		}
		if (e.getSource() == getAnalyseButton()) {
			if (ip.getXMLController().getSelectedDocs() != null) {
				ip.loadDatabase();
				mf.setState(mf.getCodePage());
				mf.reloadStatePanel();
			}
		}
		if (e.getSource() == box) {
			updateProject();
		}
	}

	/**
	 * Removes a project out the system.
	 */
	private void remove() {
		ip.removeProject((String) getComboBox().getSelectedItem());
		getComboBox().removeItemAt(getComboBox().getSelectedIndex());
		updateProject();
	}

	/**
	 * Updates the project settings. Sets controller on the new one and loads
	 * into the editor.
	 */
	protected void updateProject() {
		ArrayList<XMLDocument> projectDocs = ip.getEditor()
				.getAllXMLDocuments();
		XMLEditorController controller = ip.getXMLController();
		controller.setDocumentsInProject(projectDocs);
		controller.setProject((String) getComboBox().getSelectedItem());
		controller.loadProject(ip.getEditor());
	}

	/**
	 * Set if the help frame is open or not.
	 * 
	 * @param helpFrameNew
	 *            the boolean value
	 */
	public void setHelpFrameOpen(boolean helpFrameNew) {
		this.helpFrameOpen = helpFrameNew;
	}

	/**
	 * Handle the help button.
	 */
	private void handleHelpButton() {
		if (!helpFrameOpen) {
			setHelpFrameOpen(true);
			ReadHelpInfoFromTXTFile test = new ReadHelpInfoFromTXTFile(
					"src/main/data/guihelpdata/inputpagehelp.txt");
			try {
				test.parse();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ArrayList<HelpFrameInfoContainer> listOfHelpFrameInfo = test
					.getHelpFrameInfoContainer();
			new HelpFrame("Input Page Help", listOfHelpFrameInfo, this);
		}
	}
}
