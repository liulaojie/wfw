package com.esen.book.api.repository;

import com.esen.book.api.entity.BookInfoEntity;
import com.esen.eorm.annotation.ApplicationRepository;
import com.esen.eorm.repository.AbstractRepository;

@ApplicationRepository
public class BookInfoRepository extends AbstractRepository<BookInfoEntity> {

		@Override
		protected String getCurrentLoginId() {
				return null;
		}

		@Override
		protected String getCurrentTenantId() {
				return null;
		}
}
