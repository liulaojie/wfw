package com.esen.borrow.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.esen.book.api.entity.*;
import com.esen.book.api.repository.*;
import com.esen.borrow.api.repository.BorrowViewRepository;
import com.esen.borrow.api.entity.BorrowViewEntity;
import com.esen.ejdbc.params.PageRequest;
import com.esen.ejdbc.params.PageResult;
import com.esen.eorm.annotation.ApplicationService;
import com.esen.eorm.service.AbstractService;
import com.esen.eutil.util.UNID;
import com.esen.eutil.util.exp.Expression;

/**
 * 借阅管理的SERVICE层
 *
 * @author liuaj
 * @since 20220816
 */
@ApplicationService
public class BorrowService extends AbstractService<BorrowViewEntity> {


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
	 * 获取借阅列表
	 * @param
	 * @return
	 * @throws
	 */
	public PageResult<BorrowViewEntity> borrowList(PageRequest page,String scaption) {
		PageResult<BorrowViewEntity> result = null;
		if (scaption != "" && scaption != null) {
			result = borrowViewRepository.findAll(page, new Expression("scaption=?"), new Object[] { scaption });
		}else{
			result = borrowViewRepository.findAll(page);
		}
		return result;
	}
	/**
	 * 获取借阅列表大小，或获取对应小类的借阅列表大小
	 * @param scaption 小类名(可为空)
	 * @return
	 * @throws
	 */
	public int borrowSize(String scaption) {
		if (scaption != "" && scaption != null) {
			return borrowViewRepository.getTotalCountByScaption(scaption);
		}else{
			return bookHistoryRepository.getTotalCount();
		}
	}

	/**
	 * 添加借书记录
	 * @param bookHistoryEntity 借阅记录实体
	 * @return
	 */
	public void addBorrow(BookHistoryEntity bookHistoryEntity) {
		bookHistoryRepository.add(bookHistoryEntity);
		bookHistoryRepository.cleanCache();
		borrowViewRepository.cleanCache();
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
	public void returnBook(BookHistoryEntity bookHistoryEntity) {
		bookHistoryRepository.save(bookHistoryEntity, "todate");
		bookHistoryRepository.cleanCache();
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
	public List<BookTypeEntity> typeList(String cid,String bcaption) {
		if (cid.length()>1) {
			return (List<BookTypeEntity>) bookTypeRepository.findAll(new Expression("cid=?"), new Object[] { cid });
		}
		if (bcaption!="null"&&bcaption!=null){
			return bookTypeRepository.getTypeListByBcaption(bcaption);
		}
		return (List<BookTypeEntity>) bookTypeRepository.findAll();

	}

	/**
	 * 获取图书列表，可得到全部列表或根据大类获取对应的图书列表
	 * @param page 页面信息 bcaption(可为空) 图书大类
	 * @return
	 */
	public PageResult<BookViewEntity> bookList(PageRequest page, String bcaption, String scaption) {
		PageResult<BookViewEntity> result = null;
		if (bcaption != "" && bcaption != null) {
			result = bookViewRepository.findAll(page, new Expression("bcaption=?"), new Object[] { bcaption });
		}else{
			if (scaption != "" && scaption != null){
				result = bookViewRepository.findAll(page, new Expression("scaption=?"), new Object[] { scaption });
			}else{
				result = bookViewRepository.findAll(page);
			}
		}
		return result;
	}

	/**
	 * 获取图书列表大小，或获取对应大类的借阅列表大小
	 * @param bcaption 大类名(可为空)
	 * @return
	 * @throws
	 */
	public int bookSize(String bcaption) {
		if (bcaption != "" && bcaption != null) {
			return bookViewRepository.getTotalCountByBcaption(bcaption);
		}else{
			return bookInfoRepository.getTotalCount();
		}
	}

	/**
	 * 检查书本是否存在
	 * @param name 书本名
	 * @return
	 */
	public boolean bookIsExists(String name) throws RuntimeException {
		BookInfoEntity bookInfoEntity = bookInfoRepository.findOneQuietly(new Expression("caption=?"),
				new Object[] { name });
		if (bookInfoEntity != null) {
			return true;
		}
		return false;
	}

	/**
	 * 添加书籍
	 * @param bookInfoEntity 图书实体
	 * @return
	 */
	public void addBook(BookInfoEntity bookInfoEntity) {
		bookInfoRepository.add(bookInfoEntity);
		bookInfoRepository.cleanCache();
		bookViewRepository.cleanCache();
	}

	/**
	 * 修改书籍
	 * @param bookInfoEntity 书籍名 图书实体
	 * @return
	 */
	public void saveBook(BookInfoEntity bookInfoEntity) {
		if (bookInfoEntity.getTid().length() == 1) {
			bookInfoRepository.save(bookInfoEntity, "caption", "desc");
		} else {
			bookInfoRepository.save(bookInfoEntity);
		}
		bookInfoRepository.cleanCache();
		bookViewRepository.cleanCache();
	}

}
