package context.healthinformatics.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

/**
 * Class that defines the most methods that are needed in several interface
 * classes.
 */
public class InterfaceHelper extends JPanel {

	private static final long serialVersionUID = 1L;
	private int tabsX;
	private static final int TABSY = 120;
	private static final int TABTEXTFONTSIZE = 40;
	public static final int TITLEFONT = 28;
	private static final int TEXTSIZE = 20;

	private static final int PARENTWIDTH = 900;
	private static final int PARENTHEIGHT = 650;
	private static final int BUTTONHEIGHT = 35;
	private static final int FORMELEMENTWIDTH = 800;
	private static final int THREE = 3;
	private static final int FORMELEMENTHEIGHT = 25;

	/**
	 * @return JPanel with attributes specified by the arguments.
	 * @param color
	 *            of panel
	 * @param width
	 *            of panel
	 * @param height
	 *            of panel
	 */
	public static JPanel createPanel(Color color, int width, int height) {
		JPanel tempPanel = new JPanel();
		tempPanel.setBackground(color);
		tempPanel.setMinimumSize(new Dimension(width, height));
		tempPanel.setMaximumSize(new Dimension(width, height));
		tempPanel.setPreferredSize(new Dimension(width, height));
		tempPanel.setLayout(new GridBagLayout());
		return tempPanel;
	}

	/**
	 * @return a panel which represents a tab indicating one of the states.
	 * @param tabName
	 *            is the name of the tab.
	 * @param col
	 *            is the color of the tab.
	 */
	protected JPanel createTab(String tabName, Color col) {
		JPanel page = createPanel(col, tabsX, TABSY);
		JLabel label = new JLabel(tabName);
		label.setFont(new Font("Arial", Font.PLAIN, TABTEXTFONTSIZE));
		page.add(label);
		return page;
	}

	/**
	 * Set the gridbag location of a Component with margin top.
	 * 
	 * @param x
	 *            coordinate.
	 * @param y
	 *            coordinate.
	 * @param marginTop
	 *            let gridbag marge top.
	 * @return the location for the Component.
	 */
	protected GridBagConstraints setGrids(int x, int y, int marginTop) {
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(marginTop, 0, 0, 0);
		c.gridx = x;
		c.gridy = y;
		return c;
	}

	/**
	 * Set the gridbag location of a Component.
	 * 
	 * @param x
	 *            coordinate.
	 * @param y
	 *            coordinate.
	 * @return the location for the Component.
	 */
	public GridBagConstraints setGrids(int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		return c;
	}

	/**
	 * Creates a text area with a specified width and height. default font is
	 * Arial with letter size 20.
	 * 
	 * @param fieldwidth
	 *            the width of the text area.
	 * @param fieldheight
	 *            the height of the text area.
	 * @return new text field area.
	 */
	protected JTextArea createTextField(int fieldwidth, int fieldheight) {
		JTextArea area = new JTextArea();
		area.setPreferredSize(new Dimension(fieldwidth, fieldheight));
		area.setFont(new Font("Arial", Font.PLAIN, TEXTSIZE));
		return area;
	}

	/**
	 * Create a text field with given font.
	 * 
	 * @return the text area
	 */
	protected JTextArea createTextField() {
		JTextArea area = new JTextArea();
		area.setFont(new Font("Arial", Font.PLAIN, TEXTSIZE));
		return area;
	}

	/**
	 * Creates a button width a specified name width and height. Default font is
	 * Arial with letter size 20.
	 * 
	 * @param name
	 *            the text in the button.
	 * @param width
	 *            of the button.
	 * @param height
	 *            of the button.
	 * @return a new button with no actionhandler.
	 */
	public JButton createButton(String name, int width, int height) {
		JButton button = new JButton(name);
		button.setPreferredSize(new Dimension(width, height));
		button.setFont(new Font("Arial", Font.PLAIN, TEXTSIZE));
		return button;
	}

	/**
	 * Creates a label with special letter size.
	 * 
	 * @param titleName
	 *            the text of the title.
	 * @return a Label with the title.
	 */
	public JLabel createTitle(String titleName) {
		JLabel title = new JLabel(titleName);
		title.setFont(new Font("Arial", Font.PLAIN, TITLEFONT));
		return title;
	}

	/**
	 * set the width of the tabs.
	 * 
	 * @param i
	 *            the new width.
	 */
	protected void setTabX(int i) {
		tabsX = i;
	}

	/**
	 * @return the width of the tabs.
	 */
	protected int getTabsX() {
		return tabsX;
	}

	/**
	 * @return the height of the tabs.
	 */
	protected int getTabsY() {
		return TABSY;
	}

