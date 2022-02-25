package com.kong.common.rabbit.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@EnableScheduling
public class RabbitService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     *  发送消息
     * @param exchange 交换机
     * @param routingKey 路由键
     * @param message 消息
     */
    public boolean sendMessage(String exchange, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        return true;
    }
//    @Scheduled(cron = "0 0 8 * * ? ")
//    public void sendMsg(String exchange, String routingKey, Object message,String ttlTime) {
//        rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData ->{
//            correlationData.getMessageProperties().setExpiration(ttlTime);
//            return correlationData;
//        });
//    }

}

