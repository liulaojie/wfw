package com.esen.borrow.web.service;

import com.esen.book.api.entity.BookViewEntity;
import com.esen.book.api.repository.BookHistoryRepository;
import com.esen.book.api.repository.BookViewRepository;
import com.esen.borrow.api.Repository.BorrowViewRepository;
import com.esen.borrow.api.entity.BorrowViewEntity;
import com.esen.ejdbc.jdbc.ConnectFactoryManager;
import com.esen.ejdbc.params.PageRequest;
import com.esen.ejdbc.params.PageResult;
import com.esen.eorm.annotation.ApplicationService;
import com.esen.eorm.service.AbstractService;
import com.esen.eutil.util.exp.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 借阅的SERVICE层
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
//------------------------------------------------图书功能----------------------------------------------------------------
	/**
	 * 获取图书列表
	 * @param page 页面信息 bcaption(可为空) 图书大类
	 * @return List<BookViewEntity> 图书列表
	 * @throws
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


}
