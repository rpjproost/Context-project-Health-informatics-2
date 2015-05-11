package context.healthinformatics.GUI;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.JPanel;

/**
 * Class which represents one of the states for the variabel panel in the mainFrame.
 */
public class OutputPage implements PanelState, Serializable {

	private static final long serialVersionUID = 1L;
	private MainFrame mf;
	
	/**
	 * Constructor.
	 * @param m is the mainframe object
	 */
	public OutputPage(MainFrame m) {
		mf = m;
	}

	@Override
	public JPanel loadPanel() {
		JPanel panel = MainFrame.createPanel(Color.decode("#086A87"),
				mf.getScreenWidth(), mf.getStatePanelSize());
		return panel;
	}
	

}
