<!DOCTYPE html>
<html lang="zh-CN">

<head>
<meta charset="utf-8">
<title>医患通主页</title>
<link rel="stylesheet" href="http://cdn.bootcss.com/fullPage.js/2.8.2/jquery.fullPage.min.css">
<style>
* { margin: 0; padding: 0;}
ul { list-style-type: none;}
.top { position: fixed; left: 0; right: 0; top: 0; z-index: 999; height: 64px; background-color: rgba(0,63,76,0.6);}
.menu { width: 1000px; margin: 0 auto; overflow: hidden;}
.logo { float: left; margin-top: 12px;}
.logo a { display: inline-block; width: 48px; height: 48px; background-image: url(http://120.26.209.71/download/img/ic_launcher.png); background-size: 48px 48px;}
.nav { float: right; margin-top: 16px; font-size: 0;}
.nav li { display: inline-block; margin-left: 40px;}
.nav a { display: inline-block; height: 30px; line-height: 30px; font-size: 16px; color: #000; text-decoration: none;}
.nav a:hover { color: #83c3f3;}
.section { text-align: center; background-repeat: no-repeat; background-position: 50% 50%; background-size: cover; overflow: hidden;}

.section img{ width:100%;height:100%;}

#section0 { background-image: url(http://120.26.209.71/download/img/slider3.png);}
#section1 { background-image: url(http://120.26.209.71/download/img/slider2.png);}
#section2 { background-image: url(http://120.26.209.71/download/img/slider1.png);}
#section3 { background-image: url(http://120.26.209.71/download/img/slider4.png);}
.title1 { opacity: 0.8; font-size: 60px; line-height: 93px; background: url(images-14/titleline1.png) no-repeat center bottom;}
.title2 { opacity: 0.8; font-size: 40px; line-height: 75px;}
.title3 { font-size: 24px; font-family: 'PingFang-SC-UltraLight', '微软雅黑', '宋体'; line-height: 48px; margin-top: 20px; opacity: 0.7;}
#section1 .title1, #section1 .title2, #section2 .title1, #section2 .title2 { color: #fff; opacity: 1;}
#section1 .title3, #section2 .title3 { color: #fff;}
#section3 .title3 { color: #000;}
#section1 .title1 { background: url(images-14/titleline2.png) no-repeat center bottom;}
#section2 .title1 { background: url(images-14/titleline3.png) no-repeat center bottom;}
#section3 .title1 { background: url(images-14/titleline4.png) no-repeat center bottom;}
.mTxt1 { width: 526px; height: 195px; margin: 0 auto; position: relative; top: 25%; background: url(images-14/oneTxt.png) no-repeat 0 0;}
.DownloadBtn { cursor: pointer; margin: 85px auto 0; width: 220px; height: 50px; position: relative; background: #5396FF; color: #fff; line-height: 50px; font-size: 24px; border-radius: 25px;}
.DownloadBtn a { display: inline-block; width: 100%; height: 100%; color: #fff; font-family: 'PingFang-SC-UltraLight', '微软雅黑', '宋体';}
.mTxt1 { width: 526px; height: 195px; margin: 0 auto; position: relative; top: 25%; background: url(images-14/oneTxt.png) no-repeat 0 0;}
.DownloadBtn { cursor: pointer; margin: 85px auto 0; width: 220px; height: 50px; position: relative; background: #5396FF; color: #fff; line-height: 50px; font-size: 24px; border-radius: 25px;}
.DownloadBtn a { display: inline-block; width: 100%; height: 100%; color: #fff; font-family: 'PingFang-SC-UltraLight', '微软雅黑', '宋体'; text-decoration: none;}
#fp-nav ul li a span, .fp-slidesNav ul li a span { background-color: #fff;}


.inner { position: relative; width: 800px; margin: 0 auto;}
.dowebok-hd { position: fixed; z-index: 1000; width: 100%; height: 64px; background-color: #000}
.dowebok-hd h1 { float: left; width: 156px; height: 26px; margin-top: 20px;}
.dowebok-hd h1 a { display: block; width: 48px; height: 48px; text-indent: -9999px; background-image: url(http://120.26.209.71/download/img/ic_launcher.png);}
.dowebok-hd .nav { float: right; list-style-type: none;}
.dowebok-hd .nav li { float: left; margin-left: 10px;}
.dowebok-hd .nav a { float: left; padding: 0 10px; line-height: 64px; color: #fff; text-decoration: none;}
.dowebok-hd .nav a:hover { height: 62px; border-bottom: 2px solid #4cb803;}


.DownloadBtn { cursor: pointer; width: 220px; height: 50px; position: relative; background: #5396FF; color: #fff; line-height: 50px; font-size: 24px; 
border-radius: 25px;float: left; margin-left: 18%;margin-top: 20%;}
.DownloadBtn a { display: inline-block; width: 100%; height: 100%; color: #fff; font-family: 'PingFang-SC-UltraLight', '微软雅黑', '宋体';}
</style>
<script src="http://cdn.bootcss.com/jquery/1.8.3/jquery.min.js"></script>
<script src="http://cdn.bootcss.com/fullPage.js/2.8.2/jquery.fullPage.min.js"></script>
<script>
$(function() {
$('#fullpage').fullpage({
	navigation: true
});
});
</script>
</head>

<body>
<div class="top">
	<div class="menu">
		<h1 class="logo"><a href="http://www.qiuyiwang.com/zhuye.php"></a></h1>
			<ul class="nav">
			<li><a href="http://www.qiuyiwang.com/zhuye.php">APP官网</a></li>
            <li><a href="http://www.ftyiliao.com">公司首页</a></li>
			<li><a href="http://www.qiuyiwang.com/reg.php">注册</a></li>
		</ul>
	</div>
</div>
<div id="fullpage">
	<div class="section" id="section0">
		<div class="DownloadBtn"><a href="http://120.26.209.71/download/yht/app/a_qiuyiwangyht.apk" target="_blank">立即下载</a></div>
	</div>
	<div class="section">
	
	<img src="http://120.26.209.71/download/img/slider2.png" alt="快速创建移动站点">
	
	</div>
	<div class="section" >
	
	<img src="http://120.26.209.71/download/img/slider4.png" alt="快速创建移动站点">
	
	</div>
	<div class="section" >
	
	<img src="http://120.26.209.71/download/img/slider1.png" alt="快速创建移动站点">
	</div>
</div>

</body>

</html>

