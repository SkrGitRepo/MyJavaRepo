<html>
<head>
<title>AJAX in Servlet using JQuery and JSON</title>
<script src="js/jquery-1.11.1.js" type="text/javascript"></script>

<script>
$(document).ready(function() {

$('#sports').change(function(event) {
	var sports = $("select#sports").val();
	$.get('JsonServlet', {
		sportsName : sports
	}, function(jsonResponse) {

	var select = $('#player');
	select.find('option').remove();
 	  $.each(jsonResponse, function(index, value) {
	  $('<option>').val(value).text(value).appendTo(select);
      });
	});
	});
});

</script>
</head>
<body>
<h3>AJAX in Servlet using JQuery and JSON</h3>
	Select Favorite Sports:
	<select id="sports">
		<option>Select Sports</option>
		<option value="Football">Football</option>
		<option value="Cricket">Cricket</option>
	</select>
	<br /> <br /> 
	Select Favorite Player:
	<select id="player">
		<option>Select Player</option>
	</select>
</body>
</html>