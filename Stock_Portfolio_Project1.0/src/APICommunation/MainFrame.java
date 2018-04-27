package APICommunation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainFrame {

	// objects for referencing java ui panels
	private static SummaryPanel sumPanel = new SummaryPanel();
	private static YourStocksPanel yourSPanel = new YourStocksPanel();
	private static StockSearchPanel sSearchPanel = new StockSearchPanel();
	private static NewsPanel dPanel = new NewsPanel();
	private static HelpPanel hPanel = new HelpPanel();
	private static JFrame menu = new JFrame("Stock Ticker Menu");
	private static JFrame frame = new JFrame("Stock Ticker");
	public static ArrayList<String> usernameList = new ArrayList<String>();
	public static ArrayList<String> passwordList = new ArrayList<String>();
	
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
		//panel to organize username and password
		JPanel usernamePan = new JPanel();
		usernamePan.setLayout(new FlowLayout());
		JPanel passwordPan = new JPanel();
		passwordPan.setLayout(new FlowLayout());
		
		// create menu frame
		menu.setSize(500, 250);
		menu.setLayout(new GridLayout(4,1));
		menu.setLocationRelativeTo(null);
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menu.setMinimumSize(new Dimension(400, 200));

		//labels and text fields
		JLabel usernameLabel = new JLabel("Enter Username");
		JLabel passwordLabel = new JLabel("Enter Password");
		JLabel errorLabel = new JLabel("<html><font color='red'>Invalid Username or Password</font></html>");
		errorLabel.setVisible(false);
		JTextField usernameField = new JTextField(20);
		JPasswordField passwordField = new JPasswordField(20);
		usernameField.setToolTipText("Please enter Username");
		passwordField.setToolTipText("Please enter Password");
		
		//add username option here
		usernamePan.add(usernameLabel);
		usernamePan.add(usernameField);
		passwordPan.add(passwordLabel);
		passwordPan.add(passwordField);
		menu.add(usernamePan);
		menu.add(passwordPan);
		menu.add(errorLabel);
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
	        	sumPanel.refresh();
	        	sumPanel.activeStocks();
	        	yourSPanel.refresh2();
	            if(tp.getSelectedIndex() == 3){
	            	//refresh news page
	            	dPanel.buildPage();
	            	dPanel.revalidate();
	            	dPanel.repaint();
	            }
	        }
	    });

		//the enter button on login screen search google sheets to see if user exists
		enter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userNameString = "https://sheets.googleapis.com/v4/spreadsheets/1Na9YXFwUkRrNXl5FSF5zrFa4lO6PZBYj55lPmFW2qbU/values/Sheet1!B%3AA?fields=values&key=AIzaSyAJ60kg9erJ7k6LjROcJYTArrZMDeDgZo4";
				URL url;
				try {
					url = new URL(userNameString);
					String jsonResultUsernames = ApiFetch.getJson(url.toString());
					parseUsernames(jsonResultUsernames);
					
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//if statement to match back-end users here
				String userNameEntered = new String(usernameField.getText());
				String passwordEntered = new String(passwordField.getPassword());
				if(usernameList.contains(userNameEntered) && passwordList.contains(passwordEntered)){
					menu.setVisible(false);
					frame.setVisible(true);
				}else{
					//print error statement
					errorLabel.setVisible(true);
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
	
	//parse usernames and passwords into a list
	public static void parseUsernames(String json) throws JSONException{
		JSONObject jObj = new JSONObject(json);
		JSONArray usernames = jObj.getJSONArray("values");

		for (int i = 0; i < usernames.length(); i++) {
			JSONArray arr = usernames.getJSONArray(i);
			String usernameStr = arr.getString(0);
			String passwordStr = arr.getString(1);
			
			usernameList.add(usernameStr);
			passwordList.add(passwordStr);

		}
	}

}