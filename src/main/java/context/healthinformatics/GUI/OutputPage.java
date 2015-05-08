package context.healthinformatics.GUI;

import java.awt.Color;

import javax.swing.JPanel;

public class OutputPage implements PanelState{

	public OutputPage() {}

	@Override
	public JPanel loadPanel() {
		JPanel panel = MainFrame.createPanel(Color.decode("#086A87"), MainFrame.getScreenWidth(), MainFrame.getStatePanelSize());
		return panel;
	}
	

}
