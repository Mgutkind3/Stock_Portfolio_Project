package csi480;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StockSearchPanel extends JPanel {

	// impliment lists to house data
	private Vector<String> priceDates = new Vector<String>();
	private Vector<String> closedPrices = new Vector<String>();
	private Vector<String> urlSources = new Vector<String>();
	private Vector<String> headlines = new Vector<String>();
	private Vector<String> symbols = new Vector<String>();
	private Vector<String> companyNames = new Vector<String>();
	private CPanel cp = new CPanel();
	private JPanel graphPanel = new JPanel();
	private ParseSpecificStockData specificStockFields = new ParseSpecificStockData();
	private String baseUrl = "https://api.iextrading.com/1.0/";
	private String testSymbol;

	public StockSearchPanel() {
		this.setLayout(new BorderLayout());
		this.setBorder(MainFrame.createTitle("Search Stocks"));
		this.cp.setyAxsis("Dollars");

		// get api response for all stocks and stock symbols available
		getAPIStocks(baseUrl + "ref-data/symbols", 0, null);

		// JComboBox for names
		JComboBox<String> searchBarNames = new JComboBox<String>(companyNames);
		searchBarNames.setEditable(false);
		searchBarNames.setPrototypeDisplayValue("Search Box");

		// JComboBox for symbols
		JComboBox<String> searchBarSymb = new JComboBox<String>(symbols);
		searchBarSymb.setEditable(false);
		searchBarSymb.setPrototypeDisplayValue("Search Box");

		// add action listeners to correlate boxes with each other (Match below)
		searchBarNames.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchBarSymb.setSelectedIndex(searchBarNames.getSelectedIndex());
			}
		});
		// add action listeners to correlate boxes with each other (Match above)
		searchBarSymb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchBarNames.setSelectedIndex(searchBarSymb.getSelectedIndex());
			}

		});

		// JLabels for the instructions
		JLabel titleInstructionsNames = new JLabel("Select or Type in a Stock Name/Symbol");
		JLabel titleInstructionsSymb = new JLabel("Or");

		// JButton
		JButton submitButton = new JButton("Submit");

		// JPanel for search and instruction labels (left panel)
		JPanel searchBarGrid = new JPanel();
		searchBarGrid.setLayout(new GridLayout(25, 1));// make the columns
														// thinner
		searchBarGrid.add(titleInstructionsNames);
		searchBarGrid.add(searchBarNames);
		searchBarGrid.add(titleInstructionsSymb);
		searchBarGrid.add(searchBarSymb);
		searchBarGrid.add(submitButton);

		searchBarGrid.setPreferredSize(new Dimension(250, 500));

		// JPanel for data results (middle panel)
		JPanel dataResultsGrid = new JPanel();
		dataResultsGrid.setLayout(new GridLayout(25, 1));// make the columns
															// thinner
		dataResultsGrid.setPreferredSize(new Dimension(200, 300));

		this.graphPanel.setLayout(new BorderLayout());
		graphPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				redrawChart();
				graphPanel.repaint();
			}
		});

		submitButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// remove current stocks data
				dataResultsGrid.removeAll();
				cp.removeAllDataset();
				closedPrices.clear();
				priceDates.clear();
				headlines.clear();
				urlSources.clear();

				String symbolSelected = symbols.elementAt(searchBarNames.getSelectedIndex());
