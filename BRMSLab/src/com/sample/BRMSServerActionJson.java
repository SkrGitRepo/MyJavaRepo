package com.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
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

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BRMSServerActionJson {
	private static final String USER_ACTIVITY_LOG_PATH = "/opt/brms/shared/logs";
	private static final String serverToolsConfigBase = "/opt/brms/shared/scripts";
	public static void main(String[] args) throws ParseException, IOException, InterruptedException {
		ArrayList<String> finalLogData = new ArrayList<String>();
		String dataCenter =  "NPRD2";
		String[] lifecycle = {"DEV","STG","POC"};
		
		JSONObject superobj = new JSONObject();

		for (String lc : lifecycle) {

			finalLogData = readUserActivityLog(dataCenter);
			JSONArray lcObjList = new JSONArray();
			JSONArray domainObjList = new JSONArray();
			for (int i = 0; i < finalLogData.size(); i++) {
				String userServerActionActivity = finalLogData.get(i);
				if (userServerActionActivity != null && userServerActionActivity.contains(lc)) {
					// System.out.println("DATA: "+userServerActionActivity);
					String[] tokens = userServerActionActivity.split("\\|");
					
					/*JSONObject domainObj = new JSONObject();
					domainObjList
					if(domainObj.containsKey(tokens[1])) {
					
						
						
					} else {
						
						
						
					}*/
					JSONObject obj1 = new JSONObject();
					obj1.put("domain", tokens[1]);
					obj1.put("hostname:port", tokens[2]);
					obj1.put("ActionTime", tokens[3]);
					obj1.put("ServerAction", tokens[4]);
					obj1.put("ActionUser", tokens[5]);
					obj1.put("ActionReason", tokens[6]);

					lcObjList.add(obj1);
					superobj.put(lc, lcObjList);

				}
			}
		}
      /*  JSONObject obj1 = new JSONObject();
        obj1.put("domain", "csm");
        obj1.put("hostname:port", "brms-nprd2-01:8100");
        obj1.put("ActionTime", "Start TC");
        obj1.put("ActionUser", "sumkuma2");
        obj1.put("ActionReason", "DB Config changes");*/

        
       /* JSONObject obj2 = new JSONObject();
        obj2.put("domain", "csm");
        obj2.put("hostname:port", "brms-nprd2-03:8100");
        obj2.put("ActionTime", "Stop TC");
        obj2.put("ActionUser", "sibeeram");
        obj2.put("ActionReason", "code deployment");*/

        
        
        //superObjList.add(obj1);
        //superObjList.add(obj2);

        
        

        try (FileWriter file = new FileWriter("c:\\users/sumkuma2/brmsserveraction.json")) {

            file.write(superobj.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print(superobj);

    }

	
public static ArrayList<String> readUserActivityLog(String vdc) throws ParseException, IOException, InterruptedException {
		
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
											String actionUser;
											String actionReason;

											if (result) {
												loggedUserAction = tokens[6] + " " + tokens[7] + " " + tokens[8];
												loggedHostAndPort = tokens[9] + ":" + tokens[10];
												loggedHost = tokens[9];
												loggedPort = tokens[10];
												actionUser = tokens[11];
												actionReason = tokens[12];
											} else {
												loggedUserAction = tokens[6] + " " + tokens[7];
												loggedUserAction.trim();
												loggedHostAndPort = tokens[8] + ":" + tokens[9];
												loggedHost = tokens[8];
												loggedPort = tokens[9];
												actionUser = tokens[10];
												actionReason = tokens[11];
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
														        loggedTimeStamp + "$" + loggedUserAction + "$" + actionUser + "$" + actionReason);
													} else {
														uniqueHostLogMap.put(loggedHostAndPort,
														        loggedTimeStamp + "$" + loggedUserAction + "$" + actionUser + "$" + actionReason);
													}
												} else {
													uniqueHostLogMap.put(loggedHostAndPort,
													        actionTimeStamp1 + "$" + loggedUserAction + "$" + actionUser + "$" + actionReason);
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
			String serverAction = timeAndAction[1];
			String actionUsername= timeAndAction[2];
			String actionReason= timeAndAction[3];
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
			//int adays  = Integer.parseInt(actionDays);
			//Date thirtyMinuteBeforeTimeStamp = new Date(System.currentTimeMillis()- 30*60*1000);
			//Date twentyFourHrsBeforeTimeStamp = new Date(System.currentTimeMillis()- adays * (24 * (60*60*1000)));
			String[] token = timeAndAction[0].split(" ");
			String loggedTimeStamp = token[0] + " " + token[1] + " " + token[2] + " "
			        + token[3] + " " + token[5];
			Date recordedTimeStamp = formatter.parse(loggedTimeStamp);
			
			
			if(value.contains("Restart WL")) {
				//if(recordedTimeStamp.after(twentyFourHrsBeforeTimeStamp)) {
					//System.out.println(key + "|" + value);
					
					
					latestLog = match_vm_from_config_file(hostAndPort[0],hostAndPort[1],env.toUpperCase(),vdc.toUpperCase(),serverAction,recordedTimeStamp,actionUsername,actionReason);
					
					if(latestLog != null){
						//System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+timeAndAction[1]+"|"+key);
						//System.out.println(latestLog);
						fetchedLog.add(latestLog);
					}
				//}
			} else if (value.contains("Start WL")) {
				//if(recordedTimeStamp.after(twentyFourHrsBeforeTimeStamp)) {
					//System.out.println(key + "|" + value);
					//System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+timeAndAction[1]+"|"+key);
					latestLog = match_vm_from_config_file(hostAndPort[0],hostAndPort[1],env.toUpperCase(),vdc.toUpperCase(),serverAction,recordedTimeStamp,actionUsername,actionReason);
					if(latestLog != null){
						//System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+timeAndAction[1]+"|"+key);
						//System.out.println(latestLog);
						fetchedLog.add(latestLog);
					}
				//}
			} else if (value.contains("Kill Start Admin")) {
				//if(recordedTimeStamp.after(twentyFourHrsBeforeTimeStamp)) {
					//System.out.println(key + "|" + value);
					//System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+timeAndAction[1]+"|"+key);
					latestLog = match_vm_from_config_file(hostAndPort[0],hostAndPort[1],env.toUpperCase(),vdc.toUpperCase(),serverAction,recordedTimeStamp,actionUsername,actionReason);
					if(latestLog != null){
						//System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+timeAndAction[1]+"|"+key);
						//System.out.println(latestLog);
						fetchedLog.add(latestLog);
					}
				//}
			} else if (value.contains("Start TC")) {
				//if(recordedTimeStamp.after(twentyFourHrsBeforeTimeStamp)) {
					//System.out.println(key + "|" + value);
					//System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+timeAndAction[1]+"|"+key);
					latestLog = match_vm_from_config_file(hostAndPort[0],hostAndPort[1],env.toUpperCase(),vdc.toUpperCase(),serverAction,recordedTimeStamp,actionUsername,actionReason);
					if(latestLog != null){
						//System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+timeAndAction[1]+"|"+key);
						//System.out.println(latestLog);
						fetchedLog.add(latestLog);
					}
				//}
			} else if (value.contains("Restart TC")) {
				//if(recordedTimeStamp.after(twentyFourHrsBeforeTimeStamp)) {
					//System.out.println(key + "|" + value);
					//System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+timeAndAction[1]+"|"+key);
					latestLog = match_vm_from_config_file(hostAndPort[0],hostAndPort[1],env.toUpperCase(),vdc.toUpperCase(),serverAction,recordedTimeStamp,actionUsername,actionReason);
					if(latestLog != null){
						//System.out.println(env.toUpperCase()+"|"+vdc.toUpperCase()+"|"+loggedTimeStamp+"|"+timeAndAction[1]+"|"+key);
						//System.out.println(latestLog);
						fetchedLog.add(latestLog);
					}
				//}
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
	public static String match_vm_from_config_file(String host, String port, String env, String vdc,String serverAction, Date timestamp, String actionUsername, String actionReason) throws IOException {
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
							output = env.toUpperCase() + "|" + domainPath[1] + "|" + vmhostPort + "|" + timestamp + "|" + serverAction + "|" +actionUsername + "|" + actionReason;
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

}
