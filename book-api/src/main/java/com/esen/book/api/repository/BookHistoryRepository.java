package com.esen.book.api.repository;


import com.esen.book.api.entity.BookHistoryEntity;
import com.esen.ejdbc.jdbc.orm.Query;
import com.esen.eorm.annotation.ApplicationRepository;
import com.esen.eorm.repository.AbstractRepository;
import com.esen.eutil.util.exp.Expression;
import org.springframework.context.annotation.Primary;

/**
 * 图书借阅记录的Repository仓库
 *
 * @author liuaj
 * @since 20220824
 */
@ApplicationRepository(path = "/config/mapping/bookhistoryentity-mapping.xml" ,value = "BookHistoryRepository")
@Primary
public class BookHistoryRepository extends AbstractRepository {

		/**
		 * 所有借阅记录数
		 * @param
		 * @return
		 */
		public int getTotalCount(){
				Query<BookHistoryEntity> query = getCurrentSession().createQuery(getEntityInfo().getBean(),getEntityName());
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
