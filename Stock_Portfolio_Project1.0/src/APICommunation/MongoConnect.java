package APICommunation;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoConnect {

	private final static String MONGOURL = "mongodb+srv://admin:admin@portfolio-6xax6.mongodb.net/";

	/*
	 * Searches for the username string entered by the user in the database. If
	 * the username is found in the db, it searches for the password. If the
	 * password string the user entered matches up with the password string in
	 * the database (AND the username checks out), then the user is logged in.
	 * Returns an int, If returnCode = 0, the username or password was incorrect
	 * if returnCode = 1, the password was incorrect if returnCode = 2, the user
	 * logged in
	 */
	public static int login(String username, String password) throws ParseException {
		int returnCode = 0;
		MongoClientURI uri = new MongoClientURI(MONGOURL);
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("portfolio");
		MongoCollection<Document> col = database.getCollection("users");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("username", username);
		String getUser, getPass;
		String jObject = null;
		JSONParser parser = new JSONParser();

		try (MongoCursor<Document> cur = col.find(whereQuery).iterator()) {
			if (cur.hasNext()) {
				Document tobj = cur.next();
				jObject = tobj.toJson();
				JSONObject json = (JSONObject) parser.parse(jObject);

				getUser = json.get("username").toString();
				getPass = json.get("pwd").toString();
				System.out.println(username + "" + getUser);
				System.out.println(password + "" + getPass);
				if (username.equals(getUser)) {
					if (password.equals(getPass)) {
						returnCode = 2;
					} else {
						returnCode = 1;
					}
				}
			} else {
				returnCode = 0;
			}

		}

		mongoClient.close();
		return returnCode;
	}

	// Checks to see if username already exists or not when a user tries to
	// create an account
	public static Boolean checkUsernameExist(String username) {
		Boolean userExists = null;
		MongoClientURI uri = new MongoClientURI(MONGOURL);
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("portfolio");
		MongoCollection<Document> collection = database.getCollection("users");
		BasicDBObject whereQuery = new BasicDBObject();
	    whereQuery.put("username", username);
	   
		try (MongoCursor<Document> cur = collection.find(whereQuery).iterator()) {
			if (cur.hasNext()) {
					userExists = true;
				} if(!cur.hasNext()) {
					userExists = false;
			}
		}
		mongoClient.close();
		return userExists;
	}

	// Creates user in DB with username and password strings added by the user
	public static void createUser(String username, String password) throws IOException {
		MongoClientURI uri = new MongoClientURI(MONGOURL);
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("portfolio");
		MongoCollection<Document> col = database.getCollection("users");

		Document document = new Document();
		document.put("username", username);
		Document document2 = new Document();
		document.put("pwd", password);
		col.insertOne(document);
		col.insertOne(document2);
		mongoClient.close(); //test
		
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		String os = getOS();
		if(os.equals("mac")){
			String myDocPath = System.getProperty("user.home")+File.separator+"Documents";
			File configFile = new File(myDocPath+"/Portfolio/config.txt");
			configFile.getParentFile().mkdir();
			configFile.createNewFile();
			

			try {

				fw = new FileWriter(configFile);
				bw = new BufferedWriter(fw);
				bw.write(username+"\n"+password);

			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {

					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}

			}
			
		} else if(os.equals("win")) {
			String myDocPath = System.getProperty("user.home") + "Documents";
			File configFile = new File(myDocPath+"/Portfolio/config.txt");
			File portFile = new File(myDocPath+"/Portfolio/portfolio.txt");
			File favFile = new File(myDocPath+"/Portfolio/favorites.txt");
			configFile.getParentFile().mkdir();
			configFile.createNewFile();
		
			try {

				fw = new FileWriter(configFile);
				bw = new BufferedWriter(fw);
				bw.write(username+"\n"+password);
				

			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {

					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}

			}
		} 
	

	}
	
	public static String getOS() {
		String opSys;
	      String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
	      if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
	        opSys = "mac";
	      } else if (OS.indexOf("win") >= 0) {
	        opSys = "win";
	      } else if (OS.indexOf("nux") >= 0) {
	        opSys = "linux";
	      } else {
	        opSys = "other";
	      }
	
	    return opSys;
	  }
	
	public static void addPortfolio(String symbol) throws IOException {
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		File f = new File(System.getProperty("user.home")+File.separator+"Documents/Portfolio/favs.txt");
		if(f.exists() && !f.isDirectory()) { 
			List<String> symbolList = new ArrayList<String>();
			  try {
			
			    BufferedReader reader = new BufferedReader(new FileReader(f));
			    String getSymbol;
			    int alreadyExists = 3;
			    if(reader.ready()){
			    	
			    	while (reader.ready()) {
			    		
			    		getSymbol = reader.readLine().toString();
			    		if (!getSymbol.equals(symbol)) {
			    			System.out.println(getSymbol + " " + symbol);
			    			symbolList.add(getSymbol);		
			    		} else {
			    			alreadyExists = 1;
			    			break;
			    		}
			    		alreadyExists = 0;
			    	}
			    	System.out.println(alreadyExists);
			    	reader.close();
				    if(alreadyExists == 0) {
				    String newline = "\n";
				    String uri = f.toString();	
				    	try {
				    		Files.write(Paths.get(uri), newline.getBytes(), StandardOpenOption.APPEND);
				    	    Files.write(Paths.get(uri), symbol.getBytes(), StandardOpenOption.APPEND);
				    	}catch (IOException e) {
				    	    //exception handling left as an exercise for the reader
				    	}			    
				    }
			    
			    } else {
			    	fw = new FileWriter(f);
					bw = new BufferedWriter(fw);
				
					char c [];
					c = symbol.toCharArray();
					for(int i = 0 ;i < c.length; i++) {
						bw.append(c[i]);	
					}
					
					bw.close();
			    }
			  
			
			  } catch (Exception e) {
			    System.err.format("File read error");
			    
			    e.printStackTrace();
			    }
		} else {
			String makeFaves = System.getProperty("user.home")+File.separator+"Documents/Portfolio";
			File configFile = new File(makeFaves+"/favs.txt");
			configFile.getParentFile().mkdir();
			configFile.createNewFile();
		}
		
		
	}
}
