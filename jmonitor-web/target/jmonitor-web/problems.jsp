<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>problems</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="/css/plugins/dataTables/datatables.min.css" rel="stylesheet">
    <link href="/css/plugins/tipso/css/tipso.min.css" rel="stylesheet" >
    <link href="/css/animate.css" rel="stylesheet">
    <link href="/css/style.css" rel="stylesheet">
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
                    <a href="/events"><i class="fa fa-bars"></i> <span class="nav-label">Events</span></a>
                </li>
                <li>
                    <a href="/transactions"><i class="fa fa-clock-o"></i> <span class="nav-label">Transactions</span></a>
                </li>
                <li>
                    <a href="/status"><i class="fa fa-bar-chart-o"></i> <span class="nav-label">Status</span>  </a>
                </li>
                <li class="cur">
                    <a href="/problems"><i class="fa fa-warning"></i> <span class="nav-label">Problems</span></a>
                </li>
                <li>
                    <a href="/docs"><i class="fa fa-tags"></i> <span class="nav-label">Docs</span>  </a>
                </li>
            </ul>

        </div>
    </nav>
    <!--<div id="page-wrapper" class="gray-bg dashbard-1">-->
    <div id="page-wrapper" class="gray-bg">
        <div class="row border-bottom">
            <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 10px">
            </nav>
        </div>


        <div class="wrapper wrapper-content animated fadeInRight">
            <div class="row">
                <div class="col-lg-12">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <div style="font-size: 16px;display: inline-block;color: #c91f18;">
                                ${startTime}:00:00
                                <%--2016-12-13 19:00:00--%>
                                &nbsp;&nbsp;to&nbsp;&nbsp;
                                ${endTime}:59:59
                            </div>

                            <div style="font-size: 17px;float:right;">
                                <span style="border-bottom:0" id="tipLastMonth" data-tipso="显示上一个月">
                                    <a href="/problems/decrease?step=month">[-1 month]</a> </span>&nbsp;&nbsp;
                                <span style="border-bottom:0" id="tipLastWeek" data-tipso="显示上一周">
                                    <a href="/problems/decrease?step=week">[-1 week]</a> </span>&nbsp;&nbsp;
                                <span style="border-bottom:0" id="tipLastDay" data-tipso="显示前一天">
                                    <a href="/problems/decrease?step=day">[-1 day]</a> </span>&nbsp;&nbsp;
                                <span style="border-bottom:0" id="tipLastHour" data-tipso="显示前一个小时">
                                    <a href="/problems/decrease?step=hour">[-1 hour]</a></span>&nbsp;&nbsp;
                                <span style="border-bottom:0" id="tipNow" data-tipso="显示现在">
                                    <a href="/problems/now">[now]</a> </span>&nbsp;&nbsp;
                                <span style="border-bottom:0" id="tipNextHour" data-tipso="显示下一个小时">
                                    <a href="/problems/increase?step=hour">[+1 hour]</a> </span>&nbsp;&nbsp;
                                <span style="border-bottom:0" id="tipNextDay" data-tipso="显示下一天">
                                    <a href="/problems/increase?step=day">[+1 day]</a> </span>&nbsp;&nbsp;
                                <span style="border-bottom:0" id="tipNextWeek" data-tipso="显示下一周">
                                    <a href="/problems/increase?step=week">[+1 week]</a> </span>&nbsp;&nbsp;
                                <span style="border-bottom:0" id="tipNextMonth" data-tipso="显示下一个月">
                                    <a href="/problems/increase?step=month">[+1 month]</a> </span>
                            </div>
                        </div>
                        <div class="ibox-content">
                            <div style="font-size: 15px;margin-bottom: 20px;">
                                <c:forEach items="${ips}" var="ip">
                                    <c:choose>
                                        <c:when test="${ip == currentIp}">
                                            <span style="color: #5b94ff;">[${ip}]</span>&nbsp;&nbsp;&nbsp;
                                        </c:when>
                                        <c:otherwise>
                                            <a style="font-size: 15px;margin-bottom: 20px;color: #717171" href="/problems?ip=${ip}"> [${ip}]</a>&nbsp;&nbsp;&nbsp;
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </div>
                            <div class="table-responsive">
                                <table class="table table-striped table-bordered table-hover " >
                                    <thead>
                                    <tr>
                                        <th>Type</th>
                                        <th>Total</th>
                                        <th>detail</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${problems}" var="problem" varStatus="status">
                                    <tr>
                                        <td style="width:300px">${problem.problemType}</td>
                                        <td style="width:300px">${problem.count}</td>
                                        <td>
                                            <div id="myCollapsible${status.index}"><a href="#detail${status.index}" data-toggle="collapse">Show Detail</a></div>
                                            <div id="detail${status.index}" class="collapse"  style="font-family: 'Courier New'; font-size: 15px;" >
                                                <pre>${problem.detail}</pre>
                                            </div>

                                        </td>
                                    </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Mainly scripts -->
<script src="/js/jquery-2.1.1.js"></script>
<script src="/js/bootstrap.min.js"></script>
<script src="/js/plugins/jquery-ui/jquery-ui.min.js"></script>
<script src="/js/plugins/dataTables/datatables.min.js"></script>
<script src="/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<script src="/js/plugins/tipso/js/tipso.min.js"></script>
<script src="/js/inspinia.js"></script>
<script src="/js/startup.js"></script>

</body>
</html>
