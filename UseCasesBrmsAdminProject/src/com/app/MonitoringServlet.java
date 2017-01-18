package com.app;

/*import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;*/

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

//import java.io.File;
import java.io.IOException;


/**
 * Servlet implementation class MonitoringServlet
 */
@WebServlet("/MonitoringServlet")
public class MonitoringServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MonitoringServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @throws IOException 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		String lifecycleName = request.getParameter("lifecycleName");
		List<String> list = new ArrayList<String>();
		String json = null;
		
		if (lifecycleName.equals("prod")) {
			list.add("eon-rch1-1-l");
			list.add("eon-rtp5-1-l");
		} else if (lifecycleName.equals("dev")) {
			list.add("eon-rtp3-1-l");
			list.add("eon-rtp3-2-l");
		} else if (lifecycleName.equals("stage")) {
			list.add("eon-nprd4-1-l");
			list.add("eon-nprd4-2-l");
		} else if (lifecycleName.equals("Select Lifecycle")) {
			list.add("Select Node");
		}

		json = new Gson().toJson(list);
		response.setContentType("application/json");
		response.getWriter().write(json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	

}
