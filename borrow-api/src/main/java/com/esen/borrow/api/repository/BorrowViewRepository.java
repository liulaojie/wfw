package com.esen.borrow.api.repository;

import com.esen.book.api.entity.*;
import com.esen.book.api.repository.*;

import com.esen.borrow.api.entity.BorrowViewEntity;


import com.esen.ejdbc.jdbc.ConnectFactoryManager;
import com.esen.ejdbc.jdbc.dialect.DbDefiner;

import com.esen.ejdbc.jdbc.orm.EntityInfo;
import com.esen.ejdbc.jdbc.util.RowHandler;
import com.esen.ejdbc.params.PageRequest;
import com.esen.ejdbc.params.PageResult;
import com.esen.eorm.annotation.ApplicationRepository;
import com.esen.eorm.repository.AbstractRepository;
import com.esen.eutil.util.ExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * 借阅的Repository仓库
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
		@Autowired
		private ConnectFactoryManager connFactoryManager;

	/**
	 * 通过小类id得到借阅列表
	 * @param tid 小类ID
	 * @return
	 */
	public PageResult<BorrowViewEntity> findAllByTid(PageRequest page, String tid){
		EntityInfo<BorrowViewEntity>bvEntityInfo = this.getEntityInfo();
		EntityInfo<BookTypeEntity>btEntityInfo = btrep.getEntityInfo();
		StringBuilder sql = new StringBuilder();
		//select * from esc55_V_borrow where scaption_ = (select caption_ from esc55_book_type where id_=tid)
		sql.append("select * from ").append(bvEntityInfo.getTable()).append(" where ");
		sql.append(bvEntityInfo.getProperty("scaption").getFieldName()).append(" = (select ");
		sql.append(btEntityInfo.getProperty("caption").getFieldName()).append(" from ");
		sql.append(btEntityInfo.getTable()).append(" where ").append(btEntityInfo.getProperty("id").getFieldName());
		sql.append(" = '").append(tid).append("' )");

		PageResult<BorrowViewEntity> result = this.query(sql.toString(), page, new RowHandler<BorrowViewEntity>() {
			@Override
			public BorrowViewEntity processRow(ResultSet resultSet) throws SQLException {
				BorrowViewEntity borrowViewEntity = new BorrowViewEntity();
				borrowViewEntity.setId(resultSet.getString("id_"));
				borrowViewEntity.setBook(resultSet.getString("book_"));
				borrowViewEntity.setPerson(resultSet.getString("person_"));
				borrowViewEntity.setScaption(resultSet.getString("scaption_"));
				borrowViewEntity.setBcaption(resultSet.getString("bcaption_"));
				borrowViewEntity.setFromdate(resultSet.getTimestamp("fromdate_"));
				borrowViewEntity.setTodate(resultSet.getTimestamp("todate_"));
				return borrowViewEntity;
			}
		});
		return result;
	}

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
							boolean ve = dbf.viewExists(conn, null, viewName);
								if (ve) {
										dbf.dropView(conn,null, viewName);
								}
								dbf.createView(conn, null,viewName, null, getViewSql());
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
				str.append("bi.").append(biEntity.getProperty("caption").getFieldName()).append(" AS book_, ");
				str.append("bt.").append(btEntity.getProperty("caption").getFieldName()).append(" AS scaption_, ");
				str.append("bc.").append(bcEntity.getProperty("caption").getFieldName()).append(" AS bcaption_, ");
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
				str.append("bi.").append(biEntity.getProperty("tid").getFieldName()).append(" = ");
				str.append("bt.").append(btEntity.getProperty("id").getFieldName()).append(" and ");
				str.append("bt.").append(btEntity.getProperty("cid").getFieldName()).append(" = ");
				str.append("bc.").append(bcEntity.getProperty("id").getFieldName());
				return str.toString();
		}

		/**
		 * 得到视图名
		 * @return
	 	*/
		public String getViewName() {
				return this.getEntityInfo().getTable();
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
