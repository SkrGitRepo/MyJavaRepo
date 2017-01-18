<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="org.json.JSONObject"%>
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
<!-- <h2>Auto Refresh time in (5 Seconds)</h2> -->
<%
   // Set refresh, autoload time as 5 seconds
   //response.setIntHeader("Refresh", 120);
   //Get current time
   Calendar calendar = new GregorianCalendar();
   String am_pm;
   int hour = calendar.get(Calendar.HOUR);
   int minute = calendar.get(Calendar.MINUTE);
   int second = calendar.get(Calendar.SECOND);
   if(calendar.get(Calendar.AM_PM) == 0)
      am_pm = "AM";
   else
      am_pm = "PM";
   String CT = hour+":"+ minute +":"+ second +" "+ am_pm;
   //out.println("Crrent Time: " + CT + "\n");
   
   //String webPage = "http://brms-test-5:7010/nprd1/brmsadmin/status/cvc";
   String lc_json = request.getParameter("lc");
   String dom_json = request.getParameter("dom");
   String app_json = request.getParameter("app");
   String host_json =  request.getParameter("hosts");
   StringBuffer reqURL = request.getRequestURL();
   String serverName = request.getServerName();
   int serverPort = request.getServerPort();
   String reqURI = request.getRequestURI();
   String cotnextPath = request.getContextPath();
   //out.println("URI: " + reqURI);
   //out.println("SERVER NAME: " + serverName);
   //out.println("SERVER PORT: " + serverPort);
   //out.println("SERVER CONTEXT PATH: " + cotnextPath);
   
   String webPage = "http://"+serverName+":"+serverPort+cotnextPath;
   
   
   //String webPage = "http://localhost:8085/J2EESampleProject/status";
   if (dom_json != null && app_json == null) {
	   if (lc_json != null) {
		   String newLC  = null;
		   if (lc_json.equalsIgnoreCase("prod"))
			   newLC="prd";
		   if (lc_json.equalsIgnoreCase("stage"))
			   newLC="stg";
   			webPage = "http://"+serverName+":"+serverPort+cotnextPath+"/status/"+newLC+"/"+dom_json;
	   } else {
		   	webPage = "http://"+serverName+":"+serverPort+cotnextPath+"/status/all/"+dom_json;
	   }
   } else if (dom_json != null && app_json != null) {
	   if (lc_json != null) {
		   String newLC  = null;
		   if (lc_json.equalsIgnoreCase("prod"))
			   newLC="prd";
		   if (lc_json.equalsIgnoreCase("stage"))
			   newLC="stg";
	   		webPage = "http://"+serverName+":"+serverPort+cotnextPath+"/status/"+newLC+"/"+dom_json+"/"+app_json;
	   } else {
		   	webPage = "http://"+serverName+":"+serverPort+cotnextPath+"/status/all/"+dom_json+"/"+app_json;
	   }
   } else {
	   if (lc_json != null) {
		   String newLC  = null;
		   if (lc_json.equalsIgnoreCase("prod"))
			   newLC="prd";
		   if (lc_json.equalsIgnoreCase("stage"))
			   newLC="stg";
	   		//webPage = "http://"+serverName+":"+serverPort+cotnextPath+"/status/"+newLC+"/"+dom_json+"/"+app_json;
	   		webPage = "http://"+serverName+":"+serverPort+cotnextPath+"/status/"+newLC;
	   } else {
	   		webPage = "http://"+serverName+":"+serverPort+cotnextPath+"/status/all";
	   }
   }
   out.println("FINAL URL: " + webPage);
   String name = "brm.gen";
   String password = "brmGen123";
   String jsonResponse = null;
   String authString = name + ":" + password;
   String decodedValue = DatatypeConverter.printBase64Binary(authString.getBytes("UTF-8"));
   String authStringEnc = decodedValue;
   URL url = new URL(webPage);
   HttpURLConnection httpCon = (HttpURLConnection)url.openConnection();
   httpCon.setRequestProperty("Authorization", "Basic " + authStringEnc);
   int statusCode = httpCon.getResponseCode();
   InputStream is = httpCon.getInputStream();
   InputStreamReader isr = new InputStreamReader(is);
   
   Gson gson = new GsonBuilder().setPrettyPrinting().create();
   //String outputJson = gson.toJson(superJsonResponse);
   //String resContent  =  http.getContentEncoding().toString();
	
	int numCharsRead;
	char[] charArray = new char[1024];
	StringBuffer sb = new StringBuffer();
	while ((numCharsRead = isr.read(charArray)) > 0) {
		sb.append(charArray, 0, numCharsRead);
	}
	jsonResponse = sb.toString();
	
	String outputJson1 = gson.toJson(jsonResponse);
	//out.print(outputJson1);
	
	JSONArray lcLevel = new JSONArray(jsonResponse);
	//String lc = null;
	int lcleveLenth = lcLevel.length();
	%>
	<h3>DOMAIN/APP EMAN PING STATUS:</h3>
	<!-- <div><table><tr>
		<th><a href="<%=reqURL%>">ALL</a></th>&nbsp;&nbsp;&nbsp;&nbsp;
		<th><a href="<%=reqURL%>?lc=prod">PROD</a></th>&nbsp;&nbsp;&nbsp;&nbsp;
		<th><a href="<%=reqURL%>?lc=stage">STAGE</a></th>&nbsp;&nbsp;&nbsp;&nbsp;
		<th><a href="<%=reqURL%>?lc=dev">DEV</a></th>&nbsp;&nbsp;&nbsp;&nbsp;
		<th><a href="<%=reqURL%>?lc=poc">POC</a></th>&nbsp;&nbsp;&nbsp;&nbsp;
		<th><a href="<%=reqURL%>?lc=lt">LT</a></th>&nbsp;&nbsp;&nbsp;&nbsp;
	</tr></table>
	</div>  -->
	<div>
	<table border="0" class="pageText" style="border-collapse: collapse;"><tr>
	<% 
	for(int i=0; i < lcLevel.length();i++) {
		JSONObject lc = lcLevel.getJSONObject(i);
	%>
	<%
		String outputJson = gson.toJson(jsonResponse);
	%>
	<%
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
					//out.print("<H3>HOST VALUE"+domainChildHosts.getString(0)+"</H3>");
					//out.print("<H3>HOST VALUE"+domainChildHosts.getString(1)+"</H3>");
					System.out.println("HOSTS VALUE"+domainChildHosts.getString(0));
					System.out.println("HOSTS NOs:"+domainChildHosts.length());
					//String bgColor = "red";
					
					if(domainAllChildJson.has("STATUS") && dom_json == null) {
						domainChildMonStatus = domainAllChildJson.get("STATUS").toString();
						String bgColor = domainChildMonStatus.equalsIgnoreCase("up")?"green":"red";
				%>
						<tr>
						<!-- <td><a href="<%=reqURL%>?lc=<%=lifecycle%>"><%=lifecycle.toUpperCase()%></a></td>  -->
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
						<!-- <td><a href="<%=reqURL%>?lc=<%=lifecycle%>"><%=lifecycle.toUpperCase()%></a></td>  -->
						<% if (domainAllChildJson.has("APPS"))  { %>
							<td style="border: 1px solid LightGray;" ><b><i><a href="<%=reqURL%>?lc=<%=lifecycle%>&dom=<%=domain%>"> <%=domain%></a></i></b></td>
						<% } else { %>
							<td style="border: 1px solid LightGray;"><%=domain%></td>
						<% }
						   if ( domainChildHosts.length() > 0 ) {
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
							//String appBgColor = "red";
							if(domainAppAllChildJson.has("STATUS")) {
								domainAppChildMonStatus = domainAppAllChildJson.get("STATUS").toString();
								String bgColor = domainAppChildMonStatus.equalsIgnoreCase("up")?"green":"red";
						%>
							   <tr>
							   <!-- <td><a href="<%=reqURL%>?lc=<%=lifecycle%>"><%=lifecycle.toUpperCase()%></a></td>  -->
								<td style="border: 1px solid LightGray;"><%=domain%>/<%=domainApp%></td>
								<% 		
						   		if ( domainAppChildHosts.length() > 0 ) {
								%>
									<td style="border: 1px solid LightGray;"><%=domainAppChildHosts.length()%></td>
								<%} %> 	
								<td bgcolor="<%=bgColor%>" style="border: 1px solid LightGray;"><i><a href=<%=domainAppChildProxyUrl%> target="_blank"><font color="white"><%=domainAppChildMonStatus%></font></a></i></td></tr>
								
						<% 	} else{
						%>	
								<tr>
								<!-- <td><a href="<%=reqURL%>?lc=<%=lifecycle%>"><%=lifecycle.toUpperCase()%></a></td>  -->
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
					//out.print("<H3>HOST VALUE"+domainChildHosts.getString(0)+"</H3>");
					//out.print("<H3>HOST VALUE"+domainChildHosts.getString(1)+"</H3>");
					System.out.println("HOSTS VALUE"+domainChildHosts.getString(0));
					System.out.println("HOSTS NOs:"+domainChildHosts.length());
					//String bgColor = "red";
					if(domainAllChildJson.has("STATUS") && dom_json == null) {
						domainChildMonStatus = domainAllChildJson.get("STATUS").toString();
						String bgColor = domainChildMonStatus.equalsIgnoreCase("up")?"green":"red";
				%>
						<tr>
						<!-- <td><a href="<%=reqURL%>?lc=<%=lifecycle%>"><%=lifecycle.toUpperCase()%></a></td>  -->
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
						<!--  <td><a href="<%=reqURL%>?lc=<%=lifecycle%>"><%=lifecycle.toUpperCase()%></a></td> -->
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
							//String appBgColor = "red";
							if(domainAppAllChildJson.has("STATUS")) {
								domainAppChildMonStatus = domainAppAllChildJson.get("STATUS").toString();
								String bgColor = domainAppChildMonStatus.equalsIgnoreCase("up")?"green":"red";
							%>
								<tr>
								<!-- <td><a href="<%=reqURL%>?lc=<%=lifecycle%>"><%=lifecycle.toUpperCase()%></a></td>  -->
								<td style="border: 1px solid LightGray;"><%=domain%>/<%=domainApp%></td>
								<% 
								if ( domainAppChildHosts.length() > 0 ){
								%>
									<td style="border: 1px solid LightGray;"><%=domainAppChildHosts.length()%></td>
								<%} %>
								<td bgcolor="<%=bgColor%>" style="border: 1px solid LightGray;"><i><a href=<%=domainAppChildProxyUrl%> target="_blank"><font color="white"><%=domainAppChildMonStatus%></font></a></i></td></tr>
							<%} else{
							%>	
								<tr>
								<!-- <td><a href="<%=reqURL%>?lc=<%=lifecycle%>"><%=lifecycle.toUpperCase()%></a></td> -->
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