package context.healthinformatics.graphs.graphspanel;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import context.healthinformatics.graphs.GraphInputContainer;
import context.healthinformatics.gui.MainFrame;

/**
 * Creates a panel specific for the box plot.
 */
public class HistogramPanel extends GraphPanel {

	private static final long serialVersionUID = 1L;
	private JCheckBox checkbox;
	private JPanel histogramPanel;
	private GraphInputContainer container;
	private int panelWidth;
	
	/**
	 * Creates a action listener for the check-box and makes a new container for this graph panel.
	 * @param width the width for this panel.
	 */
	public HistogramPanel(int width) {
		panelWidth = width;
		container = new GraphInputContainer();
		histogramPanel = initGraphPanel("Histogram");
		this.checkbox = createCheckBox("Histogram", MainFrame.OUTPUTTABCOLOR);
		this.checkbox.addActionListener(this);
	}
	
	/**
	 * Initialize the graph panel for the histogram.
	 */
	@Override
	public JPanel initGraphPanel(String title) {
		JPanel graphPanel = makePanel(title, panelWidth, FOUR,  container);
		graphPanel.add(makeFormRowPanelWithComboBox("Select data for the x-as:", 
				container.getxValue(), panelWidth - 2 * INSETS, CHECKBOXHEIGHT), 
				setGrids(0, 2));
		graphPanel.add(makeFormRowPanelWithComboBox("Select column for the y-as:", 
				container.getyValue(), panelWidth - 2 * INSETS, CHECKBOXHEIGHT), 
				setGrids(0, THREE));
		return graphPanel;
	}

	/**
	 * Performs an action if the histogram check-box is clicked.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == checkbox) {
			checkVisibility(checkbox, histogramPanel);
		}
	}
	
	/**
	 * @return the panel with all data.
	 */
	@Override
	public JPanel loadPanel() {
		return histogramPanel;
	}

	@Override
	public void updateContainer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GraphInputContainer getGraphContainer() {
		return container;
	}

	@Override
	public JCheckBox getCheckbox() {
		return checkbox;
	}

	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void plot() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JPanel getGraphPanel() {
		// TODO Auto-generated method stub
		return new JPanel();
	}

}
