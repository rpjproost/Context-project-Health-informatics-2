package context.healthinformatics.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
	private JButton selectButton;
	private JButton helpButton;
	private JButton analyseButton;
	private int screenWidth;

	public static final int PROJECTSELECTIONPANELHEIGHT = 100;
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
	 * @return section1 Panel.
	 */
	public JPanel loadProjectSelection() {
		JPanel projectSelectionPanel = MainFrame.createPanel(Color.decode(InputPage.COLOR),
				screenWidth, PROJECTSELECTIONPANELHEIGHT);
		addProjectLabel(projectSelectionPanel);
		projectSelectionPanel.add(box, ip.setGrids(1, 0));
		addProjectButton(projectSelectionPanel);
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
		panel.add(projectLabel, ip.setGrids(0, 0));
	}

	/**
	 * @param panel
	 *            to which the projectButton will be added.
	 */
	public void addProjectButton(JPanel panel) {
		GridBagConstraints c = ip.setGrids(2, 0);
		projectButton = ip.createButton("ADD new Project", DIMESIONWIDTH,
				DIMESIONHEIGHT);
		projectButton.addActionListener(this);
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS,
				BUTTONINSETS);
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(projectButton, c);
	}

	/**
	 * @return Button  Panel.
	 */
	public JPanel loadButtonSection() {
		JPanel buttonSection = MainFrame.createPanel(Color.decode(InputPage.COLOR),
				screenWidth, FOLDERSECTIONHEIGHT);
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
		JPanel analyseButton = MainFrame.createPanel(
				Color.decode(InputPage.COLOR), screenWidth,
				FOLDERSECTIONHEIGHT);
		addAnalyseButton(analyseButton);
		return analyseButton;
	}
	
	/**
	 * @param panel
	 *            to which the addAnalyseButton will be added.
	 */
	public void addAnalyseButton(JPanel panel) {
		analyseButton = ip.createButton("ANALYSE", HELPBUTTONWIDTH,
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
	 * @return the SelectButton.
	 */
	public JButton getSelectButton() {
		return selectButton;
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
		if (e.getSource() == getOpenFileButton()) {
			if (ip.openFileChooser() == JFileChooser.APPROVE_OPTION) {
				String path = ip.getSelecter().getSelectedFile().toString();
				ip.addFile(path);
			}
			ip.getSelecter().setVisible(false);
		}
		if (e.getSource() == getHelpButton()) {
			return; // TODO
		}
		if (e.getSource() == getAnalyseButton()) {
			ip.loadDatabase();
			mf.setState(mf.getCodePage());
			mf.reloadStatePanel();
		}
	}

}
