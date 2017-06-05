<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.cisco.appsui.core.PageComposer" %>

<%@ taglib uri="/WEB-INF/camp.tld" prefix="camp"%>

<%

    // set rootPath dynamically in CCX env, since path differs in dev, stage, and prod

	System.setProperty("com.cisco.appsui.rootPath", config.getServletContext().getRealPath("/") +

		"/WEB-INF/templates/");


     // instantiate PageComposer

	PageComposer myPage = new PageComposer(

		config.getServletContext().getRealPath("/") +

                	"/WEB-INF/data/PageComposer.properties",true);

					
     // set PageComposer entitlement level with access level from LDAP

     // do not hard code as shown here - must get LDAP access level from entitlement API

     	myPage.setEntitlementLevel("4");


     // get CDC image path

        String cdcImagePath = myPage.getProperty("appImagePath");


%>

<%-- AppHeaderTag inserts the header of the application --%>

<camp:AppHeaderTag pageComposer="<%=myPage%>"/>

<%-- Insert your application content here --%>

<p><strong>Embedded Template Using Tags</strong></p>

<p> This example shows the basic embedded template page created with AppHeaderTag and AppFooterTag.</p>

<p>Image Path from PageComposer is: <%=cdcImagePath%></p>

<p><strong>Status</strong></p>

<ul>
	<li>This template was updated from Libra to MasterBrand in 2010. Details on this and any additional minor code changes can be found on the <a href="http://iwe.cisco.com/web/cdc-application-ui-services/news">CAMP Release Notes page</a>.</li>
</ul>


<%-- AppFooterTag inserts the footer of the application --%>

<camp:AppFooterTag pageComposer="<%=myPage%>"/>