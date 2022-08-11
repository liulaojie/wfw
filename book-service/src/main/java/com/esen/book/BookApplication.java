package com.esen.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 图书管理服务
 * @author chenlan
 * @since 2022年8月11日
 */
@SpringBootApplication(scanBasePackages = { "com.esen"})
public class BookApplication {
	public static void main(String[] args) {
		SpringApplication.run(BookApplication.class, args);
	}
}
