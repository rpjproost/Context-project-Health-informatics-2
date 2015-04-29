package context.healthinformatics;

import context.healthinformatics.Parser.*;

public class Main {

	public static void main(String[] args) {
		XMLParser xmlp = new XMLParser("src/main/data/ExampleStatSensor.xml");
		xmlp.parse();
		TXTParser txtp = new TXTParser("src/main/data/ExampleTXT.txt",5,",");
		txtp.parse();
	}
}
