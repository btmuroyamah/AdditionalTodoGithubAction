package com.example.todo.api.todo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.todo.domain.service.todo.TodoService;
import com.github.dozermapper.core.Mapper;

@ContextConfiguration(locations = {
        "classpath:META-INF/spring/applicationContext.xml",
        "classpath:META-INF/spring/test-context.xml",
        "classpath:META-INF/spring/spring-mvc-rest.xml"})
public class TodoRestControllerTest {
	
	@Mock
	TodoService todoService;
	
	@Mock
	TodoResource todoResource;
	
	@InjectMocks
	TodoRestController target;
	
	@Inject
	Mapper beanMapper;

	MockMvc mockMvc;
	
	@Before
    public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(target).alwaysDo(log()).build();
    }
	
	@Test
	public void getMappingのテスト() throws Exception{
		mockMvc.perform(get("/api/v1/todos"))
			.andExpect(status().isOk())
			.andReturn();
		
		
	}

}
