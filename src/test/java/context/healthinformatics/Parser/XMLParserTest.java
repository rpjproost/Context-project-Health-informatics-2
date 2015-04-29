package context.healthinformatics.Parser;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class XMLParserTest {
	XMLParser xmlp;
	
	@Before
	public void before() {
		xmlp = new XMLParser("");
	}
	
	@Test
	public void correctTest() {
		xmlp.setFileName("src/test/data/goodXML.xml");
		xmlp.parse();
		assertEquals(xmlp.getDelimiter(), ",");
		assertEquals(xmlp.getDocName(), "StatSensor");
		assertEquals(xmlp.getPath(), "data/xxx.txt");
		assertEquals(xmlp.getStartLine(), 7);
		
		ArrayList<Column> cols = new ArrayList<Column>();
		cols.add(new Column(2,"value","Integer"));
		cols.add(new Column(5,"date","String"));
		cols.add(new Column(6,"time","String"));
		assertTrue(compare(xmlp.getColumns(), cols));
	}
		
	@Test(expected=NullPointerException.class)
	public void emptyFieldTest() {
		xmlp.setFileName("src/test/data/emptyField.xml");
		xmlp.parse();
	}

	@Test(expected=NullPointerException.class)
	public void noDelimiterTest() {
		xmlp.setFileName("src/test/data/noDelimiter.xml");
		xmlp.parse();
	}
	
	@Test(expected=NumberFormatException.class)
	public void noIDsTest() {
		xmlp.setFileName("src/test/data/noIDs.xml");
		xmlp.parse();
	}
	
	@Test(expected=NullPointerException.class)
	public void noPathTest() {
		xmlp.setFileName("src/test/data/noPath.xml");
		xmlp.parse();
	}
	
	@Test
	public void noStartTest() {
		xmlp.setFileName("src/test/data/noStart.xml");
		xmlp.parse();
		assertTrue((xmlp.getStartLine() == 1));
		
	}
	
	@Test(expected=FileNotFoundException.class)
	public void fileNotFoundTest() {
		xmlp.setFileName("yolo.xml");
		xmlp.parse();
	}
	
	public boolean compare(ArrayList<Column> l1, ArrayList<Column> l2){
		if(l1.size() == l2.size()) {
			for(int i = 0; i < l1.size(); i++){
				if(!l1.get(i).equals(l2.get(i)))
					return false;
			}
		}
		return true;
	}
}
