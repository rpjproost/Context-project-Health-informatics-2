package context.healthinformatics.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import context.healthinformatics.Parser.TXTParser;
import context.healthinformatics.Parser.XMLParser;

/**
 * Class which represents one of the states for the variabel panel in the mainFrame.
 */
public class InputPage implements PanelState {
	
	private static JTextField txt;
	private static JButton button;
	private Dimension dim;
	private static String input;
	private static GridBagConstraints c;
	
	public static final int TXTFIELDWIDTH = 50;
	public static final int BUTTONFONTSIZE = 30;
	public static final int DIMESIONHEIGHT = 100;
	public static final int DIMESIONWIDTH = 40;
	
	/**
	 * Constructor.
	 */
	public InputPage() { }

	/**
	 * @return Panel of this state.
	 */
	public JPanel loadPanel() {
		JPanel panel = MainFrame.createPanel(Color.decode("#81DAF5"),
				MainFrame.getScreenWidth(), MainFrame.getStatePanelSize());
		
		c = new GridBagConstraints();
		dim = new Dimension(DIMESIONHEIGHT, DIMESIONWIDTH);
		txt = new JTextField(TXTFIELDWIDTH);
		c.gridx = 0;
		c.gridy = 0;
		panel.add(txt, c);
		
		// create and add button
		button = new JButton("test");
		makeButton(button);
		c.gridx = 1;
		c.gridy = 0;
		panel.add(button, c);
		
		return panel;
	}
	
	/**
	 * Method which creates a button.
	 * @param input button
	 */
	public void makeButton(JButton input) {
		input.setPreferredSize(dim);
		input.addActionListener(new ActionHandler());
		input.setFont(new Font("Arial", Font.PLAIN, BUTTONFONTSIZE));
	}
	
	/**
	 * Class which handles the actions when buttons are clicked.
	 */
	static class ActionHandler implements ActionListener {

		/**
		 * Method which is fired after a certain event.
		 * @param e event
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == button) {
				input = txt.getText();
				XMLParser xmlp = new XMLParser(input);
				try {
					xmlp.parse();
					TXTParser txtp = new TXTParser(xmlp.getPath(),
								xmlp.getStartLine(), xmlp.getDelimiter(), xmlp.getColumns());
					txtp.parse();
				} catch (IOException e1) {
					System.out.println("No such file... Try again!");
				}
			}
		}
	}
}
