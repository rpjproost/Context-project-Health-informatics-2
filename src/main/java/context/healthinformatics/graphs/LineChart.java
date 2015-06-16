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
	 * Fills a lineChart object.
	 */
		public void initDataset() {
			dataset = new XYSeriesCollection();
			final XYSeries series1 = new XYSeries("First");
			dataset.addSeries(series1);
			ArrayList<Integer> data = getChunkData();
			double horizontal = 1.0;
			for (int i : data) {
				series1.add(horizontal, i);
				horizontal++;
			}
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
	 * Gets amount of childs of a chunk and puts them in an ArrayList.
	 * 
	 * @return ArrayList of integers with amount of children in a chunk.
	 */
	public ArrayList<Integer> getChunkData() {
		Interpreter interpreter = SingletonInterpreter.getInterpreter();
		ArrayList<Chunk> chunks = interpreter.getChunks();
		ArrayList<Integer> res = new ArrayList<Integer>();
		for (Chunk c : chunks) {
			res.add(c.getAmountOfChilds());
		}
		return res;
	}

	/**
	 * Create data for the x-as.
	 * 
	 * @return number of days between first and last day.
	 * @throws SQLException
	 *             if data is not found
	 */
	public int xAs() throws SQLException {
		Interpreter interpreter = SingletonInterpreter.getInterpreter();
		ArrayList<Chunk> chunks = interpreter.getChunks();
		Db data = SingletonDb.getDb();
		Date first = data.selectDate(chunks.get(0).getLine());
		Date last = data.selectDate(chunks.get(chunks.size() - 1).getLine());
		f = new DateTime(first);
		DateTime l = new DateTime(last);
		return Days.daysBetween(f, l).getDays();
	}

	/**
	 * Returns integer of how many days it differs from first.
	 * @param actual date to be checked.
	 * @return difference in integer.
	 */
	public int differsFromDate(DateTime actual) {
		return Days.daysBetween(f, actual).getDays();
	}

	/**
	 * getData for dates.
	 * @return on what date entered value in hashmap.
	 * @throws SQLException database exception.
	 */
	public HashMap<Integer, Double> getData() throws SQLException {
		Interpreter interpreter = SingletonInterpreter.getInterpreter();
		ArrayList<Chunk> chunks = interpreter.getChunks();
		HashMap<Integer, Double> res = new HashMap<Integer, Double>();
		Db data = SingletonDb.getDb();
		for (Chunk c : chunks) {
			int difference = differsFromDate(new DateTime(data.selectDate(c.getLine())));
			double value = c.getValue("value");
			res.put(difference, value);
		}
		return res;
	}

	/**
	 * new method for initializing data on dates.
	 */
	public void initDatasetOnDate() {
		try {
			dataset = new XYSeriesCollection();
			final XYSeries series1 = new XYSeries("First");
			dataset.addSeries(series1);
			HashMap<Integer, Double>  data = getData();
			for (int i = 0; i < xAs(); i++) {
				if (data.get(i) == null) {
					series1.add(i, 0.0);
				}
				else {
					series1.add(i, data.get(i));
				}
			}
		}
		catch (Exception e) {

		}
	}

}
