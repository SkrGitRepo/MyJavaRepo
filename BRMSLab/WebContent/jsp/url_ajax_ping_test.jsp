<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Ajax Ping Test</title>
 <script>
		function loadDoc(url, cFunction) {
		  var xhttp;
		  xhttp=new XMLHttpRequest();
		  xhttp.onreadystatechange = function() {
		    if (this.readyState == 4 && this.status == 200) {
		      cFunction(this);
		    } else if (this.readyState == 4 && this.status == 404) {
			  cFunction(this);
		    }
		  };
		  xhttp.open("GET", url, true);
		  xhttp.send();
		}
		function myFunction(xhttp) {
			alert('readyState'+ xhttp.readyState);
			alert('status'+ xhttp.status);
			alert('Status Text :: '+xhttp.statusText);
			alert('response'+ xhttp.responseText);
		  document.getElementById("demo").innerHTML =
		  xhttp.responseText;
		}
</script>

</head>
<body>
	<div id="demo">
	
		<h1>The XMLHttpRequest Object</h1>
		
		<button type="button"
			onclick="loadDoc('https://ibpm-stage.cisco.com/casp/app/PRRestService/CiscoSample/Services/eman', myFunction)">Change Content
		</button>
	</div>
</body>
</html>