package com.cisco.brmspega.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.catalina.tribes.group.interceptors.DomainFilterInterceptor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
//import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

/**
 * Servlet implementation class NewDomainJsonServlet
 */
@WebServlet("/BRMSDomainStatusJsonThreadServlet")
public class BRMSDomainStatusJsonThreadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BRMSDomainStatusJsonThreadServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();

		String reqURI = request.getRequestURI();
		System.out.println("URI is :" + reqURI);
		out.println(reqURI);
		String searchDomain = null;
		String searchApp = null;
		// String sampleURI = "/nprd1/brmsadmin/status";
		// String[] uriSampleParams = sampleURI.split("\\/");
		String[] uriParams = reqURI.split("\\/");
		// out.println(uriParams.length);
		//out.println(uriParams.length);
		response.setContentType("application/json");
		if (uriParams.length == 4) {
			searchDomain = uriParams[3];
		} else if (uriParams.length == 5) {
			searchDomain = uriParams[3];
			searchApp = uriParams[4];
		} else if (uriParams.length == 3) {
			searchDomain = null;
			// searchDomain = "cvc";
			// searchApp = "score";
		}

		long startTime = System.currentTimeMillis();
		System.out.println("********** START TIME :" + startTime);

		final int MYTHREADS = 6;
		ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);

		List<Map<String, Object>> superJsonResponse = new ArrayList<Map<String, Object>>();

		//String[] lifecycle = { "DEV", "PRD", "LT", "STG", "POC" };
		String[] lifecycle = { "LT" };

		for (int i = 0; i < lifecycle.length; i++) {

			String lc = lifecycle[i];
			Runnable worker = new MyRunnable(lc, superJsonResponse, request, response, searchDomain, searchApp);
			executor.execute(worker);
		}
		executor.shutdown();
		// Wait until all threads are finish
		while (!executor.isTerminated()) {

		}

		if (superJsonResponse != null) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String outputJson = gson.toJson(superJsonResponse);
			out.println(outputJson);
			//System.out.println(outputJson);
		}
		out.flush();
		out.close();

		long endTime = System.currentTimeMillis();
		System.out.println("END TIME :" + endTime);
		long elapsedTime = endTime - startTime;
		System.out.println("******** TOTAL EXECUTION TIME :" + elapsedTime);
		System.out.println("\nFinished all threads");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

class MyRunnable implements Runnable {
	String folderLoc = "/opt/brms/shared/scripts/";
	Collection<File> fileList = new ArrayList<File>();
	Iterator<File> fileListIterator = null;
	String searchDomain = null;
	String searchApp = null;
	private final String lc;
	List<Map<String, Object>> superJsonResponse;
	HttpServletRequest req;
	HttpServletResponse res;

	MyRunnable(String lc, List<Map<String, Object>> superJsonResponse, HttpServletRequest request,
			HttpServletResponse response, String findDomain, String findApp) {
		this.lc = lc;
		this.superJsonResponse = superJsonResponse;
		this.req = request;
		this.res = response;
		this.searchApp = findApp;
		this.searchDomain = findDomain;
	}

