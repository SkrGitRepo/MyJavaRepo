package com.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

//import org.apache.tomcat.util.codec.binary.Base64;

/**
 * Servlet implementation class EmanServiceServlet
 */
@WebServlet("/EmanServiceServlet")
public class GetEmanResponseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetEmanResponseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		String webPage = null;
		
		System.out.println("REQUESTED URL :"+request.getRequestURL());
		System.out.println("REQUESTED URI :"+request.getRequestURI());
	    System.out.println("REQUESTED SERVLET PATH :"+request.getServletPath());
		String lifeCycle  =  request.getParameter("lc");
		if (lifeCycle.equals("dev")){
			//webPage = "https://ibpm-dev.cisco.com/ea/PRSOAPServlet/SOAP/CiscoSample/Services?wsdl";
			webPage = "https://wsgi-stage.cisco.com/ef/coverage/services/rest/serialnumber";
		} else if(lifeCycle.equals("stage")) {
			webPage = "https://ibpm-stage.cisco.com/ea/PRSOAPServlet/SOAP/CiscoSample/Services?wsdl";
		} else if (lifeCycle.equals("poc")) {
			//webPage = "https://ibpm-poc.cisco.com/prweb7/PRSOAPServlet/SOAP/CiscoSample/Services?wsdl";
			//webPage ="https://ibpm-poc.cisco.com/prweb7/PRRestService/CiscoSample/Services/HelloWorld";
			webPage ="https://ibpm.cisco.com/ea/PRRestService/CiscoSample/Services/HelloWorld";
		} else if (lifeCycle.equals("prd")) {
			webPage="https://ibpm.cisco.com:443/ea/deploy/DiagnosticData?path=StaticContent/global/ServiceExport/Cisco_EAFrameWorks_010192_testingCodeMigration.jar";
		}
		
		try {
			//String stageWebPage = "https://ibpm-stage.cisco.com/ea/PRSOAPServlet/SOAP/CiscoSample/Services?wsdl";
			//String pocWebPage = "https://ibpm-poc.cisco.com/prweb7/PRSOAPServlet/SOAP/CiscoSample/Services?wsdl";
			//String devWebPage = "https://ibpm-dev.cisco.com/ea/PRSOAPServlet/SOAP/CiscoSample/Services?wsdl";
			//String webPage = "https://ibpm-stage.cisco.com/nprd2/brmsadmin/disablemon";
			String name = "drdtest.gen";//"brm.gen";
			String password = "c1sco123";//"brmGen123";
			String emanResponse = null;

			String authString = name + ":" + password;
			//System.out.println("auth string: " + authString);
			//byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
			//String authStringEnc = new String(authEncBytes);
			//System.out.println("Base64 encoded auth string: " + authStringEnc);
			
			//String decodedValue = Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8)); 
			
			String decodedValue = DatatypeConverter.printBase64Binary(authString.getBytes("UTF-8"));
			//Base64.getDecoder().decode(authString.getBytes());
			//String authStringEnc = new String(decodedValue);
			//String (decodedValue, StandardCharsets.UTF_8);
			String authStringEnc = decodedValue;

			URL url = new URL(webPage);
					
			//URLConnection urlConnection = url.openConnection();
			//urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
			
			//InputStream is = urlConnection.getInputStream();
			//InputStreamReader isr = new InputStreamReader(is);

			HttpURLConnection httpCon = (HttpURLConnection)url.openConnection();
			httpCon.setRequestProperty("Authorization", "Basic " + authStringEnc);
			int statusCode = httpCon.getResponseCode();
			InputStream is = httpCon.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			
			//String resContent  =  http.getContentEncoding().toString();
			
			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			emanResponse = sb.toString();
			System.out.println(" ******* URL RESPONSE IS : *****");
			System.out.println("URL : "+webPage);
			System.out.println("Response STATUS CODE is :"+statusCode);
			String  resMessage = null;
			System.out.println(emanResponse);
			if (emanResponse.contains("HelloWorld")) {
				System.out.println(emanResponse);
				resMessage = emanResponse;
				//resMessage = "SUCCESS";
			} else {
				//resMessage = "FAILURE";
				System.out.println(emanResponse);
				resMessage = emanResponse;
			}
			System.out.println(" *******************************");
			request.setAttribute("urlResponse", resMessage);
			request.setAttribute("statusCode", Integer.toString(statusCode));
			//request.setAttribute("resContent", resContent);
			request.setAttribute("lc", lifeCycle);
			RequestDispatcher reqDispatcher = getServletConfig().getServletContext().getRequestDispatcher("/jsp/test_brms_eman_response.jsp");
			try {
				reqDispatcher.forward(request,response);
				httpCon.disconnect();
			} catch (IOException e) {
				System.err.println(" An exception occured while sending eman response to jsp.");
				e.printStackTrace();
				httpCon.disconnect();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//========================================================================================================
		
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		//PrintWriter out = response.getWriter();
		//out.println("<H5> AN EMAN PING RESPONSE");
				
		/*String url =  "https://ibpm-stage.cisco.com/aes/brms-nprd2-stg5/brmsadmin/eman";
		//http://ibpm-stage.cisco.com/ea/PRSOAPServlet/SOAP/CiscoSample/Services
		//url = url.replaceFirst("^https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.
		int timeout = 20000;
		String emanResponse = null;
	    URLConnection yc;
		try {
			URL urldemo = new URL(url);
			yc = urldemo.openConnection();
			BufferedReader resReader = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
					    
			while ((inputLine = resReader.readLine()) != null)
				emanResponse = inputLine;
			resReader.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(" ** An exception occured during https connection/response to/from EMAN url");
			e.printStackTrace();
		}
	  
	    request.setAttribute("emanResponse", emanResponse);
		RequestDispatcher reqDispatcher = getServletConfig().getServletContext().getRequestDispatcher("/jsp/test_brms_eman_response.jsp");
		try {
			reqDispatcher.forward(request,response);
		} catch (IOException e) {
			System.err.println(" An exception occured while sending eman response to jsp.");
			e.printStackTrace();
		}
		*/
	}
	
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
