<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<%@page import="com.sample.utility.BrmsJsonUtil"%>
<%@page import="com.cisco.csc.sdk.test.TestService_Upload"%>



<%@page import="org.apache.commons.io.FileUtils" %>
<%@page import="org.apache.commons.io.filefilter.WildcardFileFilter"%>



<%

	TestService_Upload cscObj = new TestService_Upload();
	String dbCfgFileDir =  "/opt/brms/install/";
	String lifecycle = request.getParameter("lc");
    //String dataCenter = request.getParameter("dc");
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
	/* for (String lc : lcList) {
		String env = lc;
		if( dataCenter != null && lifecycle != null ) {
			
			fileFilterString = "BRMS_ALL_ALL_DOMAINs_DB_CONF_REPORT.csv";
		} else if ( dataCenter != null && lifecycle != null ) {
			fileFilterString = "brms_vm_cfg_*" + env + ".txt";
		} */
		
		fileFilterString = "BRMS_ALL_ALL_DOMAINs_DB_CONF_REPORT.csv";
		fileList = FileUtils.listFiles(new File(dbCfgFileDir), new WildcardFileFilter(fileFilterString), null);
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
							//String[] tokens = line.split(",");

							//String allOutput = tokens[10] + "|" + tokens[0] + "|" + tokens[1] + "|" + tokens[2]
							//		+ "|" + tokens[3] + "|" + tokens[4] + "|" + tokens[5] + "|" + tokens[6] + "|"
							//		+ tokens[7] + "|" + tokens[8] + "|" + tokens[9];
							
							//String allOutput =  tokens[10] + "|" + tokens[0] + "|" + tokens[2] + "|" + tokens[3] + "|" + tokens[9];
							//System.out.println(allOutput);
							hostList.add(line);
						}
					}
				}
			}
		}
	/* } */
	
    }
%>

<html>
<title>BRMS Host Port Report</title>

