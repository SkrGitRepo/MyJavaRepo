package com.sample;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public class GetIpAndWriteToLog {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		URL obj;

		/* writing the log to the file */
		String folderLoc = "/opt/brms/shared/logs/";
		// get all headers
		// Set-Cookie

		obj = new URL("https://ibpmadm-dev.cisco.com/gssc-dv3/rmain/PRRestService/CiscoSample/Services/eman");
		URLConnection conn = obj.openConnection();
		Map<String, List<String>> headerFields = conn.getHeaderFields();

		Set<String> headerFieldsSet = headerFields.keySet();
		Iterator<String> hearerFieldsIter = headerFieldsSet.iterator();
		String domainName = null;
		String port = null;
		String Hostname = null;

		String usrResponse = "User Details {'UserID': 'mmohan', 'RequestorID': 'H2EF1225F1A6C0EF498684DBFC845A52D', 'Client': 'Google Chrome', 'Event': 'Link: Log Files', 'Date': '2019-1-15 11:7:33', 'Application Name': 'Source To Stock V 4, 'User Role': S2S -- System Admin , 'IPAddress': 173.38.66.49}";

		while (hearerFieldsIter.hasNext()) {

			String headerFieldKey = hearerFieldsIter.next();

			if ("Set-Cookie".equalsIgnoreCase(headerFieldKey)) {

				List<String> headerFieldValue = headerFields.get(headerFieldKey);

				for (String headerValue : headerFieldValue) {

					String[] fields = headerValue.split(";\\s*");
					String cookieValue = fields[0];
					if (cookieValue.contains("JSESSIONID")) {
						String expires = null;
						String path = null;
						String domain = null;
						boolean secure = false;

						// Parse each field
						for (int j = 1; j < fields.length; j++) {
							if ("secure".equalsIgnoreCase(fields[j])) {
								secure = true;
							} else if (fields[j].indexOf('=') > 0) {
								String[] f = fields[j].split("=");
								if ("expires".equalsIgnoreCase(f[0])) {
									expires = f[1];
								} else if ("domain".equalsIgnoreCase(f[0])) {
									domain = f[1];
								} else if ("path".equalsIgnoreCase(f[0])) {
									path = f[1];
								}
							}

						}
						domainName = path.split("\\/")[1];
						port = cookieValue.split("\\.")[1].split("-")[3].replace("84", "81");
						Hostname = cookieValue.split("\\.")[1].split("-")[0] + "-"
								+ cookieValue.split("\\.")[1].split("-")[1] + "-"
								+ cookieValue.split("\\.")[1].split("-")[2];
						//System.out.println("cookieValue:" + cookieValue);
						//System.out.println("Host-PORT" + cookieValue.split("\\.")[1]);
						//System.out.println("expires:" + expires);
						//System.out.println("path:" + path);
						//System.out.println("domainNAME:" + domainName);
						//System.out.println("domain:" + domain);
						//System.out.println("secure:" + secure);
					}
				}
			}
			continue;
		}

		
		System.out.println("*****************************************");
		InetAddress address = InetAddress.getByName(Hostname);
		System.out.println("IP Address of Domain/App JVM's:"+address.getHostAddress());
		//System.out.println(address.getHostName());
		folderLoc = folderLoc + "/" + Hostname + "/" + domainName + "-" + port;
		
		//Checking if Current Date file exist , if yes then log will be written to same else new date lof file will be created
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
		File user_access_log_file = new File(
				folderLoc + "/user_access_"+ dateFormat.format(date) +".log");
		if (!user_access_log_file.getParentFile().exists()) {
			user_access_log_file.getParentFile().mkdirs();
		}

		
		// This will 
		/*
		 *
		 * long days = 1; long eligibleFileCreationDate = System.currentTimeMillis() -
		 * (days * 24 * 60 * 60 * 1000); System.out.println(eligibleFileCreationDate);
		 * 
		 * if (user_access_log_file.lastModified() > eligibleFileCreationDate) {
		 * user_access_log_file.createNewFile(); }
		 */                        
		
		//Writing logs to existing file or creating new if does not exist
		  FileWriter fr = new FileWriter(user_access_log_file, true);
		  BufferedWriter br = new BufferedWriter(fr);
		  PrintWriter pr = new PrintWriter(br);
		  //System.out.println(usrResponse);
		  pr.println(usrResponse); 
		  pr.close();
		  br.close();
		  fr.close();
		  
	}

}
