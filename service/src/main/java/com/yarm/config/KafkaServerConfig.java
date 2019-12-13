package com.yarm.config;

import com.yarm.constant.KafkaCommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: yarm-mq
 * @description: Kafka配置类
 * @author: yarm.yang
 * @create: 2019-12-12 18:09
 */
@Slf4j
@Configuration
public class KafkaServerConfig {

    @Value("${kafka.consumer.demo.bootstrapServers}")
    private String consumerServers;

    @Value("${kafka.producer.demo.bootstrapServers}")
    private String producerServcers;

    /**
    *@Description: 创建生产者配置,创建多个生产工厂和生产template时只修改地址，其他写死不动
    *@Param: 
    *@return: 
    *@Author: yarm.yang
    *@date: 2019/12/13
    */
    public Map<String, Object> createProducerConfigs(String producerServcers) {

        Map<String, Object> props = new HashMap<String, Object>();
        // 集群的服务器地址
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerServcers);
        //  缓冲等待发送 内存字节数 默认33554432
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, KafkaCommonConstant.KAFKA_PRODUCER_BUFFER_MEMORY);
        // 生产者空间不足时，send()被阻塞的时间，默认60s
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, KafkaCommonConstant.KAFKA_PRODUCER_MAX_BLOCK_MS);
        // 生产者重试次数
        props.put(ProducerConfig.RETRIES_CONFIG, KafkaCommonConstant.KAFKA_PRODUCER_RETRIES);
        // 指定ProducerBatch（消息累加器中BufferPool中的）可复用大小
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,  KafkaCommonConstant.KAFKA_PRODUCER_BATCH_SIZE);
        // 生产者会在ProducerBatch被填满或者等待超过LINGER_MS_CONFIG时发送
        props.put(ProducerConfig.LINGER_MS_CONFIG, KafkaCommonConstant.KAFKA_PRODUCER_LINGER_MS);
        // key 和 value 的序列化
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
        //
        props.put(ProducerConfig.ACKS_CONFIG, KafkaCommonConstant.KAFKA_PRODUCER_ACKS);

        return props;
    }

    /**
    *@Description: 消息生产者工厂类,可创建生产者
    *@Param: 
    *@return: 
    *@Author: yarm.yang
    *@date: 2019/12/13
    */
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<String, String>(createProducerConfigs(producerServcers));
    }

    /**
    *@Description: 收发消息，多个创建多个实例
    *@Param: 
    *@return: 
    *@Author: yarm.yang
    *@date: 2019/12/13
    */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<String, String>(producerFactory());
    }

    /**
    *@Description: 监听容器工厂， 需要对多个地址进行监听消费，请创建多个实例，在@KafkaListener指定该容器工厂
    *@Param: 
    *@return: 
    *@Author: yarm.yang
    *@date: 2019/12/13
    */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> containerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        //设置初始配置
        factory.setConsumerFactory(consumerFactory());
        //设置并发数
        factory.setConcurrency(KafkaCommonConstant.KAFKA_CONSUMER_COUNT);
        //设置为批量消费，每个批次数量在Kafka配置参数中设置ConsumerConfig.MAX_POLL_RECORDS_CONFIG
        factory.setBatchListener(false);
        //设置提交偏移量的方式
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    /**
    *@Description: 消费者创建工厂，可自行创建消费者，需要对多个地址消费，请创建多个实例
    *@Param: 
    *@return: 
    *@Author: yarm.yang
    *@date: 2019/12/13
    */
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(createConsumerConfigs(consumerServers));
    }

    /**
    *@Description: 创建消费者配置,创建多个消费工厂和监听容器工厂时只修改地址，其他写死不动
    *@Param: 
    *@return: 
    *@Author: yarm.yang
    *@date: 2019/12/13
    */
    public Map<String, Object> createConsumerConfigs(String consumerServers) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerServers);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, KafkaCommonConstant.KAFKA_CONSUMER_ENABLEAUTOCOMMIT);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, KafkaCommonConstant.KAFKA_CONSUMER_AUTOCOMMITINTERVAL);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, KafkaCommonConstant.KAFKA_CONSUMER_SESSIONTIMEOUT);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaCommonConstant.KAFKA_DEFUALT_GROUPID);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KafkaCommonConstant.KAFKA_CONSUMER_AUTOOFFSETRESET);
        log.info("初始化kafka配置参数：{}",props);
        return props;
    }
}
