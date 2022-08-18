package com.esen.borrow.api.Repository;

import com.esen.book.api.entity.BookCategoryEntity;
import com.esen.book.api.entity.BookHistoryEntity;
import com.esen.book.api.entity.BookInfoEntity;
import com.esen.book.api.entity.BookTypeEntity;
import com.esen.book.api.repository.BookCategoryRepository;
import com.esen.book.api.repository.BookHistoryRepository;
import com.esen.book.api.repository.BookInfoRepository;
import com.esen.book.api.repository.BookTypeRepository;

import com.esen.borrow.api.entity.BorrowViewEntity;

import com.esen.ecore.commons.BeanNames;
import com.esen.ejdbc.jdbc.ConnectFactoryManager;
import com.esen.ejdbc.jdbc.dialect.DbDefiner;
import com.esen.ejdbc.jdbc.dialect.Dialect;
import com.esen.ejdbc.jdbc.orm.EntityInfo;
import com.esen.ejdbc.jdbc.orm.Query;
import com.esen.eorm.annotation.ApplicationRepository;
import com.esen.eorm.repository.AbstractRepository;
import com.esen.eutil.util.ExceptionHandler;
import com.esen.eutil.util.exp.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.sql.Connection;


/**
 * 借阅的DAO层
 * 负责数据库的增删改查。
 *
 * @author liuaj
 * @since 20220816
 */

@ApplicationRepository
public class BorrowViewRepository extends AbstractRepository<BorrowViewEntity> {

		@Autowired
		private BookHistoryRepository bhrep;
		@Autowired
		private BookInfoRepository birep;
		@Autowired
		private BookTypeRepository btrep;
		@Autowired
		private BookCategoryRepository bcrep;
		/**
		 * person的所有借阅记录数
		 * @param person 借阅人的姓名
		 * @return
		 */
		public int getTotalCountByPerson(String person){
				Query<BorrowViewEntity> query = getCurrentSession().createQuery(getEntityInfo().getBean(),getEntityName());
				return query.query(new Expression("person = ?"),null,person).calcTotalCount();
		}

		@Autowired
		@Qualifier(BeanNames.ConnectFactoryManager)
		private ConnectFactoryManager connFactoryManager;
		/**
		 * 生成视图
		 * {@inheritDoc}
		 */
		public void repairTable() {
				DbDefiner dbf = connFactoryManager.getDefaultConnectionFactory().getDbDefiner();
				String viewName = getViewName();
				try {
						Connection conn = connFactoryManager.getDefaultConnection();
						try {
							boolean ve = dbf.viewExists(conn, null, "{VERSIONID}_V_BORROW");
								if (ve) {
										dbf.dropView(conn,null, "{VERSIONID}_V_BORROW");
								}
								dbf.createView(conn, null,"{VERSIONID}_V_BORROW", null, getViewSql());
						} finally {
								conn.close();
						}
				} catch (Exception e) {
						ExceptionHandler.rethrowRuntimeException(e);
				}
		}
		/**
		 * 生成视图所用的SQL
		 * @return
		 */
		protected String getViewSql() {
				EntityInfo<BookHistoryEntity> bhEntity = bhrep.getEntityInfo();
				EntityInfo<BookInfoEntity> biEntity = birep.getEntityInfo();
				EntityInfo<BookTypeEntity> btEntity = btrep.getEntityInfo();
				EntityInfo<BookCategoryEntity> bcEntity = bcrep.getEntityInfo();
				StringBuffer str = new StringBuffer();
				str.append(" SELECT ");
				str.append("bh.").append(bhEntity.getProperty("id").getFieldName()).append(" , ");
				str.append("bh.").append(bhEntity.getProperty("person").getFieldName()).append(" , ");
				str.append("bi.").append(bhEntity.getProperty("caption").getFieldName()).append(" AS book_, ");
				str.append("bt.").append(bhEntity.getProperty("caption").getFieldName()).append(" AS scaption_, ");
				str.append("bc.").append(bhEntity.getProperty("caption").getFieldName()).append(" AS bcaption_, ");
				str.append("bh.").append(bhEntity.getProperty("fromdate").getFieldName()).append("  , ");
				str.append("bh.").append(bhEntity.getProperty("todate").getFieldName()).append(" ");
				str.append("FROM ");
				str.append(bhEntity.getTable()).append(" bh, ");
				str.append(biEntity.getTable()).append(" bi, ");
				str.append(btEntity.getTable()).append(" bt, ");
				str.append(bcEntity.getTable()).append(" bc  ");
				str.append("WHERE ");
				str.append("bh.").append(bhEntity.getProperty("bid").getFieldName()).append(" = ");
				str.append("bi.").append(biEntity.getProperty("id").getFieldName()).append(" and ");
				str.append("bi.").append(bhEntity.getProperty("tid").getFieldName()).append(" = ");
				str.append("bt.").append(biEntity.getProperty("id").getFieldName()).append(" and ");
				str.append("bt.").append(bhEntity.getProperty("cid").getFieldName()).append(" = ");
				str.append("bc.").append(biEntity.getProperty("id").getFieldName());
				return str.toString();
		}

		public void throwViewNoSupportMethodException() {
				ExceptionHandler.throwRuntimeException("com.esen.emeta.base.metaviewrepository.view.method.error",
								"视图Repository不支持该方法调用");
		}

		public String getViewName() {
				return this.getEntityInfo().getTable();
		}

	protected Dialect getDialect() {
		return connFactoryManager.getDefaultConnectionFactory().getDialect();
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
