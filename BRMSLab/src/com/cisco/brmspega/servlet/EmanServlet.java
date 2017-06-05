package com.cisco.brmspega.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

import com.cisco.brmspega.bundles.PropertyLoader;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

public class EmanServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9025288579935013341L;

	public EmanServlet() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		System.out.println("EmanServlet.doGet()...");
		PrintWriter out = null;
		String responseStr = null;
		String eInd = null;
		int statusCode=0;
		try {
			System.out.println("In EmanServlet!!!!!!!!!!!!!!");
			// String context = req.getContextPath();
			// String contextId = "";
			// if (context.indexOf("/") != -1 && context.lastIndexOf("/") != -1 && context.indexOf("/") < context.lastIndexOf("/")) {
			// contextId = context.substring(context.indexOf("/") + 1, context.lastIndexOf("/"));
			// }
			// // Checking if the brmsAdmin is the main server
			// if (contextId.equals("nprd1") || contextId.equals("nprd2") || contextId.equals("prd1") || contextId.equals("prd2")) {
			// responseStr = "EMAN OK! contextId : " + contextId;
			// } else {
			String url = req.getParameter("u");
			String server = req.getParameter("s");
			String port = req.getParameter("p");
			String context = req.getParameter("c");
			eInd = req.getParameter("eInd");
			long startTime,elapsed; 
			
			if (url != null) {
				System.out.println("EmanServlet.doGet() --> the URL is not null");
				// This is for direct url access
				System.out.println("Url hit : " + url);
				if (url.startsWith("http://")) {
					System.out.println("EmanServlet.doGet() --> the URL is not null and start with http://");
					// Http call
					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet(url);
					startTime = new java.util.Date().getTime();;
					HttpResponse response = client.execute(request);
					elapsed = new java.util.Date().getTime() - startTime;
					statusCode = response.getStatusLine().getStatusCode();
					String reasonPhrase = response.getStatusLine().getReasonPhrase();
					if (statusCode == 200) {
						BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
						String line = null;
						while ((line = rd.readLine()) != null) {
							if (responseStr == null) {
								responseStr = "";
							}
							responseStr += line;
						}
					} else {
						throw new Exception("Got status code : " + statusCode + ", reason phrase : " + reasonPhrase);
					}
				} else {
					System.out.println("EmanServlet.doGet() --> the URL is not null and not start with http://");
					// Https call
					startTime = new java.util.Date().getTime();;
					ClientConfig config = configureClient();
					Client client = Client.create(config);
					HTTPBasicAuthFilter authFilter = new HTTPBasicAuthFilter("brm.gen", "brmGen123");
					client.addFilter(authFilter);
					WebResource resource = client.resource(url);
					ClientResponse response = resource.type(MediaType.TEXT_PLAIN).post(ClientResponse.class, "");
					elapsed = new java.util.Date().getTime() - startTime;
					statusCode = response.getStatus();
					String reasonPhrase = response.getClientResponseStatus().getReasonPhrase();
					if (statusCode == 200) {
						responseStr = response.getEntity(String.class);
					} else {
						throw new Exception("Got status code : " + statusCode + ", reason phrase : " + reasonPhrase);
					}
					client.destroy();
				}
			} else {
				// Url formation call
				System.out.println("EmanServlet.doGet() --> the URL value is null");
				if (server != null) {
					url = "http://" + server + ":" + port + context + PropertyLoader.getInstance().getProperty("server_tools_eman_servlet");
				} else {
					url = "http://" + req.getLocalName() + ":" + req.getLocalPort()
							+ req.getRequestURI().substring(0, req.getRequestURI().indexOf("brmsadmin/eman") - 1)
							+ PropertyLoader.getInstance().getProperty("server_tools_eman_servlet");
				}
				System.out.println("EmanServlet.doGet() --> URL: "+url);
				HttpParams params = new BasicHttpParams();
				params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 100000);
				params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 100000);
				//HttpClient client = new DefaultHttpClient(params);
				
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(url);
				System.out.println("EmanServlet.doGet() --> request.getURI :"+request.getURI());
				System.out.println("EmanServlet.doGet() -->"+" client.toString() :"+client.toString());
				startTime = new java.util.Date().getTime();
				HttpResponse response = client.execute(request);
				elapsed = new java.util.Date().getTime() - startTime;
				Header[] h = response.getAllHeaders();
				if(h != null) {
					for(int i=0;i<h.length;i++) {
						System.out.println("EmanServlet.doGet() --> HEADER "+" Name :"+h[i].getName()+" :: "+"Value :"+h[i].getValue());
					}
				}
				statusCode = response.getStatusLine().getStatusCode();
				System.out.println("EmanServlet.doGet() --> statusCode :"+statusCode);
				String reasonPhrase = response.getStatusLine().getReasonPhrase();
				System.out.println("EmanServlet.doGet() --> reasonPhrase :"+reasonPhrase);
				if (statusCode == 200) {
					BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					String line = null;
					while ((line = rd.readLine()) != null) {
						if (responseStr == null) {
							responseStr = "";
						}
						responseStr += line;
					}
				} else {
					System.out.println("EmanServlet.doGet() --> throw exception -->"+"Got status code : " + statusCode + ", reason phrase : " + reasonPhrase);
					throw new Exception("Got status code : " + statusCode + ", reason phrase : " + reasonPhrase);
				}
			}
			res.setContentType("text/html"); 
			out = res.getWriter();
//			out.println(responseStr+" ("+elapsed+" ms)");
			out.println(responseStr);
			out.flush();
		} catch (Exception e) {
			System.out.println("============ Issue ============"+e.toString());
			e.printStackTrace();
			// throw new ServletException("EMAN Failed with errors : " + e);
			if (eInd != null && !"".equals(eInd)) {
				responseStr = "UNABLE TO CONNECT. SERVER DOWN!" + e.getMessage();
			} else {
				responseStr = "UNABLE TO CONNECT. SERVER DOWN!";
			}
			res.setContentType("text/html");
			res.setStatus(statusCode);
			out = res.getWriter();
			out.println(responseStr);
			out.flush();
		} finally {
			if (out != null)
				out.close();
		}
	}
	
	public static ClientConfig configureClient() {
		TrustManager[ ] certs = new TrustManager[ ] {
	           new X509TrustManager() {
					public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
						System.out.println("In checkClientTrusted with arg0 : " + arg0.length + ", arg1 : " + arg1);
					}
					public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
						// System.out.println("In checkServerTrusted with arg0 : " + arg0.length + ", arg1 : " + arg1);
					}
					public X509Certificate[] getAcceptedIssuers() {
						// System.out.println("In getAcceptedIssuers");
						return null;
					}
					
				}
	   };
	   SSLContext ctx = null;
	   try {
	       ctx = SSLContext.getInstance("TLS");
	       ctx.init(null, certs, new SecureRandom());
	       // System.out.println("Done with init context.");
	   } catch (java.security.GeneralSecurityException ex) {
		   System.out.println("Sachin : Context Error : ");
		   ex.printStackTrace();
	   }
	   HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
	   
	   ClientConfig config = new DefaultClientConfig();
	   try {
		   config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(
		       new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						// System.out.println("In verify with hostname : " + hostname + ", session : " + session);
						return true;
					}
		       }, 
		       ctx
		   ));
	   } catch(Exception e) {
		   System.out.println("Sachin : Config Error : ");
		   e.printStackTrace();
	   }
	   return config;
	}
	
}
