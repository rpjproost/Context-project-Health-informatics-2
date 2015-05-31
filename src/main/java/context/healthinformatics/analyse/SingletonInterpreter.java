package context.healthinformatics.analyse;

/**
 * The SingleTonInterpreter class.
 */
public final class SingletonInterpreter {

	private static Interpreter interpreter = createInterpreter();

	/**
	 * method to defeat the constructor.
	 */
	private SingletonInterpreter() {

	}

	/**
	 * method returning the Interpreter.
	 * 
	 * @return the Interpreter.
	 */
	public static Interpreter getInterpreter() {
		return interpreter;
	}

	private static Interpreter createInterpreter() {
		return new Interpreter();
	}
}
