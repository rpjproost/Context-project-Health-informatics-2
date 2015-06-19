package context.healthinformatics.graphs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.gui.InterfaceHelper;
import context.healthinformatics.sequentialdataanalysis.Chunk;
import context.healthinformatics.writer.ScreenImage;

/**
 * The state transition matrix class.
 */
public class StateTransitionMatrix extends InterfaceHelper {
	private static final long serialVersionUID = 1L;
	private static final int ROW_WIDTH = 75;
	private static final int HEIGHT = 28;
	private static final int MINUSTWO = -2;
	private static final int STATE_TRANSITION_PANEL_HEIGHT = 400;
	private static final int STATE_TRANSITION_TABLE_HEIGHT = 350;
	private int width;

	private ArrayList<String> codes;
	private HashMap<ConnectionSet, Integer> countMap;

	private JTable table;
	private JScrollPane scroll;
	private JPanel mainPanel;
	private JPanel tableContainerPanel;
	private JTable headerTable;
	private JLabel graphTitle;
	private JFileChooser savePopup;
	private JPopupMenu menu = new JPopupMenu("Popup");

	/**
	 * The constructor of the transition matrix.
	 */
	public StateTransitionMatrix() {
		width = getScreenWidth() / 2 - FOUR * INSETS;
		codes = new ArrayList<String>();
		countMap = new HashMap<ConnectionSet, Integer>();
		initMatrixPanels();
		addItemsToPopUp();
	}

	/**
	 * Initialize the panels of the StateTransitionMatrix.
	 */
	private void initMatrixPanels() {
		tableContainerPanel = createEmptyWithGridBagLayoutPanel();
		tableContainerPanel.setPreferredSize(new Dimension(width,
				STATE_TRANSITION_TABLE_HEIGHT));
		mainPanel = createEmptyWithGridBagLayoutPanel();
		mainPanel.setPreferredSize(new Dimension(width,
				STATE_TRANSITION_PANEL_HEIGHT));
		mainPanel.add(tableContainerPanel, setGrids(0, 0));
	}

