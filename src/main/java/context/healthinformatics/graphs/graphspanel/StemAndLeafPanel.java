package context.healthinformatics.graphs.graphspanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import context.healthinformatics.graphs.GraphInputContainer;
import context.healthinformatics.gui.InterfaceHelper;

/**
 * Creates a panel specific for the stem-and-leaf plot.
 */
public class StemAndLeafPanel extends InterfaceHelper implements GraphPanel {

	private static final long serialVersionUID = 1L;
	private JCheckBox checkbox;
	private JPanel stemAndLeafPanel;
	private GraphInputContainer container;
	private int panelWidth;
	
	/**
	 * Creates a action listener for the check-box and makes a new container for this graph panel.
	 * @param checkbox the check-box that is for this panel.
	 * @param width the width for this panel.
	 */
	public StemAndLeafPanel(JCheckBox checkbox, int width) {
		this.checkbox = checkbox;
		panelWidth = width;
		container = new GraphInputContainer();
		stemAndLeafPanel = initGraphPanel("Stem-and-Leaf Plot");
		this.checkbox.addActionListener(this);
	}
	
	/**
	 * Initialize the graph panel for the stem-and-leaf plot.
	 */
	@Override
	public JPanel initGraphPanel(String title) {
		JPanel graphPanel = createEmptyWithGridBagLayoutPanel(Color.WHITE);
		graphPanel.setPreferredSize(new Dimension(panelWidth - 2 * INSETS, 
				THREE * CHECKBOXHEIGHT));
		graphPanel.setVisible(false);
		graphPanel.add(createSubTitle(title, panelWidth, CHECKBOXHEIGHT), setGrids(0, 0));
		graphPanel.add(makeFormRowPanelWithTextField("Graph Title:", 
				container.getGraphTitle(), panelWidth - 2 * INSETS, CHECKBOXHEIGHT), 
				setGrids(0, 1));
		graphPanel.add(makeFormRowPanelWithComboBox("Select a column for stem-and-leaf plot:", 
				container.getxValue(), panelWidth - 2 * INSETS, CHECKBOXHEIGHT), 
				setGrids(0, 2));
		return graphPanel;
	}

	/**
	 * Performs an action if the stem-and-leaf check-box is clicked.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == checkbox) {
			checkVisibility(checkbox, stemAndLeafPanel);
			System.out.println("check: " + checkbox.isSelected());
			System.out.println(stemAndLeafPanel.isVisible());
		}
	}
	
	/**
	 * @return the panel with all data.
	 */
	@Override
	public JPanel loadPanel() {
		return stemAndLeafPanel;
	}

}
