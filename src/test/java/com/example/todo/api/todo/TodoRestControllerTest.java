package com.example.todo.api.todo;

import static org.mockito.Mockito.mockingDetails;
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
import org.mockito.Mockito;
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
	public void testgetTodosByLimit_start20200801でend20201231の場合() throws Exception {
		
		//検証するデータを登録
		List<Todo> todos = new ArrayList<Todo>();
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
	
	@Test
	public void testgetTodosByLimit_startに不正な値が来た場合() throws Exception {
		
		//検証するデータを登録
		List<Todo> todos = new ArrayList<Todo>();
		Todo todo = new Todo("1", "title1", false, LocalDate.of(2020, 8, 1));
		todos.add(todo);
		when(todoService.findByLimit(Mockito.any(), Mockito.any())).thenReturn(todos);
			mockMvc.perform(
					get("/todos")
					.param("start", "日付")
					.param("end", "2020-12-31")
					)
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testgetTodosByLimit_endに不正な値が来た場合() throws Exception {
		
		//検証するデータを登録
		List<Todo> todos = new ArrayList<Todo>();
		Todo todo = new Todo("1", "title1", false, LocalDate.of(2020, 8, 1));
		todos.add(todo);
		when(todoService.findByLimit(Mockito.any(), Mockito.any())).thenReturn(todos);
			mockMvc.perform(
					get("/todos")
					.param("start", "2020-08-01")
					.param("end", "日付")
					)
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testgetTodosByLimit_startとendに不正な値が来た場合() throws Exception {
		
		//検証するデータを登録
		List<Todo> todos = new ArrayList<Todo>();
		Todo todo = new Todo("1", "title1", false, LocalDate.of(2020, 8, 1));
		todos.add(todo);
		when(todoService.findByLimit(Mockito.any(), Mockito.any())).thenReturn(todos);
			mockMvc.perform(
					get("/todos")
					.param("start", "日付")
					.param("end", "日付")
					)
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testgetTodosByLimit_startがnullでendもnullの場合() throws Exception {
		
		//検証するデータを登録
		List<Todo> todos = new ArrayList<Todo>();
		Todo todo = new Todo("1", "title1", false, LocalDate.of(2020, 8, 1));
		todos.add(todo);
		when(todoService.findByLimit(Mockito.any(), Mockito.any())).thenReturn(todos);
		
		mockMvc.perform(get("/todos"))
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"))
		.andExpect(content().json("[{\"todoId\": \"1\",\"todoTitle\": \"title1\",\"finished\": false,\"createdAt\": null,\"deadLine\": \"2020-08-01\"}]"));
	}
	
	@Test
	public void testgetTodosByLimit_startがnullでend20201231の場合() throws Exception {
		
		//検証するデータを登録
		List<Todo> todos = new ArrayList<Todo>();
		Todo todo = new Todo("1", "title1", false, LocalDate.of(2020, 8, 1));
		todos.add(todo);
		when(todoService.findByLimit(Mockito.any(), Mockito.any())).thenReturn(todos);
		
		mockMvc.perform(get("/todos")
				.param("end", "2020-12-31")
				)
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"))
		.andExpect(content().json("[{\"todoId\": \"1\",\"todoTitle\": \"title1\",\"finished\": false,\"createdAt\": null,\"deadLine\": \"2020-08-01\"}]"));
	}
	
	@Test
	public void testgetTodosByLimit_startが20200801でendがnullの場合() throws Exception {
		
		//検証するデータを登録
		List<Todo> todos = new ArrayList<Todo>();
		Todo todo = new Todo("1", "title1", false, LocalDate.of(2020, 8, 1));
		todos.add(todo);
		when(todoService.findByLimit(Mockito.any(), Mockito.any())).thenReturn(todos);
		
		mockMvc.perform(get("/todos")
				.param("start", "2020-08-01")
				)
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"))
		.andExpect(content().json("[{\"todoId\": \"1\",\"todoTitle\": \"title1\",\"finished\": false,\"createdAt\": null,\"deadLine\": \"2020-08-01\"}]"));
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

		when(todoService.findByLimit(Mockito.any(), Mockito.any())).thenReturn(mockList);

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
