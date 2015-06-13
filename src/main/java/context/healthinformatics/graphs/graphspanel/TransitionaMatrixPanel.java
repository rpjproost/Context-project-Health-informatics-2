package context.healthinformatics.graphs.graphspanel;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import context.healthinformatics.graphs.GraphInputContainer;

/**
 * Creates a panel specific for the stem-and-leaf plot.
 */
public class TransitionaMatrixPanel extends GraphPanel {

	private static final long serialVersionUID = 1L;
	private JCheckBox checkbox;
	private JPanel transitionMatrixPanel;
	private GraphInputContainer container;
	private int panelWidth;
	
	/**
	 * Creates a action listener for the check-box and makes a new container for this graph panel.
	 * @param checkbox the check-box that is for this panel.
	 * @param width the width for this panel.
	 */
	public TransitionaMatrixPanel(JCheckBox checkbox, int width) {
		this.checkbox = checkbox;
		panelWidth = width;
		container = new GraphInputContainer();
		transitionMatrixPanel = initGraphPanel("State-Transition Matrix");
		this.checkbox.addActionListener(this);
	}
	
	/**
	 * Initialize the graph panel for the stem-and-leaf plot.
	 */
	@Override
	public JPanel initGraphPanel(String title) {
		return makePanel(title, panelWidth, 2, container);
	}

	/**
	 * Performs an action if the state transition matrix is clicked.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == checkbox) {
			checkVisibility(checkbox, transitionMatrixPanel);
		}
	}
	
	/**
	 * @return the panel with all data.
	 */
	@Override
	public JPanel loadPanel() {
		return transitionMatrixPanel;
	}

	@Override
	public void updateContainer() {
		container.updateX(getColumnNames());
	}

	@Override
	public GraphInputContainer getGraphContainer() {
		return container;
	}

}
