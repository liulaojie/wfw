package com.esen.book.api.entity;

import com.esen.eorm.entity.RootEntity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 图书借阅记录的实体类.
 *
 * @author liuaj
 * @since 20220824
 */
public class BookHistoryEntity extends RootEntity implements Serializable {

		private static final long serialVersionUID = -3764378326887998646L;

		/**
		 * 主键借阅记录ID
		 */
		private String id;
		/**
		 * 借阅者
		 */
		private String person;
		/**
		 * 书籍名
		 */
		private String bid;
		/**
		 * 借阅时间
		 */
		private Timestamp fromdate;
		/**
		 * 归还时间（空表示暂未归还）
		 */
		private Timestamp todate;

	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * @return the id
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * @param id to set
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the person
	 */
	public String getPerson() {
		return person;
	}

	/**
	 * @param person to set
	 */
	public void setPerson(String person) {
		this.person = person;
	}

	/**
	 * @return the bid
	 */
	public String getBid() {
		return bid;
	}

	/**
	 * @param bid to set
	 */
	public void setBid(String bid) {
		this.bid = bid;
	}

	/**
	 * @return the fromdate
	 */
	public Timestamp getFromdate() {
		return fromdate;
	}

	/**
	 * @param fromdate to set
	 */
	public void setFromdate(Timestamp fromdate) {
		this.fromdate = fromdate;
	}

	/**
	 * @return the todate
	 */
	public Timestamp getTodate() {
		return todate;
	}

	/**
	 * @param todate to set
	 */
	public void setTodate(Timestamp todate) {
		this.todate = todate;
	}
}
