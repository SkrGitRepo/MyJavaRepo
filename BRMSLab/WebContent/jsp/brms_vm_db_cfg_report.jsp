<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.HashMap"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="java.io.File"%>
<%@page import="java.util.List" %>
<%@page import="java.util.Collection" %>
<%@page import="java.util.Collections" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.util.Map" %>
<%@page import="java.util.TreeMap" %>
<%@page import="com.cisco.brmspega.util.file.BRMSFileParseUtil" %>
<%@page import="com.cisco.brmspega.util.ts.TimeStampCreator" %>
<%@page import="org.apache.commons.io.FileUtils" %>
<%@page import="org.apache.commons.io.filefilter.WildcardFileFilter"%>



<%
String strURLPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
String lifecycle = request.getParameter("lc");
String inDomain = request.getParameter("dname");
String showPwd = request.getParameter("spwd");
String genReport = request.getParameter("genReport");
String reportType = request.getParameter("rtype");

String idContent = null;
String processScriptStr = null;
String outputInd = "false";
String outputStr = "";
String scriptPath= null;
String scriptName = null;
scriptPath = "/opt/brms/shared/scripts/";
String dbCfgFileDir =  "/opt/brms/install/";
scriptName = "brms_db_report_job.sh";
String dbCfgFileName = "BRMS_ALL_ALL_DOMAINs_DB_CONF_REPORT.csv";
List<String> hostList = new ArrayList<String>();

if ( reportType != null && reportType.equalsIgnoreCase("jvm")) {
	dbCfgFileName = "BRMS_ALL_ALL_JVMs_DB_CONF_REPORT.csv";
}

HashMap<String,Integer> uniqueDomainMap = new HashMap<String,Integer>();
Map<String, Integer> sortedUniqueDomMap = null;
	
File dbCfgFile = new File(dbCfgFileDir + dbCfgFileName);
boolean dbCFGFileExist = false; 

if (dbCfgFile.exists()) {
	dbCFGFileExist = true;
	BRMSFileParseUtil brmsFileUtil = new BRMSFileParseUtil();
	hostList = brmsFileUtil.get_brms_db_cfg_report(dbCfgFileName);

	if (!hostList.isEmpty() || hostList != null) {
		Collections.sort(hostList);
		int domain_count = 1;
		for (String data : hostList) {
			String[] tokens = data.split("\\,");
			String dom_name = tokens[1];
			if (!uniqueDomainMap.containsKey(dom_name)) {
				uniqueDomainMap.put(dom_name, domain_count);
				domain_count += 1;
			}
		}
		sortedUniqueDomMap = new TreeMap<String, Integer>(uniqueDomainMap);
	}
}
%>

