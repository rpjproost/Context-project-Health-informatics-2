package context.healthinformatics.interfacecomponents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import context.healthinformatics.analyse.Interpreter;
import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.gui.InterfaceHelper;
import context.healthinformatics.parser.Column;
import context.healthinformatics.sequentialdataanalysis.Chunk;

/**
 * Panel which displays the intermediate results of the list of chunks.
 */
public class IntermediateResults extends InterfaceHelper {
	private static final long serialVersionUID = 1L;
	private int intermediateResultWidth;
	private int intermediateResultHeight;

	private JPanel interMediateResultParentPanel;
	private JEditorPane displayHtmlPane = new JEditorPane();
	private JScrollPane scroll;
	private Interpreter interpreter = SingletonInterpreter.getInterpreter();

	private int globalChunkCounter = 1;
	private String title;

	/**
	 * Constructor of the IntermediateResults class.
	 * 
	 * @param color the color of the panel.
	 * @param width the width of the panel.
	 * @param height the height of this panel.
	 * @param title the title of the panel.
	 */
	public IntermediateResults(int width, int height, String title, Color color) {
		intermediateResultWidth = width;
		intermediateResultHeight = height;
		this.title = title;
		interMediateResultParentPanel = createEmptyWithGridBagLayoutPanel(color);
		initDisplayHTMLPane();
		initScrollPane();
		addEverythingToMainPanel();
	}

	/**
	 * Load the main panel for the intermediate result.
	 * 
	 * @return the panel with the intermediate result
	 */
	public JPanel loadPanel() {
		return interMediateResultParentPanel;
	}

	/**
	 * Initialize the pane to display the HTML.
	 */
	private void initDisplayHTMLPane() {
		this.displayHtmlPane.setPreferredSize(new Dimension(
				intermediateResultWidth, intermediateResultHeight));
		this.displayHtmlPane.setEditable(false);
		this.displayHtmlPane.setContentType("text/html");
		updateIntermediateResult();
		this.displayHtmlPane.setCaretPosition(0);
	}

	/**
	 * Initialize the scroll pane.
	 */
	private void initScrollPane() {
		scroll = new JScrollPane(displayHtmlPane);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(new Dimension(intermediateResultWidth, intermediateResultHeight));
		scroll.getVerticalScrollBar().setValue(0);
	}

	/**
	 * Add the title and the display pane to the parent panel.
	 */
	private void addEverythingToMainPanel() {
		GridBagConstraints c = setGrids(0, 0);
		c.anchor = GridBagConstraints.LINE_START;
		interMediateResultParentPanel.add(
				createTitle(title), c);
		interMediateResultParentPanel.add(scroll, setGrids(0, 1));

	}

	/**
	 * Update the text of the intermediate result.
	 */
	public void updateIntermediateResult() {
		if (interpreter.getChunks() != null) {
			globalChunkCounter = 1;
			this.displayHtmlPane.setText(buildHtmlOfIntermediateResult());
			this.displayHtmlPane.setCaretPosition(0);
			this.displayHtmlPane.revalidate();
		} else {
			this.displayHtmlPane
					.setText("<h1>There is no intermediate result to display</h1>");
		}
	}

	/**
	 * Build HTML content of the intermediate chunks.
	 * 
	 * @return the string with the HTML
	 */
	private String buildHtmlOfIntermediateResult() {
		ArrayList<Chunk> chunks = interpreter.getChunks();
		StringBuilder buildString = new StringBuilder();
		Db database = SingletonDb.getDb();
		String htmlOfColumnTableRow = buildColumnsHTMLTableRow(database
				.getColumns());
		String htmlOfTableContent = loopThroughChunks(chunks);
		buildString
				.append("<html><body><h2>Number of chunks: "
						+ (globalChunkCounter - 1)
						+ "</h2><table style='width:100%;'>");
		buildString.append(htmlOfColumnTableRow);
		buildString.append(htmlOfTableContent);

		buildString.append("</table></body></html>");
		return buildString.toString();
	}

	/**
	 * Make HTML table row for the columns of the results.
	 * 
	 * @param columns
	 *            the columns
	 * @return HTML table row string
	 */
	private String buildColumnsHTMLTableRow(ArrayList<Column> columns) {
		StringBuilder buildString = new StringBuilder();
		buildString
				.append("<tr><td><h2>Line:</h2></td><td><h2>Code:</h2></td>");
		buildString.append("<td><h2>Comment:</h2></td>");
		for (int i = 0; i < columns.size(); i++) {
			buildString.append("<td><h2>" + columns.get(i).getColumnName()
					+ ":</h2></td>");
		}
		buildString.append("</tr>");
		return buildString.toString();
	}

	/**
	 * Create new HTML list for the children of a chunk.
	 * 
	 * @param the
	 *            chunk with children
	 * @return HTML list with the children
	 */
	private String getChildsOfChunk(Chunk chunk) {
		ArrayList<Chunk> childChunks = chunk.getChildren();
		StringBuilder buildString = new StringBuilder();
		buildString.append(loopThroughChunks(childChunks));
		return buildString.toString();
	}

	/**
	 * Build a table row for the chunk values.
	 * 
	 * @param chunks
	 *            the list of chunks
	 * @return a string of html formatted for a table.
	 */
	private String loopThroughChunks(ArrayList<Chunk> chunks) {
		StringBuilder buildString = new StringBuilder();
		for (int i = 0; i < chunks.size(); i++) {
			Chunk currentChunk = chunks.get(i);
			buildString.append("<tr><td>" + globalChunkCounter + "</td>");
			globalChunkCounter++;
			if (currentChunk.hasChild()) {
				buildString.append(processChunkWithChilds(currentChunk));
			} else {
				buildString.append(processChunk(currentChunk));
			}
			buildString.append("</tr>");
		}
		return buildString.toString();
	}

	/**
	 * Build html table content of a chunk.
	 * 
	 * @param currentChunk
	 *            the currentChunk
	 * @return HTML string with table row
	 */
	private String processChunk(Chunk currentChunk) {
		StringBuilder buildString = new StringBuilder();
		buildString.append("<td>" + currentChunk.getCode() + "</td>");
		buildString.append("<td>" + currentChunk.getComment() + "</td>");
		ArrayList<String> values = currentChunk.toArray();
		for (int j = 0; j < values.size(); j++) {
			buildString.append("<td>" + values.get(j) + "</td>");
		}
		return buildString.toString();
	}

	/**
	 * Build HTML table content of a chunk with childs.
	 * 
	 * @param currentChunk
	 *            the current chunk with childs.
	 * @return HTML string with table rows for every child and the chunk itself
	 */
	private String processChunkWithChilds(Chunk currentChunk) {
		StringBuilder buildString = new StringBuilder();
		buildString.append("<td><h2>");
		buildString.append(currentChunk.toArray());
		buildString.append("</h2></td>");
		buildString.append(getChildsOfChunk(currentChunk));
		buildString.append("<td><h2>[End Of Chunk]</h2></td>");
		return buildString.toString();
	}
}
