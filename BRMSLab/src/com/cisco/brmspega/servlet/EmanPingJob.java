package com.cisco.brmspega.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.sample.utility.BrmsJsonUtil;

public class EmanPingJob implements Runnable {
	
	private static final String configFileFolderLoc = "/opt/brms/shared/scripts/";//BrmsPropertyLoader.getInstance().getProperty("server_tools_cofig_base");
	ConcurrentHashMap<String, String> getAllEMANStatus = new ConcurrentHashMap<String, String>();
	ConcurrentHashMap<String, String> getDownStatus = new ConcurrentHashMap<String, String>();
	
	static EmanPingJob job = new EmanPingJob();

	
	private EmanPingJob() {
	}
	@Override
	public void run() { 
		
		long startEmanPingTime = System.currentTimeMillis();
		System.out.println("********** START OF EMAN PING TIME :"+startEmanPingTime);
		
		try {
			getAllEMANStatus = collectEMANStatus();
			//prepare_domain_app_down_notification(getDownStatus,getAllEMANStatus);
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long endEmanPingTime = System.currentTimeMillis();
		System.out.println("********** END OF EMAN PING TIME :"+endEmanPingTime);
		long elapsedEmanPingTime = (endEmanPingTime - startEmanPingTime);
		System.out.println("***** TOTAL EMAN PING EXECUTION TIME :"+ elapsedEmanPingTime);
		
	}

	private void prepare_domain_app_down_notification(ConcurrentHashMap<String, String> downStatus, ConcurrentHashMap<String, String> allStatus) {
		// TODO Auto-generated method stub
		StringBuffer devDomains = new StringBuffer();
		StringBuffer stgDomains = new StringBuffer();
		StringBuffer prodDomains = new StringBuffer();
		String notifyDevDomainList = null;
		String notifyStageDomainList = null;
		String notifyProdDomainList = null;
		String currentStatus = null;
		for (String domainApp : allStatus.keySet()) {
			currentStatus = allStatus.get(domainApp);
			if (domainApp.contains("dev")) {
				if (currentStatus.equalsIgnoreCase("down")) {
					String domainName = domainApp.split("_")[1];
					//System.out.println("*** DEV DOWN Domain" + domainName);
					devDomains.append(domainName + ",");
				}
			} else if (domainApp.contains("stage")) {
				if (currentStatus.equalsIgnoreCase("down")) {
					String domainName = domainApp.split("_")[1];
					//System.out.println("*** STAGE DOWN Domain" + domainName);
					stgDomains.append(domainName + ",");
				} 
			} else if (domainApp.contains("prod")) {
				if (currentStatus.equalsIgnoreCase("down")) {
					String domainName = domainApp.split("_")[1];
					prodDomains.append(domainName + ",");
				}
			}
		}
		
		notifyDevDomainList = devDomains.toString();
		if(!"".equals(notifyDevDomainList)) {
			send_notification("dev", devDomains);
		} 
		notifyStageDomainList = stgDomains.toString();
		if (!"".equals(notifyStageDomainList)) {
			send_notification("stage",stgDomains);
		}
		notifyProdDomainList = prodDomains.toString();
		if (!"".equals(notifyProdDomainList)){
			send_notification("prod",stgDomains);
		}
	}
	
	private void send_notification(String lifecycle, StringBuffer lcDomains) {
		String line;
		String baseUrl = null;
		try {
			baseUrl = getCurrentUrl();
		} catch (MalformedObjectNameException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//System.out.println( "CURRENT URL ::"+urlString);
		//System.out.println(lifecycle.toUpperCase()+" :: LC DOMAIN NOTIFICATION ::"+lcDomains);
		try {
			URL url = new URL(baseUrl+"?lc=" + lifecycle + "&down_domains="+lcDomains.toString());
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			//line = in.readLine();
			// System.out.println( line );
			//in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getCurrentUrl() throws MalformedURLException, MalformedObjectNameException, UnknownHostException{

	    MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();

        Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
                Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));

        String host = InetAddress.getLocalHost().getHostAddress();
        String hostname = InetAddress.getLocalHost().getHostName();
        String port = objectNames.iterator().next().getKeyProperty("port");

        System.out.println("Name of System : "+hostname );
        System.out.println("IP Address of System : "+host );
        System.out.println("port of tomcat server : "+port);
        String url = "http://"+hostname+":"+port+"/nprd2/brmsadmin/poststatus";
		return url;
	}
	
	public static EmanPingJob getInstance() {
		return job;
	}
	
	public ConcurrentHashMap<String, String> getEMANPingMap() {
        return getAllEMANStatus;
   }
	
	protected ConcurrentHashMap<String, String> collectEMANStatus() throws IOException {
		ExecutorService executor = null;
		ConcurrentHashMap<String, String> domainStatusMap = new ConcurrentHashMap<String, String>();
		int MYTHREADS = 10;
		//String folderLoc = "/opt/brms/shared/scripts/";
		Collection<File> fileList = new ArrayList<File>();
		Iterator<File> fileListIterator = null;
		
		//String[] lifecycle = { "POC" };
		//String[] lifecycle = { "STG" };
		String[] lifecycle = { "PRD","STG","DEV","LT","POC" };
		if ( lifecycle.length >= 4 && lifecycle[4].equals("poc")) {
			MYTHREADS = 2;
		}
		executor = Executors.newFixedThreadPool(MYTHREADS);

		for (int i = 0; i < lifecycle.length; i++) {
			
			String lc = lifecycle[i];
			
			final String fileFilterString = "brms_vm_cfg_*" + lc + ".txt";
			fileList = FileUtils.listFiles(new File(configFileFolderLoc), new WildcardFileFilter(fileFilterString), null);
			File file;
			fileListIterator = fileList.iterator();
			while (fileListIterator.hasNext()) {
				file = (File) fileListIterator.next();
				/*System.out.println("-----------------------------------------------");
				System.out.println("Read config file" + file.getName());
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
								// line:(lc,host,)" + allOutput);

								if (tokens.length >= 16) {
									// domainPath = tokens[9].split("/");
									String[] domainAppPath = new String[3];
									
									allOutput = tokens[10] + "|" + tokens[0] + "|" + tokens[1] + "|" + tokens[2] + "|"
											+ tokens[3] + "|" + tokens[4] + "|" + tokens[5] + "|" + tokens[6] + "|"
											+ tokens[7] + "|" + tokens[8] + "|" + tokens[9] + "|" + tokens[14] + "|"
											+ tokens[15];
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
										/*
										 * System.out.println("Domain/App:: "
										 * +domainAppPath[1]+"/"+domainAppPath[2
										 * ]); System.out.println(
										 * "DOMAIN PATH LENGTH"
										 * +domainAppPath.length);
										 * System.out.println(
										 * "DOMIAN PATH 1ts POS: "
										 * +domainAppPath[1]);
										 * System.out.println(
										 * "DOMIAN PATH 2ND POS: "
										 * +domainAppPath[2]);
										 */

										Runnable worker = new MyRunnable(tokens[10], tokens[0], tokens[3],
												domainAppPath, domainStatusMap);
										executor.execute(worker);
									}
								} else if (tokens.length < 16){
									Runnable worker = new MyRunnable(tokens[10], tokens[0], tokens[3], domainPath,
											domainStatusMap);
									executor.execute(worker);
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
		System.out.println("\n Finished all threads");
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
			String emanResult;
			if (!domainStatusMap.containsKey(key)) {
				//System.out.println("KEY to get ping status for : "+key);
				
				emanResult = emanStatusResponse(lifecycle, domain, app, host, port);
				domainStatusMap.put(key, emanResult);
			} else {
				//System.out.println("KEY already have ping status for \""+key+"\":"+ domainStatusMap.get(key));
			}
		}
	}
		
	protected String emanStatusResponse(String lifecycle,String domain,String dApp, String host, String port) {
		String webPage = null;
		String  resMessage = null;
		
		BrmsJsonUtil proxyUrl =  new BrmsJsonUtil();
		webPage = proxyUrl.createDomainProxyURL(lifecycle,domain,dApp);
		
		try {
			//System.out.println("INPUT EMAN URL TO CHECK::"+webPage);
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
			
			//System.out.println(emanResponse);
			if (emanResponse.contains("EMAN OK")) {
				/*System.out.println(emanResponse);
				System.out.println("SUCCESS EMAN Response"+emanResponse);*/
				resMessage = "Up";
			} else {
				/*System.out.println("Failure EMAN Response"+emanResponse);*/
				resMessage = "Down";
			}
			//System.out.println(" *******************************");
			
		} catch (MalformedURLException e) {
			//e.printStackTrace();
			//System.out.println("** Exception :: Malformed URL Exception");
		} catch (IOException e) {
			//e.printStackTrace();
			//System.out.println("** Exception :: IO Exception");
		} finally {
			if (resMessage == null) {
				resMessage = "Down";
			}
		}
		return resMessage;
	}
	
	protected String createDomainProxyURL (String lifecycle, String domain, String dApp){
		
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
