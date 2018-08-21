<%@ page language="java" import="java.util.*,com.xa.qyw.*"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>news.html</title>
	
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="this is my page">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    
    <!--<link rel="stylesheet" type="text/css" href="./styles.css">-->

  </head>
  
  <body>
    <div>
   	 <%=(String)request.getAttribute("newsDetail")%>
    </div>
  </body>
</html>
