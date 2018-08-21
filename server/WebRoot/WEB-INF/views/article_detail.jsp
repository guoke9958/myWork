<%@ page language="java" import="java.util.*,com.xa.qyw.*"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>文章详情</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<style>
body,html {
	padding: 0;
	margin: 0;
	height: 100%;
	font-family: Arial, Microsoft YaHei;
	color: #111;
}
</style>
</head>
<body>
	<!--must content ul li,or shoupi-->
	<div>

		<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin: 0 auto;">
			<tr>
				<td align="center"><h3>${article.title}</h3></td>
			</tr>

			<tr>
				<td align="center">${article.newsContent}</td>
			</tr>
		</table>


	</div>

</body>
</html>