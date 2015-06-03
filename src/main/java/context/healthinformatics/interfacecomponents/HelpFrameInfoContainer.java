package context.healthinformatics.interfacecomponents;

/**
 * Contain the info for explanation of a function in the program.
 */
public class HelpFrameInfoContainer {
	private String title;
	private String info;

	/**
	 * Constructor of HelpFrameInfoContainer.
	 * 
	 * @param title
	 *            the title
	 * @param info
	 *            the info/explanation
	 */
	public HelpFrameInfoContainer(String title, String info) {
		this.title = title;
		this.info = info;
	}

	/**
	 * Get the title.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the title.
	 * 
	 * @param title
	 *            the title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get the info.
	 * 
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * Set the info.
	 * 
	 * @param info
	 *            the info
	 */
	public void setInfo(String info) {
		this.info = info;
	}
}
