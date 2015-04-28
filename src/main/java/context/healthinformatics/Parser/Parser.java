package context.healthinformatics.Parser;

public abstract class Parser {
	
	String filename;
	
	public Parser (String fileName) {
		filename = fileName;
	}

	public void setFileName(String fileName) {
		filename = fileName;
	}
	
	public abstract void parse();
}
