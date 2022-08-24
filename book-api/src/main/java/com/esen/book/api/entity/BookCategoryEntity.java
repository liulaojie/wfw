package com.esen.book.api.entity;

import com.esen.eorm.entity.RootEntity;

import java.io.Serializable;

/**
 * 图书分类的实体类 .
 *
 * @author liuaj
 * @since 20220824
 */
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

	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * @return the caption
	 */
	@Override
	public String getCaption() {
		return caption;
	}

	/**
	 * @param caption to set
	 */
	@Override
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * @return the id
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * @param  id to set
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}
}
