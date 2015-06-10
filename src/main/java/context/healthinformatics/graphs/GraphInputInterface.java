package context.healthinformatics.graphs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import context.healthinformatics.gui.InterfaceHelper;
import context.healthinformatics.gui.MainFrame;

public class GraphInputInterface extends InterfaceHelper {

	private static final long serialVersionUID = 1L;
	private static final int CHECKBOXHEIGHT = 25;
	private static final int CHECKBOXES = 4;
	private static final int CHECKBOXESINSETS = 40;
	private int graphInputWidth;
	private int graphInputHeight;
	private JPanel graphInputParentPanel;
	private JCheckBox boxPlotCheckBox;
	private JCheckBox frequencyCheckBox;
	private JCheckBox stemAndLeafPlotCheckBox;
	private JCheckBox histogramCheckbox;

	public GraphInputInterface(int width, int height, Color color) {
		graphInputWidth = width;
		graphInputHeight = height;
		graphInputParentPanel = createEmptyWithGridBagLayoutPanel(color);
		graphInputParentPanel.setPreferredSize(new Dimension(graphInputWidth, graphInputHeight));
		initCheckBoxes();
//		initScrollPane();
		initAll();
	}

	public JPanel loadPanel() {
		return graphInputParentPanel;
	}
	
	private void initCheckBoxes() {
		boxPlotCheckBox = createCheckBox("Box Plot", MainFrame.OUTPUTTABCOLOR);
		frequencyCheckBox = createCheckBox("Frequency Bar", MainFrame.OUTPUTTABCOLOR);
		stemAndLeafPlotCheckBox = createCheckBox("Stem-and-Leaf Plot", MainFrame.OUTPUTTABCOLOR);
		histogramCheckbox = createCheckBox("Histogram", MainFrame.OUTPUTTABCOLOR);
	}
	
	private void initAll() {
		initCheckBoxesToPanel();
	}

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

}
