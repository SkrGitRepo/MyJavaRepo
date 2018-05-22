package com.cisco.brmspega.servlet;

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

import com.cisco.brmspega.bundles.PropertyLoader;

/**
 * Servlet implementation class DisableMonitoringServlet
 */
@WebServlet("/GetJVMLastStartDateStatusServlet")
public class GetJVMLastStartDateStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String USER_ACTIVITY_LOG_PATH = "/opt/brms/shared/logs";
	private static final String serverToolsConfigBase = "/opt/brms/shared/scripts";//PropertyLoader.getInstance().getProperty("server_tools_cofig_base");
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetJVMLastStartDateStatusServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String brmsContext = request.getContextPath();
		String[] getVDC = brmsContext.split("/");
		String vdc = getVDC[1];
		String inEnv = "POC";
		String inType = "NS";
		String dayLimit = "15";
		
		ArrayList<String> finalLogData;
		ArrayList<String> allJVMData;
		
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();

		try {
			allJVMData = collect_vm_from_config_file(inEnv, vdc); 
			
			
			finalLogData = readUserActivityLog(vdc);
			if(!inType.equalsIgnoreCase("NS")) {
			for(int i=0; i <finalLogData.size();i++) {
				String monitorHost = finalLogData.get(i);
				if(monitorHost!= null && monitorHost.contains(inEnv))
					out.println(monitorHost);
			}
			}
			
			if(inType.equalsIgnoreCase("NS")) {
			out.print("<br/><br/><h2> ALL JVM LIST:::</h2>");
			for(int i=0; i <allJVMData.size();i++) {
				String eachJVMHostPort = allJVMData.get(i);
				if(eachJVMHostPort!= null)
					if(!finalLogData.contains(eachJVMHostPort))
						out.println(eachJVMHostPort);
			}
			}
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			if(out!=null) {
				out.flush();
				out.close();
			}
		}
	}

	
	/* This method read all '/opt/brms/shared/log/useractivity-*{Date}*.log' of last 1-month
	 * and will sort out the all the jvm(host:port) and latest user action(Start WL/Restart WL/Stop WL/Kill WL) performed
	 * based on logged Time stamp. 
	 * 
	 */
	private ArrayList<String> readUserActivityLog(String vdc) throws ParseException, IOException, InterruptedException {
		
		HashMap<String, String> uniqueHostLogMap = new HashMap<String, String>();
		SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy");
		ArrayList<String> allHostList = new ArrayList<String>();
		
		String folderLoc = USER_ACTIVITY_LOG_PATH;
		folderLoc += "/";
		FilenameFilter logFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
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
						//System.out.println(folderLoc + auditFile.getName());
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
										        .compile("\\b(Start WL|Kill WL|Stop WL|Restart WL|Kill Start WL|Kill Start Admin|Start TC|Stop TC|Kill TC)\\b");
										Matcher actionMatch = actionPatern.matcher(loggedLine);

										if (actionMatch.find()) {
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

											if ((loggedUserAction.matches("Start WL|Restart WL|Stop WL|Kill WL|Kill Start Admin|Start TC|Stop TC|Kill TC"))) {
												Date loggedTimeStamp = formatter.parse(actionTimeStamp);
												if (uniqueHostLogMap.containsKey(loggedHostAndPort)) {
													String timeAndActions = uniqueHostLogMap.get(loggedHostAndPort);
													String[] timeAndAction = timeAndActions.split("\\$");
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
														uniqueHostLogMap.put(loggedHostAndPort,
														        loggedTimeStamp + "$" + loggedUserAction);
													} else {
														uniqueHostLogMap.put(loggedHostAndPort,
														        loggedTimeStamp + "$" + loggedUserAction);
													}
												} else {
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
		
		String latestLog = null;
		ArrayList<String> fetchedLog = new ArrayList<String>();
		
		//System.out.println("*********************************************************************");
		//System.out.println(" LATEST ACTION :: All list of host(JVM's) from last "
		//		+ "1-Month useractivity based on latest ACTION:");
		//System.out.println("*********************************************************************");
		
		for (Map.Entry<String, String> entry : uniqueHostLogMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			String[] hostAndPort = key.split(":");
			String[] timeAndAction = value.split("\\$");
			String[] envHost = hostAndPort[0].split("-");
			String env = envHost[2];
			String domain = envHost[1];
			;
			env = env.replaceAll("[0-9]", "");
			if(domain.equalsIgnoreCase("test")) {
				env = "dev";
			}
			
			Pattern isDomain = Pattern
			        .compile("\\b(prd\\d+)\\b");
			Matcher prdDomainMatch = isDomain.matcher(domain);
			
			if(prdDomainMatch.find()) {
				env = "prd";
			}
			
			//Date thirtyMinuteBeforeTimeStamp = new Date(System.currentTimeMillis()- 30*60*1000);
			Date twentyFourHrsBeforeTimeStamp = new Date(System.currentTimeMillis()- 15 * (24 * (60*60*1000)));
			String[] token = timeAndAction[0].split(" ");
			String loggedTimeStamp = token[0] + " " + token[1] + " " + token[2] + " "
			        + token[3] + " " + token[5];
			Date recordedTimeStamp = formatter.parse(loggedTimeStamp);
			
			
			if(value.contains("Restart WL")) {
				if(recordedTimeStamp.after(twentyFourHrsBeforeTimeStamp)) {
					//System.out.println(key + "|" + value);
					
					latestLog = match_vm_from_config_file(hostAndPort[0],hostAndPort[1],env.toUpperCase(),vdc.toUpperCase());
					
					if(latestLog != null){
						System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+timeAndAction[1]+"|"+key);
						System.out.println(latestLog);
						fetchedLog.add(latestLog);
					}
				}
			} else if (value.contains("Start WL")) {
				if(recordedTimeStamp.after(twentyFourHrsBeforeTimeStamp)) {
					//System.out.println(key + "|" + value);
					//System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+timeAndAction[1]+"|"+key);
					latestLog = match_vm_from_config_file(hostAndPort[0],hostAndPort[1],env.toUpperCase(),vdc.toUpperCase());
					if(latestLog != null){
						System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+timeAndAction[1]+"|"+key);
						System.out.println(latestLog);
						fetchedLog.add(latestLog);
					}
				}
			} else if (value.contains("Kill Start Admin")) {
				if(recordedTimeStamp.after(twentyFourHrsBeforeTimeStamp)) {
					//System.out.println(key + "|" + value);
					//System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+timeAndAction[1]+"|"+key);
					latestLog = match_vm_from_config_file(hostAndPort[0],hostAndPort[1],env.toUpperCase(),vdc.toUpperCase());
					if(latestLog != null){
						System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+timeAndAction[1]+"|"+key);
						System.out.println(latestLog);
						fetchedLog.add(latestLog);
					}
				}
			} else if (value.contains("Start TC")) {
				if(recordedTimeStamp.after(twentyFourHrsBeforeTimeStamp)) {
					//System.out.println(key + "|" + value);
					//System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+timeAndAction[1]+"|"+key);
					latestLog = match_vm_from_config_file(hostAndPort[0],hostAndPort[1],env.toUpperCase(),vdc.toUpperCase());
					if(latestLog != null){
						System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+timeAndAction[1]+"|"+key);
						System.out.println(latestLog);
						fetchedLog.add(latestLog);
					}
				}
			} else if (value.contains("Restart TC")) {
				if(recordedTimeStamp.after(twentyFourHrsBeforeTimeStamp)) {
					//System.out.println(key + "|" + value);
					//System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+timeAndAction[1]+"|"+key);
					latestLog = match_vm_from_config_file(hostAndPort[0],hostAndPort[1],env.toUpperCase(),vdc.toUpperCase());
					if(latestLog != null){
						System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+timeAndAction[1]+"|"+key);
						System.out.println(latestLog);
						fetchedLog.add(latestLog);
					}
				}
			}
		
		}
		
		//System.out.println("*********************************************************************");
		//System.out.println("ALL SCANNED HOST :: All list of host(JVM's) from last "
		//		+ "1-Month useractivity ");
		//System.out.println("*********************************************************************");

		//if(!allHostList.isEmpty()) {
		//	for (int i = 0; i < allHostList.size(); i++) {
		//		//System.out.println(allHostList.get(i));
		//	}
		//}
		return fetchedLog;
	}
	
	/* This method will match the host:port in respective brms_vm_cfg_{vdc}_{lifecycle}.txt file
	 * to fetch the respective Domain of the specific configured jvm(host:port) 
	 * 
	 */
	private String match_vm_from_config_file(String host, String port, String env, String vdc) throws IOException {
		String fileName = serverToolsConfigBase+"/brms_vm_cfg_"+vdc+"_"+env+".txt";
		//System.out.println("Reading config file :"+fileName);
		File file = new File(fileName);
		String output = null;
		
		if(file.exists()){
			List<String> lines = FileUtils.readLines(file);
			for(String line:lines){
				if (!line.contains("CONTEXT")) {
					if (!line.isEmpty()) {
						String[] tokens = line.split(",");
						//System.out.println("LINE" + line);
						String jvm_port = tokens[3];
						if (tokens[1].contains("/tc")) {
							String[] jvm_name_token = tokens[1].split("/")[5].split("-");
							jvm_port = jvm_name_token[jvm_name_token.length - 1];
						}

						if (host.equals(tokens[0]) && port.equals(jvm_port)) {
							String[] domainPath = tokens[9].split("/");
							String vmhostPort = tokens[0] + ".cisco.com:" + jvm_port;
							output = env.toUpperCase() + "|" + domainPath[1] + "|" + vmhostPort;
							// System.out.println("-----------------------------------------------");
							// System.out.println(output);
							// System.out.println("============================================================================");
						}
					}
				}
			}
		} else {
			System.out.println("Matching config file does not exist : "+fileName);
		}
		return output;
	}
	
	
	private ArrayList<String> collect_vm_from_config_file(String env, String vdc) throws IOException {
		String fileName = serverToolsConfigBase+"/brms_vm_cfg_"+vdc+"_"+env+".txt";
		//System.out.println("Reading config file :"+fileName);
		File file = new File(fileName);
		ArrayList<String> allJVMData =  new ArrayList<String>();
		String output =  null;
		
		if(file.exists()){
			List<String> lines = FileUtils.readLines(file);
			for(String line:lines){
				if (!line.contains("CONTEXT")) {
					if (!line.isEmpty()) {
						String[] tokens = line.split(",");
						//System.out.println("LINE" + line);
						String jvm_port = tokens[3];
						if (tokens[1].contains("/tc")) {
							String[] jvm_name_token = tokens[1].split("/")[5].split("-");
							jvm_port = jvm_name_token[jvm_name_token.length - 1];
						}

						//if (host.equals(tokens[0]) && port.equals(jvm_port)) {
							String[] domainPath = tokens[9].split("/");
							String vmhostPort = tokens[0] + ".cisco.com:" + jvm_port;
							if (tokens[10].equalsIgnoreCase("STAGE")){
								env = "STG";
							} else if (tokens[10].equalsIgnoreCase("PROD")){
								env = "PRD";
							}
							output = env.toUpperCase() + "|" + domainPath[1] + "|" + vmhostPort;
							allJVMData.add(output);
							// System.out.println("-----------------------------------------------");
							// System.out.println(output);
							// System.out.println("============================================================================");
						//}
					}
				}
			}
		} else {
			System.out.println("Matching config file does not exist : "+fileName);
		}
		return allJVMData;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
