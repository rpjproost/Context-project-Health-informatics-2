package context.healthinformatics.graphs;

/**
 * CLass connection set contains a connection.
 */
public class ConnectionSet {
	private String codeFrom;
	private String codeTo;

	/**
	 * Constructor of the ConnectionSet.
	 * 
	 * @param codeFrom
	 *            the code from
	 * @param codeTo
	 *            the code to
	 */
	public ConnectionSet(String codeFrom, String codeTo) {
		this.codeFrom = codeFrom;
		this.codeTo = codeTo;
	}

	/**
	 * Get Code From.
	 * 
	 * @return the code from
	 */
	public String getCodeFrom() {
		return codeFrom;
	}

	/**
	 * Get the code to.
	 * 
	 * @return the code to
	 */
	public String getCodeTo() {
		return codeTo;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ConnectionSet) {
			return ((ConnectionSet) other).getCodeFrom().equals(this.codeFrom)
					&& ((ConnectionSet) other).getCodeTo().equals(this.codeTo);
		} else {
			return false;
		}
	}

	/**
	 * Generates hashcode for object connection set.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result;
		if (codeFrom != null) {
			result += codeFrom.hashCode();
		}
		result = prime * result + codeTo.hashCode();
		result = prime * result;
		return result;
	}

	@Override
	public String toString() {
		return "Code from: " + codeFrom + " code to: " + codeTo;
	}
}
