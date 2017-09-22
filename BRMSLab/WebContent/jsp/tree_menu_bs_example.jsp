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
<body onload="setInitValues()">

 <div class="container">
<div class="row"><h3>BRSMAdmin Menu:</h3></div>
</div>

<div class="row">
  <div class="span3">
    <div class="well">
        <div>
            <ul class="nav nav-list">
            	<li><label class="tree-toggle nav-header">BRMS Servers</label>
                    <ul class="nav nav-list tree">
                        <li><a href="#">Servers by VMList</a></li>
                        <li><a href="#">Servers by EnvList</a></li>
                        <li><a href="#">Servers by DomainList</a></li>
                        <li><label class="tree-toggle nav-header">Buttons</label>
                            <ul class="nav nav-list tree">
                                <li><a href="#">Colors</a></li>
                                <li><a href="#">Sizes</a></li>
                                <li><label class="tree-toggle nav-header">Forms</label>
                                    <ul class="nav nav-list tree">
                                        <li><a href="#">Horizontal</a></li>
                                        <li><a href="#">Vertical</a></li>
                                    </ul>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </li>
                <li class="divider"></li>
                <li><label class="tree-toggle nav-header"><a href="#">BRMS Onramp</a></label>
                <li class="divider"></li>
                <li><label class="tree-toggle nav-header">BRMS LDAP</label>
                    <ul class="nav nav-list tree">
                        <li><a href="#">All Ldap Listing</a></li>
                        <li><a href="#">Ldap Group Listing</a></li>
                        <li><a href="#">Ldap User Listing</a></li>
                        <li><label class="tree-toggle nav-header">Create Ldap Mapping</label>
                            <ul class="nav nav-list tree">
                                <li><a href="#">OnRamp - EA Process</a></li>
                            </ul>
                        </li>
                    </ul>
                </li>
                <li class="divider"></li>
                <li><label class="tree-toggle nav-header">Server Monitoring</label>
                    <ul class="nav nav-list tree">
                        <li><a href="#">Pega Health</a></li>
                        <li><a href="#">Database Health</a></li>
                        <li><a href="#">BRMS Monitoring System</a></li>
                    </ul>
                </li>
                
                <li class="divider"></li>
                <li><label class="tree-toggle nav-header">BRMS Support</label>
                    <ul class="nav nav-list tree">
                        <li><a href="#">Manage Servers</a></li>
                        <li><a href="#">Database Health</a></li>
                        <li><a href="#">BRMS Monitoring System</a></li>
                    </ul>
                </li>
                
                <li class="divider"></li>
                <li><label class="tree-toggle nav-header">Responsive</label>
                    <ul class="nav nav-list tree">
                        <li><a href="#">Overview</a></li>
                        <li><a href="#">CSS</a></li>
                        <li><label class="tree-toggle nav-header">Media Queries</label>
                            <ul class="nav nav-list tree">
                                <li><a href="#">Text</a></li>
                                <li><a href="#">Images</a></li>
                                <li><label class="tree-toggle nav-header">Mobile Devices</label>
                                    <ul class="nav nav-list tree">
                                        <li><a href="#">iPhone</a></li>
                                        <li><a href="#">Samsung</a></li>
                                    </ul>
                                </li>
                            </ul>
                        </li>
                        <li><label class="tree-toggle nav-header">Coding</label>
                            <ul class="nav nav-list tree">
                                <li><a href="#">JavaScript</a></li>
                                <li><a href="#">jQuery</a></li>
                                <li><label class="tree-toggle nav-header">HTML DOM</label>
                                    <ul class="nav nav-list tree">
                                        <li><a href="#">DOM Elements</a></li>
                                        <li><a href="#">Recursive</a></li>
                                    </ul>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
    </div>
</div>

<!-- <vr/> -->

</body>
</html>