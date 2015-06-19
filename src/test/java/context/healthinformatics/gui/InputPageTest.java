package context.healthinformatics.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.writer.XMLDocument;

/**
 * Test for the InputPage of the Interface.
 */
public class InputPageTest {

	private MainFrame mf;
	private InputPage ip;
	private ArrayList<ArrayList<String>> folder;
	private String testString = "TestInputPageTest";
	/**
	 * object calling the database.
	 */
	private Db data = SingletonDb.getDb();

	public static final int THREE = 3;

	/**
	 * Create for each test a new InputPage to test with.
	 */
	@Before
	public void createFrame() {
		if (data != null) {
			SingletonDb.dropAll(data);
		}
		mf = new MainFrame();
		ip = (InputPage) mf.getInputPage();
		ip.getFileTree().initTree();
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		list1.add("(default)");
		list2.add(testString);
		folder = new ArrayList<ArrayList<String>>();
		folder.add(list1);
		folder.add(list2);
		ip.getXMLController().setProject(testString);
	}
	
	/**
	 * Empty the interpreter.
	 */
	@After
	public void after() {
		try {
			SingletonDb.dropAll(data);
			SingletonInterpreter.getInterpreter().interpret("undoAll");
		} catch (Exception e) {
		}
		SingletonInterpreter.getInterpreter().setIntialChunks(null);
	}

	/**
	 * Checks if the method finds the correct project.
	 */
	@Test
	public void testfindFolderProject() {
		assertEquals(ip.findFolderProject("(default)"), 0);
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
		String[] test = { "(default)" };
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
	 * Tests if the method correctly instantiates to only a default project.
	 */
	@Test
	public void testRunClearedProject() {
		ip.runClearedProject();
		assertEquals(ip.findFolderProject("(default)"), 0);
	}

	/**
	 * Tests if an XML file is correctly added.
	 */
	@Test
	public void testAddXMLFile() {
		ip.addXmlFile("src/test/data/mergeTableFiles/twoDocs.xml");
		assertFalse(ip.getXMLController().getProjectDocuments().isEmpty());
	}

	/**
	 * Tests if an XML document is correctly made.
	 */
	@Test
	public void testmakeDocument() {
		String path = "src/test/data/mergeTableFiles/twoDocs.xml";
		XMLDocument doc = ip.makeDocument(path);
		assertEquals(doc.getPath(), path);
	}
	
	/**
	 * Test the load database method.
	 * @throws Exception could be thrown.
	 */
	@Test
	public void testLoadDatabase() throws Exception {
		ip.addXmlFile("src/test/data/mergeTableFiles/twoDocs.xml");
		ip.getXMLController().selectDocument("src/test/data/mergeTableFiles/inputTXT.txt");
		ip.getInputPageComponent().handleSpecifiedFilter(new String[0]);
		SingletonDb.dropAll(SingletonDb.getDb());
		SingletonInterpreter.getInterpreter().interpret("undoall");
		SingletonInterpreter.getInterpreter().setIntialChunks(null);
	}
	
	/**
	 * Test select pop-ups.
	 */
	@Test
	public void testSelecter() {
		JFileChooser actual = ip.getFileSelecter();
		JFileChooser expected = ip.openFileChooser();
		assertNotEquals(actual, expected);
	}

	/**
	 * After the test the MainFrame should be closed.
	 */
	@After
	public void closeMainFrame() {
		ip.removeProject(testString);
		mf.closeFrame();
	}
}