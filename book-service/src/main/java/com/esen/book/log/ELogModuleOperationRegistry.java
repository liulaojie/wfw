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
		set.add(ELogOperation.LOG_VIEW.getOperId());
		return set;
	}

	/**
	 * 日志模块的所有操作对象
	 */
	public enum ELogOperation implements Operation {

		LOG_VIEW("ELOG0100VE", "查看日志", "com.esen.book.log.elogmoduleoperationregistry.viewlog"),
		LOG_SEARCH("ELOG10100SE", "搜索日志", "com.esen.book.log.elogmoduleoperationregistry.searchlog"),
		LOG_EXPORT("ELOG0100EX", "导出日志", "com.esen.book.log.elogmoduleoperationregistry.exportlog"),
		LOG_VIEWCONFIG("ELOG0200VE", "日志配置查看", "com.esen.book.log.elogmoduleoperationregistry.viewlogconf"),
		LOG_EDITCONFIG("ELOG0200MD", "日志配置编辑", "com.esen.book.log.elogmoduleoperationregistry.editlogconf"),
		LOG_ARCHIVE("ELOG0100ZZ", "日志归档", "com.esen.book.log.elogmoduleoperationregistry.archivelog"),
		LOG_CLEAN("ELOG0100CR", "日志清理", "com.esen.book.log.elogmoduleoperationregistry.cleanlog"),
		TYPE_VIEW("EDSO0200VE","查看小类列表","com.esen.book.log.elogmoduleoperationregistry.viewtype"),
		CATEGORY_VIEW("EDSO0201VE","查看大类列表","com.esen.book.log.elogmoduleoperationregistry.viewcategory"),
		BOOK_VIEW("EDSO0202VE","查看图书列表","com.esen.book.log.elogmoduleoperationregistry.viewbook"),
		BOOK_ADD("EDSO0203NE","添加书籍","com.esen.book.log.elogmoduleoperationregistry.addbook"),
		BOOK_EDIT("EDSO0104MD","编辑书籍","com.esen.book.log.elogmoduleoperationregistry.editbook"),
		LOG_SLF4J_SAVE("ELOG0300MD", "保存slf4j日志设置", "com.esen.book.log.elogmoduleoperationregistry.saveslf4j"),
		LOG_SLF4J_VIEW("ELOG0300VE", "查看slf4j日志设置", "com.esen.book.log.elogmoduleoperationregistry.viewslf4j");

		private static final OperationModule module = new OperationModule("ELOG000000", "日志管理", "com.esen.book.log.elogmoduleoperationregistry.elogmanager");

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