package com.cisco.brmspega.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

/**
 * Servlet implementation class BrmsMonitorAdminServlet
 */
@WebServlet("/BrmsMonitorAdminServletNew")
public class BrmsMonitorAdminServletNew extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String DOMAIN_PATH = "/opt/brms/shared/wl";
	private static final String monitorConfigBase = "/opt/brms/monitor/config";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BrmsMonitorAdminServletNew() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
		// TODO Auto-generated method stub
		httpResponse.getWriter().append("Served at: ").append(httpRequest.getContextPath());
		

		System.out.println("BrmsMonitorAdminServlet.doGet().."+httpRequest.getParameter("formAction"));
				
		String formAction = httpRequest.getParameter("formAction");
		String view= httpRequest.getParameter("view");
		if(null == view || view.equals("")) 
			view="MonitorConfig";
		if(null== formAction){
			httpRequest.getRequestDispatcher("./jsp/brmsMonitorAdminNew.jsp").forward(httpRequest, httpResponse);
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
			httpRequest.getRequestDispatcher("./jsp/brmsMonitorAdminNew.jsp?successParam=Monitor Config Updated Succesfully&view="+view).forward(httpRequest, httpResponse);
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
			httpRequest.getRequestDispatcher("./jsp/brmsMonitorAdminNew.jsp?successParam=Monitor SMTP Config Updated Succesfully&view="+view).forward(httpRequest, httpResponse);
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
			httpRequest.getRequestDispatcher("./jsp/brmsMonitorAdminNew.jsp?successParam=Alert Details Updated Succesfully&view="+view+"&env="+env+"&domainApp="+domainApp).forward(httpRequest, httpResponse);
		} else if ("UPDATE_ENABLE_DISABLE_MONITOR".equals(formAction)) {
			System.out.println("BrmsMonitorAdminServlet : " + formAction);
			String userID = "sumkuma2";// UserUtil.getUserId(httpRequest);
			String env = httpRequest.getParameter("env");
			String domainApp = httpRequest.getParameter("domainApp");
			String allDisable = httpRequest.getParameter("allDisable");
			String allInactive = httpRequest.getParameter("allInactive");
			
			System.out.println("ALL INACTIVE VALUEEEEE:"+allInactive);
			String vdc = httpRequest.getParameter("vdc");
			String vdcHost = httpRequest.getParameter("vdcHost");
			List<String> existingIndividualHosts = new ArrayList<String>();
			if (null == allDisable || allDisable.equals(""))
				allDisable = "false";

			String[] hostsToDisableArr = httpRequest.getParameterValues("individualHost");
			String[] inactiveHostArr = httpRequest.getParameterValues("individualInactiveHost");

			ArrayList<String> hostsToDisable = null;
			if (null != hostsToDisableArr && hostsToDisableArr.length > 0)
				hostsToDisable = new ArrayList<String>(Arrays.asList(hostsToDisableArr));

			ArrayList<String> hostsToInactive = null;
			if (null != inactiveHostArr && inactiveHostArr.length > 0)
				hostsToInactive = new ArrayList<String>(Arrays.asList(inactiveHostArr));

			File file = new File(monitorConfigBase + "/brms_mon_" + env + ".txt.mask");
			if (file.exists()) {
				List<String> lines = FileUtils.readLines(file);
				for (String line : lines) {
					if (!line.startsWith("#")) {
						String[] tokens = line.split("\\|");
						if (!vdcHost.equalsIgnoreCase("None")) {
							if (tokens.length == 3 && tokens[2].toUpperCase().contains(vdcHost.toUpperCase())) {
								existingIndividualHosts.add(tokens[0] + "|" + tokens[1] + "|" + tokens[2]);
							} else if (tokens.length > 3 && tokens[2].toUpperCase().contains(vdcHost.toUpperCase())) {
								System.out.println("TOKEN LENGTH IN VDC::"+tokens.length);
								System.out.println("INACTIVE VALUE FOR VDC::::"+tokens[0] + "|" + tokens[1] + "|" + tokens[2] + "|"
										+ tokens[3] + "|" + tokens[4]);
								existingIndividualHosts.add(tokens[0] + "|" + tokens[1] + "|" + tokens[2] + "|"
										+ tokens[3] + "|" + tokens[4] + "|" + tokens[5]);
							}
						} else if (!domainApp.equalsIgnoreCase("All")) {
							if (tokens[1].equalsIgnoreCase(domainApp)) {
								System.out.println("TOEKN LENGTH for MASK FILE ::" + tokens.length);
								if (tokens.length == 2) {
									// existingIndividualHosts.add(env+"|"+domainApp+"|"+tokens[2]+"|"+tokens[3]);
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

				if (allDisable.equalsIgnoreCase("true")) {
					// lines.add(env+"|"+domainApp);
					lines.add(env + "|" + domainApp + "|" + "all" + "|Active|" + userID + "|" + new Date());
				}

				if (allInactive.equalsIgnoreCase("true")) {
					// lines.add(env+"|"+domainApp);
					lines.add(env + "|" + domainApp + "|" + "all" + "|Inactive|" + userID + "|" + new Date());
				}

				if (!(null == hostsToDisable) && hostsToDisable.size() > 0) {
					lines.addAll(hostsToDisable);
				}

				if (!(null == hostsToInactive) && hostsToInactive.size() > 0) {
					lines.addAll(hostsToInactive);
				}
				FileUtils.writeLines(file, lines);

			} else {
				file.createNewFile();
				List<String> lines = new ArrayList<String>();

				if (!(null == hostsToDisable) && hostsToDisable.size() > 0) {
					lines.addAll(hostsToDisable);
				}

				if (!(null == hostsToInactive) && hostsToInactive.size() > 0) {
					lines.addAll(hostsToInactive);
				}

				if (allDisable.equalsIgnoreCase("true") && allInactive.equalsIgnoreCase("false")) {
					lines.add(env + "|" + domainApp + "|" + "all" + "|Aactive|" + userID + "|" + new Date());

				}

				if (allDisable.equalsIgnoreCase("true") && allInactive.equalsIgnoreCase("true")) {
					lines.add(env + "|" + domainApp + "|" + "all" + "|inactive|" + userID + "|" + new Date());
				}

				FileUtils.writeLines(file, lines);
			}
			httpRequest.setAttribute("formAction", formAction);
			if (!vdcHost.equalsIgnoreCase("None"))
				httpRequest
						.getRequestDispatcher(
								"./jsp/brmsMonitorAdminNew.jsp?successParam=Enable Disable Config Updated Succesfully&view="
										+ view + "&env=" + env + "&vdc=" + vdc + "&vdcHost=" + vdcHost)
						.forward(httpRequest, httpResponse);
			else
				httpRequest
						.getRequestDispatcher(
								"./jsp/brmsMonitorAdminNew.jsp?successParam=Enable Disable Config Updated Succesfully&view="
										+ view + "&env=" + env + "&domainApp=" + domainApp)
						.forward(httpRequest, httpResponse);
		}else{
			httpRequest.setAttribute("formAction", formAction);
			httpRequest.getRequestDispatcher("./jsp/brmsMonitorAdminNew.jsp?view="+view).forward(httpRequest, httpResponse);
		}
		
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
