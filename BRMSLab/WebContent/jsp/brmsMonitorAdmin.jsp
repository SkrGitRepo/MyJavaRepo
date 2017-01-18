<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="java.util.Map"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.Date"%>
<%@page import="java.net.URL"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.TreeSet"%>
<%@page import="java.util.HashSet"%>
<%@page import="org.apache.commons.io.FileUtils"%>
<%@page import="java.util.List"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FilenameFilter"%>



<%
String strURLPath = "http://localhost:8090/nprd2/brmsadmin/";//ParamUtil.getURLPathFromSession(request);
String domainApp =  request.getParameter("domainApp");
String userID	=	"sumkuma2";
if(null == domainApp || "".equals(domainApp))
	domainApp="All";

String serverDomain="";

String env=request.getParameter("env");
if(null==env || "".equals(env))
	env="All";

String view=request.getParameter("view");
if(null == view || "".equals(view))
	view="MonitorConfig";

String vdc = request.getParameter("vdc");
if(null == vdc || "".equals(vdc))
	vdc="None";

String vdcHost = request.getParameter("vdcHost");
if(null == vdcHost || "".equals(vdcHost))
	vdcHost="None";

String successParam= request.getParameter("successParam");
	if(null== successParam || "".equalsIgnoreCase(successParam))
		successParam="";

String folderLoc = "/opt/brms/monitor/config";//PropertyLoader.getInstance().getProperty("monitor_config_base");

/*************Monitor Config File**************************/
File monitor_config_file = new File(folderLoc+"/monitor.conf");
if (!monitor_config_file.getParentFile().exists()) {
	monitor_config_file.getParentFile().mkdir();
}
if (!monitor_config_file.exists()) {
	monitor_config_file.getParentFile().mkdir();
	monitor_config_file.createNewFile();
}
List<String> monitor_config_lines = FileUtils.readLines(monitor_config_file);
String RETRY_MAX="";
String RETRY_DELAY="";
String TIMEOUT="";
String SMTP_HOST="";
String SMTP_PORT="";
String SMTP_USER="";
String SMTP_PASSWD="";
boolean INCREMENTAL_TIMEOUTS=false;
String MON_DIR="";
for(String fileLine : monitor_config_lines){
	String key = fileLine.split("=")[0];
	String value=fileLine.split("=")[1];
	if(key.equalsIgnoreCase("RETRY_MAX"))
		RETRY_MAX=value;
	else if(key.equalsIgnoreCase("RETRY_DELAY"))
		RETRY_DELAY=value;
	else if(key.equalsIgnoreCase("INCREMENTAL_TIMEOUTS")){
		if(value.equals("1"))
			INCREMENTAL_TIMEOUTS=true;}
	else if(key.equalsIgnoreCase("TIMEOUT"))
			TIMEOUT=value;
	else if(fileLine.contains("MON_DIR"))
		MON_DIR=value;
	else if(fileLine.contains("SMTP_HOST"))
		SMTP_HOST=value;
	else if(fileLine.contains("SMTP_PORT"))
		SMTP_PORT=value;
	else if(fileLine.contains("SMTP_USER"))
		SMTP_USER=value;
	else if(fileLine.contains("SMTP_PASSWD"))
		SMTP_PASSWD=value;
}


/*************Alert Config File**************************/


File alert_lists_file = new File(folderLoc+"/alert_lists.conf");
if (!alert_lists_file.getParentFile().exists()) {
	alert_lists_file.getParentFile().mkdir();
}
if (!alert_lists_file.exists()) {
	alert_lists_file.createNewFile();
}
List<String> alert_lists_lines = FileUtils.readLines(alert_lists_file);

TreeSet<String> errorDisplayTable=(TreeSet)new TreeSet<String>().descendingSet();
TreeSet<String> warningDisplayTable=(TreeSet)new TreeSet<String>().descendingSet();

Set<String> envList=new HashSet<String>();
TreeSet<String> sortedEnvList= new TreeSet<String>();

