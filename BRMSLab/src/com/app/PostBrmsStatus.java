package com.app;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;


import org.apache.http.impl.client.DefaultHttpClient;

import com.app.PostIOSNotification;


/**
 * Servlet implementation class PostBrmsStatus
 */
@WebServlet("/PostBrmsStatus")
public class PostBrmsStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostBrmsStatus() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		// PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		String downDomains = request.getParameter("down_domains");
		String downDomainsLifecycle = request.getParameter("lc");
		String postdata = null;
		PostIOSNotification postIOSNotification =  new PostIOSNotification();

		if (downDomainsLifecycle.equalsIgnoreCase("prod")) {
			postdata = "{" + "\"notification\": {" + "\"title\": \"Testing Ignore:: Cisco iBPM Notification\",\"body\" : \"Domain(s):"
					+ downDomains + "are down\"," + "\"icon\" : \"ic_add_alert_black_24dp.png\"," + "}," + "\"data\": {"
					+ "\"url\": \"https://ibpm.cisco.com/prd2/brmsadmin/status\","
					+ "\"domainUrl\": \"https://ibpm.cisco.com/ea/brmsadmin\"," + "}," + "\"to\":\"/topics/myTopic\""
					+ "}";
			postIOSNotification.sendNotification("mon",downDomainsLifecycle,downDomains,null);

		} else {
			postdata = "{" + "\"notification\": {" + "\"title\": \"" + downDomainsLifecycle.toUpperCase()
					+ ": Testing Ignore::Cisco iBPM Notification\"," + "\"body\" : \"Domain(s):" + downDomains + "are down\","
					+ "\"icon\" : \"ic_add_alert_black_24dp.png\"," + "}," + "\"data\": {"
					+ "\"url\": \"https://ibpm.cisco.com/prd2/brmsadmin/status\","
					+ "\"domainUrl\": \"https://ibpm.cisco.com/ea/brmsadmin\"," + "}," + "\"to\":\"/topics/myTopic\""
					+ "}";
			postIOSNotification.sendNotification("mon",downDomainsLifecycle,downDomains,null);
		}

		String postUrl = "https://gcm-http.googleapis.com/gcm/send";
		//System.out.println(" POST DATA ::" + postdata);
		postJson(postdata,postUrl);
		
	}

	private void postJson(String jsonData, String postUrl) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub

		final String USER_AGENT = "Mozilla/5.0";
		@SuppressWarnings({ "deprecation", "resource" })
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(postUrl);

		// add header
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Accept", "application/json");
		post.setHeader("Authorization", "key=AIzaSyBTXdvWyrcRfyTS7sfZ100Wp7IfLkSMaxY");
		StringEntity se = new StringEntity(jsonData);
		post.setEntity(se);

		HttpResponse response = client.execute(post);
		System.out.println("\n Sending 'POST' request to URL : " + postUrl);
		System.out.println("Post parameters : " + post.getEntity());

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		InputStreamReader isr = new InputStreamReader(response.getEntity().getContent());

		int numCharsRead;
		char[] charArray = new char[1024];
		StringBuffer sb = new StringBuffer();
		while ((numCharsRead = isr.read(charArray)) > 0) {
			sb.append(charArray, 0, numCharsRead);
		}
		System.out.println("**RESPONSE AFTER SENDING ANDROID NOTIFICATION:"+sb.toString());
	}
}
