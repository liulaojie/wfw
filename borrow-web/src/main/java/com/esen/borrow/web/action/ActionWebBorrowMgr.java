package com.esen.borrow.web.action;

import com.esen.book.api.entity.*;
import com.esen.borrow.api.entity.AnalyzeEntity;
import com.esen.borrow.api.entity.BorrowViewEntity;
import com.esen.borrow.service.BorrowService;
import com.esen.borrow.web.log.ELogModuleOperationRegistry;
import com.esen.ejdbc.params.PageRequest;
import com.esen.ejdbc.params.PageResult;
import com.esen.elog.api.LogService;
import com.esen.elog.api.log.Log;

import com.esen.eutil.util.StrFunc;
import com.esen.eutil.util.i18n.I18N;
import org.apache.poi.ss.formula.functions.DateFunc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.*;


/**
 * 借阅web，负责借阅界面的页面跳转
 *
 * @author liuaj
 * @since 20220816
 */
@Controller
@RequestMapping("/web/borrow")
public class ActionWebBorrowMgr {

	@Autowired
	private LogService logService;
	/**
	 * 对于这个变量的描述
	 */
	@Autowired
	private BorrowService borrowService;

	@RequestMapping("test")
	@ResponseBody
	public String test(){
		return "hello world";
	}

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
	@RequestMapping("analyze")
	public String analyze(){
		return "borrow/analyze";
	}

	//------------------------------------------------借阅功能----------------------------------------------------------------
	/**
	 * 获取借阅列表
	 */
	@RequestMapping(value = "/borrowList")
	@ResponseBody
	public Map borrowList(HttpServletRequest req,String pageIndex,String scaption) {
		Log log = logService.create().start();
		try{
			PageRequest page = new PageRequest(StrFunc.str2int(pageIndex,0), 30);
			PageResult<BorrowViewEntity> result= borrowService.borrowList(page,scaption);
			Map map = new HashMap();
			map.put("totalCount",result.getTotalCount());
			map.put("list",result.list());
			log.info().desc(I18N.getString("com.esen.borrow.web.action.ActionWebBorrowMgr.borrowlist",
					"查看借阅列表", I18N.getDefaultLocale(), null))
					.op(ELogModuleOperationRegistry.ELogOperation.Borrow_VIEW).rid("rid")
					.rname("rname").end().add();
			return map;
		}catch (Exception e){
			log.error().desc(I18N.getString("com.esen.borrow.web.action.ActionWebBorrowMgr.borrowlist",
					"查看借阅列表", I18N.getDefaultLocale(), null))
					.op(ELogModuleOperationRegistry.ELogOperation.Borrow_VIEW).rid("rid")
					.rname("rname").exception(e).end().add();
			throw e;
		}

	}
	/**
	 *  添加借书记录
	 *  @param person 借阅人 bid 书籍ID fromdate 借书时间
	 * 	@return 添加结果
	 */
	@RequestMapping(value = "/addBorrow")
	@ResponseBody
	public boolean addBorrow(String person, String bid, String fromdate) {
		Log log = logService.create().start();
		try{
			BookHistoryEntity bookHistoryEntity = new BookHistoryEntity();
			bookHistoryEntity.setPerson(person);
			bookHistoryEntity.setBid(bid);
			Calendar c= StrFunc.str2date(fromdate,"YYYYmmDD");
			Timestamp t = new Timestamp(c.getTimeInMillis());
			bookHistoryEntity.setFromdate(t);
			borrowService.addBorrow(bookHistoryEntity);
			log.info().desc(I18N.getString("com.esen.borrow.web.action.ActionWebBorrowMgr.addborrow",
					"添加一条借阅记录", I18N.getDefaultLocale(), null))
					.op(ELogModuleOperationRegistry.ELogOperation.Borrow_ADD).rid("rid")
					.rname("rname").end().add();
			return true;
		}catch (Exception e){
			log.error().desc(I18N.getString("com.esen.borrow.web.action.ActionWebBorrowMgr.addborrow",
					"添加一条借阅记录", I18N.getDefaultLocale(), null))
					.op(ELogModuleOperationRegistry.ELogOperation.Borrow_ADD).rid("rid")
					.rname("rname").exception(e).end().add();
			throw e;
		}
	}
	/**
	 * 更新借书记录，还书
	 */
	@RequestMapping(value = "/saveBorrow"  )
	@ResponseBody
	public boolean saveBorrow(String id, String todate){
		Log log = logService.create().start();
		try{
			BookHistoryEntity bookHistoryEntity = new BookHistoryEntity();
			bookHistoryEntity.setId(id);
			Calendar c= StrFunc.str2date(todate,"YYYYmmDD");
			Timestamp t = new Timestamp(c.getTimeInMillis());
			bookHistoryEntity.setTodate(t);
			borrowService.returnBook(bookHistoryEntity);
			log.info().desc(I18N.getString("com.esen.borrow.web.action.ActionWebBorrowMgr.returnborrow",
					"还书,更新借书记录", I18N.getDefaultLocale(), null))
					.op(ELogModuleOperationRegistry.ELogOperation.Borrow_REFRESH)
					.rid("rid").rname("rname").end().add();
			return true;
		}catch (Exception e){
			log.error().desc(I18N.getString("com.esen.borrow.web.action.ActionWebBorrowMgr.returnborrow",
					"还书,更新借书记录", I18N.getDefaultLocale(), null))
					.op(ELogModuleOperationRegistry.ELogOperation.Borrow_REFRESH)
					.rid("rid").rname("rname").exception(e).end().add();
			throw e;
		}

	}
	@RequestMapping(value = "deleteBorrow")
	@ResponseBody
	public boolean deleteBorrow(String id) {
		Log log = logService.create().start();
		try{
			boolean flag = borrowService.deleteBorrow(id);
			log.info().desc(I18N.getString("com.esen.borrow.web.action.ActionWebBorrowMgr.deleteborrow",
					"删除借书记录", I18N.getDefaultLocale(), null))
					.op(ELogModuleOperationRegistry.ELogOperation.Borrow_DELETE)
					.rid("rid").rname("ranme").end().add();
			return flag;
		}catch (Exception e){
			log.info().desc(I18N.getString("com.esen.borrow.web.action.ActionWebBorrowMgr.deleteborrow",
					"删除借书记录", I18N.getDefaultLocale(), null))
					.op(ELogModuleOperationRegistry.ELogOperation.Borrow_DELETE)
					.rid("rid").rname("ranme").exception(e).end().add();
			throw e;
		}


	}

