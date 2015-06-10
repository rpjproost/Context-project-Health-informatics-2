package context.healthinformatics.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.interfacecomponents.IntermediateResults;
import context.healthinformatics.parser.XMLParser;
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
	private static final int INSETS = 10;
	private static final int FIELDCORRECTION = 130;
	private MainFrame mf;
	private JButton exportFileButton;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel outputParentPanel;
	private int panelWidth;
	private int panelHeight;
	private JFileChooser c;
	private IntermediateResults imr;

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
		exportFileButton = createButton("Export File", BUTTONWIDTH, BUTTONHEIGHT);
	}

	private void initButtonActionListeners() {
		exportFileButton.addActionListener(this);
	}

	private void initAll() {
		outputParentPanel = createPanel(MainFrame.OUTPUTTABCOLOR, 
				mf.getScreenWidth(), mf.getStatePanelSize());
		outputParentPanel.setLayout(new GridBagLayout());
		leftPanel.setPreferredSize(new Dimension(mf.getScreenWidth() / 2, 
				mf.getStatePanelSize()));
		rightPanel.setPreferredSize(new Dimension(mf.getScreenWidth() / 2, 
				mf.getStatePanelSize()));
		setLeftPanel();
		setRightPanel();
	}

	private void setLeftPanel() {
		setGraphTitle();
		setGraphInputPanel();
		leftPanel.add(imr.loadPanel(), setGrids(0, 2, new Insets(0, INSETS, INSETS, 0)));
		setButtonArea(exportFileButton, leftPanel, THREE);
		outputParentPanel.add(leftPanel, setGrids(0, 0));
	}

	private void setGraphTitle() {
		GridBagConstraints c = setGrids(0, 0, new Insets(0, INSETS, 0, 0));
		c.anchor = GridBagConstraints.LINE_START;
		leftPanel.add(createTitle("Graph Input:"), c);
	}

	private void setGraphInputPanel() {
		JPanel graphInputPanel = createEmptyWithGridBagLayoutPanel(MainFrame.CODETABCOLOR);
		graphInputPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
		leftPanel.add(graphInputPanel, setGrids(0, 1, new Insets(0, INSETS, INSETS, 0)));
	}
	
	private void setButtonArea(JButton button, JPanel parent, int y) {
		JPanel buttonArea = createPanel(MainFrame.OUTPUTTABCOLOR, panelWidth, FIELDCORRECTION);
		buttonArea.add(button, setGrids(0, 0));
		parent.add(buttonArea, setGrids(0, y));
	}


	private void setRightPanel() {
//		rightPanel.add(exportGraphButton, setGrids(0, 0));
		outputParentPanel.add(rightPanel, setGrids(1, 0));
	}

	/**
	 * @return the export file button.
	 */
	public JButton getFileButton() {
		return exportFileButton;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		c = new JFileChooser();
		if (e.getSource() == getFileButton()) {
			int rVal = c.showSaveDialog(getFileButton());
			try {
				fileChooser(rVal);
			} catch (SQLException | IOException e1) {
				e1.printStackTrace();
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
			// WriteToTXT write = new WriteToTXT(c.getSelectedFile().getName(),
			// c.getCurrentDirectory().toString());
		}
	}
}
