package com.example.common.validation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;

import javax.validation.ConstraintValidatorContext;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.terasoluna.gfw.common.date.ClassicDateFactory;

public class FutureDateCheckValidatorTest {
	
	@Rule
	public MockitoRule mockito = MockitoJUnit.rule();
	
	@Mock
	ClassicDateFactory dateFactory;
	
	@InjectMocks
	FutureDateCheckValidator target;
	
	ConstraintValidatorContext context = spy(ConstraintValidatorContext.class);
	
	@Before
	public void setUp() throws Exception {
		// 2020-01-01を生成
		when(dateFactory.newSqlDate()).thenReturn(new Date(1577866341000L));
	}
	
	@Test
	public void testIsValid_nullでtrueを返す() {
		boolean result = target.isValid(null, context);
		assertThat(result, is(true));
	}
	
	@Test
	public void testIsValid_過去の日付でfalseを返す() {
		boolean result = target.isValid(LocalDate.of(2019, 12, 31), context);
		assertThat(result, is(false));
	}
	
	@Test
	public void testIsValid_今日の日付でtrueを返す() {
		boolean result = target.isValid(LocalDate.of(2020, 1, 1), context);
		assertThat(result, is(true));
	}
	
	@Test
	public void testIsValid_一年以内の未来の日付でtrueを返す() {
		boolean result = target.isValid(LocalDate.of(2020, 8, 4), context);
		assertThat(result, is(true));
	}
	
	@Test
	public void testIsValid_364日未来の日付でtrueを返す() {
		boolean result = target.isValid(LocalDate.of(2020, 12, 31), context);
		assertThat(result, is(true));
	}
	
	@Test
	public void testIsValid_一年未来の日付でfalseを返す() {
		boolean result = target.isValid(LocalDate.of(2021, 1, 1), context);
		assertThat(result, is(false));
	}

}
