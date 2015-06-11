package context.healthinformatics.graphs.graphspanel;

import java.awt.event.ActionListener;

import javax.swing.JPanel;

import context.healthinformatics.graphs.GraphInputContainer;

/**
 * Interface for creating panels for graphs.
 */
public interface GraphPanel extends ActionListener {
	
	int CHECKBOXHEIGHT = 25;
	int INPUTELEMENTS = 4;
	
	/**
	 * Initialize the container for the panel.
	 */
	void updateContainer();
	
	/**
	 * @param title the name of the graph where this panel is for.
	 * @return a general graph panel with for a title and specific data elements.
	 */
	JPanel initGraphPanel(String title);
	
	/**
	 * @return the panel with all data on it.
	 */
	JPanel loadPanel();
	
	/**
	 * @return the container of the graph.
	 */
	GraphInputContainer getGraphContainer();

}
