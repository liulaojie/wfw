package com.esen.book.api.repository;


import com.esen.book.api.entity.BookHistoryEntity;
import com.esen.ejdbc.jdbc.orm.Query;
import com.esen.eorm.annotation.ApplicationRepository;
import com.esen.eorm.repository.AbstractRepository;
import com.esen.eutil.util.exp.Expression;

@ApplicationRepository
public class BookHistoryRepository extends AbstractRepository {

		/**
		 * person的所有借阅记录数
		 * @param person 借阅人的姓名
		 * @return
		 */
		public int getTotalCountByPerson(String person){
				Query<BookHistoryEntity> query = getCurrentSession().createQuery(getEntityInfo().getBean(),getEntityName());
				return query.query(new Expression("person = ?"),null,person).calcTotalCount();
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
