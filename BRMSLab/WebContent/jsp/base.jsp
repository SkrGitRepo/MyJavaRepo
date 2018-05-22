<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>EMANPrimeOpticalUsers</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <!-- <link rel="stylesheet" href="{% static 'css/bootstrap.css' %}" media="screen">  -->
    <link href="../css/bootstrap.min.css" rel='stylesheet'>
    <script src="../js/jquery-1.11.3/jquery-1.11.3.min.js"></script>
    <script src="../js/jquery-sparklines-2.1.2/jquery.sparkline.min.js"></script>

  </head>

  <body>
  <nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-2">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="/prime/optical">IBPM Platform - Administration</a>
    </div>

    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-2">
      <ul class="nav navbar-nav">
        <!--  <li class="active"><a href="#">Link <span class="sr-only">(current)</span></a></li>
        <li><a href="#">Link</a></li> -->
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">BRMS NON-PROD<span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu">
            <li><a href="/prime/optical/cpo-dev-superuser">NPRD2-DEV</a></li>
            <li class="divider"></li>
            <li><a href="/prime/optical/cpo-dev-sysadmin">NPRD1-DEV</a></li>
            <li class="divider"></li>
            <li><a href="/prime/optical/cpo-dev-networkadmin">NPRD1-STAGE</a></li>
            <li class="divider"></li>
            <li><a href="/prime/optical/cpo-dev-operator">NPRD2-STAGE</a></li>
            <li class="divider"></li>
            <li><a href="/prime/optical/cpo-dev-provisioner">NPRD1-LT</a></li>
            <li class="divider"></li>
            <li><a href="/prime/optical/cpo-dev-provisioner">NPRD2-POC</a></li>
          </ul>
        </li>
        
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">BRMS PROD<span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu">
            <li><a href="/prime/optical/cpo-prod-superuser/">PRD1 (ALLN)</a></li>
            <li class="divider"></li>
            <li><a href="/prime/optical/cpo-prod-sysadmin">PRD2 (AMSTERDOM)</a></li>
            <li class="divider"></li>
            <li><a href="/prime/optical/cpo-prod-networkadmin">PRD4 (RCDN)</a></li>
            <li class="divider"></li>
            <li><a href="/prime/optical/cpo-prod-operator">PRD3 (DR)</a></li>
          </ul>
        </li>
      </ul>
      
	  <!--  <form class='navbar-form navbar-right' role='search' action='/prime/optical/search/user' method='POST'> -->
	  <form class='navbar-form navbar-right' role='search' action='/prime/optical/search/user' method='GET'>
	
	  	 <div class="form-group">
		     
		     	<input class='form-control' placeholder='User Cisco ID' type='text' name='userid'>
		     
		 </div>
	  	 <div class='form-group'>
		  	 <select class='form-control' title='Select a search criteria' id='select' name='userrole'>
		       
		     
		        	<option value='cpo-all-userrole' selected>All-UserRole</option>
		     
		        	<option value='cpo-dev-superuser' selected>As-Dev-Superuser</option>
		     
		        	<option value='cpo-dev-sysadmin' seleted>As-Dev-Sysadmin</option>
		     
		        	<option value='cpo-dev-networkadmin' selected>As-Dev-NetworkAdmin</option>
		     
		        	<option value='cpo-dev-operator' selected>As-Dev-Operator</option>
		     
		        	<option value='cpo-dev-provisioner' selected>As-Dev-Provisioner</option>
		     
		        	<option value='cpo-prod-superuser' selected>As-Prod-Superuser</option>
		     
		        	<option value='cpo-prod-sysadmin' seleted>As-Prod-Sysadmin</option>
		     
		        	<option value='cpo-prod-networkadmin' selected>As-Prod-NetworkAdmin</option>
		     
		        	<option value='cpo-prod-operator' selected>As-Prod-Operator</option>
		     
		        	<option value='cpo-prod-provisioner' selected>As-Prod-Provisioner</option>
		     
		        <option value='cpo-all-userrole'>All-UserRole</option>
		        <option value='cpo-dev-superuser'>As-Dev-Superuser</option>
		      	<option value='cpo-dev-sysadmin'>As-Dev-Sysadmin</option>
		      	<option value='cpo-dev-networkadmin'>As-Dev-NetworkAdmin</option>
		      	<option value='cpo-dev-operator'>As-Dev-Operator</option>
		      	<option value='cpo-dev-provisioner'>As-Dev-provisioner</option>
		      	<option value='cpo-prod-superuser'>As-Prod-superuser</option>
		      	<option value='cpo-prod-sysadmin'>As-Prod-sysadmin</option>
		      	<option value='cpo-prod-networkadmin'>As-Prod-networkadmin</option>
		      	<option value='cpo-prod-operator'>As-Prod-operator</option>
		      	<option value='cpo-prod-provisioner'>As-Prod-provisioner</option>
		      </select>
		      
		      <button type='submit' class='btn btn-default' name='action' value='searchuser'>Search</button>
	     </div>
	 </form>
    </div>
  </div>
  </nav>
  

<!--   
  <div class="container">
        
      <div class="row">
         <p>{% block homepage %} {% endblock %} </p>
         <p>{% block cpo_prod_users %} {% endblock %} </p>
         <p>{% block cpo_dev_users %} {% endblock %} </p>
         <p>{% block search_result %} {% endblock %}</p>
         
      </div>
      <hr>

      <footer align='bottom'>
        <p>&copy; Cisco Infosystems. 2016</p>
      </footer>

    </div>  --><!-- /container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="{% static 'js/jquery.min.js' %}"></script>
    <script src="{% static 'js/bootstrap.min.js' %}"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="{% static 'js/ie10-viewport-bug-workaround.js' %}"></script>
    
  </body>
</html>

