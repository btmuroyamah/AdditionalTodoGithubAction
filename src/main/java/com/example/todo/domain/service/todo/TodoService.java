package com.example.todo.domain.service.todo;


import java.time.LocalDate;
import java.util.Collection;



import com.example.todo.domain.model.Todo;

public interface TodoService {
	
	Todo findOne(String todoId);
	
	Collection<Todo> findByLimit(LocalDate start, LocalDate end);
	
    Collection<Todo> findAll();

    Todo create(Todo todo);

    Todo finish(String todoId);

    void delete(String todoId);
}