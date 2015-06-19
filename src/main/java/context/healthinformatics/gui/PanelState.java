package context.healthinformatics.gui;

import javax.swing.JPanel;

/**
 * Interface for the panel states.
 */
public interface PanelState {
	
	/**
	 * @return the panel which will be variable in the mainframe.
	 */
	JPanel loadPanel();
}
