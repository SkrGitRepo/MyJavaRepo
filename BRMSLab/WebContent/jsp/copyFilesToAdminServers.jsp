<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="java.net.InetAddress"%>
<%@page import="java.io.File"%>
<%@page import="java.net.UnknownHostException"%>

<%@page import="java.util.*" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String[] destHostList = request.getParameterValues("destHost");
    String srcFilePathAndName = request.getParameter("srcFile");
    String srcHost = request.getParameter("srcHost");
    String respMessage = null;
    List<String> selDestHost = new ArrayList<String>();
    
    if( destHostList !=null && destHostList.length > 0) {
    	for (String destHost : destHostList) {
    		selDestHost.add(destHost);
    	}
    }
    
    String isFileExist = "No";
    
    if (srcFilePathAndName != null) {
		File f = new File(srcFilePathAndName);
		if (f.exists() && !f.isDirectory()) {
			// do something
				isFileExist = "Yes";
		}

	}

	List<String> admServerList = new ArrayList<String>();
	admServerList.add("brms-nprd1-adm1");
	admServerList.add("brms-nprd2-adm1");
	admServerList.add("brms-prd1-adm1");
	admServerList.add("brms-prd2-adm1");
	admServerList.add("brms-prd3-adm1");
	admServerList.add("brms-prd4-adm1");

	InetAddress ip;
	String hostname = null;

	try {
		ip = InetAddress.getLocalHost();
		hostname = ip.getHostName();
		System.out.println("Your current IP address : " + ip);
		System.out.println("Your current Hostname : " + hostname);

	} catch (UnknownHostException e) {
		e.printStackTrace();
	}

	if (srcFilePathAndName != null && srcHost != null && destHostList.length > 0) {
		System.out.println(
				"File Path" + srcFilePathAndName + "Src Host " + srcHost + "Dest Hosts " + destHostList[1]);
	}
%>

<html>
	<title>BRMS Copier tool</title>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title>EA OnRamp Process</title>
		<%-- <%@include file="headerHead.jsp"%> --%>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
		<script type="text/javascript">
		
		function enableDisableSubmit(){
			if(document.getElementById('ldapName').value.trim().length > 0 && document.getElementById('onRampResourceID').value.trim().length>0){
				document.getElementById('validateAndGenerateURL').disabled=false;
			}else{
				document.getElementById('validateAndGenerateURL').disabled=true;
			}
			return true;
		}
		
		function validateFilePath(){
			var msg='';
			if(document.getElementById("srcFileId").value == ''){
				if(msg==''){
					msg +='Please provide a valid File to copy.';
					document.getElementById("srcFileId").focus();	
				} 
			} else {
				var filePath =  <%=isFileExist%> ;
				alert()
				if(filePath == "No") {
					msg +='File does not exist.';
					document.getElementById("srcFileId").focus();
				}
			}
			
			if(msg != ''){
				alert(msg);
				return false;
			}
			return true;
		}
		
		function fileExistCheck(filePath)	{
		    var http = new XMLHttpRequest();
		    http.open('HEAD', filePath, false);
		    http.send();
		    return http.status!=404;
		}
		
		function onWindowLoad(){
			var srcHost='<%=srcHost%>';
			
			var selSrcHost=document.getElementById('srcHost');
			for (var i = 0; i < selectSrcHost.options.length; i++) {
				if (selectSrcHost.options[i].text.toUpperCase() == srcHost.toUpperCase()) {
					selectSrcHost.options[i].selected = true;
				}
			}
			
		}
		
		</script>
	</head>

	<body onload="onWindowLoad()">
		<%-- <%@include file="headerBody.jsp"%> --%>
		<br>
		<div id="SearchSRStatusDiv">
		<fieldset style="width:60%;margin: auto;text-align:center;" >
			<legend class="pageText" align="left"><b>Copy File:</b></legend>
			<form action="" name="copyFileForm" method="post">
			<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff" class="brmsTable" width="100%">
				<tr>
					<th align="left" width="30%" >Source Host : </th>
					<td align="left">
						<%-- <select id="srcHost" name="srcHost">
						<% for (String serverName : admServerList)  {%>
							<option value=<%=serverName %>><%=serverName %></option>
						<% } %>
						</select> --%>
						<input type="text" id="localHostId" name="localHost" value=<%=hostname%>>
					</td>
					<th align="left" width="30%" >Destination Host : </th>
					<td align="left">
						<% for (String serverName : admServerList) {
							if ( (!selDestHost.isEmpty()) && selDestHost.contains(serverName)) {
							%>
								<input type="checkbox" name="destHost" value=<%=serverName%> checked>&nbsp; <%=serverName%> <br/>
							<% } else { %>
								<input type="checkbox" name="destHost" value=<%=serverName%>>&nbsp; <%=serverName%> <br/>
						<% }
						} %>
					</td>
				</tr>
				<tr>
					<th align="left" width="30%" >Source File: </th>
					<td align="left" width="30%" colspan="3">
						<input type="text" id="srcFileId" name="srcFile" >
						<%-- <input type="text" id="localHostId" name="localHost" value=<%=hostname %>> --%>
					</td>
				</tr>
							
				<tr>
					<td colspan="4" align="center">
						<input align="middle" type="submit" value="Copy File" id="copyFile" onclick="return validateFilePath();">
					</td>
				</tr>
			</table>
			</form>
		
			</fieldset>
		</div>			
</body>
</html>