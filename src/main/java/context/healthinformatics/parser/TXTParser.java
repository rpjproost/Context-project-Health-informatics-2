package context.healthinformatics.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

import context.healthinformatics.database.Db;
import context.healthinformatics.database.SingletonDb;

/**
 * Class TxtParser.
 */
public class TXTParser extends Parser {

	private String delimiter;
	private int startLine;
	private File file;
	private Scanner sc;
	private ArrayList<Column> columns;
	private String docName;
	private Logger log = Logger.getLogger(TXTParser.class.getName());

	/**
	 * Constructor of the TXTParser.
	 * 
	 * @param fileName
	 *            name of txt file to open
	 * @param startLine
	 *            line where relevant data starts
	 * @param delimiter
	 *            the delimiter of the data
	 * @param columns
	 *            the arraylist with columns
	 * @param docName
	 *            name of doc.
	 * 
	 */
	public TXTParser(String fileName, int startLine, String delimiter,
			ArrayList<Column> columns, String docName) {
		super(fileName);
		setStartLine(startLine);
		this.delimiter = delimiter;
		this.columns = columns;
		this.docName = docName;
	}

	/**
	 * Set the delimiter.
	 * 
	 * @param delimiter
	 *            the new delimiter.
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * Getter for delimiter.
	 * 
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return this.delimiter;
	}

	/**
	 * Get the start line.
	 * @return start line
	 */
	public int getStartLine() {
		return startLine;
	}

	/**
	 * Set the start line.
	 * 
	 * @param startLine
	 *            the starting line
	 */
	public void setStartLine(int startLine) {
		if (startLine > 0) {
			this.startLine = startLine;
		} else {
			this.startLine = 1;
		}
	}

	/**
	 * Get columns.
	 * 
	 * @return the columns
	 */
	public ArrayList<Column> getColumns() {
		return columns;
	}

	/**
	 * Set the columns arraylist.
	 * 
	 * @param columns
	 *            the columns arraylist
	 */
	public void setColumns(ArrayList<Column> columns) {
		this.columns = columns;
	}

	/**
	 * getter for the document name.
	 * 
	 * @return String containing the document name.
	 */
	public String getDocName() {
		return docName;
	}

	/**
	 * Open the file to parse.
	 * 
	 * @param filename
	 *            name of the file
	 * @return the file
	 * @throws FileNotFoundException
	 *             if file not found throw exception
	 */
	private File openFile(String filename) {
		return new File(filename);
	}

	/**
	 * Skip the first lines till the relevant data.
	 */
	private void skipFirxtXLines() {
		for (int i = 1; i < startLine; i++) {
			if (sc.hasNextLine()) {
				sc.nextLine();
			}
		}
	}

	/**
	 * Parse the relevant lines to the database.
	 * 
	 * @throws FileNotFoundException
	 *             if the file is not found throw FileNotFoundException
	 */
	@Override
	public void parse() throws FileNotFoundException {
		try {
			this.file = openFile(this.getFileName());
			this.sc = new Scanner(file, "UTF-8");
			skipFirxtXLines();
			readRelevantLines();
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("The TXT file was not found!");
		} catch (SQLException e) {
			throw new FileNotFoundException(
					"TXT data could not be inserted into the database");
		}
		sc.close();
	}

	/**
	 * Read the relevant lines from startline.
	 * 
	 * @throws SQLException
	 *             the sql exception
	 */
	private void readRelevantLines() throws SQLException {
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (canSplit(line)) {
				insertToDb(splitLine(line));
			}
		}
	}

	/**
	 * Insert a row to the db.
	 * 
	 * @param cells
	 *            the string array with the cell values
	 * @throws SQLException
	 *             the exception if data can not be inserted in database
	 */
	private void insertToDb(String[] lines) throws SQLException {
		Db data = SingletonDb.getDb();
		try {
			data.insert(docName, lines, columns);
		} catch (SQLException e) {
			StringBuilder builder = new StringBuilder();
			builder.append("Error inserting: ");
			builder.append(printCells(lines));
			builder.append(" into: ").append(docName);
			log.info(builder.toString());
		}
	}

	/**
	 * Check if line can be split and added to the database.
	 * 
	 * @param line
	 *            the line to be split
	 * @return true if can split false if not
	 */
	private boolean canSplit(String line) {
		String[] strings = line.split(delimiter);
		return strings.length >= columns.size();
	}

	/**
	 * Split a line into a String array on the delimiter.
	 * 
	 * @param line
	 *            the line to split
	 * @return the split line.
	 */
	public String[] splitLine(String line) {
		String[] res = new String[columns.size()];
		String[] strings = line.split(delimiter);
		for (int i = 0; i < columns.size(); i++) {
			res[i] = formatString(strings, i);
		}
		return res;
	}

	/**
	 * If columntype is int it and surrounded by whitespaces, the whitespaces
	 * have to be removed.
	 * 
	 * @param strings
	 *            the splitted line
	 * @param currentColumn
	 *            the current column
	 * @return the formatted string
	 */
	private String formatString(String[] strings, int currentColumn) {
		if (columns.get(currentColumn).getColumnType().equals("INT")) {
			return strings[columns.get(currentColumn).getColumnNumber() - 1]
					.replaceAll("\\s", "");
		} else {
			return strings[columns.get(currentColumn).getColumnNumber() - 1];
		}
	}

	/**
	 * Test function for txt parser to build a splitted line string.
	 * 
	 * @param lines
	 *            the splitted line
	 * @return returns the created string
	 */
	public String printCells(String[] lines) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lines.length; i++) {
			sb.append("|");
			sb.append(lines[i]);
			sb.append("|");
		}
		return sb.toString();
	}

}