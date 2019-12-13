package com.yarm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @program: yarm-mq
 * @description: api启动类
 * @author: yarm.yang
 * @create: 2019-12-12 16:37
 */
// 不连接数据库
@SpringBootApplication(exclude= DataSourceAutoConfiguration.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
