package com.esen.borrow.web.service;

import com.esen.borrow.api.Repository.BorrowRepository;
import com.esen.borrow.api.entity.BorrowEntity;
import com.esen.ejdbc.jdbc.ConnectFactoryManager;
import com.esen.ejdbc.params.PageRequest;
import com.esen.ejdbc.params.PageResult;
import com.esen.eorm.annotation.ApplicationService;
import com.esen.eorm.service.AbstractService;
import com.esen.eutil.util.exp.Expression;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 借阅的SERVICE层
 *
 * @author liuaj
 * @since 20220816
 */
@ApplicationService
public class BorrowService  extends AbstractService<BorrowEntity> {

		@Autowired
		protected ConnectFactoryManager connectFactoryManager;
		@Autowired
		protected BorrowRepository borrowRepository;
		/**
		 * person的所有借阅记录数
		 * @param person 借阅人的姓名
		 * @return 借阅的总数
		 */
		public int getTotalCountByPerson(String person) {
			return borrowRepository.getTotalCountByPerson(person);
		}
		/**
		 * person的所有借阅记录数
		 * @param person 借阅人的姓名
		 * @return 借阅记录
		 */
		public List<BorrowEntity> listBorrowsByperson(String person, PageRequest page){
				List<String> list = new ArrayList<String>();
				list.add(person);
				Expression expression = new Expression("person=?");
				PageResult<BorrowEntity> result =
								borrowRepository.findAll(page,expression,list.toArray(new String[list.size()]));
				return result.list();
		}

}
