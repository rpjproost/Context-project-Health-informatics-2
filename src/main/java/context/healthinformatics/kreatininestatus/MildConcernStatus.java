package context.healthinformatics.kreatininestatus;

import java.util.Date;
/**
 * mild concern class.
 */
public class MildConcernStatus extends KreatinineStatus {

	/**
	 * constructor.
	 * @param date date of the status.
	 * @param value value of the status.
	 */
	public MildConcernStatus(Date date, double value) {
		setValue(value);
		setDate(date);
	}

	@Override
	public String getAdvice(KreatinineStatus yesterday) {
		return yesterday.getMildConcernAdvice();
	}

	@Override
	protected String getConcernAdvice() {
		return "contact hospital";
	}

	@Override
	protected String getMildConcernAdvice() {
		return "contact hospital";
	}

	@Override
	protected String getReasonableAdvice() {
		return "measure again tommorrow";
	}

	@Override
	protected String getSafeAdvice() {
		return "nothing extra";
	}

	@Override
	public KreatinineStatus getStatus(KreatinineStatus seccondStatus) {
		return seccondStatus.getStatus(this);
	}

	@Override
	protected KreatinineStatus getStatus(ConcernStatus firstStatus) {
		return firstStatus;
	}

	@Override
	protected KreatinineStatus getStatus(MildConcernStatus firstStatus) {
		return firstStatus;
	}
	
	/**
	 * returns string representation of this object.
	 */
	@Override
	public String toString() {
		return "Mild concern";
	}

}
