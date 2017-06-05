package com.cisco.brmspega.servlet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.cisco.brmspega.bundles.PropertyLoader;

public class BrmsMonitorAdminServlet extends HttpServlet {

	private static final long serialVersionUID = -9025288579935013341L;

	private static final String DOMAIN_PATH = "/opt/brms/shared/wl";
	
	private static final String monitorConfigBase = "/opt/brms/monitor/config"; //PropertyLoader.getInstance().getProperty("monitor_config_base");

	public BrmsMonitorAdminServlet() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}

	public void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
		System.out.println("BrmsMonitorAdminServlet.doGet().."+httpRequest.getParameter("formAction"));
				
		String formAction = httpRequest.getParameter("formAction");
		String view= httpRequest.getParameter("view");
		if(null == view || view.equals("")) 
			view="MonitorConfig";
		if(null== formAction){
			httpRequest.getRequestDispatcher("./jsp/brmsMonitorAdmin.jsp").forward(httpRequest, httpResponse);
			httpRequest.setAttribute("formAction", formAction);
		} else if("UPDATE_MONITOR_CONFIG".equals(formAction)){
			System.out.println("BrmsMonitorAdminServlet : "+formAction); 
			File file = new File(monitorConfigBase+"/monitor.conf");
			List<String> lines = FileUtils.readLines(file);
			for(int i=0;i<lines.size();i++){
				String key = lines.get(i).split("=")[0];
				String value=lines.get(i).split("=")[1];
				if(key.equalsIgnoreCase("RETRY_MAX"))
					lines.set(i, "RETRY_MAX="+httpRequest.getParameter("RETRY_MAX"));
				else if(key.equalsIgnoreCase("RETRY_DELAY"))
					lines.set(i, "RETRY_DELAY="+httpRequest.getParameter("RETRY_DELAY"));
				else if(key.equalsIgnoreCase("INCREMENTAL_TIMEOUTS")){
					String INCREMENTAL_TIMEOUTS = httpRequest.getParameter("INCREMENTAL_TIMEOUTS");
					if(null == INCREMENTAL_TIMEOUTS ||  INCREMENTAL_TIMEOUTS.equals(""))
						INCREMENTAL_TIMEOUTS="0";
					lines.set(i, "INCREMENTAL_TIMEOUTS="+INCREMENTAL_TIMEOUTS);
				}
				else if(key.equalsIgnoreCase("TIMEOUT"))
					lines.set(i, "TIMEOUT="+httpRequest.getParameter("TIMEOUT"));
				else if(key.equalsIgnoreCase("MON_DIR")){
					String MON_DIR = httpRequest.getParameter("MON_DIR");
					if( MON_DIR!=null)
						lines.set(i, "MON_DIR="+MON_DIR);
				}
			}
			FileUtils.writeLines(file, lines);
			httpRequest.setAttribute("formAction", formAction);
			httpRequest.getRequestDispatcher("./jsp/brmsMonitorAdmin.jsp?successParam=Monitor Config Updated Succesfully&view="+view).forward(httpRequest, httpResponse);
		}
		else if("UPDATE_MONITOR_SMTP_CONFIG".equals(formAction)){
			System.out.println("BrmsMonitorAdminServlet : "+formAction); 
			File file = new File(monitorConfigBase+"/monitor.conf");
			List<String> lines = FileUtils.readLines(file);
			for(int i=0;i<lines.size();i++){
				String key = lines.get(i).split("=")[0];
				String value=lines.get(i).split("=")[1];
				if(key.equalsIgnoreCase("SMTP_HOST"))
					lines.set(i, "SMTP_HOST="+httpRequest.getParameter("SMTP_HOST"));
				else if(key.equalsIgnoreCase("SMTP_PORT"))
					lines.set(i, "SMTP_PORT="+httpRequest.getParameter("SMTP_PORT"));
				else if(key.equalsIgnoreCase("SMTP_USER"))
					lines.set(i, "SMTP_USER="+httpRequest.getParameter("SMTP_USER"));
				else if(key.equalsIgnoreCase("SMTP_PASSWD")) {
					String SMTP_PASSWD = httpRequest.getParameter("SMTP_PASSWD");
					if( SMTP_PASSWD!=null)
						lines.set(i, "SMTP_PASSWD="+SMTP_PASSWD);
				}
			}
			FileUtils.writeLines(file, lines);
			httpRequest.setAttribute("formAction", formAction);
			httpRequest.getRequestDispatcher("./jsp/brmsMonitorAdmin.jsp?successParam=Monitor SMTP Config Updated Succesfully&view="+view).forward(httpRequest, httpResponse);
		}
		else if("UPDATE_PING_DOMAIN_LIST".equals(formAction)){
			System.out.println("BrmsMonitorAdminServlet : "+formAction); 
			File file = new File(monitorConfigBase+"/new_ping_url_domain_list.txt");
			List<String> lines = FileUtils.readLines(file);
			System.out.println("DEV LIST :"+httpRequest.getParameter("DEV_DOMAIN_LIST"));
			System.out.println("LT LIST :"+httpRequest.getParameter("LT_DOMAIN_LIST"));
			System.out.println("STAGE LIST :"+httpRequest.getParameter("STAGE_DOMAIN_LIST"));
			/*for(int i=0;i<lines.size();i++){
				String key = lines.get(i).split("=")[0];
				String value=lines.get(i).split("=")[1];
				if(key.equalsIgnoreCase("DEV")) {
					lines.set(i, "dev="+httpRequest.getParameter("DEV_DOMAIN_LIST"));
				} else if(key.equalsIgnoreCase("STAGE") || key.equalsIgnoreCase("STG") ) {
					lines.set(i, "stage="+httpRequest.getParameter("STAGE_DOMAIN_LIST"));
				} else if(!key.equalsIgnoreCase("LT") && httpRequest.getParameter("LT_DOMAIN_LIST") != null) {
					lines.set(i, "lt="+httpRequest.getParameter("LT_DOMAIN_LIST"));
				} else if(key.equalsIgnoreCase("LT")) {
					lines.set(i, "lt="+httpRequest.getParameter("LT_DOMAIN_LIST"));
				} else if(key.equalsIgnoreCase("PROD") || key.equalsIgnoreCase("PRD")) {
					String PROD_LIST = httpRequest.getParameter("PROD_DOMAIN_LIST");
					lines.set(i, "prod="+PROD_LIST);
				}
			}*/
				if(httpRequest.getParameter("DEV_DOMAIN_LIST") != null) {
					String dev_value = lines.get(0);
					if(dev_value == null) {
						lines.add(0, "dev="+httpRequest.getParameter("DEV_DOMAIN_LIST"));
					} else {
						lines.set(0, "dev="+httpRequest.getParameter("DEV_DOMAIN_LIST"));
					}
				} else if(httpRequest.getParameter("STAGE_DOMAIN_LIST") != null) {
					String stg_value = lines.get(1);
					//lines.contains("stage")
					if(stg_value == null) {
						lines.add(1, "stage="+httpRequest.getParameter("STAGE_DOMAIN_LIST"));
					} else {
						lines.set(1, "stage="+httpRequest.getParameter("STAGE_DOMAIN_LIST"));
					}
				} else if(httpRequest.getParameter("LT_DOMAIN_LIST") != null) {
					String lt_value = lines.get(2);
					if(lt_value == null) {
						lines.add(2, "lt="+httpRequest.getParameter("LT_DOMAIN_LIST"));
					} else {
						lines.set(2, "lt="+httpRequest.getParameter("LT_DOMAIN_LIST"));
					}
				} else if( httpRequest.getParameter("PROD_DOMAIN_LIST") != null) {
					String prod_value = lines.get(3);
					if(prod_value == null) {
						lines.add(3, "prod="+httpRequest.getParameter("PROD_DOMAIN_LIST"));
					} else {
						lines.set(3, "prod="+httpRequest.getParameter("PROD_DOMAIN_LIST"));
					}
				}
//				lines.add("lt=csc,wpr");
//				
//				lines.set(1, "stage=cswwwww");
//				lines.set(2, "lt=cscccccc");
			
			System.out.println("LINES:"+lines);
			FileUtils.writeLines(file, lines);
			httpRequest.setAttribute("formAction", formAction);
			httpRequest.getRequestDispatcher("./jsp/brmsMonitorAdmin.jsp?successParam=Domain List Updated Succesfully&view="+view).forward(httpRequest, httpResponse);
		}
	else if("UPDATE_RR_CONFIG".equals(formAction)){
		System.out.println("BrmsMonitorAdminServlet : "+formAction); 
		File file = new File(monitorConfigBase+"/rr.conf");
		List<String> lines = FileUtils.readLines(file);
		for(int i=0;i<lines.size();i++){
			String key = lines.get(i).split("=")[0];
			String value=lines.get(i).split("=")[1];
			if(key.equalsIgnoreCase("RR_EMAIL_TO")) {
				String rr_email_to = httpRequest.getParameter("RR_EMAIL_TO");
				if (rr_email_to.endsWith(",")) {
					lines.set(i, "RR_EMAIL_TO="+ rr_email_to);
				} else {
					lines.set(i, "RR_EMAIL_TO="+ rr_email_to +",");
				}
			} else if(key.equalsIgnoreCase("RR_EMAIL_FROM")) {
				lines.set(i, "RR_EMAIL_FROM="+httpRequest.getParameter("RR_EMAIL_FROM"));
			} else if(key.equalsIgnoreCase("RR_WAIT_TIME")) {
				lines.set(i, "RR_WAIT_TIME="+httpRequest.getParameter("RR_WAIT_TIME"));
			}
		}
		FileUtils.writeLines(file, lines);
		httpRequest.setAttribute("formAction", formAction);
		httpRequest.getRequestDispatcher("./jsp/brmsMonitorAdmin.jsp?successParam=Rolling Restart Config Updated Succesfully&view="+view).forward(httpRequest, httpResponse);
	}
		else if("UPDATE_ALERT_CONTACT".equals(formAction)){
			System.out.println("BrmsMonitorAdminServlet : "+formAction);
			File file = new File(monitorConfigBase+"/alert_lists.conf");
			List<String> lines = FileUtils.readLines(file);
			String env=httpRequest.getParameter("env");
			String domainApp=httpRequest.getParameter("domainApp");
			boolean[] updatedEntryTrack = new boolean[httpRequest.getParameterValues("alertType").length];
			String[] alertTypes = httpRequest.getParameterValues("alertType");
			String[] emailIDTypes = httpRequest.getParameterValues("emailIDType");
			String[] emailIds = httpRequest.getParameterValues("emailIds");
			
			
			for(int lineIndex=0;lineIndex<lines.size();lineIndex++){
				for(int i=0;i<updatedEntryTrack.length;i++){
					String lineToUpdate=env+"|"+domainApp+"|"+alertTypes[i]
											+"|"+emailIDTypes[i]
											+"|"+emailIds[i];
					String lineToMatch=env+"|"+domainApp+"|"+alertTypes[i]+"|"+emailIDTypes[i];
					if(lines.get(lineIndex).toLowerCase().startsWith(lineToMatch.toLowerCase())){
						System.out.println("Updating entry : "+lineToUpdate);
						lines.set(lineIndex, lineToUpdate);
						updatedEntryTrack[i]=true;
						break;
					}
				}
			}
			for(int i=0;i<updatedEntryTrack.length;i++){
				if(!updatedEntryTrack[i]){
					System.out.println("In adding lines");
					String lineToUpdate=env+"|"+domainApp+"|"+alertTypes[i]
							+"|"+emailIDTypes[i]
							+"|"+emailIds[i];
					lines.add(lineToUpdate);
				}				
			}
			FileUtils.writeLines(file, lines);
			httpRequest.setAttribute("formAction", formAction);
			httpRequest.getRequestDispatcher("./jsp/brmsMonitorAdmin.jsp?successParam=Alert Details Updated Succesfully&view="+view+"&env="+env+"&domainApp="+domainApp).forward(httpRequest, httpResponse);
		}else if("UPDATE_ENABLE_DISABLE_MONITOR".equals(formAction)){
			System.out.println("BrmsMonitorAdminServlet : "+formAction);
			String userID = "sumkuma2";//
			String env=httpRequest.getParameter("env");
			String domainApp=httpRequest.getParameter("domainApp");
			String allDisable=httpRequest.getParameter("allDisable");
			String vdc=httpRequest.getParameter("vdc");
			String vdcHost=httpRequest.getParameter("vdcHost");
			List<String> existingIndividualHosts = new ArrayList<String>();
			if(null == allDisable || allDisable.equals(""))
				allDisable="false";
			
			String[] hostsToDisableArr = httpRequest.getParameterValues("individualHost");
			
			ArrayList<String> hostsToDisable= null;
			if(null != hostsToDisableArr && hostsToDisableArr.length > 0)
				hostsToDisable= new ArrayList<String>(Arrays.asList(hostsToDisableArr));	
			
				File file = new File(monitorConfigBase+"/brms_mon_"+env+".txt.mask");
				if(file.exists()){
					List<String> lines = FileUtils.readLines(file);
					for(String line:lines){
						if(!line.startsWith("#")){
							String[] tokens=line.split("\\|");
							if(!vdcHost.equalsIgnoreCase("None")){
								if(tokens.length>2 && tokens[2].toUpperCase().contains(vdcHost.toUpperCase())){
									existingIndividualHosts.add(tokens[0]+"|"+tokens[1]+"|"+tokens[2]);
								}
						} else if (!domainApp.equalsIgnoreCase("All")) {
							System.out.println("TOEKN LENGTH for MASK FILE ::" + tokens.length);
							if (tokens[1].equalsIgnoreCase(domainApp)) {
								
								if (tokens.length == 2) {
										existingIndividualHosts.add(env + "|" + domainApp);
								} else if (tokens.length == 3) {
									existingIndividualHosts.add(env + "|" + domainApp + "|" + tokens[2]);
								} else {
									if (tokens.length == 5) {
										if (tokens[2].equalsIgnoreCase("all")) {
											existingIndividualHosts.add(env + "|" + domainApp + "|" + tokens[2] + "|"
													+ tokens[3] + "|" + tokens[4]);
										} else {
											existingIndividualHosts.add(env + "|" + domainApp + "|" + tokens[2] + "|"
													+ tokens[3] + "|" + tokens[4]);
										}
									} else if (tokens.length == 6) {
										if (tokens[2].equalsIgnoreCase("all")) {
											existingIndividualHosts.add(env + "|" + domainApp + "|" + tokens[2] + "|"
													+ tokens[3] + "|" + tokens[4] + "|" + tokens[5]);
										} else {
											existingIndividualHosts.add(env + "|" + domainApp + "|" + tokens[2] + "|"
													+ tokens[3] + "|" + tokens[4] + "|" + tokens[5]);
										}
									}
								}
							}
						}								
						}
					}
					lines.removeAll(existingIndividualHosts);
					
					if(allDisable.equalsIgnoreCase("true")){
							lines.add(env + "|" + domainApp + "|" + "all" + "|" + userID + "|" + new Date());
					}	
					
					if(!(null == hostsToDisable) && hostsToDisable.size()>0) {
							lines.addAll(hostsToDisable);
							//lines.add(env + "|" + domainApp + "|" + "all" + "|" + userID + "|" + new Date());
					}
					
					FileUtils.writeLines(file, lines);
				} else {
					file.createNewFile();
					List<String> lines = new ArrayList<String>();
					if(!(null == hostsToDisable) && hostsToDisable.size()>0)
						lines.addAll(hostsToDisable);
					
					if(allDisable.equalsIgnoreCase("true")){
							//lines.add(env+"|"+domainApp);
							lines.add(env + "|" + domainApp + "|" + "all" + "|" + userID + "|" + new Date());
					}
					FileUtils.writeLines(file, lines);
				}		
			httpRequest.setAttribute("formAction", formAction);
			if(!vdcHost.equalsIgnoreCase("None"))
				httpRequest.getRequestDispatcher("./jsp/brmsMonitorAdmin.jsp?successParam=Enable Disable Config Updated Succesfully&view="+view+"&env="+env+"&vdc="+vdc+"&vdcHost="+vdcHost).forward(httpRequest, httpResponse);
			else
				httpRequest.getRequestDispatcher("./jsp/brmsMonitorAdmin.jsp?successParam=Enable Disable Config Updated Succesfully&view="+view+"&env="+env+"&domainApp="+domainApp).forward(httpRequest, httpResponse);
		}else{
			httpRequest.setAttribute("formAction", formAction);
			httpRequest.getRequestDispatcher("./jsp/brmsMonitorAdmin.jsp?view="+view).forward(httpRequest, httpResponse);
		}
		
	} 
	

}