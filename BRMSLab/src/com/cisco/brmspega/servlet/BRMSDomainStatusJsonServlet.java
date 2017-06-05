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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.jasper.tagplugins.jstl.core.ForEach;

import com.cisco.brmspega.bundles.BrmsPropertyLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sample.utility.BrmsJsonUtil;

/**
 * Servlet implementation class NewDomainJsonServlet
 */
@WebServlet("/BRMSDomainStatusJsonServlet")
public class BRMSDomainStatusJsonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String configFileFolderLoc =  "/opt/brms/shared/scripts/";//BrmsPropertyLoader.getInstance().getProperty("server_tools_cofig_base");

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BRMSDomainStatusJsonServlet() {
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
		//out.println(reqURI);
		String searchDomain = null;
		String searchApp = null;
		String searchLifecycle =  null;
		//String sampleURI = "/nprd1/brmsadmin/status";
		//String[] uriSampleParams = sampleURI.split("\\/");
		String[] uriParams = reqURI.split("\\/");
		String contextPath = request.getContextPath();
		String domainFromContextPath = contextPath.split("\\/")[2];
		System.out.println("Domain from context :" + domainFromContextPath);
		out.println(uriParams.length);
		//out.println(uriSampleParams.length);
		response.setContentType("application/json");
		if (contextPath.contains("brmsadmin")) {
			
			if (uriParams.length == 6) {
				searchDomain = uriParams[5];
				searchLifecycle = uriParams[4];
			} else if (uriParams.length == 7) {
				searchDomain = uriParams[5];
				searchApp = uriParams[6];
				searchLifecycle = uriParams[4];

			} else if (uriParams.length == 5) {
				
				searchDomain = null;
				searchApp = null;
				searchLifecycle = uriParams[4];
				// searchDomain = "cvc";
				// searchApp = "score";
			} 
		} else {
			if (uriParams.length == 3) {
				searchDomain = domainFromContextPath;
				searchApp = null;
			}
		}
		
		
		
		/*if (uriParams.length == 4) {
			searchDomain = uriParams[3];
		} else if (uriParams.length == 5) {
			searchDomain = uriParams[3];
			searchApp = uriParams[4];
		} else if (uriParams.length == 3) {
			searchDomain = null;
			searchApp = null;
			//searchDomain = "cvc";
			//searchApp = "score";
		} */
		
		//String folderLoc = "/opt/brms/shared/scripts/";
		Collection<File> fileList = new ArrayList<File>();
		Iterator<File> fileListIterator = null;
		List<Map<String, Object>> superJsonResponse = new ArrayList<Map<String, Object>>();
		
		long startEmanPingTime = System.currentTimeMillis();
		
		//System.out.println("********** START OF EMAN PING TIME :"+startEmanPingTime);
		//ConcurrentHashMap<String, String> getAllEMANStatus = collectEMANStatus(searchDomain,searchApp,uriParams);
	
		ConcurrentHashMap<String, String> getAllEMANStatus = EmanPingJob.getInstance().getEMANPingMap();
		
		long endEmanPingTime = System.currentTimeMillis();
		//System.out.println("********** END OF EMAN PING TIME :"+endEmanPingTime);
		//long elapsedEmanPingTime = (endEmanPingTime - startEmanPingTime);
		//System.out.println("***** TOTAL EMAN PING EXECUTION TIME :"+ elapsedEmanPingTime);
		
		long startTime = System.currentTimeMillis();
		//System.out.println("********** START TIME JSON Creation:"+startTime);
		//System.out.println("********** START TIME JSON Creation:"+searchLifecycle);
		//String [] lcList =  {"DEV","PRD","LT","STG","POC"};
		//String [] lcList =  {"PRD","STG","DEV","LT","POC"};
		//String [] lcList =  {"PRD"};
		
		
		
		/*HashMap<String, String> lcMap = new HashMap<String,String>();
		lcMap.put("PROD", "PRD");
		lcMap.put("STAGE", "STG");
		lcMap.put("DEVLOPEMNT", "DEV");
		lcMap.put("LT", "LT");
		lcMap.put("POC", "POC");
		
 		String [] lcList;
		if (searchLifecycle.equalsIgnoreCase("all")) {
			lcList = new String[] {"PRD","STG","DEV","LT","POC"};
		} else {
			System.out.println("********** START TIME JSON Creation:"+searchLifecycle);
			lcList = new String[] {searchLifecycle.toUpperCase()};
			System.out.println("********** ARRA ELEMNET:"+lcList[0]);
		}*/
	
	/*	for (String lc : lcList) {
			String env = lc;
			final String fileFilterString = "brms_vm_cfg_*" + env + ".txt";
			//final String fileFilterString = "brms_vm_cfg_PRD1_PRD.txt";
			fileList = FileUtils.listFiles(new File(configFileFolderLoc), new WildcardFileFilter(fileFilterString), null);
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
				System.out.println("-----------------------------------------------");
				System.out.println("Read config file"+file.getName());
				System.out.println("-----------------------------------------------");

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
								// System.out.println("All input
								// line:(lc,host,)"+allOutput);
								System.out.println("** LINE LENGTH :"+tokens.length);
								if (tokens.length >= 16) {
									String[] domainAppPath = new String[3];
									String[] appList = tokens[15].split("\\:");

									for (int j = 0; j < appList.length; j++) {
										// System.out.println("Domain/App::
										// "+tokens[9]+"/"+appList[j]);
										if (domainPath.length == 3) {
											domainAppPath[1] = domainPath[1].replace("/", "");
											domainAppPath[2] = appList[j];
										} else {
											domainAppPath[1] = tokens[9].replace("/", "");
											domainAppPath[2] = appList[j];
										}
									}

									if ((domainPath[1].equals(searchDomain)) && (searchApp == null)) {

										for (int j = 0; j < appList.length; j++) {
											// System.out.println("Domain/App::
											// "+tokens[9]+"/"+appList[j]);
											if (domainPath.length == 3) {
												domainAppPath[1] = domainPath[1].replace("/", "");
												domainAppPath[2] = appList[j];
											} else {
												domainAppPath[1] = tokens[9].replace("/", "");
												domainAppPath[2] = appList[j];
											}

											finalJsonMap = createJson(request, response, jsonResponse, domainAppPath,
													hostList, tokens, hostMap, domainMap, lifecycleMap, appMap, tempMap,
													getAllEMANStatus);
										}
									} else if ((domainPath[1].equals(searchDomain))
											&& (tokens[15].contains(searchApp))) {
										domainAppPath[1] = tokens[9].replace("/", "");
										domainAppPath[2] = searchApp;
										finalJsonMap = createJson(request, response, jsonResponse, domainAppPath,
												hostList, tokens, hostMap, domainMap, lifecycleMap, appMap, tempMap,
												getAllEMANStatus);
									} else if ((searchDomain == null) && (searchApp == null)
											&& (uriParams.length <= 5)) {
										for (int j = 0; j < appList.length; j++) {
											if (domainPath.length == 3) {
												domainAppPath[1] = domainPath[1].replace("/", "");
												domainAppPath[2] = appList[j];
											} else {
												domainAppPath[1] = tokens[9].replace("/", "");
												domainAppPath[2] = appList[j];
											}

											finalJsonMap = createJson(request, response, jsonResponse, domainAppPath,
													hostList, tokens, hostMap, domainMap, lifecycleMap, appMap, tempMap,
													getAllEMANStatus);
										}
									}

								} else {
									if ((domainPath[1].equals(searchDomain)) && ((domainPath.length >= 2))
											&& (searchApp == null)) {
										finalJsonMap = createJson(request, response, jsonResponse, domainPath, hostList,
												tokens, hostMap, domainMap, lifecycleMap, appMap, tempMap,
												getAllEMANStatus);
									} else if ((domainPath[1].equals(searchDomain)) && (domainPath.length == 3)
											&& (domainPath[2].equals(searchApp))) {
										finalJsonMap = createJson(request, response, jsonResponse, domainPath, hostList,
												tokens, hostMap, domainMap, lifecycleMap, appMap, tempMap,
												getAllEMANStatus);
									} else if ((searchDomain == null) && (searchApp == null)
											&& (uriParams.length <= 5)) {
										finalJsonMap = createJson(request, response, jsonResponse, domainPath, hostList,
												tokens, hostMap, domainMap, lifecycleMap, appMap, tempMap,
												getAllEMANStatus);
									}
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
		}*/
		/*if (superJsonResponse != null) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String outputJson = gson.toJson(superJsonResponse);
			//System.out.println(json);
			//String outputJson = new Gson().toJson(superJsonResponse);
			//System.out.println(outputJson);
			if(outputJson.equals("[]")) {
				out.println("No matching data found");
			} else {
				out.println(outputJson);
			}
		}*/ 
		//BrmsJsonUtil jsonUtil = new BrmsJsonUtil();
		System.out.println(searchLifecycle+":"+searchDomain +":"+ searchApp);
		String jsonOutput =  BrmsJsonUtil.getJson(searchLifecycle, searchDomain, searchApp);
		out.println(jsonOutput);
		
		out.flush();
		out.close();
		
		long endTime = System.currentTimeMillis();
		System.out.println("END TIME :"+endTime);
		long elapsedTime = endTime - startTime;
		System.out.println("***** TOTAL EXECUTION TIME :"+ elapsedTime);
		
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
	
	/*protected  Map<String, Object> createJson(HttpServletRequest req, HttpServletResponse res, JsonResponse jsonResponse, String[] domainPath, List<String> hostList,
	        String[] tokens, Map<String, Object> hostMap, Map<String, Object> domainMap,
	        Map<String, Object> lifecycleMap, Map<String, Object> appMap, Map<String, Object> tempMap, ConcurrentHashMap<String, String> getAllEMANStatus) {
		
		if ( !(null == jsonResponse.getDomain()) && jsonResponse.getDomain().containsKey(domainPath[1]) ) {
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
					String emanResponse = getAllEMANStatus.get(tokens[10]+"_"+domainPath[1]+"_"+domainPath[2]);// "Up";//emanStatusResponse(res, req, tokens[10], domainPath[1], domainPath[2]);
					hostMap.put("STATUS", emanResponse);
					hostMap.put("DateTime", new Date(System.currentTimeMillis()));
					String proxy_url = createDomainProxyURL(tokens[10], domainPath[1], domainPath[2]);
					System.out.println("PROXY_URL::"+proxy_url);
					hostMap.put("ProxyURL",proxy_url);
					System.out.println("DOMAIN_APP_APP KEY::"+tokens[10]+"_"+domainPath[1]+"_"+domainPath[2]);
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
					String emanResponse = getAllEMANStatus.get(tokens[10]+"_"+domainPath[1]+"_"+domainPath[2]);//"Up";//emanStatusResponse(res, req, tokens[10], domainPath[1], domainPath[2]);
					hostMap.put("STATUS", emanResponse);
					hostMap.put("DateTime", new Date(System.currentTimeMillis()));
					String proxy_url = createDomainProxyURL(tokens[10], domainPath[1], domainPath[2]);
					System.out.println("PROXY_URL::"+proxy_url);
					hostMap.put("ProxyURL",proxy_url);
					System.out.println("DOMAIN_APP_APP KEY::"+tokens[10]+"_"+domainPath[1]+"_"+domainPath[2]);
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
					String emanResponse = getAllEMANStatus.get(tokens[10]+"_"+domainPath[1]+"_"+domainPath[2]);//"Up";//emanStatusResponse(res, req, tokens[10], domainPath[1], domainPath[2]);
					hostMap.put("STATUS", emanResponse);
					hostMap.put("DateTime", new Date(System.currentTimeMillis()));
					String proxy_url = createDomainProxyURL(tokens[10], domainPath[1], domainPath[2]);
					System.out.println("PROXY_URL::"+proxy_url);
					hostMap.put("ProxyURL",proxy_url);
					System.out.println("DOMAIN_APP_APP KEY::"+tokens[10]+"_"+domainPath[1]+"_"+domainPath[2]);
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
					String emanResponse = getAllEMANStatus.get(tokens[10]+"_"+domainPath[1]);//"Up";//emanStatusResponse(res, req, tokens[10], domainPath[1], null);
					map.put("STATUS", emanResponse);
					map.put("DateTime", new Date(System.currentTimeMillis()));
					String proxy_url = createDomainProxyURL(tokens[10], domainPath[1], null);
					System.out.println("PROXY_URL::"+proxy_url);
					map.put("ProxyURL",proxy_url);
					System.out.println("DOMAIN_APP KEY::"+tokens[10]+"_"+domainPath[1]);
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
					String emanResponse = getAllEMANStatus.get(tokens[10]+"_"+domainPath[1]);//"Up";//emanStatusResponse(res, req, tokens[10], domainPath[1], null);
					hostMap.put("STATUS", emanResponse);
					hostMap.put("DateTime", new Date(System.currentTimeMillis()));
					String proxy_url = createDomainProxyURL(tokens[10], domainPath[1], null);
					System.out.println("PROXY_URL::"+proxy_url);
					hostMap.put("ProxyURL",proxy_url);
					System.out.println("DOMAIN_APP KEY::"+tokens[10]+"_"+domainPath[1]);
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
					String emanResponse = getAllEMANStatus.get(tokens[10]+"_"+domainPath[1]+"_"+domainPath[2]);//"Up";//emanStatusResponse(res, req, tokens[10], domainPath[1], domainPath[2]);
					hostMap.put("STATUS", emanResponse);
					hostMap.put("DateTime", new Date(System.currentTimeMillis()));
					String proxy_url = createDomainProxyURL(tokens[10], domainPath[1], domainPath[2]);
					System.out.println("PROXY_URL::"+proxy_url);
					hostMap.put("ProxyURL",proxy_url);
					System.out.println("DOMAIN_APP_APP KEY::"+tokens[10]+"_"+domainPath[1]+"_"+domainPath[2]);
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
					String emanResponse = getAllEMANStatus.get(tokens[10]+"_"+domainPath[1]);//"Up";//emanStatusResponse(res, req, tokens[10], domainPath[1], null);
					hostMap.put("STATUS", emanResponse);
					hostMap.put("DateTime", new Date(System.currentTimeMillis()));
					String proxy_url = createDomainProxyURL(tokens[10], domainPath[1], null);
					System.out.println("PROXY_URL::"+proxy_url);
					hostMap.put("ProxyURL",proxy_url);
					System.out.println("DOMAIN_APP KEY::"+tokens[10]+"_"+domainPath[1]);
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
					String emanResponse = getAllEMANStatus.get(tokens[10]+"_"+domainPath[1]+"_"+domainPath[2]);//"Up";//emanStatusResponse(res, req, tokens[10], domainPath[1], domainPath[2]);
					hostMap.put("STATUS", emanResponse);
					hostMap.put("DateTime", new Date(System.currentTimeMillis()));
					String proxy_url = createDomainProxyURL(tokens[10], domainPath[1], domainPath[2]);
					System.out.println("PROXY_URL::"+proxy_url);
					hostMap.put("ProxyURL",proxy_url);
					System.out.println("DOMAIN_APP_APP KEY::"+tokens[10]+"_"+domainPath[1]+"_"+domainPath[2]);
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
					String emanResponse = getAllEMANStatus.get(tokens[10]+"_"+domainPath[1]);//"Up";//emanStatusResponse(res, req, tokens[10], domainPath[1], null);
					hostMap.put("STATUS", emanResponse);
					hostMap.put("DateTime", new Date(System.currentTimeMillis()));
					String proxy_url = createDomainProxyURL(tokens[10], domainPath[1], null);
					System.out.println("PROXY_URL::"+proxy_url);
					hostMap.put("ProxyURL",proxy_url);
					System.out.println("DOMAIN_APP KEY::"+tokens[10]+"_"+domainPath[1]);
					domainMap.put(domainPath[1], hostMap);
					jsonResponse.setDomain(domainMap);
				}
				lifecycleMap.put(tokens[10], domainMap);
				jsonResponse.setLifecycle(lifecycleMap);
			}
		}
		return lifecycleMap;
	}*/
	
	/*protected ConcurrentHashMap<String, String> collectEMANStatus(String searchDomain, String searchApp, String[] uriParams) throws IOException {

		final int MYTHREADS = 10;
		ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);
		
		ConcurrentHashMap<String, String> domainStatusMap = new ConcurrentHashMap<String, String>();

		String folderLoc = "/opt/brms/shared/scripts/";
		Collection<File> fileList = new ArrayList<File>();
		Iterator<File> fileListIterator = null;
		//String[] lifecycle = { "DEV","PRD","LT","STG","POC" };
		String[] lifecycle = { "LT" };

		for (int i = 0; i < lifecycle.length; i++) {

			String lc = lifecycle[i];

			final String fileFilterString = "brms_vm_cfg_*" + lc + ".txt";
			fileList = FileUtils.listFiles(new File(folderLoc), new WildcardFileFilter(fileFilterString), null);
			File file;
			fileListIterator = fileList.iterator();
			while (fileListIterator.hasNext()) {
				file = (File) fileListIterator.next();
				System.out.println("-----------------------------------------------");
				System.out.println("Read config file" + file.getName());
				System.out.println("-----------------------------------------------");

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
								// System.out.println("All input
								// line:(lc,host,)" + allOutput);
								String lcDomainApp = null;
								if (domainPath.length == 3) {
									lcDomainApp = tokens[10] + "_" + domainPath[1] + "_" + domainPath[2];
								} else {
									lcDomainApp = tokens[10] + "_" + domainPath[1];
								}
								
								if ( (domainPath[1].equals(searchDomain)) && ((domainPath.length >= 2)) && (searchApp == null) ) {
									if (!domainStatusMap.containsKey(lcDomainApp)) {
										Runnable worker = new MyRunnable(tokens[10], tokens[0], tokens[3], domainPath,
												domainStatusMap);
										executor.execute(worker);
									}
								} else if ( ( domainPath[1].equals(searchDomain)) && (domainPath.length == 3) && (domainPath[2].equals(searchApp)) ) {
									if(!domainStatusMap.containsKey(lcDomainApp)) {
										Runnable worker = new MyRunnable(tokens[10], tokens[0], tokens[3], domainPath,
											domainStatusMap);
										executor.execute(worker);
									}
								} else if ( (searchDomain == null) && (searchApp == null) && (uriParams.length <= 5) ){
									if (!domainStatusMap.containsKey(lcDomainApp)) {
										Runnable worker = new MyRunnable(tokens[10], tokens[0], tokens[3], domainPath,
											domainStatusMap);
										executor.execute(worker);
									}
								}
								
							}
						}
					}
				} else {
					System.out.println("No matching file exists to read...!");
				}

			}
		}
		executor.shutdown();
		// Wait until all threads are finish
		while (!executor.isTerminated()) {

		}
		System.out.println("\nFinished all threads");
		// System.out.println(domainStatusMap);
		// System.out.println("");

		return domainStatusMap;
	}

	class MyRunnable implements Runnable {
		private String lifecycle;
		private String host;
		private String port;
		private String domain;
		private String app;
		private String key;

		ConcurrentHashMap<String, String> domainStatusMap;

		MyRunnable(String lifecycle, String hostname, String port, String[] domainPath,
				ConcurrentHashMap<String, String> domainStatusMap) {
			this.lifecycle = lifecycle;
			this.host = hostname;
			this.port = port;
			this.domainStatusMap = domainStatusMap;
			if (domainPath.length == 3) {
				this.domain = domainPath[1];
				this.app = domainPath[2];
			} else {
				this.domain = domainPath[1];
				this.app = null;
			}
		}

		@Override
		public void run() {
			String key;

			if (app == null) {
				key = lifecycle + "_" + domain;
			} else {
				key = lifecycle + "_" + domain + "_" + app;
			}
			String emanResult = emanStatusResponse(lifecycle, domain, app);
			// System.out.println("THREAD NAME" +
			// Thread.currentThread().getName());
			// System.out.println(" EMAN RESULT :" + lifecycle + "|" + domain +
			// "|" + app + "|" + emanResult);
			domainStatusMap.put(key, emanResult);
		}
	}
		
	protected String emanStatusResponse(String lifecycle,String domain,String dApp) {
		String webPage = null;
		String  resMessage = null;
		
		webPage = createDomainProxyURL(lifecycle,domain,dApp);
		
		if (lifecycle.equals("dev")){
			if(dApp == null) {
				webPage = "https://ibpm-dev.cisco.com/"+domain+"/brmsadmin/eman";
			} else {
				webPage = "https://ibpm-dev.cisco.com/"+domain+"/"+dApp+"/brmsadmin/eman";
			}
		} else if(lifecycle.equals("stage")) {
			if(dApp == null) {
				webPage = "https://ibpm-stage.cisco.com/"+domain+"/brmsadmin/eman";
			} else {
				webPage = "https://ibpm-stage.cisco.com/"+domain+"/"+dApp+"/brmsadmin/eman";
			}
		} else if(lifecycle.equals("lt")) {
			if(dApp == null) {
				webPage = "https://ibpm-lt.cisco.com/"+domain+"/brmsadmin/eman";
			} else {
				webPage = "https://ibpm-lt.cisco.com/"+domain+"/"+dApp+"/brmsadmin/eman";
			}
		} else if (lifecycle.equals("poc")) {
			if(dApp == null) {
				webPage = "https://ibpm-poc.cisco.com/"+domain+"/brmsadmin/eman";
			} else {
				webPage = "https://ibpm-poc.cisco.com/"+domain+"/"+dApp+"/brmsadmin/eman";
			}
		} else if (lifecycle.equals("prod")) {
			if(dApp == null) {
				webPage = "https://ibpm.cisco.com/"+domain+"/brmsadmin/eman";
			} else {
				webPage = "https://ibpm.cisco.com/"+domain+"/"+dApp+"/brmsadmin/eman";
			}
		}
		
		
		try {
			System.out.println("INPUT EMAN URL TO CHECK::"+webPage);
			URL url = new URL(webPage);

			HttpURLConnection httpCon = (HttpURLConnection)url.openConnection();
			//httpCon.setRequestProperty("Authorization", "Basic " + authStringEnc);
			int statusCode = httpCon.getResponseCode();
			InputStream is = httpCon.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			//String resContent  =  http.getContentEncoding().toString();
			
			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			String emanResponse = sb.toString();
			System.out.println(" ******* URL RESPONSE IS : *****");
			System.out.println("URL : "+webPage);
			System.out.println("Response STATUS CODE is :"+statusCode);
			
			System.out.println(emanResponse);
			if (emanResponse.contains("EMAN OK")) {
				System.out.println(emanResponse);
				System.out.println("SUCCESS EMAN Response"+emanResponse);
				resMessage = "Up";
			} else {
				System.out.println("Failure EMAN Response"+emanResponse);
				resMessage = "Down";
			}
			//System.out.println(" *******************************");
			
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
	}*/
	
	/*protected String createDomainProxyURL (String lifecycle, String domain, String dApp){
		
		String proxyURL = null;
		
		if (lifecycle.equals("dev")){
			if(dApp == null) {
				proxyURL = "https://ibpm-dev.cisco.com/"+domain+"/brmsadmin/eman";
			} else {
				proxyURL = "https://ibpm-dev.cisco.com/"+domain+"/"+dApp+"/brmsadmin/eman";
			}
		} else if(lifecycle.equals("stage")) {
			if(dApp == null) {
				proxyURL = "https://ibpm-stage.cisco.com/"+domain+"/brmsadmin/eman";
			} else {
				proxyURL = "https://ibpm-stage.cisco.com/"+domain+"/"+dApp+"/brmsadmin/eman";
			}
		} else if(lifecycle.equals("lt")) {
			if(dApp == null) {
				proxyURL = "https://ibpm-lt.cisco.com/"+domain+"/brmsadmin/eman";
			} else {
				proxyURL = "https://ibpm-lt.cisco.com/"+domain+"/"+dApp+"/brmsadmin/eman";
			}
		} else if (lifecycle.equals("poc")) {
			if(dApp == null) {
				proxyURL = "https://ibpm-poc.cisco.com/"+domain+"/brmsadmin/eman";
			} else {
				proxyURL = "https://ibpm-poc.cisco.com/"+domain+"/"+dApp+"/brmsadmin/eman";
			}
		} else if (lifecycle.equals("prod")) {
			if(dApp == null) {
				proxyURL = "https://ibpm.cisco.com/"+domain+"/brmsadmin/eman";
			} else {
				proxyURL = "https://ibpm.cisco.com/"+domain+"/"+dApp+"/brmsadmin/eman";
			}
		}
		
		return proxyURL;
		
	}*/
}



