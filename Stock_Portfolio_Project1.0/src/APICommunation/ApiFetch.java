package csi480;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ApiFetch {
	
	
	//retrieve json from a url string and return it to the main
			public static String getJson(String url) throws Exception{
			try {
			      URL request = new URL(url);
			      URLConnection connection = request.openConnection();
			      
			      //timeout if connection is stuck
			      connection.setConnectTimeout(1000);
			      connection.setReadTimeout(1000);

			      InputStreamReader inputStream = new InputStreamReader(connection.getInputStream(), "UTF-8");
			      BufferedReader bufferedReader = new BufferedReader(inputStream);
			      StringBuilder responseBuilder = new StringBuilder();

			      String line;
			      while ((line = bufferedReader.readLine()) != null) {
			        responseBuilder.append(line);
			      }
			      bufferedReader.close();
			      return responseBuilder.toString();
			    } catch (IOException e) {
			      throw new Exception("failure sending request", e);
			    }
			}
}