Set<String> domainList=new HashSet<String>();
TreeSet<String> sortedDomainList= new TreeSet<String>();


for(String line:alert_lists_lines){
	if(!line.startsWith("#")){
		String[] tokens=line.split("\\|");
		envList.add(tokens[0]);
		if(!domainApp.equals("All") && (!env.equals("All"))){			
			if(tokens[0].equalsIgnoreCase(env) && tokens[1].equalsIgnoreCase(domainApp)){
				if(tokens[2].equalsIgnoreCase("ERROR"))
					errorDisplayTable.add(line);
				else
					warningDisplayTable.add(line);
			}
			if(tokens[0].equalsIgnoreCase(env))
				domainList.add(tokens[1]);
		}else{
			if(!env.equals("All"))
				if(tokens[0].equalsIgnoreCase(env))
				domainList.add(tokens[1]);
		}
	}	
}
sortedEnvList= new TreeSet<String>(envList);
sortedDomainList =  new TreeSet<String>(domainList);

/*************Enable disable Host Config File**************************/
String filename="";
String EDEnv="";
//Special case since we dont have a refernce to the filename that needs to be read.
if(env.equals("All"))
	EDEnv="DEV";
else
	EDEnv=env;
filename="/brms_mon_"+EDEnv+".txt";

//This is required, Since entry of 'env' value in *PRD_PaaS.txt* file is PRD
if(env.equals("PRD_PaaS"))
	EDEnv="PRD";
/*************Mask Config File**************************/

boolean allDisable=false;
File maskFile=new File(folderLoc+filename+".mask");

if (!maskFile.getParentFile().exists()) {
	maskFile.getParentFile().mkdir();
}
if (!maskFile.exists()) {
	maskFile.createNewFile();
}
List<String> maskEntries=null;
List<String> maskEntriesDomApp= new ArrayList<String>();
TreeMap<String,Boolean> sortedMaskEntries= new TreeMap<String,Boolean>();
if(maskFile.exists()){
	maskEntries= FileUtils.readLines(maskFile);
	for(String entry : maskEntries){
		if(null==entry  || entry.trim().equalsIgnoreCase("")){
			continue;
		}else{
			String[] entries=entry.split("\\|"); 
			if(entries[1].equalsIgnoreCase(domainApp)){
				if(entries.length==2) {
					allDisable=true;
				} else if(entries.length == 5 && (entries[2].equalsIgnoreCase("all")) ) {
					allDisable=true;
				} else {
					sortedMaskEntries.put(entries[2], true);
				}
			}else if(!vdc.equalsIgnoreCase("None") && !vdcHost.equalsIgnoreCase("None")){
				if(entries.length>2 && entries[2].contains(vdcHost))
					sortedMaskEntries.put(entries[2]+":"+entries[1], true);				
			}	
		}	
	}
}

TreeSet<String> sortedVDCList= new TreeSet<String>();
TreeSet<String> sortedVDCHostList = new TreeSet<String>();

Set<String> EDdomainList=new HashSet<String>();
File EDMonitorfile = new File(folderLoc+filename);
if (!EDMonitorfile.getParentFile().exists()) {
	EDMonitorfile.getParentFile().mkdir();
}
if (!EDMonitorfile.exists()) {
	EDMonitorfile.createNewFile();
}

List<String> EDMonitorlines = FileUtils.readLines(EDMonitorfile);
for(String line:EDMonitorlines){
	if(null==line  && line.trim().equalsIgnoreCase("")){
		continue;
	}else{
		if(!line.startsWith("#")){
			String[] tokens=line.split("\\|");
			URL aURL = new URL(tokens[3]);
			String hostName=aURL.getHost();
			int port = aURL.getPort();
			if(tokens[0].equalsIgnoreCase(EDEnv))
				EDdomainList.add(tokens[1]);	
			sortedVDCList.add(hostName.split("-")[1].toUpperCase());
			
			if(!domainApp.equals("All")){					
				if(tokens[0].equalsIgnoreCase(EDEnv) && tokens[1].equalsIgnoreCase(domainApp)){					
					if(!sortedMaskEntries.containsKey(hostName+":"+port))
						sortedMaskEntries.put(hostName+":"+port, false);
				}				
			}else{
				if(!vdc.equalsIgnoreCase("None")){
					if(!vdcHost.equalsIgnoreCase("None"))
						if(!sortedMaskEntries.containsKey(hostName+":"+port+":"+tokens[1]) && hostName.equalsIgnoreCase(vdcHost))
							sortedMaskEntries.put(hostName+":"+port+":"+tokens[1], false);
					if(vdc.equalsIgnoreCase(hostName.split("-")[1]))
						sortedVDCHostList.add(hostName);
				}				
			}			
		}
	}	
}
TreeSet<String> sortedEDDomainList =  new TreeSet<String>(EDdomainList);
%>

