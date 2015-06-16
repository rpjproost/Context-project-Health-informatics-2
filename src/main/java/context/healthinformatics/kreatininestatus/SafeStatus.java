package context.healthinformatics.kreatininestatus;

/**
 * Safe status class.
 */
public class SafeStatus extends KreatinineStatus {

	/**
	 * constructor.
	 */
	public SafeStatus() { }

	@Override
	public String getAdvice(KreatinineStatus yesterday) {
		return yesterday.getAdvice(this);
	}

	@Override
	protected String getConcernAdvice() {
		return "contact hospital";
	}

	@Override
	protected String getMildConcernAdvice() {
		return "measure again tommorow";
	}

	@Override
	protected String getReasonableAdvice() {
		return "nothing extra";
	}

	@Override
	protected String getSafeAdvice() {
		return "nothing extra";
	}

	@Override
	public KreatinineStatus getStatus(KreatinineStatus seccondStatus) {
		return this;
	}

	@Override
	protected KreatinineStatus getStatus(ConcernStatus firstStatus) {
		return new ReasonablySafeStatus();
	}

	@Override
	protected KreatinineStatus getStatus(MildConcernStatus firstStatus) {
		return new SafeStatus();
	}
	
	/**
	 * returns string representation of this object.
	 */
	@Override
	public String toString() {
		return "Safe";
	}

}
