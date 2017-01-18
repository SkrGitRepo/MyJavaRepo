package com.cisco.dataconnect.utils;

import java.net.UnknownHostException;

import com.mongodb.util.JSON;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public final class MongoDbInsert {
	

	private Mongo mongo             = null;
	private String serverName       = null ; 
	private int port                = 0; 
	private String collectionName   = null;
	private DBCollection collection = null; 
	private String dbName           = null; 
	private DBObject dbObject       = null; 

	
	
	public static void main(String[] args) 
	{
		boolean result          = false; 
		
		// TODO Auto-generated method stub
		MongoDbInsert mongoDbInsert = new MongoDbInsert(); 
		mongoDbInsert.setServerName("ibpmlab-01");
		mongoDbInsert.setPort(27017); 
		mongoDbInsert.openConnection();
		mongoDbInsert.setDBname("MyDb");
		mongoDbInsert.setCollectioName("MyCollection" );
		
		String json = "{ '_id' : '124', 'database' : 'mkyongDB2','table' : 'hosting'," +
				  "'detail' : {'records' : 99, 'index' : 'vps_index1', 'active' : 'true'}}}";

		result = mongoDbInsert.insert(json); 
		System.out.println("Insert Complete" + result );
		//mongoDbInsert.getAllRecords2();
		mongoDbInsert.getAllRecords();
		mongoDbInsert.printAllRecords();
		
		result = mongoDbInsert.remove(json); 
		System.out.println("Insert Complete" + result );
		//mongoDbInsert.getAllRecords2();
		mongoDbInsert.getAllRecords();
		mongoDbInsert.printAllRecords();
		
		
		mongoDbInsert.closeConnection();	
		
	}
	
	public void setCollectioName (String collectionName )
	{
		this.collectionName = collectionName; 
	}
	
	public String setCollectioName ()
	{
		return this.collectionName ; 
	}
	public void setDBname(String dbName)
	{
		this.dbName = dbName;
	}
	
	public String getDBName()
	{
		return this.dbName;
	}
	public DB getDB()
	{
		return  this.mongo.getDB(this.dbName);
	}
	
	public boolean closeConnection()
	{
		try
		{
		    this.mongo.close();
		} catch ( Exception e )
		{
			e.printStackTrace();
			return false; 
		}
		finally
		{
			// close & Clean all 
			this.mongo            = null;
			this.serverName       = null ; 
			this.port                = 0; 
			this.collectionName   = null;
			this.collection       = null; 
			this.dbName           = null; 
			
		}
		return true; 
	}
	
	
	public boolean openConnection() {

		//@SuppressWarnings("deprecation")
		
		try {
			this.mongo = new Mongo(this.serverName, this.port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false; 
		}
		return true; 
	}

	/**
	 * @param args
	 */
	public void test() {
		// TODO Auto-generated method stub


	 try {

			@SuppressWarnings("deprecation")
			Mongo mongo = new Mongo("ibpmlab-01", 27017);
			DB db = mongo.getDB("yourdb");

			DBCollection collection = db.getCollection("dummyColl");

			// 1. BasicDBObject example
			System.out.println("BasicDBObject example...");
			BasicDBObject document = new BasicDBObject();
			document.put("database", "mkyongDB");
			document.put("table", "hosting");

			BasicDBObject documentDetail = new BasicDBObject();
			documentDetail.put("records", 99);
			documentDetail.put("index", "vps_index1");
			documentDetail.put("active", "true");
			document.put("detail", documentDetail);

			collection.insert(document);

			DBCursor cursorDoc = collection.find();
			while (cursorDoc.hasNext()) {
				System.out.println(cursorDoc.next());
			}

			collection.remove(new BasicDBObject());

			// 2. BasicDBObjectBuilder example
			System.out.println("BasicDBObjectBuilder example...");
			BasicDBObjectBuilder documentBuilder = BasicDBObjectBuilder.start()
				.add("database", "mkyongDB")
		                .add("table", "hosting");

			BasicDBObjectBuilder documentBuilderDetail = BasicDBObjectBuilder.start()
		                .add("records", "99")
		                .add("index", "vps_index1")
				.add("active", "true");

			documentBuilder.add("detail", documentBuilderDetail.get());

			collection.insert(documentBuilder.get());

			DBCursor cursorDocBuilder = collection.find();
			while (cursorDocBuilder.hasNext()) {
				System.out.println(cursorDocBuilder.next());
			}

			collection.remove(new BasicDBObject());

			// 3. Map example
			System.out.println("Map example...");
			Map<String, Object> documentMap = new HashMap<String, Object>();
			documentMap.put("database", "mkyongDB");
			documentMap.put("table", "hosting");

			Map<String, Object> documentMapDetail = new HashMap<String, Object>();
			documentMapDetail.put("records", "99");
			documentMapDetail.put("index", "vps_index1");
			documentMapDetail.put("active", "true");

			documentMap.put("detail", documentMapDetail);

			collection.insert(new BasicDBObject(documentMap));

			DBCursor cursorDocMap = collection.find();
			while (cursorDocMap.hasNext()) {
				System.out.println(cursorDocMap.next());
			}

			collection.remove(new BasicDBObject());

			// 4. JSON parse example
			System.out.println("JSON parse example...");

			String json = "{'database' : 'mkyongDB','table' : 'hosting'," +
			  "'detail' : {'records' : 99, 'index' : 'vps_index1', 'active' : 'true'}}}";

			DBObject dbObject = (DBObject)JSON.parse(json);

			collection.insert(dbObject);

			DBCursor cursorDocJSON = collection.find();
			while (cursorDocJSON.hasNext()) {
				System.out.println(cursorDocJSON.next());
			}

			collection.remove(new BasicDBObject());

		    } catch (UnknownHostException e) {
			e.printStackTrace();
		    } catch (MongoException e) {
			e.printStackTrace();
		    }

		  }
	
	public boolean insert(DBCollection collection, String jsonString )
	{
	
		try
		{
				DBObject dbObject = (DBObject)JSON.parse(jsonString);
				collection.insert(dbObject);
		} catch (Exception e ) 
		{
			e.printStackTrace();
			return false; 
		}
		return true; 
		
	}
	
	public boolean  insert(String jsonString )
	{	
		try
		{
			    //system.out.println("Value : " +  jsonString );
				dbObject = (DBObject)JSON.parse(jsonString);			
				getDB().getCollection(this.collectionName).insert(dbObject);
				
				
		} catch (Exception e ) 
		{
			e.printStackTrace();
			return false; 
		}
		finally
		{
			dbObject = null;
		}
		return true; 
		
	}
	
	public boolean remove(String jsonString )
	{	
		try
		{
				dbObject = (DBObject)JSON.parse(jsonString);			
				getDB().getCollection(this.collectionName).remove(dbObject);
				
				
		} catch (Exception e ) 
		{
			e.printStackTrace();
			return false; 
		}
		finally
		{
			dbObject = null;
		}
		return true; 
		
	}
	

	public String getServerName() {
		return serverName;
	}


	public void setServerName(String serverName) {
		this.serverName = serverName;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}


	public DBCollection getCollection() {
		return getDB().getCollection(this.collectionName);

	}

	public void setCollection(DBCollection collection) {
		this.collection = collection;
	}
	
	public DBCursor getAllRecords()
	{
		return  this.getCollection().find();
	}
	

	
	public String getResultByColumn(String id, String value )
	{
		DBCursor cursorDocJSON = null; 
		try
		{
			cursorDocJSON = this.getCollection().find(new BasicDBObject(id, value));
			System.out.println("entry " );
			return  cursorDocJSON.next().toString();

		} catch (Exception e )
		{
			cursorDocJSON = null; 
			return null; 
		}
	}
	
	
	public String getResultAsJson(String jsonQuery)
	{
		try
		{
			System.out.println("INSIDE GET RESULT JSON"+jsonQuery);
			DBObject dbObj = (DBObject) JSON.parse(jsonQuery);
			System.out.println("AFTER  JSON"+jsonQuery);
			//System.out.println("Value : " + this.getCollection().findOne(dbObj));
			return this.getCollection().findOne(dbObj).toString();

		} catch (Exception e )
		{
			System.out.println("Error : " + e );
			return null; 
		}
	}
	
	public List<String> getResultByColumn(String id, String value, String columnName  )
	{
		DBCursor cursorDocJSON = null; 
		@SuppressWarnings("rawtypes")
		List<String>  lis = new ArrayList() ;  
		
		try
		{
			DBObject db = new BasicDBObject(id, value);
			System.out.println(" object in json : " + db.toString());
			//cursorDocJSON = this.getCollection().fin
			cursorDocJSON = this.getCollection().find(new BasicDBObject(id, value));
			while ( cursorDocJSON.hasNext())
			{
				System.out.println("entry " );
				lis.add(cursorDocJSON.next().get(columnName).toString() );
			}

		} catch (Exception e )
		{
			cursorDocJSON = null; 
			lis = null; 
			return null; 
		}
		return lis;
	}
	
	
	public DBCursor getAllRecords(String whereCondition)
	{
		// to be done 
		return  this.getCollection().find();
	}
	
	public void printAllRecords()
	{
		DBCursor cursorDocJSON = getAllRecords(); 
		while (cursorDocJSON.hasNext()) {
			System.out.println(cursorDocJSON.next());
		}
		cursorDocJSON = null;
	}
		
}
