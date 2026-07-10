package com.zhy.yxkj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Administrator
 */
@MapperScan("com.zhy.yxkj.mapper")
@SpringBootApplication()
public class YxkjApplication {
    public static void main(String[] args) {
        SpringApplication.run(YxkjApplication.class, args);
    }

}
