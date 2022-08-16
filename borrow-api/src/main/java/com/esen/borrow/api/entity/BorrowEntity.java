package com.esen.borrow.api.entity;


import com.esen.eorm.entity.IdEntityImpl;

import java.util.Calendar;

public class BorrowEntity extends IdEntityImpl  {
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
		private Calendar fromdate;
		/**
		 * 归还时间
		 */
		private Calendar todate;

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

		public Calendar getFromdate() {
				return fromdate;
		}

		public void setFromdate(Calendar fromdate) {
				this.fromdate = fromdate;
		}

		public Calendar getTodate() {
				return todate;
		}

		public void setTodate(Calendar todate) {
				this.todate = todate;
		}
}
