#数据采集传输工程

#准备

###docker rabbitmq
~~~
docker run -d --hostname rabbitmq-server --name rabbitmq-server   -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin -p 15672:15672 -p 5672:5672 rabbitmq
~~~
~~~
rabbitmq-plugins enable rabbitmq_management
~~~
###kafka

###redis

#开发

##client模块
###数据监听
###数据传输
###发送消息和心跳

##server模块

###接收消息
###处理文件
###监测心跳
###下发采集配置

##公共对象

###redis心跳，在线状态
###采集配置
###多实例文件采集锁



