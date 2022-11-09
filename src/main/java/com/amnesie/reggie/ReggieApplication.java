package com.amnesie.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Description: 项目启动类
 * @author: Amnesie
 * @Date: 2022-10-02
 */

@ServletComponentScan
@Slf4j
@SpringBootApplication
@EnableTransactionManagement
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class, args);
        log.info("项目启动成功....");
        log.info("滴滴滴...");
    }
}
//test add
//rest
