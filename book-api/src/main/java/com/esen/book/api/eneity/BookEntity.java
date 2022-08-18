package com.esen.book.api.eneity;

import com.esen.eorm.entity.RootEntity;

import java.io.Serializable;

public class BookEntity extends RootEntity implements Serializable {
		/**
		 * 序列号
		 */
		private static final long serialVersionUID = 8500921607578266851L;
		/**
		 * 书籍ID
		 */
		private String id;
		/**
		 * 书籍名
		 */
		private String name;
		/**
		 * 书籍描述
		 */
		private String desc;
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

		public String getName() {
				return name;
		}

		public void setName(String name) {
				this.name = name;
		}

		@Override
		public String getDesc() {
				return desc;
		}

		@Override
		public void setDesc(String desc) {
				this.desc = desc;
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
