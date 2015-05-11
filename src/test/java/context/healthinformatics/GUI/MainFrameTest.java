package context.healthinformatics.GUI;

import static org.junit.Assert.assertEquals;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import context.healthinformatics.GUI.MainFrame.MouseHandler;

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
		ml = (MouseHandler) MainFrame.p1.getMouseListeners()[0];
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
		int height = screenSize.height;
		assertEquals(MainFrame.getStatePanelSize(), height - mf.getTabsY());
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
	public void checkMouseClickEventInputState()  {
		MouseEvent me = new MouseEvent(MainFrame.p1, 0, 0, 0, 0, 0, 0, false);
		ml.mouseClicked(me);
		assertEquals(mf.getPanelState(), MainFrame.inputState);
	}
	
	/**
	 * Checks if the state changes when you click code tab.
	 */
	@Test
	public void checkMouseClickEventCodePage() {
		MouseEvent me = new MouseEvent(MainFrame.p2, 0, 0, 0, 0, 0, 0, false);
		ml.mouseClicked(me);
		assertEquals(mf.getPanelState(), MainFrame.codeState);
	}
	
	/**
	 * Checks if the state changes when you click output tab.
	 */
	@Test
	public void checkMouseClickEventOutputPage() {
		MouseEvent me = new MouseEvent(MainFrame.p3, 0, 0, 0, 0, 0, 0, false);
		ml.mouseClicked(me);
		assertEquals(mf.getPanelState(), MainFrame.outputState);
	}
	
	/**
	 * Checks if the state changes when you click anywhere in the variable page.
	 */
	@Test
	public void checkMouseClickEventVariablePage() {
		MouseEvent me = new MouseEvent(MainFrame.varPanel, 0, 0, 0, 0, 0, 0, false);
		ml.mouseClicked(me);
		assertEquals(mf.getPanelState(), MainFrame.state);
	}
	
	/**
	 * After the test the MainFrame should be closed.
	 */
	@After
	public void closeMainFrame() {
		mf.closeFrame();
	}

}
