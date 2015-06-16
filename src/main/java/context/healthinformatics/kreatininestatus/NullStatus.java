package context.healthinformatics.kreatininestatus;

/**
 * nullStatus class.
 */
public class NullStatus extends KreatinineStatus {

	/**
	 * constructor.
	 */
	public NullStatus() { }

	@Override
	public String getAdvice(KreatinineStatus yesterday) {
		return "null, not measured";
	}

	@Override
	protected String getConcernAdvice() {
		return "null, not measured";
	}

	@Override
	protected String getMildConcernAdvice() {
		return "null, not measured";
	}

	@Override
	protected String getReasonableAdvice() {
		return "null, not measured";
	}

	@Override
	protected String getSafeAdvice() {
		return "null, not measured";
	}

	@Override
	public KreatinineStatus getStatus(KreatinineStatus seccondStatus) {
		return this;
	}

	@Override
	protected KreatinineStatus getStatus(ConcernStatus firstStatus) {
		return this;
	}

	@Override
	protected KreatinineStatus getStatus(MildConcernStatus firstStatus) {
		return this;
	}
	
	/**
	 * to string, returns empty string.
	 * @return empty string.
	 */
	public String toString() {
		return "";
	}
}
