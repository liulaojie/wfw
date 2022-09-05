package com.esen.book.web;

import com.esen.eservice.EsenApplication;
import com.esen.eservice.start.RunTimeConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 图书管理web工程启动类
 * @author liuaj
 * @since 2022年8月11日
 */

@EnableFeignClients
public class BookWebApplication {

	public static void main(String[] args) {
		RunTimeConfig.setRuntime(false);
		SpringApplication.run(EsenApplication.class, args);
	}

}
