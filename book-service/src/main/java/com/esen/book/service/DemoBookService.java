package com.esen.book.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 测试对外提供接口
 *
 * @author liuaj
 * @since 20220902
 */
@Component
@FeignClient(value = "127.0.0.1:8016",contextId = "test")
public interface DemoBookService {

	@RequestMapping(value = "/web/borrow/test")
	@ResponseBody
	public String test();
}
