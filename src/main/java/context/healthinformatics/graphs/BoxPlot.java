package context.healthinformatics.graphs;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.Log;
import org.jfree.util.LogContext;

import context.healthinformatics.analyse.Interpreter;
import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.gui.InterfaceHelper;
import context.healthinformatics.sequentialdataanalysis.Chunk;

/**
 * Demonstration of a box-and-whisker chart using a {@link CategoryPlot}.
 */
public class BoxPlot extends InterfaceHelper implements ActionListener {

	private ChartPanel chartPanel;
	private JPanel mainPanel;
	private static final int BOX_PLOT_WIDTH = 455;
	private static final int BOX_PLOT_HEIGHT = 600;
	private JButton boxPlotTempRefreshButton;

	private DefaultBoxAndWhiskerCategoryDataset dataset;

	/**
	 * Creates a new demo.
	 *
	 * @param title
	 *            the frame title.
	 */
	public BoxPlot(final String title) {
		boxPlotTempRefreshButton = new JButton("Refresh BoxPlotWithData");
		boxPlotTempRefreshButton.addActionListener(this);
		mainPanel = createEmptyWithGridBagLayoutPanel();
		mainPanel.setPreferredSize(new java.awt.Dimension(BOX_PLOT_WIDTH,
				BOX_PLOT_HEIGHT));
		mainPanel.add(boxPlotTempRefreshButton, setGrids(0, 1));
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
	 * Create a botplot.
	 */
	public void createBoxPlot() {

		final CategoryAxis xAxis = new CategoryAxis("Type");
		final NumberAxis yAxis = new NumberAxis("Value");
		yAxis.setAutoRangeIncludesZero(false);
		final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
		renderer.setFillBox(false);
		renderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
		final CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis,
				renderer);

		final JFreeChart chart = new JFreeChart("Box-and-Whisker Demo",
				new Font("SansSerif", Font.BOLD, 14), plot, true);
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(450, 270));
		mainPanel.add(chartPanel, setGrids(0, 0));
		mainPanel.revalidate();
	}

	/**
	 * Create the data set for the boxplot.
	 * 
	 * @param columns
	 *            the columns for which the box plot is made
	 * @throws Exception
	 *             if the dtaa of the columns is not found throw exception
	 */
	public void setData(ArrayList<String> columns) throws Exception {
		dataset = new DefaultBoxAndWhiskerCategoryDataset();
		Interpreter interpreter = SingletonInterpreter.getInterpreter();
		ArrayList<Chunk> chunks = interpreter.getChunks();

		for (int j = 0; j < columns.size(); j++) {
			ArrayList<Double> dataList = new ArrayList<Double>();
			for (int i = 0; i < chunks.size(); i++) {

				dataList.add(getChunkData(chunks.get(i), columns.get(j)));

			}
			dataset.add(dataList, columns.get(j), " Type " + j);
		}
		createBoxPlot();

	}

	private double getChunkData(Chunk chunk, String column) throws Exception {
		Db data = SingletonDb.getDb();
		try {
			ResultSet rs;
			rs = data.selectResultSet("result", column,
					"resultid = " + chunk.getLine());
			ResultSetMetaData rsmd = rs.getMetaData();
			String value = "";
			if (rs.next()) {
				value = rs.getObject(column).toString();
			}

			rs.close();
			Double res = parseToDouble(value);
			System.out.println(res);
			return res;
			// rsmd.getColumnName(i)
			// processResultSet(rsmd.getColumnCount(), rs);

		} catch (SQLException e) {
			throw new Exception("Something went wrong creating the boxplot");
		}

	}

	public Double parseToDouble(String value) {
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			System.out.println("wut");
			return null;
		}

	}

	/**
	 * Creates a sample dataset.
	 * 
	 * @return A sample dataset.
	 */
	private BoxAndWhiskerCategoryDataset createSampleDataset() {

		// number of different data series
		final int seriesCount = 3;
		// number of categories
		final int categoryCount = 4;
		// number of values added to the dataset
		final int entityCount = 22;

		DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
		for (int i = 0; i < seriesCount; i++) {

			for (int j = 0; j < categoryCount; j++) {
				// list per categorie with values
				final ArrayList<Double> list = new ArrayList<Double>();

				// add some values...
				for (int k = 0; k < entityCount; k++) {
					final double value1 = 10.0 + Math.random() * 3;
					list.add(new Double(value1));
					final double value2 = 11.25 + Math.random(); // concentrate
																	// values in
																	// the
																	// middle
					list.add(new Double(value2));
				}
				dataset.add(list, "Series " + i, " Type " + j);
			}

		}
		return dataset;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == boxPlotTempRefreshButton) {
			ArrayList<String> listOfColumns = new ArrayList<String>();
			listOfColumns.add("value");
			try {
				setData(listOfColumns);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

}
