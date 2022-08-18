package com.esen.book.api.entity;

import com.esen.eorm.entity.RootEntity;

import java.io.Serializable;

public class BookCategoryEntity  extends RootEntity implements Serializable {
		private static final long serialVersionUID = -1828049103087540393L;
		/**
		 * 唯一字段
		 */
		private String id;
		/**
		 * 大类名称
		 */
		private String caption;

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

		@Override
		public String getCaption() {
				return caption;
		}

		@Override
		public void setCaption(String caption) {
				this.caption = caption;
		}
}
