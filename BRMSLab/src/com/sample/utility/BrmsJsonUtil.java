package com.sample.utility;

import java.io.File;

import com.cisco.brmspega.util.ldap.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.cisco.brmspega.servlet.EmanPingJob;
import com.cisco.brmspega.servlet.JsonResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BrmsJsonUtil {
	private static final String configFileFolderLoc =  "/opt/brms/shared/scripts/";
	
	public static String getJson(String searchLifecycle,String searchDomain, String searchApp) throws IOException {
		Collection<File> fileList = new ArrayList<File>();
		Iterator<File> fileListIterator = null;
		List<Map<String, Object>> superJsonResponse = new ArrayList<Map<String, Object>>();
		ConcurrentHashMap<String, String> getAllEMANStatus = EmanPingJob.getInstance().getEMANPingMap();
		
		String [] lcList;
		if ( (searchLifecycle == null) || (searchLifecycle.equalsIgnoreCase("all"))) {
			lcList = new String[] {"PRD","STG","DEV","LT","POC"};
			//lcList = new String[] {"PRD"};
		} else {
			lcList = new String[] {searchLifecycle.toUpperCase()};
		}
	
		for (String lc : lcList) {
			String env = lc;
			String fileFilterString = "brms_vm_cfg_*" + env + ".txt";
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
				/*System.out.println("-----------------------------------------------");
				System.out.println("Read config file"+file.getName());
				System.out.println("-----------------------------------------------");*/

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
								//System.out.println("** LINE LENGTH :"+tokens.length);
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

											finalJsonMap = createJson(jsonResponse, domainAppPath,
													hostList, tokens, hostMap, domainMap, lifecycleMap, appMap, tempMap,
													getAllEMANStatus);
										}
										
									} else if ((domainPath[1].equals(searchDomain))
											&& (tokens[15].contains(searchApp))) {
										domainAppPath[1] = tokens[9].replace("/", "");
										domainAppPath[2] = searchApp;
										finalJsonMap = createJson(jsonResponse, domainAppPath,
												hostList, tokens, hostMap, domainMap, lifecycleMap, appMap, tempMap,
												getAllEMANStatus);
										
									} else if ((searchDomain == null) && (searchApp == null)) {
										for (int j = 0; j < appList.length; j++) {
											if (domainPath.length == 3) {
												domainAppPath[1] = domainPath[1].replace("/", "");
												domainAppPath[2] = appList[j];
											} else {
												domainAppPath[1] = tokens[9].replace("/", "");
												domainAppPath[2] = appList[j];
											}

											finalJsonMap = createJson(jsonResponse, domainAppPath,
													hostList, tokens, hostMap, domainMap, lifecycleMap, appMap, tempMap,
													getAllEMANStatus);
										}
									}

								} else {
									if ((domainPath[1].equals(searchDomain)) && ((domainPath.length >= 2))
											&& (searchApp == null)) {
										finalJsonMap = createJson(jsonResponse, domainPath, hostList,
												tokens, hostMap, domainMap, lifecycleMap, appMap, tempMap,
												getAllEMANStatus);
									} else if ((domainPath[1].equals(searchDomain)) && (domainPath.length == 3)
											&& (domainPath[2].equals(searchApp))) {
										finalJsonMap = createJson(jsonResponse, domainPath, hostList,
												tokens, hostMap, domainMap, lifecycleMap, appMap, tempMap,
												getAllEMANStatus);
									} else if ((searchDomain == null) && (searchApp == null)) {
										finalJsonMap = createJson(jsonResponse, domainPath, hostList,
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
		}
		
		String outputJson = "No matching data found";
		if (superJsonResponse != null) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			outputJson = gson.toJson(superJsonResponse);
		}

		return outputJson;
	} 

	public static  Map<String, Object> createJson(JsonResponse jsonResponse, String[] domainPath, List<String> hostList,
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
					//System.out.println("PROXY_URL::"+proxy_url);
					hostMap.put("ProxyURL",proxy_url);
					//System.out.println("DOMAIN_APP_APP KEY::"+tokens[10]+"_"+domainPath[1]+"_"+domainPath[2]);
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
					//System.out.println("PROXY_URL::"+proxy_url);
					hostMap.put("ProxyURL",proxy_url);
					//System.out.println("DOMAIN_APP_APP KEY::"+tokens[10]+"_"+domainPath[1]+"_"+domainPath[2]);
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
					//System.out.println("PROXY_URL::"+proxy_url);
					hostMap.put("ProxyURL",proxy_url);
					//System.out.println("DOMAIN_APP_APP KEY::"+tokens[10]+"_"+domainPath[1]+"_"+domainPath[2]);
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
					//System.out.println("PROXY_URL::"+proxy_url);
					map.put("ProxyURL",proxy_url);
					//System.out.println("DOMAIN_APP KEY::"+tokens[10]+"_"+domainPath[1]);
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
					//System.out.println("PROXY_URL::"+proxy_url);
					hostMap.put("ProxyURL",proxy_url);
					//System.out.println("DOMAIN_APP KEY::"+tokens[10]+"_"+domainPath[1]);
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
					//System.out.println("PROXY_URL::"+proxy_url);
					hostMap.put("ProxyURL",proxy_url);
					//System.out.println("DOMAIN_APP_APP KEY::"+tokens[10]+"_"+domainPath[1]+"_"+domainPath[2]);
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
					//System.out.println("PROXY_URL::"+proxy_url);
					hostMap.put("ProxyURL",proxy_url);
					//System.out.println("DOMAIN_APP KEY::"+tokens[10]+"_"+domainPath[1]);
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
					//System.out.println("PROXY_URL::"+proxy_url);
					hostMap.put("ProxyURL",proxy_url);
					//System.out.println("DOMAIN_APP_APP KEY::"+tokens[10]+"_"+domainPath[1]+"_"+domainPath[2]);
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
					//System.out.println("PROXY_URL::"+proxy_url);
					hostMap.put("ProxyURL",proxy_url);
					//System.out.println("DOMAIN_APP KEY::"+tokens[10]+"_"+domainPath[1]);
					domainMap.put(domainPath[1], hostMap);
					jsonResponse.setDomain(domainMap);
				}
				lifecycleMap.put(tokens[10], domainMap);
				jsonResponse.setLifecycle(lifecycleMap);
			}
		}
		return lifecycleMap;
	}

	public static String createDomainProxyURL (String lifecycle, String domain, String dApp){
		
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
		
	}
}
