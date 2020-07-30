package com.example.todo.domain.service.todo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.date.ClassicDateFactory;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessages;

import com.example.todo.domain.model.Priority;
import com.example.todo.domain.model.Todo;
import com.example.todo.domain.repository.todo.TodoRepository;

import lombok.Data;

@Service
@Transactional
@Data
public class TodoServiceImpl implements TodoService {

	@Inject
	ClassicDateFactory dateFactory;

	@Inject
	TodoRepository todoRepository;
	
	private static final long MAX_UNFINISHED_COUNT = 50;

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
	
	public List<Todo> findAll() {
		return todoRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Todo> findByLimit(LocalDate start, LocalDate end) {

		List<Todo> todos = todoRepository.findByLimit(start, end);

		// Comparatorインターフェースを匿名クラスで使用して優先度でソート
		Collections.sort(todos, new Comparator<Todo>() {
			public int compare(Todo e1, Todo e2) {
				// TodoクラスのPriorityを取り出し、Enumの中のIdを取り出して比較する
				return Integer.compare(e1.getPriority().getId(), e2.getPriority().getId());
			}
		});

		// 各優先度のtodoをList<Todo> todosHogeHogeに入れる
		List<Todo> todosHigh = todos.stream()
				.filter(h -> h.getPriority() == Priority.High)
				.sorted(Comparator.comparing(Todo::getCreatedAt).reversed())
				.collect(Collectors.toList());

		List<Todo> todosMiddle = todos.stream()
				.filter(m -> m.getPriority() == Priority.Middle)
				.sorted(Comparator.comparing(Todo::getCreatedAt).reversed())
				.collect(Collectors.toList());

		List<Todo> todosLow = todos.stream()
				.filter(l -> l.getPriority() == Priority.Low)
				.sorted(Comparator.comparing(Todo::getCreatedAt).reversed())
				.collect(Collectors.toList());

		List<Todo> sortedTodos = new ArrayList<>();

		sortedTodos.addAll(todosHigh);
		sortedTodos.addAll(todosMiddle);
		sortedTodos.addAll(todosLow);

		return sortedTodos;
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
		LocalDateTime createdAt = LocalDateTime.now();

		todo.setTodoId(todoId);
		todo.setCreatedAt(createdAt);
		todo.setFinished(false);

		if (todo.getPriority() == null){
			todo.setPriority(Priority.Low);
		}

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