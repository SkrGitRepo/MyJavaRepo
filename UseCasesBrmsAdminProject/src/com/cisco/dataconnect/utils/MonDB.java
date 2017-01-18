package com.cisco.dataconnect.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.util.JSON;
import org.apache.commons.io.FileUtils;

//import com.sun.xml.internal.txw2.Document;

@SuppressWarnings({ "deprecation", "unused" })
public final class MonDB {
	
	
	private FileUtils fileUtils = new FileUtils(); 
	
	private MongoClient mongoClient = null; 
	private String databaseName     = null; 
	private String collectionName   = null; 
	
	private DB mongoDataBase        = null;	
	private DBCollection mongoCollection = null ;
	
	//mongodb://host:27017/mydb
	//mongodb://:27017/pega

	public MonDB() {
		// TODO Auto-generated constructor stub
	}

	public void close()
	{
		 this.mongoClient.close();
    }
	
	
	
	
	
	public void connect( String hostName, int port, String databaseName )
	{
		 try {
			 System.out.println("start 2 ");
			this. mongoClient = new MongoClient( hostName, port );
			System.out.println("connetd ");

			 this.databaseName = databaseName; 
			 mongoDataBase = mongoClient.getDB(databaseName);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public DBCollection getCollection(String name) 
	{
		mongoCollection = mongoDataBase.getCollection(name);
		return mongoCollection; 
	}
	
	/*
	public DBCursor searchCollection(String name, String value)
	{	
		BasicDBObject searchQuery = new BasicDBObject().append(name, value);
        return mongoCollection.find(searchQuery); 
	}
	*/
	
	
	public DBCursor searchCollection(String object)
	{	
        return mongoCollection.find((DBObject) JSON.parse(object) );
	}
	
	/*
	
	public long updateCollection(String newObject, String name, String value)
	{	
		BasicDBObject searchQuery = new BasicDBObject().append(name, value);
		System.out.println("size " + searchQuery.size() ); 
		DBCursor cursor = mongoCollection.find(searchQuery); 
		DBObject dbObject = (DBObject) JSON.parse(newObject);
		// check if you mutiple outcome 
		
		long i = 0 ; 
		while (cursor.hasNext()) {
			//System.out.println("Values : " + cursor.next());
			 mongoCollection.findAndModify(cursor.next(), dbObject);
			 i++;
		}
        return i; 
		
	}
	*/
	
	public long updateOrInsertCollection(String newObject, String object)
	{	
		DBCursor cursor = mongoCollection.find((DBObject) JSON.parse(object)); 	
		// check if you mutiple outcome 
		long i = 0 ; 
		if ( cursor == null || cursor.count() < 1 )
		{
			i = 1; 
			// then insert 
			this.insertCollection(newObject);
		} else 
		{
			DBObject dbObject = (DBObject) JSON.parse(newObject);
			while (cursor.hasNext()) {
				 mongoCollection.findAndModify(cursor.next(), dbObject);
				 i++;
			}
			dbObject = null; 
		}
        return i; 	
	}
	
	public long updateCollection(String newObject, String object)
	{	
		DBCursor cursor = mongoCollection.find((DBObject) JSON.parse(object)); 	
		// check if you mutiple outcome 
		long i = 0 ; 

		DBObject dbObject = (DBObject) JSON.parse(newObject);
		while (cursor.hasNext()) {
			 mongoCollection.findAndModify(cursor.next(), dbObject);
			 i++;
		}
		dbObject = null; 
        return i; 	
	}
	
	public boolean  insertCollection(String jsonObject)
	{
		DBObject dbObject = (DBObject) JSON.parse(jsonObject);
		mongoCollection.insert(dbObject);
		dbObject = null; 
		return true;
	}
	
	
	public void printAllDocuments() {
		DBCursor cursor = this.mongoCollection.find();
		while (cursor.hasNext()) {
			System.out.println("Values : " + cursor.next());
		}
	}
	

	
	public long recordCount()
	{
		return mongoCollection.count();
	}
	
	public boolean insertFile(String fileName )
	{	
		try 
		{
			 this.insertCollection(fileUtils.readFileToString( new File(fileName)) );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false; 
		}
		return true; 
	}
	
	/*
	public boolean updateFile(String fileName, String name, String value  )
	{
		try 
		{
			 this.updateCollection(fileUtils.readFileToString( new File(fileName)), name, value );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false; 
		}
		return true; 
	}
	*/
	
	public boolean updateOrInsertFile(String fileName )
	{
		
		return false ;
	}
	
	
	public File[] getFileList(String dir)
	{
		File[] files = null;
		FileFilter fileFilter = new FileFilter() {
			public boolean accept(File file) {
				return (true);
			}
		};
		files = new File(dir).listFiles(fileFilter);
		return files;
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)   {
		// TODO Auto-generated method stub
		
		try
		{
			System.out.println("new4");
			MonDB testDb = new MonDB(); 
			testDb.connect("ibpmlab-01" , 27017,"myTest");
			System.out.println("connected 4" );
			//System.out.println("Count :" + testDb.recordCount() );
			testDb.getCollection("myTestCollection");
			System.out.println("Count :" + testDb.recordCount() );
			//testDb.insertCollection("{ name : 'test'  }");
			System.out.println( " find one value : " + testDb.searchCollection(  "{ '_id' : { '$oid' : '5620459a1027ba94126a603f'} , 'name' : 'kkkkkk'}"  ));
			testDb.updateCollection("{ name : 'kkkkkk'  }",  "{ '_id' : { '$oid' : '562142e410279ecb57c6cd0a'} , 'name' : 'sdsdsdd'}");
			//testDb.updateOrInsertCollection("{ name : 'kkkkkk'  }",  "{ '_id' : { '$oid' : '562148d61027a8586e4273ea'} , 'name' : 'sdsdsdd'}");

			System.out.println("Count :" + testDb.recordCount() );
			testDb.printAllDocuments();
			//save one file file 
		    testDb.insertFile("c:/temp/test.json");
			File[] files = testDb.getFileList("c:/json");
			for (File file : files) {
				System.out.println("File : " + file.getPath() );
				testDb.insertFile(file.getPath() );
			}
		    testDb.close(); 	
	        //MongoDatabase database = ((Object) mongoClient).getDatabase("pega");
	        //database.listCollections()
	        //MongoCollection<Document> collection =  database.getCollection("test");
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
          
	}

}
