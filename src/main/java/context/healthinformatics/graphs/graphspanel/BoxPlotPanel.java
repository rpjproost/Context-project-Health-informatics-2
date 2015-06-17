package context.healthinformatics.graphs.graphspanel;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import context.healthinformatics.graphs.BoxPlot;
import context.healthinformatics.graphs.GraphInputContainer;
import context.healthinformatics.gui.MainFrame;

/**
 * Creates a panel specific for the box plot.
 */
public class BoxPlotPanel extends GraphPanel {

	private static final long serialVersionUID = 1L;
	private JCheckBox checkbox;
	private JPanel boxPlotPanel;
	private GraphInputContainer container;
	private int panelWidth;
	private JPanel boxPlotGraph;
	private BoxPlot boxPlot;

	/**
	 * Creates a action listener for the check-box and makes a new container for
	 * this graph panel.
	 * 
	 * @param width
	 *            the width for this panel.
	 */
	public BoxPlotPanel(int width) {
		panelWidth = width;
		boxPlot = new BoxPlot();
		container = new GraphInputContainer();
		boxPlotPanel = initGraphPanel("Box Plot");
		boxPlotGraph = boxPlot.getPanel();
		boxPlotGraph.setVisible(false);
		this.checkbox = createCheckBox("Box Plot", MainFrame.OUTPUTTABCOLOR);
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
		graphPanel.add(
				makeFormRowPanelWithComboBox("Select data for type:",
						container.getxValue(), panelWidth - 2 * INSETS,
						CHECKBOXHEIGHT), setGrids(0, 2));
		graphPanel.add(
				makeFormRowPanelWithComboBox("Select column for values:",
						container.getyValue(), panelWidth - 2 * INSETS,
						CHECKBOXHEIGHT), setGrids(0, THREE));
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

	@Override
	public JCheckBox getCheckbox() {
		return checkbox;
	}

	@Override
	public boolean isSelected() {
		return checkbox.isSelected();
	}

	@Override
	public void plot() {
		String type = container.getSelectedColumnX();
		ArrayList<String> columns = new ArrayList<String>();
		columns.add(container.getSelectedColumnY());
		try {
			if (type.equals("All")) {
				boxPlot.setDataPerColumn(columns);
			} else if (type.equals("Chunks")) {
				boxPlot.setDataPerChunk(columns);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),
					"Error creating BoxPlot!", JOptionPane.WARNING_MESSAGE);
		}
		boxPlotGraph = boxPlot.getPanel();
		boxPlotGraph.setVisible(true);
		boxPlot.createBoxPlot(container.getGraphTitleValue());
	}

	@Override
	public JPanel getGraphPanel() {
		return boxPlotGraph;
	}

}
