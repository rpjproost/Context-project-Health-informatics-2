package context.healthinformatics.GUI;

import static org.junit.Assert.assertEquals;

import java.awt.AWTException;
//import java.awt.Robot;
import java.awt.event.ActionEvent;
//import java.awt.event.KeyEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import context.healthinformatics.GUI.InputPage.ActionHandler;

/**
 * Test for the InputPage of the Interface.
 */
public class InputPageTest {

	private MainFrame mf;
	private InputPage ip;
	private ActionHandler handler;
	
	/**
	 * Create for each test a new InputPage to test with.
	 */
	@Before
	public void createFrame() {
		mf = new MainFrame();
		ip = (InputPage) mf.getInputPage();
		handler = (ActionHandler) ip.getanalyseButton().getActionListeners()[0];
	}
	
	/**
	 * Checks if the state changes when you click code tab.
	 * @throws AWTException 
	 */
	@Test
	public void testAnalyseButton() throws AWTException {
		ActionEvent e = new ActionEvent(ip.getanalyseButton(), 0, "");
		handler.actionPerformed(e);		
		assertEquals(mf.getPanelState(), mf.getCodePage());
	}
	
	/**
	 * Checks if the project is added to the folder after  using the project button.
	 * @throws AWTException 
	 */
//	@Test
//	public void testProjectButton() throws AWTException {
//		Robot r = new Robot();
//		ActionEvent e = new ActionEvent(ip.getProjectButton(), 0, "");
//		handler.actionPerformed(e);
//		r.keyPress(KeyEvent.VK_T);
//		r.keyPress(KeyEvent.VK_ENTER);
//		assertEquals(ip.getFolder().get(ip.getFolder().size() - 1), "t");
//	}
	
	/**
	 * After the test the MainFrame should be closed.
	 */
	@After
	public void closeMainFrame() {
		mf.closeFrame();
	}
}
