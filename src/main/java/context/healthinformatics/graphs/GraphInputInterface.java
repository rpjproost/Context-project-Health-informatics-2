package context.healthinformatics.graphs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import context.healthinformatics.gui.InterfaceHelper;
import context.healthinformatics.gui.MainFrame;

/**
 * Creates a interface to specify which data the user want to use to create a specific graph.
 */
public class GraphInputInterface extends InterfaceHelper implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final int CHECKBOXHEIGHT = 25;
	private static final int INPUTELEMENTS = 4;
	private static final int CHECKBOXESINSETS = 40;
	private static final int SUBTITLEFONT = 20;
	private int graphInputWidth;
	private int graphInputHeight;
	private JPanel graphInputParentPanel;
	private JCheckBox boxPlotCheckBox;
	private JCheckBox frequencyCheckBox;
	private JCheckBox stemAndLeafPlotCheckBox;
	private JCheckBox histogramCheckbox;
	private JPanel boxPlotPanel;
	private JPanel frequencyPanel;
	private JPanel stemAndLeafPlotPanel;
	private JPanel histogramPanel;
	private JPanel containerScrollPanel;
	private GraphInputContainer boxPlotContainer;
	private GraphInputContainer frequencyContainer;
	private GraphInputContainer stemAndLeafContainer;
	private GraphInputContainer histogramContainer;
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
		initCheckBoxListeners();
		initContainers();
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
		stemAndLeafPlotCheckBox = createCheckBox("Stem-and-Leaf Plot", MainFrame.OUTPUTTABCOLOR);
		histogramCheckbox = createCheckBox("Histogram", MainFrame.OUTPUTTABCOLOR);
	}
	
	/**
	 * Initialize for each check-box a action listener.
	 */
	private void initCheckBoxListeners() {
		boxPlotCheckBox.addActionListener(this);
		frequencyCheckBox.addActionListener(this);
		stemAndLeafPlotCheckBox.addActionListener(this);
		histogramCheckbox.addActionListener(this);
	}
	
	/**
	 * Creates for each graph type a container.
	 */
	private void initContainers() {
		boxPlotContainer = new GraphInputContainer();
		frequencyContainer = new GraphInputContainer();
		stemAndLeafContainer = new GraphInputContainer();
		histogramContainer = new GraphInputContainer();
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
		boxPlotPanel = initGraphPanel("Box Plot", boxPlotContainer);
		containerScrollPanel.add(boxPlotPanel, setGrids(0, 0));
		frequencyPanel = initGraphPanel("Frequency Bar", frequencyContainer);
		containerScrollPanel.add(frequencyPanel, setGrids(0, 1, new Insets(INSETS, 0, 0, 0)));
		stemAndLeafPlotPanel = initGraphPanel("Stem-and-Leaf Plot", stemAndLeafContainer);
		containerScrollPanel.add(stemAndLeafPlotPanel, setGrids(0, 2, new Insets(INSETS, 0, 0, 0)));
		histogramPanel = initGraphPanel("Histogram", histogramContainer);
		containerScrollPanel.add(histogramPanel, setGrids(0, THREE, new Insets(INSETS, 0, 0, 0)));
	}
	
	/**
	 * @param title the name of the graph where this panel is for.
	 * @param container the data collector of this graph.
	 * @return a general graph panel with for a title and specific data elements.
	 */
	private JPanel initGraphPanel(String title, GraphInputContainer container) {
		JPanel graphPanel = createEmptyWithGridBagLayoutPanel(Color.WHITE);
		graphPanel.setPreferredSize(new Dimension(graphInputWidth - 2 * INSETS, 
				INPUTELEMENTS * CHECKBOXHEIGHT));
		graphPanel.setVisible(false);
		graphPanel.add(createSubTitle(title), setGrids(0, 0));
		graphPanel.add(makeFormRowPanelWithTextField("Graph Title:", 
				container.getGraphTitle(), graphInputWidth - 2 * INSETS, CHECKBOXHEIGHT), 
				setGrids(0, 1));
		graphPanel.add(makeFormRowPanelWithComboBox("Select column for the x-as:", 
				container.getxValue(), graphInputWidth - 2 * INSETS, CHECKBOXHEIGHT), 
				setGrids(0, 2));
		graphPanel.add(makeFormRowPanelWithComboBox("Select column for the y-as:", 
				container.getyValue(), graphInputWidth - 2 * INSETS, CHECKBOXHEIGHT), 
				setGrids(0, THREE));

		return graphPanel;
	}
	
	/**
	 * @param titleName the name of that will be shown on the screen.
	 * @return a subtitle in italic with a size of 20.
	 */
	private JLabel createSubTitle(String titleName) {
		JLabel title = new JLabel(titleName);
		title.setFont(new Font("Arial", Font.ITALIC, SUBTITLEFONT));
		title.setPreferredSize(new Dimension(graphInputWidth - 2 * INSETS, CHECKBOXHEIGHT));
		return title;
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
		checkboxes.add(stemAndLeafPlotCheckBox, setGrids(2, 0, 
				new Insets(0, 0, 0, CHECKBOXESINSETS)));
		checkboxes.add(histogramCheckbox, setGrids(THREE, 0, 
				new Insets(0, 0, 0, CHECKBOXESINSETS)));
		graphInputParentPanel.add(checkboxes, setGrids(0, 0));
	}

	/**
	 * For each check-box it will perform an event if it is clicked.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == boxPlotCheckBox) {
			checkVisibility(boxPlotCheckBox, boxPlotPanel);
		} if (e.getSource() == frequencyCheckBox) {
			checkVisibility(frequencyCheckBox, frequencyPanel);
		} if (e.getSource() == stemAndLeafPlotCheckBox) {
			checkVisibility(stemAndLeafPlotCheckBox, stemAndLeafPlotPanel);
		} if (e.getSource() == histogramCheckbox) {
			checkVisibility(histogramCheckbox, histogramPanel);
		}
	}
	
	/**
	 * Checks for a check-box if its selected or not.
	 * On this basis it will determined whether the panel is visible or not.
	 * @param checkbox the check-box that will be checked.
	 * @param panel the panel that must be set visible or not.
	 */
	private void checkVisibility(JCheckBox checkbox, JPanel panel) { 
		if (checkbox.isSelected()) {
			panel.setVisible(true);
		} else {
			panel.setVisible(false);
		}
	}

}
