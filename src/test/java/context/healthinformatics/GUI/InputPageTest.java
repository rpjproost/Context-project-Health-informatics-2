package context.healthinformatics.GUI;

import static org.junit.Assert.assertEquals;

import java.awt.AWTException;
//import java.awt.Robot;
import java.awt.event.ActionEvent;
//import java.awt.event.KeyEvent;

import java.util.ArrayList;

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
		
		ArrayList<ArrayList<String>> folder = new ArrayList<ArrayList<String>>();
		folder.add(new ArrayList<String>());
		folder.get(0).add("1");
		folder.get(0).add("2");
		folder.get(0).add("3");
		folder.add(new ArrayList<String>());
		folder.get(1).add("4");
		folder.get(1).add("5");
		folder.get(1).add("6");
		ip.setFolder(folder);
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
	 * Checks if the tree is filled correctly.
	 */
	@Test
	public void testFillTree() {
		ip.fillTree();
		assertEquals(ip.getRoot().getChildCount(), 4);
	}
	
	/**
	 * Checks if the xmlList is initialised correctly.
	 */
//	@Test
//	public void testinitXml() {
//		ip.initXmlList();
//		assertEquals(ip.getXmlList().size(), ip.getFolder().size());
//	}
	
	/**
	 * Checks if the projects are read from the folder coorectly.
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testGetProjects() {
		String[] test = {"1", "4"};
		assertEquals(ip.getProjects(), test);
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
