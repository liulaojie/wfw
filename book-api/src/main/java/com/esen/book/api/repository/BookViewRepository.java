package com.esen.book.api.repository;

import com.esen.book.api.entity.*;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 图书视图的Repository仓库类
 *包含了生成视图的方法和对应的sql语句
 * @author liuaj
 * @since 20220824
 */
@ApplicationRepository
@Primary
public class BookViewRepository extends AbstractRepository<BookViewEntity> {

	/**
	 * 图书信息表的Repository类
	 */
	@Autowired
	@Qualifier("BookInfoRepository")
	private BookInfoRepository birep;
	/**
	 * 图书种类表的Repository类
	 */
	@Autowired
	@Qualifier("BookTypeRepository")
	private BookTypeRepository btrep;
	/**
	 * 图书分类表的Repository类
	 */
	@Autowired
	@Qualifier("BookCategoryRepository")
	private BookCategoryRepository bcrep;
	/**
	 * 连接工厂管理类
	 */
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

	/**
	 * 获取视图名
	 * @return
	 */
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
		str.append("bt.").append(btEntity.getProperty("id").getFieldName()).append(" AS tid_, ");
		str.append("bt.").append(btEntity.getProperty("caption").getFieldName()).append(" AS scaption_, ");
		str.append("bc.").append(bcEntity.getProperty("id").getFieldName()).append(" AS cid_, ");
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
