package com.esen.book.api.repository;

import com.esen.book.api.entity.*;
import com.esen.ejdbc.jdbc.ConnectFactoryManager;
import com.esen.ejdbc.jdbc.dialect.DbDefiner;
import com.esen.ejdbc.jdbc.orm.EntityInfo;
import com.esen.eorm.annotation.ApplicationRepository;
import com.esen.eorm.repository.AbstractRepository;
import com.esen.eutil.util.ExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
/**
 * 图书的Repository仓库
 *
 * @author liuaj
 * @since 20220824
 */
@ApplicationRepository
public class BookViewRepository extends AbstractRepository<BookViewEntity> {

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

	public String getViewName() {
		return this.getEntityInfo().getTable();
	}

	/**
	 * 生成视图所用的SQL
	 * @return
	 */
	protected String getViewSql() {
		EntityInfo<BookInfoEntity> biEntity = birep.getEntityInfo();
		EntityInfo<BookTypeEntity> btEntity = btrep.getEntityInfo();
		EntityInfo<BookCategoryEntity> bcEntity = bcrep.getEntityInfo();
		StringBuffer str = new StringBuffer();
		str.append(" SELECT ");
		str.append("bi.").append(biEntity.getProperty("id").getFieldName()).append(" , ");
		str.append("bi.").append(biEntity.getProperty("caption").getFieldName()).append(" AS name_, ");
		str.append("bi.").append(biEntity.getProperty("desc").getFieldName()).append(" , ");
		str.append("bt.").append(btEntity.getProperty("caption").getFieldName()).append(" AS scaption_, ");
		str.append("bc.").append(bcEntity.getProperty("caption").getFieldName()).append(" AS bcaption_ ");
		str.append("FROM ");
		str.append(biEntity.getTable()).append(" bi, ");
		str.append(btEntity.getTable()).append(" bt, ");
		str.append(bcEntity.getTable()).append(" bc  ");
		str.append("WHERE ");
		str.append("bi.").append(biEntity.getProperty("tid").getFieldName()).append(" = ");
		str.append("bt.").append(btEntity.getProperty("id").getFieldName()).append(" and ");
		str.append("bt.").append(btEntity.getProperty("cid").getFieldName()).append(" = ");
		str.append("bc.").append(bcEntity.getProperty("id").getFieldName());
		return str.toString();
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
