package context.healthinformatics.graphs;

import java.util.ArrayList;

import javax.swing.JPanel;

import context.healthinformatics.graphs.graphspanel.GraphPanel;
import context.healthinformatics.gui.InterfaceHelper;

/**
 * Controls the graphs.
 */
public class GraphController extends InterfaceHelper {

	private static final long serialVersionUID = 1L;
	private GraphInputInterface graphInput;

	/**
	 * Constructs for each graph a class.
	 * @param graphInput the interface to control the graphs.
	 */
	public GraphController(GraphInputInterface graphInput) {
		this.graphInput = graphInput;
	}

	/**
	 * updates the graphs which are needed to be plotted.
	 */
	public void updateGraphs() {
		ArrayList<GraphPanel> panels = graphInput.getGraphPanels();
		for (GraphPanel graphPanel : panels) {
			if (graphPanel.isSelected()) {
				graphPanel.plot();
			} else {
				graphPanel.getGraphPanel().setVisible(false);
			}
		}
	}
	
	/**
	 * @param panel to add all graphs on.
	 * @return a panel with all possible graphs.
	 */
	public JPanel getPlot(JPanel panel) {
		ArrayList<GraphPanel> panels = graphInput.getGraphPanels();
		for (int i = 0; i < panels.size(); i++) {
			panel.add(panels.get(i).getGraphPanel(), setGrids(0, i));
		}
		return panel;
	}
}
