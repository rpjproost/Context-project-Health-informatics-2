package context.healthinformatics.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Class which sets-up the frame of the GUI.
 */
public class MainFrame extends JPanel {

	private static final long serialVersionUID = 1L;
	private PanelState state;
	private PanelState inputState = new InputPage(this);
	private PanelState codeState = new CodePage(this);
	private PanelState outputState = new OutputPage(this);
	
	public static final int NMBROFTABS = 3;
	public static final int TABTEXTFONTSIZE = 40;
	public static final int BORDERSIZE = 10;
	
	private int tabsX;
	private static final int TABSY = 120;
	private int screenWidth;
	
	private GridBagConstraints c;
	private JPanel mainPanel;
	private JFrame f;
	private JPanel inputTab;
	private JPanel codeTab;
	private JPanel outputTab;
	private JPanel varPanel = new JPanel();
	private MouseHandler mouse = new MouseHandler();
	
	/**
	 * Constructor, which initialises the mainFrame in the inputState.
	 */
	public MainFrame() {
		state = inputState;		
		load();
	}
	
	/**
	 * Method which sets the state of the GUI.
	 * @param p which is the desired state.
	 */
	public void setState(PanelState p) {
		state = p;
	}
	
	/**
	 * Method which sets the state of the GUI.
	 * @return inputpage
	 */
	public PanelState getInputPage() {
		return inputState;
	}
	
	/**
	 * Method which sets the state of the GUI.
	 * @return codePAge
	 */
	public PanelState getCodePage() {
		return codeState;
	}
	
	/**
	 * Method which sets the state of the GUI.
	 * @return outputPage
	 */
	public PanelState getOutputPage() {
		return outputState;
	}
	
	/**
	 * @return input tab as JPanel.
	 */
	public JPanel getInputTab() {
		return inputTab;
	}
	
	/**
	 * @return code tab as JPanel.
	 */
	public JPanel getCodeTab() {
		return codeTab;
	}
	
	/**
	 * @return output tab as JPanel.
	 */
	public JPanel getOutputTab() {
		return outputTab;
	}
	
	/**
	 * @return Variable panel as JPanel.
	 */
	public JPanel getVarPanel() {
		return varPanel;
	}

	/**
	 * Method which creates the frame of the GUI and creates the tabs.
	 */
	public void load() {
		makeFrame();
		c = new GridBagConstraints();
		mainPanel = new JPanel(new GridBagLayout());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setScreenWidth(screenSize.width);
		tabsX = getScreenWidth() / NMBROFTABS;
		
		JPanel panel1 = createPanel(Color.gray, getScreenWidth(), TABSY);
		panel1.setLayout(new GridBagLayout());
		inputTab = createTab("stap 1", 0, Color.decode("#81DAF5"));
		panel1.add(inputTab, c);
		codeTab = createTab("stap 2", 1, Color.decode("#01A9DB"));
		panel1.add(codeTab, c);
		outputTab = createTab("stap 3", 2, Color.decode("#086A87"));
		panel1.add(outputTab, c);
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(panel1, c);
		
		varPanel.setLayout(new GridBagLayout());
		varPanel.add(state.loadPanel());
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(varPanel, c);
		
		mainPanel.setBackground(Color.gray);
		f.add(mainPanel);
		f.setVisible(true);
	}
	
	/**
	 * @return a panel which represents a tab indicating one of the states.
	 * @param tabName is the name of the tab.
	 * @param tabNmbr is the number of the tab.
	 * @param col is  the color of the tab.
	 */
	public JPanel createTab(String tabName, int tabNmbr, Color col) {
		JPanel page = createPanel(col, tabsX, TABSY);
		JLabel label = new JLabel(tabName);
		label.setFont(new Font("Arial", Font.PLAIN, TABTEXTFONTSIZE));
		page.add(label);
		c.gridx = tabNmbr;
		c.gridy = 0;
		page.setBorder(new EmptyBorder(BORDERSIZE, BORDERSIZE, BORDERSIZE, BORDERSIZE));
		page.addMouseListener(mouse);
		return page;
	}
	
	/**
	 * @return the height of the state panel.
	 */
	public int getStatePanelSize() {
		return Toolkit.getDefaultToolkit().getScreenSize().height - TABSY;
	}
	
	/**
	 * @return the width of the tabs.
	 */
	public int getTabsX() {
		return tabsX;
	}
	
	/**
	 * @return the height of the tabs.
	 */
	public int getTabsY() {
		return TABSY;
	}
	
	/**
	 * @return the panel state.
	 */
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
	 */
	public void makeFrame() {
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setTitle("PRODUCT NAME");
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	/**
	 * Method which closes the GUI.
	 */
	public void closeFrame() {
		f.dispose();
	}
	
	/**
	 * @return return the width of the screen.
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * @param screenWidth the width of the screen.
	 */
	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	/**
	 * Class which handles moue events.
	 */
	class MouseHandler implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			varPanel.removeAll();
			if (e.getSource() == inputTab) {
				setState(inputState);
			}
			else if (e.getSource() == codeTab) {
				setState(codeState);
			}
			else if (e.getSource() == outputTab) {
				setState(outputState);
			}
			varPanel.add(state.loadPanel());
			varPanel.revalidate();
		}

		@Override
		public void mousePressed(MouseEvent e) { }

		@Override
		public void mouseReleased(MouseEvent e) { }

		@Override
		public void mouseEntered(MouseEvent e) { }
		
		@Override
		public void mouseExited(MouseEvent e) { }
	}	
}
