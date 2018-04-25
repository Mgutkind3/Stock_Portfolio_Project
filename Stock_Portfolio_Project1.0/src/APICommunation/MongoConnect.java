package APICommunation;

import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
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
				getPass = json.get("pwd").toString();
				getUser = json.get("username").toString();

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
		// String getName;
		MongoClientURI uri = new MongoClientURI(MONGOURL);
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("portfolio");
		MongoCollection<Document> col = database.getCollection("users");
		BasicDBObject whereQuery = new BasicDBObject();

		whereQuery.put("username", username);
		try (MongoCursor<Document> cur = col.find(whereQuery).iterator()) {
			if (cur.hasNext()) {
				// Document tobj = cur.next();
				// getName = tobj.toJson();

				userExists = true;

			} else {

				userExists = false;
			}
		}

		mongoClient.close();
		return userExists;

	}

	// Creates user in DB with username and password strings added by the user
	public static void createUser(String username, String password) {
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

	}
}