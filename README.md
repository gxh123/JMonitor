##### JMonitor是一个监控系统，用于Java应用程序的监控。

##### JMonitor系统结构
这里有一段简单介绍

##### JMonitor支持的监控消息类型包括：
+  **Transaction**	主要用来记录某段代码的执行时间和次数。
+  **Event**	    主要用来记录某个事件发生的次数。
+  **Status**	    主要用来记录状态信息，包括CPU使用率、内存、堆、线程等信息。
+  **Problem**	    主要用来记录程序运行过程中的一些异常或者错误。


Quick Started
---------------------
运行JMonitor-server
1、进入JMonitor-master\jmonitor-core文件夹，执行：mvn install
2、连接mysql数据库，执行JMonitor-master下的jmonitor.sql文件
3、修改JMonitor-master\jmonitor-core\src\main\resources下的c3p0.properties，主要修改c3p0.user、c3p0.password这两个属性
4、用IDEA打开jmonitor-core，jmonitor-web这两个项目，运行jmonitor-core\src\main\java\com\jmonitor\core\server下的JMonitorServer.java
5、再运行jmonitor-core\src\test\java下的JMonitorTest.java进行测试

运行web
1、进入JMonitor-master\jmonitor-web文件夹，执行：mvn install
2、修改JMonitor-master\jmonitor-web\src\main\resources下的jfinal_config.txt，主要修改user、password这两个属性
3、用IDEA打开jmonitor-web这个项目，通过tomcat运行项目