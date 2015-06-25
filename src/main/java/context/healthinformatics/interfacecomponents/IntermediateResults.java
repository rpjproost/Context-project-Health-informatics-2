package context.healthinformatics.interfacecomponents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

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
	private static final int MAX_RESULTS_TO_DISPLAY = 1000;
	private JPanel interMediateResultParentPanel;
	private JEditorPane displayHtmlPane = new JEditorPane();
	private JScrollPane scroll;
	private Db database;
	private int globalChunkCounter = 1;
	private int numberOfDataColumns;
	private String title;
	private static final int THREE = 3;
	private static final int FOUR = 4;

	/**
	 * Constructor of the IntermediateResults class.
	 * 
	 * @param color
	 *            the color of the panel.
	 * @param width
	 *            the width of the panel.
	 * @param height
	 *            the height of this panel.
	 * @param title
	 *            the title of the panel.
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
		scroll.setPreferredSize(new Dimension(intermediateResultWidth,
				intermediateResultHeight));
		scroll.getVerticalScrollBar().setValue(0);
	}

	/**
	 * Add the title and the display pane to the parent panel.
	 */
	private void addEverythingToMainPanel() {
		GridBagConstraints c = setGrids(0, 0);
		c.anchor = GridBagConstraints.LINE_START;
		interMediateResultParentPanel.add(createTitle(title), c);
		interMediateResultParentPanel.add(scroll, setGrids(0, 1));

	}

	/**
	 * Update the text of the intermediate result.
	 */
	public void updateIntermediateResult() {
		if (SingletonInterpreter.getInterpreter().getChunks() != null) {
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
		ArrayList<Chunk> chunks = SingletonInterpreter.getInterpreter()
				.getChunks();
		StringBuilder buildString = new StringBuilder();
		database = SingletonDb.getDb();
		numberOfDataColumns = database.getColumns().size();
		String htmlOfColumnTableRow = buildColumnsHTMLTableRow(database
				.getColumns());
		String htmlOfTableContent = loopThroughChunks(chunks, true);
		buildString.append("<html><body><h2>Number of chunks: "
				+ (chunks.size()) + "</h2><table style='width:100%;'>");
		buildString.append(htmlOfColumnTableRow);
		buildString.append(htmlOfTableContent);
		buildString.append("</table></body></html>");
		return buildString.toString();
	}

	/**
	 * Make HTML table header with column names for the results.
	 * 
	 * @param columns
	 *            the columns
	 * @return HTML table row string
	 */
	private String buildColumnsHTMLTableRow(ArrayList<Column> columns) {
		StringBuilder buildString = new StringBuilder();
		buildString.append("<tr>");
		buildString.append("<td><h2>line:</h2></td>");
		buildString.append("<td><h2>haschildren:</h2></td>");
		buildString.append("<td><h2>code:</h2></td>");
		buildString.append("<td><h2>comment:</h2></td>");
		for (int i = 0; i < columns.size(); i++) {
			buildString.append("<td><h2>" + columns.get(i).getColumnName()
					+ ":</h2></td>");
		}
		buildString.append("<td><h2>difference:</h2></td>");
		buildString.append("<td><h2>connection:</h2></td>");
		buildString.append("<td><h2>computation:</h2></td>");
		buildString.append("</td>");
		return buildString.toString();
	}

	/**
	 * Build a HTML table row for the chunks.
	 * 
	 * @param chunks
	 *            the list of chunks
	 * @return a string of HTML formatted for a table.
	 */
	private String loopThroughChunks(ArrayList<Chunk> chunks, boolean topLevel) {
		StringBuilder buildString = new StringBuilder();
		for (int i = 0; i < setMaxNumberOfDisplay(chunks); i++) {
			Chunk currentChunk = chunks.get(i);
			if (topLevel) {
				buildString.append("<tr><td>" + globalChunkCounter + "</td>");
				globalChunkCounter++;
			} else {
				buildString.append("<tr><td></td>");
			}
			if (currentChunk.hasChild()) {
				buildString.append(processChunkWithChildren(currentChunk));
			} else {
				buildString.append(processChunk(currentChunk));
			}
			buildString.append("</tr>");
		}
		return buildString.toString();
	}

	/**
	 * Check if the number of chunks is not larger then the max number to
	 * display.
	 * 
	 * @param chunks
	 *            the current number of chunks
	 * @return the total number to display
	 */
	private int setMaxNumberOfDisplay(ArrayList<Chunk> chunks) {
		int total = chunks.size();
		if (total > MAX_RESULTS_TO_DISPLAY) {
			total = MAX_RESULTS_TO_DISPLAY;
		}
		return total;
	}

	/**
	 * Build HTML table content of a chunk.
	 * 
	 * @param currentChunk
	 *            the currentChunk
	 * @return HTML string with table row
	 */
	private String processChunk(Chunk currentChunk) {
		StringBuilder buildString = new StringBuilder();
		buildString
				.append("<td>no</td><td>" + currentChunk.getCode() + "</td>");
		buildString.append("<td>" + currentChunk.getComment() + "</td>");
		ArrayList<String> values = currentChunk.toArray();
		for (int j = 0; j < values.size(); j++) {
			buildString.append("<td>" + values.get(j) + "</td>");
		}
		return buildString.toString();
	}

	/**
	 * Build HTML table content of a chunk with children.
	 * 
	 * @param currentChunk
	 *            the current chunk with children.
	 * @return HTML string with table rows for every child and the chunk itself
	 */
	private String processChunkWithChildren(Chunk currentChunk) {
		StringBuilder buildString = new StringBuilder();
		buildString.append("<td>yes</td><td><h2>");
		ArrayList<String> chunkWithChildren = currentChunk.toArray();
		buildString.append("<td>" + chunkWithChildren.get(0) + "</td>");
		buildString.append("<td>" + chunkWithChildren.get(1) + "</td>");
		for (int i = 1; i < numberOfDataColumns; i++) {
			buildString.append("<td></td>");
		}
		buildString.append("<td>" + chunkWithChildren.get(2) + "</td>");
		buildString.append("<td>" + chunkWithChildren.get(THREE) + "</td>");
		buildString.append("<td>" + chunkWithChildren.get(FOUR) + "</td>");
		buildString.append("</h2></td>");
		buildString.append(getChildrenOfChunk(currentChunk));
		buildString.append("<tr><td>[Chunk ends here]</td></tr>");
		return buildString.toString();
	}

	/**
	 * Create new HTML list for the children of a chunk.
	 * 
	 * @param the
	 *            chunk with children
	 * @return HTML list with the children
	 */
	private String getChildrenOfChunk(Chunk chunk) {
		StringBuilder buildString = new StringBuilder();
		buildString.append(loopThroughChunks(chunk.getChildren(), false));
		return buildString.toString();
	}
}
