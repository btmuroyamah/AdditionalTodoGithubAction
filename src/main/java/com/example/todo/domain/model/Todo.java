package com.example.todo.domain.model;

import java.io.Serializable;
import java.time.LocalDate;

import com.example.common.validation.DateCheck;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.Data;

@Data
public class Todo implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private String todoId;

    private String todoTitle;

    private boolean finished;
    


    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate createdAt;
    
    @DateCheck
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate deadLine;


}
