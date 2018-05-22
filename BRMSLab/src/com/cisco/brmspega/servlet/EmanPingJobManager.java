package com.cisco.brmspega.servlet;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.cisco.brmspega.bundles.BrmsPropertyLoader;

@WebListener
public class EmanPingJobManager implements ServletContextListener {

	String emanPingInterval = "20";//PropertyLoader.getInstance().getProperty("eman_job_ping_enterval"); 
	long pingInterval = Long.parseLong(emanPingInterval);
	
	private ScheduledExecutorService schedulerService;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		// TODO Auto-generated method stub
		System.out.println("STARTING EMAN PING");
		//schedulerService = Executors.newSingleThreadScheduledExecutor();
		//schedulerService.scheduleAtFixedRate(EmanPingJob.getInstance(),0,pingInterval, TimeUnit.MINUTES);
		String hostname;
		try {
			hostname = InetAddress.getLocalHost().getHostName();
			//System.out.println("****** HOSTNAME :::"+hostname);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
		//schedulerService.shutdown();
	}
}
