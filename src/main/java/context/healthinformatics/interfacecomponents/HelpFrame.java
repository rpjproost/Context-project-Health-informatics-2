package context.healthinformatics.interfacecomponents;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import context.healthinformatics.gui.InterfaceHelper;

/**
 * A Frame for helping the user understand the input page.
 */
public class HelpFrame extends InterfaceHelper {
	private static final long serialVersionUID = 1L;

	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 500;
	private static final int TEXTAREA_WIDTH = 20;
	private static final int TEXTARE_HEIGHT = 20;
	private static final int BUTTON_WIDTH = 180;
	private static final int BUTTON_HEIGHT = 30;
	private static final float FONT_SIZE = 15.0f;
	private static final int MARGINTOP = 10;
	private static final int THREE = 3;

	private JFrame helpMainFrame;

	private JPanel mainPanel;
	private JPanel tabPanel;
	private JPanel infoPanel;
	private JScrollPane scroll;
	private int width;
	private int height;
	private int textareawidth;

	private JEditorPane displayHtmlPane = new JEditorPane();

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
		width = FRAME_WIDTH;
		height = FRAME_HEIGHT;
		textareawidth = (width * 2) / THREE;
		setUpHelpFrame(titleFrame, listOfHelpFrameInfo);
		this.inputPageComponents = inputPageComponents;
		setWindowListener();
		initMainPanel();
	}

	/**
	  * Constructor of the HelpFrame class.
	 * 
	 * @param titleFrame
	 *            the title of the frame
	 * @param listOfHelpFrameInfo
	 *            the list with info for the help frame
	 * @param width the width of where it will be loaded in.
	 * @param height the height of where it will be loaded in.
	 */
	public HelpFrame(String titleFrame, 
			ArrayList<HelpFrameInfoContainer> listOfHelpFrameInfo, int width, int height) {
		this.width = width;
		this.height = height;
		textareawidth = (width * 2) / THREE;
		setUpHelpFrame(titleFrame, listOfHelpFrameInfo);
		initMainPanelNoPopUp();
	}

	private void setUpHelpFrame(String titleFrame, 
			ArrayList<HelpFrameInfoContainer> listOfHelpFrameInfo) {
		this.listOfHelpFrameInfo = listOfHelpFrameInfo;
		helpMainFrame = new JFrame(titleFrame);
		setTextAreaSettings();
		addHyperLinkListener();
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
		this.displayHtmlPane.setEditable(false);
		displayHtmlPane.setContentType("text/html");
		this.displayHtmlPane.setPreferredSize(new Dimension(textareawidth - TEXTAREA_WIDTH,
				height - TEXTARE_HEIGHT));
		this.displayHtmlPane.setFont(displayHtmlPane.getFont().deriveFont(
				FONT_SIZE));
		this.displayHtmlPane.setCaretPosition(0);
		initScrollPane();
	}

	/**
	 * Initialize the scrollPane.
	 */
	private void initScrollPane() {
		scroll = new JScrollPane(displayHtmlPane);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(new Dimension(textareawidth - TEXTAREA_WIDTH, 
				height - TEXTARE_HEIGHT));
		scroll.getVerticalScrollBar().setValue(0);
	}

	/**
	 * Initialize the main panel with given width.
	 */
	private void initMainPanel() {
		mainPanel = createEmptyWithGridBagLayoutPanel();
		mainPanel.setPreferredSize(new Dimension(width, height));
		initHelpMainFrame();
	}
	
	private void initMainPanelNoPopUp() {
		mainPanel = createEmptyWithGridBagLayoutPanel();
		mainPanel.setPreferredSize(new Dimension(width, height));
		initTabAndInfoPanel();
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
		tabPanel.setPreferredSize(new Dimension(width / THREE, height));
		tabPanel.setBackground(Color.WHITE);
		infoPanel = createEmptyWithGridBagLayoutPanel();
		infoPanel
				.setPreferredSize(new Dimension(textareawidth, height));
		infoPanel.setBackground(Color.WHITE);
		addTabAndInfoPanel();
	}

	/**
	 * Add the tab and info panel to the Main Panel.
	 */
	private void addTabAndInfoPanel() {
		infoPanel.add(scroll, setGrids(0, 0));
		mainPanel.add(tabPanel, setGrids(0, 0));
		mainPanel.add(infoPanel, setGrids(1, 0));
		addButtonsToTabPanel();
		adddisplayHtmlPaneToInfo();
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
				displayHtmlPane.setText(listOfHelpFrameInfo.get(buttonIndex)
						.getInfo());
				displayHtmlPane.setCaretPosition(0);
			}
		});
	}
	
	/**
	 * @return the help panel where everything will be showed.
	 */
	public JPanel getJPanel() {
		return mainPanel;
	}

	/**
	 * Add the HtmlPane to the info panel.
	 */
	private void adddisplayHtmlPaneToInfo() {
		displayHtmlPane.setText(listOfHelpFrameInfo.get(0).getInfo());
		displayHtmlPane.setCaretPosition(0);
	}

	/**
	 * Create action listener for a link in the html documents.
	 */
	private void addHyperLinkListener() {
		this.displayHtmlPane.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED
						&& Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().browse(e.getURL().toURI());
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}

				}
			}
		});
	}
}
