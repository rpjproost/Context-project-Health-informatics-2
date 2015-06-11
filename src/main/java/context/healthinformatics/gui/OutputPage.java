package context.healthinformatics.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import context.healthinformatics.graphs.GraphController;
import context.healthinformatics.graphs.GraphInputInterface;
import context.healthinformatics.interfacecomponents.IntermediateResults;
import context.healthinformatics.writer.WriteToTXT;

/**
 * Class which represents one of the states for the variabel panel in the
 * mainFrame.
 */
public class OutputPage extends InterfaceHelper implements PanelState,
		Serializable, ActionListener {

	private static final long serialVersionUID = 1L;
	private static final int BUTTONWIDTH = 200;
	private static final int BUTTONHEIGHT = 80;
	private static final int SELECTERHEIGHT = 500;
	private static final int SELECTERWIDTH = 600;
	private MainFrame mf;
	private IntermediateResults imr;
	private JButton exportFileButton;
	private JButton updateGraphButton;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel outputParentPanel;
	private int panelWidth;
	private int panelHeight;
	private JFileChooser savePopup;
	private GraphInputInterface graphInputInterface;
	private GraphController graphController;
	private JPanel graphArea;

	/**
	 * Constructor.
	 * 
	 * @param m
	 *            is the mainframe object
	 */
	public OutputPage(MainFrame m) {
		mf = m;
		panelWidth = mf.getScreenWidth() / 2 - 2 * INSETS;
		panelHeight = mf.getStatePanelSize() / 2 - FIELDCORRECTION;
		imr = new IntermediateResults(panelWidth, panelHeight, "The Result:",
				MainFrame.OUTPUTTABCOLOR);
		graphController = new GraphController();
		leftPanel = createEmptyWithGridBagLayoutPanel(MainFrame.OUTPUTTABCOLOR);
		rightPanel = createEmptyWithGridBagLayoutPanel(MainFrame.OUTPUTTABCOLOR);
		initComponents();
		initAll();
	}

	/**
	 * Load the panel which contains all components.
	 */
	@Override
	public JPanel loadPanel() {
		imr.updateIntermediateResult();
		graphInputInterface.update();
		JPanel panel = MainFrame.createPanel(MainFrame.OUTPUTTABCOLOR,
				mf.getScreenWidth(), mf.getStatePanelSize());
		panel.add(outputParentPanel);
		return panel;
	}

	private void initComponents() {
		initButtons();
		initButtonActionListeners();
	}

	private void initButtons() {
		exportFileButton = createButton("Export File", BUTTONWIDTH,
				BUTTONHEIGHT);
		updateGraphButton = createButton("Update Graphs", BUTTONWIDTH,
				BUTTONHEIGHT);
	}

	private void initButtonActionListeners() {
		exportFileButton.addActionListener(this);
		updateGraphButton.addActionListener(this);
	}

	private void initAll() {
		outputParentPanel = createPanel(MainFrame.OUTPUTTABCOLOR,
				mf.getScreenWidth(), mf.getStatePanelSize());
		outputParentPanel.setLayout(new GridBagLayout());
		leftPanel.setPreferredSize(new Dimension(mf.getScreenWidth() / 2, mf
				.getStatePanelSize()));
		rightPanel.setPreferredSize(new Dimension(mf.getScreenWidth() / 2, mf
				.getStatePanelSize()));
		setLeftPanel();
		setRightPanel();
	}

	private void setLeftPanel() {
		setTitle(leftPanel, "Graph Input:");
		setGraphInputPanel();
		leftPanel.add(imr.loadPanel(),
				setGrids(0, 2, new Insets(0, INSETS, 0, 0)));
		setButtonArea(exportFileButton, leftPanel, THREE);
		outputParentPanel.add(leftPanel, setGrids(0, 0));
	}

	private void setTitle(JPanel parent, String title) {
		GridBagConstraints c = setGrids(0, 0, new Insets(0, INSETS, 0, 0));
		c.anchor = GridBagConstraints.LINE_START;
		parent.add(createTitle(title), c);
	}

	private void setGraphInputPanel() {
		graphInputInterface = new GraphInputInterface(panelWidth, panelHeight,
				MainFrame.OUTPUTTABCOLOR);
		leftPanel.add(graphInputInterface.loadPanel(),
				setGrids(0, 1, new Insets(0, INSETS, INSETS, 0)));
	}

	private void setButtonArea(JButton button, JPanel parent, int y) {
		JPanel buttonArea = createPanel(MainFrame.OUTPUTTABCOLOR, panelWidth,
				FIELDCORRECTION);
		buttonArea.add(button, setGrids(0, 0));
		parent.add(buttonArea, setGrids(0, y));
	}

	private void setRightPanel() {
		setTitle(rightPanel, "Graph(s):");
		setGraphArea();
		outputParentPanel.add(rightPanel, setGrids(1, 0));
		setButtonArea(updateGraphButton, rightPanel, 2);
	}

	private void setGraphArea() {
		graphArea = createEmptyWithGridBagLayoutPanel(MainFrame.CODETABCOLOR);
		// TODO change color
		graphArea.setPreferredSize(new Dimension(panelWidth, mf
				.getStatePanelSize()
				/ HUNDERTPROCENT
				* CORRECION
				- FIELDCORRECTION - INSETS));
		rightPanel.add(graphArea,
				setGrids(0, 1, new Insets(0, INSETS, 0, INSETS)));
	}

	/**
	 * @return the export file button.
	 */
	public JButton getFileButton() {
		return exportFileButton;
	}

	/**
	 * @param button
	 *            the button where the pop up is for.
	 * @param filter
	 *            which file type will be saved.
	 * @return the anwser of the filechooser.
	 */
	public int saveFileChooser(JButton button, String filter) {
		savePopup = new JFileChooser();
		savePopup.setMultiSelectionEnabled(true);
		String filtername = "save as *." + filter;
		FileNameExtensionFilter extenstionFilter = new FileNameExtensionFilter(
				filtername, filter);
		savePopup.setFileFilter(extenstionFilter);
		savePopup
				.setPreferredSize(new Dimension(SELECTERWIDTH, SELECTERHEIGHT));
		savePopup.setFileSelectionMode(JFileChooser.FILES_ONLY);
		Action details = savePopup.getActionMap().get("viewTypeDetails");
		details.actionPerformed(null);
		return savePopup.showSaveDialog(button);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getFileButton()) {
			saveFileChooser(getFileButton(), "txt");
		}
		if (e.getSource() == updateGraphButton) {
			try {
				graphController.updateGraphs(graphInputInterface);
				graphArea.add(graphController.getBoxPlot(), setGrids(0, 0));
				rightPanel.revalidate();
			} catch (NullPointerException excep) {
				JOptionPane.showMessageDialog(null,
						"Your input fields aren't filled in correctly!!",
						"Graph Error", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	/**
	 * Decide of there must be written to a file or doing nothing.
	 * 
	 * @param rVal
	 *            the number of which button is chosen.
	 * @throws SQLException
	 *             the sql exception of resultset
	 * @throws IOException
	 *             if the parsing of the xmlDocument goes wrong.
	 */
	public void fileChooser(int rVal) throws SQLException, IOException {
		if (rVal == JFileChooser.APPROVE_OPTION) {
			WriteToTXT write = new WriteToTXT(savePopup.getSelectedFile()
					.getName(), savePopup.getCurrentDirectory().toString());
			write.toString(); // TODO
		}
	}
}
