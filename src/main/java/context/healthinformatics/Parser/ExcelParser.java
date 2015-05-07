package context.healthinformatics.Parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * The ExcelParser class. Parses a Excel sheet.
 *
 */
public class ExcelParser extends Parser {
	private int startLine;
	private ArrayList<Column> columns;

	/**
	 * Constructor for the ExcelParser.
	 * 
	 * @param fileName
	 *            the filename of the excel file to be processed
	 * @param startLine
	 *            the number of the start line of the excel sheet
	 * @param columns
	 *            the relevant columns in the excel sheet
	 */
	public ExcelParser(String fileName, int startLine, ArrayList<Column> columns) {
		super(fileName);
		this.startLine = startLine;
		this.columns = columns;
	}

	/**
	 * Get the startLine.
	 * 
	 * @return the number of the start line
	 */
	public int getStartLine() {
		return startLine;
	}

	/**
	 * Set startLine.
	 * 
	 * @param startLine
	 *            the number of the start line.
	 */
	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}

	/**
	 * Get Column array.
	 * 
	 * @return the column array
	 */
	public ArrayList<Column> getColumns() {
		return columns;
	}

	/**
	 * Set columns array.
	 * 
	 * @param columns
	 *            column array
	 */
	public void setColumns(ArrayList<Column> columns) {
		this.columns = columns;
	}

	/**
	 * Get FileInputStream.
	 * 
	 * @param fileName
	 *            the name of the file.
	 * @return the file.
	 * @throws FileNotFoundException
	 *             throw filenotfoundexception if file not found
	 */
	public FileInputStream openFile(String fileName)
			throws FileNotFoundException {
		return new FileInputStream(fileName);
	}

	@Override
	public void parse() throws IOException {
		// Try to open input file
		FileInputStream fis = openFile(this.getFileName());

		// Open Excel file
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		// Get number of sheets
		int numSheets = wb.getNumberOfSheets();
		for (int k = 0; k < numSheets; k++) {
			// Process a sheet
			processSheet(wb.getSheetAt(k));
		}
		wb.close();
	}

	/**
	 * Process one sheet from the excel file.
	 * 
	 * @param ws
	 *            the sheet processed.
	 */
	public void processSheet(XSSFSheet ws) {
		int rowNum = ws.getLastRowNum() + 1;
		for (int i = 0; i < rowNum; i++) {
			processRow(ws.getRow(i));

		}
	}

	/**
	 * Process one row from the sheet.
	 * 
	 * @param row
	 *            the row processed.
	 */
	public void processRow(XSSFRow row) {
		int numcells = row.getLastCellNum();
		String[] cells = new String[numcells];
		for (int c = 0; c < numcells; c++) {
			cells[c] = row.getCell(c).toString();
		}
		String[] res = splitLine(cells);
		// TODO insert splitted string into db.
		System.out.println(res[0] + " " + res[1] + " " + res[2]);
	}

	/**
	 * Get relevant cells based on columns array.
	 * 
	 * @param cells
	 *            the cells from the excel sheet
	 * @return relevant cells based on columns array
	 */
	public String[] splitLine(String[] cells) {
		String[] res = new String[columns.size()];
		try {
			for (int i = 0; i < columns.size(); i++) {
				res[i] = cells[columns.get(i).getColumnNumber() - 1];
			}
			return res;
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException();
		}
	}

}
