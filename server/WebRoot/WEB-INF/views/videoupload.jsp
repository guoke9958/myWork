<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>新版本发布</title>
<style type="text/css">
ul {
	list-style: none;
}

li {
	padding: 5px;
}
</style>
</head>
<body>
	<!-- 定义表单 -->
	<form action="/YHT/api/video/create" method="post"
		enctype="multipart/form-data" name="form1" id="form1"
		onsubmit="validate()">
		<ul>
			<li>添加视频播放文件</li>
			</li>
			<li>选择文件： <input type="file" name="videofile" /> <!-- 文件上传组件 --></li>
			</li>
			<li>选择视频封面： <input type="file" name="videoimage" /> <!-- 文件上传组件 --></li>
			</li>
			<li>视频名称：<input type="text" name="name" size="10"/>
			</li>
			<li>视频类型:<input type="text" name="videotype" size="10"/>
			<li>(只能输入数字     1=电影,2=电视剧，3=电视直播，4=综艺，5=医疗手术，6=体育运动，7=动漫，8=游戏竞技，9=健康养生)</li>
			</li>
			<li><input type="submit" name="Submit" value="上传" /> <input
				type="reset" name="Submit2" value="重置" /></li>
		</ul>
		<%
			if (request.getAttribute("result") != null) { //判断保存在request范围内的对象是否为空
				out.println("<script >alert('" + request.getAttribute("result")
						+ "');</script>"); //页面显示提示信息    	  
			}
		%>
	</form>
</body>
</html>