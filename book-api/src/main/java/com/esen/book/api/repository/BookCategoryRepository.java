package com.esen.book.api.repository;

import com.esen.book.api.entity.BookCategoryEntity;
import com.esen.eorm.annotation.ApplicationRepository;
import com.esen.eorm.repository.AbstractRepository;


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
