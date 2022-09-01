package com.esen.borrow.api.entity;


import java.io.Serializable;
import java.sql.Timestamp;

import com.esen.eorm.entity.RootEntity;

/**
 * 借阅信息的实体类
 *
 * @author liuaj
 * @since 20220824
 */
public class AnalyzeEntity  {
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
	 * @return the id
	 */

	public String getId() {
		return id;
	}

	/**
	 * @param id to set
	 */

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

	public AnalyzeEntity() {
	}

	public AnalyzeEntity(String id, String person, String book, Timestamp fromdate, Timestamp todate, String scaption, String bcaption) {
		this.id = id;
		this.person = person;
		this.book = book;
		this.fromdate = fromdate;
		this.todate = todate;
		this.scaption = scaption;
		this.bcaption = bcaption;
	}
}
