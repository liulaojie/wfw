package com.esen.borrow.service;

import com.esen.borrow.api.entity.BookHistoryEntity;
import com.esen.borrow.api.repository.BookHistoryRepository;
import com.esen.eutil.util.StrFunc;
import com.esen.eutil.util.exp.Expression;
import org.springframework.beans.factory.annotation.Autowired;

import com.esen.borrow.api.repository.BorrowViewRepository;
import com.esen.borrow.api.entity.BorrowViewEntity;
import com.esen.ejdbc.params.PageRequest;
import com.esen.ejdbc.params.PageResult;
import com.esen.eorm.annotation.ApplicationService;
import com.esen.eorm.service.AbstractService;

/**
 * 借阅管理的service类
 *实现了获取借阅列表，添加借阅信息，删除借阅信息，修改借阅信息（还书）的功能
 * @author liuaj
 * @since 20220816
 */
@ApplicationService
public class BorrowService extends AbstractService<BorrowViewEntity> {


	@Autowired
	protected BorrowViewRepository borrowViewRepository;

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
			result = borrowViewRepository.findAll(page,new Expression("tid=?"),new Object[]{tid});
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
