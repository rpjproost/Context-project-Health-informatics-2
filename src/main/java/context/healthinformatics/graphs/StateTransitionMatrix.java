package context.healthinformatics.graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

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
		scroll = new JScrollPane(table);
		mainPanel.add(scroll, setGrids(0, 0));
	}

	public JPanel getStateTransitionMatrix() {
		return mainPanel;
	}

	public String[] getColumnNames() {
		String[] columnNames = new String[codes.size()];
	//	columnNames[0] = "";
		for (int i = 1; i < codes.size(); i++) {
			columnNames[i] = codes.get(i);
		}
		return columnNames;
	}

	public Object[][] getTableData() {
		Object[][] data = new Object[codes.size()][codes.size()];
		for (int i = 0; i < codes.size(); i++) {
			for (int j = 0; j < codes.size(); j++) {
				data[i][j] = i + j;
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
