package com.esen.book.api.entity;

import java.io.Serializable;

import com.esen.eorm.entity.RootEntity;

public class BookInfoEntity extends RootEntity implements Serializable {
		private static final long serialVersionUID = 1891369344854226667L;
		/**
		 * 唯一字段
		 */
		private String id;
		/**
		 * 小类唯一字段
		 */
		private String tid;
		/**
		 * 书籍名称
		 */
		private String caption;
		/**
		 * 描述
		 */
		private String desc;

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

		public String getTid() {
				return tid;
		}

		public void setTid(String tid) {
				this.tid = tid;
		}

		@Override
		public String getCaption() {
				return caption;
		}

		@Override
		public void setCaption(String caption) {
				this.caption = caption;
		}

		@Override
		public String getDesc() {
				return desc;
		}

		@Override
		public void setDesc(String desc) {
				this.desc = desc;
		}
}
