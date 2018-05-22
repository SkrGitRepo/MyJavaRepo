<%@page import="java.util.HashMap"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="com.sample.utility.BrmsJsonUtil"%>
<%@page import="org.apache.http.client.methods.HttpPost"%>
<%@page import="org.apache.http.entity.StringEntity"%>
<%@page import="org.apache.http.client.HttpClient"%> 
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
<%@page import="java.text.ParseException" %>
<%@page import="com.cisco.brmspega.util.file.BrmsJvmStartStatus" %>

<%@page import="org.apache.commons.io.FileUtils" %>
<%@page import="org.apache.commons.io.filefilter.WildcardFileFilter"%>



<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%
	String strURLPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	String vmCfgFileDir =  "/opt/brms/shared/scripts/";
	String lifecycle = request.getParameter("lc");
    String dataCenter = request.getParameter("dc");
    String jvmAction =  request.getParameter("action");
    String actionDays =  request.getParameter("days");
    BrmsJvmStartStatus statusObj = new BrmsJvmStartStatus();
    ArrayList<String> finalLogData = new ArrayList<String>();
	ArrayList<String> allJVMData = new ArrayList<String>();
    
    if (lifecycle != null && dataCenter != null && jvmAction != null && actionDays != null) {
		

		try {
			allJVMData = statusObj.collect_vm_from_config_file(lifecycle, dataCenter);

			finalLogData = statusObj.readUserActivityLog(dataCenter,actionDays);


		} catch (ParseException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
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
		
		function filterOnDays(path){
			var dc=document.getElementById('selectDC').value;
			var lc=document.getElementById('selectLC').value;
			var action = document.getElementById('actionType').value;
			var days = document.getElementById('selectDays').value;
	
			document.reportPortForm.action = path + "/jsp/brms_jvm_start_days_status.jsp?lc="+lc+"&dc="+dc+"&action="+action+"&days="+days;
			document.reportPortForm.submit();
		}
		
		function onWindowLoad(){
			var dataCenter ='<%=dataCenter%>';
			var lfcly ='<%=lifecycle%>';
			var jvmAction = <%=jvmAction%>;
			var actionDays = <%=actionDays%>;
			
			var selectLCObj=document.getElementById('selectLC');
			for (var i = 0; i < selectLCObj.options.length; i++) {
				if (selectLCObj.options[i].text.toUpperCase() == lfcly.toUpperCase()) {
					selectLCObj.options[i].selected = true;
				} 
			}
			
			var selectDCObj=document.getElementById('selectDC');
			for (var i = 0; i < selectDCObj.options.length; i++) {
				if (selectDCObj.options[i].text.toUpperCase() == dataCenter.toUpperCase()) {
					selectDCObj.options[i].selected = true;
				}
			}
			
			var selectActionObj=document.getElementById('actionType');
			for (var i = 0; i < selectActionObj.options.length; i++) {
				if (selectActionObj.options[i].text.toUpperCase() == jvmAction.toUpperCase()) {
					selectActionObj.options[i].selected = true;
				}
			}
			
			var selectDaysObj=document.getElementById('selectDays');
			for (var i = 0; i < selectDaysObj.options.length; i++) {
				if (selectDaysObj.options[i].text.toUpperCase() == actionDays.toUpperCase()) {
					selectDaysObj.options[i].selected = true;
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
					<th align="center" width="30%" >Select Time</th>
					<th align="center" width="30%" >Select Day</th>
				</tr>
				<tr>
					<td align="center">
					<select id="selectLC" name="lc" >
						<!-- <option value="ALL">ALL</option> -->
						<option value="DEV">DEV</option>
						<option value="STG">STAGE</option>
						<option value="LT">LT</option>
						<option value="PRD">PROD</option>
						<option value="POC">POC</option>
					</select>
					</td>
					
					<td align="center">
					<select id="selectDC" name="dc">
						<option value="none">--None--</option>
						<option value="NPRD1">NPRD1</option>
						<option value="NPRD2">NPRD2</option>
						<option value="PRD1">PRD1</option>
						<option value="PRD2">PRD2</option>
						<option value="PRD3">PRD3</option>
						<option value="PRD4">PRD4</option>
					</select>
					</td>
					
					<td align="center">
					<select id="actionType" name="action">
						<option value="none">--None--</option>
						<option value="SB">Started Before</option>
						<option value="SA">Started After</option>
					</select>
					</td>
					
					<td align="center">
					<select id="selectDays" name="days" onchange="filterOnDays('<%=strURLPath%>')">
						<option value="none">--None--</option>
						<option value="7">1-Week</option>
						<option value="14">2-Weeks</option>
						<option value="21">3-Weeks</option>
						<option value="28">4-Weeks</option>
						<option value="30">1-Month</option>
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
		
		
		
			<% if ( (!allJVMData.isEmpty()) && (!finalLogData.isEmpty())) { %>
			<br/> <br/>
			<table border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff" class="brmsTable" width="100%">
				<tr>
					<th align="left">SL. No.</th>
					<th align="center" width="30%" colspan="2">LIFECYCLE</th>
					<th align="center" width="30%" colspan="2">DOMAIN</th>
					<th align="center" width="30%" colspan="2">HOSTNAME</th>
					<th align="center" width="30%" colspan="2">PORT</th>
				</tr>
				
					<% 
						int count = 1;
										
						
						if(jvmAction.equalsIgnoreCase("SA")) {
							for(int i=0; i <finalLogData.size();i++) {
								String monitorHost = finalLogData.get(i);
									if(monitorHost!= null && monitorHost.contains(lifecycle)) {
									//System.out.println("DATA: "+data);
									String[] tokens = monitorHost.split("\\|");
									
								%>
									<tr>
									<td><%=count %></td>
									<td colspan="2" ><%=tokens[0] %></td>
									
									<td colspan="2" ><%=tokens[1] %></td>
									
									<td colspan="2" ><%=tokens[2].split(":")[0] %></td>
									
									<td colspan="2" ><%=tokens[2].split(":")[1] %></td>
									</tr>
								<%
									count = count + 1;
								}
							}
						} else if(jvmAction.equalsIgnoreCase("SB")) {
							
							for(int i=0; i <allJVMData.size();i++) {
									String eachJVMHostPort = allJVMData.get(i);
									if(eachJVMHostPort!= null && eachJVMHostPort.contains(lifecycle) && !finalLogData.contains(eachJVMHostPort)) {
									//System.out.println("DATA: "+data);
										String[] tokens = eachJVMHostPort.split("\\|");
										
									%>
										<tr>
										<td><%=count %></td>
										<td colspan="2" ><%=tokens[0] %></td>
										
										<td colspan="2" ><%=tokens[1] %></td>
										
										<td colspan="2" ><%=tokens[2].split(":")[0] %></td>
										
										<td colspan="2" ><%=tokens[2].split(":")[1] %></td>
										</tr>
									<%
										count = count + 1;
								}
							}
						}
						%>
			</table>
		<% } %>
		</fieldset>
		</div>			
</body>
</html>