	/**
	 * Initialize the table.
	 * 
	 * @param title
	 *            the title of the matrix.
	 */
	public void createStateTransitionMatrix(String title) {
		mainPanel.remove(tableContainerPanel);
		reinitStateTransitionMatrix(title);
		reinitHeaderTable();
		scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(width, codes.size() * HEIGHT));
		scroll.setRowHeaderView(headerTable);
		tableContainerPanel.add(scroll, setGrids(0, 1));
		mainPanel.add(tableContainerPanel, setGrids(0, 0));
		mainPanel.revalidate();
		addMouseListeners();
	}

	/**
	 * Reinitialize all the panels for the StateTransitionMatrix.
	 * 
	 * @param title
	 *            the title of the matrix
	 */
	private void reinitStateTransitionMatrix(String title) {
		tableContainerPanel = createEmptyWithGridBagLayoutPanel();
		tableContainerPanel.setPreferredSize(new Dimension(width,
				STATE_TRANSITION_TABLE_HEIGHT));
		graphTitle = new JLabel("State-Transition Matrix: " + title);
		graphTitle.setFont(new Font("SansSerif", Font.BOLD, TEXTSIZE));
		tableContainerPanel.add(graphTitle, setGrids(0, 0));
		table = new JTable(getTableData(), getColumnNames());
	}

	/**
	 * Reinitialize the HeaderTable.
	 */
	private void reinitHeaderTable() {
		headerTable = new JTable(createModel());
		for (int i = 0; i < table.getRowCount(); i++) {
			headerTable.setValueAt(codes.get(i), i, 0);
		}
		headerTable.setShowGrid(false);
		headerTable.setPreferredScrollableViewportSize(new Dimension(ROW_WIDTH,
				0));
		headerTable.getColumnModel().getColumn(0).setPreferredWidth(ROW_WIDTH);
		headerTable.getColumnModel().getColumn(0)
				.setCellRenderer(createTableCellRenderer());
	}

	/**
	 * Add the MouseListeners.
	 */
	private void addMouseListeners() {
		mainPanel.addMouseListener(new PopupTriggerListener(menu));
		scroll.addMouseListener(new PopupTriggerListener(menu));
		headerTable.addMouseListener(new PopupTriggerListener(menu));
		table.addMouseListener(new PopupTriggerListener(menu));
		graphTitle.addMouseListener(new PopupTriggerListener(menu));
	}

	/**
	 * Add the items to PopUp.
	 */
	private void addItemsToPopUp() {
		JMenuItem item = new JMenuItem("Save as...");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				savePopup = saveFileChooser("png");
				saveImage(savePopup, savePopup.showSaveDialog(table));
			}
		});
		menu.add(item);
	}

	/**
	 * Saves the matrix in a PNG file.
	 * 
	 * @param chooser
	 *            the chooser where you must check.
	 * @param choice
	 *            the choice of the user.
	 */
	public void saveImage(JFileChooser chooser, int choice) {
		if (choice == JFileChooser.APPROVE_OPTION) {
			BufferedImage bi = ScreenImage.createImage(mainPanel);
			try {
				ScreenImage.writeImage(bi, chooser.getSelectedFile()
						.getAbsolutePath() + ".png");
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null,
						"Something went wrong exporting your Graph!!",
						"Export Graph Error", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	/**
	 * @return a new table model.
	 */
	private TableModel createModel() {
		return new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public int getColumnCount() {
				return 1;
			}

			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}

			@Override
			public int getRowCount() {
				return table.getRowCount();
			}

			@Override
			public Class<?> getColumnClass(int colNum) {
				switch (colNum) {
				case 0:
					return String.class;
				default:
					return super.getColumnClass(colNum);
				}
			}
		};
	}

	/**
	 * @return a TableCellRenderer.
	 */
	private TableCellRenderer createTableCellRenderer() {
		return new TableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable x,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				Component component = table
						.getTableHeader()
						.getDefaultRenderer()
						.getTableCellRendererComponent(table, value, false,
								false, -1, MINUSTWO);
				((JLabel) component)
						.setHorizontalAlignment(SwingConstants.CENTER);
				component.setFont(component.getFont().deriveFont(Font.PLAIN));
				return component;
			}
		};
	}

	/**
	 * @return get the panel with the matrix.
	 */
	public JPanel getStateTransitionMatrix() {
		return mainPanel;
	}

	/**
	 * @return a string array with column names.
	 */
	public String[] getColumnNames() {
		String[] columnNames = new String[codes.size()];
		for (int i = 0; i < codes.size(); i++) {
			columnNames[i] = codes.get(i);
		}
		return columnNames;
	}

	/**
	 * @return the data of the table.
	 */
	public Object[][] getTableData() {
		Object[][] data = new Object[codes.size()][codes.size()];
		for (int i = 0; i < codes.size(); i++) {
			for (int j = 0; j < codes.size(); j++) {
				data[i][j] = getValue(i, j);
			}
		}
		return data;
	}

	/**
	 * Get the value from the ConnectionSet. If i == j no connection return "x"
	 * 
	 * @param i
	 *            the x-as
	 * @param j
	 *            the y-as
	 * @return the value
	 */
	private String getValue(int i, int j) {
		if (i == j) {
			return "x";
		} else {
			return getValue(countMap.get(new ConnectionSet(codes.get(i), codes
					.get(j))));
		}
	}

	/**
	 * Get the real value of the connection.
	 * 
	 * @param i
	 *            the x-as
	 * @param j
	 *            the y-as
	 * @param value
	 *            the current value of the number of connections
	 * @return the value
	 */
	private String getValue(Integer value) {
		if (value != null) {
			return value.toString();
		} else {
			return "0";
		}
	}

	/**
	 * Fills the matrix with the data of the chunks.
	 * 
	 * @param chunks
	 *            the data.
	 */
	public void fillTransitionMatrix(ArrayList<Chunk> chunks) {
		countMap = new HashMap<ConnectionSet, Integer>();
		codes = new ArrayList<String>();
		if (chunks != null) {
			addChunksToMap(SingletonInterpreter.getInterpreter().getChunks());
		}
	}

	/**
	 * Add the chunks to the HashMap.
	 * 
	 * @param chunks
	 *            the chunks
	 */
	private void addChunksToMap(ArrayList<Chunk> chunks) {
		for (int i = 0; i < chunks.size(); i++) {
			Chunk currentChunk = chunks.get(i);
			if (!currentChunk.getCode().isEmpty()
					&& !currentChunk.getPointer().isEmpty()) {
				processChunk(currentChunk);
			}
		}
	}

	/**
	 * Process a chunk and decides if its code is already added or not.
	 * 
	 * @param currentChunk
	 *            the current chunk
	 */
	private void processChunk(Chunk currentChunk) {
		String code = currentChunk.getCode();
		if (codes.contains(code)) {
			processChunkWithPointer(currentChunk);
		} else {
			codes.add(code);
			processChunkWithPointer(currentChunk);
		}
	}

	/**
	 * Process for each pointer a new value to the map.
	 * 
	 * @param currentChunk
	 *            the chunk with the current pointer.
	 */
	private void processChunkWithPointer(Chunk currentChunk) {
		HashMap<Chunk, String> pointerMap = currentChunk.getPointer();
		for (Entry<Chunk, String> e : pointerMap.entrySet()) {
			addPointers(e.getKey().getCode(), currentChunk);
		}
	}

	/**
	 * Handle different cases for the pointers.
	 * 
	 * @param currentCodeFromPointer
	 *            the current pointer
	 * @param currentChunk
	 *            the current chunk
	 */
	private void addPointers(String currentCodeFromPointer, Chunk currentChunk) {
		if (!codes.contains(currentCodeFromPointer)) {
			codes.add(currentCodeFromPointer);
		}
		ConnectionSet currentSet = new ConnectionSet(currentChunk.getCode(),
				currentCodeFromPointer);
		if (countMap.get(currentSet) != null) {
			Integer value = countMap.remove(currentSet) + 1;
			countMap.put(currentSet, value);
		} else {
			countMap.put(currentSet, 1);
		}
	}
}
