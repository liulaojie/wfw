package com.esen.borrow.web.action;

import com.esen.book.api.entity.BookViewEntity;
import com.esen.borrow.api.entity.BorrowViewEntity;
import com.esen.borrow.web.service.BorrowService;
import com.esen.ejdbc.params.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


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
		@RequestMapping(value = "/test2")
		public String getTotalCountByPerson2(HttpServletRequest req, String person){
				req.setAttribute("number", borrowService.getTotalCountByPerson("张三"));
				return "borrow/test";
		}

		@RequestMapping(value = "/test3")
		public String test3(HttpServletRequest req){
				return "borrow/index";
		}

		@RequestMapping(value = "/list1")
		public String borrowList(HttpServletRequest req){
				PageRequest page = new PageRequest(1,10);
				List<BorrowViewEntity> list = borrowService.borrowList(page);
				req.setAttribute("borrowlist",list);
				return "borrow/list";
		}

		@RequestMapping(value = "/list2")
		public String bookList(HttpServletRequest req,String bcaption){
			PageRequest page = new PageRequest(1,10);
			List<BookViewEntity> list = borrowService.bookList(page,bcaption);
			req.setAttribute("booklist",list);
			return "borrow/booklist";
		}

}
