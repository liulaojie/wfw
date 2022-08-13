package com.esen.borrow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.esen.eservice.EsenApplication;

/**
 * 图书管理服务
 * @author chenlan
 * @since 2022年8月11日
 */
@SpringBootApplication(scanBasePackages = { "com.esen"})
public class BorrowApplication {
	public static void main(String[] args) {
		SpringApplication.run(EsenApplication.class, args);
	}
}
