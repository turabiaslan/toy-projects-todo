package com.beam.todo.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@Document("ToDo")
@TypeAlias("ToDo")

public class ToDo extends Base{

    private String title;

    private String notes;

    private Date start;

    private boolean completed;

    private String location;


    private Priority priority;

    private boolean flagged;

    @DBRef
    private User user;


}
