package context.healthinformatics.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.AWTException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import context.healthinformatics.database.SingletonDb;

/**
 * Test for the InputPage of the Interface.
 */
public class InputPageTest {

	private MainFrame mf;
	private InputPage ip;
	private ActionListener handler;
	private ArrayList<ArrayList<String>> folder;
	
	public static final int THREE = 3;
	
	/**
	 * Create for each test a new InputPage to test with.
	 */
	@Before
	public void createFrame() {
		mf = new MainFrame();
		ip = (InputPage) mf.getInputPage();
		handler = (ActionListener) ip.getInputPageComponent();
		ip.getFileTree().initTree();
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("(default)");
		folder = new ArrayList<ArrayList<String>>();
		folder.add(temp);
	}
	
	/**
	 * Checks if the state changes when you click code tab.
	 * @throws AWTException 
	 */
	@Test
	public void testAnalyseButton() throws AWTException {
		ActionEvent e = new ActionEvent(ip.getInputPageComponent().getAnalyseButton(), 0, "");
		handler.actionPerformed(e);		
		assertEquals(mf.getPanelState(), mf.getCodePage());
	}
	
	/**
	 * Checks if the tree is filled correctly.
	 */
	@Test
	public void testFillTree() {
		ip.getFileTree().fillTree();
		assertEquals(ip.getFileTree().getRoot().getChildCount() / 2, 1);
	}
	
	/**
	 * Checks if the method finds the correct project.
	 */
	@Test
	public void testfindFolderProject() {
		assertEquals(ip.findFolderProject("(default)"), 0);
	}
	
	/**
	 * Testing if the database will be made.
	 */
	@Test
	public void testLoadDatabase() {
		ip.loadDatabase();
		assertNotNull(SingletonDb.getDb());
	}
	
	/**
	 * Checks if the method return -1 if the input string is not a project.
	 */
	@Test
	public void testfindFolderProjectWrong() {
		assertEquals(ip.findFolderProject("Incorrect-Input"), -1);
	}
	
	/**
	 * Checks if the a comboItem can be added correctly.
	 */
	@Test
	public void testAddComboItem() {
		ip.addComboItem("test");
		ArrayList<String> list = ip.getFolder().get(ip.getFolder().size() - 1);
		assertEquals(list.get(list.size() - 1), "test");
	}
	
	/**
	 * Checks if the projects are read from the folder correctly.
	 */
	@Test
	public void testGetProjects() {
		String[] test = {"(default)"};
		for (int i = 0; i < test.length; i++) {
			assertEquals(test[i], ip.getProjects()[i]);
		}
	}
	
	/**
	 * Checks if the file name in the TextArea is added correctly.
	 */
	@Test
	public void testAddFile() {
		ip.addFile("test");
		ArrayList<String> list = ip.getFolder().get(0);
		assertEquals(list.get(list.size() - 1), "test");
	}
	
	/**
	 * After the test the MainFrame should be closed.
	 */
	@After
	public void closeMainFrame() {
		mf.closeFrame();
	}
}