	@Override
	public void run() {
		System.out.println(" ** THREAD RUN STARTED for LIFECYCLE :"+lc);

		try {

			final String fileFilterString = "brms_vm_cfg_*" + lc + ".txt";
			fileList = FileUtils.listFiles(new File(folderLoc), new WildcardFileFilter(fileFilterString), null);
			File file;

			JsonResponse jsonResponse = new JsonResponse();
			List<String> hostList = null;
			Map<String, Object> appMap = new HashMap<String, Object>();
			Map<String, Object> tempMap = new HashMap<String, Object>();
			Map<String, Object> domainMap = new HashMap<String, Object>();
			Map<String, Object> lifecycleMap = new HashMap<String, Object>();
			Map<String, Object> finalJsonMap = new HashMap<String, Object>();
			Map<String, Object> hostMap = null;

			fileListIterator = fileList.iterator();

			while (fileListIterator.hasNext()) {
				file = (File) fileListIterator.next();
				/*System.out.println("-----------------------------------------------");
				System.out.println("Read config file" + file.getName());
				System.out.println("-----------------------------------------------");
*/
				if (file.exists()) {
					List<String> lines = FileUtils.readLines(file);
					for (String line : lines) {
						if ((!line.contains("CONTEXT")) || (!line.contains(""))) {
							if (!line.contains("#")) {
								String[] tokens = line.split(",");
								String[] domainPath = tokens[9].split("/");

								String allOutput = tokens[10] + "|" + tokens[0] + "|" + tokens[1] + "|" + tokens[2]
										+ "|" + tokens[3] + "|" + tokens[4] + "|" + tokens[5] + "|" + tokens[6] + "|"
										+ tokens[7] + "|" + tokens[8] + "|" + tokens[9];
								/*System.out.println("All input line:(lc,host,)" + allOutput);*/

								if ((domainPath[1].equals(searchDomain)) && ((domainPath.length >= 2))
										&& (searchApp == null)) {
									finalJsonMap = createJson(req, res, jsonResponse, domainPath, hostList, tokens,
											hostMap, domainMap, lifecycleMap, appMap, tempMap);
								} else if ((domainPath[1].equals(searchDomain)) && (domainPath.length == 3)
										&& (domainPath[2].equals(searchApp))) {
									finalJsonMap = createJson(req, res, jsonResponse, domainPath, hostList, tokens,
											hostMap, domainMap, lifecycleMap, appMap, tempMap);
								} else if ((searchDomain == null) && (searchApp == null)) {
									finalJsonMap = createJson(req, res, jsonResponse, domainPath, hostList, tokens,
											hostMap, domainMap, lifecycleMap, appMap, tempMap);
								}
							}
						}
					}
				} else {
					System.out.println("No matching file exists to read...!");
				}
			}
			if (!finalJsonMap.isEmpty()) {
				superJsonResponse.add(finalJsonMap);
			}

		} catch (Exception e) {

		}
		System.out.println(" ** THREAD RUN ENDED for LIFECYCLE :"+lc);
	}

