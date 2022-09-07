package com.esen.borrow.action;

import com.esen.book.api.entity.BookHistoryEntity;
import com.esen.borrow.api.entity.BorrowViewEntity;
import com.esen.borrow.log.ELogModuleOperationRegistry;
import com.esen.borrow.service.BorrowService;
import com.esen.ejdbc.params.PageRequest;
import com.esen.ejdbc.params.PageResult;
import com.esen.elog.api.log.Log;
import com.esen.eutil.util.StrFunc;
import com.esen.eutil.util.i18n.I18N;
import com.esen.elog.api.LogService;
import com.esen.eutil.util.security.SecurityFunc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 对借阅管理中的Ajax请求进行处理
 *
 * @author liuaj
 * @since 2022/9/2
 */
@RestController
@RequestMapping("/borrow")
public class ActionBorrow {
	@Autowired
	BorrowService borrowService;
	@Autowired
	LogService logService;

	/**
	 * 获取对应小类的借阅列表或全部借阅列表
	 * @param pageIndex 页面索引
	 * @param tid 小类ID
	 * @return
	 */
	@RequestMapping(value = "/borrowList")
	@ResponseBody
	public Map borrowList(@RequestParam("pageIndex") String pageIndex, @RequestParam("tid") String tid) {
		Log log = logService.create().start();
		try{
			SecurityFunc.checkSQLParam(tid);
			PageRequest page = new PageRequest(StrFunc.str2int(pageIndex,0), 30);
			PageResult<BorrowViewEntity> result= borrowService.borrowList(page,tid);
			Map map = new HashMap();
			map.put("totalCount",result.getTotalCount());
			map.put("list",result.list());
			log.info().desc(I18N.getString("com.esen.borrow.action.actionborrow.borrowlistdesc",
					"查看借阅列表", I18N.getDefaultLocale(), null))
					.detail(I18N.getString("com.esen.borrow.action.actionborrow.borrowlistdetail",
							"查看小类id为{0}的借阅列表", I18N.getDefaultLocale(), new Object[]{tid}))
					.op(ELogModuleOperationRegistry.ELogOperation.BORROW_VIEW).rid("rid")
					.rname("rname").end().add();
			return map;
		}catch (Exception e){
			log.error().desc(I18N.getString("com.esen.borrow.action.actionborrow.borrowlistdesc",
					"查看借阅列表", I18N.getDefaultLocale(), null))
					.detail(I18N.getString("com.esen.borrow.action.actionborrow.borrowlistdetail",
							"查看小类id为{0}的借阅列表", I18N.getDefaultLocale(), new Object[]{tid}))
					.op(ELogModuleOperationRegistry.ELogOperation.BORROW_VIEW).rid("rid")
					.rname("rname").exception(e).end().add();
			throw e;
		}

	}
	/**
	 * 添加一条借书记录
	 * @param person 借阅人
	 * @param bid 书籍ID
	 * @param fromdate 借书时间
	 * @return
	 */
	@RequestMapping(value = "/addBorrow")
	@ResponseBody
	public boolean addBorrow(@RequestParam("person") String person, @RequestParam("bid")String bid,
							 @RequestParam("fromdate")String fromdate) {
		Log log = logService.create().start();
		BookHistoryEntity bookHistoryEntity = new BookHistoryEntity();
		try{
			SecurityFunc.checkXSSParam(person);
			bookHistoryEntity.setPerson(person);
			bookHistoryEntity.setBid(bid);
			Calendar c= StrFunc.str2date(fromdate,"YYYYmmDD");
			Timestamp t = new Timestamp(c.getTimeInMillis());
			bookHistoryEntity.setFromdate(t);
			borrowService.addBorrow(bookHistoryEntity);
			log.info().desc(I18N.getString("com.esen.borrow.action.actionborrow.addborrowdesc",
					"添加一条借阅记录", I18N.getDefaultLocale(), null))
					.detail(I18N.getString("com.esen.borrow.action.actionborrow.addborrowdetail",
							"添加一条借阅记录{0}", I18N.getDefaultLocale(), new Object[]{bookHistoryEntity.toString()}))
					.op(ELogModuleOperationRegistry.ELogOperation.BORROW_ADD).rid("rid")
					.rname("rname").end().add();
			return true;
		}catch (Exception e){
			log.error().desc(I18N.getString("com.esen.borrow.action.actionborrow.addborrowdesc",
					"添加一条借阅记录", I18N.getDefaultLocale(), null))
					.detail(I18N.getString("com.esen.borrow.action.actionborrow.addborrowdetail",
							"添加一条借阅记录{0}", I18N.getDefaultLocale(), new Object[]{bookHistoryEntity.toString()}))
					.op(ELogModuleOperationRegistry.ELogOperation.BORROW_ADD).rid("rid")
					.rname("rname").exception(e).end().add();
			throw e;
		}
	}

