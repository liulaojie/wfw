package com.esen.abistudy;

import org.springframework.boot.SpringApplication;

import com.esen.esenbase.EsenBaseApplication;
import com.esen.eweb.web.RunTimeConfig;

/**
 * ABI框架学习程序主入口
 * 
 * @author chenlan
 * @since 2020年12月12日
 */
public class AbiStudyApplication {
	public static void main(String[] args) {
		RunTimeConfig.setRuntime(false);
		SpringApplication.run(EsenBaseApplication.class, args);
	}
}
