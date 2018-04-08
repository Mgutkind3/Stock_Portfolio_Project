package csi480;

import java.net.URL;



import java.awt.Dimension;
import java.awt.Font;

import javax.swing.border.TitledBorder;
import javax.swing.*;

//add pasword protection?
//java card layout
public class apiFetchFunction {
		
	//objects for referencing java ui panels
	static summaryPanel sumPanel = new summaryPanel();
	static yourStocksPanel yourSPanel = new yourStocksPanel();
	static dataPanel dPanel = new dataPanel();
	static stockSearchPanel sSearchPanel = new stockSearchPanel();
	static helpPanel hPanel = new helpPanel();
	static apiFetch jsonFetch = new apiFetch();
	
	public static void main(String[] args) throws Exception {
	        
				//TODO test url for AAPL call
				URL testURL = new URL("https://api.iextrading.com/1.0/stock/market/batch?symbols=aapl&types=quote");
				//URL testURL = new URL("https://www.quandl.com/api/v3/datasets/WIKI/FB.json?column_index=4&start_date=2017-01-01&end_date=2017-12-31&collapse=monthly&transform=rdiff&api_key=ZGGxFod_7TVXrEU-UeuL");
				String result = apiFetch.getJson(testURL.toString());
				//System.out.println(result);
				
				//parseIt(result);
				
				//initialize frame for UI
				JFrame frame = new JFrame("Stock Ticker");
				frame.setSize(600, 500);
				JTabbedPane tp = new JTabbedPane();
		
				//initialize menu frame
				JFrame menu = new JFrame("Stock Ticker Menu");
				menu.setSize(500, 250);
				menu.setLayout(new FlowLayout());
				menu.setLocationRelativeTo(null);
				menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				menu.setMinimumSize(new Dimension(400, 200));

				//add password field
				JLabel passwordLabel = new JLabel("Enter password");
				JPasswordField passwordField = new JPasswordField(20); 
				passwordField.setToolTipText("Please enter password");
				menu.add(passwordLabel);
				menu.add(passwordField);
				JButton enter = new JButton("Login");  
				menu.add(enter);

				//create logout button
				JButton logOut = new JButton("Log out");
				JPanel logOutPanel = new JPanel();
				FlowLayout fLayout = new FlowLayout();
				fLayout.setAlignment(FlowLayout.TRAILING);
				logOutPanel.setLayout(fLayout);
				logOutPanel.add(logOut);
				menu.setVisible(true);
				
				//create scroll panes for every page
				JScrollPane summaryScroll = new JScrollPane(sumPanel);
				JScrollPane yourStocksScroll = new JScrollPane(yourSPanel);
				JScrollPane dataScroll = new JScrollPane(dPanel);
				JScrollPane stockSearchScroll = new JScrollPane(sSearchPanel);
				JScrollPane helpScoll = new JScrollPane(hPanel);

				//add tabs to the tab panel
				tp.addTab("Summary", summaryScroll);
				tp.addTab("Your Stocks", yourStocksScroll);
				tp.addTab("Data", dataScroll);
				tp.addTab("Stock Search", stockSearchScroll);
				tp.addTab("Help", helpScoll);
				
				//set essential rules for using the jframe
				frame.getContentPane().add(tp);
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setMinimumSize(new Dimension(400, 200));
			}//end of main

				
	
	
	//create border
	public static TitledBorder createTitle(String titleName) {
		TitledBorder title = BorderFactory.createTitledBorder(titleName);
		title.setTitleJustification(TitledBorder.CENTER);
		title.setTitleFont(new Font("Arial", Font.BOLD, 20));

		return title;
	}
 
	//submit password
	enter.addActionListener(new ActionListener() {  
		    public void actionPerformed(ActionEvent e) {  
		    	if (Arrays.equals(passwordField.getPassword(), new char[]{'h','i'})){   
		    	   passwordField.setText("");
		    	   menu.setVisible(false);
		       	   frame.setVisible(true);
		       }
		    }  
		    }); 
	

			
}
