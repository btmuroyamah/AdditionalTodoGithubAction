package com.example.todo.api.todo;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.todo.domain.model.Priority;
import com.example.todo.domain.model.Todo;
import com.example.todo.domain.service.todo.TodoService;

public class TodoRestControllerTest {

	@Rule
	public MockitoRule mockito = MockitoJUnit.rule();

	@InjectMocks
	TodoRestController target;

	@Mock
	TodoService todoService;

	MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(target).alwaysDo(log()).build();
	}

	@Test
	public void getMappingのテスト() throws Exception {
		//設定
		List<Todo> mockList = new ArrayList<>();
		
		Todo high = new Todo();
		high.setTodoId("1");
		high.setTodoTitle("high");
		high.setFinished(false);
		high.setCreatedAt(LocalDate.of(2020, 7, 1));
		high.setDeadLine(null);
		high.setPriority(Priority.High);
		
		mockList.add(high);
		
		when(todoService.findAll()).thenReturn(mockList);
		
		//実行
		mockMvc.perform(
					get("/api/v1/todos")
					.accept(MediaType.APPLICATION_JSON)
				)
		
				//	ステータス、json、コンテントタイプをチェックする=アサーション
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(content().json("[{\"todoId\": \"1\",\"todoTitle\": \"high\",\"finished\": false,"
						+ "\"createdAt\": LocalDate.of(2020, 7, 1) ,\"deadLine\": null,\"priority\": \"High\"}]"));
	}

}
