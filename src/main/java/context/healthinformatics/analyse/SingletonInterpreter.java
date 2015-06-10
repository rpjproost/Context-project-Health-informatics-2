package context.healthinformatics.analyse;

import java.util.HashMap;

import context.healthinformatics.interfacecomponents.Observable;
import context.healthinformatics.interfacecomponents.Observer;
/**
 * The SingleTonInterpreter class.
 */
public final class SingletonInterpreter {

	private static HashMap<String, Interpreter > interpreters = new HashMap<String, Interpreter>();
	private static String project = "analyse";

	/**
	 * method to defeat the constructor.
	 */
	private SingletonInterpreter() {

	}

	/**
	 * method returning the Interpreter.
	 * @param name the name of the interpreter.
	 * @return the Interpreter.
	 */
	private static Interpreter getInterpreter(String name) {
		if (!interpreters.containsKey(name)) {
			interpreters.put(name, createInterpreter(name));
		}
		return interpreters.get(name);
	}

	private static synchronized Interpreter createInterpreter(String name) {
		if (!interpreters.containsKey(name)) {
			return new Interpreter(name);
		}
		return interpreters.get(name);
	}
	
	/**
	 * method returning the default Interpreter.
	 * @return the Interpreter.
	 */
	public static Interpreter getInterpreter() {
		return getInterpreter(project);
	}

	static protected void update(String param) {
		project = param;
	}
}
