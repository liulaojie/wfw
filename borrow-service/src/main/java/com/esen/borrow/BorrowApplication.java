package com.esen.borrow;

import com.esen.eservice.start.RunTimeConfig;
import org.springframework.boot.SpringApplication;

import com.esen.eservice.EsenApplication;

/**
 * 借阅管理服务借阅service工程启动类
 * @author liuaj
 * @since 2022年9月2日
 */
public class BorrowApplication {
	public static void main(String[] args) {
		RunTimeConfig.setRuntime(false);
		SpringApplication.run(EsenApplication.class, args);
	}
}
