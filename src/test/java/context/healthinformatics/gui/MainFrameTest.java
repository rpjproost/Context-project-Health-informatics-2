package context.healthinformatics.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import context.healthinformatics.gui.MainFrame.MouseHandler;
import context.healthinformatics.interfacecomponents.LoadingScreen;

/**
 * Test for the MainFrame of the Interface.
 */
public class MainFrameTest {

	private MainFrame mf;
	private Dimension screenSize;
	private MouseHandler ml;
	private static final int TABS = 3;
	private static final int TABSHEIGHT = 120;

	/**
	 * Create for each test a new MainFrame to test with.
	 */
	@Before
	public void createFrame() {
		mf = new MainFrame();
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		ml = (MouseHandler) mf.getInputTab().getMouseListeners()[0];
	}

	/**
	 * Test when the other state is set is stored in a variable.
	 */
	@Test
	public void testState() {
		PanelState outputState = new OutputPage();
		mf.setState(outputState);

		assertEquals(mf.getPanelState(), outputState);
	}

	/**
	 * The state panel height must be the same as the screen height minus the
	 * tabs height.
	 */
	@Test
	public void testGetStatePanelSize() {
		int height = screenSize.height;
		assertEquals(mf.getStatePanelSize(), height - mf.getTabsY());
	}

	/**
	 * The tabs width should be the screen width divided by 3.
	 */
	@Test
	public void testGetTabsX() {
		int width = screenSize.width;
		assertEquals(mf.getTabsX(), width / TABS);
	}

	/**
	 * The tabs height should be 200, that we divined.
	 */
	@Test
	public void testGetTabsY() {
		assertEquals(mf.getTabsY(), TABSHEIGHT);
	}

	/**
	 * Checks if the state changes when you click the input tab.
	 */
	@Test
	public void checkMouseClickEventInputState() {
		try {
			MouseEvent me = new MouseEvent(mf.getInputTab(), 0, 0, 0, 0, 0, 0,
					false);
			ml.mouseClicked(me);
			assertEquals(mf.getPanelState(), mf.getInputPage());
		} catch (ClassCastException e) {
			fail();
		}
	}

	/**
	 * Checks if the state changes when you click code tab.
	 */
	@Test
	public void checkMouseClickEventCodePage() {
		try {
			MouseEvent me = new MouseEvent(mf.getCodeTab(), 0, 0, 0, 0, 0, 0,
					false);
			ml.mousePressed(me);
			assertEquals(mf.getPanelState(), mf.getCodePage());
		} catch (ClassCastException e) {
			fail();
		}
	}

	/**
	 * Checks if the state changes when you click output tab.
	 */
	@Test
	public void checkMouseClickEventOutputPage() {
		try {
			MouseEvent me = new MouseEvent(mf.getOutputTab(), 0, 0, 0, 0, 0, 0,
					false);
			ml.mousePressed(me);
			assertEquals(mf.getPanelState(), mf.getOutputPage());
		} catch (ClassCastException e) {
			fail();
		}
	}

	/**
	 * Tests the loading screen.
	 */
	@Test
	public void testLoadingScreen() {
		LoadingScreen ls = new LoadingScreen();
		ls.displayMessage("Test");
		ls.closeLoadFrame();
	}

	/**
	 * After the test the MainFrame should be closed.
	 */
	@After
	public void closeMainFrame() {
		mf.closeFrame();
	}

}
