package com.esen.book.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.esen.book.api.repository.BookHistoryRepository;
import com.esen.eorm.annotation.ApplicationService;
import com.esen.eorm.service.AbstractService;

@ApplicationService
public class BookHistoryService extends AbstractService {
		@Autowired
		private BookHistoryRepository bookHistoryRepository;

		public int getTotalCountByPerson(){
				return bookHistoryRepository.getTotalCount();
		}
}
