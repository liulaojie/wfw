package com.esen.book.api.repository;

import com.esen.book.api.entity.BookCategoryEntity;
import com.esen.eorm.annotation.ApplicationRepository;
import com.esen.eorm.repository.AbstractRepository;

/**
 *图书分类的Repository仓库
 *
 * @author liuaj
 * @since 20220824
 */
@ApplicationRepository
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
