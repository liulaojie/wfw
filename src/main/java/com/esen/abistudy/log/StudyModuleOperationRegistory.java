package com.esen.abistudy.log;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.esen.ecore.log.Operation;
import com.esen.ecore.log.extend.OperationModule;
import com.esen.ecore.log.extend.SystemModuleOperationRegistry;
import com.esen.util.i18n.I18N;

/**
 * 日志模块
 * 
 * @author admin
 * @since 2020年6月20日
 */
@Component
public class StudyModuleOperationRegistory implements SystemModuleOperationRegistry {
	
	@Override
	public List<Operation> getAllOperations() {
		Operation[] logOperations = StudyLogOperation.values();
		return Arrays.asList(logOperations);
	}

	@Override
	public Set<String> getFilterOperIds() {
		return null;
	}
	
	public enum StudyLogOperation implements Operation {
		
		OP_IMPPT("ESTU0601IM", "导入", "com.esen.abistudy.log.studymoduleoperationregistory.import"),
		OP_EXPPT("ESTU0601EX", "导出", "com.esen.abistudy.log.studymoduleoperationregistory.export");

		private static final OperationModule module = new OperationModule("STU000000", "新人学习",
				"com.esen.abistudy.log.studymoduleoperationregistory.module");

		private String operId;

		private String defaultCaption;

		private String captionKey;

		/**
		 * @param operId 操作id
		 * @param defaultCaption 默认的操作标题
		 * @param captionKey 操作标题对应的国际化key
		 */
		private StudyLogOperation(String operId, String defaultCaption, String captionKey) {
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
			return I18N.getString(captionKey, defaultCaption, I18N.getDefaultLocale(), null);
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