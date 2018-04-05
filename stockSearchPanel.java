package csi480;


import java.awt.BorderLayout;

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
	
	private parseSpecificStockData specificStockFields = new parseSpecificStockData();
	private apiFetch jsonFetch = new apiFetch();
	String baseUrl = "https://api.iextrading.com/1.0/";
	
		public stockSearchPanel() {
			setLayout(new BorderLayout());
			setBorder(apiFetchFunction.createTitle("Search Stocks"));
			
			//get api response for all stocks and stock symbols available
			 getAPIStocks(baseUrl + "ref-data/symbols", 0, null);
			 
			 //JComboBox for names
			JComboBox<String> searchBarNames = new JComboBox<String>(companyNames);
			searchBarNames.setEditable(false);
			searchBarNames.setPrototypeDisplayValue("Search Box");
			
			//add action listeners to correlate boxes with each other (Match)
			
			//JComboBox for symbols
			JComboBox<String> searchBarSymb = new JComboBox<String>(symbols);
			searchBarSymb.setEditable(false);
			searchBarSymb.setPrototypeDisplayValue("Search Box");
			
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
			
			submitButton.addActionListener(new ActionListener(){
				 
				@Override
				public void actionPerformed(ActionEvent e)
		            {
					//remove current stocks
					dataResultsGrid.removeAll();

						//only searching for names combo box now, make it so one combo box is disabled
		                //use latest price field for the latest price (never null)
		                getAPIStocks(baseUrl + "stock/market/batch?symbols="+ symbols.elementAt(searchBarNames.getSelectedIndex()) +"&types=quote,news,chart", 1, symbols.elementAt(searchBarNames.getSelectedIndex()) );     
		                
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
		                

		               revalidate();
		                
		            }
			});
			
			

			//provide options to search for
	//		AutoCompleteDecorator.decorate(searchBarNames); 
	//		AutoCompleteDecorator.decorate(searchBarSymb);
			
			add(searchBarGrid, BorderLayout.LINE_START);
			add(dataResultsGrid, BorderLayout.CENTER);
			

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
				
				//create list of closed prices for the last 10 days
				closedPrices.add(closeP);
				System.out.println(closeP);
			}
				
			
		}

		
		
		
		
}//end of class