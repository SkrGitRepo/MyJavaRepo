package com.cisco.brmspega.bundles;

import java.util.Properties;

public class BrmsPropertyLoader {
	 private static Properties prop = new Properties();
	  private static BrmsPropertyLoader bemsPropertyLoader = new BrmsPropertyLoader();
	  
	  static
	  {
	    try
	    {
	      ClassLoader loader = Thread.currentThread().getContextClassLoader();
	      prop.load(loader.getResourceAsStream("com/cisco/brmspega/bundles/brmsAdminPega.properties"));
	    }
	    catch (Exception e)
	    {
	      System.err.println("Property file 'brmsAdminPega.properties' missing ......");
	    }
	  }
	  
	  public static BrmsPropertyLoader getInstance()
	  {
	    return bemsPropertyLoader;
	  }
	  
	  public String getProperty(String key)
	  {
	    return prop.getProperty(key);
	  }
	}