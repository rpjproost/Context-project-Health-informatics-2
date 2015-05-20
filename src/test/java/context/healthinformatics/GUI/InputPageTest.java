package context.healthinformatics.GUI;

import static org.junit.Assert.assertEquals;

import java.awt.AWTException;
import java.awt.event.ActionEvent;
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
	private ArrayList<ArrayList<String>> folder;
	
	public static final int THREE = 3;
	
	/**
	 * Create for each test a new InputPage to test with.
	 */
	@Before
	public void createFrame() {
		mf = new MainFrame();
		ip = (InputPage) mf.getInputPage();
		handler = (ActionHandler) ip.getanalyseButton().getActionListeners()[0];
		
		folder = new ArrayList<ArrayList<String>>();
		folder.add(new ArrayList<String>());
		folder.get(0).add("1");
		folder.add(new ArrayList<String>());
		folder.get(1).add("2");
		folder.get(1).add("3");
		folder.get(1).add("4");
		folder.add(new ArrayList<String>());
		folder.get(2).add("5");
		folder.get(2).add("6");
		ip.setFolder(folder);
		ip.initXmlList();
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
		assertEquals(ip.getRoot().getChildCount(), THREE);
	}
	
	/**
	 * Checks if the folder getter works correctly.
	 */
	@Test
	public void testgetFolder() {
		assertEquals(ip.getFolder(), folder);
	}
	
	/**
	 * Checks if the node selection is intitialised as empty.
	 */
	@Test
	public void testgetSelectedNodesisEmpty() {
		assertEquals(ip.getSelectedFiles().isEmpty(), true);
	}
	
	/**
	 * Checks if the method finds the correct project.
	 */
	@Test
	public void testfindFolderProject() {
		assertEquals(ip.findFolderProject("5"), 2);
	}
	
	/**
	 * Checks if the method return -1 if the input string is not a project.
	 */
	@Test
	public void testfindFolderProjectWrong() {
		assertEquals(ip.findFolderProject("Incorrect-Input"), -1);
	}
	
	/**
	 * Checks if the method reloads the tree model correctly.
	 */
	@Test
	public void testReloadTree() {
		ArrayList<ArrayList<String>> list = ip.getFolder();
		ArrayList<String> project = list.get(0);
		project.add("test");
		list.set(0, project);
		ip.setFolder(list);
		ip.reloadTree();
		assertEquals(ip.getModel().getChild(ip.getRoot().getFirstChild(), 0).toString(), "test");
	}
	
	/**
	 * Checks if the xmlList is initialised correctly.
	 */
	@Test
	public void testinitXml() {
		assertEquals(ip.getXmlList().size(), ip.getFolder().size());
	}
	
	/**
	 * Checks if the projects are read from the folder correctly.
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testGetProjects() {
		String[] test = {"1", "2", "5"};
		assertEquals(ip.getProjects(), test);
	}
	
	/**
	 * After the test the MainFrame should be closed.
	 */
	@After
	public void closeMainFrame() {
		mf.closeFrame();
	}
}
