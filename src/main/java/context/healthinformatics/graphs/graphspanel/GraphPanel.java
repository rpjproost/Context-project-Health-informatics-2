package context.healthinformatics.graphs.graphspanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import context.healthinformatics.graphs.GraphInputContainer;
import context.healthinformatics.gui.InterfaceHelper;

/**
 * Interface for creating panels for graphs.
 */
public abstract class GraphPanel extends InterfaceHelper implements ActionListener {

	private static final long serialVersionUID = 1L;
	protected static final int CHECKBOXHEIGHT = 25;
	protected static final int INPUTELEMENTS = 4;
	
	/**
	 * Initialize the container for the panel.
	 */
	public abstract void updateContainer();
	
	/**
	 * @param title the name of the graph where this panel is for.
	 * @return a general graph panel with for a title and specific data elements.
	 */
	abstract JPanel initGraphPanel(String title);
	
	/**
	 * @return the panel with all data on it.
	 */
	public abstract JPanel loadPanel();
	
	/**
	 * @return the checkbox of the panel
	 */
	public abstract JCheckBox getCheckbox();
	
	/**
	 * Makes a field for the title of a graph and sets the title of panel itself.
	 * @param title the title of the panel.
	 * @param panelWidth the width of the panel.
	 * @param needFields the number of input fields is needed.
	 * @param container the container with data for the title of the graph.
	 * @return a new panel with a field to input a title for the graph.
	 */
	protected JPanel makePanel(String title, int panelWidth, int needFields, 
			GraphInputContainer container) {
		JPanel graphPanel = createEmptyWithGridBagLayoutPanel(Color.WHITE);
		graphPanel.setPreferredSize(new Dimension(panelWidth - 2 * INSETS, 
				needFields * CHECKBOXHEIGHT));
		graphPanel.setVisible(false);
		graphPanel.add(createSubTitle(title, panelWidth, CHECKBOXHEIGHT), setGrids(0, 0));
		graphPanel.add(makeFormRowPanelWithTextField("Graph Title:", 
				container.getGraphTitle(), panelWidth - 2 * INSETS, CHECKBOXHEIGHT), 
				setGrids(0, 1));
		return graphPanel;
	}

	/**
	 * @return boolean if the checkbox is selected or not.
	 */
	public abstract boolean isSelected();

	/**
	 * plots the specific graph that belongs to the input.
	 */
	public abstract void plot();

	/**
	 * @return the panel of the graph itself.
	 */
	public abstract JPanel getGraphPanel();

}
