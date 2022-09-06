package com.esen.book.action;

import com.esen.book.api.entity.BookCategoryEntity;
import com.esen.book.api.entity.BookInfoEntity;
import com.esen.book.api.entity.BookTypeEntity;
import com.esen.book.api.entity.BookViewEntity;
import com.esen.book.log.ELogModuleOperationRegistry;
import com.esen.book.service.BookService;
import com.esen.ejdbc.params.PageRequest;
import com.esen.ejdbc.params.PageResult;
import com.esen.elog.api.log.Log;
import com.esen.eutil.util.StrFunc;
import com.esen.eutil.util.i18n.I18N;
import com.esen.elog.api.LogService;
import com.esen.eutil.util.security.SecurityFunc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对于这个类的描述.
 *
 * @author liuaj
 * @since 2022/9/5
 */
@RestController
@RequestMapping("/book")
public class ActionBook {
	
	@Autowired
    private BookService bookService;
	@Autowired
	private LogService logService;

	/**
	 * 获取小类列表，可得到全部列表或根据大类获取对应的小类列表
	 * @param cid 大类ID bcaption 大类名
	 * @return
	 */
	@RequestMapping(value = "/typeList" )
	@ResponseBody
	public List<BookTypeEntity> typeList(String cid,String bcaption) {
		Log log = logService.create().start();
		try {
			SecurityFunc.checkSQLParam(bcaption);
			List list = bookService.typeList(cid,bcaption);
			log.info().desc(I18N.getString("com.esen.book.action.actionbook.typelistdesc",
					"查看小类列表", I18N.getDefaultLocale(), null))
					.op(ELogModuleOperationRegistry.ELogOperation.TYPE_VIEW).rid("rid")
					.detail(I18N.getString("com.esen.book.action.actionbook.typelistdetail",
							"查看大类ID为{0}或大类类型为{1}的借阅列表", I18N.getDefaultLocale(), new Object[]{cid,bcaption}))
					.rname("rname").end().add();
			return list;
		}catch (Exception e){
			log.info().desc(I18N.getString("com.esen.book.action.actionbook.typelistdesc",
					"查看小类列表", I18N.getDefaultLocale(), null))
					.detail(I18N.getString("com.esen.book.action.actionbook.typelistdetail",
							"查看大类ID为{0}或大类类型为{1}的借阅列表", I18N.getDefaultLocale(), new Object[]{cid,bcaption}))
					.op(ELogModuleOperationRegistry.ELogOperation.TYPE_VIEW)
					.rid("rid").rname("ranme").exception(e).end().add();
			throw e;
		}
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
		Log log = logService.create().start();
		try{
			List list = bookService.categoryList();
			log.info().desc(I18N.getString("com.esen.book.action.actionbook.categorylist",
					"查看大类列表", I18N.getDefaultLocale(), null))
					.detail(I18N.getString("com.esen.book.action.actionbook.categorylist",
							"查看大类列表", I18N.getDefaultLocale(),null))
					.op(ELogModuleOperationRegistry.ELogOperation.CATEGORY_VIEW).rid("rid")
					.rname("rname").end().add();
			return list;
		}catch (Exception e){
			log.info().desc(I18N.getString("com.esen.book.action.actionbook.categorylist",
					"查看大类列表", I18N.getDefaultLocale(), null))
					.detail(I18N.getString("com.esen.book.action.actionbook.categorylist",
							"查看大类列表", I18N.getDefaultLocale(),null))
					.op(ELogModuleOperationRegistry.ELogOperation.CATEGORY_VIEW)
					.rid("rid").rname("ranme").exception(e).end().add();
			throw e;
		}
	}

