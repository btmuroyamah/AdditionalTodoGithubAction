package com.example.todo.domain.service.todo;

import java.util.List;

import com.example.todo.domain.model.Todo;

public interface TodoService {

	Todo findOne(String todoId);

	List<Todo> findAll();

	Todo create(Todo todo);

	Todo finish(String todoId);

	void delete(String todoId);
}