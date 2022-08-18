package com.esen.book.web.service;

import com.esen.book.api.repository.BookHistoryRepository;
import com.esen.eorm.annotation.ApplicationService;
import com.esen.eorm.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationService
public class BookHistoryService extends AbstractService {
		@Autowired
		private BookHistoryRepository bookHistoryRepository;

		public int getTotalCountByPerson(String person){
				return bookHistoryRepository.getTotalCountByPerson(person);
		}
}
