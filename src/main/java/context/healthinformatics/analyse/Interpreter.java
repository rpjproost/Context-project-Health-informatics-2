package context.healthinformatics.analyse;


import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import context.healthinformatics.sequentialdataanalysis.Chunk;
import context.healthinformatics.sequentialdataanalysis.Chunking;
import context.healthinformatics.sequentialdataanalysis.Codes;
import context.healthinformatics.sequentialdataanalysis.Comments;
import context.healthinformatics.sequentialdataanalysis.Computations;
import context.healthinformatics.sequentialdataanalysis.Connections;
import context.healthinformatics.sequentialdataanalysis.Constraints;
import context.healthinformatics.sequentialdataanalysis.Task;


/**
 * class handling the interpreting of the user input.
 */
public class Interpreter {
	
	private Stack<Task> tasks;
	private ArrayList<Chunk> firstList;
	/**
	 * constructor for the Interpreter.
	 */
	protected Interpreter() {
		tasks = new Stack<Task>();
	}

	/**
	 * method for interpreting the things to do.
	 * 
	 * @param code
	 *            code to interpret.
	 * @throws Exception 
	 */
	public void interpret(String code) throws Exception {
		Scanner sc = new Scanner(code);
		
		while (sc.hasNextLine()) {
			ArrayList<String> strings = new ArrayList<String>();
			String parameter = splitLine(strings, sc);
			strings = checkSplittedLineForUnwantedSpaces(strings);
			Task task = createTask(strings.get(0), parameter);
			if (task != null) { //task == null if revert / undo was called
				task.run(buildStringArray(strings));
				tasks.push(task);
			} else if (strings.get(0).equals("undo") || strings.get(0).equals("revert")) {
				undo();
			}
		}
		sc.close();
	}
	
	/**
	 * Method to handle the splitting of the line.
	 * @param a new list for the splitted strings.
	 * @return the parameter found.
	 * @throws Exception 
	 */
	private String splitLine(ArrayList<String> strings, Scanner sc) throws Exception {
		String[] splittedParameterLine = splitParameter(sc.nextLine());
		String parameter = null;
		addStrings(splittedParameterLine[0].split(" "), strings);
		try {
			if (splittedParameterLine.length > 1) {
				addStrings(splittedParameterLine[2].split(" "), strings);
				parameter = splittedParameterLine[1];
			}
		} catch (Exception e) {
			sc.close();
			throw new Exception(e.getMessage());
		}
		return parameter;
	}
	
	/**
	 * method to create the correct task.
	 * @param k key to check witch task to create.
	 * @param parameter parameter for the task.
	 * @return the newly created task.
	 */
	private Task createTask(String k, String parameter) {
		String[] keys = k.split("\\(");
		String key = keys[0];
		if (key.equals("chunk")) {
			if (parameter != null) {
				return new Chunking(parameter);
			} else {
				return new Chunking();
			}
		}
		if (key.equals("filter")) {
			return new Constraints();
		}
		if (key.equals("code")) {
			return new Codes(parameter);
		}
		if (key.equals("comment")) {
			return new Comments(parameter);
		}
		if (key.equals("connect")) {
			return new Connections(parameter);
		}
		if (key.equals("compute")) {
			return new Computations();
		}
			
			
		return null;
	}

	/**
	 * getter for the current chunkList.
	 * @return the current chunkList
	 */
	public ArrayList<Chunk> getChunks() {
		if (!tasks.isEmpty()) {
			return tasks.peek().getResult();
		}
		else {
			return firstList;
		}
		
	}
	
	/**
	 * checks the splitted line for empty strings in case of dubble " ".
	 * @param splittedLine ArrayList of strings with the splitted line.
	 * @param 
	 * @return
	 */
	private ArrayList<String> checkSplittedLineForUnwantedSpaces(ArrayList<String> splittedLine) {
		ArrayList<String> strings = new ArrayList<String>();
		for (String split : splittedLine) {
			if (split.length() > 0) {
				strings.add(split);
			}
		}
		return strings;
	}
	
	/**
	 * setter for the initial list of chunks.
	 * @param list list to set
	 */
	public void setIntialChunks(ArrayList<Chunk> list) {
		firstList = list;
	}
	
	/**
	 * split the line to get the parameter out.
	 * @param line line to split
	 * @return the splitted line;
	 */
	private String[] splitParameter(String line) {
		ArrayList<String> strings = new ArrayList<String>();
		int fp = findChar(line, "(");
		int sp = findChar(line, ")");
		if (fp == -1) { //geen haakjes
			strings.add(line);
			return buildStringArray(strings);
		}
		strings.add(line.substring(0, fp));
		strings.add(line.substring(fp + 1, sp));
		strings.add(line.substring(sp + 1, line.length()));
		return buildStringArray(strings);
	}
	
	/**
	 * builds an array from strings from an ArrayList of Strings.
	 * @param strings arrayList to build from.
	 * @return the created String array.
	 */
	private String[] buildStringArray(ArrayList<String> strings) {
		String[] ans  = new String[strings.size()];
		for (int i = 0; i < strings.size(); i++) {
			ans[i] = strings.get(i);
		}
		return ans;
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
	 * deep coppy a list of chunks.
	 * @param list list to copy.
	 * @return the copy.
	 */
	public ArrayList<Chunk> copyChunks(ArrayList<Chunk> list) {
		ArrayList<Chunk> ans = new ArrayList<Chunk>();
		for (Chunk c : list) {
			ans.add(c.copy());
		}
		return ans;
	}
	
	/**
	 * undo last task.
	 */
	private void undo() {
		if (!tasks.isEmpty()) {
			tasks.pop().undo();
		}
	}
	
	private int findChar(String string, String cha) {
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == cha.charAt(0)) {
				return i;
			}
		}
		return -1;
	}

}