	/**
	 * 获取图书列表，可得到全部列表或根据大类获取对应的图书列表
	 * @param bcaption bcaption(可为空) 图书大类 scaption(可为空)图书小类 pageIndex 页面索引
	 * @return
	 */
	@RequestMapping(value = "/bookList")
	@ResponseBody
	public Map bookList
	(String bcaption,String scaption,String pageIndex) {
		Log log = logService.create().start();
		try{
			SecurityFunc.checkSQLParam(bcaption);
			SecurityFunc.checkSQLParam(scaption);
			PageRequest page = new PageRequest(StrFunc.str2int(pageIndex,0), 30);
			PageResult<BookViewEntity> result = bookService.bookList(page, bcaption,scaption);
			Map map = new HashMap();
			map.put("totalCount",result.getTotalCount());
			map.put("list",result.list());
			log.info().desc(I18N.getString("com.esen.book.action.actionbook.booklistdesc",
					"查看图书列表", I18N.getDefaultLocale(), null))
					.detail(I18N.getString("com.esen.book.action.actionbook.booklistdetail",
							"查看大类类型为{0}或小类类型为{1}的书籍列表", I18N.getDefaultLocale(), new Object[]{bcaption,scaption}))
					.op(ELogModuleOperationRegistry.ELogOperation.BOOK_VIEW).rid("rid")
					.rname("rname").end().add();
			return map;
		}catch (Exception e){
			log.info().desc(I18N.getString("com.esen.book.action.actionbook.booklist",
					"查看图书列表", I18N.getDefaultLocale(), null))
					.detail(I18N.getString("com.esen.book.action.actionbook.booklistdetail",
							"查看大类类型为{0}或小类类型为{1}的书籍列表", I18N.getDefaultLocale(), new Object[]{bcaption,scaption}))
					.op(ELogModuleOperationRegistry.ELogOperation.BOOK_VIEW)
					.rid("rid").rname("ranme").exception(e).end().add();
			throw e;
		}
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
		Log log = logService.create().start();
		BookInfoEntity bookInfoEntity = new BookInfoEntity();
		try{
			SecurityFunc.checkXSSParam(name);
			SecurityFunc.checkXSSParam(desc);
			SecurityFunc.checkSQLParam(name);
			if (bookService.bookIsExists(name)) {
				throw new RuntimeException(I18N.getString("com.esen.book.action.actionbook.repeat", "书名重复"));
			}else{
				bookInfoEntity.setDesc(desc);
				bookInfoEntity.setTid(tid);
				bookInfoEntity.setCaption(name);
				bookService.addBook(bookInfoEntity);
			}
			log.info().desc(I18N.getString("com.esen.book.action.actionbook.addbookdesc",
					"添加书籍", I18N.getDefaultLocale(), null))
					.detail(I18N.getString("com.esen.book.action.actionbook.addbookdetail",
							"添加一本书籍{0}", I18N.getDefaultLocale(), new Object[]{bookInfoEntity.toString()}))
					.op(ELogModuleOperationRegistry.ELogOperation.BOOK_ADD).rid("rid")
					.rname("rname").end().add();
			return true;
		}catch (Exception e){
			log.info().desc(I18N.getString("com.esen.book.action.actionbook.addbook",
					"添加书籍", I18N.getDefaultLocale(), null))
					.detail(I18N.getString("com.esen.book.action.actionbook.addbookdetail",
							"添加一本书籍{0}", I18N.getDefaultLocale(), new Object[]{bookInfoEntity.toString()}))
					.op(ELogModuleOperationRegistry.ELogOperation.BOOK_ADD)
					.rid("rid").rname("ranme").exception(e).end().add();
			throw e;
		}

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
		Log log = logService.create().start();
		BookInfoEntity bookInfoEntity = new BookInfoEntity();
		try{
			SecurityFunc.checkXSSParam(name);
			SecurityFunc.checkXSSParam(desc);
			bookInfoEntity.setTid(id);
			bookInfoEntity.setCaption(name);
			bookInfoEntity.setTid(tid);
			bookInfoEntity.setDesc(desc);
			bookService.saveBook(bookInfoEntity);
			log.info().desc(I18N.getString("com.esen.book.action.actionbook.savebookdesc",
					"编辑图书", I18N.getDefaultLocale(), null))
					.detail(I18N.getString("com.esen.book.action.actionbook.savebookdetail",
							"修改书籍信息为{0}，tid,cid为0时，代表未修改大小类", I18N.getDefaultLocale(),
							new Object[]{bookInfoEntity.toString()}))
					.op(ELogModuleOperationRegistry.ELogOperation.BOOK_EDIT).rid("rid")
					.rname("rname").end().add();
			return true;
		}catch (Exception e){
			log.info().desc(I18N.getString("com.esen.book.action.actionbook.savebook",
					"编辑图书", I18N.getDefaultLocale(), null))
					.detail(I18N.getString("com.esen.book.action.actionbook.savebookdetail",
							"修改书籍信息为{0}，tid,cid为0时，代表未修改大小类", I18N.getDefaultLocale(),
							new Object[]{bookInfoEntity.toString()}))
					.op(ELogModuleOperationRegistry.ELogOperation.BOOK_EDIT)
					.rid("rid").rname("ranme").exception(e).end().add();
			throw e;
		}
	}
}
