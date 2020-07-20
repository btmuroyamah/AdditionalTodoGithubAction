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

}
