package context.healthinformatics.graphs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import context.healthinformatics.gui.InterfaceHelper;
import context.healthinformatics.sequentialdataanalysis.Chunk;

/**
 * The state transition matrix class.
 */
public class StateTransitionMatrix extends InterfaceHelper {
	private ArrayList<String> codes;
	private HashMap<ConnectionSet, Integer> countMap;
	private JTable table;
	private JScrollPane scroll;
	private JPanel mainPanel;
	private JTable headerTable;

	/**
	 * The constructor of the transition matrix.
	 */
	public StateTransitionMatrix() {
		codes = new ArrayList<String>();
		countMap = new HashMap<ConnectionSet, Integer>();
		mainPanel = createEmptyWithGridBagLayoutPanel();
	}

	public void initTable() {
		table = new JTable(getTableData(), getColumnNames());
		table.setEnabled(false);
		TableModel model = new DefaultTableModel() {

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
        headerTable = new JTable(model);
        for (int i = 0; i < table.getRowCount(); i++) {
            headerTable.setValueAt(codes.get(i), i, 0);
        }
        headerTable.setShowGrid(false);
        headerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        headerTable.setPreferredScrollableViewportSize(new Dimension(50, 0));
        headerTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        headerTable.getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable x, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                boolean selected = table.getSelectionModel().isSelectedIndex(row);
                Component component = table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(table, value, false, false, -1, -2);
                ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER);
                if (selected) {
                    component.setFont(component.getFont().deriveFont(Font.BOLD));
                    component.setForeground(Color.red);
                } else {
                    component.setFont(component.getFont().deriveFont(Font.PLAIN));
                }
                return component;
            }
        });
		scroll = new JScrollPane(table);
		scroll.setRowHeaderView(headerTable);
		mainPanel.add(scroll, setGrids(0, 0));
	}

	public JPanel getStateTransitionMatrix() {
		return mainPanel;
	}

	public String[] getColumnNames() {
		String[] columnNames = new String[codes.size()];
		for (int i = 0; i < codes.size(); i++) {
			columnNames[i] = codes.get(i);
		}
		return columnNames;
	}

	public Object[][] getTableData() {
		Object[][] data = new Object[codes.size()][codes.size()];
		for (int i = 0; i < codes.size(); i++) {
			for (int j = 0; j < codes.size(); j++) {
				if (i == j) {
					data[i][j] = "x";
				} else {
					ConnectionSet current = new ConnectionSet(codes.get(i), codes.get(j));
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

	public void fillTransitionMatrix(ArrayList<Chunk> chunks) {
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

}
