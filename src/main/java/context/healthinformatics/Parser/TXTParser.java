package context.healthinformatics.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class TxtParser.
 */
public class TXTParser extends Parser {
	private String delimiter;
	private int startLine;
	private File file;
	private Scanner sc;
	private ArrayList<Column> columns;

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
	 */
	public TXTParser(String fileName, int startLine, String delimiter,
			ArrayList<Column> columns) {
		super(fileName);
		setStartLine(startLine);
		this.delimiter = delimiter;
		this.columns = columns;
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
		if(startLine > 0){
		this.startLine = startLine;
		}else{
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
			throw new FileNotFoundException();
		}
		skipFirxtXLines();
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			// TODO insert splitted string into db.
			String[] splittedLine = splitLine(line);
			System.out.println(splittedLine[0] + " " + splittedLine[1] + " "
					+ splittedLine[2]);
		}
		sc.close();
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
		try {
			for (int i = 0; i < columns.size(); i++) {
				res[i] = strings[columns.get(i).getColumnNumber() - 1];
			}
			return res;
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException();
		}
	}

}