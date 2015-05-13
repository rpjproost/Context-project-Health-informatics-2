package context.healthinformatics.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import context.healthinformatics.Database.Db;
import context.healthinformatics.Database.SingletonDb;

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
	 * @param docName name of doc.
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
	 * Get the startline.
	 * 
	 * @return startline
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
	 * @return String containing the document name.
	 */
	public String getDocName() {
		return docName;
	}

	/**
	 * setter for the document name.
	 * @param docName document name to set.
	 */
	public void setDocName(String docName) {
		this.docName = docName;
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
	public File openFile(String filename) {
		return new File(filename);
	}

	/**
	 * Skip the first lines till the relevant data.
	 */
	public void skipFirxtXLines() {
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
			this.sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("The TXT file was not found!");
		}
		skipFirxtXLines();
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			// System.out.println(line);

			if (canSplit(line)) {
				// TODO insert splitted string into db.
				String[] splittedLine = splitLine(line);
				Db data = SingletonDb.getDb();
				try {
					data.insert(docName, splittedLine, columns);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					
				}
			}
		}
		sc.close();
	}

	/**
	 * Check if line can be split and added to the database.
	 * 
	 * @param line
	 *            the line to be split
	 * @return true if can split false if not
	 */
	public boolean canSplit(String line) {
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
			res[i] = strings[columns.get(i).getColumnNumber() - 1];
		}
		return res;

	}

}