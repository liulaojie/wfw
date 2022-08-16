package com.esen.borrow.web.action;

import com.esen.borrow.web.service.BorrowService;
import com.esen.ejdbc.params.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


/**
 * 书籍管理类，负责借阅的增删改查
 *
 * @author 刘傲杰
 * @since 20220816
 */
@Controller
@RequestMapping("/study/borrow")
public class ActionBorrow {

		@Autowired
		private BorrowService borrowService;

		@RequestMapping("/list")
		public String listBorrowPage(String person, int page ,int size){
				PageRequest request = new PageRequest(page,size);
				return "study/borrow/list";
		}

		@RequestMapping(value = "/test1",method = RequestMethod.GET)
		@ResponseBody
		public int  getTotalCountByPerson1(String person){
				System.out.println(person);
				return borrowService.getTotalCountByPerson(person);
		}

		@RequestMapping(value = "/test2")
		public String getTotalCountByPerson2(HttpServletRequest req, String person){
				req.setAttribute("number",borrowService.getTotalCountByPerson(person));
				return "borrow/test";
		}

}
