package context.healthinformatics.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.parser.Column;

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
	protected static final int TEXTSIZE = 20;
	protected static final int THREE = 3;
	protected static final int HUNDERTPROCENT = 100;
	protected static final int CORRECION = 98;
	protected static final int INSETS = 10;
	protected static final int FIELDCORRECTION = 130;
	private static final int SUBTITLEFONT = 20;
	private static final int SELECTERHEIGHT = 500;
	private static final int SELECTERWIDTH = 600;
	
	protected static final int FOUR = 4;

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
	 * Set the GridBag location of a Component with margin top.
	 * 
	 * @param x
	 *            coordinate.
	 * @param y
	 *            coordinate.
	 * @param insets
	 *            set insets on the gridBagConstraints
	 * @return the location for the Component.
	 */
	protected GridBagConstraints setGrids(int x, int y, Insets insets) {
		GridBagConstraints c = setGrids(x, y);
		c.insets = insets;
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
	 * Create a text field with given font.
	 * 
	 * @return the text area
	 */
	protected JTextArea creatTextArea() {
		JTextArea area = new JTextArea();
		area.setFont(new Font("Arial", Font.PLAIN, TEXTSIZE));
		return area;
	}

	/**
	 * Create a TextArea which has focus.
	 * 
	 * @return the text area
	 */
	protected JTextArea createTextAreaWithFocus() {
		JTextArea area = new JTextArea() {
			private static final long serialVersionUID = 1L;

			public void addNotify() {
				super.addNotify();
				requestFocus();
			}
		};
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
	 * @param buttonWidth
	 *            the width of the button
	 * @param buttonHeight
	 *            the height of the button
	 * @return the panel with white space and the button
	 */
	public JPanel makeFormRowPanelWithTwoButton(JButton buttonLeft,
			JButton buttonRight, int buttonWidth, int buttonHeight) {
		JPanel buttonPanel = createPanel(Color.WHITE, buttonWidth, buttonHeight);
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
	 * @param width
	 *            the with of the panel
	 * @param height
	 *            the height of the panel
	 * @return the panel containing the buttons
	 */
	public JPanel makeFormRowPanelWithThreeButton(JButton buttonLeft,
			JButton middleButton, JButton buttonRight, int width, int height) {
		JPanel buttonPanel = createPanel(Color.WHITE, width, height);
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
	 * @param width
	 *            the width of the panel
	 * @param height
	 *            the height of the panel
	 * @return panel with the two textfields
	 */
	public JPanel makeFormRowPanelWithComboBox(String name,
			JComboBox<String> comboBox, int width, int height) {
		JPanel containerPanel = createPanel(Color.WHITE, width, height);
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
	 * @param width
	 *            the width of the panel
	 * @param height
	 *            the height of the panel
	 * @return panel with the the textfield and label
	 */
	public JPanel makeFormRowPanelWithTextField(String labelName,
			JTextField textField, int width, int height) {
		JPanel containerPanel = createPanel(Color.WHITE, width, height);
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
	 * @param width
	 *            the width of the scrollpane
	 * @param height
	 *            the height of the scrollpane
	 * @return the scrollPane
	 */
	public JScrollPane makeScrollPaneForContainerPanel(JPanel containerPanel,
			int width, int height) {
		JScrollPane scrollPane = new JScrollPane(containerPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(width, height));
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
	 * Create a textfield with scroll pane.
	 * 
	 * @param textFieldWithScollPane
	 *            the text field
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @return a scrollpane with the textfield
	 */
	public JScrollPane makeScrollPaneForTextField(
			JTextField textFieldWithScollPane, int width, int height) {
		JScrollPane scrollPane = new JScrollPane(textFieldWithScollPane,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(width, height));
		return scrollPane;
	}

	/**
	 * Create a container panel for the document of xml files to container.
	 * 
	 * @param width
	 *            the width of the panel
	 * @param height
	 *            the height of the panel
	 * @return the panel to container the documents
	 */
	public JPanel createContainerWithGivenSizePanel(int width, int height) {
		JPanel parentPanel = new JPanel();
		parentPanel.setMinimumSize(new Dimension(width, height));
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
		JPanel panel = createEmptyWithGridBagLayoutPanel();
		panel.setBackground(color);
		return panel;
	}
	
	/**
	 * Creates a check box.
	 * @param title the title of the check box.
	 * @param color the color of the background of the check box.
	 * @return a check box.
	 */
	public JCheckBox createCheckBox(String title, Color color) {
		JCheckBox checkbox = new JCheckBox(title);
		checkbox.setBackground(color);
		checkbox.setFont(new Font("Arial", Font.PLAIN, TEXTSIZE));
		return checkbox;
	}
	
	/**
	 * @param titleName the name of that will be shown on the screen.
	 * @param width the width of the label with the title.
	 * @param height the height of the label with the title.
	 * @return a subtitle in italic with a size of 20.
	 */
	protected JLabel createSubTitle(String titleName, int width, int height) {
		JLabel title = new JLabel(titleName);
		title.setFont(new Font("Arial", Font.ITALIC, SUBTITLEFONT));
		title.setPreferredSize(new Dimension(width - 2 * INSETS, height));
		return title;
	}
	
	/**
	 * Checks for a check-box if its selected or not.
	 * On this basis it will determined whether the panel is visible or not.
	 * @param checkbox the check-box that will be checked.
	 * @param panel the panel that must be set visible or not.
	 */
	public void checkVisibility(JCheckBox checkbox, JPanel panel) { 
		if (checkbox.isSelected()) {
			panel.setVisible(true);
		} else {
			panel.setVisible(false);
		}
	}
	
	/**
	 * @return the column names of the database.
	 */
	public String[] getColumnNames() {
		String[] columnNames;
		ArrayList<Column> columns = SingletonDb.getDb().getColumns();
		if (!columns.isEmpty()) {
			columnNames = new String[columns.size()];
			for (int i = 0; i < columns.size(); i++) {
				columnNames[i] = columns.get(i).getColumnName();
			}
		} else {
			columnNames = new String[0];
		}
		return columnNames;
	}
	
	/**
	 * @param filter
	 *            which file type will be saved.
	 * @return the anwser of the filechooser.
	 */
	public JFileChooser saveFileChooser(String filter) {
		JFileChooser savePopup = new JFileChooser();
		savePopup.setMultiSelectionEnabled(true);
		String filtername = "save as *." + filter;
		FileNameExtensionFilter extenstionFilter = new FileNameExtensionFilter(
				filtername, filter);
		savePopup.setFileFilter(extenstionFilter);
		savePopup
				.setPreferredSize(new Dimension(SELECTERWIDTH, SELECTERHEIGHT));
		savePopup.setFileSelectionMode(JFileChooser.FILES_ONLY);
		Action details = savePopup.getActionMap().get("viewTypeDetails");
		details.actionPerformed(null);
		return savePopup;
	}
}
