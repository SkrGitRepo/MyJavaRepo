<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="com.sample.utility.BrmsJsonUtil"%>
<%@page import="org.apache.http.client.methods.HttpPost"%>
<%@page import="org.apache.http.entity.StringEntity"%>
<%@page import="org.apache.http.client.HttpClient"%> 
<%@page import="java.io.InputStreamReader"%>
<%@page import="org.apache.http.HttpResponse"%>
<%@page import="javax.xml.bind.DatatypeConverter"%>
<%@page import="com.cisco.dataconnect.process.Load" %>
<%@page import="com.app.PostIOSNotification" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
<title>BRMS SR Status</title>

<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title>EA OnRamp Process</title>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>

		<script type="text/javascript">
		
		   function assign(sType)
		   {
		      var val = sType;
		      window.location.replace("brms_send_ios_notification.jsp?sType="+val);
		   }

		</script>
		</head>

		<body>
		<br>
		<div id="SendNotificationDiv">
		<fieldset style="width:60%;margin: auto;text-align:center;" >
			<legend class="pageText" align="left"><b>Send Notification:</b></legend>
			<form action="" name="sendIOSNotification" method="post">
			<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff" class="brmsTable" width="100%">
				<tr>
					<th align="left" width="30%" >Send MONITORING Notification</th>
					<td align="left">
						<button type="button" onclick="javascript:assign('mon');">Send Notification</button>
					</td>
				</tr>
				<tr>
					<th align="left" width="30%" >Send STATUS Notification :</th>
					<td align="left">
						<button type="button" onclick="javascript:assign('status');">Send Notification</button> 
					</td>
				</tr>
				</table>
			</form>
			</fieldset>
			
		<%
        		String sendType=request.getParameter("sType");
			 	PostIOSNotification iosNotification = new PostIOSNotification();
				
        		if(sendType!=null && sendType.equalsIgnoreCase("mon")){
            		 
            		 String msg = iosNotification.sendNotification(sendType,"eabv", null);
            		 //out.println(msg);
            		 if (msg.equalsIgnoreCase("success")) {
            			 out.println("<br/><center><b>"+msg.toUpperCase()+": Notification sent for MONITORING... </b></center>");
            		 } else {
            			 out.println("<br/><center><b>"+msg.toUpperCase()+": Notification sent for MONITORING... </b></center>");
            		 }
            		 
        		} else if(sendType!=null && sendType.equalsIgnoreCase("status")) {
        			String msg = iosNotification.sendNotification(sendType,"gssc","DRD-1235492");
        			 
        			if (msg.equalsIgnoreCase("success")) {
            			 out.println("<br/><center><b>"+msg.toUpperCase()+": Notification sent for STATUS...  </b></center>");
            		} else {
            			out.println("<br/><center><b>"+msg.toUpperCase()+": Notification sent for STATUS... </b></center>");
            		}
        		} 
    	%>
		</div>			
</body>
</html>