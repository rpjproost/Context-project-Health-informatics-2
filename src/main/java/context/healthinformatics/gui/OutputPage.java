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

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.graphs.GraphController;
import context.healthinformatics.graphs.GraphInputInterface;
import context.healthinformatics.interfacecomponents.IntermediateResults;
import context.healthinformatics.writer.WriteToTXT;

/**
 * Class which represents one of the states for the variable panel in the
 * mainFrame.
 */
public class OutputPage extends InterfaceHelper implements PanelState,
		Serializable, ActionListener {

	private static final long serialVersionUID = 1L;
	private static final int BUTTONWIDTH = 200;
	private static final int BUTTONHEIGHT = 80;
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
	private JScrollPane scrollPane;

	/**
	 * Constructor.
	 */
	public OutputPage() {
		panelWidth = getScreenWidth() / 2 - 2 * INSETS;
		panelHeight = getStatePanelSize() / 2 - FIELDCORRECTION;
		imr = new IntermediateResults(panelWidth, panelHeight, "The Result:",
				MainFrame.OUTPUTTABCOLOR);
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
				getScreenWidth(), getStatePanelSize());
		panel.add(outputParentPanel);
		return panel;
	}

	/**
	 * Method which initializes the components.
	 */
	private void initComponents() {
		initButtons();
		initButtonActionListeners();
	}

	/**
	 * Method which initializes the buttons.
	 */
	private void initButtons() {
		exportFileButton = createButton("Export File", BUTTONWIDTH,
				BUTTONHEIGHT);
		updateGraphButton = createButton("Update Graphs", BUTTONWIDTH,
				BUTTONHEIGHT);
	}

	/**
	 * Method which sets the actionListeners for the buttons.
	 */
	private void initButtonActionListeners() {
		exportFileButton.addActionListener(this);
		updateGraphButton.addActionListener(this);
	}

	/**
	 * Method which initializes all the panels.
	 */
	private void initAll() {
		outputParentPanel = createPanel(MainFrame.OUTPUTTABCOLOR,
				getScreenWidth(), getStatePanelSize());
		outputParentPanel.setLayout(new GridBagLayout());
		leftPanel.setPreferredSize(new Dimension(getScreenWidth() / 2,
				getStatePanelSize()));
		rightPanel.setPreferredSize(new Dimension(getScreenWidth() / 2,
				getStatePanelSize()));
		setLeftPanel();
		setRightPanel();
	}

	/**
	 * Method which set the left panel.
	 */
	private void setLeftPanel() {
		setTitle(leftPanel, "Graph Input:");
		setGraphInputPanel();
		leftPanel.add(imr.loadPanel(),
				setGrids(0, 2, new Insets(0, INSETS, 0, 0)));
		setButtonArea(exportFileButton, leftPanel, THREE);
		outputParentPanel.add(leftPanel, setGrids(0, 0));
	}

	/**
	 * Method which sets the title of a panel.
	 * @param parent whose title will be set.
	 * @param title
	 */
	private void setTitle(JPanel parent, String title) {
		GridBagConstraints c = setGrids(0, 0, new Insets(0, INSETS, 0, 0));
		c.anchor = GridBagConstraints.LINE_START;
		parent.add(createTitle(title), c);
	}

	/**
	 * Method which sets the input panel for the graphs.
	 */
	private void setGraphInputPanel() {
		graphInputInterface = new GraphInputInterface(panelWidth, panelHeight,
				MainFrame.OUTPUTTABCOLOR);
		leftPanel.add(graphInputInterface.loadPanel(),
				setGrids(0, 1, new Insets(0, INSETS, INSETS, 0)));
		graphController = new GraphController(graphInputInterface);
	}

	/**
	 * Method which sets the button area.
	 * @param button to be set.
	 * @param parent to which the buttons should be added.
	 * @param y
	 */
	private void setButtonArea(JButton button, JPanel parent, int y) {
		JPanel buttonArea = createPanel(MainFrame.OUTPUTTABCOLOR, panelWidth,
				FIELDCORRECTION);
		buttonArea.add(button, setGrids(0, 0));
		parent.add(buttonArea, setGrids(0, y));
	}

	/**
	 * Method which sets the right panel.
	 */
	private void setRightPanel() {
		setTitle(rightPanel, "Graph(s):");
		setGraphArea();
		outputParentPanel.add(rightPanel, setGrids(1, 0));
		setButtonArea(updateGraphButton, rightPanel, 2);
	}

	/**
	 * Method which sets the area which contains the graph.
	 */
	private void setGraphArea() {
		int height = getStatePanelSize() / HUNDERTPROCENT * CORRECION
				- FIELDCORRECTION - INSETS;
		graphArea = createContainerWithGivenSizePanel(panelWidth - 2 * INSETS,
				height - INSETS);
		scrollPane = makeScrollPaneForContainerPanel(graphArea, panelWidth,
				height);
		graphArea = graphController.getPlot(graphArea);
		rightPanel.add(scrollPane,
				setGrids(0, 1, new Insets(0, INSETS, 0, INSETS)));
	}
	
	/**
	 * @param fileButton for which button the save file is.
	 * @return the choice of the user.
	 */
	private int saveFile(JButton fileButton) {
		savePopup = saveFileChooser("txt");
		return savePopup.showSaveDialog(fileButton);
	}
	
	/**
	 * @return the export file button.
	 */
	public JButton getFileButton() {
		return exportFileButton;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getFileButton()) {
			try {
				fileChooser(saveFile(getFileButton()));
			} catch (SQLException | IOException e1) {
				JOptionPane.showMessageDialog(null,
						"Something went wrong exporting your file!!",
						"Export File Error", JOptionPane.WARNING_MESSAGE);
			}
		}
		if (e.getSource() == updateGraphButton) {
				graphController.updateGraphs();
				scrollPane.revalidate();
		}
	}

	/**
	 * Decide of there must be written to a file or doing nothing.
	 * @param rVal the number of which button is chosen.
	 * @throws SQLException the sql exception of resultset
	 * @throws IOException if the parsing of the xmlDocument goes wrong.
	 */
	private void fileChooser(int rVal) throws SQLException, IOException {
		if (rVal == JFileChooser.APPROVE_OPTION) {
			WriteToTXT write = new WriteToTXT(savePopup.getSelectedFile()
					.getAbsolutePath());
			write.writeSPSSDataToFile(
					SingletonInterpreter.getInterpreter().getChunks(),
					SingletonDb.getDb());
			setLastDirectory(savePopup.getCurrentDirectory());
		}
	}
}
