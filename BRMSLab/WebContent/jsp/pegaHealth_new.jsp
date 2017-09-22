<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="java.io.FilenameFilter"%>
<%@page import="java.io.File" %>
<%@page import="java.util.*" %>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%@page import="org.apache.commons.io.FileUtils" %>
<%@page import="com.cisco.brmspega.bundles.PropertyLoader" %>
<%-- <%@page import="com.cisco.brmspega.useracess.UserUtil" %> --%>
<%@page import="com.cisco.brmspega.util.EnvBiListMapCache" %>

<%
			String domainName = request.getServerName();

			String domainApp =  request.getParameter("domainApp");
			if(null == domainApp || "".equals(domainApp))
				domainApp="All";
			
			String serverDomain="";
			
			String env=request.getParameter("env");
			if(null==env || "".equals(env))
				env="All";
			
			String strURLPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();//ParamUtil.getURLPathFromSession(request);
			//String userId = UserUtil.getUserId(request);
			//boolean isSysAdmin = UserUtil.isUserValid(userId, "BRMS_ADMIN") || UserUtil.isUserValid(userId, "BRM_ADMIN");
			boolean isReInstallStartAdmin = true;//UserUtil.isUserValid(userId, PropertyLoader.getInstance().getProperty("reinstall_start_admin"));
			int pageNum=0;
			if(null!=request.getParameter("page") && !request.getParameter("page").isEmpty())
				pageNum = Integer.parseInt(request.getParameter("page"));
			else
				pageNum=1;
			
			String folderLoc = PropertyLoader.getInstance().getProperty("server_tools_cofig_base");
			
			FilenameFilter logFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					String lowercaseName = name.toLowerCase();
					if (lowercaseName.contains("vm_cfg") && lowercaseName.endsWith(".txt")) {
						return true;
					} else {
						return false;
					}
				}
			};
			Set<String> domainList = new HashSet<String>();
			TreeSet<String> sortedDomainList=null;

			Set<String> envList=new HashSet<String>();
			TreeSet<String> sortedEnvList= null;

			List<String> fileLines = null;
			File[] fileList =new File(folderLoc).listFiles(logFilter);
			if (fileList.length > 1) {		
				String[] fileLineData = null;				
				for(File file:fileList){
					fileLines = FileUtils.readLines(file);
					if (fileLines.size() >= 1) {
						for (String fileLine : fileLines) {
							if (!fileLine.trim().startsWith("#")) {
								fileLineData = fileLine.split(",");
								if (fileLineData[0].indexOf("adm") == -1 && fileLineData.length > 9 && !"".equals(fileLineData[9])) {
									domainList.add(fileLineData[9]);
									if(fileLineData.length > 10 && !"".equals(fileLineData[10])){
										if(fileLineData[10].equalsIgnoreCase("dev") || fileLineData[10].equalsIgnoreCase("stage")
												|| fileLineData[10].equalsIgnoreCase("lt") || fileLineData[10].equalsIgnoreCase("poc")
												 || fileLineData[10].equalsIgnoreCase("prod")){
											envList.add(fileLineData[10]);
										}
									}
								}
							}
						}
					}
				}
				sortedDomainList =  new TreeSet<String>(domainList);
				sortedEnvList = new TreeSet<String>(envList);
			}
		%>
		
<%
/****************************************************************/
	String monDirLoc = "/opt/brms/monitor/config";//PropertyLoader.getInstance().getProperty("monitor_config_base");
	File new_domain_ping_cfg_file = new File(monDirLoc+"/new_ping_url_domain_list.txt");
	if (!new_domain_ping_cfg_file.getParentFile().exists()) {
		new_domain_ping_cfg_file.getParentFile().mkdir();
	}
	if (!new_domain_ping_cfg_file.exists()) {
		new_domain_ping_cfg_file.getParentFile().mkdir();
		new_domain_ping_cfg_file.createNewFile();
	}
	
	List<String> new_ping_env_domain_list = FileUtils.readLines(new_domain_ping_cfg_file);
	String DEV_LIST="";
	String STAGE_LIST="";
	String LT_LIST="";
	String PROD_LIST="";
	
	for(String fileLine : new_ping_env_domain_list){
		String key = fileLine.split("=")[0];
		String value=fileLine.split("=")[1];
		if(key !=  null && key.equalsIgnoreCase("DEV"))
			DEV_LIST=value;
		else if( key !=  null && key.equalsIgnoreCase("STAGE") || key.equalsIgnoreCase("STG"))
			STAGE_LIST=value;
		else if( key !=  null && key.equalsIgnoreCase("LT"))
			LT_LIST=value;
		else if(key !=  null && key.equalsIgnoreCase("PROD") || key.equalsIgnoreCase("PRD"))
			PROD_LIST=value;
	}
