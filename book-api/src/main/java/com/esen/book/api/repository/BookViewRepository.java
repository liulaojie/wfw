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
 * 图书的Repository仓库
 *
 * @author liuaj
 * @since 20220824
 */
@ApplicationRepository
@Primary
public class BookViewRepository extends AbstractRepository<BookViewEntity> {

	/**
	 * 图书信息表的Repository
	 */
	@Autowired
	@Qualifier("BookInfoRepository")
	private BookInfoRepository birep;
	/**
	 * 图书种类表的Repository
	 */
	@Autowired
	@Qualifier("BookTypeRepository")
	private BookTypeRepository btrep;
	/**
	 * 图书分类表的Repository
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
	 * 通过大类id得到对应大类的书籍列表
	 * @param cid 大类ID
	 * @return 图书视图的PageResult
	 */
	public PageResult<BookViewEntity> findAllByCid(PageRequest page,String cid){
		EntityInfo<BookViewEntity>bvEntityInfo = this.getEntityInfo();
		EntityInfo<BookCategoryEntity>bcEntityInfo = bcrep.getEntityInfo();
		StringBuilder sql = new StringBuilder();
		//select * from esc55_V_book where bcaption_ = (select caption_ from esc55_book_category where id_=cid)
		sql.append("select * from ").append(bvEntityInfo.getTable()).append(" where ");
		sql.append(bvEntityInfo.getProperty("bcaption").getFieldName()).append(" = (select ");
		sql.append(bcEntityInfo.getProperty("caption").getFieldName()).append(" from ");
		sql.append(bcEntityInfo.getTable()).append(" where ").append(bcEntityInfo.getProperty("id").getFieldName());
		sql.append(" = '").append(cid).append("' )");

		PageResult<BookViewEntity> result = this.query(sql.toString(), page, new RowHandler<BookViewEntity>() {
			@Override
			public BookViewEntity processRow(ResultSet resultSet) throws SQLException {
				BookViewEntity bookViewEntity = new BookViewEntity();
				bookViewEntity.setId(resultSet.getString("id_"));
				bookViewEntity.setName(resultSet.getString("name_"));
				bookViewEntity.setScaption(resultSet.getString("scaption_"));
				bookViewEntity.setBcaption(resultSet.getString("bcaption_"));
				bookViewEntity.setDesc(resultSet.getString("desc_"));
				return bookViewEntity;
			}
		});
		return result;
	}

	/**
	 * 通过小类id得到对应小类的书籍列表
	 * @param tid 小类ID
	 * @return 图书视图的PageResult
	 */
	public PageResult<BookViewEntity> findAllByTid(PageRequest page,String tid){
		EntityInfo<BookViewEntity>bvEntityInfo = this.getEntityInfo();
		EntityInfo<BookTypeEntity>btEntityInfo = btrep.getEntityInfo();
		StringBuilder sql = new StringBuilder();
		//select * from esc55_V_book where scaption_ = (select caption_ from esc55_book_type where id_=tid)
		sql.append("select * from ").append(bvEntityInfo.getTable()).append(" where ");
		sql.append(bvEntityInfo.getProperty("scaption").getFieldName()).append(" = (select ");
		sql.append(btEntityInfo.getProperty("caption").getFieldName()).append(" from ");
		sql.append(btEntityInfo.getTable()).append(" where ").append(btEntityInfo.getProperty("id").getFieldName());
		sql.append(" = '").append(tid).append("' )");

		PageResult<BookViewEntity> result = this.query(sql.toString(), page, new RowHandler<BookViewEntity>() {
			@Override
			public BookViewEntity processRow(ResultSet resultSet) throws SQLException {
				BookViewEntity bookViewEntity = new BookViewEntity();
				bookViewEntity.setId(resultSet.getString("id_"));
				bookViewEntity.setName(resultSet.getString("name_"));
				bookViewEntity.setScaption(resultSet.getString("scaption_"));
				bookViewEntity.setBcaption(resultSet.getString("bcaption_"));
				bookViewEntity.setDesc(resultSet.getString("desc_"));
				return bookViewEntity;
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
	 * 获取视图名
	 * @return
	 */
	public String getViewName() {
		return this.getEntityInfo().getTable();
	}

	/**
	 * 生成视图所用的SQL
	 * SELECT bi.ID_ , bi.CAPTION_ AS name_, bi.DESC_ , bt.CAPTION_ AS scaption_, bc.CAPTION_ AS bcaption_
	 * FROM ESC55_BOOK_INFO bi, ESC55_BOOK_TYPE bt, ESC55_BOOK_CATEGORY bc
	 * WHERE bi.TID_ = bt.ID_ and bt.CID_ = bc.ID_;
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
