package com.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


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
		// String webPage = null;
		String downDomains = request.getParameter("down_domains");
		String downDomainsLifecycle = request.getParameter("lc");
		String postdata = null;
		/*
		 * System.out.println("DOWN_DOMAINS ::: "+downDomains);
		 * System.out.println("DOWN_DOMAINS LC ::: "+downDomainsLifecycle);
		 * System.out.println("REQUESTED URL :" + request.getRequestURL());
		 * System.out.println("REQUESTED URI :" + request.getRequestURI());
		 * System.out.println("REQUESTED SERVLET PATH :" +
		 * request.getServletPath());
		 */
		  String webPage = "http://localhost:8090/nprd2/brmsadmin/status/all";
		 

		//try {
		
		 String name = "brm.gen"; String password = "brmGen123"; String
		 statusResponse = null; String authString = name + ":" + password;
		 String decodedValue =
		 DatatypeConverter.printBase64Binary(authString.getBytes("UTF-8"));
		 String authStringEnc = decodedValue;
		  
		 URL url = new URL(webPage); 
		 HttpURLConnection httpCon =
		 (HttpURLConnection) url.openConnection();
		 httpCon.setRequestProperty("Authorization", "Basic " +
		 authStringEnc); 
		 int statusCode = httpCon.getResponseCode();
		 InputStream is = httpCon.getInputStream(); InputStreamReader isr =		 new InputStreamReader(is);
		
		 //String resContent = http.getContentEncoding().toString();
		 
		 int numCharsRead; char[] charArray = new char[1024]; StringBuffer sb
		  = new StringBuffer(); while ((numCharsRead = isr.read(charArray)) >
		  0) { sb.append(charArray, 0, numCharsRead); } statusResponse =
		  sb.toString();
		 
		  // String postUrl =
			// "http://localhost:8085/J2EESampleProject/getstatus";
			// String postUrl =
			// "http://brms-test-5:7010/nprd2/brmsadmin/getstatus";
		/*if (downDomainsLifecycle.equalsIgnoreCase("prod")) {
			postdata = "{" + "\"notification\": {" + "\"title\": \"Cisco iBPM Notification\",\"body\" : \"Domain(s):"
					+ downDomains + "are down\"," + "\"icon\" : \"ic_add_alert_black_24dp.png\"," + "}," + "\"data\": {"
					+ "\"url\": \"https://ibpm.cisco.com/prd2/brmsadmin/status\","
					+ "\"domainUrl\": \"https://ibpm.cisco.com/ea/brmsadmin\"," + "}," + "\"to\":\"/topics/myTopic\""
					+ "}";

		} else {
			postdata = "{" + "\"notification\": {" + "\"title\": \"" + downDomainsLifecycle.toUpperCase()
					+ ": Cisco iBPM Notification\"," + "\"body\" : \"Domain(s):" + downDomains + "are down\","
					+ "\"icon\" : \"ic_add_alert_black_24dp.png\"," + "}," + "\"data\": {"
					+ "\"url\": \"https://ibpm.cisco.com/prd2/brmsadmin/status\","
					+ "\"domainUrl\": \"https://ibpm.cisco.com/ea/brmsadmin\"," + "}," + "\"to\":\"/topics/myTopic\""
					+ "}";
		}*/

		//String postUrl = "https://gcm-http.googleapis.com/gcm/send";
		
		//System.out.println(" POST DATA ::" + postdata);
		//postJson(statusResponse,postUrl);
		// postJson(postdata,postUrl);

		/*
		 * } catch (MalformedURLException e) { e.printStackTrace(); } catch
		 * (IOException e) { e.printStackTrace(); }
		 */
		//String postUrl = "http://localhost:8090/J2EESampleProject/getstatus";
		//String postUrl = "http://brms-test-5:7010/nprd1/brmsadmin/work/info/csc";
		String postUrl = "http://localhost:8090/nprd2/brmsadmin/work/info/cpe/history";
		postJson(statusResponse,postUrl);
	}

	@SuppressWarnings("unused")
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
		StringEntity se = new StringEntity( "{  $or: [ {'_id' : 'RS-55934'} ] }" );
		//StringEntity se = new StringEntity(jsonData);
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
		String respMessage = sb.toString();
		System.out.println("RESPONSE MESSAGE:"+respMessage);
	}
}
