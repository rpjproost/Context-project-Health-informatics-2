package context.healthinformatics.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 * Class which represents one of the states for the variabel panel in the mainFrame.
 */
public class OutputPage implements PanelState, Serializable {

	private static final long serialVersionUID = 1L;
	private static final int FONTSIZE = 18;
	private static final int BUTTONWIDTH = 300;
	private static final int BUTTONHEIGHT = 50;
	private MainFrame mf;
	private JButton exportFile;
	
	/**
	 * Constructor.
	 * @param m is the mainframe object
	 */
	public OutputPage(MainFrame m) {
		mf = m;
	}

	/**
	 * Load the panel which contains all components.
	 */
	@Override
	public JPanel loadPanel() {
		JPanel panel = MainFrame.createPanel(Color.decode("#086A87"),
				mf.getScreenWidth(), mf.getStatePanelSize());
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		exportFile = createButton("Export File...");
		panel.add(exportFile, c);
		return panel;
	}
	
	/**
	 * Creates a button with default buttonwidth and buttonheight.
	 * @param name of the button.
	 * @return a button with the name.
	 */
	private JButton createButton(String name) {
		JButton button = new JButton(name);
		button.setPreferredSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT));
		button.setFont(new Font("Arial", Font.ITALIC, FONTSIZE));
		button.addActionListener(new ActionHandler());
		return button;
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
			JFileChooser c = new JFileChooser();
			if (e.getSource() == exportFile) {
				int rVal = c.showSaveDialog(exportFile);
			      if (rVal == JFileChooser.APPROVE_OPTION) {
			        exportFile.setText(c.getCurrentDirectory().toString() 
			        		+ "\\" + c.getSelectedFile().getName());
			      }
			}
		}
	}

}
