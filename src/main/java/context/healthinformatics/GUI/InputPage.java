package context.healthinformatics.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Class which represents one of the states for the variabel panel in the mainFrame.
 */
public class InputPage implements PanelState, Serializable {
	
	private static final long serialVersionUID = 1L;

	private MainFrame mf;
	private ArrayList<ArrayList<String>> folder;
	
	private JTextField txt;
	private JButton projectButton;
	private JButton fileButton;
	private JButton selectButton;
	private Dimension dim;
	private GridBagConstraints c;
	private GridBagLayout l;
	private JPanel panel;
	private JComboBox<String> box;
	
	public static final int TXTFIELDWIDTH = 50;
	public static final int BUTTONFONTSIZE = 15;
	public static final int DIMESIONHEIGHT = 150;
	public static final int DIMESIONWIDTH = 40;
	public static final int SECTION1HEIGHT = 100;
	public static final int PROJECTLABELFONTSIZE = 20;
	public static final int PROJECTBUTTONINSETS = 10;
	public static final int THREE = 3;
	
	/**
	 * Constructor.
	 * @param m is the mainframe object
	 */
	public InputPage(MainFrame m) {
		mf = m;
		
		//////////test
		folder = new ArrayList<ArrayList<String>>();
		folder.add(new ArrayList<String>());
		folder.get(0).add("1");
		folder.add(new ArrayList<String>());
		folder.get(1).add("2");
	}

	/**
	 * @return Panel of this state.
	 */
	public JPanel loadPanel() {
		panel = MainFrame.createPanel(Color.decode("#81DAF5"),
				mf.getScreenWidth(), mf.getStatePanelSize());
		c = new GridBagConstraints();
		l = new GridBagLayout();
		panel.setLayout(l);
		dim = new Dimension(DIMESIONHEIGHT, DIMESIONWIDTH);
		
		JPanel section1 = makeSection1();
		c.gridx = 0;
		c.gridy = 0;
		panel.add(section1, c);
		
		JPanel section2 = makeSection2();
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		panel.add(section2, c);
		
		
		return panel;
	}
	
	/**
	 * @return section1 Panel.
	 */
	public JPanel makeSection1() {
		JPanel section1 = MainFrame.createPanel(Color.decode("#81DAF5"),
				mf.getScreenWidth(), SECTION1HEIGHT);
		c = new GridBagConstraints();
		section1.setLayout(l);
		
		JLabel projectLabel = new JLabel("      Project :   ");
		projectLabel.setFont(new Font("Arial", Font.PLAIN, PROJECTLABELFONTSIZE));
		projectLabel.setSize(dim);
		c.gridx = 0;
		c.gridy = 0;
		section1.add(projectLabel, c);
		
		box = new JComboBox<String>(getProjects(folder));
		c.gridx = 1;
		c.gridy = 0;
		section1.add(box, c);
		
		projectButton = makeButton("ADD new Project", 2, 0);
		c.insets = new Insets(PROJECTBUTTONINSETS, PROJECTBUTTONINSETS,
				PROJECTBUTTONINSETS, PROJECTBUTTONINSETS);
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		section1.add(projectButton, c);
		
		return section1;
	}
	
	/**
	 * @return section2 Panel.
	 */
	public JPanel makeSection2() {
		JPanel section2 = MainFrame.createPanel(Color.decode("#81DAF5"),
				mf.getScreenWidth(), SECTION1HEIGHT);
		c = new GridBagConstraints(); section2.setLayout(l);
		
		JLabel fileLabel = new JLabel("      File :   ");
		fileLabel.setFont(new Font("Arial", Font.PLAIN, PROJECTLABELFONTSIZE));
		fileLabel.setSize(dim);
		c.gridx = 0;
		c.gridy = 0;
		section2.add(fileLabel, c);
		
		txt = new JTextField(TXTFIELDWIDTH);
		c.gridx = 1;
		c.gridy = 0;
		section2.add(txt, c);
		
		selectButton = makeButton("SELECT", 2, 0);
		c.insets = new Insets(PROJECTBUTTONINSETS, PROJECTBUTTONINSETS,
				PROJECTBUTTONINSETS, PROJECTBUTTONINSETS);
		section2.add(selectButton, c);
		
		fileButton = makeButton("ADD new File", THREE, 0);
		c.insets = new Insets(PROJECTBUTTONINSETS, PROJECTBUTTONINSETS,
				PROJECTBUTTONINSETS, PROJECTBUTTONINSETS);
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		section2.add(fileButton, c);
		return section2;
	}
	
	/**
	 * Method which creates the list of projects.
	 * @param f the folder object.
	 * @return list of projects.
	 */
	public String[] getProjects(ArrayList<ArrayList<String>> f) {
		String[] res = new String[f.size()];
		for (int i = 0; i < f.size(); i++) {
			res[i] = f.get(i).get(0);
		}
		return res;
	}
	
	/**
	 * @return folder of projects and files.
	 */
	public ArrayList<ArrayList<String>> getFolder() {
		return folder;
	}
	
	/**
	 * @param  f a 2D array of projects and files.
	 */
	public void getFolder(ArrayList<ArrayList<String>> f) {
		folder = f;
	}
	
	/**
	 * Method which asks the user to enter a new Project name, and inserts it in the combobox.
	 */
	public void addComboItem() {
		String newProject =  (String) JOptionPane.showInputDialog(panel,
				"New PRoject Name : ");
		ArrayList<String> list = new ArrayList<String>();
		list.add(newProject);
		folder.add(list);
		box.addItem(newProject);
	}
	
	/**
	 * Method which creates a button.
	 * @param txt text on the button
	 * @param c1 gridx coordinate.
	 * @param c2 gridy coordinate.
	 * @return button.
	 */
	public JButton makeButton(String txt, int c1, int c2) {
		JButton button = new JButton(txt);
		
		c.gridx = c1;
		c.gridy = c2;
		button.setPreferredSize(dim);
		button.addActionListener(new ActionHandler());
		button.setFont(new Font("Arial", Font.PLAIN, BUTTONFONTSIZE));
		return button;
	}
	
	/**
	 * Class which handles the actions when buttons are clicked.
	 */
	class ActionHandler implements ActionListener {

		/**
		 * Method which is fired after a certain event.
		 * @param e event
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == projectButton) {
				addComboItem();
			}
			if (e.getSource() == fileButton) {
				System.out.println(txt.getText());
			}
			if (e.getSource() == selectButton) {
				JFileChooser selecter = new JFileChooser();
				selecter.setDialogType(JFileChooser.SAVE_DIALOG);
				int result = selecter.showSaveDialog(panel);
				if (result == JFileChooser.APPROVE_OPTION) {
				    String path = selecter.getSelectedFile().toString();
				    txt.setText(path);
				}
				selecter.setVisible(false);
			}
		}
	}
}
