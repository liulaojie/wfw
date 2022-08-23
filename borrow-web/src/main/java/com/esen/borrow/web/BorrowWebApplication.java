package com.esen.borrow.web;

import com.esen.eservice.start.RunTimeConfig;
import org.springframework.boot.SpringApplication;

import com.esen.eservice.EsenApplication;

/**
 * 图书管理web工程启动类
 * @author chenlan
 * @since 2022年8月11日
 */
public class BorrowWebApplication {

	public static void main(String[] args) {
		RunTimeConfig.setRuntime(false);
		SpringApplication.run(EsenApplication.class, args);
	}

}
