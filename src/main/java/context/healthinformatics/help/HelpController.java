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
	private ReadHelpInfoFromTXTFile readHelpFromTXTFile;

	/**
	 * Makes a reader for the help area and sets the frame open on false.
	 * 
	 * @param path
	 *            the path to where the file is stored.
	 */
	public HelpController(String path) {
		helpFrameOpen = false;
		readHelpFromTXTFile = new ReadHelpInfoFromTXTFile(path);
	}

	/**
	 * Handles the pop up of the help frame. if it isn't already up it will show
	 * it.
	 */
	public void handleHelpButton() {
		if (!helpFrameOpen) {
			setHelpFrameOpen(true);
			try {
				readHelpFromTXTFile.parse();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ArrayList<HelpFrameInfoContainer> listOfHelpFrameInfo = readHelpFromTXTFile
					.getHelpFrameInfoContainer();
			new HelpFrame("Input Page Help", listOfHelpFrameInfo, this);
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
