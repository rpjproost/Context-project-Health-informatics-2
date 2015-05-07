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
	protected PanelState state;
	protected PanelState inputState = new InputPage(this);
	protected PanelState codeState = new CodePage(this);
	protected PanelState outputState = new OutputPage(this);
	
	private static int tabsX;
	private static int tabsY = 200;
	
	static GridBagConstraints c;
	static JPanel mainPanel;
	static JFrame f;
	static JButton button;
	static Dimension dim;
	static MouseHandler mouse = new MouseHandler();
	
	public MainFrame() {
		state = inputState;		
		load();
	}
	
	public void setState(PanelState p) {
		state = p;
	}

	public void load() {
		makeFrame();
		dim = new Dimension(100,40);
		c = new GridBagConstraints();
		mainPanel = new JPanel(new GridBagLayout());
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		tabsX = width / 3;
		
		JPanel panel1 = createPanel(Color.gray, width, tabsY);
		panel1.setLayout(new GridBagLayout());
		JPanel p1 = createTab("stap 1", 0, Color.red);
		panel1.add(p1, c);
		JPanel p2 = createTab("stap 2", 1, Color.green);
		panel1.add(p2, c);
		JPanel p3 = createTab("stap 3", 2, Color.orange);
		panel1.add(p3, c);
		
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(panel1, c);
		
		JPanel panel2 = this.state.loadPanel();		
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(panel2, c);
		
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
	
	public int getStatePanelSize() {
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
	
	/**
	 * Method which creates a button.
	 * @param input buuton
	 */
//	public void makeButton(JButton input) {
//		input.setPreferredSize(dim);
//		input.addActionListener(new ActionHandler());
//		input.setFont(new Font("Arial", Font.PLAIN, 30));
//	}
	
//	/**
//	 * Class which handles the actions when buttons are clicked.
//	 */
//	static class ActionHandler implements ActionListener {
//
//		/**
//		 * Method which is fired after a certain event.
//		 * @param e event
//		 */
//		public void actionPerformed(ActionEvent e) {
//			if (e.getSource() == button) {
//				input = txt.getText();
//				XMLParser xmlp = new XMLParser(input);
//				try {
//					xmlp.parse();
//					TXTParser txtp = new TXTParser(xmlp.getPath(),
//								xmlp.getStartLine(), xmlp.getDelimiter(), xmlp.getColumns());
//					txtp.parse();
//				} catch (IOException e1) {
//					System.out.println("No such file... Try again!");
//				}
//			}
//		}
//	}
	
	static class MouseHandler implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
//			f.dispose();
//			if(e.getSource() == MainFrame.p1) {
//				
//			}
//			else if(e.getSource() == MainFrame.p2) {
//							
//			}
//			else if(e.getSource() == MainFrame.p3) {
//				
//			}
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
