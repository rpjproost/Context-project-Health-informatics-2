package context.healthinformatics.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import context.healthinformatics.Parser.TXTParser;
import context.healthinformatics.Parser.XMLParser;

public class MainFrame extends JPanel {

	private static final long serialVersionUID = 1L;
	protected static PanelState state;
	protected static PanelState inputState = new InputPage();
	protected static PanelState codeState = new CodePage();
	protected static PanelState outputState = new OutputPage();
	
	private static int tabsX;
	private static int tabsY = 120;
	private static int screenWidth;
	
	static GridBagConstraints c;
	JPanel mainPanel;
	static JFrame f;
	static JPanel p1;
	static JPanel p2;
	static JPanel p3;
	static JPanel varPanel = new JPanel();
	MouseHandler mouse = new MouseHandler();
	
	public MainFrame() {
		state = inputState;		
		load();
	}
	
	public static void setState(PanelState p) {
		state = p;
	}
	
	public static int getScreenWidth(){
		return screenWidth;
	}

	public void load() {
		makeFrame();
		c = new GridBagConstraints();
		mainPanel = new JPanel(new GridBagLayout());
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screenSize.width;
		tabsX = screenWidth / 3;
		
		JPanel panel1 = createPanel(Color.gray, screenWidth, tabsY);
		panel1.setLayout(new GridBagLayout());
		p1 = createTab("stap 1", 0, Color.decode("#81DAF5"));
		panel1.add(p1, c);
		p2 = createTab("stap 2", 1, Color.decode("#01A9DB"));
		panel1.add(p2, c);
		p3 = createTab("stap 3", 2, Color.decode("#086A87"));
		panel1.add(p3, c);
		
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(panel1, c);
		
		varPanel.setLayout(new GridBagLayout());
		varPanel.add(MainFrame.state.loadPanel());
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(varPanel, c);
		
		mainPanel.setBackground(Color.gray);
		
		f.add(mainPanel);
		f.setVisible(true);
	}
	
	public JPanel createTab(String tabName, int tabNmbr, Color col) {
		JPanel page = createPanel(col, tabsX, tabsY);
		JLabel label = new JLabel(tabName);
		label.setFont(new Font("Arial", Font.PLAIN, 40));
		page.add(label);
		c.gridx = tabNmbr;
		c.gridy = 0;
		page.setBorder(new EmptyBorder(10, 10, 10, 10) );
		page.addMouseListener(mouse);
		return page;
	}
	
	public static int getStatePanelSize() {
		return Toolkit.getDefaultToolkit().getScreenSize().height - tabsY;
	}
	
	public int getTabsX(){
		return tabsX;
	}
	
	public int getTabsY(){
		return tabsY;
	}
	
	public PanelState getPanelState() {
		return state;
	}
	
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
		return tempPanel;
	}
	
	/**
	 * Method which makes the outer-container-frame.
	 * @param x height
	 * @param y width
	 */
	public void makeFrame() {
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setTitle("PRODUCT NAME");
	}
	
	public void closeFrame() {
		f.dispose();
	}
	
	static class MouseHandler implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			varPanel.removeAll();
			if (e.getSource() == MainFrame.p1) {
				MainFrame.setState(inputState);
			}
			else if (e.getSource() == MainFrame.p2) {
				MainFrame.setState(codeState);
			}
			else if (e.getSource() == MainFrame.p3) {
				MainFrame.setState(outputState);
			}
			varPanel.add(MainFrame.state.loadPanel());
			varPanel.revalidate();
		}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}
		
		@Override
		public void mouseExited(MouseEvent e) {}
	}
	
}
