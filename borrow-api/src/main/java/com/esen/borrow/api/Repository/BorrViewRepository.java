package com.esen.borrow.api.Repository;

import com.esen.borrow.api.entity.BorrowViewEntity;
import com.esen.ecore.commons.BeanNames;
import com.esen.ejdbc.jdbc.ConnectFactoryManager;
import com.esen.ejdbc.jdbc.dialect.DbDefiner;
import com.esen.ejdbc.jdbc.dialect.Dialect;
import com.esen.eorm.entity.IdEntity;
import com.esen.eorm.repository.AbstractRepository;
import com.esen.eutil.util.ExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.esen.eutil.util.exp.Expression;

import java.sql.Connection;

public abstract class BorrViewRepository<T extends IdEntity> extends AbstractRepository<T> {
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
								boolean ve = dbf.viewExists(conn,"esenabi","STU11_BORROW");
								if (ve) {
										dbf.dropView(conn,"esenabi", "STU11_BORROW");
								}
								dbf.createView(conn, "esenabi","STU11_BORROW", null, getViewSql());
						} finally {
								conn.close();
						}
				} catch (Exception e) {
						ExceptionHandler.rethrowRuntimeException(e);
				}
		}

		protected Dialect getDialect() {
				return connFactoryManager.getDefaultConnectionFactory().getDialect();
		}
		/**
		 * 生成视图所用的SQL
		 * @return
		 */
		protected abstract String getViewSql();
		public String getViewName() {
				return this.getEntityInfo().getTable();
		}

		public void throwViewNoSupportMethodException() {
				ExceptionHandler.throwRuntimeException("com.esen.emeta.base.metaviewrepository.view.method.error",
								"视图Repository不支持该方法调用");
		}

}
