package com.esen.borrow.web.action;

import com.esen.book.api.entity.BookCategoryEntity;
import com.esen.book.api.entity.BookHistoryEntity;
import com.esen.book.api.entity.BookTypeEntity;
import com.esen.book.api.entity.BookViewEntity;
import com.esen.borrow.api.Repository.BorrowViewRepository;
import com.esen.borrow.api.entity.BorrowViewEntity;
import com.esen.borrow.web.service.BorrowService;
import com.esen.ejdbc.params.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;

/**
 * 书籍管理类，负责借阅的增删改查
 *
 * @author liuaj
 * @since 20220816
 */
@Controller
@RequestMapping("/web/borrow")
public class ActionWebBorrowMgr {
	private static final String REPEAT = "书名重复";

	/**
	 * 对于这个变量的描述
	 */
	@Autowired
	private BorrowService borrowService;

	@RequestMapping("index")
	public String test(){
		return "borrow/index";
	}

	//------------------------------------------------借阅功能----------------------------------------------------------------
	/**
	 * 获取借阅列表
	 */
	@RequestMapping(value = "/borrowList")
	public String borrowList(HttpServletRequest req) {
		PageRequest page = new PageRequest(1, 10);
		List<BorrowViewEntity> list = borrowService.borrowList(page);
		req.setAttribute("borrowlist", list);
		return "borrow/borrowList";
	}
	/**
	 *  添加借书记录
	 *  @param person 借阅人 bid 书籍ID fromdate 借书时间
	 * 	@return 添加结果
	 */
	@RequestMapping(value = "/addBorrow", method = RequestMethod.POST)
	@ResponseBody
	public String addBorrow(String person, String bid, Timestamp fromdate) {
		borrowService.addBorrow(person, bid, fromdate);
		return "借书成功！";
	}
	/**
	 * 更新借书记录，还书
	 */
	@RequestMapping(value = "/saveBorrow",method = RequestMethod.POST)
	public String saveBorrow(BorrowViewEntity borrowViewEntity){
		borrowService.returnBook(borrowViewEntity);
		return "还书成功";
	}

	public String deleteBorrow(BorrowViewEntity borrowViewEntity){
		if (borrowService.deleteBorrow(borrowViewEntity)){
			return "删除成功！";
		}
		return  "删除失败";
	}

	//------------------------------------------------图书功能----------------------------------------------------------------
	/**
	 * 获取小类列表，可得到全部列表或根据大类获取对应的小类列表
	 * @param cid 大类ID
	 * @return
	 */
	@RequestMapping(value = "/typeList")
	@ResponseBody
	public List<BookTypeEntity> typeList(String cid) {
		return borrowService.typeList(cid);

	}
	/**
	 * 获取大类列表
	 * @param
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "/categoryList")
	@ResponseBody
	public List<BookCategoryEntity> categoryList() {
		return borrowService.categoryList();
	}

	/**
		 * 获取图书列表，可得到全部列表或根据大类获取对应的图书列表
		 * @param bcaption bcaption(可为空) 图书大类
		 * @return
		 */
	@RequestMapping(value = "/bookList")
	public String bookList(HttpServletRequest req, String bcaption) {
		PageRequest page = new PageRequest(1, 10);
		List<BookViewEntity> list = borrowService.bookList(page, bcaption);
		req.setAttribute("booklist", list);
		return "borrow/bookList";
	}

	/**
	 * 添加书籍
	 *  @param name 书籍名 scaptionn 小类名称 desc 书本描述
	 * 	@return 添加结果
	 */
	@RequestMapping(value = "/addBook", method = RequestMethod.POST)
	@ResponseBody
	public String addBook(String name, String tid, String desc) {
		if (borrowService.bookIsExists(name) == REPEAT) {
			return "书名重复!";
		}
		borrowService.addBook(name, tid, desc);
		return "添加成功！";
	}
	/**
	 * 编辑书籍
	 */
	@RequestMapping(value = "/saveBook",method = RequestMethod.POST)
	public String saveBook(String name,String desc,String scaption,String bcaption){
		return null;
	}

}
