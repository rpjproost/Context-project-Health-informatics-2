package context.healthinformatics.analyse;


import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import context.healthinformatics.sequentialdataanalysis.Chunk;
import context.healthinformatics.sequentialdataanalysis.Chunking;
import context.healthinformatics.sequentialdataanalysis.Codes;
import context.healthinformatics.sequentialdataanalysis.Comments;
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
			String[] splittedParameterLine = splitParameter(sc.nextLine());
			String parameter = null;
			ArrayList<String> strings = new ArrayList<String>();
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
			strings = checkSplittedLineForUnwantedSpaces(strings, new ArrayList<String>());
			Task task = createTask(strings.get(0), parameter);
			if (task != null) { //task == null if revert / undo was called
				task.run(buildStringArray(strings));
				tasks.push(task);
			} 
		}
		sc.close();
	}
	
	private Task createTask(String k, String parameter) {
		String[] keys = k.split("\\(");
		String key = keys[0];
		if (key.equals("chunk")) {
			return new Chunking();
		}
		if (key.equals("filter")) {
			return new Constraints();
		}
		if (key.equals("code")) {
			return new Codes(parameter);
		}
		if (key.equals("commment")) {
		//	return new Comments(parameter);
		}
		if (key.equals("connect")) {
			return new Connections();
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
	
	private ArrayList<String> checkSplittedLineForUnwantedSpaces(ArrayList<String> splittedLine,
			ArrayList<String> strings) {
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
	
	private String[] splitParameter(String line) {
		ArrayList<String> strings = new ArrayList<String>();
		String[] splittedLine = line.split("\\(");
		strings.add(splittedLine[0]);
		if (splittedLine.length > 1) { // if this line has a parameter:
			String[] nextPart = splittedLine[1].split("\\)");
			strings.add(nextPart[0]); //add parameter.
			strings.add(nextPart[1]); //add rest of line.
		}
		return buildStringArray(strings);
	}
	
	private String[] buildStringArray(ArrayList<String> strings) {
		String[] ans  = new String[strings.size()];
		for (int i = 0; i < strings.size(); i++) {
			ans[i] = strings.get(i);
		}
		return ans;
	}
	
	private void addStrings(String[] array, ArrayList<String> list) {
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
	}

}
