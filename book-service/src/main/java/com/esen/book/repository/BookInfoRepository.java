package com.esen.book.repository;

import com.esen.book.api.entity.BookInfoEntity;
import com.esen.eorm.annotation.ApplicationRepository;
import com.esen.eorm.repository.AbstractRepository;
import org.springframework.context.annotation.Primary;

/**
 * 图书信息的Repository仓库类
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
