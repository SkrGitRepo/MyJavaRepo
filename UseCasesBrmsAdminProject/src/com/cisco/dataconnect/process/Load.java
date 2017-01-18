package com.cisco.dataconnect.process;

import com.cisco.dataconnect.utils.BulkProcess;
import com.cisco.dataconnect.utils.MongoDbInsert;

public  class Load {

	public Load() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Load l = new Load();
		//l.bulk();
		System.out.println("value : " + l.search( "  {  $or: [  {'_id' : 'RS-10525'} ]	}  " )) ;

	}
	
	public String search(String qryJson)
	{
		MongoDbInsert mongoDbInsert = null; 
		try {
			mongoDbInsert = new MongoDbInsert(); 
			mongoDbInsert.setServerName("ibpmlab-01");
			mongoDbInsert.setPort(27017);
			mongoDbInsert.openConnection();
			mongoDbInsert.setDBname("MyDb");
			mongoDbInsert.setCollectioName("MyCollection" );
			//System.out.println("value : " + mongoDbInsert.getResultByColumn("_id","RS-10525" ));
			//System.out.println("value : " + mongoDbInsert.getResultByColumn("_id","RS-10525","pyStatusWork"  ));
			return mongoDbInsert.getResultAsJson( qryJson ); 
			
		} 
		catch ( Exception e ) 
		{
			return null; 
		}
		finally
		{
			//mongoDbInsert.closeConnection();
			//mongoDbInsert = null; 
		}

		
		
		  // {  $or: [   { '_id' : 'RS-4288' } ]	}        
			        
	}
	
	
	
	public void bulk()
	{
		// for mongo	

		MongoDbInsert mongoDbInsert = new MongoDbInsert(); 
		mongoDbInsert.setServerName("ibpmlab-01");
		mongoDbInsert.setPort(27017); 
		mongoDbInsert.openConnection();
		mongoDbInsert.setDBname("MyDb");
		mongoDbInsert.setCollectioName("MyCollection" );

		// call process
		new BulkProcess().processDir("c://mongotest", "json", "MONGO",  mongoDbInsert );
		
		// close all and clean
		//mongoDbInsert.printAllRecords();
		mongoDbInsert.closeConnection();
		mongoDbInsert = null; 
		
	}
	
	

}
