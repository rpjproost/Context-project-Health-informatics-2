package context.healthinformatics.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import context.healthinformatics.Database.SingletonDb;
import context.healthinformatics.Parser.XMLParser;
import context.healthinformatics.Writer.WriteToTXT;

/**
 * Class which represents one of the states for the variabel panel in the mainFrame.
 */
public class OutputPage implements PanelState, Serializable {

	private static final long serialVersionUID = 1L;
	private static final int FONTSIZE = 18;
	private static final int BUTTONWIDTH = 300;
	private static final int BUTTONHEIGHT = 50;
	private MainFrame mf;
	private JButton exportFile;
	
	/**
	 * Constructor.
	 * @param m is the mainframe object
	 */
	public OutputPage(MainFrame m) {
		mf = m;
	}

	/**
	 * Load the panel which contains all components.
	 */
	@Override
	public JPanel loadPanel() {
		JPanel panel = MainFrame.createPanel(Color.decode("#086A87"),
				mf.getScreenWidth(), mf.getStatePanelSize());
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		exportFile = createButton("Export File...");
		panel.add(exportFile, c);
		return panel;
	}
	
	/**
	 * Creates a button with default buttonwidth and buttonheight.
	 * @param name of the button.
	 * @return a button with the name.
	 */
	private JButton createButton(String name) {
		JButton button = new JButton(name);
		button.setPreferredSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT));
		button.setFont(new Font("Arial", Font.ITALIC, FONTSIZE));
		button.addActionListener(new ActionHandler());
		return button;
	}
	
	/**
	 * @return the export file button.
	 */
	public JButton getFileButton() {
		return exportFile;
	}
	
	/**
	 * Class which handles the actions when buttons are clicked.
	 */
	public class ActionHandler implements ActionListener {

		private JFileChooser c;
		
		/**
		 * Action when the button is pressed the save pop up will be shown.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			c = new JFileChooser();
			if (e.getSource() == getFileButton()) {
				int rVal = c.showSaveDialog(getFileButton());
				try {
					fileChooser(rVal);
				} catch (SQLException | IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		/**
		 * Decide of there must be written to a file or doing nothing.
		 * @param rVal the number of which button is chosen.
		 * @throws SQLException
		 *             the sql exception of resultset
		 * @throws IOException 
		 */
		public void fileChooser(int rVal) throws SQLException, IOException {
			if (rVal == JFileChooser.APPROVE_OPTION) {
//				WriteToTXT write = new WriteToTXT(c.getSelectedFile().getName(), 
//						c.getCurrentDirectory().toString());
				WriteToTXT write = new WriteToTXT("testButtonOutputPage.txt", 
						"src/test/data/guifiles/");
				XMLParser xmlp = new XMLParser("src/test/data/parsertodbfiles/textxml.xml");
				xmlp.parse();
				//TODO
				ResultSet rs = SingletonDb.getDb().selectResultSet("stat", "*");
				write.writeToFile(rs);
			} 
		}
	}

}
