package com.example.todo.api.todo;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.todo.domain.model.Todo;
import com.example.todo.domain.service.todo.TodoService;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

public class TodoRestControllerTest {
	
	@Rule
	public MockitoRule mokito = MockitoJUnit.rule();

	@InjectMocks
	TodoRestController target;
	
	MockMvc mockMvc;
	
	@Mock
	TodoService todoService;
	
	@Before
	public void setUp() throws Exception {
		 mockMvc = MockMvcBuilders.standaloneSetup(target).alwaysDo(log()).build();
		 //mapperの設定を追加
		 Mapper mapper = DozerBeanMapperBuilder.buildDefault();
		 ReflectionTestUtils.setField(target, "beanMapper", mapper);
	}
	
	@Test
	public void testgetTodosByLimit_1() throws Exception {
		
		//検証するデータを登録
		Collection<Todo> todos = new ArrayList<Todo>();
		Todo todo = new Todo("1", "title1", false, LocalDate.of(2020, 8, 1));
		todos.add(todo);
		when(todoService.findByLimit(Mockito.any(), Mockito.any())).thenReturn(todos);
		
		mockMvc.perform(
				get("/todos")
				.param("start", "2020-08-01")
				.param("end", "2020-12-31")
				)
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"))
		.andExpect(content().json("[{\"todoId\": \"1\",\"todoTitle\": \"title1\",\"finished\": false,\"createdAt\": null,\"deadLine\": \"2020-08-01\"}]"));
		}
	}
	
