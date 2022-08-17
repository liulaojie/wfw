package com.esen.borrow.api.entity;


import com.esen.eorm.entity.IdEntityImpl;
import com.esen.eorm.entity.RootEntity;

import java.sql.Timestamp;


public class BorrowEntity extends RootEntity {
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
		 * 书籍的ID
		 */
		private String bid;
		/**
		 * 借阅时间
		 */
		private Timestamp fromdate;
		/**
		 * 归还时间
		 */
		private Timestamp todate;

		public String getId() {
				return id;
		}

		public void setId(String id) {
				this.id = id;
		}

		public String getPerson() {
				return person;
		}

		public void setPerson(String person) {
				this.person = person;
		}

		public String getBid() {
				return bid;
		}

		public void setBid(String bid) {
				this.bid = bid;
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
}
