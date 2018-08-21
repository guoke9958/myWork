<%@ page language="java" import="java.util.*,com.xa.qyw.*"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>医生介绍</title>
<link rel="stylesheet" href="../../css/weui.css" />
<link rel="stylesheet" href="../../css/example.css" />
<script src="../../js/zepto.min.js"></script>
<script src="../../js/router.min.js"></script>
<script src="../../js/example.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<style>
body {
	text-align: center;
}

#parent {
	width: 100%;
	margin: 0 auto;
}
</style>
</head>
<body>


	<div class="weui_dialog_alert" id="dialog2" style="display: none;">
		<div class="weui_mask"></div>
		<div class="weui_dialog">
			<div class="weui_dialog_hd">
				<strong class="weui_dialog_title">温馨提示</strong>
			</div>
			<div class="weui_dialog_bd">联系医生，请下载求医网APP到医生主界面联系医生</div>
			<div class="weui_dialog_ft">
				<a href="http://www.qiuyiwang.com:8081/Doctor" class="weui_btn_dialog primary">确定</a>
			</div>
		</div>
	</div>


	<div id="parent">

		<img width="150px" height="180px"
			src="http://www.qiuyiwang.com/${doctor.userPhoto}">

		<p></p>


		<img  onclick="showDialog()" src="<%=request.getContextPath()%>/img/zixun.png">



		<h3>姓名：${doctor.trueName}</h3>
		<p>科室：${doctor.departmentName}</p>
		<p>医院：${doctor.hospitalName}</p>
		<p>擅长：${doctor.goodAt}</p>
		<p>介绍：${doctor.introduction}</p>

	</div>

	

<script language="javascript">
	function showDialog() {
		document.getElementById("dialog2").style.display="";//显
		return true;
	}
</script>


</body>
</html>