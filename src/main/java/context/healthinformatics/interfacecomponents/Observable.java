package context.healthinformatics.interfacecomponents;

/**
 * Interface for Observable.
 */
public interface Observable {

	/**
	 * notify all observers.
	 * @param param parameter.
	 */
	void notifyObservers(String param);
}
