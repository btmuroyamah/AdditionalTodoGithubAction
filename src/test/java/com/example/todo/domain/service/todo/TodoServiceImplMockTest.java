package com.example.todo.domain.service.todo;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;

import com.example.todo.domain.repository.todo.TodoRepository;

public class TodoServiceImplMockTest {
	
	@Mock
	TodoRepository mockTodoRepository;
	
	@InjectMocks
	private TodoServiceImpl target;
	
	@Rule
	public MockitoRule mockito = MockitoJUnit.rule();
	

}
