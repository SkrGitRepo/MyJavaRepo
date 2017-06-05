<%@page import="java.util.HashMap"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="com.sample.utility.BrmsJsonUtil"%>
<%@page import="org.apache.http.client.methods.HttpPost"%>
<%@page import="org.apache.http.entity.StringEntity"%>
<%@page import="org.apache.http.client.HttpClient"%> 
<%@page import="org.apache.http.impl.client.DefaultHttpClient"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="org.apache.http.HttpResponse"%>
<%@page import="javax.xml.bind.DatatypeConverter"%>
<%@page import="java.io.File"%>
<%@page import="com.cisco.dataconnect.process.Load" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Collection" %>
<%@page import="java.util.Collections" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.Iterator" %>

<%@page import="org.apache.commons.io.FileUtils" %>
<%@page import="org.apache.commons.io.filefilter.WildcardFileFilter"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%
	String vmCfgFileDir =  "/opt/brms/shared/scripts/";
	String lifecycle = request.getParameter("lc");
    String dataCenter = request.getParameter("dc");
    Collection<File> fileList = new ArrayList<File>();
	Iterator<File> fileListIterator = null;
	List<String> hostList = new ArrayList<String>();
	String fileFilterString = null;
	String strURLPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	System.out.println("URL PATH:"+strURLPath);
    
    String [] lcList;
    
    if (lifecycle != null) {
	
		//lcList = new String[] {"PRD","STG","DEV","LT","POC"};
		if ( lifecycle.equalsIgnoreCase("ALL")) {
			lcList = new String[] {"PRD","STG","DEV","LT","POC"};
		} else {
			lcList = new String[] { lifecycle.toUpperCase() };
		}
	for (String lc : lcList) {
		String env = lc;
		if( dataCenter != null && lifecycle != null ) {
			fileFilterString = "brms_vm_cfg_" + dataCenter + "_" + env + ".txt";
		} else if ( dataCenter != null && lifecycle != null ) {
			fileFilterString = "brms_vm_cfg_*" + env + ".txt";
		}
		fileList = FileUtils.listFiles(new File(vmCfgFileDir), new WildcardFileFilter(fileFilterString), null);
		File file;
		fileListIterator = fileList.iterator();
		
		while (fileListIterator.hasNext()) {
			file = (File) fileListIterator.next();
			/*System.out.println("-----------------------------------------------");
			System.out.println("Read config file"+file.getName());
			System.out.println("-----------------------------------------------");*/

			if (file.exists()) {
				List<String> lines = FileUtils.readLines(file);
				for (String line : lines) {
					if ((!line.contains("CONTEXT")) || (!line.contains(""))) {
						if (!line.contains("#")) {
							String[] tokens = line.split(",");
							String[] domainPath = tokens[9].split("/");

							//String allOutput = tokens[10] + "|" + tokens[0] + "|" + tokens[1] + "|" + tokens[2]
							//		+ "|" + tokens[3] + "|" + tokens[4] + "|" + tokens[5] + "|" + tokens[6] + "|"
							//		+ tokens[7] + "|" + tokens[8] + "|" + tokens[9];
							
							String allOutput =  tokens[10] + "|" + tokens[0] + "|" + tokens[2] + "|" + tokens[3] + "|" + tokens[9];
							//System.out.println(allOutput);
							hostList.add(allOutput);
						}
					}
				}
			}
		}
	}
	
    }
%>

<html>
<title>BRMS Host Port Report</title>

