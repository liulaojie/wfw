package com.esen.book.api.repository;

import com.esen.book.api.entity.BookHistoryEntity;
import com.esen.book.api.entity.BookInfoEntity;
import com.esen.ejdbc.jdbc.orm.Query;
import com.esen.eorm.annotation.ApplicationRepository;
import com.esen.eorm.repository.AbstractRepository;
import org.springframework.context.annotation.Primary;

/**
 * 图书信息的Repository仓库
 *
 * @author liuaj
 * @since 20220824
 */
@ApplicationRepository(value = "BookInfoRepository")
@Primary
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
