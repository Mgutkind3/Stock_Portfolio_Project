package csi480;

import java.awt.BorderLayout;

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
import javax.swing.JPanel;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataPanel extends JPanel implements ActionListener {

	// Implement lists to house data
	private Vector<String> closedPrices = new Vector<String>();
	private Vector<String> symbols = new Vector<String>();
	private Vector<String> companyNames = new Vector<String>();
	private Vector<String> priceDates = new Vector<String>();
	private CPanel cp = new CPanel();

	private ParseSpecificStockData data = new ParseSpecificStockData();
	final String baseUrl = "https://api.iextrading.com/1.0/";
	private int testStock = 1;
	private JPanel graphPanel = new JPanel();

	public DataPanel() {
		final JButton btnRandom = new JButton("Random Graph");
		final JButton btnAdd = new JButton("Add Series");
		this.setBorder(MainFrame.createTitle("Data"));

		// get api response for all stocks and stock symbols available
		getAPIStocks(baseUrl + "ref-data/symbols", 0, null);

		this.setLayout(new BorderLayout());
		this.graphPanel.setLayout(new BorderLayout());

		this.cp.setxAxsis("Days");
		this.cp.setyAxsis("Dollars");
		this.cp.setTitle("");

		btnRandom.setActionCommand("RANDOM");
		btnRandom.addActionListener(this);
		btnAdd.setActionCommand("ADD_DATASET");
		btnAdd.addActionListener(this);

		graphPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				redrawChart();
				graphPanel.repaint();
			}
		});

		this.add(graphPanel, BorderLayout.CENTER);
		this.add(btnRandom, BorderLayout.NORTH);
		this.add(btnAdd, BorderLayout.SOUTH);
		this.updateChart();
		this.revalidate();
		this.repaint();
	}

	public void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand().equals("ADD_DATASET")) {
			updateChart();
		} else if (e.getActionCommand().equals("RANDOM")) {
			cp.removeAllDataset();
			cp.setTitle("");
			updateChart();
		}

	}

	private void redrawChart() {
		cp.removeAll();
		graphPanel.add(cp.getXYChart(), BorderLayout.CENTER);
		graphPanel.setPreferredSize(graphPanel.getPreferredSize());
		revalidate();
		repaint();
	}

	private void updateChart() {
		cp.removeAll();
		createChart();
		graphPanel.add(cp.getXYChart(), BorderLayout.CENTER);
		graphPanel.setPreferredSize(graphPanel.getPreferredSize());
		revalidate();
		repaint();

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
		cp.addDataset(dataset);
	}

	// creates a chart from a random stock
	private void createChart() {
		testStock = new Random().nextInt(companyNames.size() - 1);
		getAPIStocks(baseUrl + "stock/market/batch?symbols=" + symbols.get(testStock) + "&types=quote,news,chart", 1,
				symbols.get(testStock));
		cp.setTitle(companyNames.get(testStock) + "\n" + cp.getTitle());
		XYSeries series = new XYSeries(symbols.get(testStock));

		// adds data to series to be used in chart
		for (int i = 0; i < closedPrices.size(); i++) {
			series.add((closedPrices.size() - i), Double.parseDouble(closedPrices.get(i)));
		}

		// Add the series to your data set
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		cp.addDataset(dataset);

	}

	// get json from url and parse it in "parseAllStockjson"
	private void getAPIStocks(String urlString, int flag, String symbol) {

		try {
			URL url = new URL(urlString);
			String jsonResult = ApiFetch.getJson(url.toString());

			// parse differently according to call
			if (flag == 0) {
				parseAllStockjson(jsonResult);
			} else {
				// parse the specific stock information
				parseSpecificStockJSON(jsonResult, symbol);

			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// parse the array that comes back from flag 1
	private void parseAllStockjson(String json) throws JSONException {

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
	private void parseSpecificStockJSON(String json, String symbol) throws JSONException {

		// create quote json object
		JSONObject jObj = new JSONObject(json);
		JSONObject result = jObj.getJSONObject(symbol);
		JSONObject result1 = result.getJSONObject("quote");

		// retrieve fields from quote section via POJO
		data.setCompanyName(result1.getString("companyName"));
		data.setSector(result1.getString("sector"));
		data.setOpenPrice(result1.getString("open"));
		data.setClosePrice(result1.getString("close"));
		data.setHighPrice(result1.getString("high"));
		data.setLowPrice(result1.getString("low"));
		data.setLatestPrice(result1.getString("latestPrice"));
		data.setPeRatio(result1.getString("peRatio"));
		data.setWeek52High(result1.getString("week52High"));
		data.setWeek52Low(result1.getString("week52Low"));

		closedPrices.clear();

		// get data to display in a chart from the last 20 days
		JSONArray chart = result.getJSONArray("chart");
		for (int i = 0; i < chart.length(); i++) {
			closedPrices.add(chart.getJSONObject(i).getString("close"));
			priceDates.add(chart.getJSONObject(i).getString("date"));
		}

	}

	public void setStockNumber(int number) {
		testStock = number;
	}
}