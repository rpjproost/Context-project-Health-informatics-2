package context.healthinformatics.GUI;

import java.awt.Color;

import javax.swing.JPanel;

/**
 * Class which represents one of the states for the variabel panel in the mainFrame.
 */
public class CodePage implements PanelState {

	/**
	 * Constructor.
	 */
	public CodePage() { }

	@Override
	public JPanel loadPanel() {
		JPanel panel = MainFrame.createPanel(Color.decode("#01A9DB"),
				MainFrame.getScreenWidth(), MainFrame.getStatePanelSize());
		return panel;
	}

}
