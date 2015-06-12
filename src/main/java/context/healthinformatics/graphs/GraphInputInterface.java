package context.healthinformatics.graphs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import context.healthinformatics.graphs.graphspanel.BoxPlotPanel;
import context.healthinformatics.graphs.graphspanel.FrequencyBarPanel;
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
	private JCheckBox boxPlotCheckBox;
	private JCheckBox frequencyCheckBox;
	private JCheckBox transitionMatrixCheckBox;
	private JCheckBox histogramCheckbox;
	private BoxPlotPanel boxPlotPanel;
	private FrequencyBarPanel frequencyPanel;
	private TransitionaMatrixPanel transitionMatrixPanel;
	private HistogramPanel histogramPanel;
	private JPanel containerScrollPanel;
	private JScrollPane scrollPane;

	/**
	 * Creates the interface.
	 * @param width the width of the panel.
	 * @param height the height of the panel.
	 * @param color the color of the panel.
	 */
	public GraphInputInterface(int width, int height, Color color) {
		graphInputWidth = width;
		graphInputHeight = height;
		graphInputParentPanel = createEmptyWithGridBagLayoutPanel(color);
		graphInputParentPanel.setPreferredSize(new Dimension(graphInputWidth, graphInputHeight));
		initCheckBoxes();
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
	 * Initialize all check-boxes.
	 */
	private void initCheckBoxes() {
		boxPlotCheckBox = createCheckBox("Box Plot", MainFrame.OUTPUTTABCOLOR);
		frequencyCheckBox = createCheckBox("Frequency Bar", MainFrame.OUTPUTTABCOLOR);
		transitionMatrixCheckBox = createCheckBox("State-Transition Matrix", 
				MainFrame.OUTPUTTABCOLOR);
		histogramCheckbox = createCheckBox("Histogram", MainFrame.OUTPUTTABCOLOR);
	}
	
	/**
	 * Creates for each graph type a container.
	 */
	private void initPanels() {
		boxPlotPanel = new BoxPlotPanel(boxPlotCheckBox, graphInputWidth);
		frequencyPanel = new FrequencyBarPanel(frequencyCheckBox, graphInputWidth);
		transitionMatrixPanel = new TransitionaMatrixPanel(transitionMatrixCheckBox, 
				graphInputWidth);
		histogramPanel = new HistogramPanel(histogramCheckbox, graphInputWidth);
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
		containerScrollPanel.add(boxPlotPanel.loadPanel(), setGrids(0, 0));
		containerScrollPanel.add(frequencyPanel.loadPanel(), 
				setGrids(0, 1, new Insets(INSETS, 0, 0, 0)));
		containerScrollPanel.add(transitionMatrixPanel.loadPanel(), 
				setGrids(0, 2, new Insets(INSETS, 0, 0, 0)));
		containerScrollPanel.add(histogramPanel.loadPanel(), 
				setGrids(0, THREE, new Insets(INSETS, 0, 0, 0)));
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
		checkboxes.add(boxPlotCheckBox, setGrids(0, 0, 
				new Insets(0, 0, 0, CHECKBOXESINSETS)));
		checkboxes.add(frequencyCheckBox, setGrids(1, 0, 
				new Insets(0, 0, 0, CHECKBOXESINSETS)));
		checkboxes.add(transitionMatrixCheckBox, setGrids(2, 0, 
				new Insets(0, 0, 0, CHECKBOXESINSETS)));
		checkboxes.add(histogramCheckbox, setGrids(THREE, 0, 
				new Insets(0, 0, 0, CHECKBOXESINSETS)));
		graphInputParentPanel.add(checkboxes, setGrids(0, 0));
	}

	/**
	 * Updates all panels.
	 */
	public void update() {
		boxPlotPanel.updateContainer();
		frequencyPanel.updateContainer();
		histogramPanel.updateContainer();
		transitionMatrixPanel.updateContainer();
	}
	
	/**
	 * @return the boxPlotPanel
	 */
	protected BoxPlotPanel getBoxPlotPanel() {
		return boxPlotPanel;
	}

	/**
	 * @return the frequencyPanel
	 */
	protected FrequencyBarPanel getFrequencyPanel() {
		return frequencyPanel;
	}

	/**
	 * @return the stemAndLeafPlotPanel
	 */
	protected TransitionaMatrixPanel getTransitionMatrixPanel() {
		return transitionMatrixPanel;
	}

	/**
	 * @return the histogramPanel
	 */
	protected HistogramPanel getHistogramPanel() {
		return histogramPanel;
	}

	/**
	 * @return the boxPlotCheckBox
	 */
	protected JCheckBox getBoxPlotCheckBox() {
		return boxPlotCheckBox;
	}

	/**
	 * @return the frequencyCheckBox
	 */
	protected JCheckBox getFrequencyCheckBox() {
		return frequencyCheckBox;
	}

	/**
	 * @return the stemAndLeafPlotCheckBox
	 */
	protected JCheckBox getTransitionMatrixCheckBox() {
		return transitionMatrixCheckBox;
	}

	/**
	 * @return the histogramCheckbox
	 */
	protected JCheckBox getHistogramCheckbox() {
		return histogramCheckbox;
	}

	/**
	 * @param checkbox the check-box that must be checked.
	 * @return  true if the check-box is selected else false.
	 */
	protected boolean isSelected(JCheckBox checkbox) {
		return checkbox.isSelected();
	}
}
