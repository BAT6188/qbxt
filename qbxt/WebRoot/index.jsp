<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
response.setHeader("Access-Control-Allow-Origin", "http://12.40.10.3:8780");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>公安技侦情报管理系统</title>
    <link rel="stylesheet" type="text/css" href="extjs/ux/css/ItemSelector.css" />
	<!-- ExtJS4 基础文件  -->
	<!-- <link rel="stylesheet" type="text/css" href="extjs/resources/css/ext-all.css" /> -->
	<link rel="stylesheet" type="text/css" href="extjs/resources/css/ext-all-neptune.css" />
	<script type="text/javascript" src="extjs/bootstrap.js"></script>
	<script type="text/javascript" src="extjs/ux/form/MultiSelect.js"></script>
	<script type="text/javascript" src="extjs/ux/form/ItemSelector.js"></script>
	<script type="text/javascript" src="extjs/locale/ext-lang-zh_CN.js"></script>
	<!--script type="text/javascript" src="http://12.40.10.3:8980/load?v=1.5"></script--> 
	<!-- 应用系统 -->
	<!-- link rel="stylesheet" type="text/css" href="images/icons.css" /><!-- 图标 -->
	<link rel="stylesheet" type="text/css" href="css/webapp.css" /><!-- 自定义样式 -->
	<script type="text/javascript" src="webapp/Main.js"></script>
	<script type="text/javascript" src="webapp/utils/Msg.js"></script>
	<script type="text/javascript" src="webapp/utils/WinUtils.js"></script>
	<script type="text/javascript" src="webapp/utils/StringUtils.js"></script>
	<!--script src="kindeditor/KindEditor.js"></script-->
	<!--script src="kindeditor/kindeditor-all.js"></script-->
	<!-- 
	<script type="text/javascript" src="js/util.js"></script> -->
	<script type="text/javascript" src="jquery/jquery-1.9.0.js"></script>
	<script type="text/javascript" src="jquery/jquery.PrintArea.js"></script>
	<script type="text/javascript">
		Ext.onReady(Ushine.Main.init);
	</script>


</head>

<body>
	<div id="system_message_window"></div>
</body>
</html>