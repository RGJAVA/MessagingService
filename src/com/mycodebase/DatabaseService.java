package com.mycodebase;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

public class DatabaseService {
		
	public MongoDatabase connectToDB() { 
		
			MongoClient mongo = new MongoClient("localhost",27017);
			MongoDatabase db = mongo.getDatabase("EmailDB");
			return db;		
	}
	
	public boolean checkUserExistsAndSignUp(String attemptedUserName, String firstN, String lastN, 
			 String pass) throws IOException { 
		boolean alreadyExists = true;
		MongoDatabase db = connectToDB(); 
		MongoCollection<Document> collection = db.getCollection("Users");
		FindIterable<Document> iterable  = collection.find();
		for(Document doc :iterable) {
			if (attemptedUserName.equals((String)doc.get("emailAddress"))){ 
				return alreadyExists; 
			}
		}
		Document doc = new Document();
		doc.put("firstName", firstN);
		doc.put("lastName", lastN);
		doc.put("emailAddress", attemptedUserName);
		collection.insertOne(doc);
		BufferedWriter output = new BufferedWriter(new FileWriter(new File("C:\\Users\\Roshan\\Desktop\\loginpass.txt"), true));
		output.newLine();
		output.write(attemptedUserName + "," + pass);
		output.flush();
		output.close();
		postToDB("admin@yellow.com", attemptedUserName, "GREETINGS!", "Welcome to Yellow Email");
		
		return false; 
}
	
	public void postToDB(String from, String to, String heading, String message){ 
		MongoDatabase db = connectToDB(); 
		MongoCollection<Document> collection = db.getCollection("Emails");
		Document document = new Document(); 
		document.put("To", to);
		document.put("From", from);
		document.put("Heading", heading);
		document.put("Message", message);
		document.put("Date", new Date());
		document.put("Unread", "Yes");
		collection.insertOne(document);
	
	}
	public String getFirstName(String emailAddress) { 
		MongoDatabase db = connectToDB(); 
		MongoCollection<Document> collection = db.getCollection("Users");
		FindIterable<Document> iterable  = collection.find(); 
		for(Document doc :iterable) { 
			if(((String)doc.getString("emailAddress")).equals(emailAddress)) {
				return (String) doc.get("firstName");
			}
		}
		return emailAddress;
	}
		
	public void deleteEmail(String uID) { 
		MongoDatabase db = connectToDB(); 
		MongoCollection<Document> collection = db.getCollection("Emails");
		FindIterable<Document> iterable  = collection.find();
		 
			for(Document doc :iterable) { 
				if(((String)doc.getObjectId("_id").toString()).equals(uID)) { 
					collection.deleteOne(doc);
					
				}
			}
	}
	
	
	public void changeReadStatus(String uID) { 
		MongoDatabase db = connectToDB(); 
		MongoCollection<Document> collection = db.getCollection("Emails");
		FindIterable<Document> iterable  = collection.find();
		 
			for(Document doc :iterable) { 
				if(((String)doc.getObjectId("_id").toString()).equals(uID)) { 
					BasicDBObject updatedDocument = new BasicDBObject();
					updatedDocument.append("$set", new BasicDBObject().append("Unread", "No"));
					BasicDBObject searchQuery = new BasicDBObject().append("_id", (doc.getObjectId("_id")));
					collection.updateOne(searchQuery, updatedDocument);
				}
			}
	}
	
	public List<Email> retrieveListOfEmails(String emailAddress)   { 
		List<Email> listOfEmails = new ArrayList<Email>();
		MongoDatabase db = connectToDB(); 
		MongoCollection<Document> collection = db.getCollection("Emails");
		FindIterable<Document> iterable  = collection.find();
		 
			for(Document doc :iterable) { 
				 if(emailAddress.equals((String)doc.get("To"))) {
				 Email email = new Email();
				// email.setTo((String)doc.get("To"));
				 email.setTo(emailAddress);
				 email.setuID((String)doc.getObjectId("_id").toString());
				 email.setFrom((String)doc.get("From"));
				 email.setHeading((String)doc.get("Heading"));
				 email.setMessage((String)doc.get("Message"));
				 email.setDate((Date)doc.get("Date"));
				 email.setUnread((String)doc.get("Unread"));
				 listOfEmails.add(email);
			} 	
			}
			System.out.println(listOfEmails.toString());
		return listOfEmails; 
	}
}
