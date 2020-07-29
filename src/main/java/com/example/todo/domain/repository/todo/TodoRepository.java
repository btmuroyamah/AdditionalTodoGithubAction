package com.example.todo.domain.repository.todo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.todo.domain.model.Todo;

public interface TodoRepository {
	Optional<Todo> findById(String todoId);

	List<Todo> findAll();

	List<Todo> findByLimit(LocalDate start, LocalDate end);

	void create(Todo todo);

	boolean updateById(Todo todo);

	void deleteById(Todo todo);

	long countByFinished(boolean finished);
}