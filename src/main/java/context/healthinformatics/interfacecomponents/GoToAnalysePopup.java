package context.healthinformatics.interfacecomponents;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

	private String[] docNames;
	private HashMap<String, String[]> columnNames = new HashMap<String, String[]>();
	private ArrayList<AnalysePopupRowContainer> rows = new ArrayList<AnalysePopupRowContainer>();
	private JPanel filterPanel;

	private JButton addFilter;
	private JButton goToAnalyse;
	private JButton removeFilter;
	private JPanel buttonPanel;
	
	private InputPageComponents ipc;

	/**
	 * Constructor of the Go to analyse popup.
	 * 
	 * @param selectedDocs
	 *            the selected xml documents
	 */
	public GoToAnalysePopup(ArrayList<XMLDocument> selectedDocs, InputPageComponents ipc) {
		initButtons();
		setupTheLoadingMainFrame();
		this.ipc = ipc;
		docNames = new String[selectedDocs.size()];
		for (int i = 0; i < selectedDocs.size(); i++) {
			XMLDocument doc = selectedDocs.get(i);
			docNames[i] = doc.getDocName();
			String[] columnNamesString = new String[doc.getColumns().size()];
			for (int j = 0; j < doc.getColumns().size(); j++) {
				columnNamesString[j] = doc.getColumns().get(j).getColumnName();
			}
			columnNames.put(docNames[i], columnNamesString);
		}
		filterPanel = createEmptyWithGridBagLayoutPanel();
		rows.add(new AnalysePopupRowContainer(docNames, columnNames));
		filterPanel.add(rows.get(0).getPanelOfRow(),
				setGrids(0, rows.size() - 1));

		mainPanel = createEmptyWithGridBagLayoutPanel();
		mainPanel.add(filterPanel, setGrids(0, 0));
		mainPanel.add(buttonPanel, setGrids(0, 1));
		popupMainFrame.add(mainPanel);
	}

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
	public void closeLoadFrame() {
		popupMainFrame.setVisible(false);
		popupMainFrame.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addFilter) {
			System.out.println("Hallo");
			rows.add(new AnalysePopupRowContainer(docNames, columnNames));
			filterPanel.add(rows.get(rows.size() - 1).getPanelOfRow(),
					setGrids(0, rows.size() - 1));
			filterPanel.revalidate();
			mainPanel.add(filterPanel, setGrids(0, 0));
		}
		if (e.getSource() == removeFilter) {
			if (rows.size() > 1) {
				JPanel panel = rows.remove(rows.size() - 1).getPanelOfRow();
				filterPanel.remove(panel);
				filterPanel.revalidate();
			}
		}
		if (e.getSource() == goToAnalyse) {
			String[] filterValues = new String[rows.size()];
			for (int i = 0; i < rows.size(); i++) {
				filterValues[i] = rows.get(i).getValues();
				System.out.println(rows.get(i).getValues());
			}
			ipc.handleSpecifiedFilter(filterValues);

		}

	}

}
