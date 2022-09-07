package com.esen.book.api.entity;

import java.io.Serializable;

import com.esen.eorm.entity.RootEntity;

/**
 * 图书信息图书信息的实体类.
 *
 * @author liuaj
 * @since 20220824
 */
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

	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * @return the id
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * @param id to set
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the tid
	 */
	public String getTid() {
		return tid;
	}

	/**
	 * @param tid to set
	 */
	public void setTid(String tid) {
		this.tid = tid;
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
	 * @return the desc
	 */
	@Override
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc to set
	 */
	@Override
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * 重写toString用于记录到日志中
	 * @return
	 */
	@Override
	public String toString() {
		return "BookInfoEntity{" + "id='" + id + '\'' + ", tid='" + tid + '\'' + ", caption='" + caption + '\'' + ", desc='"
				+ desc + '\'' + '}';
	}
}
