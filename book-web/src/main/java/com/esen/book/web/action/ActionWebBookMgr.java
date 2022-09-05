package com.esen.book.web.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/web/book")
public class ActionWebBookMgr  {

		@RequestMapping("/index")
		public String test(){
			return "book/index";
		}

	@RequestMapping("bookmgr")
	public String bookMgr(HttpServletRequest req, String bcaption){
		req.setAttribute("bcaption",bcaption);
		return "book/bookmgr";
	}


}
