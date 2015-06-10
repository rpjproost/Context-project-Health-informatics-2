package context.healthinformatics.graphs;

import java.awt.Font;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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
import context.healthinformatics.sequentialdataanalysis.Chunk;

/**
 * Demonstration of a box-and-whisker chart using a {@link CategoryPlot}.
 */
public class BoxPlot {

	private Interpreter interpreter = SingletonInterpreter.getInterpreter();
	private ChartPanel chartPanel;

	/**
	 * Creates a new demo.
	 *
	 * @param title
	 *            the frame title.
	 */
	public BoxPlot(final String title) {

		final BoxAndWhiskerCategoryDataset dataset = createSampleDataset();

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
	}

	public ChartPanel getPanel() {
		return chartPanel;
	}

	public void setData(ArrayList<String> columns) throws Exception {
		int line;
		ArrayList<Chunk> chunks = interpreter.getChunks();
		ArrayList<Double> dataList = new ArrayList<Double>();
		for (int i = 0; i < chunks.size(); i++) {
			for (int j = 0; j < columns.size(); j++) {
				dataList.add(getChunkData(chunks.get(i), columns.get(i)));
			}
		}

	}

	private double getChunkData(Chunk chunk,String column) throws Exception{
		Db data = SingletonDb.getDb();
		try {
			ResultSet rs;
			rs = data.selectResultSet("result", column, "resultid = " + chunk.getLine());
			
			System.out.println(rs.getObject(column));
			rs.close();
			return 2.0;
//			rsmd.getColumnName(i)
//			processResultSet(rsmd.getColumnCount(), rs);
			
		
		} catch (SQLException e) {
			throw new Exception("Something went wrong creating the boxplot");
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

}
