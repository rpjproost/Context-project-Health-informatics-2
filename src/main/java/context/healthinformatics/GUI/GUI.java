package context.healthinformatics.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import context.healthinformatics.Parser.TXTParser;
import context.healthinformatics.Parser.XMLParser;

public class GUI {
	
	static GridBagConstraints c;
	static JPanel mainPanel;
	static JFrame f;
	static JButton button;
	static Dimension dim;
	static JTextField txt;
	static String input;

	public static void main(String[] args) {
		makeGUI();
	}
	
	public static void makeGUI() {
		makeFrame(1000,500);
		dim = new Dimension(100,40);
		c = new GridBagConstraints();
		mainPanel = new JPanel(new GridBagLayout());
		
		// Creates the main panel
		mainPanel.setSize(1000,500);
		mainPanel.setBackground(Color.gray);

		// Makes the title 2048
		JPanel panel1 = createPanel(Color.gray, 800, 200);
		JLabel label = new JLabel("PRODUCT");
		label.setFont(new Font("Arial", Font.PLAIN, 70));
		label.setForeground(Color.white);
		panel1.add(label);
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(panel1, c);
		
		JPanel panel2 = createPanel(Color.gray, 800, 200);
		
		//create and add textfield
		txt = new JTextField(50);
		c.gridx = 0;
		c.gridy = 0;
		panel2.add(txt,c);
		
		// create and add button
		button = new JButton("test");
		makeButton(button);
		c.gridx = 1;
		c.gridy = 0;
		panel2.add(button, c);
		
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(panel2, c);
		f.add(mainPanel);
		f.setVisible(true);
	}
	
	/**
	 * @return JPanel with attributes specified by the arguments.
	 * @param color
	 *            of panel
	 * @param width
	 *            of panel
	 * @param height
	 *            of panel
	 */
	public static JPanel createPanel(Color color, int width, int height) {
		JPanel tempPanel = new JPanel();
		tempPanel.setBackground(color);
		tempPanel.setMinimumSize(new Dimension(width, height));
		tempPanel.setMaximumSize(new Dimension(width, height));
		tempPanel.setPreferredSize(new Dimension(width, height));
		return tempPanel;
	}
	
	/**
	 * Method which makes the outer-container-frame.
	 * @param x
	 * @param y
	 */
	public static void makeFrame(int x, int y){
		f = new JFrame();
		f.setSize(x-100, y-100);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setTitle("PRODUCT NAME");
	}
	
	/**
	 * Method which creates a button.
	 * @param input
	 */
	public static void makeButton(JButton input) {
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
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == button) {
				input = txt.getText();
				XMLParser xmlp = new XMLParser(input);
				try {
					xmlp.parse();
					TXTParser txtp = new TXTParser(xmlp.getPath(),xmlp.getStartLine(),xmlp.getDelimiter(), xmlp.getColumns());
					txtp.parse();
				} catch (IOException e1) {
					System.out.println("No such file... Try again!");;
				}
			}
		}
	}
}
