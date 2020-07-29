package com.example.todo.api.todo;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.todo.domain.model.Priority;
import com.example.todo.domain.model.Todo;
import com.example.todo.domain.service.todo.TodoService;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

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
		Mapper mapper = DozerBeanMapperBuilder.buildDefault();
		ReflectionTestUtils.setField(target, "beanMapper", mapper);
	}

	@Test
	public void testGetTodos_Request200() throws Exception {
		// 設定
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

		// 実行
		mockMvc.perform(get("/todos").accept(MediaType.APPLICATION_JSON))

				// ステータス、json、コンテントタイプをチェックする=アサーション
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(content().json("[{\"todoId\": \"1\",\"todoTitle\": \"high\",\"finished\": false,"
						+ "\"createdAt\": 2020-07-01 ,\"deadLine\": null,\"priority\": \"High\"}]"));
	}
	
	@Test
	public void testPostTodos_Request415() throws Exception{
		
		//設定
		List<Todo> mockList = new ArrayList<>();
		
		Todo fakePriority = new Todo();
		fakePriority.setTodoId("1");
		fakePriority.setTodoTitle("fake");
		fakePriority.setFinished(false);
		fakePriority.setCreatedAt(LocalDate.of(2020, 7, 1));
		fakePriority.setDeadLine(null);
		fakePriority.setPriority(null);
		
		mockList.add(fakePriority);
		
		when(todoService.findAll()).thenReturn(mockList);
		
		//実行
		mockMvc.perform(post("/todos").content("{\"todoTitle\": \"５\",\"priority\":\"ぽんぽん\"}").accept(MediaType.APPLICATION_JSON))
		
				//ステータス、json、コンテントタイプのチェックをする=アサーション
				.andExpect(status().isUnsupportedMediaType());
				
	}

}
