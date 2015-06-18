package context.healthinformatics.graphs.graphspanel;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.graphs.GraphInputContainer;
import context.healthinformatics.graphs.StateTransitionMatrix;
import context.healthinformatics.gui.MainFrame;

/**
 * Creates a panel specific for the stem-and-leaf plot.
 */
public class TransitionaMatrixPanel extends GraphPanel {

	private static final long serialVersionUID = 1L;
	private JCheckBox checkbox;
	private JPanel transitionMatrixPanel;
	private GraphInputContainer container;
	private int panelWidth;
	private StateTransitionMatrix stm;
	private JPanel transitionPanel;
	
	/**
	 * Creates a action listener for the check-box and makes a new container for this graph panel.
	 * @param width the width for this panel.
	 */
	public TransitionaMatrixPanel(int width) {
		panelWidth = width;
		container = new GraphInputContainer();
		stm = new StateTransitionMatrix();
		transitionPanel = stm.getStateTransitionMatrix();
		transitionPanel.setVisible(false);
		transitionMatrixPanel = initGraphPanel("State-Transition Matrix");
		this.checkbox = createCheckBox("State-Transition Matrix", MainFrame.OUTPUTTABCOLOR);
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
		stm.fillTransitionMatrix(SingletonInterpreter.getInterpreter().getChunks());
		stm.createStateTransitionMatrix(container.getGraphTitleValue());
		transitionPanel = stm.getStateTransitionMatrix();
		transitionPanel.setVisible(true);
	}

	@Override
	public JPanel getGraphPanel() {
		return transitionPanel;
	}

}
