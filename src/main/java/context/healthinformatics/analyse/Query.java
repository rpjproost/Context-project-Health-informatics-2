package context.healthinformatics.analyse;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * class containing the user query for parsing.
 */
public class Query {
	private ArrayList<String> query;
	private String parameter;
	private int index;
	
	/**
	 * Constructor for a query.
	 * @param line is the String with the query.
	 */
	public Query(String line) {
		index = 0;
		parameter = "";
		query = splitLine(line);
	}
	
	/**
	 * empty constructor for testing purposes.
	 */
	protected Query() { }
	
	
	/**
	 * Method to handle the splitting of the line.
	 * @param a new list for the splitted strings.
	 * @return the parameter found.
	 * @throws Exception Might throw an index out of bounds exception.
	 */
	private ArrayList<String> splitLine(String inputLine) {
		String line = splitParameter(inputLine);
		ArrayList<String> parts = new ArrayList<String>();
		addStrings(line.split(" "), parts);
		checkSplittedLineForUnwantedSpaces(parts);
		return parts;
	}
	
	/**
	 * checks the splitted line for empty strings in case of dubble " ".
	 * @param splittedLine ArrayList of strings with the splitted line.
	 * @return A list of strings without empty places.
	 */
	private void checkSplittedLineForUnwantedSpaces(ArrayList<String> splittedLine) {
		int i = 0;
		while (i < splittedLine.size()) {
			if (!(splittedLine.get(i).length() > 0)) {
				splittedLine.remove(i);
			} else {
				i++;
			}
		}
	}
	
	/**
	 * split the line to get the parameter out.
	 * @param line line to split
	 * @return the line without the parameter, parameter is now set.;
	 */
	private String splitParameter(String line) {
		int fp = findChar(line, "(");
		int sp = findChar(line, ")");
		if (fp != -1 && checkForCommand(line.substring(0, fp))) { //has parameter
			parameter = line.substring(fp + 1, sp);
			return line.substring(0, fp) + line.substring(sp + 1, line.length());
		} else {
			return line;
		}
	}
	
	
	/**
	 * adds all strings from the string array to the list.
	 * @param array array of strings to add
	 * @param list list to add the strings to.
	 */
	private void addStrings(String[] array, ArrayList<String> list) {
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
	}
	
	/**
	 * find char in a string.
	 * @param string string to find for
	 * @param cha character (first cha of string)
	 * @return the location of the cha in the string.
	 */
	private int findChar(String string, String cha) {
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == cha.charAt(0)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * checks if the string given is 1 word.
	 * @param s given string
	 * @return boolean containing the answer. True for 1 word. False for more then 1 word.
	 */
	protected boolean checkForCommand(String s) { //checks if the string is 1 word.
		return Pattern.matches("^\\s*[A-Za-z]+\\s*$", s);
	}
	
	/**
	 * set the queryPointer to the int.
	 * does nothing if outOfBounds.
	 * @param i int to point to.
	 */
	public void setPointer(int i) {
		if (i >= 0 && i < query.size()) {
			index = i;
		}
	}
	/**
	 * returns the current queryPart.
	 * @return string containing the current query part.
	 */
	public String part() {
		return query.get(index);
	}
	
	/**
	 * returns the parameter for this query.
	 * @return the parameter.
	 */
	public String getParameter() {
		return parameter;
	}
	
	/**
	 * increment index with 1.
	 */
	public void inc() {
		setPointer(index + 1);
	}
	
	/**
	 * increment index with given number.
	 * @param i number to add.
	 */
	public void inc(int i) {
		setPointer(index + i);
	}
	
	/**
	 * returns true if there is a next queryPart.
	 * @return true or false.
	 */
	public boolean hasNext() {
		return index + 1 < query.size();
	}
	
	/**
	 * return the next Querypart. 
	 * @return the next part of the query.
	 */
	public String next() {
		inc();
		return part();
	}
}
