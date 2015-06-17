package context.healthinformatics.kreatininestatus;

/**
 * mild concern class.
 */
public class MildConcernStatus extends KreatinineStatus {

	/**
	 * constructor.
	 */
	public MildConcernStatus() {	}

	@Override
	public String getAdvice(KreatinineStatus yesterday) {
		return yesterday.getMildConcernAdvice();
	}

	@Override
	protected String getMildConcernAdvice() {
		return "contact hospital";
	}

	@Override
	protected String getReasonableAdvice() {
		return "measure tomorrow";
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

	@Override
	public String needToReMeasure() {
		return "Yes";
	}

}
