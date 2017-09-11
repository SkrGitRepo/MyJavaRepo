<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileInputStream" %>
<%@page import="java.security.KeyStore"%>
<%@page import="java.security.cert.X509Certificate"%>
<%@page import="java.util.Enumeration"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View BRMS Install Certs</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<script type="text/javascript"
src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
</head>

<body>

<div id="reportDBConfig">
	<fieldset style="width: 70%; margin: auto; text-align: center;">
		<legend class="pageText" align="left">
			<b>BRMS Installed CERT Report:</b>
		</legend>
		<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff"
				class="brmsTable" width="100%">
				<tr>
					<th align="center" width="30%">Cert No.</th>
					<th align="center" width="30%">Alias Name</th>
					<th align="center" width="30%">Cert Owner</th>
					<th align="center" width="30%">Installed Date</th>
					<th align="center" width="30%">Valid From</th>
					<th align="center" width="30%">Expiring On</th>
					<th align="center" width="30%">Provider</th>
				</tr>
				<%
				
				try {
			        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			        keystore.load(new FileInputStream("/Users/sumkuma2/Desktop/brmsclient.jks"), "password".toCharArray());
			        Enumeration<String> aliases = keystore.aliases();
			        int count = 1;
			        while(aliases.hasMoreElements()){
			            String alias = aliases.nextElement();
			            if(keystore.getCertificate(alias).getType().equals("X.509")){
			            	 //String certOwner = ((X509Certificate) keystore.getCertificate(alias)).getSubjectDN();
			     %>
			     		<tr>
							<td align="center" width="10%" ><%=count%></td>
							<td align="center" width="20%"><%=alias%></td>
							<td align="center" width="30%"><%=((X509Certificate) keystore.getCertificate(alias)).getSubjectDN()%></td>
							<td align="center" width="40%"><%=keystore.getCreationDate(alias)%></td>
							<td align="center" width="40%"><%=((X509Certificate) keystore.getCertificate(alias)).getNotBefore()%></td>
							<td align="center" width="40%"><%=((X509Certificate) keystore.getCertificate(alias)).getNotAfter()%></td>
							<td align="center" width="40%"><%=keystore.getProvider()%></td>
						</tr>
			     <% 
			     
			     			System.out.println(alias + " Subject DSN: " + ((X509Certificate) keystore.getCertificate(alias)).getSubjectDN());
			            	System.out.println(alias + " DSN: " + ((X509Certificate) keystore.getCertificate(alias)).getIssuerDN());
			            	System.out.println(alias + " Valid From: " + ((X509Certificate) keystore.getCertificate(alias)).getNotBefore());
			                System.out.println(alias + " Expires On: " + ((X509Certificate) keystore.getCertificate(alias)).getNotAfter());
			                System.out.println(alias + " Provider: " +  keystore.getProvider());
			                System.out.println(alias + " CreationDate: " +  keystore.getCreationDate(alias));
			                //System.out.println(alias + " CERTIFICATE: " +  keystore.getCertificate(alias));
			                //( (X50Certificate) keystore.getCertificate(alias)).getNotBefore());
			                count+= count +1;
			            }
			        }
			    } catch (Exception e) {
			        e.printStackTrace();
			    }
				
				
				%>
		</table>
		</fieldset>
		</div>


</body>
</html>