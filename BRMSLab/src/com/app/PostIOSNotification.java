package com.app;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.cisco.brmspega.servlet.StoreIOSDeviceToken;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;
import com.notnoop.apns.PayloadBuilder;

public class PostIOSNotification {

	public String sendNotification(String sType,String lifeCycle,String domain,String sr_id) throws IOException {
		// TODO Auto-generated method stub
		
		String message = "Failed";
		String payload = null;
		String notificationBody;
		
		ApnsService service = APNS.newService().withCert("/CiscoJars/ibpmcertificate.p12", "cisco123")
			    .withSandboxDestination()
			    .build();
		
		//ApnsService service = APNS.newService().withCert("/opt/brms/shared/brmsclient.jks", "cisco123")
		//	    .withSandboxDestination()
		//	    .build();
		
		
		if (sType != null && sType.equalsIgnoreCase("mon") && domain != null) {
			if ( lifeCycle != null && lifeCycle.equalsIgnoreCase("prod")) {
				notificationBody = "TESTING :: Cisco IBPM Mon Notification. Domain(s):" + domain +" are down.";
			} else {
				notificationBody = "TESTING :: Cisco IBPM Mon Notification."+ lifeCycle + "-Domain(s):" + domain +" are down.";
			}
			payload = APNS.newPayload().alertBody(notificationBody)
					.badge(1)
					.customField("Identifier", "monitor")
					.build();

		} else if (sType != null && sType.equalsIgnoreCase("status") && domain != null && sr_id != null ) {
			
			notificationBody = "Cisco IBPM Status Notification. Domain:" + domain +" SR_ID:" +sr_id;
			payload = APNS.newPayload().alertBody(notificationBody)
					.badge(1)
					.customField("Identifier", "status")
					.customField("Domain", domain)
					.customField("sr_id", sr_id)
					.build();
			
		}
		
		List<String> tokenList = new ArrayList<String>();
		//StoreIOSDeviceToken getIOSToken = new StoreIOSDeviceToken();
		//List<String> tokenList = getIOSToken.iosDeviceTokenList;
		//tokenList.add("59f9b03cbcf269449e0994aaf449f9505ff658cd557271f79b739fd729e21103");
		//tokenList.add("dc09fe83c5f68ace0d97491e100f170473199cfa7dba2417180b65fe9ac3b76e");
		//String token = "59f9b03cbcf269449e0994aaf449f9505ff658cd557271f79b739fd729e21103";
		tokenList = get_device_token_list();
		
		if (payload != null && !tokenList.isEmpty()) {
			for (String token : tokenList) {
				service.push(token, payload);
				message = "success";
			}
			
			service.testConnection();
			Map<String, Date> inactiveDevices = service.getInactiveDevices();
			
			for (String deviceToken : inactiveDevices.keySet()) {
				Date inactiveAsOf = inactiveDevices.get(deviceToken);
				System.out.println(deviceToken);
				System.out.println(inactiveAsOf);
			}
			
		}
		
		return message;

	}

	
	public List<String> get_device_token_list() throws IOException {
		
		List<String> deviceTokeList = new ArrayList<String>();
		String folderLoc = "/opt/brms/shared/scripts";//PropertyLoader.getInstance().getProperty("monitor_config_base");
		File token_lists_file = new File(folderLoc+"/ios_device_token_list.txt");
		
		List<String> deviceTokenEntries = null;
		
		if (token_lists_file.exists()) {
			deviceTokenEntries = FileUtils.readLines(token_lists_file);
			if(!deviceTokenEntries.isEmpty()) {
				for (String token : deviceTokenEntries) {
					if (null == token || token.trim().equalsIgnoreCase("")) {
						continue;
					} else {
						System.out.println("Device token read from file is: "+token);
						deviceTokeList.add(token);
					}
				}
			}
		}
		
		return deviceTokeList;
	}

}
