package context.healthinformatics.graphs.graphspanel;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import context.healthinformatics.graphs.GraphInputContainer;
import context.healthinformatics.gui.MainFrame;

/**
 * Creates a panel specific for the box plot.
 */
public class FrequencyBarPanel extends GraphPanel {

	private static final long serialVersionUID = 1L;
	private JCheckBox checkbox;
	private JPanel frequencyBarPanel;
	private GraphInputContainer container;
	private int panelWidth;
	
	/**
	 * Creates a action listener for the check-box and makes a new container for this graph panel.
	 * @param width the width for this panel.
	 */
	public FrequencyBarPanel(int width) {
		panelWidth = width;
		container = new GraphInputContainer();
		frequencyBarPanel = initGraphPanel("Frequency Bar");
		this.checkbox = createCheckBox("Frequency Bar", MainFrame.OUTPUTTABCOLOR);
		this.checkbox.addActionListener(this);
	}
	
	/**
	 * Initialize the graph panel for the frequency bar.
	 */
	@Override
	public JPanel initGraphPanel(String title) {
		return makePanel(title, panelWidth, 2, container);
	}

	/**
	 * Performs an action if the frequency bar is clicked.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == checkbox) {
			checkVisibility(checkbox, frequencyBarPanel);
		}
	}
	
	/**
	 * @return the panel with all data.
	 */
	@Override
	public JPanel loadPanel() {
		return frequencyBarPanel;
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
