package com.kong.yygh.task.schedled;

import com.kong.common.rabbit.constant.MqConst;
import com.kong.common.rabbit.service.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduledTask {
    @Autowired
    private RabbitService rabbitService;
    //@Scheduled(cron = "0 0 8 * * ? ")
    @Scheduled(cron = "0/30 * * * * ?")
    public void taskPatient(){
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_TASK,MqConst.ROUTING_TASK_8,"10");
    }
}
