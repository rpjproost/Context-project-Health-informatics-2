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
	
	/**
	 * lets an observer subscribe to the observable.
	 * @param o Observer who wants to subscribe.
	 */
	void subscribe(Observer o);
	
	/**
	 * lets an observer unsubscribe to the observable.
	 * @param o Observer who wants to subscribe.
	 */
	void unsubscribe(Observer o);
	
}
