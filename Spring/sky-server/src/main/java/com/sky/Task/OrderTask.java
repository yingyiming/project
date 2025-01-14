package com.sky.Task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron="0 * * * * ?")
    public void processTimeOutOrder(){
        log.info("定时处理超时订单：{}", LocalDateTime.now());

        //查询数据库来判断是否未支付时间已经超时
        List<Orders> byStatusAndOrderTimeLT = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15));

        if(byStatusAndOrderTimeLT!=null&&!byStatusAndOrderTimeLT.isEmpty()){
            for (Orders orders : byStatusAndOrderTimeLT) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单已超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    //处理一直处于派送中的订单，每天的凌晨一点来处理
    @Scheduled(cron="0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("定时处理配送订单：{}", LocalDateTime.now());

        //查询数据库
        List<Orders> byStatusAndOrderTimeLT = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusHours(-1));

        if(byStatusAndOrderTimeLT!=null&&!byStatusAndOrderTimeLT.isEmpty()){
            for (Orders orders : byStatusAndOrderTimeLT) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
