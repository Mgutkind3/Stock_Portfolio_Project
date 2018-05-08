package csi480;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class SummaryPanel extends JPanel {
	public static ArrayList<String> favorites = new ArrayList<String>();
	private static ArrayList<String> changePerc = new ArrayList<String>();
	public static ArrayList<String> myStocks = new ArrayList<String>();
	public static ArrayList<String> activePercentChange = new ArrayList<String>();
	public static ArrayList<String> activeSymbols = new ArrayList<String>();
	private JTextPane tPane = new JTextPane();
	private JTextPane mostActivePane = new JTextPane();
	private String baseUrl = "https://api.iextrading.com/1.0/";
	private static DecimalFormat df2 = new DecimalFormat("#.##");

	public SummaryPanel() {

		tPane.setEditable(false);
		mostActivePane.setEditable(false);
		// favorites.add("stock 1");
		// favorites.add("stock 2");
		// myStocks.add("stock1");
		// myStocks.add("stock2");

		// create logout button
		JButton logOut = new JButton("Log out");
		JPanel logOutPanel = new JPanel();
		FlowLayout fLayout = new FlowLayout();
		fLayout.setAlignment(FlowLayout.TRAILING);
		logOutPanel.setLayout(fLayout);
		logOutPanel.add(logOut);

		setBorder(MainFrame.createTitle("Summary"));

		GridLayout grid = new GridLayout(3, 2);
		setLayout(grid);

		JLabel favLabel = new JLabel("Favorites");
		JLabel sumLabel = new JLabel("Most Active Stocks");

		// get most active api call
		getMostActive(baseUrl + "stock/market/list/mostactive");
		activeStocks();
		refresh();

		// create fav panel
		JPanel favPanel = new JPanel();
		favPanel.setLayout(new BorderLayout());
		favPanel.add(favLabel, BorderLayout.NORTH);
		favPanel.add(tPane, BorderLayout.CENTER);

		// create most Active panel
		JPanel actPanel = new JPanel();
		actPanel.setLayout(new BorderLayout());
		actPanel.add(sumLabel, BorderLayout.NORTH);
		actPanel.add(mostActivePane, BorderLayout.CENTER);
		actPanel.add(logOutPanel, BorderLayout.EAST);

		add(favPanel);
		add(actPanel);
		add(logOutPanel);

		// logout button
		logOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainFrame.logout();
			}
		});
	}

	// api call to get most active stocks
	public void getMostActive(String urlString) {
		try {
			URL url = new URL(urlString);
			String jsonResult = ApiFetch.getJson(url.toString());
			parseMostActive(jsonResult);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// parse most active json
	public void parseMostActive(String json) {
		try {
			JSONArray arr = new JSONArray(json);

			for (int i = 0; i < arr.length(); i++) {
				String symbol = arr.getJSONObject(i).getString("symbol");
				String change = arr.getJSONObject(i).getString("changePercent");
				// String combined = name + "-" + symbol;

				// populate vectors with symbols and names
				activeSymbols.add(symbol);
				activePercentChange.add(change);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// refresh most active stocks and favorite stocks
	public void refresh() {
		StyledDocument doc = tPane.getStyledDocument();
		Style style = tPane.addStyle("I'm a Style", null);

		// erase doc contents
		try {
			doc.remove(0, doc.getLength());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}

		// loop through list of favorite stocks
		for (int i = 0; i < favorites.size(); i++) {

			// get percent change value as a double
			double number = Double.parseDouble(changePerc.get(i));

			if (number > 0) {
				try {
					StyleConstants.setForeground(style, Color.GREEN);
					doc.insertString(doc.getLength(),
							favorites.get(i) + ":  %" + df2.format(number) + " Increase Today" + "\n", style);
				} catch (BadLocationException e) {
				}
				// color code green
			} else {
				try {
					StyleConstants.setForeground(style, Color.RED);
					doc.insertString(doc.getLength(),
							favorites.get(i) + ":  %" + df2.format(number) + " Decrease Today" + "\n", style);
				} catch (BadLocationException e) {
				}
				// color code red
			}
		}

		this.revalidate();
		this.repaint();
	}

	public void activeStocks() {

		StyledDocument doc1 = mostActivePane.getStyledDocument();
		Style style1 = mostActivePane.addStyle("I'm a Style", null);

		// erase doc contents
		try {
			doc1.remove(0, doc1.getLength());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}

		// loop through list of most Active stocks
		for (int i = 0; i < activeSymbols.size(); i++) {

			// get percent change value as a double
			double activeNumber = Double.parseDouble(activePercentChange.get(i));

			if (activeNumber > 0) {
				try {
					StyleConstants.setForeground(style1, Color.GREEN);
					doc1.insertString(doc1.getLength(),
							activeSymbols.get(i) + ":  %" + df2.format(activeNumber) + " Increase as of Now" + "\n",
							style1);
				} catch (BadLocationException e) {
				}
				// color code green
			} else {

				try {
					StyleConstants.setForeground(style1, Color.RED);
					doc1.insertString(doc1.getLength(),
							activeSymbols.get(i) + ":  %" + df2.format(activeNumber) + " Decrease as of Now" + "\n",
							style1);
				} catch (BadLocationException e) {
				}
				// color code red
			}
		}
		this.revalidate();
		this.repaint();
	}

	// make sure no duplicate stocks can be added to the user's lists of
	// favorites
	public static void addFavorite(String s, String changePercent) {
		if (!favorites.contains(s)) {
			changePerc.add(changePercent);
			favorites.add(s);
		}

	}

}