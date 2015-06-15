package context.healthinformatics.graphs;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import context.healthinformatics.analyse.Interpreter;
import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.gui.InterfaceHelper;
import context.healthinformatics.sequentialdataanalysis.Chunk;

public class LineChart extends InterfaceHelper {

	private int width;
	private JPanel chartContainerPanel;
	private JPanel mainPanel;
	private static final int LINECHART_PANEL_HEIGHT = 400;
	private static final int LINECHART_HEIGHT = 350;
	private XYSeriesCollection dataset;
	private ChartPanel chartPanelTest;

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

	public JPanel getPanel() {
		return mainPanel;
	}

	public void initDataset() {
		dataset = new XYSeriesCollection();
		final XYSeries series1 = new XYSeries("First");
		dataset.addSeries(series1);
	}

	public void createBoxPlot(String title) {
		final JFreeChart chart = ChartFactory.createXYLineChart(
				title, // chart title
				"X", // x axis label
				"Y", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		chart.setBackgroundPaint(Color.white);

		// final StandardLegend legend = (StandardLegend) chart.getLegend();
		// legend.setDisplaySeriesShapes(true);

		// get a reference to the plot for further customisation...
		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesLinesVisible(0, false);
		renderer.setSeriesShapesVisible(1, false);
		plot.setRenderer(renderer);

		// change the auto tick unit selection to integer units only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// OPTIONAL CUSTOMISATION COMPLETED.
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

	public ArrayList<Integer> getChunkData() {
		Interpreter interpreter = SingletonInterpreter.getInterpreter();
		ArrayList<Chunk> chunks = interpreter.getChunks();
		ArrayList<Integer> res = new ArrayList<Integer>();
		for (Chunk c : chunks) {
			res.add(c.getAmountOfChilds());
		}
		return res;
	}

}
