package com.esen.borrow.web.action;


import com.esen.book.web.action.ActionWebBookMgr;

import com.esen.eutil.util.security.SecurityFunc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;



/**
 * 借阅web的action接口，负责借阅界面的页面跳转
 *包含跳转到主页，跳转到图书管理界面，跳转到借阅管理界面，跳转到分析表界面的功能
 * @author liuaj
 * @since 20220816
 */
@Controller
@RequestMapping("/web/borrow")
public class ActionWebBorrowMgr {


	@Autowired
	ActionWebBookMgr actionWebBookMgr;

	/**
	 * 跳转到主页
	 * @return
	 */
	@RequestMapping("/index")
	public String index() {
		return "borrow/index";
	}
	/**
	 * 跳转到借阅管理界面
	 * @return
	 */
	@RequestMapping("/borrowmgr")
	public String borrowmgr(HttpServletRequest req, String tid) {
		SecurityFunc.checkIdentifier(req,tid);
		req.setAttribute("tid", tid);
		return "borrow/borrowmgr";
	}
	/**
	 * 跳转到分析表界面
	 * @return
	 */
	@RequestMapping("/analyze")
	public String analyze() {
		return "borrow/analyze";
	}


}
