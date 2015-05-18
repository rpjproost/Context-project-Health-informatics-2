package context.healthinformatics.GUI;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JButton;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import context.healthinformatics.GUI.OutputPage.ActionHandler;

/**
 * Test the Output Page.
 */
public class OutputPageTest {
	
	private MainFrame mf;
	private OutputPage op;
	private ActionHandler al;


	/**
	 * Create the output page tab before each test.
	 */
	@Before
	public void setUp() {
		mf = new MainFrame();
		op = new OutputPage(mf);
		op.loadPanel();
	}
	
	/**
	 * Test if the path is given to the new writer.
	 * @throws IOException
	 * 			   the io exception of the writer.
	 * @throws SQLException
	 *             the sql exception of resultset
	 */
	@Test
	public void trytoWriteTest() throws SQLException, IOException {
		JButton button = op.getFileButton();
		al = (ActionHandler) button.getActionListeners()[0];
		al.fileChooser(0);
	}
	
	/**
	 * Test the getButton if the string is the same as we created.
	 */
	@Test
	public void getButtonTest() {
		JButton button = op.getFileButton();
		assertEquals(button.getText(), "Export File...");
	}
	
	/**
	 * When cancel is pressed nothing will happen.
	 * @throws IOException
	 * 				the io exception of the writer.
	 * @throws SQLException
	 *             the sql exception of resultset.
	 */
	@Test
	public void doNothing() throws SQLException, IOException {
		JButton button = op.getFileButton();
		al = (ActionHandler) button.getActionListeners()[0];
		al.fileChooser(-1);
	}

	/**
	 * close the frames after each test.
	 */
	@After
	public void closeMainFrame() {
		mf.closeFrame();
	}

}
