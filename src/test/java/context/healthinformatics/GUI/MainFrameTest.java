package context.healthinformatics.GUI;

import static org.junit.Assert.assertEquals;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for the MainFrame of the Interface.
 */
public class MainFrameTest {
	
	private MainFrame mf;
	
	/**
	 * Create for each test a new MainFrame to test with.
	 */
	@Before
	public void createFrame() {
		mf = new MainFrame();
	}

	/**
	 * Test when the other state is set is stored in a variable.
	 */
	@Test
	public void testState() {
//		PanelState outputState = new OutputPage(mf);
//		mf.setState(outputState);
		PanelState outputState = new OutputPage();
		MainFrame.setState(outputState);
		
		assertEquals(mf.getPanelState(), outputState);
	}

	/**
	 * The state panel height must be the same as the screen height minus the tabs height.
	 */
	@Test
	public void testGetStatePanelSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = screenSize.height;
		assertEquals(mf.getStatePanelSize(), height - mf.getTabsY());
	}

	/**
	 * The tabs width should be the screen width divided by 3.
	 */
	@Test
	public void testGetTabsX() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		assertEquals(mf.getTabsX(), width / 3);
	}

	/**
	 * The tabs height should be 200, that we divined.
	 */
	@Test
	public void testGetTabsY() {
		assertEquals(mf.getTabsY(), 200);
	}
	
	/**
	 * After the test the MainFrame should be closed.
	 */
	@After
	public void closeMainFrame() {
		mf.closeFrame();
	}

}
