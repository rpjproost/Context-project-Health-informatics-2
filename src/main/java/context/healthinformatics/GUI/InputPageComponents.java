package context.healthinformatics.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Class which can load components of the InputPage.
 */
public class InputPageComponents implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private MainFrame mf;
	private InputPage ip;
	
	private JComboBox<String> box;
	private JTextArea txt;
	private JButton projectButton;
	private JButton fileButton;
	private JButton selectButton;
	private JButton helpButton;
	private JButton analyseButton;
	
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
	public static final int FOLDERSECTIONHEIGHT = 400;
	
	/**
	 * Constructor.
	 * @param m is the mainframe object.
	 * @param p is the InptPage object.
	 */
	public InputPageComponents(MainFrame m, InputPage p) {
		mf = m;
		ip = p;
	}

	/**
	 * @return section1 Panel.
	 */
	public JPanel loadProjectSelection() {
		JPanel section1 = MainFrame.createPanel(Color.decode(InputPage.COLOR),
				mf.getScreenWidth(), PROJECTSELECTIONPANELHEIGHT);
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
		panel.add(projectLabel, ip.setGrids(0, 0));
	}
	
	/**
	 * @param panel to which the combobox will be added.
	 */
	public void addCombobox(JPanel panel) {
		box = new JComboBox<String>(ip.getProjects());
		panel.add(box, ip.setGrids(1, 0));
	}
	
	/**
	 * @param panel to which the projectButton will be added.
	 */
	public void addProjectButton(JPanel panel) {
		GridBagConstraints c = ip.setGrids(2, 0);
		projectButton = ip.createButton("ADD new Project", DIMESIONWIDTH, DIMESIONHEIGHT);
		projectButton.addActionListener(ip);
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS, BUTTONINSETS);
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(projectButton, c);
	}
	
	/**
	 * @return section2 Panel.
	 */
	public JPanel loadFileSelection() {
		JPanel section2 = MainFrame.createPanel(Color.decode(InputPage.COLOR),
				mf.getScreenWidth(), PROJECTSELECTIONPANELHEIGHT);
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
		panel.add(fileLabel, ip.setGrids(0, 0));
	}
	
	/**
	 * @param panel to which the addTextArea will be added.
	 */
	public void addTextArea(JPanel panel) {
		txt = new JTextArea();
		txt.setMinimumSize(new Dimension(TXTFIELDWIDTH, TXTFIELDHEIGHT));
		panel.add(txt, ip.setGrids(1, 0));
	}
	
	/**
	 * @param panel to which the addSelectButton will be added.
	 */
	public void addSelectButton(JPanel panel) {
		GridBagConstraints c = ip.setGrids(2, 0);
		selectButton = ip.createButton("SELECT", DIMESIONWIDTH, DIMESIONHEIGHT);
		selectButton.addActionListener(ip);
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS, BUTTONINSETS);
		panel.add(selectButton, c);
	}
	
	/**
	 * @param panel to which the addFileButton will be added.
	 */
	public void addFileButton(JPanel panel) {
		GridBagConstraints c = ip.setGrids(THREE, 0);
		fileButton = ip.createButton("ADD new File", DIMESIONWIDTH, DIMESIONHEIGHT);
		fileButton.addActionListener(ip);
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS, BUTTONINSETS);
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(fileButton, c);
	}
	
	/**
	 * @return section4 Panel.
	 */
	public JPanel loadButtonSection() {
		JPanel section4 = MainFrame.createPanel(Color.decode(InputPage.COLOR),
				mf.getScreenWidth(), FOLDERSECTIONHEIGHT);
		addHelpButton(section4);
		addAnalyseButton(section4);
		return section4;
	}
	
	/**
	 * @param panel to which the addHelpButton will be added.
	 */
	public void addHelpButton(JPanel panel) {
		helpButton = ip.createButton("HELP", HELPBUTTONWIDTH, HELPBUTTONHEIGHT);
		helpButton.addActionListener(ip);
		GridBagConstraints c = ip.setGrids(0, 0);
		c.weightx = 1;
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS, BUTTONINSETS);
		c.anchor = GridBagConstraints.WEST;
		panel.add(helpButton, c);
	}
	
	/**
	 * @param panel to which the addAnalyseButton will be added.
	 */
	public void addAnalyseButton(JPanel panel) {
		analyseButton = ip.createButton("ANALYSE", HELPBUTTONWIDTH, HELPBUTTONHEIGHT);
		analyseButton.addActionListener(ip);
		GridBagConstraints c = ip.setGrids(0, 0);
		c.weightx = 1;
		c.insets = new Insets(BUTTONINSETS, BUTTONINSETS, BUTTONINSETS, BUTTONINSETS);
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
	public JButton getFileButton() {
		return fileButton;
	}
	
	/**
	 * @return the TextArea.
	 */
	public JTextArea getTextArea() {
		return txt;
	}
	
	/**
	 * @return the ComboBox.
	 */
	public JComboBox<String> getComboBox() {
		return box;
	}
	
}
