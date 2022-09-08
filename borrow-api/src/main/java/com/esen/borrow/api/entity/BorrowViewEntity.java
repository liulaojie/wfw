package com.esen.borrow.api.entity;


import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 借阅信息的实体类
 *
 * @author liuaj
 * @since 20220824
 */
public class BorrowViewEntity extends BookHistoryEntity implements Serializable {
		/**
		 * 序列号
		 */
		private static final long serialVersionUID = -935089358231842005L;
		/**
		 * 书籍名
		 */
		private String book;
		/**
		 * 书籍小类
		 */
		private String scaption;
		/**
		 * 书籍大类
		 */
		private String bcaption;
		/**
		 * 小类ID
		 */
		private String tid;
		/**
		 * 大类ID
		 */
		private String cid;

	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
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

	/**
	 * @return the tid
	 */
	public String getTid() {
		return tid;
	}

	/**
	 * @param tid to set
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}

	/**
	 * @return the cid
	 */
	public String getCid() {
		return cid;
	}

	/**
	 * @param cid to set
	 */
	public void setCid(String cid) {
		this.cid = cid;
	}
}
