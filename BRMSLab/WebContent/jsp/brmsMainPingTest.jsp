<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="com.cisco.brmspega.util.EnvBiListMapCache"%>
<%@page import="java.io.File" %>
<%@page import="java.util.*" %>
<%@page import="org.apache.commons.io.FileUtils" %>
<%@page import="com.cisco.brmspega.useracess.UserUtil" %>
<%@page import="com.cisco.brmspega.bundles.PropertyLoader" %>
<%@page import="com.cisco.brmspega.cache.DomainCache" %>
<%-- <%@page import="com.cisco.brmspega.util.param.ParamUtil" %> --%>


<%
String strURLPath= "https://ibpm.cisco.com/prd1/brmsadmin";
%>
<html>
	<head>
		<title>BRMS Administration</title>
		<script>
			function blinkIt() {
				 if (!document.all) return;
				 else {
				   	for(i=0;i<document.all.tags('blink').length;i++){
				      	s=document.all.tags('blink')[i];
				    	s.style.visibility=(s.style.visibility=='visible')?'hidden':'visible';
					}
				}
			}
			function pingServer(url) {
				var url = url.substring(4);
				//alert( 'URL is '+url);
				var xmlHttp;
				var numberPattern = /\d+/g;
				if (window.XMLHttpRequest) {
					// code for IE7+, Firefox, Chrome, Opera, Safari
				  	xmlHttp=new XMLHttpRequest();
				} else {
					// code for IE6, IE5
				  	xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
				}
				
				xmlHttp.onreadystatechange=function() {
					//alert(url);
					//alert('readyState'+ xmlHttp.readyState);
					alert('status'+ xmlHttp.status);
					alert('Status Text :: '+xmlHttp.statusText);
					//alert('response'+ xmlHttp.responseText);
					if (xmlHttp.readyState==4 && xmlHttp.status==200) {
				  		clearTimeout(xmlHttpTimeout)
				  		if (url.indexOf("ibpm") != -1) {
				    		document.getElementById("url="+url).innerHTML="<a style='color:white;' href='" + url.substring(4) + "'>"+xmlHttp.responseText+"</a>";
				    		if (xmlHttp.responseText.indexOf("EMAN OK!") != -1) {
					    		document.getElementById("url="+url).style.backgroundColor="Green";
					    	} else {
					    		document.getElementById("url="+url).style.backgroundColor="Grey";
					    	}
				    	} else {
				    		if (xmlHttp.responseText.indexOf("Health good.") != -1) {
					    		document.getElementById(url).style.backgroundColor="Green";
					    		//Extract numeric values from - 'Pega BRMBVSTG - US Health good. Time Taken is 2 milliseconds';
					    		var timeTaken = xmlHttp.responseText.match( numberPattern );
					    		document.getElementById(url).innerHTML="<a style='color:white;' href='" + url + "'>Good Health ("+timeTaken+" ms)</a>";				    		
					    	}else {
					    		document.getElementById(url).style.backgroundColor="Red";
					    		document.getElementById(url).innerHTML="<a style='color:white;' href='" + url + "'>Bad Health</a>";
					    	}
				    	}				    	
				    }
				}
				alert('new url'+ url);
				xmlHttp.open("GET", url, true);
				xmlHttp.send();
				// Timeout to abort in 10 seconds
				var xmlHttpTimeout=setTimeout(function() {
    				xmlHttp.abort();    				
    				if (url.indexOf("ibpm") != -1) {
   						document.getElementById("url="+url).innerHTML="<a style='color:white;' href='" + url.substring(4) + "'>Request timed out.</a>";
   					} else {
   						document.getElementById("url="+url).innerHTML="<a style='color:white;' href='" + url.substring(4) + "'>Request timed out.</a>";
   					}
   					document.getElementById("url="+url).style.backgroundColor="Red";
				}, 25000);
			}
			
			function onLoad() {
				setInterval('blinkIt()',500);
				//alert("i am here");
				var divs = document.getElementsByTagName("div");
				//alert(divs[1].id);
				for(var i=0;i<divs.length;i++) {
					alert(divs[i].id);
					if ((divs[i].id) != "" && divs[i].id != "nonAdminDiv") {
						pingServer(divs[i].id);
						alert(divs[i].id);
					}					
				}
			}
		</script>
	</head>
	<body onload=onLoad() >
		<table border="0" class="pageText">
			<tr>
			<%-- 	<td>
					<b><span id="cCIdMsg" style="display:inline-block;">&nbsp;&nbsp;BRMS Master Admin</span></b>
					<ul>
						<!--<li><a id="otherVDCLink" href="javascript:top.window.location=(oContextId!=''?'/'+oContextId:'')+'/brmsadmin'">BRMS Admin</a></li>-->
						<!--<li><a href="javascript:top.window.location=(contextId!=''?'/'+contextId:'')+'/prsysmgmt'">BRMS–System Monitoring</a></li>-->
						<li><a href="<%=strURLPath %>/jsp/brmsSystemMonitor.jsp">BRMS–System Monitoring</a></li>
						<li><a href="<%=strURLPath %>/jsp/listLdapEnv.jsp">BRMS–LDAP Setup</a></li>
						<li><a href="http://eman-core.cisco.com/SERVICE/OnRamp/resource/89133714;mode=Request">Onramp</a></li>
						<li><a href="<%=strURLPath %>/jsp/listVMs.jsp">VM List</a></li>
						<li><a href="<%=strURLPath %>/jsp/listEnvs.jsp?vm=ALL">Environment List (Servers List)</a></li>
						<li><a href="<%=strURLPath %>/jsp/listDomains.jsp?vm=ALL&envCF=ALL&env=ALL&ser=ALL">Domain List</a></li>
						<!--  <li><a href="<%=strURLPath %>/jsp/pegaHealth.jsp?page=1">Pega Health</a></li>-->
						<li><a href="<%=strURLPath %>/jsp/pegaHealth.jsp?domainApp=All&env=All&page=1">Pega Health</a></li>
						<!--<li><a href="serverHealth.jsp">Server Health</a></li>-->
						<!--<li><a href="<%=strURLPath %>/jsp/listFiles.jsp?fN=/opt/brms/shared/monitor/logs&fV=y">WebLogic Performance</a></li>-->
						<li><a href="<%=strURLPath %>/jsp/databaseHealth.jsp">Database Health</a></li>
						<li><a href="<%=strURLPath %>/jsp/clientServerDetails.jsp">Client Server Details</a></li>
						<!--<li><a href="listFiles.jsp">Log Details</a></li>-->
						<li><a href="<%=strURLPath %>/jsp/ldapGroupListing.jsp">Ldap Group Listing</a></li>
						<li><a href="<%=strURLPath %>/jsp/ldapUserListing.jsp">Ldap User Listing</a></li>
						<li><a href="https://pso-tools.cisco.com/trends">VM Trends</a></li>
						
						<!-- <li><a href="jmxCreateServer_1.jsp">Create VMs</a></li> -->

						<!-- <li><a href="serverActions.jsp">Server Actions</a></li> -->
						<li><a href="<%=strURLPath %>/jsp/urlTaskTriggers.jsp">Url Task Triggers</a></li>
						<li><a href="<%=strURLPath %>/jsp/lobReaderInput.jsp">LOB Reader</a></li>
						<li><a href="<%=strURLPath%>/hostConfig?formAction=MENU">Manage JVMs</a></li>
						<li><a href="<%=strURLPath %>/jsp/brms_domain_sr_status.jsp">BRMS Domain SR Status</a></li>
	
						<li><a href="<%=strURLPath%>/jsp/OnRampEAProcess.jsp">OnRamp - EA Process</a></li>
						<li><a href="<%=strURLPath%>/jsp/brmsMonitorAdmin.jsp?view=DisableEnableAlert">BRMS Monitoring System</a></li>
						<li><a href="<%=strURLPath %>/jsp/ManageCoreHprofLogs.jsp">Manage Core/hprof logs</a></li>
						<li><a href="<%=strURLPath %>/jsp/monitorDirectorySpace.jsp">Check Host Filesystem Usages Status</a></li>
						<li><a href="<%=strURLPath %>/jsp/brms_host_active_port_report.jsp">BRMS Used Host:Port Report.</a></li>
						<li><a href="<%=strURLPath %>/jsp/pingKafka.jsp">Ping Kafka</a></li>
						<li><a href="<%=strURLPath %>/jsp/mountLogInfo.jsp">Mount Log info</a></li>
						<li><a href="https://cdca.cloudapps.cisco.com/cdca/synch.do">Cisco SSO sync-up with DEV and STAGE servers</a></li>
						<li><a href="http://mailer-backend.cisco.com/mailer_tools/onramp_sync.pcgi">Onramp-mailer-ldap sync</a></li>
						<li><a href="http://wwwin-tools.cisco.com/ccpadmin/">CCP Admin</a></li>
						<li><a href="http://dbatool-prod.cisco.com:7777/dbts/jsp/dbMon/dbTablespaces.jsp?sid=">Database Tablespaces</a></li>
						<li><a href="http://dbatool-prod.cisco.com:7777/newdbts/trace/">Database Trace</a></li>
						<li><a href="http://iwe.cisco.com/web/database-operations/tools">Database Operations</a></li>
						<li><a href="<%=strURLPath %>/jsp/ServerSocketConnectionTest.jsp">Server Port Connectivity Test</a></li>
						<li><a href="<%=strURLPath %>/jsp/brmsDomainAppStatus.jsp">Domain/App EMAN Status</a></li>
						<!--<li><a href="dbTest.jsp">Database Ping Test</a></li>-->
						</ul>
					<div id="nonAdminDiv" style="display:none">
						<b>&nbsp;&nbsp;BRMS App Admin</b>
						<ul>
							<li><a href="<%=strURLPath %>/jsp/jndiDbTest.jsp?qP=jndiDbTest_query.jsp&qPS=355">JNDI Database Test</a></li>
							<li><a href="<%=strURLPath %>/jsp/jndiDbTest.jsp?qP=listTableSize_query.jsp&qPS=155">Table Size List</a></li>
							<li><a href="<%=strURLPath %>/jsp/jmsProducer.jsp">JMS Producer</a></li>
							<li><a href="<%=strURLPath %>/jsp/jmsConsumer.jsp">JMS Consumer</a></li>
							<!--<li><a href="listTables.jsp">Prop JNDI Database Test</a></li>-->
							<li><a href="<%=strURLPath %>/jsp/classLoadTest.jsp">Test Class Load</a></li>		
						</ul>
					</div>
				</td> --%>

					</table>
				</td>
	<%
	
	//String folderLoc = "/opt/brms/shared/script";//PropertyLoader.getInstance().getProperty("server_tools_cofig_base");
	File file = new File("C:\\opt\\brms\\shared\\scripts\\HealthCheck.cfg");
	if (file.exists()) {
	%>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td valign="top">
					<br/><br/>
					<table border="0" class="pageText" style="border-collapse: collapse;">
						<tr>
							<td style="border: 1px solid LightGray;"><b>App Name</b></td>
							<td style="border: 1px solid LightGray;"><b>Description</b></td>
							<td style="border: 1px solid LightGray;"><b>Status</b></td>
						</tr>	
	<%
		List<String> fileLines = FileUtils.readLines(file);;
		String fileLine = null;
		String[] fileLineData = null;
		String domainDesc = null;
		for (int i=0; i < fileLines.size(); i++) {
			fileLine = fileLines.get(i);
			fileLineData = fileLine.split(",");
			domainDesc = DomainCache.getInstance().getDomainDesc(fileLineData[0]);
	%>
						<tr>
							<td style="border: 1px solid LightGray;"><a href="<%=fileLineData[5]%>" target="_blank"><%=fileLineData[0]%><%=!"".equals(fileLineData[1])? " - " + fileLineData[1]:""%>&nbsp;&nbsp;(<%=fileLineData[2]%>)</a></td>
							<td style="border: 1px solid LightGray;"><%=(domainDesc != null ? domainDesc : "")%></td>
							<td style="border: 1px solid LightGray;">
								<%=fileLineData[3]%>
								<div id="url=<%=fileLineData[3]%>" class="pageText" style="font-style: italic;">
									<blink>Pinging....</blink>
								</div>
							</td>
						</tr>
	<%
		}
	} else {
	%>
		<tr><td>FIle does not exist</td></tr>
	<%} %>
					</table>
				</td>

			</tr>
		</table>
	</body>
</html>
