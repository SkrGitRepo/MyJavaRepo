package com.app;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

/**
 * Servlet implementation class LogBrmsVmRestartActions
 */
@WebServlet("/LogBrmsVmRestartActions")
public class LogBrmsVmRestartActionsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogBrmsVmRestartActionsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		//String monitoringUrl = request.getRequestURL().toString();
		String monitoringUrl = "https://ibpm-stage.cisco.com/nprd2/brmsadmin/disableMonitoring";
		//System.out.println("URL :"+monitoringUrl);
		
		String contextPath =  "/nprd2/brmsadmin"; //request.getContextPath();
		String brmsContext = contextPath;//request.getContextPath();
		
		
		String contextRoot = new File(getServletContext().getRealPath("/")).getName();
		PrintWriter out = response.getWriter();
		
		response.setContentType("text/plain");
		out.println(contextPath);
		
		String responseStr = null;
		String eInd = null;
		int statusCode=0;
		String[] getVDC = brmsContext.split("/");
		String vdc = getVDC[1];
		ArrayList<String> finalLogData;
		try {
			finalLogData = readUserActivityLog(vdc);
			for(int i=0; i <finalLogData.size();i++) {
				String monitorHost = finalLogData.get(i);
				if(monitorHost!= null)
					out.println(monitorHost);
			}
			out.flush();
			out.close();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ArrayList<String> readUserActivityLog(String vdc) throws IOException, ParseException {
		// TODO Auto-generated method stub
		
		String dir = "C:/opt/brms/shared/logs";
		File latestUserActivityFile = lastFileModified(dir);
		
		ArrayList<String> fetchedLog = new ArrayList();
		Date loggedTimeStamp = null;
		File file = new File(latestUserActivityFile.toString());
		if(file.exists()){
			List<String> lines = FileUtils.readLines(file);
			for(String line:lines){
				if (line.length() >0 ){
					if(!line.endsWith("Username")) {
						line = line.trim().replaceAll("\\s+", " ");
						//System.out.println(line);
						String[] lineDatas = line.split(" ");
						/*String time = lineDatas[3]+" "+lineDatas[4];
						String userAction = lineDatas[6]+" "+lineDatas[7];
						String hostNamePort = lineDatas[8]+":"+lineDatas[9];
						String host = lineDatas[8];
						String port = lineDatas[9];*/
						Date currentTimeStamp =  new Date(System.currentTimeMillis());
						Date thirtyMinuteBeforeTimeStamp = new Date(System.currentTimeMillis()-30*60*1000);
						
						String loggedTStamp = lineDatas[0]+" "+lineDatas[1]+" "+lineDatas[2]+" "+lineDatas[3]+" "+lineDatas[5];
						SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy");
						loggedTimeStamp = formatter.parse(loggedTStamp);
						
						if( loggedTimeStamp.after(thirtyMinuteBeforeTimeStamp) ) {
						boolean result = lineDatas[9].matches("brms-.*");
						String loggedHostAndPort;
						String loggedTime;
						String loggedHost;
						String loggedPort;
						String userAction;
						if (result){	
							userAction = lineDatas[6]+" "+lineDatas[7]+" "+lineDatas[8];
							loggedHostAndPort = lineDatas[9]+":"+lineDatas[10];
							loggedTime = lineDatas[3]+" "+lineDatas[4];
							loggedHost = lineDatas[9];
							loggedPort = lineDatas[10];
						} else {
							userAction = lineDatas[6]+" "+lineDatas[7];
							loggedHostAndPort = lineDatas[8]+":"+lineDatas[9];
							loggedTime = lineDatas[3]+" "+lineDatas[4];
							loggedHost = lineDatas[8];
							loggedPort = lineDatas[9];
						}
						
						//System.out.println("TIME -> "+time);
						//System.out.println("userAction -> "+userAction);
						//System.out.println("Host:Port -> "+hostNamePort);
						System.out.println("Data from useractivity Log:");
						boolean actionKill = lineDatas[6].matches("Kill *");
						boolean actionStop = lineDatas[6].matches("Stop *");
						Pattern actionPatern = Pattern
						        .compile("\\b(Start WL|Restart WL)\\b");
						Matcher actionMatch = actionPatern.matcher(userAction);
						
						//if ( (!lineDatas[6].equalsIgnoreCase("Kill")) ) {
						if (actionMatch.find()) {
							
							//if ( (!lineDatas[6].equalsIgnoreCase("Stop")) ) {
							String[] envHost = loggedHost.split("-");
							String env = envHost[2];
							String domain = envHost[1];
							
							env = env.replaceAll("[0-9]", "");
							if(domain.equalsIgnoreCase("test")) {
								env = "dev";
							}
							System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+userAction+"|"+loggedHostAndPort);
							String finalLog = read_vm_config_file(loggedHost,loggedPort,env.toUpperCase(),vdc.toUpperCase());
							
							if(finalLog != null){
								fetchedLog.add(finalLog);
							}
							
							
							
							
								//System.out.println(actionKill);
								//System.out.println(actionStop);
								/*String[] envHost = loggedHost.split("-");
								String env = envHost[1];
								env = env.replaceAll("[0-9]", "");
								if(env.equalsIgnoreCase("test")) {
									env = "dev";
								}
								System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTime+"|"+userAction+"|"+loggedHostAndPort);
								
								String finalLog = read_vm_config_file(loggedHost,loggedPort,env.toUpperCase(),vdc.toUpperCase());
								if(finalLog != null){
									fetchedLog.add(finalLog);
								} */
							//} else {
							//	System.out.println("User activity log Action is "+userAction + "So skipping...!");
							//}
						
						} else {
							System.out.println("User activity log Action is "+userAction + "So skipping...!");
						}
						}
					}
				}
			}
		} else {
			System.out.println("Required file does not exist... !");
		}
		
		return fetchedLog;
	}


	private File lastFileModified(String dir) {
		// TODO Auto-generated method stub
		 File fl = new File(dir);
		    File[] files = fl.listFiles(new FileFilter() {          
		        public boolean accept(File file) {
		            return file.isFile();
		        }
		    });
		    long lastMod = Long.MIN_VALUE;
		    File choice = null;
		    for (File file : files) {
		        if (file.lastModified() > lastMod) {
		            choice = file;
		            lastMod = file.lastModified();
		        }
		    }
		    return choice;
	}

	private String read_vm_config_file(String host, String port, String env, String vdc) throws IOException {
		String fileName= "C:/opt/brms/shared/scripts"+"/brms_vm_cfg_"+vdc+"_"+env+".txt";
		
		System.out.println("Config File :"+fileName);
		//"C:/opt/brms/shared/scripts/brms_vm_cfg_PRD1_PRD.txt"
		// TODO Auto-generated method stub
		System.out.println("-----------------------------------------------");
		System.out.println("Read config file");
		System.out.println("-----------------------------------------------");
		File file = new File(fileName);
		
		//host = "brms-prd1-13";
		//port ="7005";
		String output = null;
		if(file.exists()){
			List<String> lines = FileUtils.readLines(file);
			for(String line:lines){
				if(!line.contains("CONTEXT")){
					String[] tokens=line.split(",");
					System.out.println(tokens[0]);
					System.out.println(tokens[3]);
					if (host.equals(tokens[0]) && port.equals(tokens[3])) {
						System.out.println(tokens[0]);
						System.out.println(tokens[3]);
						System.out.println(tokens[9]);
						System.out.println(tokens[10]);
						
						String[] domainPath = tokens[9].split("/"); 
						
						System.out.println("Domain is "+domainPath[1]);
						
						
						output = tokens[10]+"|"+domainPath[1]+"|"+tokens[0]+":"+tokens[3];
						System.out.println("-----------------------------------------------");
						System.out.println("Final Output :");
						System.out.println(output);
						
					} else {
						System.out.println("Host : "+host+" with PORT "+port+" not found.");
					}
				}
			}
		} else {
			System.out.println("File does not exist");
		}
		return output;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	
	
}
