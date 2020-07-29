package com.example.todo.domain.service.todo;


import java.time.LocalDate;
import java.util.List;

import com.example.todo.domain.model.Todo;

public interface TodoService {

	Todo findOne(String todoId);

	List<Todo> findByLimit(LocalDate start, LocalDate end);

	List<Todo> findAll();

	Todo create(Todo todo);

	Todo finish(String todoId);

	void delete(String todoId);
}