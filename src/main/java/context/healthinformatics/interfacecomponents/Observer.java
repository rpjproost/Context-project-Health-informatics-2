package context.healthinformatics.interfacecomponents;

/**
 * Observer Interface.
 */
public interface Observer {
	/**
	 * updates the observer. Is called by the Observable.
	 * @param param String with the parameter.
	 */
	void update(String param);
}