	/**
	 * 还书，保存借阅记录的还书时间
	 * @param id
	 * @param todate
	 * @return
	 */
	@RequestMapping(value = "/saveBorrow"  )
	@ResponseBody
	public boolean saveBorrow(@RequestParam("id") String id, @RequestParam("todate") String todate){
		Log log = logService.create().start();
		try{
			SecurityFunc.checkSQLParam(id);
			BookHistoryEntity bookHistoryEntity = new BookHistoryEntity();
			bookHistoryEntity.setId(id);
			Calendar c= StrFunc.str2date(todate,"YYYYmmDD");
			Timestamp t = new Timestamp(c.getTimeInMillis());
			bookHistoryEntity.setTodate(t);
			borrowService.returnBook(bookHistoryEntity);
			log.info().desc(I18N.getString("com.esen.borrow.action.actionborrow.returnborrowdesc",
					"还书,更新借书记录", I18N.getDefaultLocale(), null))
					.detail(I18N.getString("com.esen.borrow.action.actionborrow.returnborrowdetail",
							"于{0}归还ID为{1}的书籍", I18N.getDefaultLocale(), new Object[]{todate,id}))
					.op(ELogModuleOperationRegistry.ELogOperation.BORROW_REFRESH)
					.rid("rid").rname("rname").end().add();
			return true;
		}catch (Exception e){
			log.error().desc(I18N.getString("com.esen.borrow.action.actionborrow.returnborrowdesc",
					"还书,更新借书记录", I18N.getDefaultLocale(), null))
					.detail(I18N.getString("com.esen.borrow.action.actionborrow.returnborrowdetail",
							"于{0}归还ID为{1}的书籍", I18N.getDefaultLocale(), new Object[]{todate,id}))
					.op(ELogModuleOperationRegistry.ELogOperation.BORROW_REFRESH)
					.rid("rid").rname("rname").exception(e).end().add();
			throw e;
		}

	}

	/**
	 * 删除ID对应的借阅记录
	 * @param id 借阅记录ID
	 * @return
	 */
	@RequestMapping(value = "deleteBorrow")
	@ResponseBody
	public boolean deleteBorrow(@RequestParam("id")String id) {
		Log log = logService.create().start();
		try{
			SecurityFunc.checkSQLParam(id);
			boolean flag = borrowService.deleteBorrow(id);
			log.info().desc(I18N.getString("com.esen.borrow.action.actionborrow.deleteborrowdesc",
					"删除借书记录", I18N.getDefaultLocale(), null))
					.detail(I18N.getString("com.esen.borrow.action.actionborrow.deleteborrowdetail",
							"删除ID为{0}的书籍", I18N.getDefaultLocale(), new Object[]{id}))
					.op(ELogModuleOperationRegistry.ELogOperation.BORROW_DELETE)
					.rid("rid").rname("ranme").end().add();
			return flag;
		}catch (Exception e){
			log.info().desc(I18N.getString("com.esen.borrow.action.actionborrow.deleteborrowdesc",
					"删除借书记录", I18N.getDefaultLocale(), null))
					.detail(I18N.getString("com.esen.borrow.action.actionborrow.deleteborrowdetail",
							"删除ID为{0}的书籍", I18N.getDefaultLocale(), new Object[]{id}))
					.op(ELogModuleOperationRegistry.ELogOperation.BORROW_DELETE)
					.rid("rid").rname("ranme").exception(e).end().add();
			throw e;
		}
	}
	}
