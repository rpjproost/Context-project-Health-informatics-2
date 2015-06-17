package context.healthinformatics.graphs;

import java.awt.Color;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
import org.joda.time.DateTime;
import org.joda.time.Days;

import context.healthinformatics.analyse.Interpreter;
import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.gui.InterfaceHelper;
import context.healthinformatics.sequentialdataanalysis.Chunk;
import context.healthinformatics.sequentialdataanalysis.ComputationData;

/**
 * Class LineChart creates a line chart with data.
 */
public class LineChart extends InterfaceHelper {
	private static final long serialVersionUID = 1L;
	private int width;
	private JPanel chartContainerPanel;
	private JPanel mainPanel;
	private static final int LINECHART_PANEL_HEIGHT = 400;
	private static final int LINECHART_HEIGHT = 350;
	private XYSeriesCollection dataset;
	private ChartPanel chartPanelTest;
	private DateTime f;

	/**
	 * Constructor of the LineChart initializes the panels.
	 */
	public LineChart() {
		width = getScreenWidth() / 2 - FOUR * INSETS;
		chartPanelTest = new ChartPanel(new JFreeChart(new CategoryPlot()));
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
		mainPanel.remove(chartContainerPanel);
		chartContainerPanel.remove(chartPanelTest);
		chartContainerPanel.setPreferredSize(new Dimension(width,
				LINECHART_HEIGHT));
		chartPanelTest = new ChartPanel(chart);
		chartPanelTest.setPreferredSize(new Dimension(width, LINECHART_HEIGHT));
		chartContainerPanel.add(chartPanelTest, setGrids(0, 0));
		mainPanel.add(chartContainerPanel, setGrids(0, 0));
		mainPanel.revalidate();
	}

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
	public void initDataset() {
		dataset = new XYSeriesCollection();
		int counter = 0;
		ArrayList<HashMap<Integer, Double>> data = ComputationData.getData();
		ArrayList<String> names = ComputationData.getNames();
		if (data != null) {
			for (HashMap<Integer, Double> x : data) {
				final XYSeries s = new XYSeries(names.get(counter));
				dataset.addSeries(s);
				int size = ComputationData.getDays();
				for (int i = 0; i < size; i++) {
					if (x.get(i) == null) {
						s.add(i, 0.0);
					} else {
						s.add(i, x.get(i));
					}
				}
				counter++;
			}
		}
	}

}
