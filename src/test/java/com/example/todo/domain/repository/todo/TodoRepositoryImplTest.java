package com.example.todo.domain.repository.todo;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.example.todo.domain.model.Todo;

public class TodoRepositoryImplTest {
	
	@InjectMocks
	TodoRepositoryImpl target;
	
	@Rule
	public MockitoRule mockito = MockitoJUnit.rule();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
//		TodoRepositoryImpl.TODO_MAP.clear();
		TodoRepositoryImpl.TODO_MAP.put("1", new Todo("1", "title1", true, LocalDate.of(2020, 1, 1)));
		TodoRepositoryImpl.TODO_MAP.put("2", new Todo("2", "title2", false, LocalDate.of(2020, 2, 2)));
		TodoRepositoryImpl.TODO_MAP.put("3", new Todo("3", "title3", false, null));
	}
	
	@Test
	public void testfindByLimit_startとendがnullの場合() {
		int result = target.findByLimit(null, null).size();
		assertThat(result, is(2));
	}
	
	@Test
	public void testfindByLimit_startがnullでendが20200101の場合() {
		int result = target.findByLimit(null, LocalDate.of(2020, 01, 01)).size();
		assertThat(result, is(1));
	}
	
	@Test
	public void testfindByLimit_startがnullでendが20200201の場合() {
		int result = target.findByLimit(null, LocalDate.of(2020, 02, 01)).size();
		assertThat(result, is(1));
	}
	
	@Test
	public void testfindByLimit_startがnullでendが20200202の場合() {
		int result = target.findByLimit(null, LocalDate.of(2020, 02, 02)).size();
		assertThat(result, is(2));
	}
	
	@Test
	public void testfindByLimit_startが20200101でendがnullの場合() {
		int result = target.findByLimit(LocalDate.of(2020, 01, 01), null).size();
		assertThat(result, is(2));
	}
	
	@Test
	public void testfindByLimit_startが20200202でendがnullの場合() {
		int result = target.findByLimit(LocalDate.of(2020, 02, 02), null).size();
		assertThat(result, is(1));
	}
	
	@Test
	public void testfindByLimit_startが20200203でendがnullの場合() {
		int result = target.findByLimit(LocalDate.of(2020, 02, 03), null).size();
		assertThat(result, is(0));
	}
	
	@Test
	public void testfindByLimit_startが20200101でendが20201231の場合() {
		int result = target.findByLimit(LocalDate.of(2020, 01, 01), LocalDate.of(2020, 12, 31)).size();
		assertThat(result, is(2));
	}

}
