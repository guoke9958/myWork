<%@ page language="java" import="java.util.*,com.xa.qyw.entiy.*"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width,initial-scale=1,user-scalable=0">
<title>加盟合作</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/weui.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/example.css" />
</head>
<body>

	<div class="weui_msg">
		<div class="weui_icon_area">
			<i class="weui_icon_success weui_icon_msg"></i>
		</div>
		<div class="weui_text_area">
			<h2 class="weui_msg_title">操作成功</h2>
			<p class="weui_msg_desc">感谢您的参与，稍后有客服人员会和您联系，请保持手机畅通，谢谢。</p>
		</div>
	</div>

</body>

<script language="javascript">
	function hint() {
		window.opener = null;
		window.open('', '_self');
		window.close();
		return true;
	}
</script>

</html>
