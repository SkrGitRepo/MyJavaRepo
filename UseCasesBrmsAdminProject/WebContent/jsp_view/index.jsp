<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/bootstrap.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-1.11.3/jquery-1.11.3.min.js"></script>
<script src="js/jquery-sparklines-2.1.2/jquery.sparkline.min.js"></script>
<script>
$(document).ready(function() {

$('#lifecycles').change(function(event) {
	var lifecycles = $("select#lifecycles").val();
	$.get('HostServlet', {
		lifecycleName : lifecycles
	}, function(jsonResponse) {

	var nodeSelect = $('#nodes');
	nodeSelect.find('option').remove();
 	  $.each(jsonResponse, function(index, value) {
	  $('<option>').val(value).text(value).appendTo(nodeSelect);
      });
	});
	});
});

</script>

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
              <h1 id="forms">Enable/Disable Host Monitoring</h1>
            </div>
          </div>
        </div>

        <!--  <div class="row">
          <div class="col-lg-6"> -->
          <div class="container">
          <table width="50%" align='center'>
          <tr><td width="50%">
            <div class="well bs-component">
              <form class="form-horizontal" action="monstatus" method="POST">
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
                    <label for="select" class="col-lg-2 control-label">Nodes</label>
                    <div class="col-lg-10">
                      <select class="form-control" id="nodes" name="node">
                        <option>Select Node</option>
                      </select>
                      <br>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-lg-2 control-label">Monitoring Status</label>
                    <div class="col-lg-10">
                      <div class="radio">
                        <label>
                          <input type="radio" name="monstatus" id="monstatus" value="enabled" checked="">
                          Enabled
                        </label>
                      </div>
                      <div class="radio">
                        <label>
                          <input type="radio" name="monstatus" id="monstatus" value="disabled">
                          Disabled
                        </label>
                      </div>
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <div class="col-lg-10 col-lg-offset-2">
                    	<button type="reset" class="btn btn-default">Cancel</button>
                        <button type="submit" class="btn btn-primary">Save</button>
                    </div>
                  </div>
                </fieldset>
              </form>
            </div>
            </td></tr></table>
            <div>
    
      </div>
</div>

</body>
</html>