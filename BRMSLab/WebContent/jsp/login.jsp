<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="../css/bootstrap.css" rel="stylesheet" type="text/css" />
<script src="../js/jquery-1.11.3/jquery-1.11.3.min.js"></script>
<script src="../js/jquery-sparklines-2.1.2/jquery.sparkline.min.js"></script>


<title>Login Window</title>
</head>
<body>
<div align="center">
      <div class="bs-docs-section">
        <div class="row">
          <div class="col-lg-12">
            <div class="page-header">
              <h1 id="forms">Login Here</h1>
            </div>
          </div>
        </div>

        <!--  <div class="row">
          <div class="col-lg-6"> -->
          <div class="container">
          <table width="50%" align='center'>
          <tr><td width="50%">
            <div class="well bs-component">
              <form class="form-horizontal" action="LoginServlet" method="POST">
                <fieldset>
                <div class="form-group">
			      <label for="inputUser" class="col-lg-2 control-label">User Name</label>
			      <div class="col-lg-10">
			        <input type="text" class="form-control" id="inputuser" name="user" placeholder="username">
			      </div>
			    </div>
			    <div class="form-group">
			      <label for="inputPassword" class="col-lg-2 control-label">Password</label>
			      <div class="col-lg-10">
			        <input type="password" class="form-control" id="inputPassword" name="pwd" placeholder="Password">
			      </div>
			    </div>
			    <div class="form-group">
			      <div class="col-lg-10 col-lg-offset-2">
			        <button type="reset" class="btn btn-default">Cancel</button>
			        <button type="submit" class="btn btn-primary">Submit</button>
			      </div>
			    </div>
			    </fieldset>
              </form>
            </div>
            </td></tr></table>
            <div>
            </div>
		</div>

</div>
</div>


<!-- <form action="LoginServlet" method="post">

Username: <input type="text" name="user">
<br>
Password: <input type="password" name="pwd">
<br>
<input type="submit" value="Login">
</form> -->
</body>
</html>