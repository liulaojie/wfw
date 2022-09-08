package com.esen.book.web.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 书籍service的action接口，负责借阅界面的页面跳转
 *包含跳转到主页和跳转到图书管理界面
 * @author liuaj
 * @since 20220905
 */
@Controller
@RequestMapping("/web/book")
public class ActionWebBookMgr  {
	/**
	 * 跳转到主页
	 * @return
	 */
	@RequestMapping("/index")
	public String index() {
		return "book/index";
	}
	/**
	 * 跳转到图书管理界面
	 * @return
	 */
	@RequestMapping("bookmgr")
	public String bookMgr(HttpServletRequest req, String cid) {
		req.setAttribute("cid", cid);
		return "book/bookmgr";
	}

}
