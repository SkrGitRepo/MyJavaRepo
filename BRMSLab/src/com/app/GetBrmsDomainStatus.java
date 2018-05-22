package com.app;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;


import com.cisco.dataconnect.process.Load;

import sun.java2d.pipe.OutlineTextRenderer;

/**
 * Servlet implementation class GetBrmsDomainStatus
 */
@WebServlet("/GetBrmsDomainStatus")
public class GetBrmsDomainStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetBrmsDomainStatus() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		PrintWriter out = response.getWriter();
		String reqURI = request.getRequestURI();
		String[] uriParams = reqURI.split("\\/");
		
		System.out.println("REQ URI:"+reqURI);
		System.out.println("URI LENGHT:"+uriParams.length);
		//String contextPath = request.getContextPath();
		//String domainFromContextPath = contextPath.split("\\/")[1];
		String jsonString = IOUtils.toString(request.getInputStream());
		System.out.println("JSON STRING:"+jsonString);
		String message = null;
	    
		Load load = new Load();
		String searchType = null;
		String searchDomain = null;
		if (uriParams.length == 7) {
			searchType = uriParams[6];
			searchDomain = uriParams[5];
		} else if (uriParams.length == 6) {
			searchType = null;
			searchDomain = uriParams[5];
		}
			 
		try {
			System.out.println("DOMAIN:"+searchDomain);
			if ( (searchType != null) && (searchType.equalsIgnoreCase("history")) && (searchDomain != null) ) {
				System.out.println(" Call History");
				//message = searchType + "," + domainFromContextPath;
				//message = load.searchHistory(searchDomain, jsonString);
				message = load.search(searchDomain+"_history", jsonString);
			} else if ((searchDomain != null) && (searchType == null)){
				System.out.println(" Call serach");
				try {
					message = load.search(searchDomain, jsonString);
				} catch (Exception e) {
					System.out.println("ERRORRRRR"+e.toString());
				}
				
			} 
		} catch (Throwable t) {
			message = "200 " + t.toString();
		
		}
		
		if (message == null) {
			System.out.println("NULL"+message);
			message = "No matching result.";
		}
		System.out.println("MESAAAGE VALUE :"+message);
		response.setContentType("text/plain");
		response.setContentLength(message.length());
		out.println(message);
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
