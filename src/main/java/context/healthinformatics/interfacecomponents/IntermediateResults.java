package context.healthinformatics.interfacecomponents;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import context.healthinformatics.analyse.Interpreter;
import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.gui.InterfaceHelper;
import context.healthinformatics.gui.MainFrame;
import context.healthinformatics.sequentialdataanalysis.Chunk;

/**
 * Panel which displays the intermediate results of the list of chunks.
 */
public class IntermediateResults extends InterfaceHelper {
	private static final long serialVersionUID = 1L;
	private static final int HEIGHT_SCROLLPANE = 500;
	private static final int HUNDRED_PERCENT = 100;
	private int intermediateResultWidth;

	private JPanel interMediateResultParentPanel;
	private JEditorPane displayHtmlPane = new JEditorPane();
	private JScrollPane scroll;
	private Interpreter interpreter = SingletonInterpreter.getInterpreter();

	/**
	 * Constructor of the IntermediateResults class.
	 * 
	 * @param mf
	 *            the mainframe to get screen width
	 */
	public IntermediateResults(MainFrame mf) {
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
				intermediateResultWidth, HEIGHT_SCROLLPANE));
		this.displayHtmlPane.setEditable(false);
		this.displayHtmlPane.setContentType("text/html");
		this.displayHtmlPane.setText(buildHtmlOfIntermediateResult());
		this.displayHtmlPane.setCaretPosition(0);
	}

	/**
	 * Initialize the scroll pane.
	 */
	private void initScrollPane() {
		scroll = new JScrollPane(displayHtmlPane);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(new Dimension(intermediateResultWidth,
				HEIGHT_SCROLLPANE));
		scroll.getVerticalScrollBar().setValue(0);

	}

	/**
	 * Add the title and the display pane to the parentpanel.
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
		this.displayHtmlPane.setText(buildHtmlOfIntermediateResult());
		this.displayHtmlPane.setCaretPosition(0);
	}

	/**
	 * Build HTML content of the intermediate chunks.
	 * 
	 * @return the string with the HTML
	 */
	private String buildHtmlOfIntermediateResult() {
		ArrayList<Chunk> chunks = interpreter.getChunks();
		System.out.println(chunks.size());
		StringBuilder buildString = new StringBuilder();
		buildString.append("<html><body><ul>");
		for (int i = 0; i < chunks.size(); i++) {
			Chunk currentChunk = chunks.get(i);
			buildString.append("<li>" + currentChunk.toString() + "</li>");
			if (currentChunk.hasChild()) {
				buildString.append(getChildsOfChunk(currentChunk));
			}
		}
		buildString.append("</ul></body></html>");
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
		buildString.append("<ul>");
		for (int i = 0; i < childChunks.size(); i++) {
			Chunk currentChunk = childChunks.get(i);
			buildString.append("<li>" + currentChunk.toString() + "</li>");
			if (currentChunk.hasChild()) {
				buildString.append(getChildsOfChunk(currentChunk));
			}
		}
		buildString.append("</ul>");
		return buildString.toString();
	}
}
