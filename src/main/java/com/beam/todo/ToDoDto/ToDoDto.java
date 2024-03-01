package com.beam.todo.ToDoDto;


import com.beam.todo.model.Priority;
import com.beam.todo.model.ToDo;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;


@Accessors(chain = true)
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ToDoDto implements Comparable<ToDoDto> {

    private String id;

    private String title;

    private String notes;

    private Date start;

    private boolean completed;

    private String Location;


    private Priority priority;

    private boolean flagged;


    public static ToDoDto toDoToToDoDto(ToDo toDo) {
        return new ToDoDto()
                .setId(toDo.getId())
                .setTitle(toDo.getTitle())
                .setNotes(toDo.getNotes() == null ? "" :
                        toDo.getNotes().length() <= 100 ? toDo.getNotes() :
                                toDo.getNotes().substring(0, 99))
                .setStart(toDo.getStart())
                .setCompleted(toDo.isCompleted())
                .setLocation(toDo.getLocation())
                .setPriority(toDo.getPriority())
                .setFlagged(toDo.isFlagged());

    }

    @Override
    public int compareTo(ToDoDto o) {
        return getPriority().ordinal() - o.getPriority().ordinal();
    }


}