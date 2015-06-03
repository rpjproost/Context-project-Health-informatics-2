package context.healthinformatics.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import context.healthinformatics.interfacecomponents.HelpFrameInfoContainer;

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
	 * Reads the lines of the file.
	 */
	private void readRelevantLines() {
		while (sc.hasNextLine()) {
			String title = sc.nextLine();
			String info = sc.nextLine().replace("\\n",
					System.getProperty("line.separator"));
			helpFrameInfoContainer.add(new HelpFrameInfoContainer(title, info));
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
