package com.esen.book.service;

import com.esen.book.api.entity.BookCategoryEntity;
import com.esen.book.api.entity.BookInfoEntity;
import com.esen.book.api.entity.BookTypeEntity;
import com.esen.book.api.entity.BookViewEntity;
import com.esen.book.api.repository.*;
import com.esen.ejdbc.params.PageRequest;
import com.esen.ejdbc.params.PageResult;
import com.esen.eorm.annotation.ApplicationService;
import com.esen.eorm.service.AbstractService;
import com.esen.eutil.util.StrFunc;
import com.esen.eutil.util.exp.Expression;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 对于这个类的描述.
 *
 * @author liuaj
 * @since 2022/9/5
 */
@ApplicationService
public class BookService  extends AbstractService<BookViewEntity> {

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
			return (List<BookTypeEntity>) bookTypeRepository.findAll(new Expression("cid=?"), new Object[] { cid });
	}

	/**
	 * 获取图书列表，可得到全部列表或根据大类获取对应的图书列表
	 * @param page 页面信息
	 * @param cid 大类ID
	 * @param tid 小类ID
	 * @return
	 */
	public PageResult<BookViewEntity> bookList(PageRequest page, String cid, String tid) {
		PageResult<BookViewEntity> result = null;
		if (!StrFunc.isNull(cid)) {
			result = bookViewRepository.findAllByCid(page,cid);
		}else{
			if (!StrFunc.isNull(tid)){
				result = bookViewRepository.findAllByTid(page, tid);
			}else{
				result = bookViewRepository.findAll(page);
			}
		}
		return result;
	}


	/**
	 * 检查书本是否存在
	 * @param name 书本名
	 * @return
	 */
	public boolean bookIsExists(String name) {
		BookInfoEntity bookInfoEntity =
				bookInfoRepository.findOneQuietly(new Expression("caption=?"), new Object[] { name });
		return bookInfoEntity != null;

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
	 * 保存书籍修改的数据
	 * @param bookInfoEntity  图书实体
	 * @return
	 */
	public void saveBook(BookInfoEntity bookInfoEntity) {
		bookInfoRepository.save(bookInfoEntity);
		bookInfoRepository.cleanCache();
		bookViewRepository.cleanCache();
	}


}
