package com.esen.borrow.api.entity;


import com.esen.eorm.entity.RootEntity;

import java.io.Serializable;
import java.sql.Timestamp;


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

		public static long getSerialVersionUID() {
				return serialVersionUID;
		}

		@Override
		public String getId() {
				return id;
		}

		@Override
		public void setId(String id) {
				this.id = id;
		}

		public String getPerson() {
				return person;
		}

		public void setPerson(String person) {
				this.person = person;
		}

		public String getBook() {
				return book;
		}

		public void setBook(String book) {
				this.book = book;
		}

		public Timestamp getFromdate() {
				return fromdate;
		}

		public void setFromdate(Timestamp fromdate) {
				this.fromdate = fromdate;
		}

		public Timestamp getTodate() {
				return todate;
		}

		public void setTodate(Timestamp todate) {
				this.todate = todate;
		}

		public String getScaption() {
				return scaption;
		}

		public void setScaption(String scaption) {
				this.scaption = scaption;
		}

		public String getBcaption() {
				return bcaption;
		}

		public void setBcaption(String bcaption) {
				this.bcaption = bcaption;
		}
}
