package context.healthinformatics.help;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Class to test the help functionality.
 */
public class HelpFrameTest {

	private HelpController hc;
	
	/**
	 * initialise.
	 */
	@Before
	public void init() {
		hc = new HelpController("src/test/data/helpfiles/helpTest.txt");
	}
	
	/**
	 * test if the helpFrame does not open. If it does not, the tests wont finish.
	 */
	@Test(expected = NullPointerException.class)
	public void frameOpenBooleanTest() {
		hc.setHelpFrameOpen(true);
		hc.handleHelpButton("test");
	}
	
	/**
	 * test if the helpFrame closes propery. If it does not, the tests wont finish.
	 */
	@Test
	public void closeFrameTest() {
		hc.handleHelpButton("test");
		hc.closeHelpFrame();
	}
	
	/**
	 * test if the list of containers is properly made.
	 */
	@Test
	public void containersTest() {
		hc.handleHelpButton("test");
		assertEquals(2, hc.getHelpFrame().getListOFContainers().size());
		hc.closeHelpFrame();
	}
	
	/**
	 * test the container getters.
	 */
	@Test
	public void containerGettersTest() {
		hc.handleHelpButton("test");
		assertEquals("Add Project", hc.getHelpFrame()
				.getListOFContainers().get(0).getTitle());
		assertEquals("<html>	<body>	test 	</body></html>", hc.getHelpFrame()
				.getListOFContainers().get(0).getInfo());
		hc.closeHelpFrame();
	}
	
	/**
	 * test the container setters.
	 */
	@Test
	public void containerSettersTest() {
		hc.handleHelpButton("test");
		hc.getHelpFrame().getListOFContainers().get(0).setTitle("A");
		assertEquals("A", hc.getHelpFrame()
				.getListOFContainers().get(0).getTitle());
		hc.getHelpFrame().getListOFContainers().get(0).setInfo("B");
		assertEquals("B", hc.getHelpFrame()
				.getListOFContainers().get(0).getInfo());
		hc.closeHelpFrame();
	}
	
}