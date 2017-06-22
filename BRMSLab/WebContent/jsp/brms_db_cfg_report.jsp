<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.HashMap"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="java.io.File"%>
<%@page import="java.util.List" %>
<%@page import="java.util.Collection" %>
<%@page import="java.util.Collections" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.util.Map" %>
<%@page import="java.util.TreeMap" %>
<%@page import="com.cisco.brmspega.util.file.BRMSFileParseUtil" %>

<%@page import="org.apache.commons.io.FileUtils" %>
<%@page import="org.apache.commons.io.filefilter.WildcardFileFilter"%>



<%
	//String dbCfgFileDir =  "/opt/brms/install/";
	String lifecycle = request.getParameter("lc");
	String inDomain = request.getParameter("dname");
    //Collection<File> fileList = new ArrayList<File>();
	//Iterator<File> fileListIterator = null;
	
	BRMSFileParseUtil brmsFileUtil = new BRMSFileParseUtil();
	List<String> hostList = brmsFileUtil.get_brms_db_cfg_report("BRMS_ALL_ALL_DOMAINs_DB_CONF_REPORT.csv");
	
	
	//List<String> hostList = new ArrayList<String>();
	
	
	//String fileFilterString = null;
	String strURLPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	System.out.println("URL PATH:"+strURLPath);

   // String [] lcList;
    
   /*  if (lifecycle != null || lifecycle == null) { */
	
		//lcList = new String[] {"PRD","STG","DEV","LT","POC"};
		/* if ( lifecycle.equalsIgnoreCase("ALL")) {
			lcList = new String[] {"PRD","STG","DEV","LT","POC"};
		} else {
			lcList = new String[] { lifecycle.toUpperCase() };
		} */
	/* for (String lc : lcList) {
		String env = lc;
		if( dataCenter != null && lifecycle != null ) {
			
			fileFilterString = "BRMS_ALL_ALL_DOMAINs_DB_CONF_REPORT.csv";
		} else if ( dataCenter != null && lifecycle != null ) {
			fileFilterString = "brms_vm_cfg_*" + env + ".txt";
		} */
	