<head>
		<!-- <meta http-equiv="X-UA-Compatible" content="IE=edge" /> -->
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>EA OnRamp Process</title>
		
		<link href="../css/bootstrap.css" rel="stylesheet" type="text/css" />
		<script src="../js/jquery-1.11.3/jquery-1.11.3.min.js"></script>
		<script src="../js/jquery-sparklines-2.1.2/jquery.sparkline.min.js"></script>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>

		<script type="text/javascript">
		
				
		function clearForm() {
		    document.getElementById("reportPortForm").reset();
		}
		
		function filterOnDCEnv(path){
			//var dc=document.getElementById('selectDC').value;
			var lc=document.getElementById('selectLC').value;
			/* alert(env);
			alert(dc);
			var allpath = path + "/jsp/brms_host_active_port_report.jsp?env="+env+"&dc="+dc;
			alert(allpath); */
			document.reportPortForm.action = path + "/jsp/brms_db_cfg__report.jsp?lc="+lc;
			document.reportPortForm.submit();
		}
		
		function onWindowLoad(){
			var dataCenter ='<%=lifecycle%>';
			var lfcly ='<%=lifecycle%>';
			
			
			var selectLCObj=document.getElementById('selectLC');
			for (var i = 0; i < selectLCObj.options.length; i++) {
				if (selectLCObj.options[i].text.toUpperCase() == lfcly.toUpperCase()) {
					selectLCObj.options[i].selected = true;
				} 
			}
			
			/* var selectDCObj=document.getElementById('selectDC');
			for (var i = 0; i < selectDCObj.options.length; i++) {
				if (selectDCObj.options[i].text.toUpperCase() == dataCenter.toUpperCase()) {
					selectDCObj.options[i].selected = true;
				}
			} */
			
		}
		
		</script>
		</head>

			
		<body onload="onWindowLoad()">
		<br>
		<div id="reportHostPort">
		
  		 
    		<%-- <form class="form-horizontal">
    		<fieldset>
    		<legend>Legend</legend>
     		  <div class="form-group">
      			<label for="select" class="col-lg-2 control-label">Select Env</label>
      			  <div class="col-lg-10">
        			<select class="form-control" id="selectLC" name="lc" onchange="filterOnDCEnv('<%=strURLPath%>')">
          				<option value="DEV">DEV</option>
						<option value="STG">STG</option>
						<option value="LT">LT</option>
						<option value="PRD">PRD</option>
						<option value="POC">POC</option>
        			</select>
      			</div>
    		 </div>
  		 </fieldset>
		</form> --%>
		
		
		<fieldset style="width:60%;margin: auto;text-align:center;" >
		<legend class="pageText" align="left"><b>BRMS DB CFG Report:</b></legend>
		
			<form action="" id="reportPortForm" name="reportPortForm" method="post">
			<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff" class="brmsTable" width="100%">
				<tr>
					<th align="center" width="30%" >Select Env</th>
					<!-- <th align="center" width="30%" >Select DataCenter</th> -->
				</tr>
				<tr>
					<td align="center">
					<select id="selectLC" name="lc" onchange="filterOnDCEnv('<%=strURLPath%>')">
						<!-- <option value="ALL">ALL</option> -->
						<option value="DEV">DEV</option>
						<option value="STG">STG</option>
						<option value="LT">LT</option>
						<option value="PRD">PRD</option>
						<option value="POC">POC</option>
					</select>
					</td>
				</tr>
				
				<%-- <select id="selectVDCHostForED" onchange="filterOnHostED('<%=strURLPath%>')">
					<option value="None">--None--</option>
						<%for(String vdcHostName :  sortedVDCHostList){%>
							<option value="<%=vdcHostName %>"><%=vdcHostName%></option><%}%>
				</select> --%>
				<!-- <tr>
					<td colspan="2" align="center">
						<input align="middle" type="submit" value="Find" id="find">
						
					</td> </tr> </table>-->
				
			</table>
			</form>
		
		 	
		
			<% if (!hostList.isEmpty() && lifecycle !=null ) { %>
			<br/> <br/>
			<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff" class="brmsTable" width="100%">
				<tr>
					<th align="left">JVM No.</th>
					<th align="center" width="30%" colspan="2">LIFECYCLE</th>
					<th align="center" width="30%" colspan="2">DOMAIN NAME</th>
					<th align="center" width="30%" colspan="2">DSN</th>
					<th align="center" width="30%" colspan="2">JNDI</th>
					<th align="center" width="30%" colspan="2">CON-STRING</th>
					<th align="center" width="30%" colspan="2">SCHEMA</th>
					<th align="center" width="30%" colspan="2">PASSWORD</th>
				</tr>
				<!-- <table class="table table-striped table-hover ">
				  <thead>
				    <tr>
				      <th>JVM No.</th>
				      <th>LIFECYCLE</th>
				      <th>DOMAIN NAME</th>
				      <th>DSN</th>
				      <th>JNDI</th>
				      <th>CON-STRING</th>
				      <th>SCHEMA</th>
				      <th>PASSWORD</th>
				    </tr>
				  </thead> -->
				
					<% 
						Collections.sort(hostList);
						int count = 1;
						HashMap<String,String> hostAdminPortMap = new HashMap<String,String>();
						HashMap<String,String> hostManagedPortMap = new HashMap<String,String>();
						
						
						
						for(String data :  hostList) {
							
							String[] tokens = data.split("\\,");
							System.out.println("Length"+tokens.length);
							if (tokens[0].equalsIgnoreCase(lifecycle.toUpperCase())) {
								System.out.println("DATA: "+data);
							%>
							<tr>
								<td><%=count %></td>
								<td colspan="2" ><%=tokens[0] %></td>
								
								<td colspan="2" ><%=tokens[1] %></td>
								<td colspan="2" ><%=tokens[2] %></td>
								<td colspan="2" ><%=tokens[3] %></td>
								<td colspan="2" ><%=tokens[4] %></td>
								<td colspan="2" ><%=tokens[5] %></td>
							<%if (tokens.length > 6) { %>
								<td colspan="2" ><%=tokens[6] %></td>
							<% } else { %>
								<td colspan="2" >NA</td>
							<% }%>
								</tr>
							
							 <%-- <tbody>
							    <tr>
							      <td><%=count %></td>
							      <td><%=tokens[0] %></td>
							      <td><%=tokens[1] %></td>
							      <td><%=tokens[2] %></td>
							      <td><%=tokens[3] %></td>
							      <td><%=tokens[4] %></td>
							      <td><%=tokens[5] %></td>
							      <%if (tokens.length > 6) { %>
								<td><%=tokens[6]%></td>
							      <% } else { %>
								<td>NA</td>
							      <% }%>
							    </tr> --%>
							
							<%
								count = count + 1; 
							}
							
						} 
						 
						%>
						
			
		<% }  else {%>
			<tr><td>No matching data found</td></tr>
		<% } %>
		</table>
		</fieldset>
		</div>			
</body>
</html>