<html>
	<head>
		<title>Brms JVMs Monitor Admin</title>
		<script>
		function updateMonitorConfig(path){
			if(document.getElementById('INCREMENTAL_TIMEOUTS').checked)
				document.getElementById('INCREMENTAL_TIMEOUTS').value='1';
			document.getElementById("formAction").value="UPDATE_MONITOR_CONFIG";
			document.brmsMonitorAdminform.action = path;
			return true;
		}

		function updateMonitorSmtpConfig(path){
			document.getElementById("formAction").value="UPDATE_MONITOR_SMTP_CONFIG";
			document.brmsMonitorAdminform.action = path;
			return true;
		}
		
		function updateEnvAppAlertDetails(path){
			var emailIdsValid=true;
			var filter=/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i
			var emailIds= document.getElementsByName('emailIds');			
			for(i = 0 ; i < emailIds.length; i++){
				if(!emailIds[i].value==''){
					var emailId = emailIds[i].value.split(',');
					for(j=0;j<emailId.length;j++){
						if(!emailId[j]==''){
							if(!filter.test(emailId[j])){
								emailIdsValid=false;
								break;
							}
						}
					}
				}
				if(!emailIdsValid)
					break;
			}
			if(emailIdsValid){
				var domainApp=document.getElementById('selectDom').value;
				var env=document.getElementById('selectEnv').value;
				document.getElementById("formAction").value="UPDATE_ALERT_CONTACT";
				document.brmsMonitorAdminform.action = path+"/monitorConfig?view=AlertConfig&env="+env+"&domainApp="+domainApp;
				document.brmsMonitorAdminform.submit();
			}else{
				alert('Invalid Email ID/s provided. Please correct');
			}
		}
		
		function updateEnableDisableMonDetails(path){
			var domainApp=document.getElementById('selectDomForED').value;
			var env=document.getElementById('selectEnvForED').value;
			var vdc=document.getElementById('selectVDCForED').value;
			var vdcHost=document.getElementById('selectVDCHostForED').value;
			var allDisable;
			if(document.getElementById('selectAllHost').checked)
				allDisable='true';
			else
				allDisable='false';
			document.getElementById("formAction").value="UPDATE_ENABLE_DISABLE_MONITOR";
			document.brmsMonitorAdminform.action = path+"/monitorConfig?view=DisableEnableAlert&allDisable="+allDisable+"&env="+env+"&domainApp="+domainApp+"&vdc="+vdc+"&vdcHost="+vdcHost;
			document.brmsMonitorAdminform.submit();
		}
		
		function filterOnDomainEnv(path){
			var domain=document.getElementById('selectDom').value;
			var env=document.getElementById('selectEnv').value;
			document.brmsMonitorAdminform.action = path + "/jsp/brmsMonitorAdmin.jsp?view=AlertConfig&domainApp="+domain+"&env="+env;
			document.brmsMonitorAdminform.submit();
		}
		
		function filterOnDomainEnvED(path){
			var domain=document.getElementById('selectDomForED').value;
			var env=document.getElementById('selectEnvForED').value;
			var vdc=document.getElementById('selectVDCForED').value;
			document.brmsMonitorAdminform.action = path + "/jsp/brmsMonitorAdmin.jsp?view=DisableEnableAlert&domainApp="+domain+"&env="+env;
			document.brmsMonitorAdminform.submit();
		}
		
		function filterOnVDCEnvED(path){
			var env=document.getElementById('selectEnvForED').value;
			var vdc=document.getElementById('selectVDCForED').value;
			document.brmsMonitorAdminform.action = path + "/jsp/brmsMonitorAdmin.jsp?view=DisableEnableAlert&domainApp=All&env="+env+"&vdc="+vdc;
			document.brmsMonitorAdminform.submit();
		}
		
		function filterOnHostED(path){
			var env=document.getElementById('selectEnvForED').value;
			var vdc=document.getElementById('selectVDCForED').value;
			var vdcHost=document.getElementById('selectVDCHostForED').value;
			document.brmsMonitorAdminform.action = path + "/jsp/brmsMonitorAdmin.jsp?view=DisableEnableAlert&domainApp=All&env="+env+"&vdc="+vdc+"&vdcHost="+vdcHost;
			document.brmsMonitorAdminform.submit();
		}
		
		function resetForm(path){
			var env=document.getElementById('selectEnv').value;
			document.brmsMonitorAdminform.action = path + "/jsp/brmsMonitorAdmin.jsp?view=AlertConfig&env="+env;
			document.brmsMonitorAdminform.submit();
		}
		
		function resetFormED(path){
			var env=document.getElementById('selectEnvForED').value;
			document.brmsMonitorAdminform.action = path + "/jsp/brmsMonitorAdmin.jsp?view=DisableEnableAlert&env="+env;
			document.brmsMonitorAdminform.submit();
		}
		
		function LoadMonitorDet(path){
			document.brmsMonitorAdminform.action = path + "/jsp/brmsMonitorAdmin.jsp?view=MonitorConfig";
			document.brmsMonitorAdminform.submit();
		}
		
		function LoadAlertDet(path){
			document.brmsMonitorAdminform.action = path + "/jsp/brmsMonitorAdmin.jsp?view=AlertConfig";
			document.brmsMonitorAdminform.submit();
		}
		
		function LoadEnableDisableDet(path){
			document.brmsMonitorAdminform.action = path + "/jsp/brmsMonitorAdmin.jsp?view=DisableEnableAlert";
			document.brmsMonitorAdminform.submit();
		}
		
		
		
		function onWindowLoad(){
			var domainApp='<%=domainApp%>';
			var env='<%=env%>';
			var view = '<%=view%>';
			var successParam='<%=successParam%>';
			var vdc='<%=vdc%>';
			var vdcHost='<%=vdcHost%>';
			if(successParam!='')
				alert(successParam);
			if(view.toUpperCase() == 'MonitorConfig'.toUpperCase()){
				document.getElementById("MonitorConfigDiv").style.display='block';
				document.getElementById("AlertConfigDiv").style.display='none';
				document.getElementById("DisableEnableAlertDiv").style.display='none';				
			}
			if(view.toUpperCase() == 'AlertConfig'.toUpperCase()){
				document.getElementById("MonitorConfigDiv").style.display='none';
				document.getElementById("AlertConfigDiv").style.display='block';
				document.getElementById("DisableEnableAlertDiv").style.display='none';	
			}
			if(view.toUpperCase() == 'DisableEnableAlert'.toUpperCase()){
				document.getElementById("MonitorConfigDiv").style.display='none';
				document.getElementById("AlertConfigDiv").style.display='none';
				document.getElementById("DisableEnableAlertDiv").style.display='block';	
			}
			
			var selectDomObj=document.getElementById('selectDom');
			for (var i = 0; i < selectDomObj.options.length; i++) {
				if (selectDomObj.options[i].text.toUpperCase() == domainApp.toUpperCase()) {
					selectDomObj.options[i].selected = true;
				}
			}
			var selectEnvObj=document.getElementById('selectEnv');
			for (var i = 0; i < selectEnvObj.options.length; i++) {
				//if (selectEnvObj.options[i].text.toUpperCase() == env.toUpperCase()) {
				if (selectEnvObj.options[i].value.toUpperCase() == env.toUpperCase()) {
					selectEnvObj.options[i].selected = true;
				}
			}
			
			var selectDomForED=document.getElementById('selectDomForED');
			for (var i = 0; i < selectDomForED.options.length; i++) {
				if (selectDomForED.options[i].text.toUpperCase() == domainApp.toUpperCase()) {
					selectDomForED.options[i].selected = true;
				}
			}			
			var selectEnvForED=document.getElementById('selectEnvForED');
			for (var i = 0; i < selectEnvForED.options.length; i++) {
				//if (selectEnvForED.options[i].text.toUpperCase() == env.toUpperCase()) {
				if (selectEnvForED.options[i].value.toUpperCase() == env.toUpperCase()) {
					selectEnvForED.options[i].selected = true;
				}
			}
			
			var selectVDCForED=document.getElementById('selectVDCForED');
			for (var i = 0; i < selectVDCForED.options.length; i++) {
				if (selectVDCForED.options[i].text.toUpperCase() == vdc.toUpperCase()) {
					selectVDCForED.options[i].selected = true;
				}
			}
			
			var selectVDCHostForED=document.getElementById('selectVDCHostForED');
			for (var i = 0; i < selectVDCHostForED.options.length; i++) {
				if (selectVDCHostForED.options[i].text.toUpperCase() == vdcHost.toUpperCase()) {
					selectVDCHostForED.options[i].selected = true;
				}
			}
			
			if(vdc.toUpperCase()!='None'.toUpperCase() && !vdcHost.toUpperCase()!='None'.toUpperCase()){
				//alert(vdc);
				//alert(vdcHost);
				document.getElementById('selectAllHost').disabled="disabled";
			}
		}
		
		function isNumber(evt){
	        var charCode = (evt.which) ? evt.which : evt.keyCode;
	        if (charCode !=8  && charCode > 31 && (charCode < 48 || charCode > 57))
	        	return false;
	        return true;         
	       } 
		function isValidEmailChars(evt){
			var charCode = (evt.which) ? evt.which : evt.keyCode;
//			alert(charCode);
			if(charCode !=8 && charCode != 64 && charCode != 44 && charCode!=45 && charCode != 46 && charCode!=95){
				if(charCode > 47 && charCode < 58){
//					alert('>47 <58');
					return true;
				}else if((charCode > 64 && charCode < 91) || (charCode > 96 && charCode < 123)){
//					alert('>64 <123');
					return true;
				}
				else
					return false;
			}else{
//				alert('8,64,44,46'); 
				return true;	
			}
		}
		
		</script>
		
	</head>
	<body onload="onWindowLoad()">
		<br>
		<form id="brmsMonitorAdminform" name="brmsMonitorAdminform" method="post">
		<div>
		<fieldset style="width:90%;margin: auto;text-align:center;" >
		<legend class="pageText"><b>BRMS Monitoring System</b></legend>
		<input type="button" id="LoadMonitorConfig" onclick="LoadMonitorDet('<%= strURLPath %>');" value="Monitor Config Details"/>&nbsp;
		<input type="button" id="LoadAlertConfig" onclick="LoadAlertDet('<%= strURLPath %>');" value="Alert Config Details"/>&nbsp;
		<input type="button" id="LoadEnableDisable" onclick="LoadEnableDisableDet('<%= strURLPath %>');" value="Enable Disable Details"/>
		<br>
		<div id="MonitorConfigDiv" style="display: none;">
			<fieldset style="width:98%;margin: auto;text-align:center;" >
			<legend class="pageText"><b>Monitor Configuration Details: </b></legend>
			<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff" class="brmsTable" width="100%">
				<tr>
					<th>RETRY_MAX</th><th>RETRY_DELAY</th><th>TIMEOUT</th><th>INCREMENTAL_TIMEOUTS<th>MON_DIR</th>
				</tr>
				<tr>
					<td align="center"><input type="text" size="30" name="RETRY_MAX" id="RETRY_MAX" value="<%=RETRY_MAX%>" placeholder="Only [0-9] allowed" onkeypress="return  isNumber(event)"></td>
					<td align="center"><input type="text" size="30" name="RETRY_DELAY" id="RETRY_DELAY" value="<%=RETRY_DELAY%>" placeholder="Only [0-9] allowed" onkeypress="return  isNumber(event)"></td>
					<td align="center"><input type="text" size="30" name="TIMEOUT" id="TIMEOUT" value="<%=TIMEOUT%>" placeholder="Only [0-9] allowed" onkeypress="return  isNumber(event)"></td>
					<td align="center"><input type="checkbox" name="INCREMENTAL_TIMEOUTS" id="INCREMENTAL_TIMEOUTS" <%=INCREMENTAL_TIMEOUTS ? "checked" : "" %>></td>					
					<td align="center"><input type="text" size="30" name="MON_DIR" id="MON_DIR" value="<%=MON_DIR%>" onkeypress="return  isNumber(event)" disabled></td>
				</tr>
				<tr>
				<td colspan="5" align="center">
					<input type="submit" id="modifyMonitorConfig" onclick="updateMonitorConfig('<%= strURLPath %>/monitorConfig');" value="Update"/>
				</td>
				</tr>
			</table>
			<br/>
			<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff" class="brmsTable" width="100%">
				<tr>
					<th>SMTP_HOST</th><th>SMTP_PORT</th><th>SMTP_USER</th><th>SMTP_PASSWORD</th>
				</tr>
				<tr>
					<td align="center"><input type="text" size="30" name="SMTP_HOST" id="SMTP_HOST" value="<%=SMTP_HOST%>" onkeypress="return  isNumber(event)"></td>
					<td align="center"><input type="text" size="30" name="SMTP_PORT" id="SMTP_PORT" value="<%=SMTP_PORT%>" onkeypress="return  isNumber(event)"></td>
					<td align="center"><input type="text" size="30" name="SMTP_USER" id="SMTP_USER" value="<%=SMTP_USER%>" onkeypress="return  isNumber(event)"></td>
					<td align="center"><input type="password" size="30" name="SMTP_PASSWD" id="SMTP_PASSWD" value="<%=SMTP_PASSWD%>" onkeypress="return  isNumber(event)" disabled></td>
				</tr>
				<tr>
				<td colspan="5" align="center">
					<input type="submit" id="modifyMonitorSmtpConfig" onclick="updateMonitorSmtpConfig('<%= strURLPath %>/monitorConfig');" value="Update"/>
				</td>
				</tr>
			</table>
			</fieldset>
		</div>	
		
		<div id="AlertConfigDiv" style="display: none;">
			<fieldset style="width:98%;margin: auto;text-align:center;" >
			<legend class="pageText"><b>Alert List Details: </b></legend>
			<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff" width="100%" style="border:black;">
				<tr>
				<th colspan="2" align="center" width="40%">Select Env : &nbsp;<select id="selectEnv" onchange="resetForm('<%=strURLPath%>')">
					<option value="All">--None--</option>
						<%for(String envVal : sortedEnvList){%>
							<option value="<%=envVal.toUpperCase()%>"><%=envVal.toUpperCase() %></option><%} %>
							</select>
				</th>
				<th colspan="2" align="center">Select Domain : &nbsp;<select id="selectDom" onchange="filterOnDomainEnv('<%=strURLPath%>')">
					<option value="All">--None--</option>
						<%for(String domain :  sortedDomainList){%>
							<option value="<%=domain.toLowerCase() %>"><%=domain%></option><%}%>
							</select>
				</th>
				</tr>
				<%if(!domainApp.equals("All") && (!env.equals("All")) && (errorDisplayTable.size()>0 || warningDisplayTable.size()>0 )){ %>
					<tr><th align="center">TYPE</th><th align="center">To/CC/BCC</th><th align="center">Contact List</th></tr>
					<%
					boolean errCC=false;
					boolean errbCC=false;
					boolean warCC=false;
					boolean warbCC=false;
					int count=0;
					for(String line : errorDisplayTable) {
						count=count+1;
						String[] token = line.split("\\|");
						if(token[3].equalsIgnoreCase("cc"))
						errCC=true;
						else if(token[3].equalsIgnoreCase("bcc"))
						errbCC=true;%>
						<tr>
							<%if(count==1){ %>
							<td align="center" rowspan="3"><%=token[2] %>
							<%} %>
							<input type="hidden" name="alertType" value="<%=token[2]%>"/></td>
							<td align="center"><%=token[3]%><input type="hidden" name="emailIDType" value="<%=token[3]%>"/></td>
							<%if(token.length==5){ %>							
								<td align="center"><textarea name="emailIds" rows="2" cols="70" onkeypress="return isValidEmailChars(event)"><%=token[4]%></textarea></td>
						<%}else{%> 
								<td align="center"><textarea name="emailIds" rows="2" cols="70" onkeypress="return isValidEmailChars(event)"></textarea></td>
						<%}%>
						</tr>
					<%}
					if(!(errCC && errbCC)){
						if(!errCC) {%>	
						<tr>					
						<td align="center">CC
							<input type="hidden" name="alertType" value="ERROR"/>
							<input type="hidden" name="emailIDType" value="CC"/>
						</td>
						<td align="center"><textarea name="emailIds" rows="2" cols="70" onkeypress="return isValidEmailChars(event)"></textarea></td></tr>
						<%}if(!errbCC) {%>
						<tr>
						<td align="center">BCC
							<input type="hidden" name="alertType" value="ERROR"/>
							<input type="hidden" name="emailIDType" value="BCC"/>
						</td>
						<td align="center"><textarea name="emailIds" rows="2" cols="70" onkeypress="return isValidEmailChars(event)"></textarea></td>
						</tr> 
				<%			}
					}
					count=0;
					for(String line : warningDisplayTable) {
						count=count+1;
					String[] token = line.split("\\|");
					if(token[3].equalsIgnoreCase("cc"))
					warCC=true;
					else if(token[3].equalsIgnoreCase("bcc"))
					warbCC=true;%>
					<tr>
						<%if(count==1){ %>
							<td align="center" rowspan="3"><%=token[2] %>
							<%} %>
						<td align="center"><%=token[3]%>
							<input type="hidden" name="alertType" value="<%=token[2]%>"/>
							<input type="hidden" name="emailIDType" value="<%=token[3]%>"/>
						</td>
						<%if(token.length==5){ %>							
							<td align="center"><textarea name="emailIds" rows="2" cols="70" onkeypress="return isValidEmailChars(event)"><%=token[4]%></textarea></td>
						<%}else{%> 
							<td align="center"><textarea name="emailIds" rows="2" cols="70" onkeypress="return isValidEmailChars(event)"></textarea></td>
						<%}%>
					</tr>
					<%}
					if(!(warCC && warbCC)){
						if(!warCC) {%>
						<tr>
						<td align="center">CC
						<input type="hidden" name="alertType" value="WARNING"/>
						<input type="hidden" name="emailIDType" value="CC"/></td>
						<td align="center"><textarea name="emailIds" rows="2" cols="70" onkeypress="return isValidEmailChars(event)"></textarea></td></tr>
						<%}if(!warbCC) {%>
						<tr>
						<td align="center">BCC
						<input type="hidden" name="alertType" value="WARNING"/>
						<input type="hidden" name="emailIDType" value="BCC"/></td>
						<td align="center"><textarea name="emailIds" rows="2" cols="70" onkeypress="return isValidEmailChars(event)"></textarea></td>
						</tr> 
					<%	}
					}
				}%>
				<tr>
				<td colspan="5" align="center">
					<input type="button" id="modifyMonitorConfig" onclick="updateEnvAppAlertDetails('<%= strURLPath %>');" value="Update"/>
				</td>
				</tr>
			</table>
			</fieldset>
		</div>
		<div id="DisableEnableAlertDiv" style="display: none;">
			<fieldset style="width:98%;margin: auto;text-align:center;" >
			<legend class="pageText"><b>Enable/Disable Monitor</b></legend>
			<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff" class="brmsTable" width="100%">
				<tr>
				<th colspan="1" align="left">Select Env : &nbsp;<select id="selectEnvForED" onchange="resetFormED('<%=strURLPath%>')">
					<option value="DEV">DEV</option>
					<option value="LT">LT</option>
					<option value="STG">STG</option>
					<option value="POC">POC</option>
					<option value="PRD">PRD</option>
					</select>
				</th>
				<th colspan="1" align="center">Select Domain : &nbsp;
					<select id="selectDomForED" onchange="filterOnDomainEnvED('<%=strURLPath%>')">
					<option value="All">--None--</option>
						<%for(String domain :  sortedEDDomainList){%>
							<option value="<%=domain.toLowerCase() %>"><%=domain%></option><%}%>
					</select>&nbsp;&nbsp; OR &nbsp;&nbsp; Select VDC
					<select id="selectVDCForED" onchange="filterOnVDCEnvED('<%=strURLPath%>')">
					<option value="None">--None--</option>
						<%for(String vdcVal :  sortedVDCList){%>
							<option value="<%=vdcVal %>"><%=vdcVal%></option><%}%>
					</select>&nbsp;Select Host : 
					<select id="selectVDCHostForED" onchange="filterOnHostED('<%=strURLPath%>')">
					<option value="None">--None--</option>
						<%for(String vdcHostName :  sortedVDCHostList){%>
							<option value="<%=vdcHostName %>"><%=vdcHostName%></option><%}%>
					</select>
				</th>
				<th colspan="1"  align="left">Mask Domain : &nbsp;
					<input type="checkbox" id="selectAllHost" style="display: block;"  name="selectAllHost" <%=allDisable ? "checked" : "" %> />
				</th>
				</tr>
				<% if(sortedMaskEntries.size()>0) { 
					if(!domainApp.equalsIgnoreCase("All")){ %>
					<tr><th>HOST NAME</th><th>PORT</th><th>Mask Individual Nodes : &nbsp;</th></tr>
					<%}else{ %>
					<tr><th>HOST NAME:PORT</th><th>Domain</th><th>Mask Individual Nodes : &nbsp;</th></tr>
					<%}
					for(Map.Entry<String,Boolean> entry : sortedMaskEntries.entrySet()) {
						String[] tokens = entry.getKey().split(":");
						if(tokens.length<3)	{						
						%>						
						<tr>
							<td><%=tokens[0]%></td>
							<td><%=tokens[1]%> </td>
							<td>
								<input type="checkbox" name="individualHost" value='<%=env+"|"+domainApp+"|"+tokens[0]+":"+tokens[1]+"|"+userID+"|"+new Date()%>' <%=entry.getValue() ? "checked" : "" %> />	
							</td>	
						</tr>
						<%} else { %>
							<tr>
							<td><%=tokens[0]%>:<%=tokens[1]%></td>
							<td><%=tokens[2]%> </td>
							<td>
								<input type="checkbox" name="individualHost" value='<%=env+"|"+tokens[2]+"|"+tokens[0]+":"+tokens[1]+"|"+userID+"|"+new Date()%>' <%=entry.getValue() ? "checked" : "" %> />	
							</td>	
						</tr>
					<%	}
					}
				}%>
				<tr>
				<td colspan="5" align="center">
					<input type="button" id="modifyMonitorConfig" onclick="updateEnableDisableMonDetails('<%= strURLPath %>');" value="Update"/>
				</td>
				</tr>
			</table>
			</fieldset>
		</div>
		</fieldset>
		</div>
			<%if(null != request.getAttribute("formAction") ){ %>
			<input type="hidden" id="formAction" name="formAction" value='<%=request.getAttribute("formAction")%>'>
			<%} else {%>
				<input type="hidden" id="formAction" name="formAction"/>
			<%} %>
		</form>
	</body>
	
</html>
	