testSymbol=symbolSelected;
				// use latest price field for the latest price (never null)
				getAPIStocks(baseUrl + "stock/market/batch?symbols=" + symbolSelected + "&types=quote,news,chart", 1,
						symbolSelected);

				// JLabel's for the specific stock data and fields
				JLabel data = new JLabel("           " + specificStockFields.getCompanyName());
				JLabel latestPrice = new JLabel("           Price: $" + specificStockFields.getLatestPrice());
				JLabel sector = new JLabel("           Sector: " + specificStockFields.getSector());
				JLabel openPrice = new JLabel("           Open Price: $" + specificStockFields.getOpenPrice());
				JLabel closePrice = new JLabel("           Close Price: $" + specificStockFields.getClosePrice());
				JLabel highPrice = new JLabel("           High Price: $" + specificStockFields.getHighPrice());
				JLabel lowPrice = new JLabel("           Low Price: $" + specificStockFields.getLowPrice());
				JLabel peRatio = new JLabel("           Per Earnings Ratio: $" + specificStockFields.getPeRatio());
				JLabel week52High = new JLabel("           Week 52 High: $" + specificStockFields.getWeek52High());
				JLabel week52Low = new JLabel("           Week 52 Low: $" + specificStockFields.getWeek52Low());
				// add JLabel fields to the UI screen
				dataResultsGrid.add(data);
				dataResultsGrid.add(latestPrice);
				dataResultsGrid.add(sector);
				dataResultsGrid.add(openPrice);
				dataResultsGrid.add(closePrice);
				dataResultsGrid.add(highPrice);
				dataResultsGrid.add(lowPrice);
				dataResultsGrid.add(peRatio);
				dataResultsGrid.add(week52High);
				dataResultsGrid.add(week52Low);


				// create two open columns between data
				// for(int i = 0; i < 4; i++){
				// searchBarGrid.add(new JLabel(" "));
				// }

				// searchBarGrid.add(new JLabel("NEWS"));
				// make this jlabel a hyperlink for the source
				// JLabel for popular news about the stock
				// for(int i = 0; i < headlines.size(); i++){
				// chartPanel.add(new JLabel(headlines.elementAt(i)));
				// }
				// + ", Source: " + urlSources.elementAt(i)));
				// fix bug where a null input will crash the program
				updateChart();
				revalidate();
				repaint();
			}
		});

		// provide options to search for
		AutoCompleteDecorator.decorate(searchBarNames);
		AutoCompleteDecorator.decorate(searchBarSymb);

		// add panels to main menu border layout
		this.add(searchBarGrid, BorderLayout.LINE_START);
		this.add(dataResultsGrid, BorderLayout.CENTER);
		this.add(graphPanel, BorderLayout.CENTER);
		submitButton.doClick();
		this.revalidate();
		this.repaint();
	}

	private void updateChart() {
		cp.removeAll();
		try {
			createChart(testSymbol);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		graphPanel.add(cp.getXYChart(), BorderLayout.CENTER);
		graphPanel.setPreferredSize(graphPanel.getPreferredSize());
		revalidate();
		repaint();

	}
	
	

	private void redrawChart() {
		cp.removeAll();
		//graphPanel.add(cp.getTimeSeriesChart(), BorderLayout.CENTER);
		graphPanel.add(cp.getXYChart(), BorderLayout.CENTER);
		graphPanel.setPreferredSize(graphPanel.getPreferredSize());
		revalidate();
		repaint();
	}

	// get json from url and parse it in "parseAllStockjson"
	public void getAPIStocks(String urlString, int flag, String symbol) {

		try {
			URL url = new URL(urlString);
			String jsonResult = ApiFetch.getJson(url.toString());
			// System.out.println(jsonResult);
			// parse differently according to call
			if (flag == 0) {
				parseAllStockjson(jsonResult);
			} else {
				// parse the specific stock information
				parseSpecificStockJSON(jsonResult, symbol);
				// System.out.println(jsonResult);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// parse the array that comes back from flag 1
	public void parseAllStockjson(String json) throws JSONException {

		try {

			JSONArray arr = new JSONArray(json);

			for (int i = 0; i < arr.length(); i++) {
				String symbol = arr.getJSONObject(i).getString("symbol");
				String name = arr.getJSONObject(i).getString("name");
				// String combined = name + "-" + symbol;

				// populate vectors with symbols and names
				symbols.add(symbol);
				companyNames.add(name);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// parse specific stock information
	public void parseSpecificStockJSON(String json, String symbol) throws JSONException {

		// create quote json object
		JSONObject jObj = new JSONObject(json);
		JSONObject result = jObj.getJSONObject(symbol);
		JSONObject result1 = result.getJSONObject("quote");

		// retrieve fields from quote section via POJO
		specificStockFields.setCompanyName(result1.getString("companyName"));
		specificStockFields.setSector(result1.getString("sector"));
		specificStockFields.setOpenPrice(result1.getString("open"));
		specificStockFields.setClosePrice(result1.getString("close"));
		specificStockFields.setHighPrice(result1.getString("high"));
		specificStockFields.setLowPrice(result1.getString("low"));
		specificStockFields.setLatestPrice(result1.getString("latestPrice"));
		specificStockFields.setPeRatio(result1.getString("peRatio"));
		specificStockFields.setWeek52High(result1.getString("week52High"));
		specificStockFields.setWeek52Low(result1.getString("week52Low"));

		// news portion of the API
		JSONArray news = result.getJSONArray("news");

		// parse the news array
		for (int i = 0; i < news.length(); i++) {
			String headline = news.getJSONObject(i).getString("headline");
			String urlString = news.getJSONObject(i).getString("url");

			// create list of headlines and url sources
			headlines.add(headline);
			urlSources.add(urlString);
		}

		// get data to display in a chart from the last 20 days
		JSONArray chart = result.getJSONArray("chart");
		// parse the chart array
		for (int i = 0; i < chart.length(); i++) {
			String closeP = chart.getJSONObject(i).getString("close");
			String date = chart.getJSONObject(i).getString("date");
			// System.out.println("date:" + date);

			// create list of closed prices for the last 20 days
			closedPrices.add(closeP);
			priceDates.add(date);
			// System.out.println(closeP);
		}

	}

	private void createChart(String symbol) throws ParseException {

		// pass symbol name
		TimeSeries series = new TimeSeries(symbol);

		// adds data to series to be used in chart
		for (int i = 0; i < closedPrices.size(); i++) {
			// parse dates to accurately fit prices
			Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(priceDates.get(i));
			// String daysStr = new SimpleDateFormat("MM.dd").format(date1);
			// System.out.println("day: " + daysStr);

			series.add(new Day(date1), Double.parseDouble(closedPrices.elementAt(i)));
		} // first input param is x axis, 2nds is y axis

		// Add the series to your data set
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(series);
		this.cp.addDataset(dataset);
		this.cp.setTitle(this.companyNames.get(symbols.indexOf(symbol)));
	}

}// end of class
