package com.esen.borrow.repository;


import com.esen.borrow.api.entity.BookHistoryEntity;
import com.esen.eorm.annotation.ApplicationRepository;
import com.esen.eorm.repository.AbstractRepository;
import org.springframework.context.annotation.Primary;

/**
 * 图书借阅记录的Repository仓库
 *
 * @author liuaj
 * @since 20220824
 */
@ApplicationRepository(path = "/config/mapping/bookhistoryentity-mapping.xml",value = "BookHistoryRepository")
@Primary
public class BookHistoryRepository extends AbstractRepository<BookHistoryEntity> {

		@Override
		protected String getCurrentLoginId() {
				return null;
		}

		@Override
		protected String getCurrentTenantId() {
				return null;
		}
}