<html>
<title>BRMS DB CONFIG Report</title>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<script type="text/javascript"
src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
<script type="text/javascript">
			
	function clearForm() {
	    document.getElementById("reportDBConfigForm").reset();
	}
	
	function filterOnDCEnv(path){
		var lc=document.getElementById('selectLC').value;
		var domName=document.getElementById('selectDom').value;
		document.reportDBConfigForm.action = path + "/jsp/brms_vm_db_cfg_report.jsp?lc="+lc+"&dname="+domName+"&rtype="+recType;
		document.reportDBConfigForm.submit();
	}
	
	function filterOnDomEnv(path){
		var lc=document.getElementById('selectLC').value;
		var domName=document.getElementById('selectDom').value;
		document.reportDBConfigForm.action = path + "/jsp/brms_vm_db_cfg_report.jsp?lc="+lc+"&dname="+domName+"&rtype="+recType;
	}
	
	function filterOnRType(path){
		var lc=document.getElementById('selectLC').value;
		var domName=document.getElementById('selectDom').value;
		var recType=document.getElementById('selectRType').value;
		document.reportDBConfigForm.action = path + "/jsp/brms_vm_db_cfg_report.jsp?lc="+lc+"&dname="+domName+"&rtype="+recType;
		document.reportDBConfigForm.submit();
	}
	
	function blinkIt() {
		 if (!document.all) return;
		 else {
		   	for(i=0;i<document.all.tags('blink').length;i++){
		      	s=document.all.tags('blink')[i];
		    	s.style.visibility=(s.style.visibility=='visible')?'hidden':'visible';
			}
		}
	}
	
	function runServerScript(url,id,lastInd,timeProcessInd) {
		var xmlHttp;
		if (window.XMLHttpRequest) {
			// code for IE7+, Firefox, Chrome, Opera, Safari
		  	xmlHttp=new XMLHttpRequest();
		} else {
			// code for IE6, IE5
		  	xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlHttp.onreadystatechange=function() {
			if (xmlHttp.readyState==4 && xmlHttp.status==200) {
		  		clearTimeout(xmlHttpTimeout);
		  		document.getElementById(id).innerHTML += xmlHttp.responseText;
				// remove running...
				if (lastInd) {
					var htmlContent = document.getElementById(id).innerHTML;
					// This is for non-IE
					htmlContent = htmlContent.substring(htmlContent.indexOf("<pre>"));
					// This is for IE
					htmlContent = htmlContent.substring(htmlContent.indexOf("<PRE>"));
					if (timeProcessInd) {
						htmlContent = "<span style='color:red'>IMPORTANT : Please wait for 5 minutes, before browsing DB Config reports.</span>" + htmlContent;
					}
					document.getElementById(id).innerHTML = htmlContent;
				}
		    }
		}
		xmlHttp.open("POST",url,true);
		xmlHttp.send();
		// Timeout to abort in 10 seconds
		var xmlHttpTimeout=setTimeout(function() {
			xmlHttp.abort();
				document.getElementById(id).innerHTML="Request timed out.";
		}, 25000);
	}
	
	function pingServer(id) {
		var urls = id.split(",");
		var url;
		var functionStr;
		var increment = 5000;
		var interval = increment;
		var lastInd = false;
		var timeProcessInd = false;
		for (var i in urls) {
			url = urls[i];
			
			if ( url.indexOf("brms_db_report_job.sh") != -1) {
				timeProcessInd = true;
			}
			if (i == (urls.length - 1)) {
				lastInd = true;
			}
			if (i == 0) {
				runServerScript(url,id,lastInd,timeProcessInd);
			} else {
				functionStr = "runServerScript('" + url + "','" + id + "'," + lastInd + "," + timeProcessInd + ")";
				setTimeout(functionStr, interval);
				interval+=increment;
			}
		}
	}
	
	function onWindowLoad(){
		var domName ='<%=inDomain%>';
		var env ='<%=lifecycle%>';
		var doGenReport ='<%=genReport%>';
		var repType = '<%=reportType%>'
		
		var selectLCObj=document.getElementById('selectLC');
		for (var i = 0; i < selectLCObj.options.length; i++) {
			if (selectLCObj.options[i].text.toUpperCase() == env.toUpperCase()) {
				selectLCObj.options[i].selected = true;
			} 
		}
		
		var selectDOMObj=document.getElementById('selectDom');
		for (var i = 0; i < selectDOMObj.options.length; i++) {
			if (selectDOMObj.options[i].text.toUpperCase() == domName.toUpperCase()) {
				selectDOMObj.options[i].selected = true;
			}
		}
		
		var selectRTypeObj=document.getElementById('selectRType');
		for (var i = 0; i < selectRTypeObj.options.length; i++) {
			if (selectRTypeObj.options[i].text.toUpperCase() == repType.toUpperCase()) {
				selectRTypeObj.options[i].selected = true;
			}
		}
		
		
		if (doGenReport != ''  && doGenReport == "y" ) {
			setInterval('blinkIt()',500);
			var divs = document.getElementsByTagName("div");
			
			for(var i=0;i<divs.length;i++) {
				
				if (divs[i].id == "reportDBConfig") {
					continue;
				}
			
				if (divs[i].id != "") {
					pingServer(divs[i].id);
				}
			}
		}
	}
</script>
</head>

<body onload="onWindowLoad()">

<br>
<div id="reportDBConfig">
	<fieldset style="width: 60%; margin: auto; text-align: center;">
		<legend class="pageText" align="left">
			<b>BRMS DB CFG Report:</b>
		</legend>
		<% if (dbCFGFileExist) { %>
			<center><p><b><font color="green"><a href='<%=strURLPath%>/jsp/brms_db_cfg_report.jsp?genReport=y'>Generate latest config report file.</a></font></b></p></center>
		<%}%>
		<form action="" id="reportDBConfigForm" name="reportDBConfigForm"
			method="post">
			<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff"
				class="brmsTable" width="100%">
				<tr>
					<th align="center" width="30%">Select Env</th>
					<th align="center" width="30%">Select Domain</th>
					<th align="center" width="30%">Report Type</th>
				</tr>
				<tr>
					<td align="center">
					<select id="selectLC" name="lc" onchange="filterOnDCEnv('<%=strURLPath%>')">
						<% if( strURLPath.contains("nprd") || strURLPath.contains("test") ) {%>
							<option value="DEV">DEV</option>
							<option value="STG">STG</option>
							<option value="LT">LT</option>
							<option value="POC">POC</option>
							<option value="PRD">PRD</option>
						<%} else if ( strURLPath.contains("prd")) { %>
							<option value="PRD">PRD</option>
						<%}%>
					</select></td>

					<td align="center">
					<select id="selectDom" name="dname"
						onchange="filterOnDomEnv('<%=strURLPath%>')">
							<option value="none">--none--</option>
							<%if (sortedUniqueDomMap != null && !sortedUniqueDomMap.isEmpty()) {%>
								<option value="all">ALL</option>
								<%for(String domain : sortedUniqueDomMap.keySet() ) {%>
								<option value="<%=domain%>"><%=domain%></option>
								<%}
							}%>
					</select>
					</td>
					
					<td align="center">
					<select id="selectRType" name="rtype"
						onchange="filterOnRType('<%=strURLPath%>')">
							<option value="DOMAIN">DOMAIN</option>
							<option value="JVM">JVM</option>
					</select>
					</td>
				</tr>
			</table>
		</form>

		<% if (!hostList.isEmpty() && lifecycle !=null  && inDomain != null) {
				
			String lifeCycle  		= "NA";
			String domainName 		= "NA";
			String hostPort   		= "NA";
			String pegVer	  		= "NA";
			String dataSourceName   = "NA";
			String jndiName			= "NA";
			String conString		= "NA";
			String dbServiceName	= "NA";
			String dbSchema			= "NA";
			String dbPwd			= "NA";
			
		%>
		<br/><br/>
		<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff"
			class="brmsTable" width="100%">
			<tr>
				<th align="left">No.</th>
				<th align="center" width="30%" colspan="2">Env.</th>
				<th align="center" width="30%" colspan="2">DOMAIN</th>
				<%if ( reportType != null && reportType.equalsIgnoreCase("jvm")) { %>
					<th align="center" width="30%" colspan="2">Host:Port</th>	
				<% }%>
				<th align="center" width="30%" colspan="2">PEGA VER.</th>
				<th align="center" width="30%" colspan="2">DSN</th>
				<th align="center" width="30%" colspan="2">JNDI NAME</th>
				<th align="center" width="30%" colspan="2">CON-STRING</th>
				<th align="center" width="30%" colspan="2">DB-SNAME</th>
				<th align="center" width="30%" colspan="2">SCHEMA</th>
				<% if(showPwd != null && showPwd.equalsIgnoreCase("y")){ %>
					<th align="center" width="30%" colspan="2">PASSWORD</th>
				<%}%>
			</tr>

			<%
				int count = 1;
				for(String data :  hostList) {
						String[] tokens = data.split("\\,");
						System.out.println("Length" + tokens.length);
						if (tokens.length > 3 && tokens[0].equalsIgnoreCase(lifecycle.toUpperCase())
									&& inDomain.equalsIgnoreCase("all")) {
							if ( reportType != null && reportType.equalsIgnoreCase("jvm")) { 
								lifeCycle = tokens[0]; domainName = tokens[1];   hostPort = tokens[3];
								pegVer=tokens[2];      dataSourceName=tokens[4]; jndiName=tokens[5];
								conString=tokens[6];   dbServiceName=tokens[7];  dbSchema=tokens[8];
								dbPwd=tokens[9];	
								dbPwd =  (dbPwd.contains("{AES}")) ? "Encrypted" : dbPwd;
							} else {
								lifeCycle = tokens[0]; domainName = tokens[1];   hostPort = tokens[3];
								pegVer=tokens[2];      dataSourceName=tokens[3]; jndiName=tokens[4];
								conString=tokens[5];   dbServiceName=tokens[6];  dbSchema=tokens[7];
								dbPwd=tokens[8];
								dbPwd =  (dbPwd.contains("{AES}")) ? "Encrypted" : dbPwd;
							}
						
				%>
				<tr>
					<td><%=count%></td>
					<td colspan="2"><%=lifeCycle%></td>
					<td colspan="2"><%=domainName%></td>
					<%if ( reportType != null && reportType.equalsIgnoreCase("jvm")) { %>
						<td colspan="2"><%=hostPort%></td>
					<% }%>
					<td colspan="2"><%=pegVer%></td>
					<td colspan="2"><%=dataSourceName%></td>
					<td colspan="2"><%=jndiName%></td>
					<td colspan="2"><%=conString%></td>
					<td colspan="2"><%=dbServiceName%></td>
					<td colspan="2"><%=dbSchema%></td>
					<% if(showPwd != null && showPwd.equalsIgnoreCase("y")) { %>
						<td colspan="2"><%=dbPwd%></td>
					<% }%>
				</tr>
				<%
					count = count + 1;
				} else if (tokens.length > 3 && tokens[0].equalsIgnoreCase(lifecycle.toUpperCase())
									&& !inDomain.equalsIgnoreCase("all") && tokens[1].equalsIgnoreCase(inDomain)) 
					{
						if ( reportType != null && reportType.equalsIgnoreCase("jvm")) { 
							lifeCycle = tokens[0]; domainName = tokens[1];   hostPort = tokens[3];
							pegVer=tokens[2];      dataSourceName=tokens[4]; jndiName=tokens[5];
							conString=tokens[6];   dbServiceName=tokens[7];  dbSchema=tokens[8];
							dbPwd=tokens[9];		
						} else {
							lifeCycle = tokens[0]; domainName = tokens[1];   hostPort = tokens[3];
							pegVer=tokens[2];      dataSourceName=tokens[3]; jndiName=tokens[4];
							conString=tokens[5];   dbServiceName=tokens[6];  dbSchema=tokens[7];
							dbPwd=tokens[8];
						}
					%>
				<tr>
					<td><%=count%></td>
					<td colspan="2"><%=lifeCycle%></td>
					<td colspan="2"><%=domainName%></td>
					<%if ( reportType != null && reportType.equalsIgnoreCase("jvm")) { %>
						<td colspan="2"><%=hostPort%></td>
					<% }%>
					<td colspan="2"><%=pegVer%></td>
					<td colspan="2"><%=dataSourceName%></td>
					<td colspan="2"><%=jndiName%></td>
					<td colspan="2"><%=conString%></td>
					<td colspan="2"><%=dbServiceName%></td>
					<td colspan="2"><%=dbSchema%></td>
					<% if(showPwd != null && showPwd.equalsIgnoreCase("y")) { %>
						<td colspan="2"><%=dbPwd%></td>
					<% }%>
				</tr>
				<%
					count = count + 1;
					}
			}
			%>
		<%} else if (!dbCFGFileExist && lifecycle == null  && inDomain == null) {%>
			<center>
			<p><b><font color="red"> Required config file does not exist..!</font></b>(<a href='<%=strURLPath%>/jsp/brms_db_cfg_report.jsp?genReport=y'>CREATE</a>)</p>
			</center>
		<%}%>
		</table>
	</fieldset>
</div>

<% if( (genReport != null && genReport.equalsIgnoreCase("y")) ) { 
		String refreshPage = "<a href='"+strURLPath+"/jsp/brms_db_cfg_report.jsp'> Refresh Page</a>";
		outputStr ="Generating DB Config report for all Domain/JVM's. "+refreshPage;
		
		//processScriptStr = "ssh -f brms@" +" "+ scriptPath + scriptName;
		processScriptStr = scriptPath + scriptName;
		idContent = strURLPath + "/process?pSS=" + processScriptStr + " &opInd=" +outputInd + " &opStr="
				+ outputStr + "&ts=" + TimeStampCreator.createTimeStamp();
		System.out.println("DB Config report IDContent :" + idContent);
%>
		<br/><br/>
		<table align='center' width='80%'>
		<tr>
			<td width="10"></td>
			<td class="tableBorder_bottom" width="300" ><b>Generate DB config report.</b><br /></td>
			<td class="tableBorder_left_bottom">
				<div id="<%=idContent%>" class="pageText" style="color: #055A78; font-weight: bold; font-style: italic;">
					<blink>Running....</blink>
				</div>
			</td>
			<td width="10"></td>
		</tr>
		</table>
		
<%}%>
</body>
</html>
