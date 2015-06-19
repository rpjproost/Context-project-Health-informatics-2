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
 * Class which output TXT file for SPSS. SPSS text file format:
 * http://www.ats.ucla.edu/stat/spss/modules/input.htm
 */
public class WriteToTXT {
	private String fileName;

	/**
	 * Constructor for writetoTXT.
	 * 
	 * @param fileName
	 *            the filename of the TXT file
	 */
	public WriteToTXT(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Try to setup new PrintWriter at specified path with CharSet UTF-8.
	 * 
	 * @return the PrintWriter.
	 * @throws FileNotFoundException
	 *             FileNotFoundException
	 */
	private PrintWriter getPrintWriter() throws FileNotFoundException {
		if (!fileName.endsWith(".txt")) {
			fileName += ".txt";
		}
		return write(null);
	}

	/**
	 * Try to write to the specified file.
	 * 
	 * @param writer
	 *            the writer
	 * @return the writer
	 * @throws FileNotFoundException
	 */
	private PrintWriter write(PrintWriter writer) throws FileNotFoundException {
		try {
			writer = new PrintWriter(fileName, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			throw new FileNotFoundException(
					"The path to write to is not found!");
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
	 *            the database to get the column names
	 * @throws FileNotFoundException
	 *             FileNotFoundException
	 */
	public void writeSPSSDataToFile(ArrayList<Chunk> chunks, Db database)
			throws FileNotFoundException {
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
	private String processColumnNames(ArrayList<Column> columns) {
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
	 * @return a String with the right formatted ResultSet
	 */
	private String processChunks(ArrayList<Chunk> chunks) {
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
