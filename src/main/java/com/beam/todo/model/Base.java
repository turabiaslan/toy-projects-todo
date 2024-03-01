package com.beam.todo.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Base {

    @Id
    private String id;

    @Override
    public boolean equals(Object obj) {
        Base other = (Base) obj;
        if (other == null) {
            return false;
        } else
            return getId().equals(other.getId());
    }
}
