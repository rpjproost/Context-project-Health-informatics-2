package context.healthinformatics.graphs;

import java.awt.Dimension;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.gui.InterfaceHelper;
import context.healthinformatics.sequentialdataanalysis.Chunk;

/**
 * Class to create the boxplot.
 */
public class BoxPlot extends InterfaceHelper {

	private static final long serialVersionUID = 1L;
	private JPanel chartContainerPanel;
	private JPanel mainPanel;
	private static final int BOX_PLOT_PANEL_HEIGHT = 400;
	private static final int BOX_PLOT_HEIGHT = 350;
	private static final int PLOTSMEANINVISIBLE = 5;

	private int width;

	private DefaultBoxAndWhiskerCategoryDataset dataset;
	private int plotsize;
	private ChartPanel chartPanelTest;

	/**
	 * Creates a new box plot.
	 */
	public BoxPlot() {
		plotsize = 0;
		width = getScreenWidth() / 2 - FOUR * INSETS;
		chartPanelTest = new ChartPanel(new JFreeChart(new CategoryPlot()));
		chartContainerPanel = createEmptyWithGridBagLayoutPanel();
		chartContainerPanel.setPreferredSize(new Dimension(width,
				BOX_PLOT_HEIGHT));
		mainPanel = createEmptyWithGridBagLayoutPanel();
		mainPanel.setPreferredSize(new Dimension(width, BOX_PLOT_PANEL_HEIGHT));
		mainPanel.add(chartContainerPanel, setGrids(0, 0));
	}

	/**
	 * Get the mainPanel of the boxplot.
	 * 
	 * @return the panel with the boxplot
	 */
	public JPanel getPanel() {
		return mainPanel;
	}

	/**
	 * Create a BoxPlot.
	 * 
	 * @param title
	 *            the title of the BoxPlot
	 */
	public void createBoxPlot(String title) {

		final CategoryAxis xAxis = new CategoryAxis("Type");
		final NumberAxis yAxis = new NumberAxis("Value");
		yAxis.setAutoRangeIncludesZero(false);
		final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
		renderer.setFillBox(false);
		if (plotsize < PLOTSMEANINVISIBLE) {
			renderer.setMeanVisible(false);
		}
		final CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis,
				renderer);
		final JFreeChart chart = new JFreeChart("Box Plot: " + title, new Font(
				"SansSerif", Font.BOLD, TEXTSIZE), plot, true);
		mainPanel.remove(chartContainerPanel);
		chartContainerPanel.remove(chartPanelTest);
		chartContainerPanel.setPreferredSize(new Dimension(width,
				BOX_PLOT_HEIGHT));
		chartPanelTest = new ChartPanel(chart);
		chartPanelTest.setPreferredSize(new Dimension(width, BOX_PLOT_HEIGHT));
		chartContainerPanel.add(chartPanelTest, setGrids(0, 0));
		mainPanel.add(chartContainerPanel, setGrids(0, 0));
		mainPanel.revalidate();
	}

	/**
	 * Create the data set for the boxplot.
	 * 
	 * @param columns
	 *            the columns for which the box plot is made
	 * @throws Exception
	 *             exception of wrong number or no data
	 */
	public void setDataPerColumn(ArrayList<String> columns) throws Exception {

		dataset = new DefaultBoxAndWhiskerCategoryDataset();
		ArrayList<Chunk> chunks = SingletonInterpreter.getInterpreter().getChunks();
		StringBuilder buildTitle = new StringBuilder();
		if (chunks != null) {
			for (int j = 0; j < columns.size(); j++) {
				buildTitle.append(" " + columns.get(j));
				ArrayList<Double> dataList = new ArrayList<Double>();
				for (int i = 0; i < chunks.size(); i++) {
					dataList.addAll(loopThroughChunks(chunks.get(i),
							columns.get(j)));
				}
				dataset.add(dataList, columns.get(j), " Type " + j);
			}
		}
		plotsize = 1;
	}

	/**
	 * Create a dataset per chunk.
	 * 
	 * @param columns
	 *            the columns which we need the data from
	 * @throws Exception
	 *             exception of wrong number or no data
	 */
	public void setDataPerChunk(ArrayList<String> columns) throws Exception {
		dataset = new DefaultBoxAndWhiskerCategoryDataset();
		ArrayList<Chunk> chunks = SingletonInterpreter.getInterpreter().getChunks();
		StringBuilder buildTitle = new StringBuilder();
		if (chunks != null) {
			for (int j = 0; j < columns.size(); j++) {
				buildTitle.append(" " + columns.get(j));
				for (int i = 0; i < chunks.size(); i++) {
					ArrayList<Double> dataList = new ArrayList<Double>();
					dataList.addAll(loopThroughChunks(chunks.get(i),
							columns.get(j)));
					dataset.add(dataList, columns.get(j), " Type " + i);
				}
			}
			plotsize = chunks.size();
		}
	}

	/**
	 * Get the values of the specified column of all the chunks and his
	 * children.
	 * 
	 * @param currentChunk
	 *            the chunk which we are looking at.=
	 * @param column
	 *            the column of the chunk we need
	 * @return the values or values if the chunk has childs
	 * @throws Exception
	 */
	private ArrayList<Double> loopThroughChunks(Chunk currentChunk,
			String column) throws Exception {
		ArrayList<Double> listOfValues = new ArrayList<Double>();

		if (currentChunk.hasChild()) {
			ArrayList<Chunk> children = currentChunk.getChildren();
			for (int i = 0; i < children.size(); i++) {
				listOfValues.addAll(loopThroughChunks(children.get(i), column));
			}
		} else {
			Double res = getChunkData(currentChunk, column);
			if (res != Integer.MIN_VALUE) {
				listOfValues.add(res);
			}
		}
		return listOfValues;
	}

	private double getChunkData(Chunk chunk, String column) throws Exception {
		Db data = SingletonDb.getDb();
		try {
			ResultSet rs = data.selectResultSet("result", column, "resultid = "
					+ chunk.getLine());
			String value = "";
			if (rs.next()) {
				if (rs.getObject(column) != null) {
					value = rs.getObject(column).toString();
				} else {
					return Integer.MIN_VALUE;
				}
			}
			rs.close();
			return parseToDouble(value);
		} catch (SQLException | NumberFormatException e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * Parses the string from the db to a value.
	 * 
	 * @param value
	 *            the value
	 * @throws NumberFormatException
	 *             the exception if value cannot be parsed to double
	 * @return double of the string
	 */
	public Double parseToDouble(String value) throws NumberFormatException {
		Double res = Double.MIN_VALUE;
		try {
			res = Double.parseDouble(value);
		} catch (NumberFormatException e) {
			throw new NumberFormatException(
					"The selected Column is not a number and cannot be plotted!");
		}
		return res;

	}
}
