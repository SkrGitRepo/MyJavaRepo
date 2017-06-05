package com.sample.utility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.json.JSONException;
import org.json.JSONObject;

public class ManageServerActionRequest {
	private static final String configFileFolderLoc =  "/opt/brms/shared/scripts/";
	
	public String  manageServerActionRequest(String jsonSARequest) {
		String env = null;
		String actionResult = null;
		String domain = null;
		String action = null;
		String jvm = null;
		String vdc = null;
		
		
		try {
			JSONObject jsonObject = new JSONObject(jsonSARequest);
	        env = jsonObject.getString("env");
	        domain = jsonObject.getString("domain");
	        action = jsonObject.getString("action");
	        jvm =  jsonObject.getString("jvm");
	        vdc = jvm.split("-")[1];
	        
	        List<String> jvmForAction = null;
	        
	        if(domain != null && jvm != null && action != null) {
	        	jvmForAction = fetchJvmDetails(env, domain, jvm, vdc);
	        	if (!jvmForAction.isEmpty()) {
		        	actionResult = "Domain:"+domain+" JVM:"+jvm +" has been: "+action;
		        	System.out.println( action + " will be performed on ::"+jvmForAction.get(0));
		        } else {
		        	actionResult = "Domain:"+domain+" JVM:"+jvm +"could not be: "+action;
		        }
	        }
	        
	        //String jvms =  jsonObject.getString("jvms");
	        /*JSONArray jvmList = new JSONArray(jvms);
	        System.out.println("REST REQUEST :: Env :"+env +" Domain "+domain + "JVM's"+jvms +" Action"+action);
	        
	        for(int i=0; i < jvmList.length();i++) {
	        	System.out.println("JVM in REQUEST :"+jvmList.getString(i));
	        	fetchJvmDetails(env, domain, jvmList.getString(i));
	        }*/
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return actionResult;
	}

	
	public List<String> fetchJvmDetails(String env,String domain,String jvm, String vdc) throws IOException {
		
		Collection<File> fileList = new ArrayList<File>();
		//String vdc = jvm.split("-")[1];
		
		String fileFilterString = "brms_vm_cfg_"+ vdc.toUpperCase() + "_"+ env.toUpperCase() + ".txt";
		System.out.println("CONFIG FILE OT READ :"+fileFilterString);
		fileList = FileUtils.listFiles(new File(configFileFolderLoc), new WildcardFileFilter(fileFilterString), null);
		File file;
		Iterator<File> fileListIterator = null;
		fileListIterator = fileList.iterator();
		List<String> serverCfgJVMList = new ArrayList<String>();
		
		
		while (fileListIterator.hasNext()) {
			file = (File) fileListIterator.next();
			System.out.println("-----------------------------------------------");
			System.out.println("Read config file"+file.getName());
			System.out.println("-----------------------------------------------");

			if (file.exists()) {
				List<String> lines = FileUtils.readLines(file);
				for (String line : lines) {
					if ((!line.contains("CONTEXT")) || (!line.contains(""))) {
						if (!line.contains("#") && line.contains(domain.toLowerCase())) {
							String[] tokens = line.split(",");
							if(tokens[9].equalsIgnoreCase("/"+domain.toLowerCase()) && tokens[0].equalsIgnoreCase(jvm)) {
								serverCfgJVMList.add(tokens[0]+"|"+tokens[1]+"|"+tokens[3]+"|"+tokens[4]+"|"+tokens[9]+"|"+tokens[10]);
								System.out.println("CONFIG FILE DATA ::: "+tokens[0]+"|"+tokens[1]+"|"+tokens[3]+"|"+tokens[4]+"|"+tokens[9]+"|"+tokens[10]);
							}
						}
					}
				}
			}
		}
		
		return serverCfgJVMList;
	}

}
