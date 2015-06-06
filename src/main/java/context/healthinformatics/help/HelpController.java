package context.healthinformatics.help;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import context.healthinformatics.parser.ReadHelpInfoFromTXTFile;

/**
 * Controls the help frame.
 */
public class HelpController implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean helpFrameOpen;
	private ReadHelpInfoFromTXTFile test;
	private ArrayList<HelpFrameInfoContainer> listOfHelpFrameInfo;
	private HelpFrame helpFrame;

	/**
	 * Makes a reader for the help area and sets the frame open on false.
	 * 
	 * @param path
	 *            the path to where the file is stored.
	 */
	public HelpController(String path) {
		helpFrameOpen = false;
		test = new ReadHelpInfoFromTXTFile(path);
		try {
			test.parse();
		} catch (IOException e) {
			e.printStackTrace();
		}
		listOfHelpFrameInfo = test.getHelpFrameInfoContainer();
	}

	/**
	 * Handles the pop up of the help frame. if it isn't already up it will show
	 * it.
	 * 
	 * @param title the title of the frame
	 */
	public void handleHelpButton(String title) {
		if (!helpFrameOpen) {
			setHelpFrameOpen(true);
			helpFrame = new HelpFrame(title, listOfHelpFrameInfo, this);
		} else {
			helpFrame.getToFront();
		}
	}

	/**
	 * Sets the help Open frame to a new value.
	 * 
	 * @param frameOpened
	 *            the new boolean if its open or not.
	 */
	protected void setHelpFrameOpen(boolean frameOpened) {
		helpFrameOpen = frameOpened;
	}

}
