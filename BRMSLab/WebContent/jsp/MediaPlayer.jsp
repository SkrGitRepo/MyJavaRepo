<%@page import="java.util.List"%>
<%@page import="com.cisco.brmspega.util.file.GetMediaFileList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%
String mediaHomeDir = "/Users/sumkuma2/git/MyJavaRepo/BRMSLab/WebContent/media";
GetMediaFileList mediaObj = new GetMediaFileList();
List<String> mp3AudioList = mediaObj.getMP3FileList(mediaHomeDir);

%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SkrMediaPlayer</title>
</head>
<body>

<div align="center">
<p><h3>Sumit's Media Library</h3></p>
<table border="0" class="pageText" style="border-collapse: collapse;">
<% for (String mp3FileName : mp3AudioList) { %>
<tr>
<td bgcolor="LightGrey" style="border: 1px solid red;"> <%=mp3FileName%>
</td>
<td style="border: 1px solid red;">
<audio controls><source src="../media/<%=mp3FileName%>"  type="audio/mpeg">Your browser does not support the audio element.</audio>
</td>
</tr>
<%} %>
</table>
</div>


</body>
</html>