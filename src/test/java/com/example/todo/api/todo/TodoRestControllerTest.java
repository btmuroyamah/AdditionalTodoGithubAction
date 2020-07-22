package com.example.todo.api.todo;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.todo.domain.model.Todo;
import com.example.todo.domain.service.todo.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration({
    "classpath:META-INF/spring/applicationContext.xml"}),
    @ContextConfiguration("classpath:META-INF/spring/spring-mvc-rest.xml") })
@WebAppConfiguration
public class TodoRestControllerTest {

	@Inject
	WebApplicationContext webApplicationContext;
	
	MockMvc mockMvc;
	
	@Rule
	public MockitoRule mokito = MockitoJUnit.rule();
	
	ObjectMapper mapper;
	
	@Mock
	TodoService todoService;
	
	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).alwaysDo(log()).build();
		mapper = new ObjectMapper();
	}
	@Test
	public void testgetTodosByLimit_1() {
		LocalDate start = LocalDate.of(2020, 8, 1);
		LocalDate end = LocalDate.of(2020, 12, 31);
		Collection<Todo> todos = new ArrayList<Todo>();
		todos.add(new Todo("1", "title1", false, LocalDate.of(2020, 8, 1)));
		//todos.add(new Todo("2", "title2", false, LocalDate.of(2020, 2, 2)));
		when(todoService.findByLimit(start, end)).thenReturn(todos);
		
		
		try {
			mockMvc.perform(
					get("/todos")
					.param("start", "2020-08-01")
					.param("end", "2020-12-31")
					)
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(jsonPath("$.todoId").value("1"))
			.andExpect(jsonPath("$.todoTitle").value("title1"))
			.andExpect(jsonPath("$.finished").value(false))
			.andExpect(jsonPath("$.deadLine").value(LocalDate.of(2020, 8, 1)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
//	@Test
//	  public void postTodoTest() throws Exception {
//	    String title = "title";
//	    TodoResource todoRequest = new TodoResource();
//	    todoRequest.setTodoTitle(title);
//	    MvcResult result =
//	        mockMvc
//	            .perform(MockMvcRequestBuilders.get("/todos")
//	            	.param("start", "2020-01-01")
//					.param("end", "2020-12-31")
//	                .content(mapper.writeValueAsString(todoRequest))
//	                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//	            .andExpect(status().isCreated()).andReturn();
//
//	    TodoResource todoResponce =
//	        mapper.readValue(result.getResponse().getContentAsString(), TodoResource.class);
//	    assertThat(todoResponce.getTodoId(), notNullValue());
//	    assertThat(todoResponce.getTodoTitle(), equalTo(title));
//	    assertThat(todoResponce.isFinished(), equalTo(false));
//	    assertThat(todoResponce.getCreatedAt(), notNullValue());
//	  }

}
