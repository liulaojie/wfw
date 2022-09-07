package com.esen.borrow.service;

import com.esen.eutil.util.StrFunc;
import org.springframework.beans.factory.annotation.Autowired;

import com.esen.book.api.entity.*;
import com.esen.book.api.repository.*;
import com.esen.borrow.api.repository.BorrowViewRepository;
import com.esen.borrow.api.entity.BorrowViewEntity;
import com.esen.ejdbc.params.PageRequest;
import com.esen.ejdbc.params.PageResult;
import com.esen.eorm.annotation.ApplicationService;
import com.esen.eorm.service.AbstractService;
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


	/**
	 * 获取借阅列表大小，或获取对应小类的借阅列表大小
	 * @param tid 小类id(可为空)
	 * @return
	 * @throws
	 */
	public PageResult<BorrowViewEntity> borrowList(PageRequest page,String tid) {
		PageResult<BorrowViewEntity> result = null;
		if (StrFunc.isNull(tid)){
			result = borrowViewRepository.findAll(page);
		}else{
			result = borrowViewRepository.findAllByTid(page, tid);
		}
		return result;
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
	 * @param id 需要删除数据的ID
	 * @return
	 */
	public boolean deleteBorrow(String id) {
		boolean flag = bookHistoryRepository.remove(id);
		borrowViewRepository.cleanCache();
		return flag;
	}

	/**
	 * 更新借书记录，还书
	 * @param bookHistoryEntity 借阅记录实体
	 * @return
	 * @throws
	 */
	public void returnBook(BookHistoryEntity bookHistoryEntity) {
		bookHistoryRepository.save(bookHistoryEntity, "todate");
		bookHistoryRepository.cleanCache();
		borrowViewRepository.cleanCache();
	}


}
