package context.healthinformatics.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import context.healthinformatics.analyse.Interpreter;
import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.MergeTable;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.interfacecomponents.HelpFrame;
import context.healthinformatics.interfacecomponents.HelpFrameInfoContainer;
import context.healthinformatics.interfacecomponents.IntermediateResults;
import context.healthinformatics.parser.ReadHelpInfoFromTXTFile;

/**
 * Class which represents one of the states for the variabel panel in the
 * mainFrame.
 */
public class CodePage extends InterfaceHelper implements PanelState,
		Serializable {

	private static final long serialVersionUID = 1L;
	private static final int FIELDCORRECTION = 130;
	private static final int ANALYZEBUTTONWIDTH = 220;
	private static final int ANALYZEBUTTONHEIGHT = 80;
	private static final int INSETS = 10;
	private static final int THREE = 3;
	private static final int FOUR = 4;
	private MainFrame mf;
	private JTextArea codeTextArea;
	private Interpreter interpreter = SingletonInterpreter.getInterpreter();
	private JPanel codePageParentpanel;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JButton analyseButton;
	private JButton goBackButton;
	private IntermediateResults imr;

	/**
	 * Constructor.
	 * 
	 * @param m
	 *            is the mainframe object
	 */
	public CodePage(MainFrame m) {
		leftPanel = createEmptyWithGridBagLayoutPanel(MainFrame.CODETABCOLOR);
		rightPanel = createEmptyWithGridBagLayoutPanel(MainFrame.CODETABCOLOR);
		mf = m;
	}

	@Override
	public JPanel loadPanel() {
		mergeTables();
		codePageParentpanel = createPanel(MainFrame.CODETABCOLOR,
				mf.getScreenWidth(), mf.getStatePanelSize());
		codePageParentpanel.setLayout(new GridBagLayout());

		setLeftPanelWithCodeField();
		setRightPanelWithIntermediateResult();

		return codePageParentpanel;
	}
	
	private void mergeTables() {
		Db db = SingletonDb.getDb();
		if (db.getTables().size() > 0 && !db.getTables().containsKey("result")) {
			MergeTable mergeTables = new MergeTable();
			String[] clause = new String[1];
			clause[0] = "meeting.createdby = 'admire2'";
			try {
				mergeTables.merge(clause);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Creates the left side of the analyze tab interface.
	 */
	private void setLeftPanelWithCodeField() {
		setCodeInputAreaTitle();
		setCodeInputArea();
		setHelpAreaTitle();
		setHelpArea();
		setButtonArea();
		codePageParentpanel.add(leftPanel, setGrids(0, 0));
	}

	/**
	 * Sets a title to the code input area panel.
	 */
	private void setCodeInputAreaTitle() {
		JLabel codeTextTitleLabel = createTitle("Code input area:");
		GridBagConstraints c = setGrids(0, 0);
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, INSETS, 0, 0);
		leftPanel.add(codeTextTitleLabel, c);
	}
	
	/**
	 * Sets the code input area.
	 */
	private void setCodeInputArea() {
		codeTextArea = createTextField(mf.getScreenWidth() / 2 - 2 * INSETS,
				mf.getStatePanelSize() / 2 - FIELDCORRECTION);
		GridBagConstraints c = setGrids(0, 1);
		c.insets = new Insets(0, INSETS, INSETS, INSETS);
		leftPanel.add(codeTextArea, c);
	}
	
	/**
	 * Sets the help area title.
	 */
	private void setHelpAreaTitle() {
		JLabel helpAreaTitle = createTitle("Help");
		GridBagConstraints c = setGrids(0, 2);
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, INSETS, 0, 0);
		leftPanel.add(helpAreaTitle, c);
	}
	
	/**
	 * Sets the help area.
	 */
	private void setHelpArea() {
		ReadHelpInfoFromTXTFile test = new ReadHelpInfoFromTXTFile(
				"src/main/data/guihelpdata/codepagehelp.txt");
		try {
			test.parse();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<HelpFrameInfoContainer> listOfHelpFrameInfo = test
				.getHelpFrameInfoContainer();
		HelpFrame helpFrame = new HelpFrame("Input Page Help", 
				listOfHelpFrameInfo, mf.getScreenWidth() / 2 - 2 * INSETS,
				mf.getStatePanelSize() / 2 - FIELDCORRECTION);
		leftPanel.add(helpFrame.getJPanel(), setGrids(0, THREE));
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
	 * @param panel the panel which the go back button will added to.
	 */
	private void setGoBackButton(JPanel panel) {
		goBackButton = createButton("Go Back", ANALYZEBUTTONWIDTH,
				ANALYZEBUTTONHEIGHT);
		goBackButton.addActionListener(new ActionHandler());
		GridBagConstraints c = setGrids(0, 0);
		c.insets = new Insets(INSETS, INSETS, INSETS, 0);
		panel.add(goBackButton, c);
	}
	
	/**
	 * Sets empty panel between the buttons.
	 * @param panel which the empty panel will be added to.
	 */
	private void setEmptyPanel(JPanel panel) {
		JPanel emptyPanel = createPanel(MainFrame.CODETABCOLOR, 
				mf.getScreenWidth() / 2 - 2 * ANALYZEBUTTONWIDTH - 2 * INSETS, 
				FIELDCORRECTION);
		panel.add(emptyPanel, setGrids(1, 0));
	}
	
	/**
	 * Sets a analyze button to the specific panel.
	 * @param panel which the analyze button will be added to.
	 */
	private void setAnalyseButton(JPanel panel) {
		analyseButton = createButton("Analyse", ANALYZEBUTTONWIDTH,
				ANALYZEBUTTONHEIGHT);
		analyseButton.addActionListener(new ActionHandler());
		GridBagConstraints c = setGrids(2, 0);
		c.insets = new Insets(INSETS, 0, INSETS, INSETS);
		panel.add(analyseButton, c);
	}

	/**
	 * Sets the right side of the interface with specific panels.
	 */
	private void setRightPanelWithIntermediateResult() {
		imr = new IntermediateResults(mf);
		rightPanel.add(imr.loadPanel(), setGrids(0, 0));
		codePageParentpanel.add(rightPanel, setGrids(1, 0));
	}

	/**
	 * Class which handles the actions when buttons are clicked.
	 */
	private class ActionHandler implements ActionListener {

		/**
		 * Action when the button is pressed the save pop up will be shown.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == analyseButton) {
				try {
					String code = codeTextArea.getText();
					interpreter.interpret(code);
					imr.updateIntermediateResult();
					codeTextArea.setText("");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (e.getSource() == goBackButton) {
				System.out.println("go back");
			}
			// String text = code.getText();
			// Interpreter interp = new Interpreter();
			// interp.interpret(text);
		}
	}
}
