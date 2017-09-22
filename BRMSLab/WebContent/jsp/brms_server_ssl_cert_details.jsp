<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.File"%>
<%@page import="com.cisco.brmspega.util.ts.TimeStampCreator"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String strURLPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();;//ParamUtil.getURLPathFromSession(request);
	String viewLatest = request.getParameter("vlatest");
	String idContent = null;
	String processScriptStr = null;
	String outputInd = "false";
	String outputStr = "";
	String scriptPath= null;
	String scriptName = null;
	scriptPath = "/opt/brms/shared/scripts/";
	scriptName = "view_brms_installed_certs_details.sh";
	String brmsCertFileName = "brms_installed_ssl_cert_details.txt";
	BufferedReader br = null;
	File certDetailsFile = new File(scriptPath + brmsCertFileName);
	boolean brmsCertFileExist = false; 
	

%>

<html>
<title>BRMS SR Status</title>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BRMS SSL DETAILS</title>

<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js">
	
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
						htmlContent = "<span style='color:red'>IMPORTANT : Please for 5 minutes and then refresh page to view latest ssl cert details..</span>" + htmlContent;
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
			
			if ( url.indexOf("view_brms_installed_certs_details.sh") != -1) {
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
		var vLatest = '<%=viewLatest%>'
		
		if (vLatest != ''  && vLatest == "y" ) {
			setInterval('blinkIt()',500);
			var divs = document.getElementsByTagName("div");
			
			if (divs[i].id == "viewCertDetails") {
				continue;
			}
			
			for(var i=0;i<divs.length;i++) {
				if (divs[i].id != "") {
					alert(divs[i].id)
					pingServer(divs[i].id);
				}
			}
		}
	}
	
	
</script>
</head>

<body onload="onWindowLoad()">
	
	<br>
	<div id="viewCertDetails">
		<fieldset style="width: 60%; margin: auto; text-align: center;">
			<legend class="pageText" align="left">
				<b>BRMS SERVER INSTALLED SSL CERT DETAILS:</b>
			</legend>
			<center><p><b><font color="green"><a href='<%=strURLPath%>/jsp/brms_server_ssl_cert_details.jsp?vlatest=y'>Generate latest BRMS SSL cert. validity report.</a></font></b></p></center>
			<%
				if (certDetailsFile.exists()) {
					brmsCertFileExist = true;
					try {
						String sCurrentLine;
						String fileContent = "";
						br = new BufferedReader(new FileReader(scriptPath + brmsCertFileName));
						while ((sCurrentLine = br.readLine()) != null) {
							//System.out.println(sCurrentLine);
							fileContent += sCurrentLine + "\n";
						}
						if (fileContent != null) {
			%>
			<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff"
				class="brmsTable" width="100%">
				<tr>
					<%-- <td><textarea rows="50" cols="150" name="respoonse" readonly><%=fileContent%></textarea></td> --%>
					<td><textarea rows="50" cols="150" name="respoonse" ><%=fileContent%></textarea></td>
				</tr>
			</table>
			<%
				}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (br != null)
								br.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
			}
			%>
		</fieldset>
	</div>

<% if( (viewLatest != null && viewLatest.equalsIgnoreCase("y")) ) { 
		String refreshPage = "<a href='"+strURLPath+"/jsp/brms_server_ssl_cert_details.jsp'> Refresh Page</a>";
		outputStr ="Get latest installed BRMS SSL certifcate details. "+refreshPage;
		processScriptStr = scriptPath + scriptName;
		idContent = strURLPath + "/process?pSS=" + processScriptStr + " &opInd=" +outputInd + " &opStr="
				+ outputStr + "&ts=" + TimeStampCreator.createTimeStamp();
		System.out.println("DB Config report IDContent :" + idContent);
%>
		<br/><br/>
		<table align='center' width='80%'>
		<tr>
			<td width="10"></td>
			<td class="tableBorder_bottom" width="300" ><b>Get latest certificate(SSL) details.</b><br /></td>
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
