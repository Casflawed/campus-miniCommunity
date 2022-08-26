package com.flameking.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbitMQ 交换机 queue 配置
 */
@Configuration
public class RabbitMQConfig {
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("fanout_msg_exchange",true,false);
    }
    @Bean
    public Queue msgQueue(){
        return new Queue("msg.fanout.queue",true);
    }
    @Bean
    public Binding msgBinding(){
        return BindingBuilder.bind(msgQueue()).to(fanoutExchange());
    }
}
