package context.healthinformatics.graphs;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

/**
 * Triggers the right-click pop up event.
 */
public class PopupTriggerListener extends MouseAdapter {
	private JPopupMenu menu;

	/**
	 * Constructor for the mouseAdapter.
	 * 
	 * @param menu
	 *            the JPopupMenu
	 */
	public PopupTriggerListener(JPopupMenu menu) {
		this.menu = menu;
	}

	/**
	 * Triggers the event when the mouse is pressed.
	 * 
	 * @param ev
	 *            the mouse event.
	 */
	public void mousePressed(MouseEvent ev) {
		if (ev.isPopupTrigger()) {
			menu.show(ev.getComponent(), ev.getX(), ev.getY());
		}
	}

	/**
	 * Triggers the pop up event when the mouse is released.
	 * 
	 * @param ev
	 *            the mouse event.
	 */
	public void mouseReleased(MouseEvent ev) {
		if (ev.isPopupTrigger()) {
			menu.show(ev.getComponent(), ev.getX(), ev.getY());
		}
	}

	/**
	 * Do nothing when the mouse is clicked.
	 * 
	 * @param ev
	 *            the mouse event.
	 */
	public void mouseClicked(MouseEvent ev) {
	}
}
