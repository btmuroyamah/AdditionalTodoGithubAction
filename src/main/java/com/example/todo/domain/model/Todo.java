package com.example.todo.domain.model;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

@Data
public class Todo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String todoId;

	private String todoTitle;

	private boolean finished;

	private LocalDate createdAt;

	private LocalDate deadLine;
	
	private Priority priority;
	
	//test用のコンストラクタ コメントアウトしないとcom.github.dozermapper.core.MappingExceptionが発生する
	public Todo(String todoId, String title, boolean finished, LocalDate deadLine){
		this.todoId = todoId;
		this.todoTitle = title;
		this.finished = finished;
		this.deadLine = deadLine;
	}
	
	public Todo() {}

}
