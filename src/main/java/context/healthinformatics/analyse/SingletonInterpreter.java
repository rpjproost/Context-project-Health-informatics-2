package context.healthinformatics.analyse;

import java.util.HashMap;

/**
 * The SingleTonInterpreter class.
 */
public final class SingletonInterpreter {

	private static HashMap<String, Interpreter > interpreters = new HashMap<String, Interpreter>();

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
	public static synchronized Interpreter getInterpreter(String name) {
		if (!interpreters.containsKey(name)) {
			interpreters.put(name, createInterpreter(name));
		}
		return interpreters.get(name);
	}

	private static Interpreter createInterpreter(String name) {
		return new Interpreter(name);
	}
	
	/**
	 * method returning the default Interpreter.
	 * @return the Interpreter.
	 */
	public static Interpreter getInterpreter() {
		return getInterpreter("analyse");
	}
}
