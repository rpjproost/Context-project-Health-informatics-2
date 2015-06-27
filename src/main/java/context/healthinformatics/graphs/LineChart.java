package context.healthinformatics.graphs;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import context.healthinformatics.gui.InterfaceHelper;
import context.healthinformatics.sequentialdataanalysis.ComputationData;

/**
 * Class LineChart creates a line chart with data.
 */
public class LineChart extends InterfaceHelper {
	private static final long serialVersionUID = 1L;

	private JPanel chartContainerPanel;
	private JPanel mainPanel;
	private ChartPanel chartPanel;

	private static final int LINECHART_PANEL_HEIGHT = 400;
	private static final int LINECHART_HEIGHT = 350;
	private int width;

	private XYSeriesCollection dataset;

	/**
	 * Constructor of the LineChart initializes the panels.
	 */
	public LineChart() {
		width = getScreenWidth() / 2 - FOUR * INSETS;
		chartPanel = new ChartPanel(new JFreeChart(new CategoryPlot()));
		chartContainerPanel = createEmptyWithGridBagLayoutPanel();
		chartContainerPanel.setPreferredSize(new Dimension(width,
				LINECHART_HEIGHT));
		mainPanel = createEmptyWithGridBagLayoutPanel();
		mainPanel
				.setPreferredSize(new Dimension(width, LINECHART_PANEL_HEIGHT));
		mainPanel.add(chartContainerPanel, setGrids(0, 0));
	}

	/**
	 * Get the panel with the LineChart.
	 * 
	 * @return Main Panel with the LineChart
	 */
	public JPanel getPanel() {
		return mainPanel;
	}

	/**
	 * Create the line chart with data and add it to the mainPanel.
	 * 
	 * @param title
	 *            the title of the chart.
	 */
	public void createLineChart(String title) {
		initDataset();
		final JFreeChart chart = ChartFactory.createXYLineChart(title, "Days",
				"Measurements", dataset, PlotOrientation.VERTICAL, true, true,
				false);
		initChart(chart);
		addChartToMainPanel(chart);
	}

	/**
	 * Adds the chart to the container panels.
	 * 
	 * @param chart
	 *            the LineChart
	 */
	private void addChartToMainPanel(JFreeChart chart) {
		mainPanel.remove(chartContainerPanel);
		chartContainerPanel.remove(chartPanel);
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(width, LINECHART_HEIGHT));
		chartContainerPanel.add(chartPanel, setGrids(0, 0));
		mainPanel.add(chartContainerPanel, setGrids(0, 0));
		mainPanel.revalidate();
	}

	/**
	 * Initialize the chart.
	 * 
	 * @param chart
	 *            the LineChart
	 */
	private void initChart(JFreeChart chart) {
		chart.setBackgroundPaint(Color.white);
		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
	}

	/**
	 * new method for initializing data on dates.
	 */
	protected void initDataset() {
		if (ComputationData.isComputed()) {
			createDataSet();
		}
	}

	/**
	 * Create the data set.
	 */
	private void createDataSet() {
		dataset = new XYSeriesCollection();
		addValuesToDataSet(ComputationData.getData(),
				ComputationData.getNames(), 0);
	}

	/**
	 * Add values to the DataSet.
	 * 
	 * @param data
	 *            the data
	 * @param names
	 *            the names of the data
	 * @param counter
	 *            the counter which counts all the DataSets
	 */
	private void addValuesToDataSet(ArrayList<HashMap<Integer, Double>> data,
			ArrayList<String> names, int counter) {
		for (HashMap<Integer, Double> map : data) {
			final XYSeries serie = new XYSeries(names.get(counter));
			dataset.addSeries(serie);
			addValuesPerData(map, serie, ComputationData.getDays());
			counter++;
		}
	}

	/**
	 * Add the values per XYSeries.
	 * 
	 * @param map
	 *            the HashMap
	 * @param serie
	 *            the XYSeries
	 * @param size
	 *            the size of the list
	 */
	private void addValuesPerData(HashMap<Integer, Double> map, XYSeries serie,
			int size) {
		for (int i = 0; i < size; i++) {
			if (map.get(i) == null) {
				serie.add(i, 0.0);
			} else {
				serie.add(i, map.get(i));
			}
		}
	}

}
