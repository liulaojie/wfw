package com.esen.book.api.entity;

import java.io.Serializable;

import com.esen.eorm.entity.RootEntity;

public class BookTypeEntity extends RootEntity implements Serializable {
		private static final long serialVersionUID = 3705905178278362500L;
		/**
		 * 大类唯一字段
		 */
		private String cid;
		/**
		 * 唯一字段
		 */
		private String id;
		/**
		 * 小类名称
		 */
		private String caption;

		public static long getSerialVersionUID() {
				return serialVersionUID;
		}

		public String getCid() {
				return cid;
		}

		public void setCid(String cid) {
				this.cid = cid;
		}

		@Override
		public String getId() {
				return id;
		}

		@Override
		public void setId(String id) {
				this.id = id;
		}

		@Override
		public String getCaption() {
				return caption;
		}

		@Override
		public void setCaption(String caption) {
				this.caption = caption;
		}
}
