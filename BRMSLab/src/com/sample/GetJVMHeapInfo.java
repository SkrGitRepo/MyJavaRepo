package com.sample;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.Iterator;
import java.util.List;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class GetJVMHeapInfo {
	private static final String serviceURL = "service:jmx:rmi:///jndi/rmi://";
	private static final String jmxrmi = "/jmxrmi";
	private static JMXConnector connector;
	private static MBeanServerConnection connection;

	public static void main(String[] args) throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		String hostname = "brms-nprd2-dev15";
		String jmxPort = "8303";

		// create jmx connection with mules jmx agent
		
		//HashMap map = new HashMap();
		//String[] credentials = new String[2];
		//credentials[0] = "tomcat";
		//credentials[1] = "tomcat";
		//map.put("jmx.remote.credentials", credentials);
		//JMXConnector c = JMXConnectorFactory.newJMXConnector(GetJVMHeapInfo(hostname, port), null);
		
		ObjectName runtimeServiceMBean = new ObjectName("com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
		
		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://"+hostname+":"+jmxPort+"/jmxrmi");
		JMXConnector c = JMXConnectorFactory.newJMXConnector(url, null);
		c.connect();
		/*Object o = c.getMBeanServerConnection().getAttribute(new ObjectName("java.lang:type=Memory"), "NonHeapMemoryUsage");
		CompositeData cd = (CompositeData) o;
		
		
		Long commited = (Long) cd.get("committed");
		Long init = (Long) cd.get("init");
		Long max = (Long) cd.get("max");
		Long used = (Long) cd.get("used");
		
		
		System.out.println(commited);
		System.out.println(init);
		System.out.println(max);
		System.out.println(used);*/
		System.out.println( hostname + "on JMX Port:"+jmxPort+" JVM Heap infos:");
		GetJVMHeapInfo(c);
		System.out.println( hostname + "on JMX Port:"+jmxPort+" JVM Non-Heap infos:");
		GetJVMNonHeapInfo(c);
		System.out.println( hostname + "on JMX Port:"+jmxPort+" Thread infos:");
		GetJVMThreadInfo(c);
		

		
		/* to get local JVM Memory details
			// Retrieve memory managed bean from management factory.
			MemoryMXBean memBean = ManagementFactory.getMemoryMXBean() ;
			MemoryUsage heap = memBean.getHeapMemoryUsage();
			MemoryUsage nonHeap = memBean.getNonHeapMemoryUsage();
	
			// Retrieve the four values stored within MemoryUsage:
			// init: Amount of memory in bytes that the JVM initially requests from the OS.
			// used: Amount of memory used.
			// committed: Amount of memory that is committed for the JVM to use.
			// max: Maximum amount of memory that can be used for memory management.
			System.err.println(String.format("Heap: Init: %d, Used: %d, Committed: %d, Max.: %d",
			  heap.getInit(), heap.getUsed(), heap.getCommitted(), heap.getMax()));
			System.err.println(String.format("Non-Heap: Init: %d, Used: %d, Committed: %d, Max.: %d",
			  nonHeap.getInit(), nonHeap.getUsed(), nonHeap.getCommitted(), nonHeap.getMax()));
		*/
		
		
		
	}

	private static void GetJVMHeapInfo(JMXConnector c) {
			
		
		try {
			Object o = c.getMBeanServerConnection().getAttribute(new ObjectName("java.lang:type=Memory"), "HeapMemoryUsage");
			
			CompositeData cd = (CompositeData) o;
			
			Long commited = (Long) cd.get("committed");
			String cmtd  = 	commited / (1024 * 1024) + " MB";
			Long init = (Long) cd.get("init");
			String initial  = 	init / (1024 * 1024) + " MB";
			Long max = (Long) cd.get("max");
			String maxMem  = 	max / (1024 * 1024) + " MB";
			Long used = (Long) cd.get("used");
			String usedMem  = 	used / (1024 * 1024) + " MB";
			
			
			System.out.println(cmtd);
			System.out.println(initial);
			System.out.println(maxMem);
			System.out.println(usedMem);
			
			
		} catch (AttributeNotFoundException | InstanceNotFoundException | MalformedObjectNameException | MBeanException
				| ReflectionException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	
private static void GetJVMNonHeapInfo(JMXConnector c) {
			
		
		try {
			Object o = c.getMBeanServerConnection().getAttribute(new ObjectName("java.lang:type=Memory"), "NonHeapMemoryUsage");
			
			CompositeData cd = (CompositeData) o;
			
			Long commited = (Long) cd.get("committed");
			String cmtd  = 	commited / (1024 * 1024) + " MB";
			Long init = (Long) cd.get("init");
			String initial  = 	init / (1024 * 1024) + " MB";
			Long max = (Long) cd.get("max");
			String maxMem  = 	max / (1024 * 1024) + " MB";
			Long used = (Long) cd.get("used");
			String usedMem  = 	used / (1024 * 1024) + " MB";
			
			
			System.out.println(cmtd);
			System.out.println(initial);
			System.out.println(maxMem);
			System.out.println(usedMem);
			
			
		} catch (AttributeNotFoundException | InstanceNotFoundException | MalformedObjectNameException | MBeanException
				| ReflectionException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}

private static void GetJVMThreadInfo(JMXConnector c) {
	
	
	try {
		Object thC = c.getMBeanServerConnection().getAttribute(new ObjectName("java.lang:type=Threading"), "ThreadCount");
		int threadCount = (int) thC;
		
		
		Object dthC = c.getMBeanServerConnection().getAttribute(new ObjectName("java.lang:type=Threading"), "PeakThreadCount");
		int deadThreadCount = (int) dthC;
		
		/*String cmtd  = 	commited / (1024 * 1024) + " MB";
		Long init = (Long) cd.get("init");
		String initial  = 	init / (1024 * 1024) + " MB";
		Long max = (Long) cd.get("max");
		String maxMem  = 	max / (1024 * 1024) + " MB";
		Long used = (Long) cd.get("used");
		String usedMem  = 	used / (1024 * 1024) + " MB";
		*/
		
		System.out.println(threadCount);
		System.out.println(deadThreadCount);
		/*System.out.println(initial);
		System.out.println(maxMem);
		System.out.println(usedMem);*/
		
		
	} catch (AttributeNotFoundException | InstanceNotFoundException | MalformedObjectNameException | MBeanException
			| ReflectionException | IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	

}



}
