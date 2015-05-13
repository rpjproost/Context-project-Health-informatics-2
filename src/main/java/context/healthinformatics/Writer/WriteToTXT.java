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
	 * @param rs
	 *            the resultset of the data
	 */
	public WriteToTXT(String fileName, String path, ResultSet rs) {
		this.fileName = fileName;
		this.path = path;
		this.rs = rs;
	}

	/**
	 * Write to file.
	 * 
	 * @throws SQLException
	 *             the sql exception of resultset
	 */
	public void writeToFile() throws SQLException {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(path + fileName, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.println("DATA LIST LIST");
		ResultSetMetaData rsmd = rs.getMetaData();
		int numColumns = rsmd.getColumnCount();
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < numColumns; i++) {
			res.append(rsmd.getColumnName(i + 1) + " ");
		}
		writer.println("/ " + res.toString());
		
		writer.println("BEGIN DATA.");
			
		StringBuffer str = processResultSet(numColumns);
		writer.println(str.toString());

		writer.println("END DATA.");
		writer.println("");
		writer.println("LIST.");
		writer.close();
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
		return str;
	}
}
