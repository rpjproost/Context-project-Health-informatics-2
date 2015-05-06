package context.healthinformatics.GUI;

import java.awt.Color;

import javax.swing.JPanel;

public class InputPage implements PanelState {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InputPage(MainFrame mainFrame) {
		// TODO Auto-generated constructor stub
	}

	public JPanel loadPanel() {
		JPanel panel = GUI.createPanel(Color.blue,100,200);
		return panel;
	}
	
}
