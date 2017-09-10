<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.File"%>
<%@page import="com.cisco.brmspega.util.ts.TimeStampCreator"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String strURLPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();;//ParamUtil.getURLPathFromSession(request);
	String viewLatest = request.getParameter("vlatest");
	String inputFile = request.getParameter("inFile");
	String isFileSaved =  request.getParameter("fSave");
	
	
	System.out.println("inputFile"+isFileSaved);
	String idContent = null;
	String processScriptStr = null;
	String outputInd = "false";
	String outputStr = "";
	String scriptPath= null;
	String scriptName = null;
	scriptPath = "/opt/brms/shared/scripts/";
	scriptName = "view_brms_installed_certs_details.sh";
	String brmsCertFileName = "brms_installed_ssl_cert_details.txt";
	BufferedReader br = null;
	File fileExist;
	File certDetailsFile = new File(scriptPath + brmsCertFileName);
	
	if(inputFile != null) {
		fileExist = new File(inputFile);
	} else {
		fileExist = new File("/opt/brms/shared/test.txt");
	}
	boolean brmsCertFileExist = false; 
	

%>

<html>
<title>BRMS File Editor</title>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BRMS File Editor</title>

<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js">
	
	
	function onWindowLoad(){
		var vLatest = '<%=viewLatest%>'
		
		if (vLatest != ''  && vLatest == "y" ) {
			setInterval('blinkIt()',500);
			var divs = document.getElementsByTagName("div");
			
			if (divs[i].id == "viewCertDetails") {
				continue;
			}
			
			for(var i=0;i<divs.length;i++) {
				if (divs[i].id != "") {
					alert(divs[i].id)
					pingServer(divs[i].id);
				}
			}
		}
	}
	
	
</script>
</head>

<body>
	
	<br>
	<div id="viewCertDetails">
		<fieldset style="width: 60%; margin: auto; text-align: center;">
			<legend class="pageText" align="left">
				<b>View/Edit File:</b>
			</legend>
			
			<form action="" name="searchFileForm" method="get">
				<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff"
					class="brmsTable" width="100%">
					<tr>
						<th align="right" width="50%" >Search File: </th>
						<td align="left" width="50%" >
						<%if(inputFile != null) {%>
							<input type="text" id="infile" name="inFile" value="<%=inputFile%>" size="50" >
						<%} else {%>
							<input type="text" id="inFile" name="inFile" value="" size="50" placeholder="Enter File with path">
						<%} %>
						
						&nbsp;&nbsp;<input align="middle" type="submit" value="Search" id="Submit" >
						</td>
					</tr>
				</table>
			</form>
			<br/><br/>
			<%
				if (fileExist.exists()) {
					brmsCertFileExist = true;
					try {
						String sCurrentLine;
						String fileContent = "";
						br = new BufferedReader(new FileReader(inputFile));
						while ((sCurrentLine = br.readLine()) != null) {
							//System.out.println(sCurrentLine);
							fileContent += sCurrentLine + "\n";
						}
						if (fileContent != null) {
			%>
			
			
			<form action="<%=strURLPath%>/savefile" name="editForm" method="post">
			<% if (isFileSaved != null && isFileSaved.equalsIgnoreCase("Y"))  {%>
				<center><p><b><font color="green">File saved successfully. </font></b></p></center>
			<% } else if (isFileSaved != null && isFileSaved.equalsIgnoreCase("N")) {
			%>
				<center><p><b><font color="red">File could not be saved.</font></b></p></center>
			<%} %>
			<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff"
				class="brmsTable" width="100%">
				
				<tr>
					<td colspan="2" align="center">
						<input align="middle" type="submit" value="Save" id="Save">
						&nbsp;&nbsp;
					</td>
				</tr>
				<tr>
					<td><textarea rows="50" cols="150" name="fileContent" ><%=fileContent%></textarea>
					<input type="hidden" value="<%=inputFile%>" id="fileName" name="fileName">
					</td>
					
				</tr>
				<tr>
					<td colspan="2" align="center">
						<input align="middle" type="submit" value="Save" id="Save">
						&nbsp;&nbsp;
					</td>
				</tr>
			</table>
			</form>
			<%
				}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (br != null)
								br.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
			}
			%>
		</fieldset>
	</div>

</body>
</html>
