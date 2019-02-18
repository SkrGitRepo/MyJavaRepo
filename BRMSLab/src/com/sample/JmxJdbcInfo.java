package com.sample;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServer;
import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.sql.DataSource;

import weblogic.management.MBeanHome;
import weblogic.management.WebLogicMBean;
import weblogic.management.WebLogicObjectName;
import weblogic.management.runtime.JDBCConnectionPoolRuntimeMBean;
import weblogic.management.runtime.JDBCDataSourceTaskRuntimeMBean;
import weblogic.management.runtime.WebAppComponentRuntimeMBean;
public class JmxJdbcInfo {
	MBeanServer server = null;
	MBeanHome mbeanHome = null;
	Context myCtx = null;
	Hashtable inStart = new Hashtable();
	String connectionException = "Connection Exception";
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String URL=null;
	private String td="<td width=\"10%\">";
	@SuppressWarnings("deprecation")	private String htmlTableStart="<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"1\" bgcolor=\"#cee7ff\" class=\"/pageText\">";
	private String htmlTableEnd="</table>";


	public JmxJdbcInfo(String host, String port, String userName, String password) throws SQLException, NamingException {
			weblogic.jndi.Environment env = new weblogic.jndi.Environment();
			env.setProviderUrl("t3://"+host.trim()+":"+port.trim());
			env.setSecurityPrincipal(userName.trim()); // username
			env.setSecurityCredentials(password.trim()); // password
			myCtx = env.getInitialContext();
			System.out.println(myCtx);
			//mbeanHome =(MBeanHome)myCtx.lookup("weblogic.management.home.localhome");
			//mbeanHome =(MBeanHome)myCtx.lookup(MBeanHome.ADMIN_JNDI_NAME);
			//mbeanHome =(MBeanHome)myCtx.lookup("weblogic.management.mbeanservers.runtime");
			//mbeanHome =(MBeanHome)myCtx.lookup("weblogic.management.home.localhome");
			mbeanHome =(MBeanHome)myCtx.lookup("weblogic.management.remote");
			
			
			Set allMBeans = mbeanHome.getAllMBeans();
            System.out.println("Size: " + allMBeans.size());
            for (Iterator itr = allMBeans.iterator(); itr.hasNext(); ) {
                WebLogicMBean mbean = (WebLogicMBean)itr.next();
                WebLogicObjectName objectName = mbean.getObjectName();
                System.out.println(objectName.getName() + " is a(n) " +
                                        mbean.getType());
            }
			
			server = mbeanHome.getMBeanServer();
	}

	public String getConnectionException() {
		return connectionException;
	}
	
	public void getMBeanByType(String type) {
		Set set=mbeanHome.getMBeansByType(type);
		Iterator it=set.iterator();
		while(it.hasNext())
		{
			WebAppComponentRuntimeMBean appRun=(WebAppComponentRuntimeMBean)it.next();
		}
	}
	
	
	public List getDataSourceInfo(String type) {
		Map<String, String> map = new HashMap<String, String>();
		List<JmxJdbcDomain> JmxJdbcList = null;
		Set set=mbeanHome.getMBeansByType(type);
		JmxJdbcList = new ArrayList<JmxJdbcDomain>();
		Iterator it=set.iterator();
		NamingEnumeration<NameClassPair> ne;	
		List <String> jndiNames =  new ArrayList<String>();
		try {
			ne = myCtx.list("jdbc");
			while(ne.hasMore()){
				jndiNames.add(ne.next().getName());
			}
		} catch (NamingException e1) {
			System.out.println("Exception while getting the available JNDIs for the Initial context!!!!");
			e1.printStackTrace();
		}
		
		
		while(it.hasNext()) {
			JmxJdbcDomain jmxJdbcDomain = new JmxJdbcDomain();
			JDBCConnectionPoolRuntimeMBean appRun=(JDBCConnectionPoolRuntimeMBean)it.next();
			jmxJdbcDomain.setName(appRun.getName());
			jmxJdbcDomain.setActiveConnectionsCurrentCount(appRun.getActiveConnectionsCurrentCount());
			jmxJdbcDomain.setConnectionsTotalCount(appRun.getConnectionsTotalCount());
			jmxJdbcDomain.setCurrCapacity(appRun.getCurrCapacity());
			jmxJdbcDomain.setState(appRun.getState());
			jmxJdbcDomain.setHighestNumAvailable(appRun.getHighestNumAvailable());
			jmxJdbcDomain.setLeakedConnectionCount(appRun.getLeakedConnectionCount());
			jmxJdbcDomain.setConnectionDelayTime(appRun.getConnectionDelayTime());
			jmxJdbcDomain.setType(appRun.getType());
			
			jmxJdbcDomain.setActiveConnectionsAverageCount(appRun.getActiveConnectionsAverageCount());
			jmxJdbcDomain.setWaitSecondsHighCount(appRun.getWaitSecondsHighCount());
			jmxJdbcDomain.setWaitingForConnectionCurrentCount(appRun.getWaitingForConnectionCurrentCount());
			jmxJdbcDomain.setWaitingForConnectionHighCount(appRun.getWaitingForConnectionHighCount());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			jmxJdbcDomain.setUsagesDate(dateFormat.format(new Date()));
			System.out.println(appRun.getMBeanInfo().getAttributes().toString());
			
			
			try {
				for(String jndiName : jndiNames){
					System.out.println("jndiName : "+jndiName+" appRun.getName() : "+appRun.getName());
					if(jndiName.equalsIgnoreCase(appRun.getName())){
						DataSource dataSource = (javax.sql.DataSource) myCtx.lookup("jdbc/"+jndiName);
						conn = dataSource.getConnection();
						DatabaseMetaData metaData= conn.getMetaData();
						jmxJdbcDomain.setSchemaName(metaData.getUserName());
					    conn.close();
					    break;
					}
				}
				
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jmxJdbcDomain.setMessage(htmlTableStart+"<tr>"+td+jmxJdbcDomain.getName()+"</td>"+td+jmxJdbcDomain.getSchemaName()+"</td>"
					+td+jmxJdbcDomain.getActiveConnectionsCurrentCount()+"</td>"+"<td width=\"5%\">"+jmxJdbcDomain.getConnectionsTotalCount()+"</td>"
					+td+jmxJdbcDomain.getCurrCapacity()+"</td>"+td+jmxJdbcDomain.getState()+"</td>"
					+td+jmxJdbcDomain.getHighestNumAvailable()+"</td>"+"<td width=\"5%\">"+jmxJdbcDomain.getLeakedConnectionCount()+"</td>"
					+td+jmxJdbcDomain.getActiveConnectionsAverageCount()+"</td>"+td+jmxJdbcDomain.getConnectionDelayTime()+"</td>"
					+td+jmxJdbcDomain.getUsagesDate()+"</td>"+"</tr>"+htmlTableEnd);
			JmxJdbcList.add(jmxJdbcDomain);
			
		}
		
		try {
	    	if(myCtx != null) {
	    		myCtx.close();
	    	}
	    } catch (NamingException namingException) {
	    	System.out.println("NamingException :"+namingException);
	    }	
		return JmxJdbcList;
	}
	
	public void getJDBCDataSourceTaskRuntime(String type) {
		Set set=mbeanHome.getMBeansByType(type);
		Iterator it=set.iterator();
		while(it.hasNext()) {
			JDBCDataSourceTaskRuntimeMBean appRun=(JDBCDataSourceTaskRuntimeMBean)it.next();
		}
	
	}
	

}
