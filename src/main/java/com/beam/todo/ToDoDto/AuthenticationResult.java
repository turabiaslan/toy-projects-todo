package com.beam.todo.ToDoDto;


import com.beam.todo.model.Profile;
import com.beam.todo.model.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AuthenticationResult {

    private Profile profile;

    private User user;

    private boolean success;

    private String message;
}
