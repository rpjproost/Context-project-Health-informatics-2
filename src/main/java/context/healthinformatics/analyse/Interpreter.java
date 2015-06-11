package context.healthinformatics.analyse;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

import context.healthinformatics.interfacecomponents.Observable;
import context.healthinformatics.interfacecomponents.Observer;
import context.healthinformatics.sequentialdataanalysis.Chunk;
import context.healthinformatics.sequentialdataanalysis.Chunking;
import context.healthinformatics.sequentialdataanalysis.Codes;
import context.healthinformatics.sequentialdataanalysis.Comments;
import context.healthinformatics.sequentialdataanalysis.Comparison;
import context.healthinformatics.sequentialdataanalysis.Computations;
import context.healthinformatics.sequentialdataanalysis.Connections;
import context.healthinformatics.sequentialdataanalysis.Constraints;
import context.healthinformatics.sequentialdataanalysis.Task;


/**
 * class handling the interpreting of the user input.
 */
public class Interpreter implements Observer {
	
	private Stack<Task> tasks;
	private ArrayList<Chunk> firstList;
	private String name;
	/**
	 * constructor for the Interpreter.
	 * @param name name of this interpreter.
	 */
	protected Interpreter(String name) {
		tasks = new Stack<Task>();
		this.name = name;
	}

	/**
	 * method for interpreting the things to do.
	 * 
	 * @param code
	 *            code to interpret.
	 * @throws Exception Throws Exception when the
	 * 				code can not be parsed.
	 */
	public void interpret(String code) throws Exception {
		Scanner sc = new Scanner(code);
		
		while (sc.hasNextLine()) {
			Query query = new Query(sc.nextLine());
			Task task = createTask(query);
			if (task != null) { //task == null if revert / undo was called
				task.run(query);
				tasks.push(task);
			} else if (query.part().equals("undo") || query.part().equals("revert")) {
				undo();
			}
		}
		sc.close();
	}
	
	/**
	 * method to create the correct task.
	 * @param k key to check witch task to create.
	 * @param parameter parameter for the task.
	 * @return the newly created task.
	 */
	private Task createTask(Query q) {
		String key = q.part().toLowerCase();
		String parameter = q.getParameter();
		if (key.equals("chunk")) {
			if (parameter != null) {
				return new Chunking(parameter);
			}
			return new Chunking();
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
		if (key.equals("compare")) {
			return new Comparison();
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
	 * setter for the initial list of chunks.
	 * @param list list to set
	 */
	public void setIntialChunks(ArrayList<Chunk> list) {
		firstList = list;
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
	
	
	/**
	 * getter for the name of the interpreter.
	 * @return the name of the interpreter.
	 */
	public String getName() {
		return name;
	}

	@Override
	public void update(String param) {
		SingletonInterpreter.update(param);
	}
	
	@Override
	public void observe(Observable o) {
		o.subscribe(this);
	}
	
	/**
	 * counts all the codes from the last chunkList.
	 * @return  a Hashmap with codes as keys, and values as ints.
	 */
	public HashMap<String, Integer> countCodes() {
		HashMap<String, Integer> res = new HashMap<String, Integer>();
		String code;
		for (Chunk c : getChunks()) {
			code = c.getCode();
			if (code != null) {
				if (res.containsKey(code)) {
					res.put(code, res.get(code) + 1);
				} else {
					res.put(code, 1);
				}
			}
		}
		return res;
	}
}
