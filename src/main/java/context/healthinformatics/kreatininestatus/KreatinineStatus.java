package context.healthinformatics.kreatininestatus;

/**
 * abstract class containing the Crea value.
 */
public abstract class KreatinineStatus {

	/**
	 * Method to generate the advise over 2 days.
	 * @param yesterday status of yesterday.
	 * @return the advise for this date or <code>null</code> if no advise could be generated.
	 */
	public abstract String getAdvice(KreatinineStatus yesterday);
	
	/**
	 * gets the advise if today was concern.
	 * @return the advise.
	 */
	protected abstract String getConcernAdvice();
	
	/**
	 * gets the advise if today was mild concern.
	 * @return the advise.
	 */
	protected abstract String getMildConcernAdvice();
	
	/**
	 * gets the advise if today was reasonably safe.
	 * @return the advise.
	 */
	protected abstract String getReasonableAdvice();
	
	/**
	 * gets the advise if today was safe.
	 * @return the advise.
	 */
	protected abstract String getSafeAdvice();
	
	/**
	 * return the status of this day.
	 * @param seccondStatus second measurement.
	 * @return the status.
	 */
	public abstract KreatinineStatus getStatus(KreatinineStatus seccondStatus);
	
	/**
	 * returns the status.
	 * @param firstStatus concern status to calculate for.
	 * @return the status.
	 */
	protected abstract KreatinineStatus getStatus(ConcernStatus firstStatus);
	
	/**
	 * returns the status.
	 * @param firstStatus mild concern status to calculate for.
	 * @return the status.
	 */
	protected abstract KreatinineStatus getStatus(MildConcernStatus firstStatus);
	
}
