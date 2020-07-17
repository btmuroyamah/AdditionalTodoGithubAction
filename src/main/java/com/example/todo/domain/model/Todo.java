package com.example.todo.domain.model;

import java.io.Serializable;

import org.joda.time.LocalDate;

import com.example.common.validation.FutureDateCheck;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Todo implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private String todoId;

    private String todoTitle;

    private boolean finished;

    private LocalDate createdAt;

    @FutureDateCheck
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate deadLine;


}
