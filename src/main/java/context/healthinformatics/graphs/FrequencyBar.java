package context.healthinformatics.graphs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.gui.InterfaceHelper;

/**
 * Creates a frequency bar.
 */
public class FrequencyBar extends InterfaceHelper {
	private JPanel chartContainerPanel;
	private JPanel mainPanel;
	private static final int BOX_PLOT_PANEL_HEIGHT = 400;
	private static final int BOX_PLOT_HEIGHT = 350;
	private int width;

	private static final double ITEM_MARGIN = 0.10;

	private static final int FONT_SIZE = 11;

	/**
	 * Constructor of FrequencyBar initialises the panels.
	 */
	public FrequencyBar() {
		width = getScreenWidth() / 2 - FOUR * INSETS;
		chartContainerPanel = createEmptyWithGridBagLayoutPanel();
		chartContainerPanel.setPreferredSize(new Dimension(width,
				BOX_PLOT_HEIGHT));
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
		// TODO WE NEED TO ADJUST THIS TO READ THE FREQUENCY OF CODES!!!!!!!!
		// HashMap<String, Double> map = new HashMap<String, Double>();
		// final double hardcodeda = 4.0;
		// final double hardcodedb = 7.0;
		// final double hardcodedc = 5.0;
		// final double hardcodedd = 10.0;
		// final double hardcodede = 12.0;
		// map.put("a", hardcodeda);
		// map.put("b", hardcodedb);
		// map.put("c", hardcodedc);
		// map.put("d", hardcodedd);
		// map.put("e", hardcodede);
		JFreeChart chart = createChart(title,
				createDataset(SingletonInterpreter.getInterpreter()
						.countCodes()));

		mainPanel.remove(chartContainerPanel);
		chartContainerPanel = createEmptyWithGridBagLayoutPanel();
		chartContainerPanel.setPreferredSize(new Dimension(width,
				BOX_PLOT_HEIGHT));
		ChartPanel chartPanelTest = new ChartPanel(chart);
		chartPanelTest.setPreferredSize(new Dimension(width - 1,
				BOX_PLOT_HEIGHT));
		chartContainerPanel.add(chartPanelTest, setGrids(0, 0));
		mainPanel.add(chartContainerPanel, setGrids(0, 0));
		mainPanel.revalidate();
	}

	/**
	 * Creates the frequency chart.
	 * 
	 * @param dataset
	 *            the dataset.
	 * 
	 * @return The chart.
	 */
	private JFreeChart createChart(String title, final CategoryDataset dataset) {
		final JFreeChart chart = ChartFactory.createBarChart("Frequency Bar: "
				+ title, "Codes", "Frequency", dataset,
				PlotOrientation.VERTICAL, false, true, false);
		chart.setBackgroundPaint(Color.white);
		final CategoryPlot plot = chart.getCategoryPlot();
		setLook(plot);
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
		plot.addRangeMarker(createIntervalMarker(), Layer.BACKGROUND);
		// set the range axis to display integers only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		setRenderer(plot);
	}

	/**
	 * Set renderer.
	 * 
	 * @param plot
	 *            the plot
	 */
	private void setRenderer(CategoryPlot plot) {
		// disable bar outlines...
		final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);
		renderer.setItemMargin(ITEM_MARGIN);
		final GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue,
				0.0f, 0.0f, Color.lightGray);
		renderer.setSeriesPaint(0, gp0);
	}

	/**
	 * Creates interval marker.
	 * 
	 * @return the interval marker
	 */
	private IntervalMarker createIntervalMarker() {
		final IntervalMarker target = new IntervalMarker(4.5, 7.5);
		target.setLabel("Target Range");
		target.setLabelFont(new Font("SansSerif", Font.ITALIC, FONT_SIZE));
		target.setLabelAnchor(RectangleAnchor.LEFT);
		target.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
		return target;
	}
}
