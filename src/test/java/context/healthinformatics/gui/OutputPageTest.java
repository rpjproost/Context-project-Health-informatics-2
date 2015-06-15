package context.healthinformatics.gui;



import org.junit.After;
import org.junit.Before;

/**
 * Test the Output Page.
 */
public class OutputPageTest {
	
	private MainFrame mf;
	private OutputPage op;


	/**
	 * Create the output page tab before each test.
	 */
	@Before
	public void setUp() {
		mf = new MainFrame();
		op = new OutputPage();
		op.loadPanel();
	}

	/**
	 * close the frames after each test.
	 */
	@After
	public void closeMainFrame() {
		mf.closeFrame();
	}

}
