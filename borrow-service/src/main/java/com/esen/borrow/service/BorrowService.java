package com.esen.borrow.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.esen.eutil.util.StrFunc;
import org.springframework.beans.factory.annotation.Autowired;

import com.esen.book.api.entity.*;
import com.esen.book.api.repository.*;
import com.esen.borrow.api.Repository.BorrowViewRepository;
import com.esen.borrow.api.entity.BorrowViewEntity;
import com.esen.ejdbc.jdbc.ConnectFactoryManager;
import com.esen.ejdbc.params.PageRequest;
import com.esen.ejdbc.params.PageResult;
import com.esen.eorm.annotation.ApplicationService;
import com.esen.eorm.service.AbstractService;
import com.esen.eutil.util.UNID;
import com.esen.eutil.util.exp.Expression;

/**
 * 图书管理的SERVICE层
 *
 * @author liuaj
 * @since 20220816
 */
@ApplicationService
public class BorrowService extends AbstractService<BorrowViewEntity> {

	@Autowired
	protected ConnectFactoryManager connectFactoryManager;

	@Autowired
	protected BorrowViewRepository borrowViewRepository;

	@Autowired
	protected BookViewRepository bookViewRepository;

	@Autowired
	protected BookInfoRepository bookInfoRepository;

	@Autowired
	protected BookTypeRepository bookTypeRepository;

	@Autowired
	protected BookCategoryRepository bookCategoryRepository;

	@Autowired
	protected BookHistoryRepository bookHistoryRepository;

	//------------------------------------------------借阅功能----------------------------------------------------------------
	/**
	 * person的所有借阅记录数
	 * @param person 借阅人的姓名
	 * @return 借阅的总数
	 */
	public int getTotalCountByPerson(String person) {
		return borrowViewRepository.getTotalCountByPerson(person);
	}

	/**
	 * 获取借阅列表
	 * @param
	 * @return
	 * @throws
	 */
	public List<BorrowViewEntity> borrowList(PageRequest page) {
		PageResult<BorrowViewEntity> result = borrowViewRepository.findAll(page);
		return result.list();
	}
	/**
	 * 获取借阅列表大小
	 * @param
	 * @return
	 * @throws
	 */
	public int borrowSize() {
		return bookHistoryRepository.getTotalCount();
	}

	/**
	 * 添加借书记录
	 * @param person 借阅人 bid 书籍ID fromdate 借书时间
	 * @return
	 */
	public void addBorrow(String person, String bid, String fromdate) {
		String id = UNID.randomID();
		BookHistoryEntity bookHistoryEntity = new BookHistoryEntity();
		bookHistoryEntity.setId(id);
		bookHistoryEntity.setBid(bid);
		bookHistoryEntity.setPerson(person);
		bookHistoryEntity.setFromdate(str2time(fromdate));
		bookHistoryRepository.add(bookHistoryEntity);
	}

	/**
	 * 删除借书记录
	 * @param id
	 * @return
	 */
	public boolean deleteBorrow(String id) {
		boolean flag = bookHistoryRepository.remove(id);
		borrowViewRepository.cleanCache();
		return flag;
	}

	/**
	 * 更新借书记录，还书
	 * @param
	 * @return
	 * @throws
	 */
	public void returnBook(String id, String todate) {
//		BookHistoryEntity bookHistoryEntity = (BookHistoryEntity) bookHistoryRepository.findOneQuietly(
//				new Expression("id=?"), new Object[] {id});
		BookHistoryEntity bookHistoryEntity = new BookHistoryEntity();
		bookHistoryEntity.setId(id);
		bookHistoryEntity.setTodate(str2time(todate));
		bookHistoryRepository.save(bookHistoryEntity, "todate");
		borrowViewRepository.cleanCache();
	}
	//------------------------------------------------图书功能----------------------------------------------------------------

	/**
	 * 获取大类列表
	 */
	public List<BookCategoryEntity> categoryList() {
		return (List<BookCategoryEntity>) bookCategoryRepository.findAll();
	}

	/**
	 * 获取小类列表，可得到全部列表或根据大类获取对应的小类列表
	 * @param cid 大类ID
	 * @return
	 */
	public List<BookTypeEntity> typeList(String cid) {
		if (cid != null) {
			return (List<BookTypeEntity>) bookTypeRepository.findAll(new Expression("cid=?"), new Object[] { cid });
		}
		return (List<BookTypeEntity>) bookTypeRepository.findAll();

	}

	/**
	 * 获取图书列表，可得到全部列表或根据大类获取对应的图书列表
	 * @param page 页面信息 bcaption(可为空) 图书大类
	 * @return
	 */
	public List<BookViewEntity> bookList(PageRequest page, String bcaption, String scaption) {
		PageResult<BookViewEntity> result = bookViewRepository.findAll(page);
		if (bcaption != "" && bcaption != null) {
			result = bookViewRepository.findAll(page, new Expression("bcaption=?"), new Object[] { bcaption });
		}
		if (scaption != "" && scaption != null) {
			result = bookViewRepository.findAll(page, new Expression("scaption=?"), new Object[] { scaption });
		}
		return result.list();
	}

	/**
	 * 获取图书列表大小
	 * @param
	 * @return
	 * @throws
	 */
	public int bookSize() {
		return bookInfoRepository.getTotalCount();
	}

	/**
	 * 检查书本是否存在
	 * @param name 书本名
	 * @return
	 */
	public String bookIsExists(String name) {
		BookInfoEntity bookInfoEntity = bookInfoRepository.findOneQuietly(new Expression("caption=?"),
				new Object[] { name });
		if (bookInfoEntity != null) {
			return "书名重复";
		}
		return null;
	}

	/**
	 * 添加书籍
	 * @param name 书籍名 scaptionn 小类名称 desc 书本描述
	 * @return
	 */
	public void addBook(String name, String tid, String desc) {
		String id = UNID.randomID();
		BookInfoEntity bookInfoEntity = new BookInfoEntity();
		bookInfoEntity.setId(id);
		bookInfoEntity.setTid(tid);
		bookInfoEntity.setCaption(name);
		bookInfoEntity.setDesc(desc);
		bookInfoRepository.add(bookInfoEntity);
	}

	/**
	 * 修改书籍
	 * @param name 书籍名 scaptionn 小类名称 desc 书本描述
	 * @return
	 */
	public void saveBook(String id, String name, String tid, String desc) {
		BookInfoEntity bookInfoEntity = new BookInfoEntity();
		bookInfoEntity.setId(id);
		bookInfoEntity.setTid(tid);
		bookInfoEntity.setCaption(name);
		bookInfoEntity.setDesc(desc);
		if (tid.length() == 1) {
			bookInfoRepository.save(bookInfoEntity, "caption", "desc");
		} else {
			bookInfoRepository.save(bookInfoEntity);
		}
		bookViewRepository.cleanCache();
	}

	public Timestamp str2time (String sdate){
		Timestamp timestamp = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date date = sdf.parse(sdate);
			 timestamp =new Timestamp(date.getTime());
			return timestamp;
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}finally {
			return timestamp;
		}
	}
}
