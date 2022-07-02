#数据采集传输工程（PIE-DPC）

#准备

java runtime
~~~
docker pull azul/zulu-openjdk-alpine
~~~

###docker rabbitmq
~~~
docker run -d --hostname rabbitmq-server --name rabbitmq-server   -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin -p 15672:15672 -p 5672:5672 rabbitmq
~~~
~~~
rabbitmq-plugins enable rabbitmq_management
~~~
~~~
echo management_agent.disable_metrics_collector = false > management_agent.disable_metrics_collector.conf
~~~

###kafka

###redis

~~~
docker run -d --hostname redis-server --name redis-server -p 6379:6379 redis
~~~

#开发

##client模块（pie-dpc-client）

###pie-dpc-client-main

客户端启动模块

###pie-dpc-client-filelistener

监听文件到达：

<br>
（1）Linux 通过内核 inotify 监听文件到达情况，可以准确获取文件到达关闭事件
<br>
（2）JDK NIO 可以监听到文件到达传输事件（内部其实是短时间轮训事件）
<br>
（3）commons-io 只能监听到文件创建、大小变化（文件大小发生增长时），删除等操作

###pie-dpc-client-notify

文件通知模块：目前支持RabbitMQ通知消息

###pie-dpc-client-transfer

文件传输模块：讲匹配正则表达式成功的文件回传服务端 

<br>
<hr>
此处用于统一回传接口（目前支持FTP方式）

##server模块(pie-dpc-server)

###pie-dpc-server-main

服务端启动模块

###pie-dpc-server-receiver

接收数据模块：监听消息和FTPServer接收文件

###pie-dpc-server-status

状态检测模块：

<br>
(1)检测客户端心跳状态信息
<br>
(2)检测主动抓取服务器在线状态
<br>
(3)安装客户端功能，同时监控进程在线信息
<br>
(4)配置下发功能
<br>

###pie-dpc-server-web

前端页面交互逻辑接口

###pie-dpc-server-filemanager

处理服务端接收的文件管理，定时清除临时文件策略等

##公共对象

公共对象和通用功能

<hr>
collection:采集配置对象和采集数据目录配置记录
<hr>
ftp:FTP文件客户端传输功能
<hr>
heartbeat:客户端心跳功能及心跳内容数据结构
<hr>
notify:通知消息结构（系统外部DI消息和内部处理通知消息）

#接口

##采集配置接受接口数据结构（rest）

##客户端心跳数据结构（redis）

##文件回传目录约定（path）

##文件回传后消息通知数据结构（MQ）


# 安装启动install
~~~
java -jar pie-dpc-client-main-1.0-dev.jar --clientID=wangxiyue.docker.agent,id --clientIpAddress=agent.xybug.com --recvIpAddress=host.docker.internal--recvPort=2121
~~~

| 属性 | 含义 | 例子 |
|:---|:---:|:---:|
|clientID|客户端标识|wangxiyue.docker.agent.id|
|clientIpAddress|客户端IP地址|agent.xybug.com|
|recvIpAddress|数据回传IP地址|127.0.0.1|
|recvPort|数据回传端口|2121|

# pie-dpc-factory
解码入库部分，算法工厂，集成解码入库算法，解码算法根据消息执行
负责对算法调度，算法安装，进行管理，（包括算法调度串接 传递参数。）

