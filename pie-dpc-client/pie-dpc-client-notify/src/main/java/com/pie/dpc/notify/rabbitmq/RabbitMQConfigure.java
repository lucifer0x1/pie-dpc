package com.pie.dpc.notify.rabbitmq;

import com.pie.common.config.LOCAL_CONSTANTS_CONFIG;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/6 22:09
 * @Description TODO :
 **/

@Configuration
public class RabbitMQConfigure {

    public static final String DEFAULT_EXCHANGE_NAME = LOCAL_CONSTANTS_CONFIG.DEFAULT_EXCHANGE_NAME;
    public static final String DEFAULT_QUEUE_FILE_ARRIVAL =LOCAL_CONSTANTS_CONFIG.DEFAULT_QUEUE_FILE_ARRIVAL;
    public static final String DEFAULT_ROUTE_FILE_ARRIVAL = LOCAL_CONSTANTS_CONFIG.DEFAULT_ROUTE_FILE_ARRIVAL;


    /**
     * 配置交换机
     */
    @Bean
    DirectExchange defaultExchange(){
        /**
         * name:交换机的名称
         * durable：交换机有持久(durable)和暂存(transient)两个状态。
         * autoDelete：当所有与之绑定的消息队列都完成了对此交换机的使用后，删掉它。默认是true
         */
        return new DirectExchange(DEFAULT_EXCHANGE_NAME,true,false);
    }

    /**
     * 配置队列
     * 文件到达（监听到文件产生后发送消息的 队列名）
     **/
    @Bean
    Queue defaultQueue(){
        //durable,autoDeletet:
        //exclusive:只被一个连接使用，当连接关闭后，队列即将被删除
        return new Queue(DEFAULT_QUEUE_FILE_ARRIVAL,true,false,false);
    }


    /**
     * 绑定路由键
     * 文件到达路由KEY
     **/
    @Bean
    Binding myDirectBinding(){
        // 队列 -> 交换机 -> 路由键
        return BindingBuilder
                .bind(defaultQueue())
                .to(defaultExchange())
                .with(DEFAULT_ROUTE_FILE_ARRIVAL);
    }



    CachingConnectionFactory rabbitConnectionFactory(){
        CachingConnectionFactory factory  = new CachingConnectionFactory();

        return null;
    }
}