/* 	if(!hostList.isEmpty()) {
		fileFilterString = "BRMS_ALL_ALL_DOMAINs_DB_CONF_REPORT.csv";
		File dbCfgFile = new File(dbCfgFileDir + fileFilterString);
		if (dbCfgFile.exists()) {
		fileList = FileUtils.listFiles(new File(dbCfgFileDir), new WildcardFileFilter(fileFilterString), null);
		File file;
		fileListIterator = fileList.iterator();
		
		while (fileListIterator.hasNext()) {
			file = (File) fileListIterator.next();

			if (file.exists()) {
				List<String> lines = FileUtils.readLines(file);
				for (String line : lines) {
					if ((!line.contains("CONTEXT")) || (!line.contains(""))) {
						if (!line.contains("#")) {
							hostList.add(line);
						}
					}
				}
			}
		 } 
	 } */
		
		/* if (!hostList.isEmpty() && lifecycle !=null ) { */
	if ( hostList != null && !hostList.isEmpty()) {
		
		HashMap<String,Integer> uniqueDomainMap = new HashMap<String,Integer>();
		Map<String, Integer> sortedUniqueDomMap = null;
			Collections.sort(hostList);
			int domain_count = 1;
			for(String data :  hostList) {
				
				String[] tokens = data.split("\\,");
				String dom_name = tokens[1];
				if (!uniqueDomainMap.containsKey(dom_name)) {
					uniqueDomainMap.put(dom_name, domain_count);
					domain_count += 1;
				}
				//domain_count += 1;
			}
			sortedUniqueDomMap = new TreeMap<String, Integer>(uniqueDomainMap);
		
%>

<html>
<title>BRMS DB CONFIG Report</title>

<head>
		<!-- <meta http-equiv="X-UA-Compatible" content="IE=edge" /> -->
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>EA OnRamp Process</title>
		
		<link href="css/bootstrap.css" rel="stylesheet" type="text/css" />
		<script src="js/jquery-1.11.3/jquery-1.11.3.min.js"></script>
		<script src="js/jquery-sparklines-2.1.2/jquery.sparkline.min.js"></script>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>

		<script type="text/javascript">
		
				
		function clearForm() {
		    document.getElementById("reportDBConfigForm").reset();
		}
		
		function filterOnDCEnv(path){
			var lc=document.getElementById('selectLC').value;
			var domName=document.getElementById('selectDom').value;
			document.reportDBConfigForm.action = path + "/jsp/brms_db_cfg_report.jsp?lc="+lc+"&dname="+domName;
			document.reportDBConfigForm.submit();
		}
		
		function filterOnDomEnv(path){
			var lc=document.getElementById('selectLC').value;
			var domName=document.getElementById('selectDom').value;
			
			alert(domName);
			
			document.reportDBConfigForm.action = path + "/jsp/brms_db_cfg_report.jsp?lc="+lc+"&dname="+domName;
			document.reportDBConfigForm.submit();
		}
		
		function onWindowLoad(){
			var domName ='<%=inDomain%>';
			var lfcly ='<%=lifecycle%>';
			
			var selectLCObj=document.getElementById('selectLC');
			for (var i = 0; i < selectLCObj.options.length; i++) {
				if (selectLCObj.options[i].text.toUpperCase() == lfcly.toUpperCase()) {
					selectLCObj.options[i].selected = true;
				} 
			}
			
			var selectDOMObj=document.getElementById('selectDom');
			for (var i = 0; i < selectDOMObj.options.length; i++) {
				if (selectDOMObj.options[i].text.toUpperCase() == domName.toUpperCase()) {
					selectDOMObj.options[i].selected = true;
				}
			}
			
		}
		
		</script>
		</head>

		<body onload="onWindowLoad()">
		<br>
		<div id="reportDBConfig">
		<fieldset style="width:60%;margin: auto;text-align:center;" >
		<legend class="pageText" align="left"><b>BRMS DB CFG Report:</b></legend>
		
			<form action="" id="reportDBConfigForm" name="reportDBConfigForm" method="post">
			<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff" class="brmsTable" width="100%">
				<tr>
					<th align="center" width="30%" >Select Env</th>
					<th align="center" width="30%" >Select Domain</th>
				</tr>
				<tr>
					<td align="center">
					<select id="selectLC" name="lc" onchange="filterOnDCEnv('<%=strURLPath%>')">
					<!-- <select id="selectLC" name="lc"> -->
						<!-- <option value="ALL">ALL</option> -->
						<option value="DEV">DEV</option>
						<option value="STG">STG</option>
						<option value="LT">LT</option>
						<option value="PRD">PRD</option>
						<option value="POC">POC</option>
					</select>
					</td>
					
					<td align="center">
					<select id="selectDom" name="dname" onchange="filterOnDomEnv('<%=strURLPath%>')">
						<option value="none">--none--</option>
						<option value="all">ALL</option>
						
						<%for(String domain : sortedUniqueDomMap.keySet() ) {%>
							<option value="<%=domain %>"><%=domain%></option>
						<%}%>
					</select>
					</td>
				</tr>
				
			</table>
			</form>
		
		 	
			<% if (!hostList.isEmpty() && lifecycle !=null  && inDomain != null) { %>
			<br/><br/>
			<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff" class="brmsTable" width="100%">
				<tr>
					<th align="left">JVM No.</th>
					<th align="center" width="30%" colspan="2">LIFECYCLE</th>
					<th align="center" width="30%" colspan="2">DOMAIN NAME</th>
					<th align="center" width="30%" colspan="2">PEGA VER</th>
					<th align="center" width="30%" colspan="2">DSN</th>
					<th align="center" width="30%" colspan="2">JNDI</th>
					<th align="center" width="30%" colspan="2">CON-STRING</th>
					<th align="center" width="30%" colspan="2">DB-SNAME</th>
					<th align="center" width="30%" colspan="2">SCHEMA</th>
					<th align="center" width="30%" colspan="2">PASSWORD</th>
				</tr>
					
					<% 
						int count = 1;
						for(String data :  hostList) {
							String[] tokens = data.split("\\,");
							System.out.println("Length"+tokens.length);
							if (tokens.length > 3 && tokens[0].equalsIgnoreCase(lifecycle.toUpperCase()) && inDomain.equalsIgnoreCase("all")) {
								System.out.println("DATA: "+data);
							%>
							<tr>
								<td><%=count %></td>
								<td colspan="2" ><%=tokens[0] %></td>
								<td colspan="2" ><%=tokens[1] %></td>
								<td colspan="2" ><%=tokens[2] %></td>
							<%if (tokens.length >= 3) { %>
								<td colspan="2" ><%=tokens[3] %></td>
							<%} else {%>
								<td colspan="2" >NA</td>
							<%} %>
								<td colspan="2" ><%=tokens[4] %></td>
								<td colspan="2" ><%=tokens[5] %></td>
								<td colspan="2" ><%=tokens[6] %></td>
							<%if (tokens.length > 7) { %>
								<td colspan="2" ><%=tokens[7] %></td>
							<% } else { %>
								<td colspan="2" >NA</td>
							<% }%>
							
							<%if (tokens.length > 7) { %>
								<td colspan="2" ><%=tokens[8] %></td>
							<% } else { %>
								<td colspan="2" >NA</td>
							<% }%>
								</tr>
													
							<%
								count = count + 1; 
							} else if (tokens.length > 3 && tokens[0].equalsIgnoreCase(lifecycle.toUpperCase()) && !inDomain.equalsIgnoreCase("all") && tokens[1].equalsIgnoreCase(inDomain)) {
								System.out.println("DATA: "+data);
							%>
							<tr>
								<td><%=count %></td>
								<td colspan="2" ><%=tokens[0] %></td>
								
								<td colspan="2" ><%=tokens[1] %></td>
							<%if (tokens.length >= 2) { %>
								<td colspan="2" ><%=tokens[2] %></td>
							<%} else {%>
								<td colspan="2" >NA</td>
							<%} %>
								<td colspan="2" ><%=tokens[3] %></td>
								<td colspan="2" ><%=tokens[4] %></td>
								<td colspan="2" ><%=tokens[5] %></td>
							<%if (tokens.length > 6) { %>
								<td colspan="2" ><%=tokens[6] %></td>
							<% } else { %>
								<td colspan="2" >NA</td>
							<% }%>
							
							<%if (tokens.length > 7) { %>
								<td colspan="2" ><%=tokens[7] %></td>
							<% } else { %>
								<td colspan="2" >NA</td>
							<% }%>
								</tr>
													
							<%
								count = count + 1; 
							}
						} 
						%>
		<% } %>
		<%-- <% else {%>
			<tr><td>No matching data found</td></tr>
		<% } %> --%>
		</table>
		</fieldset>
		</div>			
</body>
</html>
<% } else {%>
	<html><body><center><b><font color="red"> Required config file does not exist..!</font></b></center></body></html>
<% }%>