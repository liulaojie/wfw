package com.esen.eplatform;

import org.springframework.boot.SpringApplication;

import com.esen.eservice.EsenApplication;

/**
 * 平台启动类
 * 就是eplatform工程
 * @author chenlan
 * @since 2022年8月11日
 */
public class EplatformApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(EsenApplication.class, args);
	}
}
