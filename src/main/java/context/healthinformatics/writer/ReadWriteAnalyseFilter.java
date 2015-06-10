package context.healthinformatics.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class to save and get the filters for the project.
 */
public class ReadWriteAnalyseFilter {

	private static final int NUMBER_OF_FILTER_VALUES = 3;

	private String fileName;
	private File file;
	private Scanner sc;
	private ArrayList<String[]> listOfFilters = new ArrayList<String[]>();

	/**
	 * Constructor of the ReadWriteAnalyseFilter sets the name of the file.
	 * 
	 * @param fileName
	 *            the name of the file with path
	 */
	public ReadWriteAnalyseFilter(String fileName) {
		this.fileName = fileName;
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
	 * Read the saved filters from the project file.
	 * 
	 * @return the list of filters
	 * @throws IOException
	 *             if file is not found
	 */
	public ArrayList<String[]> readFilter() throws IOException {
		file = openFile(this.fileName);
		sc = new Scanner(file, "UTF-8");
		readLines();
		sc.close();
		return listOfFilters;

	}

	/**
	 * Read the actual lines.
	 */
	private void readLines() {
		while (sc.hasNextLine()) {
			String[] listOfValues = new String[NUMBER_OF_FILTER_VALUES];
			String thisLine = sc.nextLine();
			listOfValues[0] = thisLine.substring(0, thisLine.indexOf("."));
			listOfValues[1] = thisLine.substring(thisLine.indexOf(".") + 1,
					thisLine.indexOf(" "));
			listOfValues[2] = thisLine.substring(thisLine.indexOf("'") + 1,
					thisLine.lastIndexOf("'"));
			listOfFilters.add(listOfValues);
		}
	}

	/**
	 * Get the printwriter to save filters.
	 * 
	 * @return the print writer
	 * @throws FileNotFoundException
	 *             filenotfoundexception
	 * @throws UnsupportedEncodingException
	 *             unsupportedencodingexception
	 */
	public PrintWriter getPrintWriter() throws FileNotFoundException,
			UnsupportedEncodingException {
		PrintWriter writer = null;

		try {
			writer = new PrintWriter(fileName, "UTF-8");
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException(
					"The path to write to is not found!");
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedEncodingException(
					"The encoding for the write to file is not supported!");
		}

		return writer;
	}

	/**
	 * Write to file.
	 * 
	 * @param filters
	 *            the specified filters which need to be stored
	 * 
	 * @throws FileNotFoundException
	 *             filenotfoundexception
	 * @throws UnsupportedEncodingException
	 *             unsupportedencodingexception
	 */
	public void writeToFile(String[] filters) throws FileNotFoundException,
			UnsupportedEncodingException {
		PrintWriter writer = getPrintWriter();
		for (int i = 0; i < filters.length; i++) {
			writer.println(filters[i]);
		}
		writer.close();
	}
}
