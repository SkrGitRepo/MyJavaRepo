package com.cisco.brmspega.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.management.ObjectName;
import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sample.JmxJdbcDomain;
import com.sample.JmxJdbcInfo;

import weblogic.jndi.Environment;
import weblogic.management.MBeanHome;
import weblogic.management.RemoteMBeanServer;
import weblogic.management.WebLogicMBean;

/**
 * Servlet implementation class GetWLDSMbeanDetailsServlet
 */
@WebServlet("/GetWLDSMbeanDetailsServlet")
public class GetWLDSMbeanDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetWLDSMbeanDetailsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		
		String host = "brms-nprd2-dev6";
		String port = "7010";
		String userName = "weblogic"; 
		String password = "cisco123";
		
		PrintWriter out=response.getWriter();
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
			    	out.println(jmxHeapJvmString);
			    } else {
			    	System.out.println("No result");
			    	out.println("No resoult");
			    }
			} catch (Exception e) {
				e.printStackTrace();
				out.close();
			}
			out.close();
		   /*MBeanHome home = null;
		   RemoteMBeanServer homeServer = null;
		   //domain variables
		   String url = "t3://localhost:7001";
		   //String username = "weblogic";
		   //String password = "weblogic";
		   //Setting an initial context.
		
		 try {
			    Environment env = new Environment();
			    env.setProviderUrl(url);
			    env.setSecurityPrincipal(userName);
			    env.setSecurityCredentials(password);
			    Context ctx = env.getInitialContext();
			    //Retrieving the Administration MBeanHome interface
			    home = (MBeanHome) ctx.lookup(MBeanHome.ADMIN_JNDI_NAME);
			    System.out.println("Got the Admin MBeanHome: " + home + " from the Admin server");
			    } catch (Exception e) {
			      System.out.println("Exception caught: " + e);
			    //Retrieving the MBeanServer interface
			    homeServer = home.getMBeanServer();
			    //Retrieving a list of MBeans with object names that include 
			    //"JDBCConnectionPool"
			   java.util.Set JDBCMBeans = homeServer.queryNames(new 
			                    ObjectName("mydomain:Type=JDBCConnectionPool,*"), query);
			   //where "query" could be any object that implements the JMX 
			   //javax.managementQueryExp
			   for (Iterator itr = JDBCMBeans.iterator(); itr.hasNext(); ) {
			            WebLogicMBean mbean = (WebLogicMBean)itr.next();
			             System.out.println("Matches to the MBean query:" + mbean);
			    }

		*/
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	
	protected String getJmxJdbc(List jmxJdbcInfoList) {
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
