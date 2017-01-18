package com.cisco.brmspega.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetJSONFromScriptServlet
 */
@WebServlet("/GetJSONFromScriptServlet")
public class GetJSONFromScriptServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetJSONFromScriptServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		PrintWriter out = response.getWriter();
		String folderLoc = "/opt/brms/shared/scripts/";
		String reqURI = request.getRequestURI();
		System.out.println("URI is :" + reqURI);
		
		String[] uriParams = reqURI.split("\\/");
		String searchDomain = null;
		String searchApp = null;
		String[] command = {"perl","C:\\opt\\brms\\shared\\scripts\\JsonDataTest.pl"};
		String outputFile = "C:/opt/brms/shared/scritps/ResponseJson.txt";
		File output = new File(outputFile);
		if (uriParams.length == 4) {
			searchDomain = uriParams[3];
			out.println("<H3>URI LENGHT IS :" +uriParams.length+"</H3>");
			out.print("<H3>DOMAIN IS :" + uriParams[3]+"</H3>");
		} else if (uriParams.length == 5) {
			searchDomain = uriParams[3];
			searchApp = uriParams[4];
 			out.print("<H3> APP/Host is: " + uriParams[4]+"</H3>");
			
		} else if (uriParams.length == 3){
			searchDomain = "All";
			out.print("<H3> ALL: " + uriParams[2]+"</H3>");
			try {
				runCommand(command, output);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public static void runCommand(final String[] command, final File output) throws IOException, InterruptedException {
     	final ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true); // optional; easier for this case to only
        // handle one stream
        System.out.println(pb.toString());
        pb.redirectOutput(Redirect.to(output));
        final Process p = pb.start();
 
        if (p.waitFor() != 0) {
            // throw an exception / return error message
        	System.err.println("Command Execution failed");
        } else {
        	System.out.println("Command Executed successfully. ");
        }
	}
}
