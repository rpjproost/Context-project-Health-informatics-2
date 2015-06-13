package context.healthinformatics.graphs.graphspanel;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import context.healthinformatics.graphs.GraphInputContainer;

/**
 * Creates a panel specific for the box plot.
 */
public class BoxPlotPanel extends GraphPanel {

	private static final long serialVersionUID = 1L;
	private JCheckBox checkbox;
	private JPanel boxPlotPanel;
	private GraphInputContainer container;
	private int panelWidth;
	
	/**
	 * Creates a action listener for the check-box and makes a new container for this graph panel.
	 * @param checkbox the check-box that is for this panel.
	 * @param width the width for this panel.
	 */
	public BoxPlotPanel(JCheckBox checkbox, int width) {
		this.checkbox = checkbox;
		panelWidth = width;
		container = new GraphInputContainer();
		boxPlotPanel = initGraphPanel("Box Plot");
		this.checkbox.addActionListener(this);
	}
	
	@Override
	public void updateContainer() {
		container.updateY(getColumnNames());
	}
	
	/**
	 * Initialize the graph panel for the box plot.
	 */
	@Override
	public JPanel initGraphPanel(String title) {
		JPanel graphPanel = makePanel(title, panelWidth, FOUR, container);
		graphPanel.add(makeFormRowPanelWithComboBox("Select data for type:", 
				container.getxValue(), panelWidth - 2 * INSETS, CHECKBOXHEIGHT), 
				setGrids(0, 2));
		graphPanel.add(makeFormRowPanelWithComboBox("Select column for values:", 
				container.getyValue(), panelWidth - 2 * INSETS, CHECKBOXHEIGHT), 
				setGrids(0, THREE));
		return graphPanel;
	}

	/**
	 * Performs an action if the checkbox is clicked.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == checkbox) {
			checkVisibility(checkbox, boxPlotPanel);
		}
	}
	
	/**
	 * @return the panel with all data.
	 */
	@Override
	public JPanel loadPanel() {
		return boxPlotPanel;
	}

	@Override
	public GraphInputContainer getGraphContainer() {
		return container;
	}

}
