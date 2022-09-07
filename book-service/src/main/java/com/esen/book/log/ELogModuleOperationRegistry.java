package com.esen.book.log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.esen.elog.api.oper.Operation;
import com.esen.elog.api.oper.OperationModule;
import com.esen.elog.api.oper.SystemModuleOperationRegistry;

/**
 * 日志管理模块的操作注册服务
 *
 * @author liuaj
 * @since 2022/9/5
 */
public class ELogModuleOperationRegistry extends SystemModuleOperationRegistry {
	/**
	 * 获取操作的集合进行注册
	 *
	 * @return
	 */
	@Override
	public List<Operation> getAllOperations() {
		Operation[] logOperations = ELogOperation.values();
		return Arrays.asList(logOperations);
	}

	/**
	 * 获取默认不记录日志的操作id集合
	 *
	 * @return
	 */
	@Override
	public Set<String> getFilterOperIds() {
		Set<String> set = new HashSet<String>();
		return set;
	}

	/**
	 * 日志模块的所有操作对象
	 */
	public enum ELogOperation implements Operation {

		TYPE_VIEW("EBOK0000VE","查看小类列表","com.esen.book.log.elogmoduleoperationregistry.viewtype"),
		CATEGORY_VIEW("EBOK0100VE","查看大类列表","com.esen.book.log.elogmoduleoperationregistry.viewcategory"),
		BOOK_VIEW("EBOK0200VE","查看图书列表","com.esen.book.log.elogmoduleoperationregistry.viewbook"),
		BOOK_ADD("EBOK0201NE","添加书籍","com.esen.book.log.elogmoduleoperationregistry.addbook"),
		BOOK_EDIT("EBOK0202MD","编辑书籍","com.esen.book.log.elogmoduleoperationregistry.editbook");

		private static final OperationModule module = new OperationModule("EBOK000000", "书籍管理", "com.esen.book.log.elogmoduleoperationregistry.ebokmanager");

		private String operId;
		private String defaultCaption;
		private String captionKey;

		/**
		 * @param operId         操作id
		 * @param defaultCaption 默认的操作标题
		 * @param captionKey     操作标题对应的国际化key
		 */
		private ELogOperation(String operId, String defaultCaption, String captionKey) {
			this.operId = operId;
			this.defaultCaption = defaultCaption;
			this.captionKey = captionKey;
		}

		@Override
		public String getOperId() {
			return operId;
		}

		@Override
		public String getDefaultCaption() {
			return defaultCaption;
		}

		@Override
		public String getCaptionKey() {
			return captionKey;
		}

		@Override
		public OperationModule getModule() {
			return module;
		}
	}
}