package com.esen.book.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 图书管理web工程启动类
 * @author chenlan
 * @since 2022年8月11日
 */
@SpringBootApplication(scanBasePackages = { "com.esen" })
public class BookWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookWebApplication.class, args);
	}

}
