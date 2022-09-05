package com.esen.book.action;

import com.esen.book.service.DemoBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * FeignClient
 *
 * @author liuaj
 * @since 2022/9/2
 */

@RestController
public class ActionTest {
	@Autowired
	private DemoBookService demoBookService;

	@RequestMapping("feign/test1")
	public String test(){
		return "hellowworld";
	}

	@RequestMapping("feign/test2")
	@ResponseBody
	public  String test2(){
		return demoBookService.test();
	}
}
