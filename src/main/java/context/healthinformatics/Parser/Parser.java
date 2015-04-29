package context.healthinformatics.Parser;

/**
 * Abstract class to specify what other Parsers must override.
 * And every Parser needs a file name to read from.
 */
public abstract class Parser {
	
	private String filename;
	
	/**
	 * Create a undefined Parser.
	 * @param fileName the source path where the file is located.
	 */
	public Parser(String fileName) {
		setFileName(fileName);
	}

	/**
	 * Change the source path.
	 * @param fileName the new source path to a file.
	 */
	public void setFileName(String fileName) {
		filename = fileName;
	}
	
	/**
	 * @return the source path of the file
	 */
	protected String getFileName() {
		return filename;
	}
	
	/**
	 * Abstract method which children must override.
	 */
	public abstract void parse();
}
