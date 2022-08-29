package com.esen.book.web.action;

import com.esen.book.service.BookHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/study/book")
public class ActionBook {

		@Autowired
		private BookHistoryService bookHistoryService;

		@RequestMapping("/test1")
		@ResponseBody
		public int getTotalCountByPerson(){
				return bookHistoryService.getTotalCountByPerson();
		}
}
