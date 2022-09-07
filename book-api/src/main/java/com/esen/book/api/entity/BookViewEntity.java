package com.esen.book.api.entity;

import com.esen.eorm.entity.RootEntity;

import java.io.Serializable;

/**
 * 图书的实体类.
 *
 * @author liuaj
 * @since 20220824
 */
public class BookViewEntity extends RootEntity implements Serializable {
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the scaption
	 */
	public String getScaption() {
		return scaption;
	}

	/**
	 * @param scaption to set
	 */
	public void setScaption(String scaption) {
		this.scaption = scaption;
	}

	/**
	 * @return the bcaption
	 */
	public String getBcaption() {
		return bcaption;
	}

	/**
	 * @param bcaption to set
	 */
	public void setBcaption(String bcaption) {
		this.bcaption = bcaption;
	}
}
