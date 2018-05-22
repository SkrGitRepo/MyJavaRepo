<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.File"%>
<%@page import="java.nio.file.Files"%>
<%@page import="java.nio.file.Path"%>
<%@page import="java.nio.file.Paths"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.cisco.brmspega.util.ts.TimeStampCreator"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String strURLPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();;//ParamUtil.getURLPathFromSession(request);
	String viewLatest = request.getParameter("vlatest");
	String inputFile = request.getParameter("inFile");
	String isFileSaved =  request.getParameter("fSave");
	String srcFile = request.getParameter("srcFile");
	String destPath = request.getParameter("destPath");
	String listFilePath = request.getParameter("listFile");
	String delFile = request.getParameter("delFile");
	
	//System.out.println("inputFile"+isFileSaved);
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
				<b>List/View/Edit/Copy File:</b>
			</legend>
			
			<form action="" name="listFileForm" method="post">
				<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff"
					class="brmsTable" width="100%">
					<tr>
						<th align="right" width="20%" >List Files: </th>
						<td align="left" width="70%" >
						<%if(listFilePath != null) {%>
							<input type="text" id="listfile" name="listFile" value="<%=listFilePath%>" size="50" >
						<%} else {%>
							<input type="text" id="listFile" name="listFile" value="" size="50" placeholder="Enter path to list files">
						<%} %>
						
						&nbsp;&nbsp;<input align="middle" type="submit" value="List Files" id="listF" >
						</td>
					</tr>
				</table>
			</form>
			<br/>
			
			<form action="" name="searchFileForm" method="post">
				<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff"
					class="brmsTable" width="100%">
					<tr>
						<th align="right" width="20%" >Open File: </th>
						<td align="left" width="70%" >
						<%if(inputFile != null) {%>
							<input type="text" id="infile" name="inFile" value="<%=inputFile%>" size="50" >
						<%} else {%>
							<input type="text" id="inFile" name="inFile" value="" size="50" placeholder="Enter File with path">
						<%} %>
						
						&nbsp;&nbsp;<input align="middle" type="submit" value="Open" id="Submit" >
						</td>
					</tr>
				</table>
			</form>
			<br/>
			
			<form action="" name="copyFileForm" method="post">
				<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff"
					class="brmsTable" width="100%">
					<tr>
						<td align="left" width="45%" >
						<%if(srcFile != null) {%>
							<input type="text" id="srcfile" name="srcFile" value="<%=srcFile%>" size="50" >
						<%} else {%>
							<input type="text" id="srcFile" name="srcFile" value="" size="50" placeholder="Enter Source File & path">
						<%} %>
						<td align="left" width="45%" >
						<%if(destPath != null) {%>
							<input type="text" id="destPath" name="destPath" value="<%=destPath%>" size="50" >
						<%} else {%>
							<input type="text" id="destPath" name="destPath" value="" size="50" placeholder="Enter Destination path">
						<%} %>
						</td>
						<td align="left" width="10%" >
						&nbsp;&nbsp;<input align="middle" type="submit" value="Copy" id="Copy" >
						</td>
					</tr>
				</table>
			</form>
			<br/>
			
			<form action="" name="delFileForm" method="post">
				<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff"
					class="brmsTable" width="100%">
					<tr>
						<th align="right" width="20%" >Delete File: </th>
						<td align="left" width="70%" >
						<%if(delFile != null) {%>
							<input type="text" id="delfile" name="delFile" value="<%=delFile%>" size="50" >
						<%} else {%>
							<input type="text" id="delFile" name="delFile" value="" size="50" placeholder="Enter Source File & path">
						<%} %>
						
						&nbsp;&nbsp;<input align="middle" type="submit" value="Delete" id="Delete" >
					</tr>
				</table>
			</form><br/>
			
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
			<left><p><b><font color="grey">Opened File: <%=inputFile%> </font></b></p></left>
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
			</form><br/>
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
			
			<br/>
			<!-- This is for list all files of given path -->
			<%if (listFilePath !=null) {
				List<String> results = new ArrayList<String>();
				File[] files = new File(listFilePath).listFiles();
				//If this pathname does not denote a directory, then listFiles() returns null. 
				for (File file : files) {
				    if (file.isFile()) {
				        results.add(file.getName());
				    }
				}
				%>
				<center><p><b><font color="grey">List of all files under path: <%=listFilePath%> </font></b></p></center>
				<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff"
				class="brmsTable" width="100%">
				<% for (String fileName : results) {
				 %>
					<tr>
					<td colspan="2" align="left">
						<%=listFilePath + "/" + fileName%>
					</td>
					</tr>
				<%}%>
				</table>
			<% } %>
			<br/>
			
			<!-- This is to delete given file -->
			<% if(delFile != null) {
				Path delFilePath = Paths.get(delFile);
				Files.delete(delFilePath);
			%>
				<center><p><b><font color="green">File deleted successfully: <%=delFile%> </font></b></p></center>
			<%}%>
			<br/>
			
			<!-- This is to copy file from source to detination path -->
			<% if(srcFile != null && destPath != null) {
				
				Path srcFilePath = Paths.get(srcFile);
				Path destDir = Paths.get(destPath);
				Files.copy(srcFilePath, destDir.resolve(srcFilePath.getFileName()));
			%>
				<center><p><b><font color="green">File copied successfully. </font></b></p></center>
			<%}%>
			<br/>
			
		</fieldset>
	</div>

</body>
</html>
