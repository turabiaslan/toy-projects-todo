package com.beam.todo.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Document("User")
@TypeAlias("User")
@Accessors(chain = true)


public class User extends Base{

    private String mailAddress;

    private String password;

    private Profile profile;




    public Profile getProfile() {
        if(profile == null){
            return new Profile();
        }else{
            return profile;
        }
    }


}
