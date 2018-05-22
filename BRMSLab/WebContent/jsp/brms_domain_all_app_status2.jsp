<%@page import="org.json.JSONObject"%>
<%@page import="com.sample.utility.BrmsJsonUtil"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
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
   String cotextPath = request.getContextPath();
   String domBgColor = null;
   String appBgColor = null;
   
 
   System.out.println("Context PATH:"+cotextPath);
   
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
	<br/><p><b>Piya Mere Bhole Bhole : </b>&nbsp; <audio controls><source src="../audio/Piya_More.mp3"  type="audio/mpeg">Your browser does not support the audio element.</audio></p>
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
				<th bgcolor="grey" style="border: 1px solid LightGray;" colspan="3"><a href="<%=reqURL%>?lc=<%=lifecycle%>"><font color="white"><%=lifecycle.toUpperCase()%></font></a></th>
				</tr>
				<tr>
				<% 
					if (app_json != null) {
				%>
					<th bgcolor="grey" style="border: 1px solid LightGray;"><font color="white">Domain</font></th>
					<th bgcolor="grey" style="border: 1px solid LightGray;"><font color="white">Status</font></th>
					<th bgcolor="grey" style="border: 1px solid LightGray;"><font color="white">Apps Status</font></th>
				<%  } else if (app_json == null ) { %>
					<th bgcolor="grey" style="border: 1px solid LightGray;"><font color="white">Domain</font></th>
					<th bgcolor="grey" style="border: 1px solid LightGray;"><font color="white">Status</font></th>
					<th bgcolor="grey" style="border: 1px solid LightGray;"><font color="white">App&nbsp;|&nbsp;Status</font></th>
				<%  } %>
				</tr>
				<% 
				while (domainKeys.hasNext()) {
					domain = (String)domainKeys.next();
					domainAllChildJson = new JSONObject(domainJson.get(domain).toString());
					domainChildDateTime = domainAllChildJson.get("DateTime").toString();
					domainChildProxyUrl = domainAllChildJson.get("ProxyURL").toString();
					domainChildHosts = domainAllChildJson.getJSONArray("HOSTS");
					
					if(domainAllChildJson.has("STATUS")) {
						domainChildMonStatus = domainAllChildJson.get("STATUS").toString();
			            domBgColor = domainChildMonStatus.equalsIgnoreCase("up")?"green":"red";
				%>
						<tr>
						<td bgcolor="grey" style="border: 1px solid LightGray;"><b><i><a href="<%=reqURL%>?lc=<%=lifecycle%>&dom=<%=domain%>"><font color="white"> <%=domain%></font></a></i></b></td>
						<td bgcolor="<%=domBgColor%>" style="border: 1px solid LightGray;"><i><a href=<%=domainChildProxyUrl%> target="_blank"><font color="white"><%=domainChildMonStatus%></font></a></i></td>
						<td>
						<table>
						<tr>
						<% if (domainAllChildJson.has("APPS"))  {
							domainAppsChild = new JSONObject(domainAllChildJson.get("APPS").toString());
							Iterator<String> domainAppKeys = domainAppsChild.keys();
							while (domainAppKeys.hasNext()) {
								domainApp = (String)domainAppKeys.next();
								domainAppAllChildJson = new JSONObject(domainAppsChild.get(domainApp).toString());
								domainAppChildDateTime = domainAppAllChildJson.get("DateTime").toString();
								domainAppChildProxyUrl = domainAppAllChildJson.get("ProxyURL").toString();
								domainAppChildHosts = domainAppAllChildJson.getJSONArray("HOSTS");
								if(domainAppAllChildJson.has("STATUS")) {
									domainAppChildMonStatus = domainAppAllChildJson.get("STATUS").toString();
									appBgColor = domainAppChildMonStatus.equalsIgnoreCase("up")?"green":"red";
						%>
									<td bgcolor="<%=appBgColor%>" style="border: 1px solid LightGray;"><i><font color="white"><%=domainApp%></font> &nbsp;| &nbsp;<a href=<%=domainAppChildProxyUrl%> target="_blank"><font color="white"><%=domainAppChildMonStatus%></font></a></i></td>
						<%		} else { %>
									<td bgcolor="yellow" style="border: 1px solid LightGray;"><i><%=domainApp%> &nbsp;| &nbsp; Pinging</i></td>
						<%	    }	
						
							}
						%>
							
						<% } else { %>
								<td bgcolor="yellow" style="border: 1px solid LightGray;"><i>NA &nbsp;| &nbsp; NA</i></td>
	
						<% } 
							%>
						</tr>
						</table>
						</td>
						</tr>
				<%
					} else {
				%>		
						<tr>
						<td bgcolor="grey" style="border: 1px solid LightGray;"><b><i><a href="<%=reqURL%>?lc=<%=lifecycle%>&dom=<%=domain%>"><font color="white"> <%=domain%></font></a></i></b></td>
						<td bgcolor="yellow" style="border: 1px solid LightGray;"><i>Pinging</i></td>
						<td>
						<table>
						<tr>
						<% if (domainAllChildJson.has("APPS"))  {
							domainAppsChild = new JSONObject(domainAllChildJson.get("APPS").toString());
							Iterator<String> domainAppKeys = domainAppsChild.keys();
							while (domainAppKeys.hasNext()) {
								domainApp = (String)domainAppKeys.next();
								domainAppAllChildJson = new JSONObject(domainAppsChild.get(domainApp).toString());
								domainAppChildDateTime = domainAppAllChildJson.get("DateTime").toString();
								domainAppChildProxyUrl = domainAppAllChildJson.get("ProxyURL").toString();
								domainAppChildHosts = domainAppAllChildJson.getJSONArray("HOSTS");
								if(domainAppAllChildJson.has("STATUS")) {
									domainAppChildMonStatus = domainAppAllChildJson.get("STATUS").toString();
									appBgColor = domainAppChildMonStatus.equalsIgnoreCase("up")?"green":"red";
						%>
									<td bgcolor="<%=appBgColor%>" style="border: 1px solid LightGray;"><i><font color="white"><%=domainApp%></font> &nbsp;| &nbsp;<a href=<%=domainAppChildProxyUrl%> target="_blank"><font color="white"><%=domainAppChildMonStatus%></font></a></i></td>
						<%		} else { %>
									<td bgcolor="yellow" style="border: 1px solid LightGray;"><i><%=domainApp%> &nbsp;| &nbsp; Pinging</i></td>
						<%	    }	
						
							}
						%>
							
						<% } else { %>
								<td bgcolor="yellow" style="border: 1px solid LightGray;"><i>NA &nbsp;| &nbsp; NA</i></td>
	
						<% } 
							%>
						</tr>
						</table>
						</td>
						</tr>
						
						
				<% 	} 
						
					}  
				}
		} else if (lc_json == null) {
			
			if ( lc.get(lifecycle) instanceof JSONObject ) {
				domainJson = new JSONObject(lc.get(lifecycle).toString());
				Iterator<String> domainKeys = domainJson.keys();
				%>
				<td valign="top" style="border: 1px solid LightGray;">
				<table border="0" class="pageText" style="border-collapse: collapse;">
				<tr>
				<th bgcolor="grey" style="border: 1px solid LightGray;" colspan="3"><a href="<%=reqURL%>?lc=<%=lifecycle%>"><font color="white"><%=lifecycle.toUpperCase()%></font></a></th>
				</tr>
				<tr>
				<% 
					if (app_json != null) {
				%>
					<th bgcolor="grey" style="border: 1px solid LightGray;"><font color="white">Domain </font> </th>
					<th bgcolor="grey" style="border: 1px solid LightGray;"><font color="white">Status</font></th>
					<th bgcolor="grey" style="border: 1px solid LightGray;"><font color="white">Apps Status</font></th>
				<%  } else if (app_json == null ) { %>
					<th bgcolor="grey" style="border: 1px solid LightGray;"><font color="white">Domain</font></th>
					<th bgcolor="grey" style="border: 1px solid LightGray;"><font color="white">Status</font></th>
					<th bgcolor="grey" style="border: 1px solid LightGray;"><font color="white">App&nbsp;|&nbsp;Status</font></th>
				<%  } %>
				</tr>
				<% 
				while (domainKeys.hasNext()) {
					domain = (String)domainKeys.next();
					domainAllChildJson = new JSONObject(domainJson.get(domain).toString());
					domainChildDateTime = domainAllChildJson.get("DateTime").toString();
					domainChildProxyUrl = domainAllChildJson.get("ProxyURL").toString();
					domainChildHosts = domainAllChildJson.getJSONArray("HOSTS");
					
					if(domainAllChildJson.has("STATUS") ) {
						domainChildMonStatus = domainAllChildJson.get("STATUS").toString();
			            domBgColor = domainChildMonStatus.equalsIgnoreCase("up")?"green":"red";
				%>
						<tr>
						<td bgcolor="grey" style="border: 1px solid LightGray;"><b><i><a href="<%=reqURL%>?lc=<%=lifecycle%>&dom=<%=domain%>"><font color="white"> <%=domain%></font></a></i></b></td>
						<td bgcolor="<%=domBgColor%>" style="border: 1px solid LightGray;"><i><a href=<%=domainChildProxyUrl%> target="_blank"><font color="white"><%=domainChildMonStatus%></font></a></i></td>
						<td>
						<table>
						<tr>
						<% if (domainAllChildJson.has("APPS"))  {
							domainAppsChild = new JSONObject(domainAllChildJson.get("APPS").toString());
							Iterator<String> domainAppKeys = domainAppsChild.keys();
							while (domainAppKeys.hasNext()) {
								domainApp = (String)domainAppKeys.next();
								domainAppAllChildJson = new JSONObject(domainAppsChild.get(domainApp).toString());
								domainAppChildDateTime = domainAppAllChildJson.get("DateTime").toString();
								domainAppChildProxyUrl = domainAppAllChildJson.get("ProxyURL").toString();
								domainAppChildHosts = domainAppAllChildJson.getJSONArray("HOSTS");
								if(domainAppAllChildJson.has("STATUS")) {
									domainAppChildMonStatus = domainAppAllChildJson.get("STATUS").toString();
									appBgColor = domainAppChildMonStatus.equalsIgnoreCase("up")?"green":"red";
						%>
									<td bgcolor="<%=appBgColor%>" style="border: 1px solid LightGray;"><font color="white"><%=domainApp%></font> &nbsp;| &nbsp;<a href=<%=domainAppChildProxyUrl%> target="_blank"><font color="white"><%=domainAppChildMonStatus%></font></a></i></td>
						<%		} else { %>
									<td bgcolor="yellow" style="border: 1px solid LightGray;"><i><%=domainApp%> &nbsp;| &nbsp; Pinging</i></td>
						<%	    }	
						
							}
						%>
							
						<% } else { %>
								<td bgcolor="yellow" style="border: 1px solid LightGray;"><i>NA &nbsp;| &nbsp; NA</i></td>
	
						<% } 
							%>
						</tr>
						</table>
						</td>
						</tr>
				<%
					} else {
				%>		
						<tr>
						<td bgcolor="grey" style="border: 1px solid LightGray;"><b><i><a href="<%=reqURL%>?lc=<%=lifecycle%>&dom=<%=domain%>"><font color="white"> <%=domain%></font></a></i></b></td>
						<td bgcolor="yellow" style="border: 1px solid LightGray;"><i>Pinging</i></td>
						<td>
						<table>
						<tr>
						<% if (domainAllChildJson.has("APPS"))  {
							domainAppsChild = new JSONObject(domainAllChildJson.get("APPS").toString());
							Iterator<String> domainAppKeys = domainAppsChild.keys();
							while (domainAppKeys.hasNext()) {
								domainApp = (String)domainAppKeys.next();
								domainAppAllChildJson = new JSONObject(domainAppsChild.get(domainApp).toString());
								domainAppChildDateTime = domainAppAllChildJson.get("DateTime").toString();
								domainAppChildProxyUrl = domainAppAllChildJson.get("ProxyURL").toString();
								domainAppChildHosts = domainAppAllChildJson.getJSONArray("HOSTS");
								if(domainAppAllChildJson.has("STATUS")) {
									domainAppChildMonStatus = domainAppAllChildJson.get("STATUS").toString();
									appBgColor = domainAppChildMonStatus.equalsIgnoreCase("up")?"green":"red";
						%>
									<td bgcolor="<%=appBgColor%>" style="border: 1px solid LightGray;"><i><font color="white"><%=domainApp%></font> &nbsp;| &nbsp;<a href=<%=domainAppChildProxyUrl%> target="_blank"><font color="white"><%=domainAppChildMonStatus%></font></a></i></td>
						<%		} else { %>
									<td bgcolor="yellow" style="border: 1px solid LightGray;"><i><%=domainApp%> &nbsp;| &nbsp; Pinging</i></td>
						<%	    }	
						
							}
						%>
							
						<% } else { %>
								<td bgcolor="yellow" style="border: 1px solid LightGray;"><i>NA &nbsp;| &nbsp; NA</i></td>
	
						<% } 
							%>
						</tr>
						</table>
						</td>
						</tr>
						
						
				<% 	} 
						
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