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
 
	%>
	
	

<h3><a href="<%=request.getContextPath()%>/emanResponse?lc=dev">Click here for DEV Response</a></h3>
<%  String devMessage = (String) request.getAttribute("urlResponse");
	String devStatusCode = (String)request.getAttribute("statusCode");
	String devLifecycle = (String)request.getAttribute("lc");
	String resContent = (String)request.getAttribute("resContent");
 if (devMessage != null && devLifecycle.equals("dev")) {
%>
	<h3> Response from DEV :</h3> 
	<b>RESULT :: </b><font color="green"><%=devMessage%></font><br/>
	<b>STATUS CODE :: </b><%=devStatusCode %><br/>
	<!--  <b>CONTENT :: </b><%=resContent %><br/>  -->
<%} %>
<br/>

<h3><a href="<%=request.getContextPath()%>/emanResponse?lc=stage">Click here for STAGE Response</a></h3>
<%  String stgMessage = (String) request.getAttribute("urlResponse");
	String stgStatusCode = (String)request.getAttribute("statusCode");
	String stgLifecycle = (String)request.getAttribute("lc");
	String resContent1 = (String)request.getAttribute("resContent");
 if (stgMessage != null && stgLifecycle.equals("stage")) {
%>
	<h3> Response from STAGE :</h3> 
	<b>RESULT:: </b><font color="green"><%=stgMessage%></font><br/>
	<b>STATUS CODE :: </b><%=stgStatusCode %><br/>
	<!--  <b>CONTENT :: </b><%=resContent1 %><br/>  -->
<%} %>
<br/>
<h3><a href="<%=request.getContextPath()%>/emanResponse?lc=poc">Click here for POC Response</a></h3>
<%  String pocMessage = (String) request.getAttribute("urlResponse");
	String pocStatusCode = (String)request.getAttribute("statusCode");
	String pocLifecycle = (String)request.getAttribute("lc");
	String resContent2 = (String)request.getAttribute("resContent");
 if (pocMessage != null && pocLifecycle.equals("poc")) {
	 String fontColor = null;
	 if(pocMessage.equals("SUCCESS")){
		 fontColor = "GREEN";
	 } else {
		 fontColor = "RED";
	 }
%>
	<h3> Response from POC :</h3> 
	<b>RESULT :: </b><font color=<%=fontColor%>><%=pocMessage%></font><br/>
	<b>STATUS CODE :: </b><%=pocStatusCode %><br/>
	 <!--  <b>CONTENT :: </b><%=resContent2 %><br/>  -->
<%} %>

<h3><a href="<%=request.getContextPath()%>/emanResponse?lc=prd">Click here for PRD Response</a></h3>
<%  String prdMessage = (String) request.getAttribute("urlResponse");
	String prdStatusCode = (String)request.getAttribute("statusCode");
	String prdLifecycle = (String)request.getAttribute("lc");	
 if (prdMessage != null && prdLifecycle.equals("prd")) {
	 String fontColor = null;
	 if(prdMessage.equals("SUCCESS")){
		 fontColor = "GREEN";
	 } else {
		 fontColor = "RED";
	 }
%>
	<h3> Response from PROD :</h3> 
	<b>RESULT :: </b><font color=<%=fontColor%>><%=prdMessage%></font><br/>
	<b>STATUS CODE :: </b><%=prdStatusCode %><br/>
<%} %>


<h3><a href="<%=request.getContextPath()%>/redirect">REDIRECT TO IBPM STAGE</a></h3>
</body>
</html>