package com.sample;
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

public class GetUrlOutput {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		long startTime,elapsed; 
		System.out.println("EmanServlet.doGet()...");
		//PrintWriter out = null;
		String responseStr = null;
		String eInd = null;
		int statusCode=0;
		
		String url = "https://ibpmadm-stage.cisco.com/ea/PRRestService/CiscoSample/Services/eman";
		//String url = "https://ibpmadm-stage.cisco.com/ea/brmsadmin/eman";
		System.out.println("EmanServlet.doGet() --> the URL is not null and not start with http://");
		// Https call
		startTime = new java.util.Date().getTime();;
		ClientConfig config = configureClient();
		Client client = Client.create(config);
		//HTTPBasicAuthFilter authFilter = new HTTPBasicAuthFilter("brm.gen", "brmGen123");
		//client.addFilter(authFilter);
		WebResource resource = client.resource(url);
		//ClientResponse response = resource.type(MediaType.TEXT_PLAIN).post(ClientResponse.class, "");
		ClientResponse response = resource.type(MediaType.TEXT_PLAIN).get(ClientResponse.class);
		elapsed = new java.util.Date().getTime() - startTime;
		statusCode = response.getStatus();
		String reasonPhrase = response.getClientResponseStatus().getReasonPhrase();
		if (statusCode == 200) {
			System.out.println("TYPE"+response.getType());
			responseStr = response.getEntity(String.class);
			System.out.println("Got status code : " + statusCode + ", reason phrase : " + reasonPhrase);
		} else {
			System.out.println("Got status code : " + statusCode + ", reason phrase : " + reasonPhrase);
			throw new Exception("Got status code : " + statusCode + ", reason phrase : " + reasonPhrase);
		}
		client.destroy();
		
		System.out.println("RESONSE:"+responseStr);
		System.out.println("RESONSE PHRASE:"+reasonPhrase);
		
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
