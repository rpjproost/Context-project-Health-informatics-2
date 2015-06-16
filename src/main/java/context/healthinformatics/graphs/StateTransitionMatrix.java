package context.healthinformatics.graphs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	private ArrayList<String> codes;
	private HashMap<ConnectionSet, Integer> countMap;
	private JTable table;
	private JScrollPane scroll;
	private JPanel mainPanel;
	private JTable headerTable;
	private int width;
	private JLabel graphTitle;
	private JFileChooser savePopup;
	private JPopupMenu menu = new JPopupMenu("Popup");

	/**
	 * The constructor of the transition matrix.
	 */
	public StateTransitionMatrix() {
		codes = new ArrayList<String>();
		graphTitle = new JLabel();
		countMap = new HashMap<ConnectionSet, Integer>();
		mainPanel = createEmptyWithGridBagLayoutPanel();
		width = getScreenWidth() / 2 - FOUR * INSETS;
		addItemsToPopUp();
	}

	/**
	 * Initialize the table.
	 * @param string the title of the matrix.
	 */
	public void initTable(String string) {
		mainPanel.remove(graphTitle);
		graphTitle = new JLabel("State-Transition Matrix: " + string);
		graphTitle.setFont(new Font("SansSerif", Font.BOLD, TEXTSIZE));
		mainPanel.add(graphTitle);
		table = new JTable(getTableData(), getColumnNames());
		table.setEnabled(false);
		TableModel model = createModel();
		headerTable = new JTable(model);
		for (int i = 0; i < table.getRowCount(); i++) {
			headerTable.setValueAt(codes.get(i), i, 0);
		}
		headerTable.setShowGrid(false);
		headerTable.setPreferredScrollableViewportSize(new Dimension(ROW_WIDTH,
				0));
		headerTable.getColumnModel().getColumn(0).setPreferredWidth(ROW_WIDTH);
		headerTable.getColumnModel().getColumn(0)
				.setCellRenderer(createTableCellRenderer());
		scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(width, codes.size() * HEIGHT));
		scroll.setRowHeaderView(headerTable);
		mainPanel.add(scroll, setGrids(0, 1));
		addMouseListeners();
	}
	
	private void addMouseListeners() {
		mainPanel.addMouseListener(new PopupTriggerListener());
		scroll.addMouseListener(new PopupTriggerListener());
		headerTable.addMouseListener(new PopupTriggerListener());
		table.addMouseListener(new PopupTriggerListener());
		graphTitle.addMouseListener(new PopupTriggerListener());
	}
	
	private void addItemsToPopUp() {
		JMenuItem item = new JMenuItem("Save as...");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				savePopup = saveFileChooser("png");
				saveImage(savePopup.showSaveDialog(table));
				System.out.println("Save as.. Test");
			}
		});
		menu.add(item);
	}
	
	/**
	 * Saves the matrix in a png file.
	 * @param choice the choice of the user.
	 */
	public void saveImage(int choice) {
		if (choice == JFileChooser.APPROVE_OPTION) {
			BufferedImage bi = ScreenImage.createImage(mainPanel);
			try {
				ScreenImage.writeImage(bi, savePopup.getSelectedFile()
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
				if (i == j) {
					data[i][j] = "x";
				} else {
					ConnectionSet current = new ConnectionSet(codes.get(i),
							codes.get(j));
					Integer value = countMap.get(current);
					if (value != null) {
						data[i][j] = value;
					} else {
						data[i][j] = 0;
					}
				}
			}
		}
		return data;
	}

	/**
	 * Fills the matrix with the data of the chunks.
	 * 
	 * @param chunks
	 *            the data.
	 */
	public void fillTransitionMatrix(ArrayList<Chunk> chunks) {
		countMap =  new HashMap<ConnectionSet, Integer>();
		if (chunks != null) {
			for (int i = 0; i < chunks.size(); i++) {
				Chunk currentChunk = chunks.get(i);
				if (!currentChunk.getCode().isEmpty()
						&& !currentChunk.getPointer().isEmpty()) {
					String code = currentChunk.getCode();
					if (codes.contains(code)) {
						processChunkWithPointer(currentChunk);
					} else {
						codes.add(code);
						processChunkWithPointer(currentChunk);
					}
				}
			}
		}
	}

	/**
	 * Process for each pointer a new value to the map.
	 * 
	 * @param currentChunk
	 *            the chunk with the current pointer.
	 */
	public void processChunkWithPointer(Chunk currentChunk) {
		HashMap<Chunk, String> pointerMap = currentChunk.getPointer();
		for (Entry<Chunk, String> e : pointerMap.entrySet()) {
			String currentCodeFromPointer = e.getKey().getCode();
			if (!codes.contains(currentCodeFromPointer)) {
				codes.add(currentCodeFromPointer);
			}
			ConnectionSet currentSet = new ConnectionSet(
					currentChunk.getCode(), currentCodeFromPointer);
			if (countMap.get(currentSet) != null) {
				Integer value = countMap.remove(currentSet) + 1;
				countMap.put(currentSet, value);
			} else {
				countMap.put(currentSet, 1);
			}
			if (currentChunk.hasChild()) {
				fillTransitionMatrix(currentChunk.getChildren());
			}
		}
	}
	
	/**
	 * Triggers the right-click pop up event.
	 */
	class PopupTriggerListener extends MouseAdapter {
		/**
		 * Triggers the event when the mouse is pressed.
		 * @param ev the mouse event.
		 */
		public void mousePressed(MouseEvent ev) {
			if (ev.isPopupTrigger()) {
				menu.show(ev.getComponent(), ev.getX(), ev.getY());
			}
		}

		/**
		 * Triggers the pop up event when the mouse is released.
		 * @param ev the mouse event.
		 */
		public void mouseReleased(MouseEvent ev) {
			if (ev.isPopupTrigger()) {
				menu.show(ev.getComponent(), ev.getX(), ev.getY());
			}
		}

		/**
		 * Do nothing when the mouse is clicked.
		 * @param ev the mouse event.
		 */
		public void mouseClicked(MouseEvent ev) {
		}
	}

}
