package com.cisco.dataconnect.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Iterator;
import com.cisco.dataconnect.utils.MongoDbInsert;

import org.apache.commons.io.FileUtils;


public class BulkProcess {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public String getFile(File file) 
	{
		System.out.println( "Process file : " + file );
		BufferedReader reader = null; 
		try 
		{
			reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream( file ))));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println(" Buffer reading failed for file : " + file );
			return null; 
		}
	    String line = null;
	    String json = "";
	    try {
			while ((line = reader.readLine()) != null) {
			    // do something with your read line
				json = json + line;
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(" File content failed ");
		}
	    finally
	    {
	    	line = null;
	    	if ( reader != null )
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	
	    }
	    //System.out.println("  : " + json );
	    return json ;
		
	}
	
	public void processDir( String dirPath, String fileTypes, String toolUsed, Object objectHandle )
	{
		@SuppressWarnings("unchecked")
		Collection<File> fileList = FileUtils.listFiles(new File(dirPath), new String[] { fileTypes  }, false);
		// for ( int i=0 ; i < fileList.size() ; i++)
		{
			if ( toolUsed.equals("MONGO"))
			{
				// Sent to mongo for processing 
				MongoDbInsert m = (MongoDbInsert) objectHandle ;		
				Iterator<File> fileListIterator = fileList.iterator();	
				File file = null; 
				while (fileListIterator.hasNext()) 
				{
					file = fileListIterator.next();
	     		    m.insert(getFile(file));
	     		    
	     		    //file = null; 
				}
				file = null;
			} else if ( toolUsed.equals("ELASTIC"))
			{
				//index Elastic 
			}
		}
	}
}