	/**
	 * @return the width of the users screen.
	 */
	public int getScreenWidth() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return screenSize.width;
	}

	/**
	 * @return the height of the state panel.
	 */
	public int getStatePanelSize() {
		return Toolkit.getDefaultToolkit().getScreenSize().height - getTabsY();
	}

	/**
	 * Create a row with a single button to add columns.
	 * 
	 * @param buttonLeft
	 *            the button on the left side of the panel
	 * @param buttonRight
	 *            the button on the right side of the panel
	 * @return the panel with white space and the button
	 */
	public JPanel makeFormRowWithButton(JButton buttonLeft, JButton buttonRight) {
		JPanel buttonPanel = createPanel(Color.WHITE, FORMELEMENTWIDTH,
				BUTTONHEIGHT);
		buttonPanel.setLayout(new GridLayout(1, THREE));
		buttonPanel.add(buttonLeft);
		buttonPanel.add(new JPanel());
		buttonPanel.add(buttonRight);
		return buttonPanel;
	}

	/**
	 * Make a Panel containing 3 buttons.
	 * 
	 * @param buttonLeft
	 *            the button on the left.
	 * @param middleButton
	 *            the button in the middle
	 * @param buttonRight
	 *            the button on the right.
	 * @return the panel containing the buttons
	 */
	public JPanel makeFormRowWithThreeButton(JButton buttonLeft,
			JButton middleButton, JButton buttonRight) {
		JPanel buttonPanel = createPanel(Color.WHITE, FORMELEMENTWIDTH,
				BUTTONHEIGHT);
		buttonPanel.setLayout(new GridLayout(1, THREE));
		buttonPanel.add(buttonLeft);
		buttonPanel.add(middleButton);
		buttonPanel.add(buttonRight);
		return buttonPanel;
	}

	/**
	 * Make a row with display text field and field to fill in value.
	 * 
	 * @param name
	 *            the name of the label.
	 * @param comboBox
	 *            the given comboBox
	 * @return panel with the two textfields
	 */
	public JPanel makeFormRowWithComboBox(String name,
			JComboBox<String> comboBox) {
		JPanel containerPanel = createPanel(Color.WHITE, FORMELEMENTWIDTH,
				FORMELEMENTHEIGHT);
		containerPanel.setLayout(new GridLayout(1, 2));
		containerPanel.add(new JLabel(name));
		containerPanel.add(comboBox);
		return containerPanel;
	}

	/**
	 * Make a row with display text field and field to fill in value.
	 * 
	 * @param labelName
	 *            the name of the label.
	 * @param textField
	 *            the textfield of the row
	 * @return panel with the the textfield and label
	 */
	public JPanel makeFormRowWithTextField(String labelName,
			JTextField textField) {
		JPanel containerPanel = createPanel(Color.WHITE, FORMELEMENTWIDTH,
				FORMELEMENTHEIGHT);
		containerPanel.setLayout(new GridLayout(1, 2));
		containerPanel.add(new JLabel(labelName));
		containerPanel.add(textField);
		return containerPanel;
	}

	/**
	 * Make a scrollPanefor the container.
	 * 
	 * @param containerPanel
	 *            the panel for which the scrollpane is made
	 * @return the scrollPane
	 */
	public JScrollPane makeScrollPaneForContainerPanel(JPanel containerPanel) {
		JScrollPane scrollPane = new JScrollPane(containerPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(PARENTWIDTH, PARENTHEIGHT));
		return scrollPane;
	}

	/**
	 * Make a scrollPanefor the TextArea.
	 * 
	 * @param textAreaWithScollPane
	 *            the TextArea for which the Scroll Pane is made
	 * @param width
	 *            the width of the scrollPane
	 * @param height
	 *            the height of the scrollPane
	 * @return the scrollPane
	 */
	public JScrollPane makeScrollPaneForTextArea(
			JTextArea textAreaWithScollPane, int width, int height) {
		JScrollPane scrollPane = new JScrollPane(textAreaWithScollPane,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(width, height));
		return scrollPane;
	}

	/**
	 * Create a container panel for the document of xml files to container.
	 * 
	 * @return the panel to container the documents
	 */
	public JPanel createContainerPanel() {
		JPanel parentPanel = new JPanel();
		parentPanel.setMinimumSize(new Dimension(PARENTWIDTH, PARENTHEIGHT));
		parentPanel.setLayout(new GridBagLayout());
		return parentPanel;
	}

	/**
	 * Create a panel with gridbaglayout.
	 * 
	 * @return the panel
	 */
	public JPanel createEmptyWithGridBagLayoutPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		return panel;
	}

	/**
	 * Create a panel with GridBagLayout and a color.
	 * 
	 * @param color
	 *            the color of the background
	 * @return the panel
	 */
	public JPanel createEmptyWithGridBagLayoutPanel(Color color) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBackground(color);
		return panel;
	}
}
