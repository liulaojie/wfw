package com.esen.book;

import com.esen.eservice.EsenApplication;
import com.esen.eservice.start.RunTimeConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 图书管理服务
 * @author chenlan
 * @since 2022年8月11日
 */
@EnableFeignClients
public class BookApplication {
	public static void main(String[] args) {
		RunTimeConfig.setRuntime(false);
		SpringApplication.run(EsenApplication.class, args);
	}
}
