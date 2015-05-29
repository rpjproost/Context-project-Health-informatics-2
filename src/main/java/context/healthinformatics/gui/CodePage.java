package context.healthinformatics.gui;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import context.healthinformatics.analyse.Interpreter;

/**
 * Class which represents one of the states for the variabel panel in the mainFrame.
 */
public class CodePage extends InterfaceHelper implements PanelState, Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final int FIELDCORRECTION = 200;
	private static final int ANALYZEBUTTONWIDTH = 200;
	private static final int ANALYZEBUTTONHEIGHT = 75;
	private MainFrame mf;
	private JTextArea code;
	private JButton button;
	
	/**
	 * Constructor.
	 * @param m is the mainframe object
	 */
	public CodePage(MainFrame m) {
		mf = m;
	}

	@Override
	public JPanel loadPanel() {
		JPanel panel = MainFrame.createPanel(Color.decode("#01A9DB"),
				mf.getScreenWidth(), mf.getStatePanelSize());
		panel.setLayout(new GridBagLayout());
		code = createTextField(mf.getScreenWidth() / 2, mf.getStatePanelSize() - FIELDCORRECTION);
		panel.add(code, setGrids(0, 0));
		button = createButton("Analyse", ANALYZEBUTTONWIDTH, ANALYZEBUTTONHEIGHT);
		button.addActionListener(new ActionHandler());
		panel.add(button, setGrids(1, 1));
		code.setText("filter date on= 2012-07-01 < result");
		return panel;
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
			String text = code.getText();
			Interpreter interp = new Interpreter();
			interp.interpret(text);
		}
	}
}
