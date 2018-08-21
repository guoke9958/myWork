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
	<script type="text/javascript">
		function validate() {
			if (form1.file.value == "") {
				alert("请选择要上传的文件");
				return false;
			}
			
			if(form1.type.value== ""){
			alert("请选择要上传的类型 0/android、1/ios、2/other");
				return false;
			}
			
			if(form1.newversion.value== ""){
			alert("请輸入上传的版本号");
				return false;
			}
			
		}
	</script>
	<!-- 定义表单 -->
	<form action="/YHT/api/app/create" method="post"
		enctype="multipart/form-data" name="form1" id="form1"
		onsubmit="validate()">
		<ul>
			<li>请选择要上传的版本文件：</li>
			<li>上传文件： <input type="file" name="file" /> <!-- 文件上传组件 --></li>
			<li>上次版本号 ：
			
			<%=(Integer)request.getAttribute("versionCode")%>
			
			</li>
			<li>版本号 ：<input type="text" name="newversion" size="10"/>
			</li>
			<li>版本名称 ：<input type="text" name="versionName" size="10"/>
			</li>
			<li>文件类型：<input type="text" name="type" size="10"/>
			</li>
			<li>是否强制更新：<input type="text" name="forces" size="10"/>
			</li>
			<li>更新内容为 ：<input type="text" name="updatecontent" />
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