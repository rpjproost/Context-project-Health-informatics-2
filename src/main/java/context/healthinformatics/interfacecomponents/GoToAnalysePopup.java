package context.healthinformatics.interfacecomponents;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import context.healthinformatics.gui.InterfaceHelper;
import context.healthinformatics.writer.XMLDocument;

/**
 * Class go to analyse popup shows a popup to set which constraints must be
 * applied before we are going to analyse the data.
 */
public class GoToAnalysePopup extends InterfaceHelper implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final int LOADINGSCREEN_WIDTH = 600;
	private static final int LOADINGSCREEN_HEIGTH = 400;

	private JFrame popupMainFrame;
	private JPanel mainPanel;

	private ArrayList<String> docNames;
	private HashMap<String, String[]> columnNames = new HashMap<String, String[]>();
	private ArrayList<AnalysePopupRowContainer> rows = new ArrayList<AnalysePopupRowContainer>();
	private JPanel filterPanel;

	private JButton addFilter;
	private JButton goToAnalyse;
	private JButton removeFilter;
	private JPanel buttonPanel;

	private GoToAnalysePopupController popupController;

	/**
	 * Constructor of the Go to analyse popup.
	 * 
	 * @param selectedDocs
	 *            the selected xml documents
	 * @param controller
	 *            the controller which handles the popupframe
	 */
	public GoToAnalysePopup(ArrayList<XMLDocument> selectedDocs,
			GoToAnalysePopupController controller) {
		initButtons();
		setupTheLoadingMainFrame();
		this.popupController = controller;

		initDropDownMenus(selectedDocs);

		filterPanel = createEmptyWithGridBagLayoutPanel();

		mainPanel = createEmptyWithGridBagLayoutPanel();
		mainPanel.add(filterPanel, setGrids(0, 0));
		mainPanel.add(buttonPanel, setGrids(0, 1));
		popupMainFrame.add(mainPanel);
		setWindowListener();
	}

	/**
	 * Initialise the comboboxes with values of the documents and columns.
	 * 
	 * @param selectedDocs
	 *            the selected documents
	 */
	private void initDropDownMenus(ArrayList<XMLDocument> selectedDocs) {
		docNames = new ArrayList<String>();
		for (int i = 0; i < selectedDocs.size(); i++) {
			XMLDocument doc = selectedDocs.get(i);
			docNames.add(doc.getDocName());
			String[] columnNamesString = new String[doc.getColumns().size()];
			for (int j = 0; j < doc.getColumns().size(); j++) {
				columnNamesString[j] = doc.getColumns().get(j).getColumnName();
			}
			columnNames.put(docNames.get(i), columnNamesString);
		}
	}

	/**
	 * Add a WindowsListener to tell the input page components when the windows
	 * is closed.
	 */
	private void setWindowListener() {
		popupMainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				popupController.setOpen(false);
			}
		});
	}

	/**
	 * Get the mainframe of the popup to the front.
	 */
	public void getToFront() {
		popupMainFrame.toFront();
	}

	/**
	 * Initialise the buttons with action listeners.
	 */
	private void initButtons() {
		buttonPanel = createEmptyWithGridBagLayoutPanel();
		addFilter = new JButton("Add pre filter");
		goToAnalyse = new JButton("Save Settings & Go to analyse");
		removeFilter = new JButton("Remove pre filter");
		addFilter.addActionListener(this);
		goToAnalyse.addActionListener(this);
		removeFilter.addActionListener(this);
		buttonPanel.add(addFilter, setGrids(0, 0));
		buttonPanel.add(removeFilter, setGrids(1, 0));
		buttonPanel.add(goToAnalyse, setGrids(2, 0));
	}

	/**
	 * Setup the mainframe for the loadingScreen.
	 */
	private void setupTheLoadingMainFrame() {
		popupMainFrame = new JFrame("We are loading your data to analyse");
		popupMainFrame.setPreferredSize(new Dimension(LOADINGSCREEN_WIDTH,
				LOADINGSCREEN_HEIGTH));
		popupMainFrame.pack();
		popupMainFrame.setVisible(true);
		popupMainFrame.setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		popupMainFrame.setLocation(dim.width / 2
				- popupMainFrame.getSize().width / 2, dim.height / 2
				- popupMainFrame.getSize().height / 2);
		popupMainFrame.setVisible(true);
	}

	/**
	 * Close the frame.
	 */
	public void closePopUp() {
		popupMainFrame.setVisible(false);
		popupMainFrame.dispose();
	}

	/**
	 * Set all the saved filter values in the popup.
	 * 
	 * @param filters
	 *            the saved filters
	 */
	public void setFilters(ArrayList<String[]> filters) {
		int counter = 0;
		for (int i = 0; i < filters.size(); i++) {
			if (docNames.contains(filters.get(i)[0])) {
				counter++;
				AnalysePopupRowContainer popupRowContainer = new AnalysePopupRowContainer(
						docNames.toArray(new String[docNames.size()]),
						columnNames);
				popupRowContainer.setValues(filters.get(i)[0],
						filters.get(i)[1], filters.get(i)[2]);
				rows.add(popupRowContainer);
				filterPanel.add(rows.get(rows.size() - 1).getPanelOfRow(),
						setGrids(0, rows.size() - 1));
			}
		}
		if (counter == 0) {
			addEmptyFilter();
		}
		filterPanel.revalidate();
	}

	/**
	 * Add an empty filter.
	 */
	public void addEmptyFilter() {
		rows.add(new AnalysePopupRowContainer(docNames
				.toArray(new String[docNames.size()]), columnNames));
		filterPanel.add(rows.get(rows.size() - 1).getPanelOfRow(),
				setGrids(0, rows.size() - 1));
		filterPanel.revalidate();
	}

	/**
	 * Get the filter values from all rows.
	 */
	private void getAllFilterValues() {
		String[] filterValues = new String[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			filterValues[i] = rows.get(i).getValues();
		}
		closePopUp();
		popupController.setOpen(false);
		popupController.handleSpecifiedFilter(filterValues);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addFilter) {
			addEmptyFilter();
		}
		if (e.getSource() == removeFilter && rows.size() > 0) {
			JPanel panel = rows.remove(rows.size() - 1).getPanelOfRow();
			filterPanel.remove(panel);
			filterPanel.revalidate();
		}
		if (e.getSource() == goToAnalyse) {
			getAllFilterValues();

		}

	}

}
