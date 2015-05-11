package context.healthinformatics.GUI;

import javax.swing.JPanel;

/**
 * Interface for the panel states.
 */
public interface PanelState {
	
	/**
	 * @return the panel which will be variable in the maninframe.
	 */
	JPanel loadPanel();

}
