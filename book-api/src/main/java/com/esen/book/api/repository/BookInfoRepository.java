package com.esen.book.api.repository;

import com.esen.book.api.entity.BookInfoEntity;
import com.esen.eorm.annotation.ApplicationRepository;
import com.esen.eorm.repository.AbstractRepository;
/**
 * 图书信息的Repository仓库
 *
 * @author liuaj
 * @since 20220824
 */
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
