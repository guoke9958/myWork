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

			if (form1.type.value == "") {
				alert("请选择要上传的类型 0/android、1/ios、2/other");
				return false;
			}

			if (form1.newversion.value == "") {
				alert("请輸入上传的版本号");
				return false;
			}

		}
	</script>
	<!-- 定义表单 -->
	<form action="/YHT/api/news/add" method="post"
		enctype="multipart/form-data" name="form1" id="form1"
		onsubmit="validate()">
		<ul>
			<li>新闻主id：<input type="text" name="id" size="10" value="<%=(Integer)request.getAttribute("id")%>" />
			</li>
			<li>是否完成：<input type="text" name="is_complete" size="10" />
			</li>
			<li>发布者：<input type="text" name="author" size="10" />
			</li>
			<li>新闻标题：<input type="text" name="title" size="10" />
			</li>
			<li>转载出处：<input type="text" name="source" size="10" />
			</li>
			<li>转载地址：<input type="text" name="http_address" size="10" />
			</li>

			<li>图片地址：<input type="text" name="image" size="10" /></li>

			<li>新闻内容 ：<input style="width: 500px;height: 1000px" type="text"
				name="content" />
			</li>
			<li><input type="submit" name="Submit" value="添加" /> <input
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