package context.healthinformatics.interfacecomponents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import context.healthinformatics.gui.InterfaceHelper;

/**
 * A Frame for helping the user understand the input page.
 */
public class HelpFrame extends InterfaceHelper {
	private static final long serialVersionUID = 1L;

	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 500;
	private static final int TEXTAREA_WIDTH = 375;
	private static final int TEXTARE_HEIGHT = 480;
	private static final int BUTTON_WIDTH = 180;
	private static final int BUTTON_HEIGHT = 30;
	private static final int TABPANEL_WIDTH = 200;
	private static final int INFOPANEL_WIDTH = 400;
	private static final float FONT_SIZE = 15.0f;
	private static final int MARGINTOP = 10;

	private JFrame helpMainFrame;

	private JPanel mainPanel;
	private JPanel tabPanel;
	private JPanel infoPanel;

	private JTextArea displayInfoTextArea = new JTextArea();

	private InputPageComponents inputPageComponents;

	private ArrayList<HelpFrameInfoContainer> listOfHelpFrameInfo;

	/**
	 * Constructor of the HelpFrame class.
	 * 
	 * @param titleFrame
	 *            the title of the frame
	 * @param listOfHelpFrameInfo
	 *            the list with info for the help frame
	 * @param inputPageComponents
	 *            the input page components
	 */
	public HelpFrame(String titleFrame,
			ArrayList<HelpFrameInfoContainer> listOfHelpFrameInfo,
			InputPageComponents inputPageComponents) {
		this.listOfHelpFrameInfo = listOfHelpFrameInfo;
		this.inputPageComponents = inputPageComponents;
		helpMainFrame = new JFrame(titleFrame);
		setTextAreaSettings();
		initMainPanel();
		setWindowListener();
	}

	/**
	 * Add a WindowsListener to tell the input page components when the windows
	 * is closed.
	 */
	private void setWindowListener() {
		helpMainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				inputPageComponents.setHelpFrameOpen(false);
			}
		});
	}

	/**
	 * Set the settings of the text area.
	 */
	private void setTextAreaSettings() {
		this.displayInfoTextArea.setEditable(false);
		this.displayInfoTextArea.setLineWrap(true);
		this.displayInfoTextArea.setWrapStyleWord(true);
		this.displayInfoTextArea.setPreferredSize(new Dimension(TEXTAREA_WIDTH,
				TEXTARE_HEIGHT));
		this.displayInfoTextArea.setFont(displayInfoTextArea.getFont()
				.deriveFont(FONT_SIZE));
	}

	/**
	 * Initialize the main panel with given width.
	 */
	private void initMainPanel() {
		mainPanel = createEmptyWithGridBagLayoutPanel();
		mainPanel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		initHelpMainFrame();
	}

	/**
	 * Initialize the main components of the helper frame.
	 */
	private void initHelpMainFrame() {
		helpMainFrame.getContentPane().add(mainPanel);
		helpMainFrame.pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		helpMainFrame.setLocation(dim.width / 2 - helpMainFrame.getSize().width
				/ 2, dim.height / 2 - helpMainFrame.getSize().height / 2);
		helpMainFrame.setVisible(true);
		initTabAndInfoPanel();
	}

	/**
	 * Initialize the panels for the Tabs and the Info.
	 */
	private void initTabAndInfoPanel() {
		tabPanel = createEmptyWithGridBagLayoutPanel();
		tabPanel.setPreferredSize(new Dimension(TABPANEL_WIDTH, FRAME_HEIGHT));
		tabPanel.setBackground(Color.WHITE);
		infoPanel = createEmptyWithGridBagLayoutPanel();
		infoPanel
				.setPreferredSize(new Dimension(INFOPANEL_WIDTH, FRAME_HEIGHT));
		infoPanel.setBackground(Color.WHITE);
		addTabAndInfoPanel();
	}

	/**
	 * Add the tab and info panel to the Main Panel.
	 */
	private void addTabAndInfoPanel() {
		infoPanel.add(displayInfoTextArea, setGrids(0, 0));
		mainPanel.add(tabPanel, setGrids(0, 0));
		mainPanel.add(infoPanel, setGrids(1, 0));
		addButtonsToTabPanel();
		addTextAreaToInfo();
	}

	/**
	 * Add all the buttons for the different help info.
	 */
	private void addButtonsToTabPanel() {
		for (int i = 0; i < listOfHelpFrameInfo.size(); i++) {
			JButton currentButton = new JButton(listOfHelpFrameInfo.get(i)
					.getTitle());
			currentButton.setPreferredSize(new Dimension(BUTTON_WIDTH,
					BUTTON_HEIGHT));
			addActionListenerToThisButton(currentButton, i);
			tabPanel.add(currentButton, setGrids(0, i, MARGINTOP));
		}
	}

	/**
	 * Add an ActionListener to the created button.
	 * 
	 * @param currentButton
	 *            the created button
	 * @param index
	 *            the index of the button
	 */
	private void addActionListenerToThisButton(JButton currentButton, int index) {
		final int buttonIndex = index;
		currentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae2) {
				displayInfoTextArea.setText(listOfHelpFrameInfo
						.get(buttonIndex).getInfo());
			}
		});
	}

	/**
	 * Add the TextArea to the info panel.
	 */
	private void addTextAreaToInfo() {
		displayInfoTextArea.setText(listOfHelpFrameInfo.get(0).getInfo());
	}
}
