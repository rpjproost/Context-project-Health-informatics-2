package context.healthinformatics.gui;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import context.healthinformatics.analyse.Interpreter;
import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.interfacecomponents.IntermediateResults;

/**
 * Class which represents one of the states for the variabel panel in the
 * mainFrame.
 */
public class CodePage extends InterfaceHelper implements PanelState,
		Serializable {

	private static final long serialVersionUID = 1L;
	private static final int FIELDCORRECTION = 200;
	private static final int ANALYZEBUTTONWIDTH = 200;
	private static final int ANALYZEBUTTONHEIGHT = 75;
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
		codePageParentpanel = MainFrame.createPanel(MainFrame.CODETABCOLOR,
				mf.getScreenWidth(), mf.getStatePanelSize());
		codePageParentpanel.setLayout(new GridBagLayout());
		setLeftPanelWithCodeField();
		setRightPanelWithIntermediateResult();

		return codePageParentpanel;
	}

	private void setLeftPanelWithCodeField() {
		codeTextArea = createTextField(mf.getScreenWidth() / 2,
				mf.getStatePanelSize() / 2 - FIELDCORRECTION);
		leftPanel.add(codeTextArea, setGrids(0, 0));
		button = createButton("Analyse", ANALYZEBUTTONWIDTH,
				ANALYZEBUTTONHEIGHT);
		button.addActionListener(new ActionHandler());
		leftPanel.add(button, setGrids(0, 1));

		codePageParentpanel.add(leftPanel, setGrids(0, 0));
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
