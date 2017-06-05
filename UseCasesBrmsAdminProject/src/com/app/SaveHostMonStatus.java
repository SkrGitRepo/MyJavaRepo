package com.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SaveHostStatus
 */
@WebServlet("/SaveHostMonStatus")
public class SaveHostMonStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		String Hosts[] = request.getParameterValues("monstatus");
		String contextPath = request.getContextPath();
		StringBuffer reqUrl = request.getRequestURL();
		
		
		
		response.setContentType("text/html");
		out.println("<html><head></head>");
		out.println("<body><center>");
		
		
        if(Hosts != null) {
        	out.println("<H3>"+reqUrl+"</H3>");
        	out.println("<H3> Below host have been disbled :</h3>");
        	out.println("<ul>");
        	
        	
        	for(int i=0; i<Hosts.length; i++) {
        		
        		String array[] = Hosts[i].split(":", -1); 
        		String hostRecordFile = "C:/Users/sumkuma2/git/MyJavaRepo/UseCasesBrmsAdminProject/"+array[0]+"_disabled_host_file.txt";
        		String hostName = array[1];
        		String hostPort = array[2];
        		String nodeName = hostName+":"+hostPort;
        		
        		boolean isHostDisbled = disableHostMonitoring(hostRecordFile, nodeName);
        		if (isHostDisbled) {
        			out.println("<li>"+Hosts[i]+"</li>");
        		}
        	}
        	out.println("</ul>");
        }
        out.println("<p><a href='"+contextPath+"'><font  color='#CC0066'><H3>Back to Home</H3></a></p>");
		out.println("</center></body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	} */
	
	public boolean disableHostMonitoring(String disabledHostRecordFile, String hostToDisabled) {
		
			boolean isHostDisbled = false;
			String newLine = "\n";
	
		    String fileName = disabledHostRecordFile;
		    PrintWriter printWriter = null;
		    File file = new File(fileName);
		    try {
		        if (!file.exists()) file.createNewFile();
		        printWriter = new PrintWriter(new FileOutputStream(fileName, true));
		        printWriter.write(hostToDisabled + newLine );
		        isHostDisbled = true;
		    } catch (IOException ioex) {
		        ioex.printStackTrace();
		        isHostDisbled = false;
		    } finally {
		        if (printWriter != null) {
		            printWriter.flush();
		            printWriter.close();
		        }
		    }
		return isHostDisbled;
	}
	
}
