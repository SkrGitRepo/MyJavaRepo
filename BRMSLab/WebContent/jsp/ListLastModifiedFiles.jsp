<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>



<%
File dir = new File("/opt/brms/shared/scripts");
File[] files = dir.listFiles();

Arrays.sort( files, new Comparator()
                           {
                               public int compare(Object a, Object b)
                               {
                                   File filea = (File)a;
                                   File fileb = (File)b;
                                   return (int)( filea.lastModified() - fileb.lastModified() ) ;
                               }
                           } );

out.print("<p>The following Accession numbers have been received:</b></p>");

out.print("<table width=700><tr>");
     out.print("<td width=30><b>#</b></td>");
     out.print("<td width=150><b>Accession #</b></td>");
     out.print("<td width=300><b>Date Received</b></td>");
     out.print("<td width=220><b>Last Mod</b></td>");
     out.print("<tr><td colspan=4><hr></td></tr>");
out.print("</tr></table>");

for (int i = 0; i < files.length; i++) 
{
     out.print("<table width=700><tr>");
          out.print("<td width=30><font size=\"2\">" + i + "</font></td>");
          out.print("<td width=150><a href=\"listseries.jsp?accession=" + java.net.URLEncoder.encode( files[i].getName() ) + "\">" + files[i].getName() + "</a></td>");
          out.print("<td width=300><font size=\"2\">" + new Date( files[ i ].lastModified() ) + "</font></td>");
          out.print("<td width=220>" + files[ i ].lastModified() + "</td>");
     out.print("</tr></table>");
}
%>





</body>
</html>