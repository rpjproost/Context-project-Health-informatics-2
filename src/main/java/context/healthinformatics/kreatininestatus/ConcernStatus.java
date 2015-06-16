package context.healthinformatics.kreatininestatus;

/**
 * concernStatus class.
 */
public class ConcernStatus extends KreatinineStatus {

	/**
	 * constructor.
	 */
	public ConcernStatus() { }

	@Override
	protected String getConcernAdvice() {
		return "follow Doctor's advise";
	}

	@Override
	protected String getMildConcernAdvice() {
		return "follow Doctor's advise";
	}

	@Override
	protected String getReasonableAdvice() {
		return "follow Doctor's advise";
	}

	@Override
	protected String getSafeAdvice() {
		return "follow Doctor's advise";
	}


	@Override
	public String getAdvice(KreatinineStatus yesterday) {
		return yesterday.getConcernAdvice();
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
		return this;
	}
	
	/**
	 * returns string representation of this object.
	 */
	@Override
	public String toString() {
		return "Concern";
	}


}
