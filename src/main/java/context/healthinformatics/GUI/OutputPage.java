package context.healthinformatics.GUI;

import java.awt.Color;

import javax.swing.JPanel;

/**
 * Class which represents one of the states for the variabel panel in the mainFrame.
 */
public class OutputPage implements PanelState {

	/**
	 * Constructor.
	 */
	public OutputPage() { }

	@Override
	public JPanel loadPanel() {
		JPanel panel = MainFrame.createPanel(Color.decode("#086A87"),
				MainFrame.getScreenWidth(), MainFrame.getStatePanelSize());
		return panel;
	}
	

}