<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title>EA OnRamp Process</title>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>

		<script type="text/javascript">
		
				
		function clearForm() {
		    document.getElementById("reportPortForm").reset();
		}
		
		function filterOnDCEnv(path){
			var dc=document.getElementById('selectDC').value;
			var lc=document.getElementById('selectEnv').value;
			/* alert(env);
			alert(dc);
			var allpath = path + "/jsp/brms_host_active_port_report.jsp?env="+env+"&dc="+dc;
			alert(allpath); */
			document.reportPortForm.action = path + "/jsp/brms_host_active_port_report.jsp?env="+lc+"&dc="+dc;
			document.reportPortForm.submit();
		}
		
		function onWindowLoad(){
			var dataCenter ='<%=dataCenter%>';
			var lifecycle ='<%=lifecycle%>';
			
			var selectDCObj=document.getElementById('selectDC');
			for (var i = 0; i < selectDCObj.options.length; i++) {
				if (selectDCObj.options[i].text.toUpperCase() == dataCenter.toUpperCase()) {
					selectDCObj.options[i].selected = true;
				}
			}
			
			var selectEnvObj=document.getElementById('selectEnv');
			for (var i = 0; i < selectEnvObj.options.length; i++) {
				if (selectEnvObj.options[i].text.toUpperCase() == lifecycle.toUpperCase()) {
					selectEnvObj.options[i].selected = true;
				}
			}
			
		}
		
		</script>
		</head>

			
		<body onload="onWindowLoad()">
		<br>
		<div id="reportHostPort">
		<fieldset style="width:60%;margin: auto;text-align:center;" >
		<legend class="pageText" align="left"><b>BRMS Host/Port Report:</b></legend>
		
			<form action="" id="reportPortForm" name="reportPortForm" method="post">
			<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff" class="brmsTable" width="100%">
				<tr>
					<th align="center" width="30%" >Select Env</th>
					<th align="center" width="30%" >Select DataCenter</th>
				</tr>
				<tr>
					<td align="center">
					<select id="selectEnv" name="lc" >
						<!-- <option value="ALL">ALL</option> -->
						<option value="DEV">DEV</option>
						<option value="STG">STAGE</option>
						<option value="LT">LT</option>
						<option value="PRD">PROD</option>
						<option value="POC">POC</option>
					</select>
					</td>
					
					<td align="center">
					<select id="selectDC" name="dc" onchange="filterOnDCEnv('<%=strURLPath%>')">
						<option value="ALL">ALL</option>
						<option value="NPRD1">NPRD1</option>
						<option value="NPRD2">NPRD2</option>
						<option value="PRD1">PRD1</option>
						<option value="PRD2">PRD2</option>
						<option value="PRD3">PRD3</option>
						<option value="PRD4">PRD4</option>
					</select>
					</td>
				</tr>
				<!-- <tr>
					<td colspan="2" align="center">
						<input align="middle" type="submit" value="Find" id="find">
						
					</td>
				</tr> -->
			</table>
			</form>
		
		
		
			<% if (!hostList.isEmpty()) { %>
			<br/> <br/>
			<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff" class="brmsTable" width="100%">
				<tr>
					<th align="left">JVM No.</th>
					<th align="center" width="30%" colspan="2">LIFECYCLE</th>
					<th align="center" width="30%" colspan="2">HOSTNAME</th>
					<th align="center" width="30%" colspan="2">MANAGED PORT</th>
					<th align="center" width="30%" colspan="2">ADMIN PORT</th>
					<th align="center" width="30%" colspan="2">DOMAIN/APP</th>
				</tr>
				
					<% 
						Collections.sort(hostList);
						int count = 1;
						HashMap<String,String> hostAdminPortMap = new HashMap<String,String>();
						HashMap<String,String> hostManagedPortMap = new HashMap<String,String>();
						
						
						
						for(String data :  hostList) {
							//System.out.println("DATA: "+data);
							String[] tokens = data.split("\\|");
							
						%>
							<tr>
							<td><%=count %></td>
							<td colspan="2" ><%=tokens[0] %></td>
							
							<td colspan="2" ><%=tokens[1] %></td>
							<% if (hostManagedPortMap.containsKey(tokens[3] + ":" + tokens[1])) { %>
								<td colspan="2" bgcolor="red"><%=tokens[3] %></td>
							<% } else {%>
								<td colspan="2" ><%=tokens[3] %></td>
							<%} %>
							<% if (hostAdminPortMap.containsKey(tokens[2] + ":" + tokens[1])) { %>
								<td colspan="2" bgcolor="red"><%=tokens[2] %></td>
							<% } else {%>
								<td colspan="2" ><%=tokens[2] %></td>
							<%} %>
							<td colspan="2" ><%=tokens[4] %></td>
							</tr>
						<%
							
							//System.out.println("INSERTING ADMIN MAP: "+tokens[2] + ":" + tokens[1] +" => " +tokens[1]);
							hostAdminPortMap.put(tokens[2] + ":" + tokens[1],tokens[1]);
							//System.out.println("INSERTING MANAGED MAP: "+tokens[3] + ":" + tokens[1] +" => " +tokens[1]);
							hostManagedPortMap.put(tokens[3] + ":" + tokens[1],tokens[1]);
							count = count + 1;
						}
						%>
			</table>
		<% } %>
		</fieldset>
		</div>			
</body>
</html>