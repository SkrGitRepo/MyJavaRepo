<%@page import="org.json.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*,javax.xml.bind.DatatypeConverter,java.net.URL,java.net.MalformedURLException,
java.net.HttpURLConnection,com.google.gson.Gson,com.google.gson.GsonBuilder,org.json.JSONArray,org.json.JSONObject" %>

<% 
	String reqURI =  request.getRequestURI();
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/bootstrap.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-1.11.3/jquery-1.11.3.min.js"></script>
<script src="js/jquery-sparklines-2.1.2/jquery.sparkline.min.js"></script>
<title>SHOW BRMS EMAN STATUS</title>
</head>
<body>
<h2>Auto Refresh time in (5 Seconds)</h2>
<%
   // Set refresh, autoload time as 5 seconds
   response.setIntHeader("Refresh", 120);
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
   out.println("Crrent Time: " + CT + "\n");
   
   String webPage = "http://brms-test-5:7010/nprd1/brmsadmin/status/cvc";
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
	String lc = "lt";
	int lcleveLenth = lcLevel.length();
	JSONObject lt = lcLevel.getJSONObject(0);
	
	//String domain = null;
	//JSONObject jsonChildObject;
	
	String outputJson = gson.toJson(jsonResponse);
       
%>
<div>
<h3>JSON RESPONSE</h3>
<p>LENGHT ::<%=lcleveLenth%></p>
<%
Iterator<String> lcKeys  = lt.keys();
String lifecycle = null;
JSONObject jsonChildObject;
while (lcKeys.hasNext()) {
	lifecycle = lcKeys.next();
	
%>
<b>DOMAIN::: <%=lifecycle%></b>
<%
}

%>
<p>LT CONTENT ::<%=lt.toString()%></p>
<p><%=outputJson%></p>
</div>



</body>
</html>