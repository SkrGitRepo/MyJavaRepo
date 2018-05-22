<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="com.sample.utility.BrmsJsonUtil"%>
<%@page import="org.apache.http.client.methods.HttpPost"%>
<%@page import="org.apache.http.entity.StringEntity"%>
<%@page import="org.apache.http.client.HttpClient"%> 
<%@page import="org.apache.http.impl.client.DefaultHttpClient"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="org.apache.http.HttpResponse"%>
<%@page import="javax.xml.bind.DatatypeConverter"%>
<%@page import="com.cisco.dataconnect.process.Load" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%
	String jsonStr = request.getParameter("jsonStr");
    String searchDomain = request.getParameter("selectDomain");
    String respMessage = null;
    String searchType = request.getParameter("selectType");
    
   
   
    if (jsonStr !=null && searchDomain != null) {
		//String postUrl = "http://localhost:8090/nprd2/brmsadmin/work/info/" + domain + "/history";
		 /* String name = "brm.gen"; String password = "brmGen123"; String
		 statusResponse = null; String authString = name + ":" + password;
		 String decodedValue = DatatypeConverter.printBase64Binary(authString.getBytes("UTF-8"));
		 String authStringEnc = decodedValue;
		 
		
		String postUrl = "https://ibpm.cisco.com/prd2/brmsadmin/work/info/" + domain + "/history";
		final String USER_AGENT = "Mozilla/5.0";
		@SuppressWarnings({ "deprecation", "resource" })
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(postUrl);

		// add header
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Accept", "application/json");
		//post.setHeader("Authorization", "key=AIzaSyBTXdvWyrcRfyTS7sfZ100Wp7IfLkSMaxY");
		post.setHeader("Authorization", "Basic " + authString);
		StringEntity se = new StringEntity("{  $or: [ {'_id' :'" + jsonStr + "' } ] }");
		//StringEntity se = new StringEntity(jsonData);
		post.setEntity(se);

		HttpResponse res = client.execute(post);
		System.out.println("\n Sending 'POST' request to URL : " + postUrl);
		System.out.println("Post parameters : " + post.getEntity());

		System.out.println("Response Code : " + res.getStatusLine().getStatusCode());

		InputStreamReader isr = new InputStreamReader(res.getEntity().getContent());

		int numCharsRead;
		char[] charArray = new char[1024];
		StringBuffer sb = new StringBuffer();
		while ((numCharsRead = isr.read(charArray)) > 0) {
			sb.append(charArray, 0, numCharsRead);
		}
		respMessage = sb.toString();
		System.out.println("RESPONSE MESSAGE:"+respMessage);
 */
    	try {
    		String jsonString = "{  $or: [ {'_id' :'" + jsonStr + "' } ] }";
    		Load load = new Load();
			if ( (searchType != null) && (searchType.equalsIgnoreCase("history")) && (searchDomain != null) ) {
				//respMessage = load.searchHistory(searchDomain, jsonString);
				respMessage = load.search(searchDomain+"_history", jsonString);
			} else if ((searchType != null) && (searchType.equalsIgnoreCase("status")) && (searchDomain != null)){
				respMessage = load.search(searchDomain, jsonString);
				
			} 
		} catch (Throwable t) {
			respMessage = "200 " + t.toString();
		}
		
		if (respMessage == null)
			respMessage = "No matching result.";
 	}
%>

<html>
<title>BRMS SR Status</title>

