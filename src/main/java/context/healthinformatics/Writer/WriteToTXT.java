package context.healthinformatics.Writer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Class which output txt file for spss. Spss text file format:
 * http://www.ats.ucla.edu/stat/spss/modules/input.htm
 */
public class WriteToTXT {
	private String fileName;
	private String path;
	private ResultSet rs;

	/**
	 * Constructor for writetoTXT.
	 * 
	 * @param fileName
	 *            the filename of the txtfile
	 * @param path
	 *            the path where the txt file is put
	 */
	public WriteToTXT(String fileName, String path) {
		this.fileName = fileName;
		this.path = path;
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

		try {
			writer = new PrintWriter(path + fileName, "UTF-8");
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
	 * @param rs 
	 * 
	 * @throws SQLException
	 *             the sql exception of resultset
	 * @throws FileNotFoundException
	 *             filenotfoundexception
	 * @throws UnsupportedEncodingException
	 *             unsupportedencodingexception
	 */
	public void writeToFile(ResultSet rs) throws SQLException, FileNotFoundException,
			UnsupportedEncodingException {
		PrintWriter writer = getPrintWriter();
		this.rs = rs;
		writer.println("DATA LIST LIST");
		ResultSetMetaData rsmd = this.rs.getMetaData();
		writer.println("/ " + processColumnNames(rsmd, rsmd.getColumnCount()).toString());

		writer.println("BEGIN DATA.");
		writer.println(processResultSet(rsmd.getColumnCount()).toString());
		writer.println("END DATA.");
		
		writer.println();
		writer.println("LIST.");
		writer.close();
	}

	/**
	 * Process the column names of the table.
	 * @param rsmd the resultsets meta data
	 * @param numColumns the number of columns
	 * @return a stringbuffer with the columnnames
	 * @throws SQLException the sqlexception
	 */
	public StringBuffer processColumnNames(ResultSetMetaData rsmd,
			int numColumns) throws SQLException {
		StringBuffer res = new StringBuffer();
		for (int i = 1; i < numColumns; i++) {
			if (i == numColumns - 1) {
				res.append(rsmd.getColumnName(i + 1));
			} else {
				res.append(rsmd.getColumnName(i + 1) + " ");
			}
		}
		
		return res;
	}

	/**
	 * Process the result set.
	 * 
	 * @param numColumns
	 *            the number of columns in the resultset
	 * @return a stringbuffer with the right formatted resultset
	 * @throws SQLException
	 *             the sql exception of the resultset
	 */
	public StringBuffer processResultSet(int numColumns) throws SQLException {
		StringBuffer str = new StringBuffer();
		while (rs.next()) {
			for (int i = 1; i < numColumns; i++) {
				if (i == numColumns - 1) {
					str.append(rs.getObject(i + 1));
				} else {
					str.append(rs.getObject(i + 1) + " ");
				}
			}
			str.append(System.lineSeparator());
		}
		str.setLength(str.length() - 2);
		rs.close();
		return str;
	}
}
