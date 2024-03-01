package com.beam.todo.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Accessors(chain = true)

public class Profile {

    private String avatar;

    private String name;

    private String surname;

}
