package context.healthinformatics.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Class which sets-up the frame of the GUI.
 */
public class MainFrame extends InterfaceHelper {

	private static final long serialVersionUID = 1L;
	private PanelState state;
	private PanelState inputState = new InputPage(this);
	private PanelState codeState = new CodePage(this);
	private PanelState outputState = new OutputPage();

	public static final Color INPUTTABCOLOR = null;
	public static final Color CODETABCOLOR = Color.decode("#DAE3E6");
	public static final Color OUTPUTTABCOLOR = Color.decode("#BDD1DE");

	public static final int NMBROFTABS = 3;
	public static final int BORDERSIZE = 10;

	private GridBagConstraints c;
	private JPanel mainPanel;
	private JFrame f;
	private JPanel inputTab;
	private JPanel codeTab;
	private JPanel outputTab;
	private JPanel varPanel = new JPanel();

	/**
	 * Constructor, which initialises the mainFrame in the inputState.
	 */
	public MainFrame() {
		state = inputState;
		load();
	}

	/**
	 * Method which sets the state of the GUI.
	 * 
	 * @param p
	 *            which is the desired state.
	 */
	public void setState(PanelState p) {
		state = p;
	}

	/**
	 * Method which sets the state of the GUI.
	 * 
	 * @return inputpage
	 */
	public PanelState getInputPage() {
		return inputState;
	}

	/**
	 * Method which sets the state of the GUI.
	 * 
	 * @return codePAge
	 */
	public PanelState getCodePage() {
		return codeState;
	}

	/**
	 * Method which sets the state of the GUI.
	 * 
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
	 * Method which creates the frame of the GUI and creates the tabs.
	 */
	public void load() {
		makeFrame();
		c = new GridBagConstraints();
		mainPanel = new JPanel(new GridBagLayout());
		setTabX(getScreenWidth() / NMBROFTABS);

		JPanel tabs = createPanel(null, getScreenWidth(), getTabsY());
		tabs.setLayout(new GridBagLayout());

		createTabs(tabs);
		c = setGrids(0, 0);
		mainPanel.add(tabs, c);

		varPanel.setLayout(new GridBagLayout());
		varPanel.add(state.loadPanel());
		c = setGrids(0, 1);
		mainPanel.add(varPanel, c);

		f.add(mainPanel);
		f.setVisible(true);
	}

	private void createTabs(JPanel tabs) {
		inputTab = createTab("Input", INPUTTABCOLOR);
		inputTab.addMouseListener(new MouseHandler());
		c = setGrids(0, 0);
		tabs.add(inputTab, c);

		codeTab = createTab("Analyse", CODETABCOLOR);
		codeTab.addMouseListener(new MouseHandler());
		c = setGrids(1, 0);
		tabs.add(codeTab, c);

		outputTab = createTab("Output", OUTPUTTABCOLOR);
		outputTab.addMouseListener(new MouseHandler());
		c = setGrids(2, 0);
		tabs.add(outputTab, c);
	}

	/**
	 * @return the panel state.
	 */
	public PanelState getPanelState() {
		return state;
	}

	/**
	 * Method which makes the outer-container-frame.
	 */
	public void makeFrame() {
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String[] arr = { "WI", "RI", "JE", "AR", "PA" };
		f.setTitle(shuffleArray(arr));
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	/**
	 * Creates the title of the program consisting of the first two letters of
	 * our names.
	 * 
	 * @param ar
	 *            the array with first two letters
	 * @return the title
	 */
	static String shuffleArray(String[] ar) {
		Random rnd = new Random();
		StringBuilder title = new StringBuilder();
		for (int i = ar.length - 1; i >= 0; i--) {
			int index = rnd.nextInt(i + 1);
			String a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
			title.append(ar[i]);
		}
		return title.toString();
	}

	/**
	 * Method which closes the GUI.
	 */
	public void closeFrame() {
		f.dispose();
	}

	/**
	 * Reload the State panel.
	 */
	public void reloadStatePanel() {
		if (!state.equals(codeState)) {
			getCodeTab().addMouseListener(new MouseHandler());
		}
		varPanel.removeAll();
		varPanel.add(state.loadPanel());
		varPanel.revalidate();
	}

	/**
	 * Class which handles moue events.
	 */
	class MouseHandler implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// Do nothing
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getSource() == inputTab) {
				setState(inputState);
			} else if (e.getSource() == codeTab) {
				codeTab.removeMouseListener(this);
				setState(codeState);
			} else if (e.getSource() == outputTab) {
				setState(outputState);
			}
			reloadStatePanel();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// Do nothing
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// Do nothing
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// Do nothing
		}
	}
}
