package com.app;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;


/**
 * Servlet implementation class MonitoringServlet
 */
@WebServlet("/HostServlet")
public class HostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HostServlet() {
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
		String hostRecordFile = "C:/EAJavauseCasePrj/HostManagement/"+lifecycleName+"_host_file.txt";
		
		if (lifecycleName.equals("prod")) {
			//list.add("eon-rch1-1-l");
			//list.add("eon-rtp5-1-l");
			
			list = readHostFromFile(hostRecordFile);
		} else if (lifecycleName.equals("dev")) {
			//list.add("eon-rtp3-1-l");
			//list.add("eon-rtp3-2-l");
			list = readHostFromFile(hostRecordFile);
		} else if (lifecycleName.equals("stage")) {
			//list.add("eon-nprd4-1-l");
			//list.add("eon-nprd4-2-l");
			list = readHostFromFile(hostRecordFile);
		} else if (lifecycleName.equals("Select Lifecycle")) {
			list.add("Select Node");
		}

		json = new Gson().toJson(list);
		response.setContentType("application/json");
		response.getWriter().write(json);
	}

	
	public List<String> readHostFromFile(String hostFile) {
		
		List<String> list = new ArrayList<String>();
		String hostRecord;

		// Open the file
		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream(hostFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		//Reading each record of Host Line By Line
		try {
			while ((hostRecord = br.readLine()) != null)   {
			  System.out.println (hostRecord);
			  
			  //Adding each host record to drop down list
			  list.add(hostRecord);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Close the input stream
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
		
		
	}

}
