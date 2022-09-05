package com.esen.book.api.repository;

import com.esen.book.api.entity.BookCategoryEntity;
import com.esen.book.api.entity.BookHistoryEntity;
import com.esen.book.api.entity.BookTypeEntity;
import com.esen.ejdbc.jdbc.orm.EntityInfo;
import com.esen.ejdbc.jdbc.util.RowHandler;
import com.esen.ejdbc.params.PageRequest;
import com.esen.ejdbc.params.PageResult;
import com.esen.eorm.annotation.ApplicationRepository;
import com.esen.eorm.repository.AbstractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 图书种类的Repository仓库
 *
 * @author liuaj
 * @since 20220824
 */
@ApplicationRepository(value = "BookTypeRepository")
@Primary
public class BookTypeRepository extends AbstractRepository<BookTypeEntity> {
	@Autowired
	private BookCategoryRepository bcrep;

	public List<BookTypeEntity> getTypeListByBcaption(String bcaption){
		EntityInfo<BookTypeEntity>btEntityInfo = this.getEntityInfo();
		EntityInfo<BookCategoryEntity>bcEntityInfo = bcrep.getEntityInfo();
		StringBuilder sql = new StringBuilder();
		//select * from esc55_book_type where cid_ in (select id_ from esc55_book_category where caption_='科技')
		sql.append("select * from ").append(btEntityInfo.getTable()).append(" where ");
		sql.append(btEntityInfo.getProperty("cid").getFieldName()).append(" in (select ");
		sql.append(bcEntityInfo.getProperty("id").getFieldName()).append(" from ");
		sql.append(bcEntityInfo.getTable()).append(" where ").append(bcEntityInfo.getProperty("caption").getFieldName());
		sql.append(" = '").append(bcaption).append("' )");

		PageRequest page = new PageRequest(-1,-1);
		PageResult<BookTypeEntity> result = this.query(sql.toString(), page, new RowHandler<BookTypeEntity>() {
			@Override
			public BookTypeEntity processRow(ResultSet resultSet) throws SQLException {
				BookTypeEntity bookTypeEntity = new BookTypeEntity();
				bookTypeEntity.setId(resultSet.getString("id_"));
				bookTypeEntity.setCid(resultSet.getString("cid_"));
				bookTypeEntity.setCaption(resultSet.getString("caption_"));
				return bookTypeEntity;
			}
		});

		return result.list();
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
