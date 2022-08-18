package com.esen.borrow.api.Repository;

import com.esen.borrow.api.entity.BorrowViewEntity;
import com.esen.ejdbc.jdbc.dialect.Dialect;
import com.esen.ejdbc.jdbc.orm.EntityInfo;
import com.esen.ejdbc.jdbc.orm.Query;
import com.esen.eorm.annotation.ApplicationRepository;
import com.esen.eutil.util.exp.Expression;


/**
 * 借阅的DAO层
 * 负责数据库的增删改查。
 *
 * @author liuaj
 * @since 20220816
 */

@ApplicationRepository
public class BorrowViewRepository extends BorrViewRepository<BorrowViewEntity> {

		private BorrViewRepository borrViewRepository;
		/**
		 * person的所有借阅记录数
		 * @param person 借阅人的姓名
		 * @return
		 */
		public int getTotalCountByPerson(String person){
				Query<BorrowViewEntity> query = getCurrentSession().createQuery(getEntityInfo().getBean(),getEntityName());
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

		@Override
		protected String getViewSql() {
				return null;
		}
}
