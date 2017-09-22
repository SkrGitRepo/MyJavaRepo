<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.IOException"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	//String jsonStr = request.getParameter("jsonStr");
	BufferedReader br = null;
	
	try {
	    String sCurrentLine;
	    String fileContent = "";
	    br = new BufferedReader(new FileReader("/opt/brms/shared/scripts/brms_installed_ssl_cert_details.txt"));
	    while ((sCurrentLine = br.readLine()) != null) {
	        System.out.println(sCurrentLine);
	        fileContent += sCurrentLine+"\n";
	    }
	    if (fileContent != null) {
%>

<html>
	<title>BRMS SR Status</title>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title>BRMS SSL DETAILS</title>
		
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
	</head>

	<body onload="onWindowLoad()">
		<br>
		<div id="SearchSRStatusDiv">
		<fieldset style="width:60%;margin: auto;text-align:center;" >
			<legend class="pageText" align="left"><b>BRMS SERVER INSTALLED SSL CERT DETAILS:</b></legend>
			<!--  <form action="" name="searchForm" method="post">  -->
			<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff" class="brmsTable" width="100%">
				<tr>
					<td><textarea rows="50" cols="100" name="respoonse" readonly><%=fileContent%></textarea></td>
				</tr>
			</table>
			</fieldset>
		</div>			
</body>
</html>
<%
	    } 
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            if (br != null)br.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
%>  