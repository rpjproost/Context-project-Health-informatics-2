package context.healthinformatics.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


import context.healthinformatics.help.HelpFrameInfoContainer;

/**
 * Class which reads a text file and makes a list of HelpFrameInfoContainers.
 */
public class ReadHelpInfoFromTXTFile extends Parser {

	private File file;
	private Scanner sc;
	private ArrayList<HelpFrameInfoContainer> helpFrameInfoContainer;

	/**
	 * Constructor of the ReadHelpInfoFromTXTFile.
	 * 
	 * @param fileName
	 *            the name of the file
	 */
	public ReadHelpInfoFromTXTFile(String fileName) {
		super(fileName);
		helpFrameInfoContainer = new ArrayList<HelpFrameInfoContainer>();
	}
	
	/**
	 * Read all lines from a text file and build a string out of it.
	 * 
	 * @return the string with all lines
	 * @throws FileNotFoundException
	 *             error file not found
	 */
	public String readByLine() throws FileNotFoundException {
		this.file = openFile(this.getFileName());
		this.sc = new Scanner(file, "UTF-8");
		StringBuilder buildString = new StringBuilder();
		while (sc.hasNextLine()) {
			buildString.append(sc.nextLine());
			buildString.append(System.getProperty("line.separator"));
		}
		return buildString.toString();
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

	@Override
	public void parse() throws IOException {
		try {
			this.file = openFile(this.getFileName());
			this.sc = new Scanner(file, "UTF-8");
			readRelevantLines();
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("The TXT file was not found!");
		}
		sc.close();
	}

	/**
	 * Initialize variables to read the lines.
	 */
	private void readRelevantLines() {
		StringBuilder buildInfoString = new StringBuilder();
		String title = "";
		int count = 0;
		readLines(title, count, buildInfoString);
	}

	/**
	 * Read the actual lines.
	 * 
	 * @param title
	 *            the title of the info
	 * @param count
	 *            count how many lines are read.
	 * @param buildInfoString
	 *            the string with HTML info
	 */
	private void readLines(String title, int count,
			StringBuilder buildInfoString) {
		while (sc.hasNextLine()) {
			String thisLine = sc.nextLine();
			if (!sc.hasNextLine()) {
				helpFrameInfoContainer.add(new HelpFrameInfoContainer(title,
						buildInfoString.toString()));
			} else if (thisLine.equals("%%")) {
				if (count > 0) {
					helpFrameInfoContainer.add(new HelpFrameInfoContainer(
							title, buildInfoString.toString()));
				}
				count++;
				title = sc.nextLine();
				buildInfoString.setLength(0);
			} else {
				buildInfoString.append(thisLine);
			}
		}
	}

	/**
	 * Get the ArrayList of all HelpFrameInfoContainers.
	 * 
	 * @return the ArrayList
	 */
	public ArrayList<HelpFrameInfoContainer> getHelpFrameInfoContainer() {
		return this.helpFrameInfoContainer;
	}
}
