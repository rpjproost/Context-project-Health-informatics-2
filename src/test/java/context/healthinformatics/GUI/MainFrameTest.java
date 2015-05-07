package context.healthinformatics.GUI;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MainFrameTest {
	
	MainFrame mf;
	
	@Before
	public void createFrame() {
		mf = new MainFrame();
	}

	@Test
	public void testState() {
		PanelState outputState = new OutputPage(mf);
		mf.setState(outputState);
		assertEquals(mf.getPanelState(), outputState);
	}

	@Test
	public void testGetStatePanelSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = screenSize.height;
		assertEquals(mf.getStatePanelSize(), height - mf.getTabsY());
	}

	@Test
	public void testGetTabsX() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		assertEquals(mf.getTabsX(), width/3);
	}

	@Test
	public void testGetTabsY() {
		assertEquals(mf.getTabsY(), 200);
	}
	
	@After
	public void closeMainFrame() {
		mf.closeFrame();
	}

}
