package com.esen.book.api.entity;

import com.esen.eorm.entity.RootEntity;

import java.io.Serializable;
import java.sql.Timestamp;

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
