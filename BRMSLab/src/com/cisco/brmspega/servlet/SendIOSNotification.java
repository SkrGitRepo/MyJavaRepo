package com.cisco.brmspega.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.PostIOSNotification;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

/**
 * Servlet implementation class PostIOSNotification
 */
@WebServlet("/PostIOSNotification")
public class SendIOSNotification extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendIOSNotification() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		PostIOSNotification postIOSNotification = new PostIOSNotification();
		postIOSNotification.sendNotification(null, null, null,null);
		
		/*ApnsService service = APNS.newService().withCert("/CiscoJars/ibpmcertificate.p12", "cisco123")
			    .withSandboxDestination()
			    .build();
		
		String payload = APNS.newPayload().alertBody("Cisco IBPM Notification: Domain EABV is down.")
				.badge(1)
				.customField("Identifier", "monitor")
				.build();
	
		List<String> tokenList = new ArrayList<String>();
		tokenList.add("59f9b03cbcf269449e0994aaf449f9505ff658cd557271f79b739fd729e21103");
		//String token = "59f9b03cbcf269449e0994aaf449f9505ff658cd557271f79b739fd729e21103";
		
		for (String token : tokenList) {
			service.push(token, payload);
		}
				
//		service.testConnection();
//		Map<String, Date> inactiveDevices = service.getInactiveDevices();
//		
//		for (String deviceToken : inactiveDevices.keySet()) {
//			Date inactiveAsOf = inactiveDevices.get(deviceToken);
//			System.out.println(deviceToken);
//			System.out.println(inactiveAsOf);
//		}
*/	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
