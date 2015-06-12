package context.healthinformatics.interfacecomponents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import context.healthinformatics.gui.InterfaceHelper;

/**
 * Create a loading screen to show the user we are processing the data.
 */
public class LoadingScreen extends InterfaceHelper {
	private JFrame loadMainFrame;
	private JPanel containerLoadPanel;
	private JEditorPane displayHtmlPane;

	private static final int LOADINGSCREEN_WIDTH = 400;
	private static final int LOADINGSCREEN_HEIGTH = 220;
	private static final int BORDER_WIDTH = 4;
	private static final int TEXTAREA_WIDTH = 392;
	private static final int TEXTAREA_HEIGTH = 120;

	/**
	 * The constructor of the loading screen.
	 */
	public LoadingScreen() {
		setupTheLoadingMainFrame();
		containerLoadPanel = createEmptyWithGridBagLayoutPanel(Color.WHITE);
		Border border = BorderFactory.createLineBorder(Color.BLACK,
				BORDER_WIDTH);
		containerLoadPanel.setBorder(border);
		initTextAreaWithLoadingMessage();

		initLoadingAnimation();

		loadMainFrame.add(containerLoadPanel);
	}

	/**
	 * Initialise the TextArea with the loading message.
	 */
	private void initTextAreaWithLoadingMessage() {
		displayHtmlPane = new JEditorPane();
		displayHtmlPane.setContentType("text/html");
		displayMessage("Processing your input files this may take a while, depending "
				+ "on the size of your input files.");
		displayHtmlPane.setEditable(false);
		JScrollPane areaScrollPane = new JScrollPane(displayHtmlPane);
		areaScrollPane.setPreferredSize(new Dimension(TEXTAREA_WIDTH,
				TEXTAREA_HEIGTH));
		areaScrollPane.setBorder(BorderFactory.createEmptyBorder());
		containerLoadPanel.add(areaScrollPane, setGrids(0, 0));
	}

	/**
	 * Initialise the loading animation.
	 */
	private void initLoadingAnimation() {
		Icon icon = new ImageIcon("src/main/data/loadingscreen/loading.gif");
		JLabel labelGif = new JLabel(icon);
		containerLoadPanel.add(labelGif, setGrids(0, 1));
	}

	/**
	 * Setup the mainframe for the loadingScreen.
	 */
	private void setupTheLoadingMainFrame() {
		loadMainFrame = new JFrame("We are loading your data to analyse");
		loadMainFrame.setUndecorated(true);
		loadMainFrame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		loadMainFrame.setPreferredSize(new Dimension(LOADINGSCREEN_WIDTH,
				LOADINGSCREEN_HEIGTH));
		loadMainFrame.pack();
		loadMainFrame.setVisible(true);
		loadMainFrame.setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		loadMainFrame.setLocation(dim.width / 2 - loadMainFrame.getSize().width
				/ 2, dim.height / 2 - loadMainFrame.getSize().height / 2);
		loadMainFrame.setVisible(true);
	}

	/**
	 * Set the textarea to display some message.
	 * 
	 * @param message
	 *            the message
	 */
	public void displayMessage(String message) {
		displayHtmlPane
				.setText("<html><body style='text-align: center; font-family: Arial; "
						+ "font-size: 12px'><h1>Please wait!</h1><br/>"
						+ message + "</body></html>");
	}

	/**
	 * Close the frame.
	 */
	public void closeLoadFrame() {
		loadMainFrame.setVisible(false);
		loadMainFrame.dispose();
	}

}
