package com.esen.borrow.web.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@FeignClient(value = "borrow",contextId = "borrow")
public interface BorrowClient {

	@RequestMapping(value = "borrow/test")
	public String test();

}