	//------------------------------------------------图书功能----------------------------------------------------------------
	/**
	 * 获取小类列表，可得到全部列表或根据大类获取对应的小类列表
	 * @param cid 大类ID bcaption 大类名
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
	public Map bookList
	(HttpServletRequest req, String bcaption,String scaption,String pageIndex) {
		PageRequest page = new PageRequest(StrFunc.str2int(pageIndex,0), 30);
		PageResult<BookViewEntity> result = borrowService.bookList(page, bcaption,scaption);
		Map map = new HashMap();
		map.put("totalCount",result.getTotalCount());
		map.put("list",result.list());
		return map;
	}
	/**
	 *
	 * @param name 书籍名
	 * @param tid 小类ID
	 * @param desc 描述
	 * @return
	 * @throws RuntimeException
	 */
	@RequestMapping(value = "/addBook")
	@ResponseBody
	public boolean addBook(HttpServletRequest req,String name, String tid, String desc) throws RuntimeException{
		if (borrowService.bookIsExists(name)) {
			throw new RuntimeException(I18N.getString("com.esen.borrow.web.action.ActionWebBorrowMgr.repeat", "书名重复"));
		}else{
			BookInfoEntity bookInfoEntity = new BookInfoEntity();
			bookInfoEntity.setDesc(desc);
			bookInfoEntity.setTid(tid);
			bookInfoEntity.setCaption(name);
			borrowService.addBook(bookInfoEntity);
		}
		return true;
	}

	/**
	 * 编辑书籍
	 * @param id 书本ID
	 * @param name 书本名
	 * @param desc 描述
	 * @param tid 小类ID
	 * @return
	 */
	@RequestMapping(value = "/saveBook")
	@ResponseBody
	public boolean saveBook(String id,String name,String desc,String tid){
		BookInfoEntity bookInfoEntity = new BookInfoEntity();
		bookInfoEntity.setTid(id);
		bookInfoEntity.setCaption(name);
		bookInfoEntity.setTid(tid);
		bookInfoEntity.setDesc(desc);
		borrowService.saveBook(bookInfoEntity);
		return true;
	}

}
