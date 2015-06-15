package context.healthinformatics.graphs.graphspanel;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import context.healthinformatics.graphs.FrequencyBar;
import context.healthinformatics.graphs.GraphInputContainer;
import context.healthinformatics.graphs.LineChart;
import context.healthinformatics.gui.MainFrame;

public class LineChartPanel extends GraphPanel {

	private static final long serialVersionUID = 1L;
	private JCheckBox checkbox;
	private JPanel lineChartPanel;
	private GraphInputContainer container;
	private int panelWidth;
	private LineChart lineChart;
	private JPanel lineChartGraph;

	public LineChartPanel(int width) {
		panelWidth = width;
		container = new GraphInputContainer();
		lineChart = new LineChart();
		lineChartGraph = lineChart.getPanel();
		lineChartGraph.setVisible(false);
		lineChartPanel = initGraphPanel("Line Chart");
		this.checkbox = createCheckBox("Line Chart",
				MainFrame.OUTPUTTABCOLOR);
		this.checkbox.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == checkbox) {
			checkVisibility(checkbox, lineChartPanel);
		}
	}

	@Override
	public void updateContainer() {
		// TODO Auto-generated method stub

	}

	@Override
	JPanel initGraphPanel(String title) {
		return makePanel(title, panelWidth, 2, container);
	}

	@Override
	public JPanel loadPanel() {
		return lineChartPanel;
	}

	@Override
	public JCheckBox getCheckbox() {
		return checkbox;
	}

	@Override
	GraphInputContainer getGraphContainer() {
		return container;
	}

	@Override
	public boolean isSelected() {
		return checkbox.isSelected();
	}

	@Override
	public void plot() {
		lineChart.createLineChart(container.getGraphTitleValue());
		lineChartGraph = lineChart.getPanel();
		lineChartGraph.setVisible(true);
//		frequencyBar.createFrequencyBar(container.getGraphTitleValue());
//		frequencyBarGraph = frequencyBar.getPanel();
//		frequencyBarGraph.setVisible(true)

	}

	@Override
	public JPanel getGraphPanel() {
		return lineChartGraph;
	}

}
