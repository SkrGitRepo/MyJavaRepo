package com.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

/**
 * Servlet implementation class DisableMonitoringServlet
 */
@WebServlet("/DisableMonitoringServlet")
public class DisableMonitoringServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DisableMonitoringServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String contextPath =  "/nprd1/brmsadmin"; //request.getContextPath();
		String brmsContext = contextPath;//request.getContextPath();
	
		PrintWriter out = response.getWriter();
		
		response.setContentType("text/plain");
		out.println(contextPath);
		
		String[] getVDC = brmsContext.split("/");
		//String vdc = getVDC[1];
		String vdc = "PRD1";
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
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ArrayList<String> readUserActivityLog(String vdc) throws ParseException, IOException, InterruptedException {
		
		HashMap<String, String> uniqueHostLogMap = new HashMap<String, String>();
		SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy");
		ArrayList<String> allHostList = new ArrayList<String>();
		// String folderLoc =
		// PropertyLoader.getInstance().getProperty("shared_context");
		String folderLoc = "/opt/brms/shared";
		folderLoc += "/logs/";
		// String hostName = "brms-prd1-11";
		// String portNum = "7006";
		FilenameFilter logFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				// System.out.println(lowercaseName);
				if (lowercaseName.endsWith(".log")) {
					return true;
				} else {
					return false;
				}
			}
		};

		File[] fileList = new File(folderLoc).listFiles(logFilter);
		Arrays.sort(fileList, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
			}
		});

		for (File auditFile : fileList) {
			if (auditFile.isFile()) {

				Date currentFileDate = new Date(auditFile.lastModified());
				Date montheLaterDate;
				Calendar cal2 = Calendar.getInstance();
				cal2.add(Calendar.MONTH, -1);
				montheLaterDate = cal2.getTime();
				// System.out.println(currentFileDate);

				if (montheLaterDate.before(currentFileDate)) {
					BufferedReader br = null;
					try {
						System.out.println(folderLoc + auditFile.getName());
						String logFileName = folderLoc + auditFile.getName();

						File file = new File(logFileName);
						if (file.exists()) {
							List<String> loggedLines = FileUtils.readLines(file);
							for (String loggedLine : loggedLines) {
								if (loggedLine.length() > 0) {
									if (!loggedLine.endsWith("Username")) {
										loggedLine = loggedLine.trim().replaceAll("\\s+", " ");
										//System.out.println(loggedLine);
										String[] tokens = loggedLine.split(" ");

										Pattern actionPatern = Pattern
										        .compile("\\b(Start WL|Kill WL|Stop WL|Restart WL|Kill Start WL|Kill Start Admin)\\b");
										Matcher actionMatch = actionPatern.matcher(loggedLine);

										if (actionMatch.find()) {
											// System.err.println(loggedLine);
											String actionTimeStamp1 = tokens[0] + " " + tokens[1] + " " + tokens[2] + " "
											        + tokens[3] + " "+tokens[4]+ " " + tokens[5];
											String actionTimeStamp = tokens[0] + " " + tokens[1] + " " + tokens[2] + " "
											        + tokens[3] + " "+ tokens[5];
											

											boolean result = tokens[9].matches("brms-.*");
											String loggedHostAndPort;
											String loggedHost;
											String loggedPort;
											String loggedUserAction;

											if (result) {
												loggedUserAction = tokens[6] + " " + tokens[7] + " " + tokens[8];
												loggedHostAndPort = tokens[9] + ":" + tokens[10];
												loggedHost = tokens[9];
												loggedPort = tokens[10];
											} else {
												loggedUserAction = tokens[6] + " " + tokens[7];
												loggedUserAction.trim();
												loggedHostAndPort = tokens[8] + ":" + tokens[9];
												loggedHost = tokens[8];
												loggedPort = tokens[9];
											}

											allHostList.add(actionTimeStamp1+"|"+loggedHostAndPort+"|"+loggedUserAction);

											if ((loggedUserAction.matches("Start WL|Restart WL|Stop WL|Kill WL|Kill Start Admin"))) {
												Date loggedTimeStamp = formatter.parse(actionTimeStamp);
												if (uniqueHostLogMap.containsKey(loggedHostAndPort)) {
													String timeAndActions = uniqueHostLogMap.get(loggedHostAndPort);
													
													String[] timeAndAction = timeAndActions.split("\\$");
													//System.out.println("Logged Time Stamp"+loggedTimeStamp);
													//System.out.println("Existing"+timeAndAction[0]);
													String[] timeStamp = timeAndAction[0].split(" ");
													String existingTimeStamp = null;
													if(timeStamp[5]!=null){
														existingTimeStamp = timeStamp[0] + " " + timeStamp[1] + " " + timeStamp[2] + " "
													        + timeStamp[3] + " " + timeStamp[5];
													} else {
														existingTimeStamp = timeStamp[0] + " " + timeStamp[1] + " " + timeStamp[2] + " "
														        + timeStamp[3] + " " + timeStamp[4];
													} 
													
													Date recordedTimeStamp1 = formatter.parse(existingTimeStamp);
													
													if(recordedTimeStamp1.before(loggedTimeStamp)) {
														//System.out.println(loggedHostAndPort+"$$$$$$$ RECORDED TIMESTAMP :"+existingTimeStamp+"**loggedUserAction**"+loggedUserAction);
														//System.out.println(loggedHostAndPort+"$$$$$$$ LOGGED TIMESTAMP :"+loggedTimeStamp+"**loggedUserAction**"+loggedUserAction);
														uniqueHostLogMap.put(loggedHostAndPort,
														        loggedTimeStamp + "$" + loggedUserAction);
													} else {
														//System.out.println(loggedHostAndPort+" *****RECORDED TIMESTAMP :"+existingTimeStamp+"**loggedUserAction**"+loggedUserAction);
														//System.out.println(loggedHostAndPort+" *****RECORDED TIMESTAMP :"+loggedTimeStamp+"**loggedUserAction**"+loggedUserAction);
														uniqueHostLogMap.put(loggedHostAndPort,
														        loggedTimeStamp + "$" + loggedUserAction);
													}
												} else {
													//System.out.println(loggedHostAndPort+" *****FRSSHH TIMESTAMP :"+actionTimeStamp1+ "**loggedUserAction**"+loggedUserAction);
													uniqueHostLogMap.put(loggedHostAndPort,
													        actionTimeStamp1 + "$" + loggedUserAction);
												}
												
											}
										}

									}
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (br != null) {
							try {
								br.close();
							} catch (IOException ioe) {
								System.out.println("IOException :" + ioe);
							}
						}
					}
				}
			}
		}
		
		System.out.println("*********************************************************************");
		System.out.println(" LATEST ACTION :: All list of host(JVM's) from last "
				+ "1-Month useractivity based on latest ACTION:");
		System.out.println("*********************************************************************");
		
		ArrayList<String> fetchedLog = new ArrayList<String>();
		for (Map.Entry<String, String> entry : uniqueHostLogMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			String[] hostAndPort = key.split(":");
			String[] timeAndAction = value.split("\\$");
			System.out.println("Latest TimeStamp: "+timeAndAction[0]);
			System.out.println("Latest User Action: "+timeAndAction[1]);
			
			String[] envHost = hostAndPort[0].split("-");
			String env = envHost[2];
			String domain = envHost[1];
			env = env.replaceAll("[0-9]", "");
			
			
			if(domain.equalsIgnoreCase("test")) {
				env = "dev";
			}
			
			Pattern actionPatern = Pattern
			        .compile("\\b(prd\\d+)\\b");
			Matcher actionMatch = actionPatern.matcher(domain);
			if(actionMatch.find()) {
				env = "prd";
			}
			
			Date thirtyMinuteBeforeTimeStamp = new Date(System.currentTimeMillis()-30*60*1000);
			String[] token = timeAndAction[0].split(" ");
			String loggedTimeStamp = token[0] + " " + token[1] + " " + token[2] + " "
			        + token[3] + " " + token[5];
			Date recordedTimeStamp = formatter.parse(loggedTimeStamp);
			System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+timeAndAction[1]+"|"+key);
			
			if(value.contains("Restart WL")) {
				if(recordedTimeStamp.after(thirtyMinuteBeforeTimeStamp)) {
					System.out.println(key + "|" + value);
					
					String finalLog = match_vm_from_config_file(hostAndPort[0],hostAndPort[1],env.toUpperCase(),vdc.toUpperCase());
					
					if(finalLog != null){
						fetchedLog.add(finalLog);
					}
				}
			} else if (value.contains("Start WL")) {
				if(recordedTimeStamp.after(thirtyMinuteBeforeTimeStamp)) {
					System.out.println(key + "|" + value);
					System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+timeAndAction[1]+"|"+key);
					String finalLog = match_vm_from_config_file(hostAndPort[0],hostAndPort[1],env.toUpperCase(),vdc.toUpperCase());
					if(finalLog != null){
						fetchedLog.add(finalLog);
					}
				}
			}  else if (value.contains("Kill Start Admin")) {
				
				if (recordedTimeStamp.after(thirtyMinuteBeforeTimeStamp)) {
					System.out.println(key + "|" + value);
					System.out.println("IN KILL START ADMIN");
					System.out.println(env.toUpperCase() + "|" + vdc.toUpperCase() + "|" + loggedTimeStamp + "|"
							+ timeAndAction[1] + "|" + key);
					String finalLog = match_vm_from_config_file(hostAndPort[0], hostAndPort[1], env.toUpperCase(),
							vdc.toUpperCase());
					System.out.println("FINAL LOG"+finalLog);
					if (finalLog != null) {
						fetchedLog.add(finalLog);
					}
				} else {
					System.out.println("OUT OF TIME FRAME");
				}
		} else {
				System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+timeAndAction[1]+"|"+key);
				String finalLog = match_vm_from_config_file(hostAndPort[0],hostAndPort[1],env.toUpperCase(),vdc.toUpperCase());
				
				if(finalLog != null){
					fetchedLog.add(finalLog);
				}
			} 
		}
		
		System.out.println("*********************************************************************");
		System.out.println("ALL SCANNED HOST :: All list of host(JVM's) from last "
				+ "1-Month useractivity ");
		System.out.println("*********************************************************************");

		if(!allHostList.isEmpty()) {
			for (int i = 0; i < allHostList.size(); i++) {
				System.out.println(allHostList.get(i));
			}
		}
		return fetchedLog;
	}
	
	
	private String match_vm_from_config_file(String host, String port, String env, String vdc) throws IOException {
		//String fileName= "C:/opt/brms/shared/scripts"+"/brms_vm_cfg_"+vdc+"_"+env+".txt";
		String fileName= "C:/opt/brms/shared/scripts"+"/brms_vm_cfg_PRD1_PRD.txt";
		
		//System.out.println("Config File :"+fileName);
		//"C:/opt/brms/shared/scripts/brms_vm_cfg_PRD1_PRD.txt"
		// TODO Auto-generated method stub
		//System.out.println("-----------------------------------------------");
		System.out.println("Reading config file :"+fileName);
		File file = new File(fileName);
	
		String output = null;
		if(file.exists()){
			List<String> lines = FileUtils.readLines(file);
			for(String line:lines){
				if(!line.contains("CONTEXT")){
					String[] tokens=line.split(",");
					//System.out.println(tokens[0]);
					//System.out.println(tokens[3]);
					if (host.equals(tokens[0]) && port.equals(tokens[3])) {
						/*System.out.println(tokens[0]);
						System.out.println(tokens[3]);
						System.out.println(tokens[9]);
						System.out.println(tokens[10]);
						*/
						String[] domainPath = tokens[9].split("/"); 
						//System.out.println("Domain is "+domainPath[1]);
						output = env+"|"+domainPath[1]+"|"+tokens[0]+":"+tokens[3];
						System.out.println("-----------------------------------------------");
						
						System.out.println(output);
						System.out.println("============================================================================");
					} //else {
					//	System.err.println("NO ENTRY FOR :Host : "+host+" with PORT "+port+"in config file...!");
					//}
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
