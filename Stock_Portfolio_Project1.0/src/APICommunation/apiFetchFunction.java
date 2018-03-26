package APICommunation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class apiFetchFunction {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
				//alpha vantage not working
				//URL testURL = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=MSFT&interval=1min&apikey=DINTRRTTDLSTY75Z");
				URL testURL = new URL("https://www.quandl.com/api/v3/datasets/WIKI/FB.json?column_index=4&start_date=2017-01-01&end_date=2017-12-31&collapse=monthly&transform=rdiff&api_key=ZGGxFod_7TVXrEU-UeuL");
				String result = getJson(testURL.toString());
				System.out.println(result);

	}
	
	//muy different
		public static String getJson(String url) throws Exception{
		try {
		      URL request = new URL(url);
		      URLConnection connection = request.openConnection();
//		      connection.setConnectTimeout(timeOut);
//		      connection.setReadTimeout(timeOut);

		      InputStreamReader inputStream = new InputStreamReader(connection.getInputStream(), "UTF-8");
		      BufferedReader bufferedReader = new BufferedReader(inputStream);
		      StringBuilder responseBuilder = new StringBuilder();

		      String line;
		      while ((line = bufferedReader.readLine()) != null) {
		        responseBuilder.append(line);
		      }
		      bufferedReader.close();
		      //System.out.println(responseBuilder.toString());
		      return responseBuilder.toString();
		    } catch (IOException e) {
		      throw new Exception("failure sending request", e);
		    }
		}

}
