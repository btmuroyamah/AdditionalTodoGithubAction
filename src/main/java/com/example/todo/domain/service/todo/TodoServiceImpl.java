package com.example.todo.domain.service.todo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessages;

import com.example.todo.domain.model.Todo;
import com.example.todo.domain.repository.todo.TodoRepository;

import lombok.Data;

@Service
@Transactional
@Data
public class TodoServiceImpl implements TodoService {

	private static final long MAX_UNFINISHED_COUNT = 5;

	@Inject
	TodoRepository todoRepository;

	@Override
	@Transactional(readOnly = true)
	public Todo findOne(String todoId) {
		Todo todo = todoRepository.findById(todoId).orElse(null);
		if (todo == null) {
			ResultMessages messages = ResultMessages.error();
			messages.add("E404", todoId);
			throw new ResourceNotFoundException(messages);
		}
		return todo;
		
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Todo> findAll() {

		// TODO Todoの一覧を取得する
		// Collection<Todo> todoAll = todoRepository.findAll();

		// TODO 取得したTodo一覧を優先順位順にソートする

//		String priorityOrder [] = {"High","Middle","Low"};
//
//		for(int i = 0; i < todoAll.size(); i++) {
//			if ( todo.getPriority().matches(priorityOrder[0])) {
//				Collections.sort(todoAll);
//				
//			} else if (todo.getPriority().matches(priorityOrder[1])) {
//				
//			} else {
//				
//			}
//		}

		// return 優先順にソートしたTodoの一覧

		Collection<Todo> todos = todoRepository.findAll();

		Collections.sort(todos, Comparator.comparing(Todo::getPriority));

		return todoRepository.findAll();

	}
	
	public enum Priority {

		High(1), Middle(2), Low(3);

		private int id;

		private Priority(int id) {
			this.id = id;
		}
	}

	@Override
	public Todo create(Todo todo) {
		long unfinishedCount = todoRepository.countByFinished(false);
		if (unfinishedCount >= MAX_UNFINISHED_COUNT) {
			ResultMessages messages = ResultMessages.error();
			messages.add("E001", MAX_UNFINISHED_COUNT);
			throw new BusinessException(messages);
		}

		String todoId = UUID.randomUUID().toString();
		Date createdAt = new Date();

		todo.setTodoId(todoId);
		todo.setCreatedAt(createdAt);
		todo.setFinished(false);

		todoRepository.create(todo);
		/*
		 * REMOVE THIS LINE IF YOU USE JPA todoRepository.save(todo); REMOVE THIS LINE
		 * IF YOU USE JPA
		 */

		return todo;
	}

	@Override
	public Todo finish(String todoId) {
		Todo todo = findOne(todoId);
		if (todo.isFinished()) {
			ResultMessages messages = ResultMessages.error();
			messages.add("E002", todoId);
			throw new BusinessException(messages);
		}
		todo.setFinished(true);
		todoRepository.updateById(todo);
		/*
		 * REMOVE THIS LINE IF YOU USE JPA todoRepository.save(todo); REMOVE THIS LINE
		 * IF YOU USE JPA
		 */
		return todo;
	}

	@Override
	public void delete(String todoId) {
		Todo todo = findOne(todoId);
		todoRepository.deleteById(todo);
	}
}