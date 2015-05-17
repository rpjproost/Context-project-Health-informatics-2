package context.healthinformatics.GUI;

import static org.junit.Assert.assertEquals;

import java.awt.AWTException;
import java.awt.event.ActionEvent;

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
	 * After the test the MainFrame should be closed.
	 */
	@After
	public void closeMainFrame() {
		mf.closeFrame();
	}
}
