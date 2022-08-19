package com.esen.borrow.web.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
	public List<BorrowViewEntity> borrowList(PageRequest page){
		PageResult<BorrowViewEntity>result = borrowViewRepository.findAll(page);
		return result.list();
	}
	/**
	 * 添加借书记录
	 * @param person 借阅人 bid 书籍ID fromdate 借书时间
	 * @return
	 */
	public void addBorrow(String person, String bid, Timestamp fromdate){
		String id = UNID.randomID();
		BookHistoryEntity bookHistoryEntity = new BookHistoryEntity();
		bookHistoryEntity.setId(id);
		bookHistoryEntity.setBid(bid);
		bookHistoryEntity.setPerson(person);
		bookHistoryEntity.setFromdate(fromdate);

	}
	public boolean deleteBorrow(BorrowViewEntity borrowViewEntity){
		return bookHistoryRepository.remove(borrowViewEntity.getId());
	}

	/**
	 * 更新借书记录，还书
	 * @param
	 * @return
	 * @throws
	 */
	public void returnBook(BorrowViewEntity borrowViewEntity){
		String id = borrowViewEntity.getId();
		BookHistoryEntity bookHistoryEntity=
				(BookHistoryEntity) bookHistoryRepository.findOneQuietly(new Expression("id=?"),new Object[]{id});
		bookHistoryEntity.setTodate(borrowViewEntity.getTodate());
		bookHistoryRepository.save(bookHistoryEntity,"todate");
	}
//------------------------------------------------图书功能----------------------------------------------------------------

	/**
	 * 获取大类列表
	 */
	public List<BookCategoryEntity> categoryList(){

		return (List<BookCategoryEntity>)bookCategoryRepository.findAll();
	}
	/**
	 * 获取小类列表，可得到全部列表或根据大类获取对应的小类列表
	 * @param cid 大类ID
	 * @return
	 */
	public List<BookTypeEntity> typeList(String cid){
		if (cid!=null){
			return (List<BookTypeEntity>)bookTypeRepository.findAll(new Expression("cid=?"),new Object[]{cid});
		}
		return (List<BookTypeEntity>) bookTypeRepository.findAll();

	}
	/**
	 * 获取图书列表，可得到全部列表或根据大类获取对应的图书列表
	 * @param page 页面信息 bcaption(可为空) 图书大类
	 * @return
	 */
	public List<BookViewEntity> bookList(PageRequest page,String bcaption){
		PageResult<BookViewEntity> result = bookViewRepository.findAll(page);
		if (bcaption!=null){
			Expression expression = new Expression("bcaption=?");
			result = bookViewRepository.findAll(page, null,expression,
					bcaption);
		}
		return result.list();
	}
	/**
	 * 检查书本是否存在
	 * @param name 书本名
	 * @return
	 */
	public  String bookIsExists(String name){
		BookInfoEntity bookInfoEntity =
				bookInfoRepository.findOneQuietly(new Expression("caption=?"),new Object[]{name});
		if (bookInfoEntity!=null){
			return "书名重复";
		}
		return null;
	}
	/**
	 * 添加书籍
	 * @param name 书籍名 scaptionn 小类名称 desc 书本描述
	 * @return
	 */
	public void addBook( String name,String tid,String desc){
		String id = UNID.randomID();
		BookInfoEntity bookInfoEntity = new BookInfoEntity();
		bookInfoEntity.setId(id);
		bookInfoEntity.setTid(tid);
		bookInfoEntity.setCaption(name);
		bookInfoEntity.setDesc(desc);
		bookInfoRepository.add(bookInfoEntity);
	}

}
