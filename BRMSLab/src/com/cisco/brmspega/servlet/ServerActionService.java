package com.cisco.brmspega.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.sample.utility.ManageServerActionRequest;

/**
 * Servlet implementation class ServerActionService
 */
@WebServlet("/ServerActionService")
public class ServerActionService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServerActionService() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");

		String serverActionRequest = IOUtils.toString(request.getInputStream());
		ManageServerActionRequest sa = new ManageServerActionRequest();
		
        System.out.println("Incoming serverAction request: "+serverActionRequest);
        String message = null;
		if (serverActionRequest != null) {
			message = sa.manageServerActionRequest(serverActionRequest);
		}
		
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
