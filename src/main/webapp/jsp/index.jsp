<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>
<%String root = request.getContextPath();%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>宁波-舟山核心港区仿真模拟系统</title>
    <link href="<%=root %>/image/favicon.ico" rel="shortcut icon">
    <link href="<%=root %>/css/themes/default/style.css" rel="stylesheet" type="text/css" media="screen"/>
    <link href="<%=root %>/css/themes/css/core.css" rel="stylesheet" type="text/css" media="screen"/>
    <link href="<%=root %>/css/themes/css/print.css" rel="stylesheet" type="text/css" media="print"/>
    <link href="<%=root %>/js/uploadify/css/uploadify.css" rel="stylesheet" type="text/css" media="screen"/>
    <!--[if IE]>
    <link href="<%=root %>/css/themes/css/ieHack.css" rel="stylesheet" type="text/css" media="screen"/>
    <![endif]-->

    <!--[if lte IE 9]>
    <script src="<%=root %>/js/dwz/speedup.js" type="text/javascript"></script>
    <![endif]-->

    <script src="<%=root %>/js/dwz/jquery-1.7.2.js" type="text/javascript"></script>
    <script src="<%=root %>/js/dwz/jquery.cookie.js" type="text/javascript"></script>
    <script src="<%=root %>/js/dwz/jquery.validate.js" type="text/javascript"></script>
    <script src="<%=root %>/js/dwz/jquery.bgiframe.js" type="text/javascript"></script>
    <script src="<%=root %>/js/xheditor/xheditor-1.2.1.min.js" type="text/javascript"></script>
    <script src="<%=root %>/js/xheditor/xheditor_lang/zh-cn.js" type="text/javascript"></script>
    <script src="<%=root %>/js/uploadify/scripts/jquery.uploadify.js" type="text/javascript"></script>

    <!-- svg图表  supports Firefox 3.0+, Safari 3.0+, Chrome 5.0+, Opera 9.5+ and Internet Explorer 6.0+
    <script type="text/javascript" src="chart/raphael.js"></script>
    <script type="text/javascript" src="chart/g.raphael.js"></script>
    <script type="text/javascript" src="chart/g.bar.js"></script>
    <script type="text/javascript" src="chart/g.line.js"></script>
    <script type="text/javascript" src="chart/g.pie.js"></script>
    <script type="text/javascript" src="chart/g.dot.js"></script>
    -->

    <!-- 可以用dwz.min.js替换前面全部dwz.*.js (注意：替换是下面dwz.regional.zh.js还需要引入)-->
    <script src="<%=root %>/js/dwz.min.js" type="text/javascript"></script>
    <script src="<%=root %>/js/dwz/dwz.regional.zh.js" type="text/javascript"></script>

    <script type="text/javascript">
        $(function () {
            DWZ.init("<%=root %>/css/themes/dwz.frag.xml", {
                loginUrl: "/harbour/login",	// 跳到登录页面
                statusCode: {ok: 200, error: 300, timeout: 301}, //【可选】
                pageInfo: {
                    pageNum: "pageNum",
                    numPerPage: "numPerPage",
                    orderField: "orderField",
                    orderDirection: "orderDirection"
                }, //【可选】
                keys: {statusCode: "statusCode", message: "message"}, //【可选】
                debug: false,	// 调试模式 【true|false】
                callback: function () {
                    initEnv();
                    $("#themeList").theme({themeBase: "<%=root %>/css/themes"}); // themeBase 相对于index页面的主题base路径
                }
            });
        });
        function logOutConfirmMsg(url) {
            alertMsg.confirm("确定退出吗?", {
                okCall: function () {
                    window.location.href = url;
                }
            });
        }
    </script>
</head>
<body scroll="no">

<div id="layout">
    <div id="header">
        <div class="headerNav">
            <a class="logo" href="https://github.com/Liuxboy" target="_blank">标志</a>
            <ul class="nav">
                <li><span style="color: #FF0000">您好,${user.userName}</span></li>
                <li>
                    <a href="javascript:;" onclick="logOutConfirmMsg('/harbour/exit')">退出</a>
                </li>
            </ul>
            <ul class="themeList" id="themeList">
                <li theme="azure">
                    <div class="selected">天蓝</div>
                </li>
                <li theme="default">
                    <div>蓝色</div>
                </li>
                <li theme="green">
                    <div>绿色</div>
                </li>
                <li theme="purple">
                    <div>紫色</div>
                </li>
                <li theme="silver">
                    <div>银色</div>
                </li>
            </ul>
        </div>
        <!-- navMenu -->

    </div>

    <div id="leftside">
        <div id="sidebar_s">
            <div class="collapse">
                <div class="toggleCollapse">
                    <div></div>
                </div>
            </div>
        </div>

        <div id="sidebar">
            <div class="toggleCollapse"><h2>宁波-舟山核心港区仿真模拟系统</h2>

            </div>
            <!--引入menu-->
            <c:import url="menu.jsp"/>
        </div>
    </div>

    <div id="container">
        <div id="navTab" class="tabsPage">
            <div class="tabsPageHeader">
                <div class="tabsPageHeaderContent"><!-- 显示左右控制时添加 class="tabsPageHeaderMargin" -->
                    <ul class="navTab-tab">
                        <li tabid="main" class="main"><a href="javascript:;"><span><span
                                class="home_icon">宁波-舟山核心港区仿真模拟系统</span></span></a></li>
                    </ul>
                </div>
                <div class="tabsLeft">left</div>
                <!-- 禁用只需要添加一个样式 class="tabsLeft tabsLeftDisabled" -->
                <div class="tabsRight">right</div>
                <!-- 禁用只需要添加一个样式 class="tabsRight tabsRightDisabled" -->
                <div class="tabsMore">more</div>
            </div>
            <ul class="tabsMoreList">
                <li><a href="javascript:;">宁波-舟山核心港区仿真模拟系统</a></li>
            </ul>

            <div class="navTab-panel tabsPageContent layoutBox">
                <div class="page unitBox">
                    <div class="accountInfo">
                        <p><span>宁波-舟山核心港区仿真模拟系统</span></p>
                    </div>
                    <div class="pageFormContent" layoutH="80" style="margin-right:230px">
                    </div>
                </div>

            </div>
        </div>
    </div>

</div>

<div id="footer">Copyright &copy; 2016 <a href="https://github.com/Liuxboy/" target="_blank">&nbsp;https://github.com/Liuxboy&nbsp;</a>
</div>
</body>
</html>