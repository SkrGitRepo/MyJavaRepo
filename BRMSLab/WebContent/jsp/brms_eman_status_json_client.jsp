<%@page import="org.json.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*,javax.xml.bind.DatatypeConverter,java.net.URL,java.net.MalformedURLException,
java.net.HttpURLConnection,com.google.gson.Gson,com.google.gson.GsonBuilder,org.json.JSONArray,org.json.JSONObject" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Get EMAN Response</title>
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
   String webPage = "http://localhost:8085/J2EESampleProject/status";
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
	JSONArray lcLevel = new JSONArray(jsonResponse);
	//String lc = null;
	int lcleveLenth = lcLevel.length();
	%>
	<h3>JSON RESPONSE JSP VIEW</h3>
	<!--  <p>LENGHT ::<%=lcleveLenth%></p>  -->
	<div>
	<table border="1"><tr><th>Lifecycle</th><th>Domain</th><th>Status</th></tr>
	<% 
	for(int i=0; i < lcLevel.length();i++) {
		JSONObject lc = lcLevel.getJSONObject(i);
		String outputJson = gson.toJson(jsonResponse);
	%>
	
	
	<%
		Iterator<String> lcKeys = lc.keys();
		String lifecycle = null;
		String domain = null;
		String domainStatus = null;
		JSONObject domainJson=null;
		JSONObject domainAllChildJson = null;
		String domainChildDateTime= null;
		String domainChildMonStatus= null;
		String domainChildProxyUrl =  null;
		JSONObject domainAppsChild =  null;
		String domainChildApps =  null;
		
		while (lcKeys.hasNext()) {
			lifecycle = (String)lcKeys.next();
			if ( lc.get(lifecycle) instanceof JSONObject ) {
				domainJson = new JSONObject(lc.get(lifecycle).toString());
				Iterator<String> domainKeys = domainJson.keys();
				
				while (domainKeys.hasNext()) {
					domain = (String)domainKeys.next();
					domainAllChildJson = new JSONObject(domainJson.get(domain).toString());
					domainChildDateTime = domainAllChildJson.get("DateTime").toString();
					domainChildProxyUrl = domainAllChildJson.get("ProxyURL").toString();
					if(domainAllChildJson.has("STATUS")) {
						domainChildMonStatus = domainAllChildJson.get("STATUS").toString();
				%>
						<tr><td></b><%=lifecycle.toUpperCase()%></td>
						<td><%=domain%></td>
						<td><a href=<%=domainChildProxyUrl%> ><%=domainChildMonStatus%></a></td></tr>
				<%
					} else {
				%>	
						<tr><td></b><%=lifecycle.toUpperCase()%></td>
						<td><%=domain%></td>
						<td>NA</td></tr>
				<% }
				   if(domainAllChildJson.has("APPS")) {
					   domainAppsChild = new JSONObject(domainAllChildJson.get("APPS"));
					   System.out.println("Domain APPS::"+domainAppsChild.toString());
					}
				}
			}			
		}
	%>	
	
	<%	
	}
	%>
	</table></div>
</body>
</html>