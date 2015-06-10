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
	private String[] columns = { "Excel", "Text", "Csv" };
	//TODO get the right columns.
	
	/**
	 * Creates fields for the input value.
	 */
	public GraphInputContainer() {
		initFields();
	}

	private void initFields() {
		graphTitle = new JTextField();
		xValue = new JComboBox<String>(columns);
		yValue = new JComboBox<String>(columns);
	}
	
	/**
	 * @return the graphTitle
	 */
	protected JTextField getGraphTitle() {
		return graphTitle;
	}

	/**
	 * @return the xValue
	 */
	protected JComboBox<String> getxValue() {
		return xValue;
	}

	/**
	 * @return the yValue
	 */
	protected JComboBox<String> getyValue() {
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

}
