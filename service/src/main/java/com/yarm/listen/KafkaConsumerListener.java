package com.yarm.listen;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.internals.Topic;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @program: yarm-mq
 * @description: kafka消费监听
 * @author: yarm.yang
 * @create: 2019-12-12 16:47
 */
@Component
@Slf4j
public class KafkaConsumerListener {

    private static final String KAFKA_TOPIC = "testDemo1";

    @KafkaListener(topics = "#{'${kafka.topics}'.split(',')}" )
    public void receive(String message, Acknowledgment ack) {
        try {
            log.info("topic[{}] received msg:{}",message);
        } catch (Exception e) {
            log.error("Consumer Listener topic[{}](message[{}]) error",KAFKA_TOPIC,message,e);
        } finally {
            log.debug("start commit offset");
            ack.acknowledge();//手动提交偏移量
            log.debug("stop commit offset");
        }
    }
}
