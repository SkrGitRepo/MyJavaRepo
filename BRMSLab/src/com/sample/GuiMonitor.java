package com.sample;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;

import javax.management.ObjectInstance;
import javax.management.ObjectName;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;



public class GuiMonitor {
	
	 
	 
	public static void main(String[] args) {
		 String td="<td width=\"10%\">";
		 @SuppressWarnings("deprecation")	
		 String htmlTableStart="<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"1\" bgcolor=\"#cee7ff\" class=\"/pageText\">";
		 String htmlTableEnd="</table>";

		try {
			
			String serverName = "brms-nprd2-stg3";
			String serverJmxPort  = "8300";
			String serverDomain = "tac";
			
			JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://"+serverName+":"+serverJmxPort+"/jmxrmi");
			JMXConnector jmxc = JMXConnectorFactory.connect(url, null);

			MBeanServerConnection server = jmxc.getMBeanServerConnection();

			Set<ObjectInstance> instances = server.queryMBeans(null, null);
			Iterator<ObjectInstance> iterator = instances.iterator();

			while (iterator.hasNext()) {
				
				

				ObjectInstance instance = iterator.next();

				System.out.println("OBject Instance:"+instance.getClassName());
				if (instance.getClassName().equalsIgnoreCase("org.apache.tomcat.util.modeler.BaseModelMBean")) {

					String objName = instance.getObjectName().toString();
					if (objName.contains("type=DataSource")) {
						ObjectName on = new ObjectName(objName);
						/*String[] attrNames = { "url", "numIdle" };
						AttributeList list = conn.getAttributes(on, attrNames);
						if ( (list.size() == attrNames.length)) {
							System.out.println("LSIT Size"+list.size());
							System.out.println("All attributes were retrieved successfully");
							System.out.println("ATTRIBUTE FROM LIST " + list.get(1).toString());
						} else {
							List<String> missing = new ArrayList<String>(Arrays.asList(attrNames));
							for (Attribute a : list.asList()) {
								missing.remove(a.getName());
								System.out.println("Did not retrieve: " + missing);
							}

						}*/

						String dbURL = (String) server.getAttribute(on, "url");
						String [] dbObjects =  objName.toString().split(",");
						
						String dataSourceName = dbObjects[4].split("/")[1].replace("\"", "");
						String contextPath =  "context="+serverDomain;
						String inContextPath = dbObjects[2].replace("/",""); 
					
						

						if (dbURL != null && contextPath.equalsIgnoreCase(inContextPath)) {
							System.out.println("****************************************");
							System.out.println("DATABASE INSTANCE FOUND :" + objName);

							//System.out.println("INNN ObjectName : " + on.toString());
							System.out.println("DSName: " +dataSourceName );
							System.out.println("DB URL:" + server.getAttribute(on, "url"));
							System.out.println("Schema Name: " + server.getAttribute(on, "username"));
							System.out.println("Active Connections:" + server.getAttribute(on, "numActive"));
							System.out.println("Total Count:" + server.getAttribute(on, "maxTotal"));
							System.out.println("Connection Pool Capacity:" + server.getAttribute(on, "initialSize"));
							System.out.println("State:" + "Running");
							System.out.println("Highest Num Available:" + server.getAttribute(on, "maxIdle"));
							//System.out.println("Leacked Connection:" + conn.getAttribute(on, "maxTotal"));
							System.out.println("Avg. Active Connection:" + server.getAttribute(on, "numIdle"));
							System.out.println("Connection Delay Time:" + server.getAttribute(on, "maxWaitMillis"));
							System.out.println("Date & time:" + server.getAttribute(on, "username"));

							System.out.println("****************************************");
							
							
							String dbURLL = (String) server.getAttribute(on, "url");
							String schemaName = (String) server.getAttribute(on, "username");
							int activeCon =  (int) server.getAttribute(on, "numActive");
							int totalCon = (int) server.getAttribute(on, "maxTotal");
							String conPoolCapacity =  server.getAttribute(on, "initialSize").toString();
							String highNumAvail = server.getAttribute(on, "maxIdle").toString();
							String activeConnectionsAverageCount = server.getAttribute(on, "numActive").toString();
							String conDelayTime = server.getAttribute(on, "maxWaitMillis").toString();
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
							String usagesDate  = dateFormat.format(new Date());
							
							String message = htmlTableStart+"<tr>"+td+dataSourceName+"</td>"+td+schemaName+"</td>"
									+td+activeCon+"</td>"+"<td width=\"5%\">"+totalCon+"</td>"
									+td+conPoolCapacity+"</td>"+td+"Running"+"</td>"
									+td+highNumAvail+"</td>"
									+td+activeConnectionsAverageCount+"</td>"+td+conDelayTime+"</td>"
									+td+usagesDate+"</td>"+"</tr>"+htmlTableEnd;
									
									//System.out.println("****** getTcJmxJdbcUrlInfo()************ JDBC Datasource Info: "+message);
							
						}

					}
				}

			}

			/*
			 * ObjectName on = new ObjectName(
			 * "Catalina:type=DataSource,host=brms-nprd2-dev1,context=/csm,class=javax.sql.DataSource,name=\"jdbc/PegaRULES\""
			 * );
			 * 
			 * System.out.println("ObjectName : " + on.toString());
			 * System.out.println("DB NumIdle" + conn.getAttribute(on,
			 * "numIdle")); System.out.println("DB URL" + conn.getAttribute(on,
			 * "url"));
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}