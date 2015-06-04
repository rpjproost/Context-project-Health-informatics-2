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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import context.healthinformatics.analyse.Interpreter;
import context.healthinformatics.analyse.SingletonInterpreter;
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
	private static final int FIELDCORRECTION = 200;
	private static final int ANALYZEBUTTONWIDTH = 200;
	private static final int ANALYZEBUTTONHEIGHT = 100;
	private static final int INSETS = 10;
	private MainFrame mf;
	private JTextArea codeTextArea;
	private JTextArea intermediateResult;
	private JButton button;
	private Interpreter interpreter = SingletonInterpreter.getInterpreter();
	private JPanel codePageParentpanel;
	private JPanel leftPanel;
	private JPanel rightPanel;

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
		codePageParentpanel = MainFrame.createPanel(MainFrame.CODETABCOLOR,
				mf.getScreenWidth(), mf.getStatePanelSize());
		codePageParentpanel.setLayout(new GridBagLayout());

		setLeftPanelWithCodeField();
		setRightPanelWithIntermediateResult();

		return codePageParentpanel;
	}
	
	private void mergeTables() {
		if (SingletonDb.getDb().getTables().size() > 0) {
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

	private void setLeftPanelWithCodeField() {
		setCodeInputAreaTitle();
		setCodeInputArea();
		setHelpArea();
		button = createButton("Analyse", ANALYZEBUTTONWIDTH,
				ANALYZEBUTTONHEIGHT);
		button.addActionListener(new ActionHandler());
		leftPanel.add(button, setGrids(0, 3));

		codePageParentpanel.add(leftPanel, setGrids(0, 0));
	}

	private void setCodeInputAreaTitle() {
		JLabel codeTextTitleLabel = createTitle("Code input area:");
		GridBagConstraints c = setGrids(0, 0);
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, INSETS, 0, 0);
		leftPanel.add(codeTextTitleLabel, c);
	}
	
	private void setCodeInputArea() {
		codeTextArea = createTextField(((mf.getScreenWidth() / 2) - (2 * INSETS)),
				mf.getStatePanelSize() / 2 - FIELDCORRECTION);
		GridBagConstraints c = setGrids(0, 1);
		c.insets = new Insets(0, INSETS, INSETS, INSETS);
		leftPanel.add(codeTextArea, c);
	}
	
	private void setHelpArea() {
		ReadHelpInfoFromTXTFile test = new ReadHelpInfoFromTXTFile(
				"src/main/data/guihelpdata/inputpagehelp.txt");
		try {
			test.parse();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<HelpFrameInfoContainer> listOfHelpFrameInfo = test
				.getHelpFrameInfoContainer();
		HelpFrame helpFrame = new HelpFrame("Input Page Help", listOfHelpFrameInfo);
		leftPanel.add(helpFrame.getJPanel(), setGrids(0, 2));
	}

	private void setRightPanelWithIntermediateResult() {
		IntermediateResults imr = new IntermediateResults(mf);
		rightPanel.add(imr.loadPanel(), setGrids(0, 0));
		codePageParentpanel.add(rightPanel, setGrids(1, 0));
	}

	/**
	 * Appends result Strings to intermediate result textfield.
	 * 
	 * @param res
	 *            String to be appended in textfield.
	 */
	public void setResult(String res) {
		intermediateResult.append(res);
	}

	/**
	 * Clears textfield of intermediate result.
	 */
	public void emptyResult() {
		intermediateResult.setText("");
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
			// String text = code.getText();
			// Interpreter interp = new Interpreter();
			// interp.interpret(text);
		}
	}
}
