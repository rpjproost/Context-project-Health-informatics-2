package context.healthinformatics;

import java.io.IOException;

import context.healthinformatics.Parser.*;

public class Main {

	public static void main(String[] args) throws IOException {
		XMLParser xmlp = new XMLParser("src/main/data/ExampleStatSensor.xml");
		xmlp.parse();
		TXTParser txtp = new TXTParser(xmlp.getPath(),xmlp.getStartLine(),xmlp.getDelimiter(), xmlp.getColumns());
		txtp.parse();
	}
}
