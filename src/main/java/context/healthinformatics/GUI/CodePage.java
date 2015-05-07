package context.healthinformatics.GUI;

import java.awt.Color;

import javax.swing.JPanel;

public class CodePage implements PanelState{

	public CodePage() {}

	@Override
	public JPanel loadPanel() {
		JPanel panel = MainFrame.createPanel(Color.decode("#01A9DB"), MainFrame.getScreenWidth(), MainFrame.getStatePanelSize());
		return panel;
	}

}
