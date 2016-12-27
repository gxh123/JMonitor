JMonitor简介
---------------------
##### JMonitor是一个监控系统，用于Java应用程序的监控。它收集应用程序的信息，处理成报告，将报告存入mysql数据库，并可以在浏览器端展示。

##### JMonitor系统结构
JMonitor分为两部分：JMonitor-core和JMonitor-web。  
JMonitor-core为系统的核心部分，主要功能有：提供可供调用的系统API来产生消息，收集与处理消息，将处理结果存入数据库。  
JMonitor-web为系统的展示部分，通过JFinal搭建而成，主要功能是将数据库中的处理结果在浏览器端展示，也可以展示JMonitor系统实时的处理结果。 
JMonitor-web运行界面：
![image](https://github.com/gxh123/picture/blob/master/jmonitor/events.JPG)  
![image](https://github.com/gxh123/picture/blob/master/jmonitor/transactions.JPG)  
![image](https://github.com/gxh123/picture/blob/master/jmonitor/status1.JPG)  
![image](https://github.com/gxh123/picture/blob/master/jmonitor/status2.PNG)  
![image](https://github.com/gxh123/picture/blob/master/jmonitor/problems.JPG)  
应用程序使用JMonitor时，需要引入JMonitor-core.jar包，具体API使用参考jmonitor-core\src\test\java下的JMonitorTest.java。  

##### JMonitor支持的监控消息类型包括：
+  **Transaction**	主要用来记录某段代码的执行时间和次数。
+  **Event**	    主要用来记录某个事件发生的次数。
+  **Status**	    主要用来记录状态信息，包括CPU使用率、内存、堆、线程等信息。
+  **Problem**	    主要用来记录程序运行过程中的一些异常或者错误。


Quick Started
---------------------
##### 运行JMonitor-server  
1、进入JMonitor-master\jmonitor-core文件夹，执行：mvn install  
2、连接mysql数据库，执行JMonitor-master下的jmonitor.sql文件  
3、修改JMonitor-master\jmonitor-core\src\main\resources下的c3p0.properties，主要修改c3p0.user、c3p0.password这两个属性  
4、用IDEA打开jmonitor-core这个项目，运行jmonitor-core\src\main\java\com\jmonitor\core\server下的JMonitorServer.java  
5、再运行jmonitor-core\src\test\java下的JMonitorTest.java进行测试  

##### 运行web  
1、进入JMonitor-master\jmonitor-web文件夹，执行：mvn install  
2、修改JMonitor-master\jmonitor-web\src\main\resources下的jfinal_config.txt，主要修改user、password这两个属性  
3、用IDEA打开jmonitor-web这个项目，通过tomcat运行项目  