<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title>EA OnRamp Process</title>
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
		
		function validateSrId(){
			var msg='';
			if(document.getElementById("srid").value == ''){
				if(msg==''){
					msg +='Please provide a SR ID';
					document.getElementById("srid").focus();	
				} 
			}
			if(msg != ''){
				alert(msg);
				return false;
			}
			return true;
		}
		
		function clearForm() {
		    document.getElementById("searchSRForm").reset();
		}
		
		function onWindowLoad(){
			var domainApp='<%=searchDomain%>';
			var infoType = '<%=searchType%>';
			
			
			var selectDomObj=document.getElementById('selectDomain');
			for (var i = 0; i < selectDomObj.options.length; i++) {
				if (selectDomObj.options[i].text.toUpperCase() == domainApp.toUpperCase()) {
					selectDomObj.options[i].selected = true;
				}
			}
			
			var selectTypeObj=document.getElementById('selectType');
			for (var i = 0; i < selectTypeObj.options.length; i++) {
				if (selectTypeObj.options[i].text.toUpperCase() == infoType.toUpperCase()) {
					selectTypeObj.options[i].selected = true;
				}
			}
			
		}
		
		</script>
		</head>



	<!-- <div id="DisableEnableAlertDiv">
		
			<fieldset style="width:75%;margin: auto;text-align:center;" >
			<form name="form" action="" method="POST">
			<legend class="pageText"><b>Search SR Status</b></legend>
			<table border="1" cellpadding="2" bgcolor="#cee7ff" class="brmsTable" width="100%">
				<tr>
				<td colspan="1" align="right">Select Domain : &nbsp;
				<select id="searchDomain" name="searchDomain">
					<option value="cpe">cpe</option>
					<option value="gssc">gssc</option>
					<option value="cpp">cpp</option>
					<option value="csc">csc</option>
					<option value="cvc">cvc</option>
				</select>
				</td>
				<td colspan="1" align="left">Search Type: &nbsp;
				<select id="searchType" name="searchType">
					<option value="info">Info</option>
					<option value="history">History</option>
				</select>
				</td>
				
				<tr>
				<td align="right" width="30%" >Search SR ID : </td>
				<td align="left" width="30%" >
				<%if(jsonStr != null) {%>
					<input type="text" name="jsonStr" value="<%=jsonStr%>">
				<%} else {%>
					<input type="text" name="jsonStr" value="">
				<%} %>
				</td></tr>
				<tr><td colspan="2" ><input type="submit" value="Search"></td>
				</tr>
			</table>
			</form>
		 -->	
			
		<body onload="onWindowLoad()">
		<br>
		<div id="SearchSRStatusDiv">
		<fieldset style="width:60%;margin: auto;text-align:center;" >
			<legend class="pageText" align="left"><b>Search SR Status:</b></legend>
			<form action="" name="searchSRForm" method="post">
			<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff" class="brmsTable" width="100%">
				<tr>
					<th align="left" width="30%" >Select Domain : </th>
					<td align="left">
					<select id="selectDomain" name="selectDomain">
						<option value="cpe">cpe</option>
						<option value="ermo">ermo</option>
						<option value="gssc">gssc</option>
						<option value="cpp">cpp</option>
						<option value="csc">csc</option>
						<option value="cvc">cvc</option>
						<option value="csw">csw</option>
						<option value="pqc">pqc</option>
						<option value="ea">ea</option>
						<option value="sccc">sccc</option>
						<option value="fin">fin</option>
						<option value="fines">fines</option>
					</select>
					</td>
				</tr>
				<tr>
					<th align="left" width="30%" >Info Type : </th>
					<td align="left">
					<select id="selectType" name="selectType">
						<option value="status">Status</option>
						<option value="history">History</option>
					</select>
					</td>
				</tr>
				<tr>
					<th align="left" width="30%" >Enter SR ID : </th>
					<td align="left" width="30%" >
					<%if(jsonStr != null) {%>
						<input type="text" id="srid" name="jsonStr" value="<%=jsonStr%>">
					<%} else {%>
						<input type="text" id="srid" name="jsonStr" value="" placeholder="e.g: RS-55934">
					<%} %>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<input align="middle" type="submit" value="Search" id="search" onclick="return validateSrId();">
						&nbsp;&nbsp;<input type="button" onclick="clearForm();" value="Clear">
					</td>
				</tr>
			</table>
			</form>
		
		<% if( (respMessage != null) ) { %>
			<table align="center"><tr><td valign="top"><b>Response &nbsp;:&nbsp;</b></td><td><textarea rows="50" cols="100" name="respoonse"><%=respMessage%></textarea></td></tr></table>
		<%} %>
			</fieldset>
		</div>			
</body>
</html>