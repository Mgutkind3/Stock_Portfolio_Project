package APICommunation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class stockSearchPanel extends JPanel {
	
	//impliment lists to house data
	public Vector<String> closedPrices = new Vector<String>();
	public Vector<String> urlSources = new Vector<String>();
	public Vector<String> headlines = new Vector<String>();
	public Vector<String> symbols = new Vector<String>();
	public Vector<String> companyNames = new Vector<String>();
	private apiFetchFunction apiFetchObj = new apiFetchFunction();
	private parseSpecificStockData specificStockFields = new parseSpecificStockData();
	private apiFetch jsonFetch = new apiFetch();
	String baseUrl = "https://api.iextrading.com/1.0/";

	
		public stockSearchPanel() {
			setLayout(new BorderLayout());
			setBorder(apiFetchObj.createTitle("Search Stocks"));
			
			//get api response for all stocks and stock symbols available
			 getAPIStocks(baseUrl + "ref-data/symbols", 0, null);
			 
			 //JComboBox for names
			JComboBox<String> searchBarNames = new JComboBox<String>(companyNames);
			searchBarNames.setEditable(false);
			searchBarNames.setPrototypeDisplayValue("Search Box");
			
			
			//JComboBox for symbols
			JComboBox<String> searchBarSymb = new JComboBox<String>(symbols);
			searchBarSymb.setEditable(false);
			searchBarSymb.setPrototypeDisplayValue("Search Box");
			
			//add action listeners to correlate boxes with each other (Match)
			searchBarNames.addActionListener(
					new ActionListener(){
						 
						public void actionPerformed(ActionEvent e)
				            {
							searchBarSymb.setSelectedIndex(searchBarNames.getSelectedIndex());
				            }
							
			});
			
			searchBarSymb.addActionListener(
					new ActionListener(){
						 
						public void actionPerformed(ActionEvent e)
				            {
							searchBarNames.setSelectedIndex(searchBarSymb.getSelectedIndex());	
				            }
							
			});
			
			//JLabel
			JLabel titleInstructionsNames = new JLabel("Select or Type in a Stock Name/Symbol");
			JLabel titleInstructionsSymb = new JLabel("Or");
			
			//JButton
			JButton submitButton = new JButton("Submit");
			
			//JPanel for search and instruction labels (left panel)
			JPanel searchBarGrid = new JPanel();
			searchBarGrid.setLayout(new GridLayout(10,1));
			searchBarGrid.add(titleInstructionsNames);
			searchBarGrid.add(searchBarNames);
			searchBarGrid.add(titleInstructionsSymb);
			searchBarGrid.add(searchBarSymb);
			searchBarGrid.add(submitButton);
			
			//JPanel for data results (middle panel)
			JPanel dataResultsGrid = new JPanel();
			dataResultsGrid.setLayout(new GridLayout(10,1));
			
			//JPanel for the chart
			JPanel chartPanel = new JPanel();
			
			submitButton.addActionListener(new ActionListener(){
				 
				public void actionPerformed(ActionEvent e)
		            {
					//remove current stocks data
					dataResultsGrid.removeAll();
					chartPanel.removeAll();
					
					String symbolSelected = symbols.elementAt(searchBarNames.getSelectedIndex());

						//only searching for names combo box now, make it so one combo box is disabled
		                //use latest price field for the latest price (never null)
		                getAPIStocks(baseUrl + "stock/market/batch?symbols="+ symbolSelected +"&types=quote,news,chart", 1, symbolSelected );     
		                
		    			//JLabel's for the specific stock data and fields
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
		                //add JLabel fields to the UI screen
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
		                
		    			//create chart and add it to the right panel
		   			 	JFreeChart chart = createChart(symbolSelected);
		                chartPanel.removeAll();
		                chartPanel.add(new ChartPanel(chart));
		                
		               revalidate();
		               repaint();
		                
		            }
			});
			
			

			//provide options to search for
			AutoCompleteDecorator.decorate(searchBarNames); 
			AutoCompleteDecorator.decorate(searchBarSymb);
			
			
			add(searchBarGrid, BorderLayout.LINE_START);
			add(dataResultsGrid, BorderLayout.CENTER);
			add(chartPanel, BorderLayout.LINE_END);
			

		}
	
		//get json from url and parse it in "parseAllStockjson"
		public void getAPIStocks(String urlString, int flag, String symbol){
			
			try {
				URL url = new URL(urlString);
				String jsonResult = jsonFetch.getJson(url.toString());
				//System.out.println(jsonResult);
				//parse differently according to call
				if(flag == 0){
				parseAllStockjson(jsonResult);
				}else{
					//parse the specific stock information
					parseSpecificStockJSON(jsonResult, symbol );
					//System.out.println(jsonResult);
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		//parse the array that comes back from flag 1
		public void parseAllStockjson(String json) throws JSONException{
			
			try {
			
				JSONArray arr = new JSONArray(json);
				
				for (int i = 0; i < arr.length(); i++){
					String symbol = arr.getJSONObject(i).getString("symbol"); 
					String name = arr.getJSONObject(i).getString("name");
					//String combined = name + "-" + symbol;

					//populate vectors with symbols and names
					symbols.add(symbol);
					companyNames.add(name);
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//parse specific stock information
		public void parseSpecificStockJSON(String json, String symbol) throws JSONException{
			
			//create quote json object
			JSONObject jObj = new JSONObject(json);
			JSONObject result = jObj.getJSONObject(symbol);
			JSONObject result1 = result.getJSONObject("quote");

			
			//retrieve fields from quote section via POJO
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
			

			//news portion of the API
			JSONArray news = result.getJSONArray("news");
			
			//parse the news array
			for (int i = 0; i < news.length(); i++){
				String headline = news.getJSONObject(i).getString("headline"); 
				String urlString = news.getJSONObject(i).getString("url");
				
				//create list of headlines and url sources
				headlines.add(headline);
				urlSources.add(urlString);
			}
			
			//get data to display in a chart from the last 20 days
			JSONArray chart = result.getJSONArray("chart");
			//parse the chart array
			for (int i = 0; i < chart.length(); i++){
				String closeP = chart.getJSONObject(i).getString("close"); 
				
				//create list of closed prices for the last 20 days
				closedPrices.add(closeP);
				System.out.println(closeP);
			}
				
			
		}
		
		//include chart
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        Graphics2D g2d = (Graphics2D) g.create();
	        g2d.dispose();
	    }
	 
	    private JFreeChart createChart(String symbol) {
	    	//put stock symbol there
	        XYSeries series = new XYSeries(symbol);
	 
	        // adds data to series to be used in chart
	        for (int i = 0; i < closedPrices.size(); i++) {
	            series.add((i), Double.parseDouble(closedPrices.elementAt(i)));
	        }//first input param is x axis, 2nds is y axis
	 
	        // Add the series to your data set
	        XYSeriesCollection dataset = new XYSeriesCollection();
	        dataset.addSeries(series);
	 
	        // Generate the graph
	        JFreeChart chart = ChartFactory.createXYLineChart(symbol, // Title
	                "Time (x-axis)", // x-axis Label
	                "Price (y-axis)", // y-axis Label
	                dataset, // Dataset
	                PlotOrientation.VERTICAL, // Plot Orientation
	                true, // Show Legend
	                true, // Use tooltips
	                false // Configure chart to generate URLs?
	        );
	 
	        
	          XYPlot xyPlot = (XYPlot) chart.getPlot();
	            xyPlot.setDomainCrosshairVisible(true);
	            xyPlot.setRangeCrosshairVisible(true);
	          
	            XYItemRenderer renderer = xyPlot.getRenderer();
	            renderer.setSeriesPaint(0, Color.blue);
	    
	          
	            //y-axis
	            NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
	            range.setAutoRange(true);
	            range.setAutoRangeIncludesZero(false);
	            
	        return chart;
	 
	    }

}//end of class

