package com.sample;
import java.io.PrintWriter;
import java.util.List;

import com.sample.JmxJdbcInfo;

public class DataSourceProperties_JMX
{
	

public static void main(String [] args)
	{
	String host = "brms-nprd2-dev6";
	String port = "7030";
	String userName = "weblogic"; 
	String password = "cisco123";
		try {
			JmxJdbcInfo jmxInfo = new JmxJdbcInfo(host, port, userName, password);
			List jmxJdbcInfoList = jmxInfo.getDataSourceInfo("JDBCConnectionPoolRuntime"); 
			
		    if(null != jmxJdbcInfoList) {
		    	//compareHostName(hostName,port,jmxJdbcInfoList,userStatus);
		    	if(jmxJdbcInfoList != null && jmxJdbcInfoList.size()>0) {
		    		for(int i=0;i<jmxJdbcInfoList.size();i++) {
		    			JmxJdbcDomain JdbcDomain = (JmxJdbcDomain)jmxJdbcInfoList.get(i);
		    			if(null != JdbcDomain) {
		    				//jmxJdbcList.add((JmxJdbcDomain)jmxJdbcInfoList.get(i));
		    			}
		    		}
		    	}
		    	String jmxHeapJvmString = getJmxJdbc(jmxJdbcInfoList);
		    	System.out.println(jmxHeapJvmString);
		    } else {
		    	System.out.println("No result");
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

private static String getJmxJdbc(List jmxJdbcInfoList) {
	 String jmxjdbcString = null;
	 //if(jmxJdbcList != null) {
	 if(jmxJdbcInfoList != null) {
		 //for(int i=0;i<jmxJdbcList.size();i++) {
		 for(int i=0;i<jmxJdbcInfoList.size();i++) {	 
			 //JmxJdbcDomain jmxJdbcDomain = (JmxJdbcDomain)jmxJdbcList.get(i);
			 JmxJdbcDomain jmxJdbcDomain = (JmxJdbcDomain)jmxJdbcInfoList.get(i);
			 if(jmxjdbcString !=null) {
				 jmxjdbcString = jmxjdbcString.trim()+jmxJdbcDomain.getMessage().trim();
			 } else {
				 jmxjdbcString = jmxJdbcDomain.getMessage().trim();
			 }
		 }
	 }
	 if(jmxjdbcString == null)
		 jmxjdbcString = "";
	 return jmxjdbcString;
}

}
