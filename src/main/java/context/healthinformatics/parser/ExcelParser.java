package context.healthinformatics.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import context.healthinformatics.database.Db;
import context.healthinformatics.database.SingletonDb;

/**
 * The ExcelParser class. Parses a Excel sheet.
 *
 */
public class ExcelParser extends Parser {
	private int startLine;
	private ArrayList<Column> columns;
	private int sheet;
	private String docName;
	private Logger log = Logger.getLogger(ExcelParser.class.getName());

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
	 *            the sheet in de excel file to be processed. Sheet int 1 is
	 *            first sheet.
	 * @param docName
	 *            the name of the document which is also the name of the table
	 *            to be inserted to
	 */
	public ExcelParser(String fileName, int startLine,
			ArrayList<Column> columns, int sheet, String docName) {
		super(fileName);
		this.startLine = startLine;
		this.columns = columns;
		this.sheet = sheet;
		this.docName = docName;
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
	private FileInputStream openFile(String fileName)
			throws FileNotFoundException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("The Excel file was not found!");
		}
		return fis;
	}

	/**
	 * Parse an excel file. Checks if we deal with an .xls or .xlsx file,
	 * because they are handled differently.
	 * 
	 * @throws IOException
	 *             if the excel file was not found
	 */
	@Override
	public void parse() throws IOException {
		FileInputStream fis = openFile(this.getFileName());
		if (this.getFileName().endsWith(".xls")) {
			// Open .xls file
			Workbook wb = null;
			try {
				wb = WorkbookFactory.create(fis);
			} catch (InvalidFormatException e) {
				throw new IOException("xls file could not be read");
			}
			processXLSSheet(wb.getSheetAt(sheet - 1));
			wb.close();
		} else {
			// Open .xlsx file
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			processXLSXSheet(wb.getSheetAt(sheet - 1));
			wb.close();
		}
	}

	/**
	 * Process one sheet from the excel file.
	 * 
	 * @param sheet
	 *            the sheet processed.
	 * @throws IOException
	 *             the exception for sql
	 */
	private void processXLSSheet(Sheet sheet) throws IOException {
		for (int i = startLine; i < sheet.getLastRowNum() + 1; i++) {
			processXLSRow(sheet.getRow(i));
		}
	}

	/**
	 * Process one sheet from the excel file.
	 * 
	 * @param ws
	 *            the sheet processed.
	 * @throws IOException
	 *             the exception for sql
	 */
	private void processXLSXSheet(XSSFSheet sheet) throws IOException {
		for (int i = startLine; i < sheet.getLastRowNum() + 1; i++) {
			if (!isRowEmpty(sheet.getRow(i))) {
				processXLSXRow(sheet.getRow(i));
			}

		}
	}

	/**
	 * Process one row from the sheet.
	 * 
	 * @param row
	 *            the row processed.
	 * @throws IOException
	 *             the exception for sql
	 */
	private void processXLSRow(Row row) throws IOException {
		String[] cells = new String[columns.size()];
		if (canSplit(row.getLastCellNum())) {
			for (int c = 0; c < columns.size(); c++) {
				cells[c] = processCellXLS(
						row.getCell(columns.get(c).getColumnNumber() - 1), c);
			}
			insertToDb(cells);
		}
	}

	/**
	 * Process one row from the sheet.
	 * 
	 * @param row
	 *            the row processed.
	 * @throws IOException
	 *             the exception for sql
	 */
	private void processXLSXRow(XSSFRow row) throws IOException {
		String[] cells = new String[columns.size()];
		if (canSplit(row.getLastCellNum())) {
			for (int c = 0; c < columns.size(); c++) {
				cells[c] = processCellXLSX(
						row.getCell(columns.get(c).getColumnNumber() - 1), c);
			}
			insertToDb(cells);
		}
	}

	/**
	 * Process a single xls cell.
	 * 
	 * @param curCell
	 *            the current cell to be processed
	 * @param c
	 *            the integer of the current column
	 * @return the right formatted cell
	 */
	private String processCellXLS(Cell curCell, int c) {
		if (columns.get(c).getColumnType().equals("INT")) {
			return formatInt(curCell);
		} else if (columns.get(c).getColumnType().equals("DATE")
				&& !curCell.toString().equals("")) {
			return formatXLSDate(curCell, c);
		} else {
			return curCell.toString();
		}
	}

	/**
	 * Process a single cell.
	 * 
	 * @param curCell
	 *            the current cell to be processed
	 * @param c
	 *            the integer of the current column
	 * @return the right formatted cell
	 */
	private String processCellXLSX(Cell curCell, int c) {
		if (columns.get(c).getColumnType().equals("Int")) {
			return formatInt(curCell);
		} else if (columns.get(c).getColumnType().equals("DATE")
				&& !curCell.toString().equals("")) {
			return new SimpleDateFormat(columns.get(c).getDateType())
					.format(curCell.getDateCellValue());
		} else if (columns.get(c).getColumnType().equals("DATE")) {
			log.info("this cell could not be parsed to a date: '" + "' in column: " + c
					+ " in file: " + docName);
			return new SimpleDateFormat(columns.get(c).getDateType())
			.format(new Date());
		}
		else {
			return curCell.toString();
		}
	}

	/**
	 * Format int for database.
	 * 
	 * @param curCell
	 *            current cell
	 * @return if not a double then -1 else the cell
	 */
	private String formatInt(Cell curCell) {
		if (isDouble(curCell.toString())) {
			return curCell.toString();
		} else {
			return "-1";
		}
	}

	/**
	 * Format a date cell for the database.
	 * 
	 * @param curCell
	 *            the current cell
	 * @param c
	 *            the current column
	 * @return the right formatted string
	 */
	private String formatXLSDate(Cell curCell, int c) {
		Date date = null;
		try {
			date = new SimpleDateFormat(columns.get(c).getDateType())
					.parse(curCell.toString());
		} catch (ParseException e) {
			return new SimpleDateFormat(columns.get(c).getDateType()).format(new Date());
		}
		return new SimpleDateFormat(columns.get(c).getDateType()).format(date);
	}

	/**
	 * Check if a string is a double.
	 * 
	 * @param str
	 *            the string to be checked
	 * @return true if is double else false
	 */
	private boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * If there are enough cells in the sheet returns true.
	 * 
	 * @param cellnums
	 *            the number of cells on a row
	 * @return true if there are enough cells
	 */
	private boolean canSplit(int cellnums) {
		return cellnums >= columns.size();
	}

	/**
	 * Check if a row is empty.
	 * 
	 * @param row
	 *            the row
	 * @return true if all columns are type blank
	 */
	private static boolean isRowEmpty(Row row) {
		for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
			Cell cell = row.getCell(c);
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Insert a row to the db.
	 * 
	 * @param cells
	 *            the string array with the cell values
	 * @throws IOException
	 *             the exception if data can not be inserted in database
	 */
	private void insertToDb(String[] cells) {
		Db data = SingletonDb.getDb();
		try {
			data.insert(docName, cells, columns);
		} catch (SQLException e) {
			StringBuilder builder = new StringBuilder();
			builder.append("Error inserting ");
			builder.append(printCells(cells));
			builder.append(" INTO ").append(docName);
			log.info(builder.toString());
		}
	}

	/**
	 * Test function to return the parsed line as a string.
	 * 
	 * @param cells
	 *            the splitted line
	 * @return the string build
	 */
	public String printCells(String[] cells) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cells.length; i++) {
			sb.append("|");
			sb.append(cells[i]);
			sb.append("|");
		}
		return sb.toString();
	}
}