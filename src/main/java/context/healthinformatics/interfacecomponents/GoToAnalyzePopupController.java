package context.healthinformatics.interfacecomponents;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import context.healthinformatics.writer.ReadWriteAnalyseFilter;
import context.healthinformatics.writer.XMLDocument;

/**
 * Class to handle the popup.
 */
public class GoToAnalyzePopupController {
	private InputPageComponents ipc;
	private boolean isOpen;
	private GoToAnalyzePopup currentPopUp;
	private String projectName;

	/**
	 * Constructor of the controller.
	 * 
	 * @param ipc
	 *            get this input page components
	 */
	public GoToAnalyzePopupController(InputPageComponents ipc) {
		this.isOpen = false;
		this.ipc = ipc;

	}

	/**
	 * Create a pop up if it isn't already open.
	 * 
	 * @param selectedDocs
	 *            the selected documents
	 * @param projectName
	 *            the name of the project
	 */
	public void createPopup(ArrayList<XMLDocument> selectedDocs,
			String projectName) {
		this.projectName = projectName;
		if (!isOpen) {
			currentPopUp = new GoToAnalyzePopup(selectedDocs, this);
			ReadWriteAnalyseFilter rwaf = new ReadWriteAnalyseFilter(
					"src/main/data/savedFilters/" + projectName + ".txt");
			try {
				currentPopUp.setFilters(rwaf.readFilter());
			} catch (IOException e) {
				currentPopUp.addEmptyFilter();
			}
			setOpen(true);
		} else {
			currentPopUp.getToFront();
		}
	}

	/**
	 * Set if the popup is open or not.
	 * 
	 * @param isOpen
	 *            false if it isnt else true
	 */
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	/**
	 * Handle the filters.
	 * 
	 * @param filterValues
	 *            array of strings with the filters
	 */
	public void handleSpecifiedFilter(String[] filterValues) {
		ipc.handleSpecifiedFilter(filterValues);
		ReadWriteAnalyseFilter rwaf = new ReadWriteAnalyseFilter(
				"src/main/data/savedFilters/" + projectName + ".txt");
		try {
			rwaf.writeToFile(filterValues);
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			JOptionPane.showMessageDialog(null,
					"Something went wrong saving your filters!",
					"Save filters error", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/**
	 * Close the pop-up.
	 */
	protected void close() {
		currentPopUp.closePopUp();
	}
}
