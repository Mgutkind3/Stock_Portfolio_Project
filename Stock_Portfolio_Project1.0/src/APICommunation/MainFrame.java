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
    private static SignUpPanel signupPanel = new SignUpPanel();
	private static YourStocksPanel yourSPanel = new YourStocksPanel();
	private static StockSearchPanel sSearchPanel = new StockSearchPanel();
	private static NewsPanel dPanel = new NewsPanel();
	private static HelpPanel hPanel = new HelpPanel();
	private static JFrame menu = new JFrame("Stock Ticker Menu");
	private static JFrame frame = new JFrame("Stock Ticker");
	
	public static void main(String[] args) throws Exception {
		
		// create menu frame
		menu.setSize(850, 750);
		
		MenuPanel menuPanel = new MenuPanel();
		menu.add(menuPanel);
		menu.setVisible(true);

		// initialize frame for UI
		frame.setSize(850, 750);
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

	}// end of main

	//lindsay's
    public void toSignup() {
        SignUpPanel signupPanel = new SignUpPanel();
        menu.getContentPane().removeAll();
        menu.repaint();
        menu.add(signupPanel);
 
        menu.repaint();
        menu.setVisible(true);
    }
    
    public void toSummary() {
        menu.setVisible(false);
        frame.setVisible(true); 
    }
    
	public void toSignIn() {
		MenuPanel menuPanel = new MenuPanel();
		menu.getContentPane().removeAll();
		menu.repaint();
		menu.add(menuPanel);
		menu.setVisible(true);
	}
	
	// create border
	public static TitledBorder createTitle(String titleName) {
		TitledBorder title = BorderFactory.createTitledBorder(titleName);
		title.setTitleJustification(TitledBorder.CENTER);
		title.setTitleFont(new Font("Arial", Font.BOLD, 20));

		return title;
	}

	//logout
	public static void logout() {
		MainFrame.menu.setVisible(true);
		MainFrame.frame.setVisible(false);
	}
	

}