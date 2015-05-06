package context.healthinformatics.GUI;

import java.awt.Color;

import javax.swing.JPanel;

public class InputPage implements PanelState {
	
	private static final long serialVersionUID = 1L;
	//private Color color = Color.green;
	MainFrame mf;
	
	public InputPage(MainFrame mainFrame) {
		this.mf = mainFrame;
	}

	public JPanel loadPanel() {
		JPanel panel = MainFrame.createPanel(Color.gray, mf.getTabsX(), mf.getStatePanelSize());
		return panel;
	}
	
}
