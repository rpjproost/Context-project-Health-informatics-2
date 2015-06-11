package context.healthinformatics.graphs;

import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 * Container to collect all input data.
 */
public class GraphInputContainer {
	
	private JTextField graphTitle;
	private JComboBox<String> xValue;
	private JComboBox<String> yValue;
	private String[] columnsX = {"All", "Chunks"};
	private String[] columnsY = {"All", "Chunks"};
	
	/**
	 * Creates fields for the input value.
	 */
	public GraphInputContainer() {
		initFields();
	}

	private void initFields() {
		graphTitle = new JTextField();
		xValue = new JComboBox<String>(columnsX);
		yValue = new JComboBox<String>(columnsY);
	}
	
	/**
	 * @return the graphTitle
	 */
	public JTextField getGraphTitle() {
		return graphTitle;
	}

	/**
	 * @return the xValue
	 */
	public JComboBox<String> getxValue() {
		return xValue;
	}

	/**
	 * @return the yValue
	 */
	public JComboBox<String> getyValue() {
		return yValue;
	}
	
	/**
	 * @return the value of the graph title.
	 */
	public String getGraphTitleValue() {
		return graphTitle.getText();
	}
	
	/**
	 * @return the selected item of the combo box for x.
	 */
	public String getSelectedColumnX() {
		return xValue.getSelectedItem().toString();
	}
	
	/**
	 * @return the selected item of the combo box for y.
	 */
	public String getSelectedColumnY() {
		return yValue.getSelectedItem().toString();
	}
	
	/**
	 * updates the x check-box with data.
	 * @param data the new values of the box.
	 */
	public void updateX(String[] data) {
		columnsX = data.clone();
		xValue.removeAllItems();
		for (int i = 0; i < data.length; i++) {
			xValue.addItem(data[i]);
		}
	}

	/**
	 * updates the y check-box with data.
	 * @param data the new values of the box.
	 */
	public void updateY(String[] data) {
		columnsY = data.clone();
		yValue.removeAllItems();
		for (int i = 0; i < data.length; i++) {
			yValue.addItem(data[i]);
		}
	}

}
