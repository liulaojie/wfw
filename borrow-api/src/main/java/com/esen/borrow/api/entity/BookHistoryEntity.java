package com.esen.borrow.api.entity;

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
		 * 归还时间
		 */
		private Timestamp todate;
}
