package context.healthinformatics.graphs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import context.healthinformatics.graphs.graphspanel.BoxPlotPanel;
import context.healthinformatics.graphs.graphspanel.FrequencyBarPanel;
import context.healthinformatics.graphs.graphspanel.GraphPanel;
import context.healthinformatics.graphs.graphspanel.HistogramPanel;
import context.healthinformatics.graphs.graphspanel.TransitionaMatrixPanel;
import context.healthinformatics.gui.InterfaceHelper;
import context.healthinformatics.gui.MainFrame;

/**
 * Creates a interface to specify which data the user want to use to create a specific graph.
 */
public class GraphInputInterface extends InterfaceHelper {

	private static final long serialVersionUID = 1L;
	private static final int CHECKBOXHEIGHT = 25;
	private static final int CHECKBOXESINSETS = 40;
	private int graphInputWidth;
	private int graphInputHeight;
	private JPanel graphInputParentPanel;
	private JPanel containerScrollPanel;
	private JScrollPane scrollPane;
	private ArrayList<GraphPanel> graphPanels;

	/**
	 * Creates the interface.
	 * @param width the width of the panel.
	 * @param height the height of the panel.
	 * @param color the color of the panel.
	 */
	public GraphInputInterface(int width, int height, Color color) {
		graphPanels = new ArrayList<GraphPanel>();
		graphInputWidth = width;
		graphInputHeight = height;
		graphInputParentPanel = createEmptyWithGridBagLayoutPanel(color);
		graphInputParentPanel.setPreferredSize(new Dimension(graphInputWidth, graphInputHeight));
		initPanels();
		initScrollPane();
		initGraphPanels();
		initAll();
	}

	/**
	 * @return the main panel of this interface.
	 */
	public JPanel loadPanel() {
		return graphInputParentPanel;
	}
	
	/**
	 * Creates for each graph type a container.
	 */
	private void initPanels() {
		graphPanels.add(new BoxPlotPanel(graphInputWidth));
		graphPanels.add(new FrequencyBarPanel(graphInputWidth));
		graphPanels.add(new TransitionaMatrixPanel(graphInputWidth));
		graphPanels.add(new HistogramPanel(graphInputWidth));
	}
	
	/**
	 * Initialize a scroll panel for all possible graph panels.
	 */
	private void initScrollPane() {
		containerScrollPanel = createContainerWithGivenSizePanel(graphInputWidth, 
				graphInputHeight - CHECKBOXHEIGHT);
		containerScrollPanel.setBackground(Color.WHITE);
		scrollPane = makeScrollPaneForContainerPanel(containerScrollPanel,
				graphInputWidth, graphInputHeight - CHECKBOXHEIGHT); 
	}
	
	/**
	 * Initialize for each possible graph a panel, for inputting data.
	 */
	private void initGraphPanels() {
		for (int i = 0; i < graphPanels.size(); i++) {
			containerScrollPanel.add(graphPanels.get(i).loadPanel(), 
					setGrids(0, i, new Insets(INSETS, 0, 0, 0)));
		}
	}

	/**
	 * Initialize all different panels to the main panel.
	 */
	private void initAll() {
		initCheckBoxesToPanel();
		graphInputParentPanel.add(scrollPane, setGrids(0, 1));
	}

	/**
	 * Initialize all different check-boxes to one panel.
	 * That one panel will be set on the main panel.
	 */
	private void initCheckBoxesToPanel() {
		JPanel checkboxes = createPanel(MainFrame.OUTPUTTABCOLOR, graphInputWidth, CHECKBOXHEIGHT);
		for (int i = 0; i < graphPanels.size(); i++) {
			checkboxes.add(graphPanels.get(i).getCheckbox(), 
					setGrids(i, 0, new Insets(0, 0, 0, CHECKBOXESINSETS)));
		}
		graphInputParentPanel.add(checkboxes, setGrids(0, 0));
	}

	/**
	 * Updates all panels.
	 */
	public void update() {
		for (GraphPanel panel : graphPanels) {
			panel.updateContainer();
		}
	}

	/**
	 * @return the graph panels of the interface.
	 */
	public ArrayList<GraphPanel> getGraphPanels() {
		return graphPanels;
	}
}
