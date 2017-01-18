package com.cisco.brmspega.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AdminStatus
 */
@WebServlet(name = "BRMSAdminStatus", urlPatterns = { "/BRMSAdminStatus" })
public class BRMSAdminStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BRMSAdminStatus() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		//long responseTime = System.currentTimeMillis();
		PrintWriter out = response.getWriter();
		String brmsAdminStatus = "DOWN";
		String hostname;
		try {
			hostname = InetAddress.getLocalHost().getHostName();
			System.out.println("****** PING HOSTNAME :::"+hostname);
			
			String ipAddress = hostname;
		    InetAddress inet = InetAddress.getByName(ipAddress);
		    System.out.println("Sending Ping request to BRMSAdmin host :: " + ipAddress);
		    brmsAdminStatus =  inet.isReachable(5000) ? "EMAN OK" : "UNABLE TO CONNECT";
		    System.out.println("BRMS Admin servlet response ::"+brmsAdminStatus);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("BRMS Admin host UNKNOW or NOT REACHABLE.");
			e.printStackTrace();
		}
		
		out.print(brmsAdminStatus);
		out.flush();
		out.close();
				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
