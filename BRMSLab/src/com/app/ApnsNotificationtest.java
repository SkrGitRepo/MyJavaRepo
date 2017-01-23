package com.app;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.PayloadBuilder;

public class ApnsNotificationtest {

	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ApnsService service = APNS.newService().withCert("/CiscoJars/ibpmcertificate.p12", "cisco123")
			    .withSandboxDestination()
			    .build();
		
		String payload = APNS.newPayload().alertBody("Domain: EA is down.")
				.badge(1)
				.customField("Identifier", "monitor")
				.build();
	
		List<String> tokenList = new ArrayList<String>();
		tokenList.add("59f9b03cbcf269449e0994aaf449f9505ff658cd557271f79b739fd729e21103");
		//String token = "59f9b03cbcf269449e0994aaf449f9505ff658cd557271f79b739fd729e21103";
		
		for (String token : tokenList) {
			service.push(token, payload);
		}
		
		
		service.testConnection();
		Map<String, Date> inactiveDevices = service.getInactiveDevices();
		
		for (String deviceToken : inactiveDevices.keySet()) {
			Date inactiveAsOf = inactiveDevices.get(deviceToken);
			System.out.println(deviceToken);
			System.out.println(inactiveAsOf);
		}
		
		//service.push(token, payload);

	}
	*/
	public void sendNotification() {
		// TODO Auto-generated method stub
		
		ApnsService service = APNS.newService().withCert("/CiscoJars/ibpmcertificate.p12", "cisco123")
			    .withSandboxDestination()
			    .build();
		
		String payload = APNS.newPayload().alertBody("Domain: EA is down.")
				.badge(1)
				.customField("Identifier", "monitor")
				.build();
	
		List<String> tokenList = new ArrayList<String>();
		tokenList.add("59f9b03cbcf269449e0994aaf449f9505ff658cd557271f79b739fd729e21103");
		//String token = "59f9b03cbcf269449e0994aaf449f9505ff658cd557271f79b739fd729e21103";
		
		for (String token : tokenList) {
			service.push(token, payload);
		}
		
		
		service.testConnection();
		Map<String, Date> inactiveDevices = service.getInactiveDevices();
		
		for (String deviceToken : inactiveDevices.keySet()) {
			Date inactiveAsOf = inactiveDevices.get(deviceToken);
			System.out.println(deviceToken);
			System.out.println(inactiveAsOf);
		}
		
		//service.push(token, payload);

	}

	

}
