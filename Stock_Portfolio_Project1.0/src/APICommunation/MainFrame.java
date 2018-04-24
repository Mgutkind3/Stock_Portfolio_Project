package APICommunation;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.*;

public class MainFrame {

	// objects for referencing java ui panels
	private static SummaryPanel sumPanel = new SummaryPanel();
	private static YourStocksPanel yourSPanel = new YourStocksPanel();
	private static StockSearchPanel sSearchPanel = new StockSearchPanel();
	private static NewsPanel dPanel = new NewsPanel();
	private static HelpPanel hPanel = new HelpPanel();
	private static JFrame menu = new JFrame("Stock Ticker Menu");
	private static JFrame frame = new JFrame("Stock Ticker");

	public static void main(String[] args) throws Exception {

		System.out.print("Enter Username");
		Scanner scan = new Scanner(System.in);
		String getUser = scan.next();
		getUser.trim();

		System.out.print("Enter Password");
		String getPass = scan.next();
		getPass.trim();
		scan.close();
		// login(getUser, getPass);

		if (MongoConnect.checkUsernameExist(getUser) == false) {
			MongoConnect.createUser(getUser, getPass);
		}

		// create menu frame
		menu.setSize(500, 250);
		menu.setLayout(new FlowLayout());
		menu.setLocationRelativeTo(null);
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menu.setMinimumSize(new Dimension(400, 200));

		JLabel passwordLabel = new JLabel("Enter password");
		JPasswordField passwordField = new JPasswordField(20);
		passwordField.setToolTipText("Please enter password");
		menu.add(passwordLabel);
		menu.add(passwordField);
		JButton enter = new JButton("Login");
		menu.add(enter);
		menu.setVisible(true);

		// initialize frame for UI

		frame.setSize(1000, 750);
		JTabbedPane tp = new JTabbedPane();

		// create scroll panes for every page
		JScrollPane summaryScroll = new JScrollPane(sumPanel);
		JScrollPane yourStocksScroll = new JScrollPane(yourSPanel);
		JScrollPane stockSearchScroll = new JScrollPane(sSearchPanel);
		JScrollPane dataScroll = new JScrollPane(dPanel);
		JScrollPane helpScoll = new JScrollPane(hPanel);

		// add tabs to the tab panel
		tp.addTab("Summary", summaryScroll);
		tp.addTab("Your Stocks", yourStocksScroll);
		tp.addTab("Stock Search", stockSearchScroll);
		tp.addTab("News", dataScroll);
		tp.addTab("Help", helpScoll);
		
		

		// set essential rules for using the jframe
		frame.getContentPane().add(tp);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(400, 200));
		
		//react to selected tabs
		tp.addChangeListener(new ChangeListener() {
	        public void stateChanged(ChangeEvent e) {
	            if(tp.getSelectedIndex() == 3){
	            	//refresh news page
	            	dPanel.buildPage();
	            	dPanel.revalidate();
	            	dPanel.repaint();
	            }
	        }
	    });

		// the enter button on login screen
		enter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Arrays.equals(passwordField.getPassword(), new char[] { 'p', 'a', 's', 's', 'w', 'o', 'r', 'd' })) {
					passwordField.setText("");
					menu.setVisible(false);
					frame.setVisible(true);
				}
			}
		});

		// hit enter to log in
		passwordField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					enter.doClick();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// Do Nothing
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// Do Nothing
			}

		});

	}// end of main

	// create border
	public static TitledBorder createTitle(String titleName) {
		TitledBorder title = BorderFactory.createTitledBorder(titleName);
		title.setTitleJustification(TitledBorder.CENTER);
		title.setTitleFont(new Font("Arial", Font.BOLD, 20));

		return title;
	}

	public static void logout() {
		MainFrame.menu.setVisible(true);
		MainFrame.frame.setVisible(false);
	}

}