package com.cisco.brmspega.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cisco.dataconnect.utils.SingleInstance;
import com.cisco.ibpm.kafka.Eman;

/**
 * Servlet implementation class KafkaPingServlet
 */
@WebServlet("/KafkaPingServlet")
public class KafkaPingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public KafkaPingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		PrintWriter out = response.getWriter();
		
		String env = request.getParameter("env");
		String pingResponse =  "Failed";
		 
		try {
			 	if(env.equalsIgnoreCase("dev")) {
			 		pingResponse = "Dev ::"+((Eman) SingleInstance.getInstance("DEV", "brms-nprd1-dev6:9092,brms-nprd1-dev7:9092,brms-nprd1-dev8:9092")).ping();
			 		System.out.println("Dev ::" + ((Eman) SingleInstance.getInstance("DEV", "brms-nprd1-dev6:9092,brms-nprd1-dev7:9092,brms-nprd1-dev8:9092")).ping());
			 	} else if (env.equalsIgnoreCase("dev")) {
			 		pingResponse = "Stage ::" + ((Eman) SingleInstance.getInstance("STAGE", "brms-nprd1-stg1:9092,brms-nprd1-stg2:9092,brms-nprd1-stg3:9092")).ping() ;
			 		System.out.println("Stage ::" + ((Eman) SingleInstance.getInstance("STAGE", "brms-nprd1-stg1:9092,brms-nprd1-stg2:9092,brms-nprd1-stg3:9092")).ping());
			 	} else if (env.equalsIgnoreCase("prod")) {
			 		pingResponse = "PROD ::" +((Eman) SingleInstance.getInstance("PROD", "brms-prd1-23:9092,brms-prd1-23:9092,brms-prd1-25:9092,brms-prd4-23:9092,brms-prd4-23:9092,brms-prd4-25:9092")).ping();
			 		System.out.println("PROD ::" + ((Eman) SingleInstance.getInstance("PROD", "brms-prd1-23:9092,brms-prd1-23:9092,brms-prd1-25:9092,brms-prd4-23:9092,brms-prd4-23:9092,brms-prd4-25:9092")).ping());
			 	}
						
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		 out.println(pingResponse);
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