/****************************************************************/

	List<String> devDomainlist;
	List<String> stgDomainlist;
	List<String> ltDomainlist;		
	List<String> prdDomainlist;

	if (DEV_LIST != null)
		devDomainlist = new ArrayList<String>(Arrays.asList(DEV_LIST.split(",")));
	else 
		devDomainlist = new ArrayList<String>(Arrays.asList(PropertyLoader.getInstance().getProperty("dev_new_ping_domain_list").split(",")));
	
	if (STAGE_LIST != null)
		stgDomainlist = new ArrayList<String>(Arrays.asList(STAGE_LIST.split(",")));
	else 
		stgDomainlist = new ArrayList<String>(Arrays.asList(PropertyLoader.getInstance().getProperty("stg_new_ping_domain_list").split(",")));
	
	if (LT_LIST != null)
		ltDomainlist = new ArrayList<String>(Arrays.asList(LT_LIST.split(",")));
	else
		ltDomainlist = new ArrayList<String>(Arrays.asList(PropertyLoader.getInstance().getProperty("lt_new_ping_domain_list").split(",")));
	
	if (PROD_LIST != null)
		prdDomainlist = new ArrayList<String>(Arrays.asList(PROD_LIST.split(",")));
	else 
		prdDomainlist = new ArrayList<String>(Arrays.asList(PropertyLoader.getInstance().getProperty("prod_new_ping_domain_list").split(",")));
 
