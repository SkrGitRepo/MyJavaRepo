package com.cisco.brmspega.servlet;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppContextListener implements ServletContextListener {
	private ScheduledExecutorService scheduleService;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

		// Your code here
		System.out.println("HelloWorld Listener has been shutdown");

	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {

		// Your code here
		System.out.println("HelloWorld Listener initialized.");

		TimerTask vodTimer = new VodTimerTask();

		Timer timer = new Timer();
		timer.schedule(vodTimer, 1000, (2 * 1000));

	}

	class VodTimerTask extends TimerTask {

		@Override
		public void run() {
			System.out.println("TimerTask " + new Date().toString());
		}
	}

}
