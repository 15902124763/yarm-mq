package com.yarm.constant;

/**
 * @program: yarm-mq
 * @description: Kafka自定义常亮
 * @author: yarm.yang
 * @create: 2019-12-12 18:08
 */
public class KafkaCommonConstant {

    public final static String KAFKA_DEFUALT_TOPIC = "kafka_hds_demo_key";

    /**
     *  默认groupId ,可指定消费
     */
    public final static String KAFKA_DEFUALT_GROUPID = "demo_q";

    /**
     * 初始偏移（latest）
     */
    public final static String KAFKA_CONSUMER_AUTOOFFSETRESET = "latest";

    /**
     * 手动提交偏移量
     */
    public final static boolean KAFKA_CONSUMER_ENABLEAUTOCOMMIT = false;
    /**
     * 自动提交频率 100ms
     */
    public final static int KAFKA_CONSUMER_AUTOCOMMITINTERVAL = 100;
    /**
     *  超时时间 15s
     */
    public final static int KAFKA_CONSUMER_SESSIONTIMEOUT = 15000;

    /**
     * 创建消费者数，即并发数
     */
    public final static int KAFKA_CONSUMER_COUNT  = 1;

    /**
     * 默认的topic
     */
    public static final long KAFKA_PRODUCER_BUFFER_MEMORY = 40960;

    /**
     * send()被阻塞的时间
     */
    public static final long KAFKA_PRODUCER_MAX_BLOCK_MS = 6000;

    /**
     *  重试次数
     */
    public static final int KAFKA_PRODUCER_RETRIES = 0;

    /**
     * ProducerBatch 可复用大小
     */
    public static final int KAFKA_PRODUCER_BATCH_SIZE = 4096;

    /**
     * 生产者会在等待超过 {@value}时发送
     */
    public static final long KAFKA_PRODUCER_LINGER_MS = 1;

    /**
     *
     */
    public static final String KAFKA_PRODUCER_ACKS = "1";
}
