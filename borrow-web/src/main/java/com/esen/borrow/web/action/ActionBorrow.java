package com.esen.borrow.web.action;

import com.esen.borrow.web.service.BorrowService;
import com.esen.ejdbc.params.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


/**
 * 书籍管理类，负责借阅的增删改查
 *
 * @author liuaj
 * @since 20220816
 */
@Controller
@RequestMapping("/study/borrow")
public class ActionBorrow {
		/**
		 * 对于这个变量的描述
		 */
		@Autowired
		private BorrowService borrowService;


		/**
		 * 对于这个方法的描述
		 * @param
		 * @return
		 * @throws
		 */
		@RequestMapping(value = "/test1",method = RequestMethod.GET)
		@ResponseBody
		public int  getTotalCountByPerson1(String person){
				System.out.println(person);
				return borrowService.getHistoryTotalCountByPerson(person);
		}
		/**
		 * 对于这个方法的描述
		 * @param
		 * @return
		 * @throws
		 */
		@RequestMapping(value = "/test2")
		public String getTotalCountByPerson2(HttpServletRequest req, String person){
				req.setAttribute("number", borrowService.getTotalCountByPerson("张三"));
				return "borrow/test";
		}

		@RequestMapping(value = "/test3")
		public String test3(HttpServletRequest req){
				return "borrow/index";
		}

		@RequestMapping(value = "/list")
		public String listBorrowsByperson(HttpServletRequest req){
				PageRequest page = new PageRequest(1,10);
				String person = "张三";
				req.setAttribute("borrowlist",borrowService.listBorrowsByperson(person,page));
				return "borrow/list";
		}

}
