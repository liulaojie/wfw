package com.esen.borrow.api.entity;


import com.esen.eorm.entity.RootEntity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 借阅信息的实体类
 *
 * @author liuaj
 * @since 20220824
 */
public class BorrowViewEntity extends RootEntity  implements Serializable {
		/**
		 * 序列号
		 */
		private static final long serialVersionUID = -935089358231842005L;
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
		private String book;
		/**
		 * 借阅时间
		 */
		private Timestamp fromdate;
		/**
		 * 归还时间
		 */
		private Timestamp todate;
		/**
		 * 书籍小类
		 */
		private String scaption;
		/**
		 * 书籍大类
		 */
		private String bcaption;

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
	 * @return the book
	 */
	public String getBook() {
		return book;
	}

	/**
	 * @param book to set
	 */
	public void setBook(String book) {
		this.book = book;
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

	/**
	 * @return the scaption
	 */
	public String getScaption() {
		return scaption;
	}

	/**
	 * @param scaption to set
	 */
	public void setScaption(String scaption) {
		this.scaption = scaption;
	}

	/**
	 * @return the bcaption
	 */
	public String getBcaption() {
		return bcaption;
	}

	/**
	 * @param bcaption to set
	 */
	public void setBcaption(String bcaption) {
		this.bcaption = bcaption;
	}
}
