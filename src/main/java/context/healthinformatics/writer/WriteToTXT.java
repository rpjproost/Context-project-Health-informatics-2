package context.healthinformatics.writer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

import context.healthinformatics.database.Db;
import context.healthinformatics.parser.Column;
import context.healthinformatics.sequentialdataanalysis.Chunk;

/**
 * Class which output txt file for spss. Spss text file format:
 * http://www.ats.ucla.edu/stat/spss/modules/input.htm
 */
public class WriteToTXT {
	private String fileName;

	/**
	 * Constructor for writetoTXT.
	 * 
	 * @param fileName
	 *            the filename of the txtfile
	 */
	public WriteToTXT(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Try to setup new printwriter at specified path with charset UTF-8.
	 * 
	 * @return the printwriter.
	 * @throws FileNotFoundException
	 *             FileNotFoundException
	 * @throws UnsupportedEncodingException
	 *             unsupportedEncodingException
	 */
	public PrintWriter getPrintWriter() throws FileNotFoundException,
			UnsupportedEncodingException {
		PrintWriter writer = null;
		if (!fileName.endsWith(".txt")) {
			fileName += ".txt";
		}
		System.out.println(fileName);
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
	 * Write text to file.
	 * 
	 * @param text
	 *            the text of the old code area
	 * @throws FileNotFoundException
	 *             if file is not found
	 * @throws UnsupportedEncodingException
	 *             if encoding is not supported
	 */
	public void writeOldCodeArea(String text) throws FileNotFoundException,
			UnsupportedEncodingException {
		PrintWriter writer = getPrintWriter();
		Scanner sc = new Scanner(text);
		while (sc.hasNextLine()) {
			writer.println(sc.nextLine());
		}
		sc.close();
		writer.close();
	}

	/**
	 * Write to file.
	 * 
	 * @param chunks
	 *            the chunks to write to file
	 * @param database
	 *            the database to get the columnnames
	 * @throws FileNotFoundException
	 *             filenotfoundexception
	 * @throws UnsupportedEncodingException
	 *             unsupportedencodingexception
	 */
	public void writeSPSSDataToFile(ArrayList<Chunk> chunks, Db database)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = getPrintWriter();
		writer.println("DATA LIST LIST");
		writer.println("/ " + processColumnNames(database.getColumns()));
		writer.println("BEGIN DATA.");
		writer.println(processChunks(chunks));
		writer.println("END DATA.");
		writer.println();
		writer.println("LIST.");
		writer.close();
	}

	/**
	 * Process the column names of the table.
	 * 
	 * @param columns
	 *            the columns of the data
	 * @return a the columns
	 */
	public String processColumnNames(ArrayList<Column> columns) {
		StringBuffer res = new StringBuffer();
		for (int i = 1; i < columns.size(); i++) {
			if (i == columns.size() - 1) {
				res.append(columns.get(i).getColumnName());
			} else {
				res.append(columns.get(i).getColumnName() + " ");
			}
		}
		return res.toString();
	}

	/**
	 * Process the result set.
	 * 
	 * @param chunks
	 *            the list of chunks
	 * @return a stringbuffer with the right formatted resultset
	 */
	public String processChunks(ArrayList<Chunk> chunks) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < chunks.size(); i++) {
			Chunk chunk = chunks.get(i);
			if (chunk.hasChild()) {
				str.append(processChunks(chunk.getChildren()));
			} else {
				ArrayList<String> values = chunk.toWriter();
				for (int j = 0; j < values.size(); j++) {
					if (j == values.size() - 1) {
						str.append(values.get(j) + "\r\n");
					} else {
						str.append(values.get(j) + ",");
					}
				}
			}
		}
		return str.toString();
	}
}
