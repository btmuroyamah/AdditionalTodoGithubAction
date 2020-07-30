package com.example.todo.domain.service.todo;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.terasoluna.gfw.common.date.ClassicDateFactory;

import com.example.todo.domain.model.Priority;
import com.example.todo.domain.model.Todo;
import com.example.todo.domain.repository.todo.TodoRepository;

public class TodoServiceTest {

	@Rule
	public MockitoRule mockito = MockitoJUnit.rule();

	@Mock
	TodoRepository todoRepository;
	
	@Mock
	ClassicDateFactory dateFactory;

	@InjectMocks
	private TodoServiceImpl target;

	@Test
	public void testFindAll_PrioritySort() {
		// findAll()を呼び出したときにmockListをかえす処理

		// 設定
		List<Todo> mockList = new ArrayList<>();

		Todo mockLow = new Todo();
		mockLow.setPriority(Priority.Low);
		mockLow.setCreatedAt(LocalDateTime.of(2020, 07, 01, 00, 00, 00));
		Todo mockMiddle = new Todo();
		mockMiddle.setPriority(Priority.Middle);
		mockMiddle.setCreatedAt(LocalDateTime.of(2020, 07, 01, 00, 00, 00));
		Todo mockHigh = new Todo();
		mockHigh.setPriority(Priority.High);
		mockHigh.setCreatedAt(LocalDateTime.of(2020, 07, 01, 00, 00, 00));



		mockList.add(mockLow);
		mockList.add(mockMiddle);
		mockList.add(mockHigh);

		when(todoRepository.findAll()).thenReturn(mockList);

		// 実行
		List<Todo> result = target.findAll();

		// アサーション
		assertThat(result.get(0).getPriority(), is(Priority.High));
		assertThat(result.get(1).getPriority(), is(Priority.Middle));
		assertThat(result.get(2).getPriority(), is(Priority.Low));
	}

	@Test
	public void testFindAll_LocalDateSort() {

		// 設定
		List<Todo> mockList = new ArrayList<>();

		Todo mockHigh1 = new Todo();
		mockHigh1.setPriority(Priority.High);
		mockHigh1.setCreatedAt(LocalDateTime.of(2020, 07, 01, 00, 00, 00));
		Todo mockHigh2 = new Todo();
		mockHigh2.setPriority(Priority.High);
		mockHigh2.setCreatedAt(LocalDateTime.of(2020, 07, 02, 00, 00, 00));
		Todo mockHigh3 = new Todo();
		mockHigh3.setPriority(Priority.High);
		mockHigh3.setCreatedAt(LocalDateTime.of(2020, 07, 03, 00, 00, 00));

		mockList.add(mockHigh1);
		mockList.add(mockHigh2);
		mockList.add(mockHigh3);

		when(todoRepository.findAll()).thenReturn(mockList);

		// 実行
		List<Todo> result = target.findAll();

		// アサーション
		assertThat(result.get(2).getCreatedAt(), is(LocalDateTime.of(07, 03, 00, 00, 00)));
		assertThat(result.get(1).getCreatedAt(), is(LocalDateTime.of(2020, 7, 2, 00, 00, 00)));
		assertThat(result.get(0).getCreatedAt(), is(LocalDateTime.of(2020, 7, 1, 00, 00, 00)));
	}
	
	@Test
	public void testCreate_NullToLow() {
		//設定
		Todo mockNull = new Todo();
		mockNull.setPriority(null);

		//実行
		Todo result = target.create(mockNull);
		
		//アサーション
		assertThat(result.getPriority(), is(Priority.Low));
	}

}
