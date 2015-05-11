package context.healthinformatics.Parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
	private int sheet;

	/**
	 * Constructor for the ExcelParser.
	 * 
	 * @param fileName
	 *            the filename of the excel file to be processed
	 * @param startLine
	 *            the number of the start line of the excel sheet
	 * @param columns
	 *            the relevant columns in the excel sheet
	 * @param sheet
	 *            the sheet in de excel file to be processed. Sheet int 1 is first sheet.
	 */
	public ExcelParser(String fileName, int startLine,
			ArrayList<Column> columns, int sheet) {
		super(fileName);
		this.startLine = startLine;
		this.columns = columns;
		this.sheet = sheet;
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
		if (this.getFileName().endsWith(".xls")) {
			// Open xls file
			Workbook wb = null;
			try {
				wb = WorkbookFactory.create(fis);
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Process sheet sheet
			processXLSSheet(wb.getSheetAt(sheet - 1));
			wb.close();
		} else {
			// Open Excel file
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			// Process sheet sheet
			processXLSXSheet(wb.getSheetAt(sheet - 1));
			wb.close();
		}

	}

	/**
	 * Process one sheet from the excel file.
	 * 
	 * @param sheet2
	 *            the sheet processed.
	 */
	public void processXLSSheet(Sheet sheet2) {
		int rowNum = sheet2.getLastRowNum() + 1;
		for (int i = 0; i < rowNum; i++) {
			processXLSRow(sheet2.getRow(i));
		}
	}

	/**
	 * Process one row from the sheet.
	 * 
	 * @param row
	 *            the row processed.
	 */
	public void processXLSRow(Row row) {
		int numcells = row.getLastCellNum();
		String[] cells = new String[numcells];
		for (int c = 0; c < numcells; c++) {
			cells[c] = row.getCell(c).toString();
			System.out.println(cells[c]);
		}
		// String[] res = splitLine(cells);
		// TODO insert splitted string into db.
	}

	/**
	 * Process one sheet from the excel file.
	 * 
	 * @param ws
	 *            the sheet processed.
	 */
	public void processXLSXSheet(XSSFSheet ws) {
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
		// String[] res = splitLine(cells);
		// TODO insert splitted string into db.
	}

}