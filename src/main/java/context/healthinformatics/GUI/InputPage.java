package context.healthinformatics.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import context.healthinformatics.Parser.TXTParser;
import context.healthinformatics.Parser.XMLParser;

public class InputPage implements PanelState {
	
	static JTextField txt;
	static JButton button;
	Dimension dim;
	static String input;
	
	
	public InputPage() {}

	public JPanel loadPanel() {
		JPanel panel = MainFrame.createPanel(Color.decode("#81DAF5"), MainFrame.getScreenWidth(), MainFrame.getStatePanelSize());
		
		dim = new Dimension(100,40);
		txt = new JTextField(50);
		MainFrame.c.gridx = 0;
		MainFrame.c.gridy = 0;
		panel.add(txt, MainFrame.c);
		
		// create and add button
		button = new JButton("test");
		makeButton(button);
		MainFrame.c.gridx = 1;
		MainFrame.c.gridy = 0;
		panel.add(button, MainFrame.c);
		
		return panel;
	}
	
	/**
	 * Method which creates a button.
	 * @param input button
	 */
	public void makeButton(JButton input) {
		input.setPreferredSize(dim);
		input.addActionListener(new ActionHandler());
		input.setFont(new Font("Arial", Font.PLAIN, 30));
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
