<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>docs</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="css/plugins/dataTables/datatables.min.css" rel="stylesheet">
    <link href="css/plugins/tipso/css/tipso.min.css" rel="stylesheet" >
    <link href="css/animate.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
</head>

<body>
<div id="wrapper">
    <nav class="navbar-default navbar-static-side " role="navigation">
        <div class="sidebar-collapse">
            <ul class="nav metismenu" id="side-menu">
                <li class="nav-header">
                    <h2>JMonitor</h2>
                </li>
                <li>
                    <a href="events"><i class="fa fa-bars"></i> <span class="nav-label">Events</span></a>
                </li>
                <li>
                    <a href="transactions"><i class="fa fa-clock-o"></i> <span class="nav-label">Transactions</span></a>
                </li>
                <li>
                    <a href="status"><i class="fa fa-bar-chart-o"></i> <span class="nav-label">Status</span>  </a>
                </li>
                <li>
                    <a href="problems"><i class="fa fa-warning"></i> <span class="nav-label">Problems</span></a>
                </li>
                <li class="cur">
                    <a href="docs"><i class="fa fa-tags"></i> <span class="nav-label">Docs</span>  </a>
                </li>
            </ul>

        </div>
    </nav>
    <!--<div id="page-wrapper" class="gray-bg dashbard-1">-->
    <div id="page-wrapper" class="gray-bg">
        <div class="row border-bottom">
            <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 10px">
                <div class="navbar-header">
                </div>

            </nav>
        </div>
        <div class="wrapper wrapper-content animated fadeInRight">
            <div class="row">
                <div class="col-lg-12">
                    <div class="ibox float-e-margins">
                        <div style="background-color: white; font-size: 16px; padding-left: 20px">
                            <strong>JMonitor简介</strong></br>
                            ---------------------</br>
                            JMonitor是一个监控系统，用于Java应用程序的监控。它收集应用程序的信息，处理成报告，将报告存入mysql数据库，并可以在浏览器端展示。</br>
                            </br>
                            <strong>JMonitor系统结构</strong></br>
                            JMonitor分为两部分：JMonitor-core和JMonitor-web。</br>
                            JMonitor-core为系统的核心部分，主要功能有：提供可供调用的系统API来产生消息，收集与处理消息，将处理结果存入数据库。</br>
                            JMonitor-web为系统的展示部分，通过JFinal搭建而成，主要功能是将数据库中的处理结果在浏览器端展示，也可以展示JMonitor系统实时的处理结果。</br>
                            应用程序使用JMonitor时，需要引入JMonitor-core.jar包，具体API使用参考jmonitor-core\src\test\java下的JMonitorTest.java。</br>
                            </br>
                            <strong>JMonitor支持的监控消息类型包括：</strong></br>
                            Transaction:主要用来记录某段代码的执行时间和次数。</br>
                            Event:主要用来记录某个事件发生的次数。</br>
                            Status:主要用来记录状态信息，包括CPU使用率、内存、堆、线程等信息。</br>
                            Problem:主要用来记录程序运行过程中的一些异常或者错误。</br>
                            </br>
                            <strong>Quick Started</strong></br>
                            ---------------------</br>
                            运行JMonitor-server</br>
                            1、进入JMonitor-master\jmonitor-core文件夹，执行：mvn install</br>
                            2、连接mysql数据库，执行JMonitor-master下的jmonitor.sql文件</br>
                            3、修改JMonitor-master\jmonitor-core\src\main\resources下的c3p0.properties，主要修改c3p0.user、c3p0.password这两个属性</br>
                            4、用IDEA打开jmonitor-core这个项目，运行jmonitor-core\src\main\java\com\jmonitor\core\server下的JMonitorServer.java</br>
                            5、再运行jmonitor-core\src\test\java下的JMonitorTest.java进行测试</br>
                            </br>
                            运行web</br>
                            1、进入JMonitor-master\jmonitor-web文件夹，执行：mvn install</br>
                            2、修改JMonitor-master\jmonitor-web\src\main\resources下的jfinal_config.txt，主要修改user、password这两个属性</br>
                            3、用IDEA打开jmonitor-web这个项目，通过tomcat运行项目</br>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Mainly scripts -->
<script src="js/jquery-2.1.1.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/plugins/jquery-ui/jquery-ui.min.js"></script>
<script src="js/plugins/dataTables/datatables.min.js"></script>
<script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<script src="js/plugins/tipso/js/tipso.min.js"></script>
<script src="js/inspinia.js"></script>
<script src="js/startup.js"></script>

</body>
</html>
