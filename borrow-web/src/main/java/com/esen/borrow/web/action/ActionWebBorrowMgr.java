package com.esen.borrow.web.action;

import com.esen.book.api.entity.BookCategoryEntity;
import com.esen.book.api.entity.BookTypeEntity;
import com.esen.book.api.entity.BookViewEntity;
import com.esen.borrow.api.entity.BorrowViewEntity;
import com.esen.borrow.service.BorrowService;
import com.esen.ejdbc.params.PageRequest;
import com.esen.eutil.util.JsonUtils;
import com.esen.eutil.util.StrFunc;
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
	public String index(){
		return "borrow/index";
	}

	@RequestMapping("bookmgr")
	public String bookMgr(HttpServletRequest req,String bcaption){
		req.setAttribute("bcaption",bcaption);
		return "borrow/bookmgr";
	}
	@RequestMapping("borrowmgr")
	public String borrowmgr(HttpServletRequest req,String scaption){
		req.setAttribute("scaption",scaption);
		return "borrow/borrowmgr";
	}


	//------------------------------------------------借阅功能----------------------------------------------------------------
	/**
	 * 获取借阅列表
	 */
	@RequestMapping(value = "/borrowList")
	@ResponseBody
	public List<BorrowViewEntity> borrowList(HttpServletRequest req,String pageIndex,String scaption) {
		PageRequest page = new PageRequest(StrFunc.str2int(pageIndex,0), 30);
		return borrowService.borrowList(page,scaption);
	}
	/**
	 * 获取借阅列表
	 */
	@RequestMapping(value = "/borrowSize")
	@ResponseBody
	public int borrowSize( String scaption) {
		return borrowService.borrowSize(scaption);
	}
	/**
	 *  添加借书记录
	 *  @param person 借阅人 bid 书籍ID fromdate 借书时间
	 * 	@return 添加结果
	 */
	@RequestMapping(value = "/addBorrow")
	@ResponseBody
	public boolean addBorrow(String person, String bid, String fromdate) {
		borrowService.addBorrow(person, bid, fromdate);
		return true;
	}
	/**
	 * 更新借书记录，还书
	 */
	@RequestMapping(value = "/saveBorrow",method = RequestMethod.POST)
	@ResponseBody
	public boolean saveBorrow(String id, String todate){
		borrowService.returnBook(id,todate);
		return true;
	}
	@RequestMapping(value = "deleteBorrow")
	@ResponseBody
	public boolean deleteBorrow(String id) {
		return borrowService.deleteBorrow(id);
	}

	//------------------------------------------------图书功能----------------------------------------------------------------
	/**
	 * 获取小类列表，可得到全部列表或根据大类获取对应的小类列表
	 * @param cid 大类ID
	 * @return
	 */
	@RequestMapping(value = "/typeList" )
	@ResponseBody
	public List<BookTypeEntity> typeList(String cid,String bcaption) {
		return borrowService.typeList(cid,bcaption);

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
	 * @param bcaption bcaption(可为空) 图书大类 scaption(可为空)图书小类 pageIndex 页面索引
	 * @return
	 */
	@RequestMapping(value = "/bookList")
	@ResponseBody
	public List<BookViewEntity> bookList
	(HttpServletRequest req, String bcaption,String scaption,String pageIndex) {
		PageRequest page = new PageRequest(StrFunc.str2int(pageIndex,0), 30);
		List<BookViewEntity> list = borrowService.bookList(page, bcaption,scaption);
		return list;
	}
	/**
	 * 获取借阅列表
	 */
	@RequestMapping(value = "/bookSize")
	@ResponseBody
	public int bookSize(String bcaption) {
		return borrowService.bookSize(bcaption);
	}

	/**
	 * 添加书籍
	 *  @param name 书籍名 scaptionn 小类名称 desc 书本描述
	 * 	@return 添加结果
	 */
	@RequestMapping(value = "/addBook")
	@ResponseBody
	public boolean addBook(HttpServletRequest req,String name, String tid, String desc) {

		if (borrowService.bookIsExists(name) == REPEAT) {
			return false;
		}else{
			borrowService.addBook(name, tid, desc);
			return true;
		}
	}
	/**
	 * 编辑书籍
	 */
	@RequestMapping(value = "/saveBook")
	@ResponseBody
	public boolean saveBook(String id,String name,String desc,String cid,String tid){
		borrowService.saveBook(id,name,tid,desc);

		return true;

	}

}
