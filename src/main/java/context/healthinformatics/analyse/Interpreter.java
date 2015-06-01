package context.healthinformatics.analyse;


import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import org.parboiled.Parboiled;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.parboiled.support.ParsingResult;

import context.healthinformatics.database.MergeTable;
import context.healthinformatics.sequentialdataanalysis.Chunk;
import context.healthinformatics.sequentialdataanalysis.Task;


/**
 * class handling the interpreting of the user input.
 */
public class Interpreter {
	
	private Stack<Task> tasks;
	private UserInputParser parser;

	/**
	 * constructor for the Interpreter.
	 */
	protected Interpreter() {
		tasks = new Stack<Task>();
		parser = Parboiled.createParser(UserInputParser.class);
	}

	/**
	 * method for interpreting the things to do.
	 * 
	 * @param code
	 *            code to interpret.
	 */
	public void interpret(String code) {
		Scanner sc = new Scanner(code);
		
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			ParsingResult<?> result = new RecoveringParseRunner<Task>(parser.task())
					.run(line);
			Task task = (Task) result.resultValue;
			task.run();
			tasks.push(task);
		}
		sc.close();
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

}