	private Map<String, Object> createJson(HttpServletRequest req, HttpServletResponse res, JsonResponse jsonResponse,
			String[] domainPath, List<String> hostList, String[] tokens, Map<String, Object> hostMap,
			Map<String, Object> domainMap, Map<String, Object> lifecycleMap, Map<String, Object> appMap,
			Map<String, Object> tempMap) {
		// TODO Auto-generated method stub
		if (!(null == jsonResponse.getDomain()) && jsonResponse.getDomain().containsKey(domainPath[1])) {
			if (domainPath.length == 3) {
				@SuppressWarnings("unchecked")
				Map<String, Object> domainLevelApps = (Map<String, Object>) jsonResponse.getDomain().get(domainPath[1]);
				@SuppressWarnings("unchecked")
				Map<String, Object> apps = (Map<String, Object>) domainLevelApps.get("APPS");
				if (!domainLevelApps.containsKey("APPS")) {
					hostList = new ArrayList<String>();
					hostList.add(tokens[0] + ":" + tokens[3]);
					hostMap = new HashMap<String, Object>();
					hostMap.put("HOSTS", hostList);
					String emanResponse = emanStatusResponse(res, req, tokens[10], domainPath[1], domainPath[2]);
					hostMap.put("STATUS", emanResponse);
					hostMap.put("DateTime", new Date(System.currentTimeMillis()));
					Map<String, Object> newApp = new HashMap<String, Object>();
					newApp.put(domainPath[2], hostMap);
					domainLevelApps.put("APPS", newApp);
					jsonResponse.setApp(domainLevelApps);
					domainMap.put(domainPath[1], domainLevelApps);
					jsonResponse.setDomain(domainMap);
				} else if (!apps.containsKey(domainPath[2])) {
					hostList = new ArrayList<>();
					hostList.add(tokens[0] + ":" + tokens[3]);
					hostMap = new HashMap<String, Object>();
					hostMap.put("HOSTS", hostList);
					String emanResponse = emanStatusResponse(res, req, tokens[10], domainPath[1], domainPath[2]);
					hostMap.put("STATUS", emanResponse);
					hostMap.put("DateTime", new Date(System.currentTimeMillis()));
					@SuppressWarnings("unchecked")
					Map<String, Object> existingAppMap = (Map<String, Object>) domainLevelApps.get("APPS");
					existingAppMap.put(domainPath[2], hostMap);
					domainLevelApps.put("APPS", existingAppMap);
					jsonResponse.setApp(domainLevelApps);
					domainMap.put(domainPath[1], domainLevelApps);
					jsonResponse.setDomain(domainMap);
				} else if (apps.containsKey(domainPath[2])) {
					@SuppressWarnings("unchecked")
					Map<String, Object> existingAppMap = (Map<String, Object>) domainLevelApps.get("APPS");
					@SuppressWarnings("unchecked")
					Map<String, Object> value = (Map<String, Object>) existingAppMap.get(domainPath[2]);
					@SuppressWarnings("unchecked")
					List<String> existingHosts = (List<String>) value.get("HOSTS");
					existingHosts.add(tokens[0] + ":" + tokens[3]);
					hostMap = new HashMap<String, Object>();
					hostMap.put("HOSTS", existingHosts);
					String emanResponse = emanStatusResponse(res, req, tokens[10], domainPath[1], domainPath[2]);
					hostMap.put("STATUS", emanResponse);
					hostMap.put("DateTime", new Date(System.currentTimeMillis()));
					existingAppMap.put(domainPath[2], hostMap);
					domainLevelApps.put("APPS", existingAppMap);
					jsonResponse.setApp(domainLevelApps);
					domainMap.put(domainPath[1], domainLevelApps);
					jsonResponse.setDomain(domainMap);
				}
			} else {
				@SuppressWarnings("unchecked")
				Map<String, Object> dHosts = (Map<String, Object>) jsonResponse.getDomain().get(domainPath[1]);
				if (!dHosts.containsKey("HOSTS")) {
					hostList = new ArrayList<>();
					hostList.add(tokens[0] + ":" + tokens[3]);
					@SuppressWarnings("unchecked")
					Map<String, Object> map = (Map<String, Object>) jsonResponse.getDomain().get(domainPath[1]);
					map.put("HOSTS", hostList);
					String emanResponse = emanStatusResponse(res, req, tokens[10], domainPath[1], null);
					map.put("STATUS", emanResponse);
					map.put("DateTime", new Date(System.currentTimeMillis()));
					jsonResponse.setHost(map);
					domainMap.put(domainPath[1], map);
					jsonResponse.setDomain(domainMap);
				} else {
					@SuppressWarnings("unchecked")
					Map<String, Object> domMap = (Map<String, Object>) jsonResponse.getDomain().get(domainPath[1]);
					@SuppressWarnings("unchecked")
					List<String> existingHosts = (List<String>) domMap.get("HOSTS");
					existingHosts.add(tokens[0] + ":" + tokens[3]);
					hostMap = new HashMap<String, Object>();
					hostMap.put("HOSTS", existingHosts);
					String emanResponse = emanStatusResponse(res, req, tokens[10], domainPath[1], null);
					hostMap.put("STATUS", emanResponse);
					hostMap.put("DateTime", new Date(System.currentTimeMillis()));
					hostMap.putAll(domMap);
					jsonResponse.setHost(hostMap);
					domainMap.put(domainPath[1], hostMap);
					jsonResponse.setDomain(domainMap);
				}
			}
			lifecycleMap.put(tokens[10], domainMap);
			jsonResponse.setLifecycle(lifecycleMap);
		} else {
			if (!(null == jsonResponse.getLifecycle()) && jsonResponse.getLifecycle().containsKey(tokens[10])) {
				@SuppressWarnings("unchecked")
				Map<String, Object> lifeCycleMap = (Map<String, Object>) jsonResponse.getLifecycle().get(tokens[10]);
				if (domainPath.length == 3) {
					hostList = new ArrayList<String>();
					hostList.add(tokens[0] + ":" + tokens[3]);
					hostMap = new HashMap<String, Object>();
					hostMap.put("HOSTS", hostList);
					String emanResponse = emanStatusResponse(res, req, tokens[10], domainPath[1], domainPath[2]);
					hostMap.put("STATUS", emanResponse);
					hostMap.put("DateTime", new Date(System.currentTimeMillis()));
					appMap.put(domainPath[2], hostMap);
					jsonResponse.setApp(appMap);
					tempMap.put("APPS", appMap);
					lifeCycleMap.put(domainPath[1], tempMap);
					jsonResponse.setDomain(lifeCycleMap);
				} else {
					hostList = new ArrayList<>();
					hostList.add(tokens[0] + ":" + tokens[3]);
					hostMap = new HashMap<String, Object>();
					hostMap.put("HOSTS", hostList);
					String emanResponse = emanStatusResponse(res, req, tokens[10], domainPath[1], null);
					hostMap.put("STATUS", emanResponse);
					hostMap.put("DateTime", new Date(System.currentTimeMillis()));
					lifeCycleMap.put(domainPath[1], hostMap);
					jsonResponse.setDomain(lifeCycleMap);
				}
				lifecycleMap.put(tokens[10], lifeCycleMap);
				jsonResponse.setLifecycle(lifecycleMap);
			} else {
				if (domainPath.length == 3) {
					hostList = new ArrayList<String>();
					hostList.add(tokens[0] + ":" + tokens[3]);
					hostMap = new HashMap<String, Object>();
					hostMap.put("HOSTS", hostList);
					String emanResponse = emanStatusResponse(res, req, tokens[10], domainPath[1], domainPath[2]);
					hostMap.put("STATUS", emanResponse);
					hostMap.put("DateTime", new Date(System.currentTimeMillis()));
					appMap.put(domainPath[2], hostMap);
					jsonResponse.setApp(appMap);
					tempMap.put("APPS", appMap);
					domainMap.put(domainPath[1], tempMap);
					jsonResponse.setDomain(domainMap);
				} else {
					hostList = new ArrayList<>();
					hostList.add(tokens[0] + ":" + tokens[3]);
					hostMap = new HashMap<String, Object>();
					hostMap.put("HOSTS", hostList);
					String emanResponse = emanStatusResponse(res, req, tokens[10], domainPath[1], null);
					hostMap.put("STATUS", emanResponse);
					hostMap.put("DateTime", new Date(System.currentTimeMillis()));
					domainMap.put(domainPath[1], hostMap);
					jsonResponse.setDomain(domainMap);
				}
				lifecycleMap.put(tokens[10], domainMap);
				jsonResponse.setLifecycle(lifecycleMap);
			}
		}
		return lifecycleMap;
	}

