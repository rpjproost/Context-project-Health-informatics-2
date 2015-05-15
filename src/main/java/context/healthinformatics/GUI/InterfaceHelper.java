package context.healthinformatics.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Class that defines the most methods that are needed in
 * several interface classes.
 */
public class InterfaceHelper extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private int tabsX;
	private static final int TABSY = 120;
	private static final int TABTEXTFONTSIZE = 40;
	private static final int TEXTSIZE = 20;
	
	/**
	 * @return JPanel with attributes specified by the arguments.
	 * @param color
	 *            of panel
	 * @param width
	 *            of panel
	 * @param height
	 *            of panel
	 */
	protected static JPanel createPanel(Color color, int width, int height) {
		JPanel tempPanel = new JPanel();
		tempPanel.setBackground(color);
		tempPanel.setMinimumSize(new Dimension(width, height));
		tempPanel.setMaximumSize(new Dimension(width, height));
		tempPanel.setPreferredSize(new Dimension(width, height));
		return tempPanel;
	}
	
	/**
	 * @return a panel which represents a tab indicating one of the states.
	 * @param tabName is the name of the tab.
	 * @param col is  the color of the tab.
	 */
	protected JPanel createTab(String tabName, Color col) {
		JPanel page = createPanel(col, tabsX, TABSY);
		JLabel label = new JLabel(tabName);
		label.setFont(new Font("Arial", Font.PLAIN, TABTEXTFONTSIZE));
		page.add(label);
		return page;
	}
	
	/**
	 * Set the gridbag location of a Component.
	 * @param x coordinate.
	 * @param y coordinate.
	 * @return the location for the Component.
	 */
	protected GridBagConstraints setGrids(int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		return c;
	}
	
	/**
	 * Creates a text area with a specified width and height.
	 * default font is Arial with letter size 20.
	 * @param fieldwidth the width of the text area.
	 * @param fieldheight the height of the text area.
	 * @return new text field area.
	 */
	protected JTextArea createTextField(int fieldwidth, int fieldheight) {
		JTextArea area = new JTextArea();
		area.setPreferredSize(new Dimension(fieldwidth, fieldheight));
		area.setFont(new Font("Arial", Font.PLAIN, TEXTSIZE));
		return area;
	}
	
	/**
	 * Creates a button width a specified name width and height.
	 * Default font is Arial with letter size 20.
	 * @param name the text in the button.
	 * @param width of the button.
	 * @param height of the button.
	 * @return a new button with no actionhandler.
	 */
	protected JButton createButton(String name, int width, int height) {
		JButton button = new JButton(name);
		button.setPreferredSize(new Dimension(width, height));
		button.setFont(new Font("Arial", Font.PLAIN, TEXTSIZE));
		return button;
	}
	
	/**
	 * set the width of the tabs.
	 * @param i the new width.
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
}
