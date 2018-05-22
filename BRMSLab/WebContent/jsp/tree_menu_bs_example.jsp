<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
	<link href="../css/treemenu.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="../js/treemenu.js"></script>
	<script type="text/javascript">
	
	function setInitValues() {
		$('.tree-toggle').click(function () {
			$(this).parent().children('ul.tree').toggle(200);
		});
		$(function(){
		$('.tree-toggle').parent().children('ul.tree').toggle(200);
		})
	}
	
	
	</script>
	<!-- <link class="cssdeck" rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.3.1/css/bootstrap.min.css">
	<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.3.1/css/bootstrap-responsive.min.css" class="cssdeck">
	<script class="cssdeck" src="//cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
	<script class="cssdeck" src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.3.1/js/bootstrap.min.js"></script>
		 -->
	<!-- <link href="../css/bootstrap.css" rel="stylesheet" type="text/css" />
	<script src="../js/jquery-1.11.3/jquery-1.11.3.min.js"></script>
	<script src="../js/jquery-sparklines-2.1.2/jquery.sparkline.min.js"></script> -->
		
</head>
<body>

<%-- <div>
<%@include file="base.jsp"%>
</div> --%>
<%-- <div>

<%@include file="brmsmenu.jsp"%>
</div> --%>


<div align="center">

<table width="100%" border="1" cellspacing="2" cellpadding="2" bgcolor="#cee7ff">
<tr width="100%"> <%@include file="base.jsp"%></tr>
<tr>
<td width="20%"><%@include file="brmsmenu.jsp"%></td>
<td width="80%">World</td></tr>
</table>
</div>

</body>
</html>