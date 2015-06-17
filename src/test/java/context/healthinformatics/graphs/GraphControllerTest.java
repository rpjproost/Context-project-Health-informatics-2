package context.healthinformatics.graphs;

import java.util.ArrayList;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

import context.healthinformatics.graphs.graphspanel.GraphPanel;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * Tests the controller of the graphs with mocks.
 */
public class GraphControllerTest {
	
	private GraphInputInterface input;
	private GraphController control;
	private GraphPanel panel;

	/**
	 * Set up all mocks and the class itself.
	 */
	@Before
	public void setUp() {
		input = mock(GraphInputInterface.class);
		control = new GraphController(input);
		panel = mock(GraphPanel.class);
		ArrayList<GraphPanel> panels = new ArrayList<GraphPanel>();
		panels.add(panel);
		when(input.getGraphPanels()).thenReturn(panels);
		when(panel.getGraphPanel()).thenReturn(new JPanel());
	}

	/**
	 * Tests the update graphs if the right methods are called.
	 */
	@Test
	public void testUpdateGraphs() {
		when(panel.isSelected()).thenReturn(true);
		control.updateGraphs();
		verify(panel).plot();
		when(panel.isSelected()).thenReturn(false);
		control.updateGraphs();
		verify(panel).getGraphPanel();
	}

	/**
	 * Test get plot if the right methods are called.
	 */
	@Test
	public void testGetPlot() {
		control.getPlot(new JPanel());
		verify(input).getGraphPanels();
		verify(panel).getGraphPanel();
	}

}
