package com.esen.book.action;

import com.esen.book.api.entity.BookCategoryEntity;
import com.esen.book.api.entity.BookInfoEntity;
import com.esen.book.api.entity.BookTypeEntity;
import com.esen.book.api.entity.BookViewEntity;
import com.esen.book.service.BookService;
import com.esen.ejdbc.params.PageRequest;
import com.esen.ejdbc.params.PageResult;
import com.esen.eutil.util.StrFunc;
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
@RequestMapping("/service/book")
public class ActionBook {
	
	@Autowired
    private BookService bookService;

	/**
	 * 获取小类列表，可得到全部列表或根据大类获取对应的小类列表
	 * @param cid 大类ID bcaption 大类名
	 * @return
	 */
	@RequestMapping(value = "/typeList" )
	@ResponseBody
	public List<BookTypeEntity> typeList(String cid, String bcaption) {
		return bookService.typeList(cid,bcaption);

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
		return bookService.categoryList();
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
		PageResult<BookViewEntity> result = bookService.bookList(page, bcaption,scaption);
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
		if (bookService.bookIsExists(name)) {
		}else{
			BookInfoEntity bookInfoEntity = new BookInfoEntity();
			bookInfoEntity.setDesc(desc);
			bookInfoEntity.setTid(tid);
			bookInfoEntity.setCaption(name);
			bookService.addBook(bookInfoEntity);
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
		bookService.saveBook(bookInfoEntity);
		return true;
	}
}
