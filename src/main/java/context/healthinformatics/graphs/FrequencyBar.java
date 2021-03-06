package context.healthinformatics.graphs;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.gui.InterfaceHelper;

/**
 * Creates a frequency bar.
 */
public class FrequencyBar extends InterfaceHelper {

	private static final long serialVersionUID = 1L;

	private JPanel chartContainerPanel;
	private JPanel mainPanel;

	private static final int BOX_PLOT_PANEL_HEIGHT = 400;
	private static final int BOX_PLOT_HEIGHT = 350;

	private int width;

	/**
	 * Constructor of FrequencyBar initializes the panels.
	 */
	public FrequencyBar() {
		width = getScreenWidth() / 2 - FOUR * INSETS;
		chartContainerPanel = createEmptyWithGridBagLayoutPanel();
		chartContainerPanel.setPreferredSize(new Dimension(width,
				BOX_PLOT_HEIGHT));
		initMainPanel();
	}

	/**
	 * Initialize the main panel of the frequency bar.
	 */
	private void initMainPanel() {
		mainPanel = createEmptyWithGridBagLayoutPanel();
		mainPanel.setPreferredSize(new Dimension(width, BOX_PLOT_PANEL_HEIGHT));
		mainPanel.add(chartContainerPanel, setGrids(0, 0));
	}

	/**
	 * Get the panel with the frequency bar.
	 * 
	 * @return the panel of the frequency bar
	 */
	public JPanel getPanel() {
		return mainPanel;
	}

	/**
	 * Create dataset from the HashMap with codes and count.
	 * 
	 * @return The dataset.
	 */
	private CategoryDataset createDataset(HashMap<String, Integer> frequencies) {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (Entry<String, Integer> e : frequencies.entrySet()) {
			if (!e.getKey().equals("")) {
				dataset.addValue(e.getValue(), "Frequency", e.getKey());
			}
		}
		return dataset;
	}

	/**
	 * Create a frequency bar with data and a title.
	 * 
	 * @param title
	 *            the title of the frequency bar
	 */
	public void createFrequencyBar(String title) {
		if (SingletonInterpreter.getInterpreter().getChunks() != null) {
			JFreeChart chart = createChart(title,
					createDataset(SingletonInterpreter.getInterpreter()
							.countCodes()));
			mainPanel.remove(chartContainerPanel);
			renewChartPanelAndContainer(chart);
			mainPanel.add(chartContainerPanel, setGrids(0, 0));
			mainPanel.revalidate();
		}
	}

	/**
	 * Renew the panels of the chart.
	 * 
	 * @param chart
	 *            the chart of the Frequency Bar
	 */
	private void renewChartPanelAndContainer(JFreeChart chart) {
		chartContainerPanel = createEmptyWithGridBagLayoutPanel();
		chartContainerPanel.setPreferredSize(new Dimension(width,
				BOX_PLOT_HEIGHT));
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(width - 1,
				BOX_PLOT_HEIGHT));
		chartContainerPanel.add(chartPanel, setGrids(0, 0));
	}

	/**
	 * Creates the frequency chart.
	 * 
	 * @param dataset
	 *            the DataSet.
	 * 
	 * @return The chart.
	 */
	private JFreeChart createChart(String title, final CategoryDataset dataset) {
		final JFreeChart chart = ChartFactory.createBarChart("Frequency Bar: "
				+ title, "Codes", "Frequency", dataset,
				PlotOrientation.VERTICAL, false, true, false);
		chart.setBackgroundPaint(null);
		setLook(chart.getCategoryPlot());
		return chart;
	}

	/**
	 * Set the look of the plot.
	 * 
	 * @param plot
	 *            the plot
	 */
	private void setLook(CategoryPlot plot) {
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
		barRenderer.setBarPainter(new StandardBarPainter());
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	}
}
