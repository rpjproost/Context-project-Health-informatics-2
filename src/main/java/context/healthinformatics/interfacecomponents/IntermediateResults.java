package context.healthinformatics.interfacecomponents;

import java.awt.Dimension;
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
import context.healthinformatics.gui.MainFrame;
import context.healthinformatics.parser.Column;
import context.healthinformatics.sequentialdataanalysis.Chunk;

/**
 * Panel which displays the intermediate results of the list of chunks.
 */
public class IntermediateResults extends InterfaceHelper {
	private static final long serialVersionUID = 1L;
	private static final int FIELDCORRECTION = 130;
	private static final int HUNDRED_PERCENT = 100;
	private static final int TEN = 100;
	private static final int NINE = 98;
	private int intermediateResultWidth;
	private Db database = SingletonDb.getDb();

	private JPanel interMediateResultParentPanel;
	private JEditorPane displayHtmlPane = new JEditorPane();
	private JScrollPane scroll;
	private Interpreter interpreter = SingletonInterpreter.getInterpreter();
	private MainFrame mf;

	/**
	 * Constructor of the IntermediateResults class.
	 * 
	 * @param mf
	 *            the mainframe to get screen width
	 */
	public IntermediateResults(MainFrame mf) {
		this.mf = mf;
		intermediateResultWidth = (mf.getScreenWidth() / 2)
				- (mf.getScreenWidth() / HUNDRED_PERCENT) * 2;
		interMediateResultParentPanel = createEmptyWithGridBagLayoutPanel(MainFrame.CODETABCOLOR);
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
				intermediateResultWidth, mf.getStatePanelSize() / TEN * NINE - FIELDCORRECTION));
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
				mf.getStatePanelSize() / TEN * NINE - FIELDCORRECTION));
		scroll.getVerticalScrollBar().setValue(0);
	}

	/**
	 * Add the title and the display pane to the parent panel.
	 */
	private void addEverythingToMainPanel() {
		interMediateResultParentPanel.add(
				createTitle("The intermediate Result:"), setGrids(0, 0));
		interMediateResultParentPanel.add(scroll, setGrids(0, 1));

	}

	/**
	 * Update the text of the intermediate result.
	 * 
	 * @param
	 */
	public void updateIntermediateResult() {
		if (interpreter.getChunks() != null) {
			this.displayHtmlPane.setText(buildHtmlOfIntermediateResult());
			this.displayHtmlPane.setCaretPosition(0);
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
		buildString.append("<html><body><h2>Number of chunks: " + chunks.size()
				+ "</h2><table style='width:100%;'>");
		buildString.append(buildColumnsHTMLTableRow(database.getColumns()));
		buildString.append(loopThroughChunks(chunks));
		buildString.append("</table></body></html>");
		return buildString.toString();
	}

	private String buildColumnsHTMLTableRow(ArrayList<Column> columns) {
		StringBuilder buildString = new StringBuilder();
		buildString
				.append("<tr><td><h2>Code:</h2></td><td><h2>Comment:</h2></td>");
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
		ArrayList<Chunk> childChunks = chunk.getChunks();
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
			buildString.append("<tr>");
			if (currentChunk.hasChild()) {
				buildString.append("<td><h2>");
				buildString.append(currentChunk.toArray());
				buildString.append("</h2></td>");
				buildString.append(getChildsOfChunk(currentChunk));
				buildString.append("<td>[End Of Chunk]</td>");
			} else {
				buildString.append("<td>" + currentChunk.getCode() + "</td>");
				buildString
						.append("<td>" + currentChunk.getComment() + "</td>");
				ArrayList<String> values = currentChunk.toArray();
				for (int j = 0; j < values.size(); j++) {
					buildString.append("<td>" + values.get(j) + "</td>");
				}
			}
			buildString.append("</tr>");

		}
		return buildString.toString();
	}
}
