package context.healthinformatics.analyse;


import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

import context.healthinformatics.database.MergeTable;
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
			String[] splittedLine = sc.nextLine().split(" ");
			splittedLine = checkSplittedLineForUnwantedSpaces(splittedLine);
			Task task = createTask(splittedLine[0]);
			if (task != null) { //task == null if revert / undo was called
				task.run(splittedLine);
				tasks.push(task);
			} 
		}
		sc.close();
	}
	
	private Task createTask(String key) {
		if (key.equals("chunk")) {
			return new Chunking();
		}
		if (key.equals("filter")) {
			return new Constraints();
		}
//		if (key.equals("code")) {
//			return new Codes();
//		}
//		if (key.equals("commment")) {
//			return new Comments();
//		}
//		if (key.equals("connect")) {
//			return new Connections();
//		}
			
		return null;
	}

	/**
	 * getter for the current chunkList.
	 * @return the current chunkList
	 */
	public ArrayList<Chunk> getChunks() {
		if (!tasks.isEmpty()) {
			return tasks.peek().getChunks();
		}
		else {
			//TODO first workspace.
			return null;
		}
		
	}
	
	private String[] checkSplittedLineForUnwantedSpaces(String[] splittedLine) {
		ArrayList<String> strings = new ArrayList<String>();
		for (int i = 0; i < splittedLine.length; i++) {
			if (splittedLine[i].length() > 0) {
				strings.add(splittedLine[i]);
			}
		}
		String[] ans  = new String[strings.size()];
		for (int i = 0; i < strings.size(); i++) {
			ans[i] = strings.get(i);
		}
		return ans;
	}

}
