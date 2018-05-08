package csi480;

import java.awt.event.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StockSearchPanel extends JPanel {

	// Implement lists to house data
	private int timeIndicator;
	private Vector<String> priceDates = new Vector<String>();
	private Vector<String> closedPrices = new Vector<String>();
	private static Vector<String> urlSources = new Vector<String>();
	private static Vector<String> headlines = new Vector<String>();
	private Vector<String> symbols = new Vector<String>();
	private Vector<String> companyNames = new Vector<String>();
	private Vector<String> monthPrices = new Vector<String>();
	private Vector<String> monthDates = new Vector<String>();
	private Vector<String> month6Prices = new Vector<String>();
	private Vector<String> month6Dates = new Vector<String>();
	private Vector<String> yearPrices = new Vector<String>();
	private Vector<String> yearDates = new Vector<String>();
	private Vector<JButton> buttonList = new Vector<JButton>();
	private CPanel cp = new CPanel();
	private JPanel graphPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private ParseSpecificStockData specificStockFields = new ParseSpecificStockData();
	private final String baseUrl = "https://api.iextrading.com/1.0/";
	private static String newsSymbol;
	final private JLabel errorMessage = new JLabel("Missing data from API");

	public StockSearchPanel() {
		this.timeIndicator = 0;
		this.setLayout(new BorderLayout());
		this.setBorder(MainFrame.createTitle("Search Stocks"));
		this.cp.setyAxsis("Dollars");

		// get api response for all stocks and stock symbols available
		getAPIStocks(baseUrl + "ref-data/symbols", 0, null, 5);

		// JComboBox for names
		JComboBox<String> searchBarNames = new JComboBox<String>(companyNames);
		searchBarNames.setEditable(false);
		searchBarNames.setPrototypeDisplayValue("Search Box");

		// JComboBox for symbols
		JComboBox<String> searchBarSymb = new JComboBox<String>(symbols);
		searchBarSymb.setEditable(false);
		searchBarSymb.setPrototypeDisplayValue("Search Box");

		// JButton
		JButton submitButton = new JButton("Submit");
		JButton addButton = new JButton("Add to graph");
		JButton addFavorite = new JButton("Add to favorites");
		JButton monthButton = new JButton("Month");
		JButton halfYearButton = new JButton("6 Months");
		JButton yearButton = new JButton("Year");
		buttonList.add(monthButton);
		buttonList.add(halfYearButton);
		buttonList.add(yearButton);

		// add action listeners to correlate boxes with each other (Match below)
		searchBarNames.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchBarSymb.setSelectedIndex(searchBarNames.getSelectedIndex());
				addFavorite.setEnabled(false);
			}
		});
		// add action listeners to correlate boxes with each other (Match above)
		searchBarSymb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchBarNames.setSelectedIndex(searchBarSymb.getSelectedIndex());
				addFavorite.setEnabled(false);
			}

		});

		// JLabels for the instructions
		JLabel titleInstructionsNames = new JLabel("Select or Type in a Stock Name/Symbol");
		JLabel titleInstructionsSymb = new JLabel("Or");

		// JPanel for search and instruction labels (left panel)
		JPanel searchBarGrid = new JPanel();
		searchBarGrid.setLayout(new GridLayout(25, 1));
		searchBarGrid.add(titleInstructionsNames);
		searchBarGrid.add(searchBarNames);
		searchBarGrid.add(titleInstructionsSymb);
		searchBarGrid.add(searchBarSymb);
		searchBarGrid.add(submitButton);
		searchBarGrid.add(addButton);
		searchBarGrid.add(addFavorite);

		// JPanel for data results (middle panel)
		JPanel dataResultsGrid = new JPanel();
		dataResultsGrid.setLayout(new GridLayout(25, 1));
		dataResultsGrid.setPreferredSize(new Dimension(200, 300));

		this.graphPanel.setLayout(new BorderLayout());
		graphPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				redrawChart();
				graphPanel.repaint();
			}
		});

		// Time series JButtons and get current time
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-M-d");
		LocalDateTime now = LocalDateTime.now();
		String todayDate = dtf.format(now).toString();

		monthButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timeIndicator = 0;
				errorMessage.setVisible(false);
				ArrayList<String> currentSymbols = cp.getStockSymbols();
				cp.removeAllDataset();
				for (int i = 0; i < currentSymbols.size(); i++) {
					monthPrices.clear();
					monthDates.clear();

					System.out.println(currentSymbols.get(i) + " is stock number " + i);

					getAPIStocks(baseUrl + "stock/market/batch?symbols=" + currentSymbols.get(i) + "&types=quote,news,chart",1, currentSymbols.get(i), 5);
					updateChart(currentSymbols.get(i));
				}
				revalidate();
				repaint();
			}
		});

		String last6MonthString = get6MonthBefore();
		halfYearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timeIndicator = 1;
				errorMessage.setVisible(false);
				ArrayList<String> currentSymbols = cp.getStockSymbols();
				cp.removeAllDataset();
				for (int i = 0; i < currentSymbols.size(); i++) {
					month6Prices.clear();
					month6Dates.clear();

					System.out.println(currentSymbols.get(i) + " is stock number " + i);
					getAPIStocks("https://www.quandl.com/api/v3/datasets/WIKI/" + currentSymbols.get(i)
							+ ".json?column_index=4&start_date=" + last6MonthString + "&end_date=" + todayDate
							+ "&collapse=daily&api_key=ZGGxFod_7TVXrEU-UeuL", 2, null, 1);
					updateChartHalfYear(currentSymbols.get(i));
				}
				revalidate();
				repaint();
			}
		});

		String lastYearString = getYearBefore();
		yearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timeIndicator = 2;
				errorMessage.setVisible(false);
				ArrayList<String> currentSymbols = cp.getStockSymbols();
				cp.removeAllDataset();

				for (int i = 0; i < currentSymbols.size(); i++) {
					yearDates.clear();
					yearPrices.clear();
					getAPIStocks("https://www.quandl.com/api/v3/datasets/WIKI/" + currentSymbols.get(i)
							+ ".json?column_index=4&start_date=" + lastYearString + "&end_date=" + todayDate
							+ "&collapse=daily&api_key=ZGGxFod_7TVXrEU-UeuL", 2, null, 2);
					updateChartYear(currentSymbols.get(i));
				}

				revalidate();
				repaint();
			}
		});

		// Adds to the Favorites on the SummaryPanel
		addFavorite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// add to favorites list
				SummaryPanel.addFavorite(symbols.elementAt(searchBarNames.getSelectedIndex()),
						specificStockFields.getChangePercent());
			}
		});

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// remove current stocks data
				dataResultsGrid.removeAll();
				//closedPrices.clear();
				//priceDates.clear();
				
				headlines.clear();
				urlSources.clear();

				String symbolSelected = symbols.elementAt(searchBarNames.getSelectedIndex());
				newsSymbol = symbolSelected;
				// use latest price field for the latest price (never null)
				getAPIStocks(baseUrl + "stock/market/batch?symbols=" + symbolSelected + "&types=quote,news,chart", 1,
						symbolSelected, 5);

				// JLabel's for the specific stock data and fields
				JLabel data = new JLabel(specificStockFields.getCompanyName());
				JLabel latestPrice = new JLabel("Price: $" + specificStockFields.getLatestPrice());
				JLabel sector = new JLabel("Sector: " + specificStockFields.getSector());
				JLabel openPrice = new JLabel("Open Price: $" + specificStockFields.getOpenPrice());
				JLabel closePrice = new JLabel("Close Price: $" + specificStockFields.getClosePrice());
				JLabel highPrice = new JLabel("High Price: $" + specificStockFields.getHighPrice());
				JLabel lowPrice = new JLabel("Low Price: $" + specificStockFields.getLowPrice());
				JLabel peRatio = new JLabel("Per Earnings Ratio: $" + specificStockFields.getPeRatio());
				JLabel week52High = new JLabel("Week 52 High: $" + specificStockFields.getWeek52High());
				JLabel week52Low = new JLabel("Week 52 Low: $" + specificStockFields.getWeek52Low());
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

				updateChart(symbolSelected);
				buttonList.get(timeIndicator).doClick();
				addFavorite.setEnabled(true);
				revalidate();
				repaint();

			}
		});

		submitButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// remove current stocks data
				cp.removeAllDataset();
				addButton.doClick();
				addFavorite.setEnabled(true);
			}
		});

		// provide options to search for
		AutoCompleteDecorator.decorate(searchBarNames);
		AutoCompleteDecorator.decorate(searchBarSymb);

		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(monthButton);
		buttonPanel.add(halfYearButton);
		buttonPanel.add(yearButton);

		errorMessage.setForeground(Color.RED);
		JPanel helperPanel = new JPanel();
		helperPanel.setLayout(new BorderLayout());
		helperPanel.add(buttonPanel, BorderLayout.CENTER);
		helperPanel.add(errorMessage, BorderLayout.NORTH);

		graphPanel.add(helperPanel, BorderLayout.SOUTH);

		// add panels to main menu border layout
		this.add(searchBarGrid, BorderLayout.LINE_START);
		this.add(dataResultsGrid, BorderLayout.LINE_END);
		this.add(graphPanel, BorderLayout.CENTER);
		submitButton.doClick();
		this.revalidate();
		this.repaint();
	}

	private void updateChartYear(String addSymbol) {
		cp.removeAll();
		try {
			createChartYear(addSymbol);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		addToGraphPanel();

	}

	private void updateChart(String addSymbol) {
		cp.removeAll();
		try {
			createChart(addSymbol);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		addToGraphPanel();

	}

	private void updateChartHalfYear(String addSymbol) {
		cp.removeAll();
		try {
			createChartHalfYear(addSymbol);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		addToGraphPanel();

	}

	private void createChartYear(String symbol) throws ParseException {
		// pass symbol name
		TimeSeries series = new TimeSeries(symbol);

		// adds data to series to be used in chart
		for (int i = 0; i < yearPrices.size(); i++) {
			// parse dates to accurately fit prices
			Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(yearDates.get(i));
			series.addOrUpdate(new Day(date1), Double.parseDouble(yearPrices.elementAt(i)));
		} // first input param is x axis, 2nds is y axis

		addSeriesToChart(series, symbol);

	}

	private void addToGraphPanel() {
		graphPanel.add(cp.getTimeSeriesChart(), BorderLayout.CENTER);
		graphPanel.setPreferredSize(graphPanel.getPreferredSize());
		revalidate();
		repaint();

	}

	private void createChartHalfYear(String symbol) throws ParseException {
		// pass symbol name
		TimeSeries series = new TimeSeries(symbol);

		// adds data to series to be used in chart
		for (int i = 0; i < month6Prices.size(); i++) {
			// parse dates to accurately fit prices
			Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(month6Dates.get(i));
			// TODO make it month
			series.addOrUpdate(new Day(date1), Double.parseDouble(month6Prices.elementAt(i)));
		} // first input param is x axis, 2nds is y axis

		// Add the series to your data set
		addSeriesToChart(series, symbol);
	}

	private void addSeriesToChart(TimeSeries series, String symbol) {
		if (series.isEmpty()) {
			errorMessage.setVisible(true);
		} 
		// Add the series to your data set
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(series);
		this.cp.addDataset(dataset, symbol);
		this.cp.setTitle(this.companyNames.get(symbols.indexOf(symbol)));
	}

	private void redrawChart() {
		cp.removeAll();
		graphPanel.add(cp.getTimeSeriesChart(), BorderLayout.CENTER);
		graphPanel.setPreferredSize(graphPanel.getPreferredSize());
		revalidate();
		repaint();
	}

	// get json from url and parse it in "parseAllStockjson"
	public void getAPIStocks(String urlString, int flag, String symbol, int timeseriesFlag) {

		try {
			URL url = new URL(urlString);
			String jsonResult = ApiFetch.getJson(url.toString());
			// System.out.println(jsonResult);
			// parse differently according to call
			if (flag == 0) {
				parseAllStockjson(jsonResult);
			}
			if (flag == 1) {
				// parse the specific stock information
				parseSpecificStockJSON(jsonResult, symbol);
			}
			if (flag == 2) {
				parseTimeSeriesJSON(jsonResult, timeseriesFlag);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// get 6 months ago from today
	private String get6MonthBefore() {
		Date date = new Date();
		long DAY_IN_MS = 1000 * 60 * 60 * 24;
		new Date(System.currentTimeMillis() - (30 * DAY_IN_MS));

		Date lastMonth = new Date(date.getTime() - (183 * DAY_IN_MS));
		String last6MonthString = lastMonth.getYear() + 1900 + "-" + (lastMonth.getMonth() + 1) + "-"
				+ lastMonth.getDate();

		return last6MonthString;
	}

	// get year ago from today
	private String getYearBefore() {
		Date date = new Date();
		long DAY_IN_MS = 1000 * 60 * 60 * 24;
		new Date(System.currentTimeMillis() - (30 * DAY_IN_MS));

		Date lastMonth = new Date(date.getTime() - (365 * DAY_IN_MS));
		String last6MonthString = lastMonth.getYear() + 1900 + "-" + (lastMonth.getMonth() + 1) + "-"
				+ lastMonth.getDate();

		return last6MonthString;
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

	// parse the timeseries data that is coming through
	public void parseTimeSeriesJSON(String json, int flag) throws JSONException {
		// parse month data

		JSONObject jObj = new JSONObject(json);
		JSONObject dataset = jObj.getJSONObject("dataset");
		JSONArray data = dataset.getJSONArray("data");

		for (int i = 0; i < data.length(); i++) {
			JSONArray monthPricesArr = data.getJSONArray(i);
			String monthPricesString = monthPricesArr.getString(1);
			String monthDatesString = monthPricesArr.getString(0);

			// parse month data
			if (flag == 0) {

				monthPrices.addElement(monthPricesString);
				monthDates.addElement(monthDatesString);
				// System.out.println("dataset: " + monthPricesString);
			}
			// parse 6 month data
			if (flag == 1) {

				month6Prices.addElement(monthPricesString);
				month6Dates.addElement(monthDatesString);
				// System.out.println("dataset 6 months: " + monthPricesString);
			}
			// parse year data
			if (flag == 2) {
				yearPrices.addElement(monthPricesString);
				yearDates.addElement(monthDatesString);
				// System.out.println("dataset 1 year: " + monthPricesString);

			}

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
			String changePercent = chart.getJSONObject(i).getString("changePercent");
			// System.out.println("change %: " + changePercent);

			// create list of closed prices for the last 20 days
			closedPrices.add(closeP);
			priceDates.add(date);
			specificStockFields.setChangePercent(changePercent);
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

			series.addOrUpdate(new Day(date1), Double.parseDouble(closedPrices.elementAt(i)));
		} // first input param is x axis, 2nds is y axis

		// Add the series to your data set
		addSeriesToChart(series, symbol);
	}

	public Vector<String> getHeadlines() {
		return StockSearchPanel.headlines;
	}

	public Vector<String> getUrlSources() {
		return StockSearchPanel.urlSources;
	}

	public static String getSymbol() {
		return newsSymbol;
	}

}// end of class