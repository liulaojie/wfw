package com.esen.book.web.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/web/book")
public class ActionWebBookMgr  {
	/**
	 * 跳转到主页
	 * @return
	 */
	@RequestMapping("/index")
	public String index() {
		return "book/web/index";
	}
	/**
	 * 跳转到图书管理界面
	 * @return
	 */
	@RequestMapping("bookmgr")
	public String bookMgr(HttpServletRequest req, String bcaption) {
		req.setAttribute("bcaption", bcaption);
		return "book/web/bookmgr";
	}

}
