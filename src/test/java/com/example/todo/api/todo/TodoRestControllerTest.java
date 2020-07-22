package com.example.todo.api.todo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.todo.domain.service.todo.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ContextConfiguration(locations = {
        "classpath:META-INF/spring/applicationContext.xml",
        "classpath:META-INF/spring/test-context.xml",
        "classpath:META-INF/spring/spring-mvc-rest.xml"})
@WebAppConfiguration
public class TodoRestControllerTest {
	
	@Rule
	public MockitoRule mockito = MockitoJUnit.rule();
	
	@Mock
	TodoService todoService;
	
	@Mock
	TodoResource todoResource;
	
	@InjectMocks
	TodoRestController target;
	
	ObjectMapper mapper;
	
	MockMvc mockMvc;
	
	@Before
    public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(target).alwaysDo(log()).build();
    }
	
	@Test
	public void getMappingのテスト() throws Exception{
		mockMvc.perform(get("/api/v1/todos")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.links[?(@.rel == 'self')].href")
			.value("http://localhost:8080/todo/api/v1/todos"))
			.andExpect(status().isOk())
			.andReturn();
		
		
	}

}
