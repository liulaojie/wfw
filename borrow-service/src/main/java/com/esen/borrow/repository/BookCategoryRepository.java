package com.esen.borrow.repository;

import org.springframework.context.annotation.Primary;

import com.esen.book.api.entity.BookCategoryEntity;
import com.esen.eorm.annotation.ApplicationRepository;
import com.esen.eorm.repository.AbstractRepository;

/**
 *图书分类的Repository仓库类
 *
 * @author liuaj
 * @since 20220824
 */
@ApplicationRepository(value = "BookCategoryRepository")
@Primary
public class BookCategoryRepository extends AbstractRepository<BookCategoryEntity> {

		@Override
		protected String getCurrentLoginId() {
				return null;
		}

		@Override
		protected String getCurrentTenantId() {
				return null;
		}
}
