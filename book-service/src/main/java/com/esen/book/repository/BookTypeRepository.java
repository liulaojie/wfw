package com.esen.book.repository;

import com.esen.book.api.entity.BookTypeEntity;
import com.esen.eorm.annotation.ApplicationRepository;
import com.esen.eorm.repository.AbstractRepository;
import org.springframework.context.annotation.Primary;


/**
 * 图书种类的Repository仓库类
 *
 * @author liuaj
 * @since 20220824
 */
@ApplicationRepository(value = "BookTypeRepository")
@Primary
public class BookTypeRepository extends AbstractRepository<BookTypeEntity> {

		@Override
		protected String getCurrentLoginId() {
				return null;
		}

		@Override
		protected String getCurrentTenantId() {
				return null;
		}
}
