package context.healthinformatics.kreatininestatus;

import java.util.Date;
/**
 * abstract class containing the Crea value.
 */
public abstract class KreatinineStatus {
	/**
	 * date of this advise.
	 */
	private Date date;
	/**
	 * the value of this Status.
	 */
	private double value;

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
	
	/**
	 * setter for date.
	 * @param date date to set.
	 */
	protected void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * setter for value.
	 * @param value value to set.
	 */
	protected void setValue(double value) {
		this.value = value;
	}
	
	/**
	 * getter for date.
	 * @return the date.
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * getter for value.
	 * @return the value.
	 */
	public double getValue() {
		return value;
	}
	
}
