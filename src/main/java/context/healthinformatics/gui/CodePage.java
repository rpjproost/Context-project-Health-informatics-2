package context.healthinformatics.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import context.healthinformatics.analyse.Interpreter;
import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.help.HelpController;
import context.healthinformatics.interfacecomponents.IntermediateResults;

/**
 * Class which represents one of the states for the variabel panel in the
 * mainFrame.
 */
public class CodePage extends InterfaceHelper implements PanelState,
		Serializable, ActionListener {

	private static final long serialVersionUID = 1L;
	private static final int FIELDCORRECTION = 130;
	private static final int ANALYZEBUTTONWIDTH = 220;
	private static final int ANALYZEBUTTONHEIGHT = 80;
	private static final int INSETS = 10;
	private static final int THREE = 3;
	private static final int FOUR = 4;

	private MainFrame mf;
	private JTextArea codeTextArea;
	private JTextArea oldCodeArea;
	private Interpreter interpreter;
	private JPanel codePageParentpanel;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JButton analyseButton;
	private JButton goBackButton;
	private JButton goToOutputPageButton;
	private JButton helpButton;
	private HelpController helpController;
	private JScrollPane scrollWithTextArea;
	private JScrollPane scrollBarForOldCodeArea;
	private IntermediateResults imr;

	private int panelWidth;
	private int panelHeight;

	private static final String TEXT_SUBMIT = "text-submit";
	private static final String INSERT_BREAK = "insert-break";

	/**
	 * Constructor.
	 * 
	 * @param m
	 *            is the mainframe object
	 */
	public CodePage(MainFrame m) {
		mf = m;
		imr = new IntermediateResults(mf);
		leftPanel = createEmptyWithGridBagLayoutPanel(MainFrame.CODETABCOLOR);
		rightPanel = createEmptyWithGridBagLayoutPanel(MainFrame.CODETABCOLOR);
		helpController = new HelpController(
				"src/main/data/guihelpdata/codepagehelp.txt");
		initComponents();
		initAll();
	}

	/**
	 * Load the panel of the code page.
	 * 
	 * @return the panel of the code page
	 */
	public JPanel loadPanel() {
		imr.updateIntermediateResult();
		JPanel panel = createPanel(MainFrame.CODETABCOLOR, mf.getScreenWidth(),
				mf.getStatePanelSize());
		panel.add(codePageParentpanel);
		return panel;
	}

	/**
	 * Init all components of the code page.
	 */
	private void initComponents() {
		initPanelWidthAndHeight();
		initButtons();
		initTextFields();
		initButtonActionListeners();
	}

	/**
	 * Set the width and height of the panel.
	 */
	private void initPanelWidthAndHeight() {
		panelWidth = mf.getScreenWidth() / 2 - 2 * INSETS;
		panelHeight = mf.getStatePanelSize() / 2 - FIELDCORRECTION;
	}

	private void initButtons() {
		goBackButton = createButton("Go Back", ANALYZEBUTTONWIDTH,
				ANALYZEBUTTONHEIGHT);
		analyseButton = createButton("Analyse", ANALYZEBUTTONWIDTH,
				ANALYZEBUTTONHEIGHT);
		goToOutputPageButton = createButton("Go to Output", ANALYZEBUTTONWIDTH,
				ANALYZEBUTTONHEIGHT);
		helpButton = createButton("Help", ANALYZEBUTTONWIDTH,
				ANALYZEBUTTONHEIGHT);
	}

	/**
	 * Initialise the textfields.
	 */
	private void initTextFields() {
		initCodeArea();
		initOldCodeArea();
	}

	/**
	 * Create the text area for code with focus.
	 */
	private void initCodeArea() {
		codeTextArea = createTextAreaWithFocus();
		scrollWithTextArea = makeScrollPaneForTextArea(codeTextArea,
				mf.getScreenWidth() / 2 - 2 * INSETS, mf.getStatePanelSize()
						/ 2 - FIELDCORRECTION);
		addKeyListenerCodeTextArea();
	}

	/**
	 * Set keylisteners to the code text area so that enter interprets code and
	 * shift enter creates a new line.
	 */
	private void addKeyListenerCodeTextArea() {
		InputMap input = codeTextArea.getInputMap();
		KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
		KeyStroke shiftEnter = KeyStroke.getKeyStroke("shift ENTER");
		input.put(shiftEnter, INSERT_BREAK);
		input.put(enter, TEXT_SUBMIT);
		ActionMap actions = codeTextArea.getActionMap();
		actions.put(TEXT_SUBMIT, new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				analyseCodeArea();
			}
		});
	}

	/**
	 * Init the textarea to display the old code.
	 */
	private void initOldCodeArea() {
		oldCodeArea = creatTextArea();
		oldCodeArea.setEditable(false);
		Border border = BorderFactory.createLineBorder(Color.GRAY, 1);
		scrollBarForOldCodeArea = makeScrollPaneForTextArea(oldCodeArea,
				panelWidth, panelHeight);
		oldCodeArea.setBorder(border);
	}

	/**
	 * Set all button action listeners.
	 */
	private void initButtonActionListeners() {
		goBackButton.addActionListener(this);
		analyseButton.addActionListener(this);
		goToOutputPageButton.addActionListener(this);
		helpButton.addActionListener(this);
	}

	/**
	 * Init all the panels with components.
	 */
	private void initAll() {
		codePageParentpanel = createPanel(MainFrame.CODETABCOLOR,
				mf.getScreenWidth(), mf.getStatePanelSize());
		codePageParentpanel.setLayout(new GridBagLayout());
		leftPanel.setPreferredSize(new Dimension(mf.getScreenWidth() / 2, mf
				.getStatePanelSize()));
		setLeftPanelWithCodeField();
		setRightPanelWithOldIntermediateResult();
	}

	/**
	 * Creates the left side of the analyze tab interface.
	 */
	private void setLeftPanelWithCodeField() {
		setCodeInputAreaTitle();
		setCodeInputArea();
		setOldCodeAreaTitle();
		setOldCodeArea();
		setButtonArea();
		codePageParentpanel.add(leftPanel, setGrids(0, 0));
	}

	/**
	 * Sets a title to the code input area panel.
	 */
	private void setCodeInputAreaTitle() {
		GridBagConstraints c = setGrids(0, 0);
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, INSETS, 0, 0);
		leftPanel.add(createTitle("Code input area:"), c);
	}

	/**
	 * Sets the code input area.
	 */
	private void setCodeInputArea() {
		GridBagConstraints c = setGrids(0, 1);
		c.insets = new Insets(0, INSETS, INSETS, INSETS);
		leftPanel.add(scrollWithTextArea, c);
	}

	/**
	 * Sets a title for the used code to analyze the data.
	 */
	private void setOldCodeAreaTitle() {
		JLabel oldCodeTitle = createTitle("Used Code:");
		GridBagConstraints c = setGrids(0, 2, new Insets(0, INSETS, 0, 0));
		c.anchor = GridBagConstraints.LINE_START;
		leftPanel.add(oldCodeTitle, c);
	}

	/**
	 * Sets a area to shown whoch codes the users used.
	 */
	private void setOldCodeArea() {
		JPanel oldCodePanel = createPanel(Color.WHITE, panelWidth, panelHeight);
		oldCodePanel.add(scrollBarForOldCodeArea);
		leftPanel.add(oldCodePanel,
				setGrids(0, THREE, new Insets(0, INSETS, INSETS, INSETS)));
	}

	/**
	 * Sets up the whole button area.
	 */
	private void setButtonArea() {
		JPanel buttonArea = createPanel(MainFrame.CODETABCOLOR,
				mf.getScreenWidth() / 2, FIELDCORRECTION);
		setGoBackButton(buttonArea);
		setEmptyPanel(buttonArea);
		setAnalyseButton(buttonArea);
		leftPanel.add(buttonArea, setGrids(0, FOUR));
	}

	/**
	 * Sets the go back button to a specific panel.
	 * 
	 * @param panel
	 *            the panel which the go back button will added to.
	 */
	private void setGoBackButton(JPanel panel) {
		panel.add(goBackButton,
				setGrids(0, 0, new Insets(INSETS, INSETS, INSETS, 0)));
	}

	/**
	 * Sets empty panel between the buttons.
	 * 
	 * @param panel
	 *            which the empty panel will be added to.
	 */
	private void setEmptyPanel(JPanel panel) {
		JPanel emptyPanel = createPanel(MainFrame.CODETABCOLOR,
				mf.getScreenWidth() / 2 - 2 * ANALYZEBUTTONWIDTH - 2 * INSETS,
				FIELDCORRECTION);
		panel.add(emptyPanel, setGrids(1, 0));
	}

	/**
	 * Sets a analyze button to the specific panel.
	 * 
	 * @param panel
	 *            which the analyze button will be added to.
	 */
	private void setAnalyseButton(JPanel panel) {
		panel.add(analyseButton,
				setGrids(2, 0, new Insets(INSETS, 0, INSETS, INSETS)));
	}

	/**
	 * Set the rightpanel.
	 */
	private void setRightPanelWithOldIntermediateResult() {
		rightPanel.add(imr.loadPanel(), setGrids(0, 2));
		codePageParentpanel.add(rightPanel, setGrids(1, 0));
		setGoToOutputPageButton();
	}

	/**
	 * Sets the button for go to output page.
	 */
	private void setGoToOutputPageButton() {
		JPanel buttonArea = createPanel(MainFrame.CODETABCOLOR,
				mf.getScreenWidth() / 2, FIELDCORRECTION);
		buttonArea.add(helpButton,
				setGrids(0, 0, new Insets(INSETS, INSETS, INSETS, 0)));
		setEmptyPanel(buttonArea);
		buttonArea.add(goToOutputPageButton,
				setGrids(THREE, 0, new Insets(INSETS, 0, INSETS, INSETS)));
		rightPanel.add(buttonArea, setGrids(0, FOUR));
	}

	/**
	 * Analyse the inputted query from the code area.
	 */
	private void analyseCodeArea() {
		interpreter = SingletonInterpreter.getInterpreter();
		String code = codeTextArea.getText();
		try {
			interpreter.interpret(code);
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(),
					"Analyse Error", JOptionPane.WARNING_MESSAGE);
		}
		imr.updateIntermediateResult();
		codeTextArea.setText("");
		oldCodeArea.append(code + "\n");
	}

	/**
	 * Action performed of buttons.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == analyseButton) {
			analyseCodeArea();
		}
		if (e.getSource() == goBackButton) {
			mf.setState(mf.getInputPage());
			mf.reloadStatePanel();
		}
		if (e.getSource() == goToOutputPageButton) {
			mf.setState(mf.getOutputPage());
			mf.reloadStatePanel();
		}
		if (e.getSource() == helpButton) {
			helpController.handleHelpButton("Code Page Help");
		}

	}
}