	private String emanStatusResponse(HttpServletResponse res, HttpServletRequest req, String lifecycle, String domain,
			String dApp) {
		// TODO Auto-generated method stub
		String webPage = null;
		String resMessage = null;
		if (lifecycle.equals("dev")) {
			if (dApp == null) {
				webPage = "https://ibpm-dev.cisco.com/" + domain + "/brmsadmin/eman";
			} else {
				webPage = "https://ibpm-dev.cisco.com/" + domain + "/" + dApp + "/brmsadmin/eman";
			}
		} else if (lifecycle.equals("stage")) {
			if (dApp == null) {
				webPage = "https://ibpm-stage.cisco.com/" + domain + "/brmsadmin/eman";
			} else {
				webPage = "https://ibpm-stage.cisco.com/" + domain + "/" + dApp + "/brmsadmin/eman";
			}
		} else if (lifecycle.equals("lt")) {
			if (dApp == null) {
				webPage = "https://ibpm-lt.cisco.com/" + domain + "/brmsadmin/eman";
			} else {
				webPage = "https://ibpm-lt.cisco.com/" + domain + "/" + dApp + "/brmsadmin/eman";
			}
		} else if (lifecycle.equals("poc")) {
			if (dApp == null) {
				webPage = "https://ibpm-poc.cisco.com/" + domain + "/brmsadmin/eman";
			} else {
				webPage = "https://ibpm-poc.cisco.com/" + domain + "/" + dApp + "/brmsadmin/eman";
			}
		} else if (lifecycle.equals("prod")) {
			if (dApp == null) {
				webPage = "https://ibpm.cisco.com/" + domain + "/brmsadmin/eman";
			} else {
				webPage = "https://ibpm.cisco.com/" + domain + "/" + dApp + "/brmsadmin/eman";
			}
		}
		// webPage="https://ibpm.cisco.com/gssc/brmsadmin/eman";

		try {
			/*System.out.println("INPUT EMAN URL TO CHECK::" + webPage);*/
			URL url = new URL(webPage);

			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			// httpCon.setRequestProperty("Authorization", "Basic " +
			// authStringEnc);
			int statusCode = httpCon.getResponseCode();
			InputStream is = httpCon.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			// String resContent = http.getContentEncoding().toString();

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			String emanResponse = sb.toString();
			/*System.out.println(" ******* URL RESPONSE IS : *****");
			System.out.println("URL : " + webPage);*/
			System.out.println("Response STATUS CODE is :" + statusCode);

			System.out.println(emanResponse);
			if (emanResponse.contains("EMAN OK")) {
				/*System.out.println(emanResponse);
				System.out.println("SUCCESS EMAN Response" + emanResponse);*/
				resMessage = "Up";
			} else {
				/*System.out.println("Failure EMAN Response" + emanResponse);*/
				resMessage = "Down";
			}
			/*System.out.println(" *******************************");*/

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (resMessage == null) {
				resMessage = "Down";
			}
		}
		return resMessage;
	}
}