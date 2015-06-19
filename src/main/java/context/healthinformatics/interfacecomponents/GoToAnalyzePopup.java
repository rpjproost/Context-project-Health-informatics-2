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
import javax.swing.JRootPane;
import javax.swing.JScrollPane;

import context.healthinformatics.gui.InterfaceHelper;
import context.healthinformatics.writer.XMLDocument;

/**
 * Class go to analyze PopUp shows a PopUp to set which constraints must be
 * applied before we are going to analyze the data.
 */
public class GoToAnalyzePopup extends InterfaceHelper implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final int FILTERSCREEN_WIDTH = 400;
	private static final int FILTERSCREEN_HEIGTH = 220;
	private static final int ELEMENT_WIDTH = 390;
	private static final int FILTER_PANEL_HEIGHT = 120;
	private static final int BUTTON_PANEL_HEIGHT = 50;

	private JFrame popupMainFrame;
	private JPanel mainPanel;

	private ArrayList<String> docNames;
	private HashMap<String, String[]> columnNames = new HashMap<String, String[]>();
	private ArrayList<AnalyzePopupRowContainer> rows = new ArrayList<AnalyzePopupRowContainer>();
	private JPanel filterPanel;

	private JButton addFilter;
	private JButton goToAnalyse;
	private JButton removeFilter;
	private JPanel buttonPanel;

	private JScrollPane scroll;

	private GoToAnalyzePopupController popupController;

	/**
	 * Constructor of the Go to analyze PopUp.
	 * 
	 * @param selectedDocs
	 *            the selected XML documents
	 * @param controller
	 *            the controller which handles the PopUp frame
	 */
	public GoToAnalyzePopup(ArrayList<XMLDocument> selectedDocs,
			GoToAnalyzePopupController controller) {
		initButtons();
		setupTheLoadingMainFrame();
		this.popupController = controller;
		initDropDownMenus(selectedDocs);
		initializePopupPanels();
		setWindowListener();
	}

	/**
	 * Initialize the panels for the analyze PopUp row.
	 */
	private void initializePopupPanels() {
		filterPanel = createEmptyWithGridBagLayoutPanel();
		scroll = new JScrollPane(filterPanel);
		scroll.setPreferredSize(new Dimension(ELEMENT_WIDTH,
				FILTER_PANEL_HEIGHT));
		mainPanel = createEmptyWithGridBagLayoutPanel();
		mainPanel.add(scroll, setGrids(0, 0));
		mainPanel.add(buttonPanel, setGrids(0, 1));
		popupMainFrame.add(mainPanel);
	}

	/**
	 * Initialize the ComboBoxes with values of the documents and columns.
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
	 * Get the mainframe of the PopUp to the front.
	 */
	public void getToFront() {
		popupMainFrame.toFront();
	}

	/**
	 * Initialize the buttons with action listeners.
	 */
	private void initButtons() {
		buttonPanel = createEmptyWithGridBagLayoutPanel();
		buttonPanel.setPreferredSize(new Dimension(ELEMENT_WIDTH,
				BUTTON_PANEL_HEIGHT));
		addFilter = new JButton("Add filter");
		goToAnalyse = new JButton("Go to analyse");
		removeFilter = new JButton("Remove filter");
		addActionListenersToButtons();
		addButtonsToButtonPanel();
	}

	/**
	 * Add the ActionListeners to the buttons.
	 */
	private void addActionListenersToButtons() {
		addFilter.addActionListener(this);
		goToAnalyse.addActionListener(this);
		removeFilter.addActionListener(this);
	}

	/**
	 * Add the buttons to the ButtonPanel.
	 */
	private void addButtonsToButtonPanel() {
		buttonPanel.add(addFilter, setGrids(0, 0));
		buttonPanel.add(removeFilter, setGrids(1, 0));
		buttonPanel.add(goToAnalyse, setGrids(2, 0));
	}

	/**
	 * Setup the mainframe for the loadingScreen.
	 */
	private void setupTheLoadingMainFrame() {
		popupMainFrame = new JFrame("Adjust pre filters");
		popupMainFrame.setPreferredSize(new Dimension(FILTERSCREEN_WIDTH,
				FILTERSCREEN_HEIGTH));
		popupMainFrame.pack();
		setLoadingScreenSettings();
	}

	/**
	 * Set specific settings for the MainFrame.
	 */
	private void setLoadingScreenSettings() {
		popupMainFrame.setVisible(true);
		popupMainFrame.setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		popupMainFrame.setLocation(dim.width / 2
				- popupMainFrame.getSize().width / 2, dim.height / 2
				- popupMainFrame.getSize().height / 2);
		popupMainFrame.setVisible(true);
		popupMainFrame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
	}

	/**
	 * Close the frame.
	 */
	public void closePopUp() {
		popupMainFrame.setVisible(false);
		popupMainFrame.dispose();
	}

	/**
	 * Set all the saved filter values in the PopUp.
	 * 
	 * @param filters
	 *            the saved filters
	 */
	public void setFilters(ArrayList<String[]> filters) {
		for (int i = 0; i < filters.size(); i++) {
			if (docNames.contains(filters.get(i)[0])) {
				addFilter(filters.get(i));
			}
		}
		filterPanel.revalidate();
	}

	/**
	 * Add the current filter.
	 * 
	 * @param filter
	 *            the filters
	 */
	private void addFilter(String[] filter) {
		AnalyzePopupRowContainer popupRowContainer = new AnalyzePopupRowContainer(
				docNames.toArray(new String[docNames.size()]), columnNames);
		popupRowContainer.setValues(filter[0], filter[1], filter[2]);
		rows.add(popupRowContainer);
		filterPanel.add(rows.get(rows.size() - 1).getPanelOfRow(),
				setGrids(0, rows.size() - 1));
	}

	/**
	 * Add an empty filter.
	 */
	public void addEmptyFilter() {
		rows.add(new AnalyzePopupRowContainer(docNames
				.toArray(new String[docNames.size()]), columnNames));
		filterPanel.add(rows.get(rows.size() - 1).getPanelOfRow(),
				setGrids(0, rows.size() - 1));
		filterPanel.revalidate();
	}

	/**
	 * Get the filter values from all rows.
	 * 
	 * @return the filter values.
	 */
	protected String[] getAllFilterValues() {
		String[] filterValues = new String[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			filterValues[i] = rows.get(i).getValues();
		}
		closePopUp();
		popupController.setOpen(false);
		return filterValues;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addFilter) {
			addEmptyFilter();
		}
		if (e.getSource() == removeFilter && rows.size() > 0) {
			handleRemoveFilter();
		}
		if (e.getSource() == goToAnalyse) {
			popupController.handleSpecifiedFilter(getAllFilterValues());
		}
	}

	/**
	 * Handle the remove filter button.
	 */
	private void handleRemoveFilter() {
		JPanel panel = rows.remove(rows.size() - 1).getPanelOfRow();
		panel.setVisible(false);
		filterPanel.remove(panel);
		scroll.revalidate();
	}

}
