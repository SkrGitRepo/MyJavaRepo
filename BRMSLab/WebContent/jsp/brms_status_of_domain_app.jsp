<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.sample.utility.BrmsJsonUtil"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*,javax.xml.bind.DatatypeConverter,java.net.URL,java.net.MalformedURLException,
java.net.HttpURLConnection,com.google.gson.Gson,com.google.gson.GsonBuilder,org.json.JSONArray,org.json.JSONObject" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BRMS Domain's EMAN Status</title>
</head>
<body>
<%
   String lc_json = request.getParameter("lc");
   String dom_json = request.getParameter("dom");
   String app_json = request.getParameter("app");
   String host_json =  request.getParameter("hosts");
   StringBuffer reqURL = request.getRequestURL();
   String jsonResponse = null;
 
   if (dom_json != null && app_json == null) {
	   if (lc_json != null) {
		   String newLC  = lc_json;
		   if (lc_json.equalsIgnoreCase("prod"))
			   newLC="prd";
		   if (lc_json.equalsIgnoreCase("stage"))
			   newLC="stg";
   		
		   jsonResponse = BrmsJsonUtil.getJson(newLC, dom_json, null);
	   } else {
		   jsonResponse = BrmsJsonUtil.getJson("all", dom_json, null);
	   }
   } else if (dom_json != null && app_json != null) {
	   if (lc_json != null) {
		   String newLC  = lc_json;
		   if (lc_json.equalsIgnoreCase("prod"))
			   newLC="prd";
		   if (lc_json.equalsIgnoreCase("stage"))
			   newLC="stg";
		   
		   jsonResponse = BrmsJsonUtil.getJson(newLC, dom_json, app_json);
	   } else {
		   	jsonResponse = BrmsJsonUtil.getJson("all", dom_json, app_json);
	   }
   } else {
	   if (lc_json != null) {
		   String newLC  = lc_json;
		   if (lc_json.equalsIgnoreCase("prod"))
			   newLC="prd";
		   if (lc_json.equalsIgnoreCase("stage"))
			   newLC="stg";
		   
		   jsonResponse = BrmsJsonUtil.getJson(newLC, null, null);
	   } else {
		   jsonResponse = BrmsJsonUtil.getJson("all", null, null);
	   }
   }
	
	JSONArray lcLevel = new JSONArray(jsonResponse);
	int lcleveLenth = lcLevel.length();
	%>
	<h3>DOMAIN/APP EMAN PING STATUS:</h3>
	<div>
	<table border="0" class="pageText" style="border-collapse: collapse;"><tr>
	<% 
	for(int i=0; i < lcLevel.length();i++) {
		JSONObject lc = lcLevel.getJSONObject(i);
		Iterator<String> lcKeys = lc.keys();
		String lifecycle = null;
		String domain = null;
		String domainApp = null;
		String domainStatus = null;
		JSONObject domainJson=null;
		JSONObject domainAllChildJson = null;
		JSONObject domainAppAllChildJson = null;
		String domainChildDateTime= null;
		String domainAppChildDateTime= null;
		String domainChildMonStatus= null;
		String domainAppChildMonStatus= null;
		String domainChildProxyUrl =  null;
		String domainAppChildProxyUrl =  null;
		JSONObject domainAppsChild =  null;
		String domainChildApps =  null;
		JSONArray domainChildHosts = null;
		JSONArray domainAppChildHosts = null;
	%>
	<%
		while (lcKeys.hasNext()) {
			lifecycle = (String)lcKeys.next();
			if (lifecycle.equalsIgnoreCase(lc_json)) {
				
			if ( lc.get(lifecycle) instanceof JSONObject ) {
				domainJson = new JSONObject(lc.get(lifecycle).toString());
				Iterator<String> domainKeys = domainJson.keys();
				%>
				<td valign="top" style="border: 1px solid LightGray;">
				<table border="0" class="pageText" style="border-collapse: collapse;">
				<tr>
				<th style="border: 1px solid LightGray;" colspan="3"><a href="<%=reqURL%>?lc=<%=lifecycle%>"><%=lifecycle.toUpperCase()%></a></th>
				</tr>
				<tr>
				<% 
					if (app_json != null) {
				%>
					<th style="border: 1px solid LightGray;">Domain</th>
					<th style="border: 1px solid LightGray;">End-points</th>
					<th style="border: 1px solid LightGray;">Status</th>
				<%  } else if (app_json == null ) { %>
					<th style="border: 1px solid LightGray;">Domain/App</th>
					<th style="border: 1px solid LightGray;">End-points</th>
					<th style="border: 1px solid LightGray;">Status</th>
				<%  } %>
				</tr>
				<% 
				while (domainKeys.hasNext()) {
					domain = (String)domainKeys.next();
					domainAllChildJson = new JSONObject(domainJson.get(domain).toString());
					domainChildDateTime = domainAllChildJson.get("DateTime").toString();
					domainChildProxyUrl = domainAllChildJson.get("ProxyURL").toString();
					domainChildHosts = domainAllChildJson.getJSONArray("HOSTS");

					if(domainAllChildJson.has("STATUS") && dom_json == null) {
						domainChildMonStatus = domainAllChildJson.get("STATUS").toString();
						String bgColor = domainChildMonStatus.equalsIgnoreCase("up")?"green":"red";
				%>
						<tr>
						<% if (domainAllChildJson.has("APPS"))  { %>
							<td style="border: 1px solid LightGray;"><b><i><a href="<%=reqURL%>?lc=<%=lifecycle%>&dom=<%=domain%>"> <%=domain%></a></i></b></td>
						<% } else { %>
							<td style="border: 1px solid LightGray;"><%=domain%></td>	
						<% }
						   if ( domainChildHosts.length() > 0 ){
						%>
							<td style="border: 1px solid LightGray;"><%=domainChildHosts.length()%></td>
						<% } %>
						<td bgcolor="<%=bgColor%>" style="border: 1px solid LightGray;"><i><a href=<%=domainChildProxyUrl%> target="_blank"><font color="white"><%=domainChildMonStatus%></font></a></i></td></tr>
				<%
					} else if (dom_json == null) {
						%>	
						<tr>
						<% if (domainAllChildJson.has("APPS"))  { %>
							<td style="border: 1px solid LightGray;" ><b><i><a href="<%=reqURL%>?lc=<%=lifecycle%>&dom=<%=domain%>"> <%=domain%></a></i></b></td>
						<% } else { %>
							<td style="border: 1px solid LightGray;"><%=domain%></td>
						<% }
						   if ( domainChildHosts.length() > 0 ) {
						%>
							<td style="border: 1px solid LightGray;"><%=domainChildHosts.length()%></td>
						<% } %> 					
						<td bgcolor="yellow" style="border: 1px solid LightGray;"><font color="white"><i>Pinging</i></font></td></tr>
					<%
					}   
					if(domainAllChildJson.has("APPS") && dom_json != null ) {
						domainAppsChild = new JSONObject(domainAllChildJson.get("APPS").toString());
						Iterator<String> domainAppKeys = domainAppsChild.keys();
						System.out.println("Domain APPS::"+domainAppsChild.toString());
						while (domainAppKeys.hasNext()) {
							domainApp = (String)domainAppKeys.next();
							domainAppAllChildJson = new JSONObject(domainAppsChild.get(domainApp).toString());
							domainAppChildDateTime = domainAppAllChildJson.get("DateTime").toString();
							domainAppChildProxyUrl = domainAppAllChildJson.get("ProxyURL").toString();
							domainAppChildHosts = domainAppAllChildJson.getJSONArray("HOSTS");
							if(domainAppAllChildJson.has("STATUS")) {
								domainAppChildMonStatus = domainAppAllChildJson.get("STATUS").toString();
								String bgColor = domainAppChildMonStatus.equalsIgnoreCase("up")?"green":"red";
						%>
							   <tr>
								<td style="border: 1px solid LightGray;"><%=domain%>/<%=domainApp%></td>
								<% 		
						   		if ( domainAppChildHosts.length() > 0 ) {
								%>
									<td style="border: 1px solid LightGray;"><%=domainAppChildHosts.length()%></td>
								<%} %> 	
								<td bgcolor="<%=bgColor%>" style="border: 1px solid LightGray;"><i><a href=<%=domainAppChildProxyUrl%> target="_blank"><font color="white"><%=domainAppChildMonStatus%></font></a></i></td></tr>
						<% 	} else {
						%>	
								<tr>
								<td style="border: 1px solid LightGray;"><%=domain%>/<%=domainApp%></td>
								<% 		
						   		if ( domainAppChildHosts.length() > 0 ) {
								%>
									<td style="border: 1px solid LightGray;"><%=domainAppChildHosts.length()%></td>
								<%} %> 
								<td bgcolor="yellow" style="border: 1px solid LightGray;"><i>Pinging</i></td></tr>
						<%
							}
						}
					}  
				}
			}
		} else if (lc_json == null) {
			if ( lc.get(lifecycle) instanceof JSONObject ) {
				domainJson = new JSONObject(lc.get(lifecycle).toString());
				Iterator<String> domainKeys = domainJson.keys();
				%>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td valign="top">
				<table border="0" class="pageText" style="border-collapse: collapse;"><tbody>
				<tr>
				<th style="border: 1px solid LightGray;" colspan="3"><a href="<%=reqURL%>?lc=<%=lifecycle%>"><%=lifecycle.toUpperCase()%></a></th>
				</tr>
				<tr>
				<% 
					if (app_json != null) {
				%>
					<th style="border: 1px solid LightGray;">Domain/App</th>
					<th style="border: 1px solid LightGray;">End-points</th>
					<th style="border: 1px solid LightGray;">Status</th>
				<%  } else if (app_json == null){ %>
					<th style="border: 1px solid LightGray;">Domain</th>
					<th style="border: 1px solid LightGray;">End-points</th>
					<th style="border: 1px solid LightGray;">Status</th>
				<%  } %>
				</tr>
				<% 
				while (domainKeys.hasNext()) {
					domain = (String)domainKeys.next();
					domainAllChildJson = new JSONObject(domainJson.get(domain).toString());
					domainChildDateTime = domainAllChildJson.get("DateTime").toString();
					domainChildProxyUrl = domainAllChildJson.get("ProxyURL").toString();
					domainChildHosts = domainAllChildJson.getJSONArray("HOSTS");
					System.out.println("HOSTS VALUE"+domainChildHosts.getString(0));
					System.out.println("HOSTS NOs:"+domainChildHosts.length());
					if(domainAllChildJson.has("STATUS") && dom_json == null) {
						domainChildMonStatus = domainAllChildJson.get("STATUS").toString();
						String bgColor = domainChildMonStatus.equalsIgnoreCase("up")?"green":"red";
				%>
						<tr>
						<% if (domainAllChildJson.has("APPS"))  { %>
							<td style="border: 1px solid LightGray;"><b><i><a href="<%=reqURL%>?lc=<%=lifecycle%>&dom=<%=domain%>"> <%=domain%></a></i></b></td>
						<% } else { %>
							<td style="border: 1px solid LightGray;"><%=domain%></td>	
						<% }
						   if ( domainChildHosts.length() > 0 ){
						%>
							<td style="border: 1px solid LightGray;"><%=domainChildHosts.length()%></td>
						<%} %>
						<td bgcolor="<%=bgColor%>" style="border: 1px solid LightGray;"><i><a href=<%=domainChildProxyUrl%> target="_blank"><font color="white"><%=domainChildMonStatus%></font></a></i></td></tr>
				<%
					} else if (dom_json == null){
						%>	
						<tr>
						<% if (domainAllChildJson.has("APPS"))  { %>
							<td style="border: 1px solid LightGray;"><b><i><a href="<%=reqURL%>?lc=<%=lifecycle%>&dom=<%=domain%>"> <%=domain%></a></i></b></td>
						<% } else { %>
							<td style="border: 1px solid LightGray;"><%=domain%></td>
						<% }
						   if ( domainChildHosts.length() > 0 ){
						%>
							<td style="border: 1px solid LightGray;"><%=domainChildHosts.length()%></td>
						 <%} %>
						<td bgcolor="yellow" style="border: 1px solid LightGray;"><font color="white"><i>Pinging</i></font></td></tr>
					<%
					}  
					if(domainAllChildJson.has("APPS") && dom_json != null ) {
						domainAppsChild = new JSONObject(domainAllChildJson.get("APPS").toString());
						Iterator<String> domainAppKeys = domainAppsChild.keys();
						System.out.println("Domain APPS::"+domainAppsChild.toString());
						while (domainAppKeys.hasNext()) {
							domainApp = (String)domainAppKeys.next();
							domainAppAllChildJson = new JSONObject(domainAppsChild.get(domainApp).toString());
							domainAppChildDateTime = domainAppAllChildJson.get("DateTime").toString();
							domainAppChildProxyUrl = domainAppAllChildJson.get("ProxyURL").toString();
							domainAppChildHosts = domainAppAllChildJson.getJSONArray("HOSTS");
							if(domainAppAllChildJson.has("STATUS")) {
								domainAppChildMonStatus = domainAppAllChildJson.get("STATUS").toString();
								String bgColor = domainAppChildMonStatus.equalsIgnoreCase("up")?"green":"red";
							%>
								<tr>
								<td style="border: 1px solid LightGray;"><%=domain%>/<%=domainApp%></td>
								<% 
								if ( domainAppChildHosts.length() > 0 ){
								%>
									<td style="border: 1px solid LightGray;"><%=domainAppChildHosts.length()%></td>
								<%} %>
								<td bgcolor="<%=bgColor%>" style="border: 1px solid LightGray;">
									<i><a href=<%=domainAppChildProxyUrl%> target="_blank"><font color="white"><%=domainAppChildMonStatus%></font></a></i></td></tr>
							<%} else{
							%>	
								<tr>
								<td style="border: 1px solid LightGray;"><%=domain%>/<%=domainApp%></td>
								<% 
								if ( domainAppChildHosts.length() > 0 ){
								%>
									<td style="border: 1px solid LightGray;"><%=domainAppChildHosts.length()%></td>
								<%} %>
								<td bgcolor="yellow" style="border: 1px solid LightGray;"><i>Pinging</i></td></tr>
							<%
						    }
						}
					} 
				}
			}
			
		}
	}
	%>	
	</tbody></table></td>
	<%	
	}
	%>
	</tr></table></div>
</body>
</html>