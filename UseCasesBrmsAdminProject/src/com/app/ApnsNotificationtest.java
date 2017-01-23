package com.app;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

public class ApnsNotificationtest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ApnsService service = APNS.newService().withCert("/CiscoJars/certificate.p12", "")
			    .withSandboxDestination()
			    .build();
		String payload = APNS.newPayload().alertBody("Cisco IBPM Notification").build();
		String token = "59f9b03cbcf269449e0994aaf449f9505ff658cd557271f79b739fd729e21103";
		service.push(token, payload);

	}

}
