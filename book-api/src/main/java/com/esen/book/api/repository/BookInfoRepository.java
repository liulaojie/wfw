package com.esen.book.api.repository;

import com.esen.book.api.entity.BookHistoryEntity;
import com.esen.book.api.entity.BookInfoEntity;
import com.esen.ejdbc.jdbc.orm.Query;
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

	/**
	 * 所有书籍数
	 * @param
	 * @return
	 */
	public int getTotalCount(){
		Query<BookInfoEntity> query = getCurrentSession().createQuery(getEntityInfo().getBean(),getEntityName());
		return query.query(null,null,null).calcTotalCount();
	}

		@Override
		protected String getCurrentLoginId() {
				return null;
		}

		@Override
		protected String getCurrentTenantId() {
				return null;
		}
}