%>
<html>
	<head>
		<title>Pega Health</title>
		
		<script>
			var intervalVar;
			function checkAll(hdrCheckboxObj) {
			  var cbs = document.getElementsByTagName('input');
			  for(var i=0; i < cbs.length; i++) {
			    if(cbs[i].type == 'checkbox' && cbs[i].id != 'hdrcheckbox' && cbs[i].id != 'brmsadmhdrcheckbox' && !cbs[i].disabled && cbs[i].style.visibility != 'hidden') {
			    	
			    	if((hdrCheckboxObj.id == 'brmsadmhdrcheckbox' && cbs[i].id == 'brmsadmchkbox') || ( hdrCheckboxObj.id == 'hdrcheckbox' && cbs[i].id != 'brmsadmchkbox') ){
			    		cbs[i].checked = hdrCheckboxObj.checked;
					} 
			    }
			  }
			  if(hdrCheckboxObj.id == 'brmsadmhdrcheckbox'){
				  enableActionButton('Admin_Restart_Action', 'server');  
			  } else{
				  enableActionButton('Restart_Button_Action', 'serverhealthchk');
			  }
			}
				
			function checkUncheckHdrCheckBox(checkBoxObj) {
				var brmsAdmFlag = true;
				var pingFailFlag = true;
				
				var cbs = document.getElementsByTagName('input');
				for (var i = 0; i < cbs.length; i++) {
					if (cbs[i].type == 'checkbox' && cbs[i].id != 'hdrcheckbox' && cbs[i].id != 'brmsadmhdrcheckbox'
							&& !cbs[i].disabled && cbs[i].style.visibility != 'hidden' && !cbs[i].checked) {
						if(cbs[i].id == 'brmsadmchkbox'){
							brmsAdmFlag = false;
						} else{
							pingFailFlag = false;		
						}
					}
				}
				
				if(checkBoxObj.id == 'brmsadmchkbox' && null != document.getElementById('brmsadmhdrcheckbox')){
					document.getElementById('brmsadmhdrcheckbox').checked = brmsAdmFlag;
				} else if(checkBoxObj.id != 'brmsadmchkbox' && null != document.getElementById('hdrcheckbox')){
					document.getElementById('hdrcheckbox').checked = pingFailFlag;
				}
			}
			
			function blinkIt() {
				if(document.all.tags('blink').length > 0){
					for (i = 0; i < document.all.tags('blink').length; i++) {
						s = document.all.tags('blink')[i];
						s.style.visibility = (s.style.visibility == 'visible') ? 'hidden'
								: 'visible';
					}
				} else{
					clearInterval(intervalVar);
				}
			}

			function pingServer(url) {
				var xmlHttp;
				
				if (window.XMLHttpRequest) {
					// code for IE7+, Firefox, Chrome, Opera, Safari
					xmlHttp = new XMLHttpRequest();
					alert("from IF");
					alert(url);
				} else {
					// code for IE6, IE5
					xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
					alert("from else");
				}
				
				
				xmlHttp.onreadystatechange = function() {
					if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
						clearTimeout(xmlHttpTimeout);
						document.getElementById(url).innerHTML = xmlHttp.responseText;
						
						if (null != document.getElementById(url + '_Restart')) {
							var status = document.getElementById(url).innerHTML;
							alert(status);
							if (status.indexOf("EMAN OK") != -1 || status.indexOf("Pinging") != -1) {
								alert("i am in IFFF");
								document.getElementById(url + '_Restart').style.visibility = 'hidden';
							} else {
								
								document.getElementById(url + '_Restart').style.visibility = 'visible';
							}
						}
					} 
				}
				
				xmlHttp.open("POST", url, true);
				xmlHttp.send();
				// Timeout to abort in 10 seconds
				var xmlHttpTimeout = setTimeout(
						function() {
							xmlHttp.abort();
							document.getElementById(url).innerHTML  =  "Request timed out.";
							if(null != document.getElementById(url + '_Restart')){
								document.getElementById(url + '_Restart').style.visibility = 'visible';	
							}
						}, 30000);
				var status = document.getElementById(url).innerHTML;
				alert(status);

			}
			function onWindowLoad() {
				var domainApp='<%=domainApp%>';
				selectDomObj=document.getElementById('selectDom');
				for (var i = 0; i < selectDomObj.options.length; i++) {
					if (selectDomObj.options[i].text.toUpperCase() == domainApp.toUpperCase()) {
						selectDomObj.options[i].selected = true;
					}
				}
				
				var env='<%=env%>';
				var selectEnvObj=document.getElementById('selectEnv');
				for (var i = 0; i < selectEnvObj.options.length; i++) {
					if (selectEnvObj.options[i].text.toUpperCase() == env.toUpperCase()) {
						selectEnvObj.options[i].selected = true;
					}
				}
				
				intervalVar = setInterval('blinkIt()', 500);
				var divs = document.getElementsByTagName("div");
				for (var i = 0; i < divs.length; i++) { 
					if (divs[i].id != "") {
						pingServer(divs[i].id);
					}
				}
			}
			
			function enableActionButton(btnId, chkboxName) {
				// Enabling/Disabling the Action button
				var btnObj = document.getElementById(btnId);
				if (isAnyChecked(chkboxName, btnId)) {
					btnObj.style.visibility = 'visible';
				} else if (document.getElementById(btnId) != null) {
					btnObj.style.visibility = 'hidden';	
				}
			}

			function adminRestartAction() {
				document.pegaHealthForm.action = "serverToolsBulkAction.jsp";
				return true;
			}
			
			function restartAction() {
				var cbs = document.getElementsByTagName('input');
				var servers = "";
				for(var i=0; i < cbs.length; i++) {
					if (cbs[i].type == 'checkbox' && cbs[i].id != 'hdrcheckbox' && cbs[i].id != 'brmsadmhdrcheckbox' && cbs[i].id != 'brmsadmchkbox'
						&& !cbs[i].disabled && cbs[i].style.visibility != 'hidden' && cbs[i].checked && cbs[i].name == 'serverhealthchk') {
						servers += cbs[i].value +"#"; 
					}
					  
				}
				if(servers.length > 0){
					servers = servers.substring(0, servers.length-1);	
				}
				document.getElementById('serverAction').value= '01_/killWL.sh,03_/startWL.sh';
				document.getElementById('servers').value= servers;
				document.pegaHealthForm.action = "serverToolsBulkActionOutput.jsp";
				return true;
			}
			
			function isAnyChecked(checkBoxName, btnId) {
				var checkBoxes = document.getElementsByName(checkBoxName);
				for (var i = 0; i < checkBoxes.length; i++) {
					if( (btnId == 'Restart_Button_Action' && checkBoxes[i].id != 'brmsadmchkbox' && checkBoxes[i].checked) ||
						(btnId == 'Admin_Restart_Action' && checkBoxes[i].id == 'brmsadmchkbox' && checkBoxes[i].checked) ){
						return true;
					}
				}
				return false;
			}
			function filterOnDomainEnv(path){
				var domain=document.getElementById('selectDom').value;
				var env=document.getElementById('selectEnv').value;
				document.pegaHealthForm.action = path + "/jsp/pegaNodeLevelHealth.jsp?domainApp="+domain+"&env="+env+"&page=1";
				document.pegaHealthForm.submit();
			}
		</script>
	</head>
	<body onLoad="onWindowLoad();">
		
		
		<h3 class="headerText" align="center">Pega Health</h3>
		<form name="pegaHealthForm" method="post">
		<table align="center" width="50%" class="OddEvenTable">
			<tr><th colspan="2" align="left">Select Domain : &nbsp;<select id="selectDom" onchange="filterOnDomainEnv('<%=strURLPath%>')">
												<option value="All">ALL</option>
												<%for(String domain :  sortedDomainList){
												%>
													<option value="<%=domain%>"><%=domain%></option>
												<%}%>
											 </select>
			</th>
			<th colspan="2" align="left">Select Env : &nbsp;<select id="selectEnv" onchange="filterOnDomainEnv('<%=strURLPath%>')">
												<option value="All">ALL</option>
												<%
													for(String envVal : sortedEnvList){
												%>
												<option value="<%=envVal.toLowerCase()%>"><%=envVal.toUpperCase() %></option>
												<%} %>
											 </select>
			</th></tr>
			<tr>
				<th width="6%" align="left"><input id ="hdrcheckbox" type="checkbox" onclick = "checkAll(this)" name="hdrcheckbox"/> Select All</th>
				<th width="15%"align="left">
					<input name="Restart_Button_Action" id="Restart_Button_Action" type="submit" value="Kill and Start" style="visibility: hidden;margin-right:10px;" onClick="restartAction()"/>
					Host Name
				</th>
				<th width="19%" align="right">
					Status<input name="Admin_Restart_Action" id="Admin_Restart_Action" type="submit" value="Server Actions" style="visibility: hidden;margin-left:100px;" onClick="adminRestartAction()"/>
				</th>
		<%
				if(isReInstallStartAdmin){
		%>		
				<th width="10%" align="left"><input id ="brmsadmhdrcheckbox" type="checkbox" onclick = "checkAll(this)" name="brmsadmhdrcheckbox"/>Select All to Restart </th>
		<%
				}
		%>
			</tr>
		<%
		
		if (fileList.length > 1) {
			String[] fileLineData = null;
			String lineDomain = null;
			String linePrePath = null;
			ArrayList<String> finalFileLines= new ArrayList<String>();
			
			String dataCenterFrom = request.getContextPath().split("/")[1];
			String dataCenterTo = null;
			boolean adminInd = false;			
			
			
			for(File file:fileList){
				fileLines = FileUtils.readLines(file);
				if (fileLines.size() >= 1) {
					for (String fileLine : fileLines) {
						if (!fileLine.trim().startsWith("#")) {
							fileLineData = fileLine.split(",");
							if (fileLineData[0].indexOf("adm") == -1 && fileLineData.length > 9 && !"".equals(fileLineData[9])) {
								serverDomain = fileLineData[9];
								if(domainApp.toLowerCase().equals("all") && env.toLowerCase().equals("all") ){
									finalFileLines.add(fileLine);	
								}else if(domainApp.toLowerCase().equals("all") && !env.toLowerCase().equals("all")){
									if(fileLineData.length > 10 && !"".equals(fileLineData[10])){
										if(fileLineData[10].equalsIgnoreCase(env)){
											finalFileLines.add(fileLine);
										}
									}
								}else if(!domainApp.toLowerCase().equals("all") && env.toLowerCase().equals("all")){
									if(serverDomain.equalsIgnoreCase(domainApp)){
										finalFileLines.add(fileLine);
									}
								}else{
									if(serverDomain.equalsIgnoreCase(domainApp) && fileLineData[10].equalsIgnoreCase(env)){
										finalFileLines.add(fileLine);
									}
								}
							}
						}
					}
				}
			}	
			fileLineData = null;
			
			int actualSize=finalFileLines.size();
			int lastIndex=0;
			int startIndex=0;
			
			if(pageNum<=0){
				pageNum=0;
				lastIndex=20;
			}
			else
				lastIndex=pageNum*20;
			
			int totalPages=0;
			if(actualSize%20 ==0)
				totalPages=actualSize/20;
			else
				totalPages=actualSize/20+1;
			
			if(actualSize>=1){
				if(lastIndex>actualSize){
					pageNum=totalPages;
					lastIndex=actualSize;
					startIndex=lastIndex-(actualSize%20);
				}
				else{
					startIndex=lastIndex-20;
				}				
				if(startIndex<0)
					startIndex=0;
				for(int i=startIndex;i<lastIndex;i++){
					
					String fileLine=finalFileLines.get(i);
					fileLineData=fileLine.split(",");
					adminInd = false;
					linePrePath = request.getContextPath()+"/";
					lineDomain = ((fileLineData.length>9 && !"".equals(fileLineData[9]))?fileLineData[9].substring(1, (fileLineData[9].substring(1).indexOf("/")!=-1?fileLineData[9].substring(1).indexOf("/") + 1:fileLineData[9].length())) : null);
					/* if (isSysAdmin || ((fileLineData.length>9 && !"".equals(fileLineData[9]))?UserUtil.isUserValid(userId, UserUtil.getRegEx("BRMS", EnvBiListMapCache.getInstance().getLdapEnvs(fileLineData[10], "|", "(", ")"), fileLineData[9].substring(1, (fileLineData[9].substring(1).indexOf("/")!=-1?fileLineData[9].substring(1).indexOf("/") + 1:fileLineData[9].length())), null, "ADMIN")):false)) {
						adminInd = true;
					} */
					adminInd = true;
					dataCenterTo = fileLineData[0].split("-")[1];			
		%>			
			<tr>
				<td width="6%">
		<%	
					if (adminInd && fileLineData[0].indexOf("adm") == -1 && dataCenterFrom.equalsIgnoreCase(dataCenterTo)) { 
		%>								
					<input name="serverhealthchk" id="<%=request.getScheme()%>s://<%=request.getServerName()%><%=fileLineData[9]%>/<%=fileLineData[0]%>/brmsadmin/eman_Restart" value="<%=fileLineData[0]%>,<%=fileLineData[1]%>,<%=fileLineData[4]%>,<%=lineDomain%>,<%=fileLineData[9]%>,<%=linePrePath%>" type="checkbox" onclick="checkUncheckHdrCheckBox(this);enableActionButton('Restart_Button_Action', this.name)"/>				
		<%	
					}
		%>
				</td>
				<td width="15%">
					<b><%=fileLineData[0]%>:<%=fileLineData[3] + fileLineData[9] %></b>
				</td>
				<td width="19%">
					<%
						String domName = fileLineData[9];
						domName = domName.substring(domName.lastIndexOf("/") + 1);
						String pingUrl="";
						String idContent = "";
						if (fileLineData[10].equalsIgnoreCase("dev")) {
							pingUrl = devDomainlist.contains(domName) ? "PRRestService/CiscoSample/Services/eman" : "brmsadmin/eman";
							idContent = "https://ibpmadm-dev.cisco.com" + fileLineData[9] + "/" + fileLineData[0] + "/" + pingUrl;
							//idContent = "http://"+ fileLineData[0] + ":" +fileLineData[3] + fileLineData[9] + "/" + pingUrl;
						} else if (fileLineData[10].equalsIgnoreCase("stg") || fileLineData[10].equalsIgnoreCase("stage") ) {
							pingUrl = stgDomainlist.contains(domName) ? "PRRestService/CiscoSample/Services/eman" : "brmsadmin/eman";
							idContent = "https://ibpmadm-stage.cisco.com" + fileLineData[9] + "/" + fileLineData[0] + "/" + pingUrl;
							//idContent = "http://"+ fileLineData[0] + ":" +fileLineData[3] + fileLineData[9] + "/" + pingUrl;
						} else if (fileLineData[10].equalsIgnoreCase("lt")) {
							pingUrl = ltDomainlist.contains(domName) ? "PRRestService/CiscoSample/Services/eman" : "brmsadmin/eman";
							idContent = "https://ibpmadm-lt.cisco.com" + fileLineData[9] + "/" + fileLineData[0] + "/" + pingUrl;
							//idContent = "http://"+ fileLineData[0] + ":" +fileLineData[3] + fileLineData[9] + "/" + pingUrl;
						} else if (fileLineData[10].equalsIgnoreCase("prod") || fileLineData[10].equalsIgnoreCase("prd")) {
							pingUrl = prdDomainlist.contains(domName) ? "PRRestService/CiscoSample/Services/eman" : "brmsadmin/eman";
							idContent = "https://ibpmadm.cisco.com" + fileLineData[9] + "/" + fileLineData[0] + "/" + pingUrl;
							//idContent = "http://"+ fileLineData[0] + ":" +fileLineData[3] + fileLineData[9] + "/" + pingUrl;
						} else {
							pingUrl = "brmsadmin/eman";
							idContent = "http://" + request.getServerName() + fileLineData[9] + "/" + fileLineData[0] + "/" + pingUrl;
						}
						System.out.println("*******##### PEGA HEALTH ID CONTENT ::: "+idContent);
						
						%>
								<div id="<%=idContent%>" class="pageText" style="color: #055A78;font-weight: bold;font-style: italic;">
								<blink>Pinging....</blink>
								</div>
				</td>
		<%	
								if (isReInstallStartAdmin && dataCenterFrom.equalsIgnoreCase(dataCenterTo)) { 
		%>					<td width="10%">						
								<input name="server" id="brmsadmchkbox" value="<%=fileLineData[0]%>,<%=fileLineData[1]%>,<%=fileLineData[4]%>,<%=lineDomain%>,<%=fileLineData[9]%>,<%=linePrePath%>" type="checkbox" onclick="checkUncheckHdrCheckBox(this);enableActionButton('Admin_Restart_Action', this.name)"/>
							</td>							
		<%	
								}
		%>		
			</tr>
		<%
				}
			}if(totalPages > 1)	{ %>
			<tr><td colspan="4" align="center">
			Pages : 
			<%
				for(int j=1;j<=totalPages ;j++){ 
			%>
				<a href="<%=strURLPath %>/jsp/pegaNodeLevelHealth.jsp?domainApp=<%=domainApp%>&env=<%=env%>&page=<%=j%>"<%if(j==pageNum){%> style="color:red;"<%}%>><%=(j==1) ? "First" : (j==totalPages) ? "Last" : j %>&nbsp;</a>
				<% 	}%>
				<a href="<%=strURLPath %>/jsp/pegaNodeLevelHealth.jsp?domainApp=<%=domainApp%>&env=<%=env%>&page=<%= (pageNum>1) ? pageNum-1 : 1 %>"> &lt;&lt;Prev&nbsp;</a>
				<a href="<%=strURLPath %>/jsp/pegaNodeLevelHealth.jsp?domainApp=<%=domainApp%>&env=<%=env%>&page=<%= (pageNum<totalPages) ? pageNum+1 : totalPages %>"> Next&gt;&gt;&nbsp;</a>
				<a href="<%=strURLPath %>/jsp/pegaNodeLevelHealth.jsp?domainApp=All&env=All&page=1">Pega Health</a>
			</td></tr>
		<% 		}
			} else {
		%>
			<tr>
				<td colspan="4">No server data found.</td>
			</tr>
		<%
		}
		%>
		
		
		</table>
		
		<input type="hidden" id="servers" name="servers"/>
		<input type="hidden" id="serverAction" name="serverAction" />
		
	</form>		
	</body>
</html>