package com.sample;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class GetUrlEndPointIP {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException,MalformedURLException {
		// TODO Auto-generated method stub

		URL obj;

		/* writing the log to the file */
		String folderLoc = "/opt/brms/shared/logs/";
		// File user_access_log_file = new File(folderLoc + "/user_access.log");

		/*
		 * try { obj = new
		 * URL("https://ibpm.cisco.com/cpe/cs/PRRestService/CiscoSample/Services/eman");
		 * URLConnection conn = obj.openConnection(); Map<String, List<String>> map =
		 * conn.getHeaderFields(); for (Map.Entry<String, List<String>> entry :
		 * map.entrySet()) { System.out.println("Key : " + entry.getKey() + " ,Value : "
		 * + entry.getValue()); } String server = conn.getHeaderField("Set-Cookie");
		 * System.out.println("*****SERVER NAME:: " + server);
		 * 
		 * String hostname= "brms-nprd2-adm1"; InetAddress address =
		 * InetAddress.getByName(hostname);
		 * System.out.println(address.getHostAddress());
		 * System.out.println(address.getHostName());
		 * 
		 * FileWriter fr = new FileWriter(user_access_log_file, true); BufferedWriter br
		 * = new BufferedWriter(fr); PrintWriter pr = new PrintWriter(br);
		 * pr.println(server); pr.close(); br.close(); fr.close();
		 * 
		 * } catch (MalformedURLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

		/*
		 * 
		 * if (!user_access_log_file.getParentFile().exists()) {
		 * user_access_log_file.getParentFile().mkdir(); } if
		 * (!user_access_log_file.exists()) {
		 * user_access_log_file.getParentFile().mkdir();
		 * user_access_log_file.createNewFile(); }
		 * 
		 * 
		 * String data = "Access entry for user: sumkuma2";
		 * 
		 * FileWriter writeData = new FileWriter(user_access_log_file) ;
		 * if(user_access_log_file.exists()) { writeData.append(data+"\n");
		 * System.out.println("FILE Already exists, appending"); } else {
		 * System.out.println("FILE does not exists, adding"); writeData.write(data +
		 * "\n"); } //writeData.append(data + "\n"); writeData.close();
		 * 
		 * //writeData.append(data);
		 */

		// get all headers

		// Set-Cookie

		obj = new URL("https://ibpmadm.cisco.com/cpe/cs/PRRestService/CiscoSample/Services/eman");
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

					System.out.println("Cookie Found...");

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

						System.out.println("cookieValue:" + cookieValue);
						port = cookieValue.split("\\.")[1].split("-")[3].replace("84", "81");
						Hostname = cookieValue.split("\\.")[1].split("-")[0] + "-"
								+ cookieValue.split("\\.")[1].split("-")[1] + "-"
								+ cookieValue.split("\\.")[1].split("-")[2];
						System.out.println("PORT" + port);
						System.out.println("HOSTNAME" + Hostname);
						System.out.println("Host-PORT" + cookieValue.split("\\.")[1]);
						System.out.println("expires:" + expires);
						System.out.println("path:" + path);
						domainName = path.split("\\/")[1];
						System.out.println("domainNAME:" + domainName);
						System.out.println("domain:" + domain);
						System.out.println("secure:" + secure);

					}
				}

			}
			continue;
		}

		System.out.println("*****************************************");

		InetAddress address = InetAddress.getByName(Hostname);
		System.out.println(address.getHostAddress());
		System.out.println(address.getHostName());
		folderLoc = folderLoc + "/" + Hostname + "/" + domainName + "-" + port;
		
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
		File user_access_log_file = new File(
				folderLoc + "/user_access_"+ dateFormat.format(date) +".log");

		if (!user_access_log_file.getParentFile().exists()) {
			user_access_log_file.getParentFile().mkdirs();
			//user_access_log_file.createNewFile();
		}

		/*
		 * long days = 1; long eligibleFileCreationDate = System.currentTimeMillis() -
		 * (days * 24 * 60 * 60 * 1000); System.out.println(eligibleFileCreationDate);
		 * 
		 * if (user_access_log_file.lastModified() > eligibleFileCreationDate) {
		 * user_access_log_file.createNewFile(); }
		 */                        
		
		  FileWriter fr = new FileWriter(user_access_log_file, true);
		  BufferedWriter br = new BufferedWriter(fr);
		  PrintWriter pr = new PrintWriter(br);
		  System.out.println(usrResponse);
		  pr.println(usrResponse); 
		  pr.close();
		  br.close();
		  fr.close();
		 

		  
		  
		  
	}

}
