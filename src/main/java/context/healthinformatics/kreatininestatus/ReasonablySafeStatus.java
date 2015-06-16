package context.healthinformatics.kreatininestatus;

/**
 * reasonably safe status.
 */
public class ReasonablySafeStatus extends KreatinineStatus {

	/**
	 * constructor.
	 */
	public ReasonablySafeStatus() {	}

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
		return "measure again";
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
		return new MildConcernStatus();
	}

	@Override
	protected KreatinineStatus getStatus(MildConcernStatus firstStatus) {
		return this;
	}
	
	/**
	 * returns string representation of this object.
	 */
	@Override
	public String toString() {
		return "Reasonably safe";
	}

}
