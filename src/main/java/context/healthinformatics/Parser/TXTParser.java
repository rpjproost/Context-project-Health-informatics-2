package context.healthinformatics.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class TxtParser.
 * 
 * @author Wim Spaargaren, April 2014
 *
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
	 */
	public TXTParser(String fileName, int startLine, String delimiter,
			ArrayList<Column> columns) {
		super(fileName);
		this.startLine = startLine;
		this.delimiter = delimiter;
		this.columns = columns;
		try {
			this.file = openFile(fileName);
			this.sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	 * Open the file to parse.
	 * 
	 * @param filename
	 *            name of the file
	 * @return the file
	 * @throws FileNotFoundException
	 *             if file not found throw exception
	 */
	public File openFile(String filename) throws FileNotFoundException {
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
	 */
	@Override
	public void parse() {
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
		try{
			for (int i = 0; i < columns.size(); i++) {
				res[i] = strings[columns.get(i).getColumnNumber() - 1];
			}
			return res;
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println(e.toString());
			return res;
		}
	}

}