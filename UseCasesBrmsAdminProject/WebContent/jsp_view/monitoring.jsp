<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import="com.app.MasterHostServlet,com.app.HostListBean" language="java" %>

<%@ page import="java.util.*" session="true"%>
<% 
	String reqURI =  request.getRequestURI();
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/bootstrap.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-1.11.3/jquery-1.11.3.min.js"></script>
<script src="js/jquery-sparklines-2.1.2/jquery.sparkline.min.js"></script>

<title>Host Management</title>
</head>
<body>


<!-- Forms
      ================================================== -->
<div align="center">
      <div class="bs-docs-section">
        <div class="row">
          <div class="col-lg-12">
            <div class="page-header">
			  <b><a href="<%=contextPath%>">Home</a></b>
			  <b><a href="<%=contextPath%>/eman/">Ping EMAN</a></b>              
              <h1 id="forms">Manage Host Monitoring</h1>
            </div>
          </div>
        </div>

        <!--  <div class="row">
          <div class="col-lg-6"> -->
          <div class="container">
          <table width="50%" align='center'>
          <tr><td width="50%">
            <div class="well bs-component">
              <form class="form-horizontal" action="masterhost" method="POST">
                <fieldset>
                <div class="form-group">
                    <label for="select" class="col-lg-2 control-label">Lifecycle</label>
                    <div class="col-lg-10">
                      <select class="form-control" id="lifecycles" name="lifecycle">
                      	<option>Select Lifecycle</option>
                        <option value="dev">DEV</option>
                        <option value="stage">STAGE</option>
                        <option value="prod">PROD</option>
                      </select>
                      <br>
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <div class="col-lg-10 col-lg-offset-2">
                    	<!--  <button type="reset" class="btn btn-default">Cancel</button> -->
                        <button type="submit" class="btn btn-primary">Search Host</button>
                    </div>
                  </div>
                </fieldset>
              </form>
            </div>
            </td></tr></table>
            <div>
    
      </div>
      
 <hr/>     
<% 
 String message1 = (String) request.getAttribute("message");
 String lifecycle = (String) request.getAttribute("lifecycle");
                    		
 if  (message1 != null && lifecycle!=null) {
%>

<div class="cotainer">

<div class="panel panel-default">
  <div class="panel-body">
    <% 
    	out.println("<H3>Showing all hosts from Lifecycle : "+lifecycle.toUpperCase()+"</H3>");
    %>
  </div>
</div>

<form class="form-horizontal" action="savehoststatus" method="POST">
<fieldset>   
<table class="table table-striped table-hover ">
  <thead>
    <tr>
      <th>Disable Monitoring</th>
      <th>HostName</th>
      <th>PortNo</th>
      <th>Lifecycle</th>
    </tr>
  </thead>
  <tbody>
  
<% 
    	ArrayList<HostListBean> list = (ArrayList<HostListBean>) request.getAttribute("HostItems");
		if (list != null) {
	 		for(HostListBean hostlist : list) {
	 			out.println("<tr><td width='20%'><div class='form-group'><div class='col-lg-10'><div class='radio'><input type='checkbox' name='monstatus' id='monstatus' value='"+lifecycle+":"+hostlist.getHostName()+":"+hostlist.getHostPort()+"'></div></td>");
	 			out.println("<td width='40%'>"+hostlist.getHostName()+"</td>");
		 		out.println("<td width='40%'>"+hostlist.getHostPort()+"</td>");
		 		out.println("<td width='40%'>"+lifecycle.toUpperCase()+"</td></tr>");
	 		}
		}
%>
 <tr><td><div class="form-group">
    <div class="col-lg-10 col-lg-offset-2">
      <button type="submit" class="btn btn-primary">Save Status</button>
    </div>
 </div></td></tr>
  </tbody>
</table>

 
</fieldset>
</form>
</div>
<%
} 
%>
</div>
</body>
</html>