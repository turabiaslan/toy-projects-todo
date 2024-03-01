package com.beam.todo.repository;

import com.beam.todo.model.Priority;
import com.beam.todo.model.ToDo;
import com.beam.todo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface ToDoRepository extends MongoRepository<ToDo, String> {

    List<ToDo> findByTitle (String title);

    List<ToDo> findByNotes (String notes);

    List<ToDo> findByUser(User user);

    @Query("{title : {$regex : ?0}}")
    List<ToDo> findTitleContains (String regex);

    @Query("{notes : {$regex : ?0}}")
    List<ToDo> findNotesContains(String regex);

    List<ToDo> findByStartDate(Date start);


    List<ToDo> findByLocation(String location);


    List<ToDo> findByPriority(Priority priority);


    @Query("{flagged : ?0}")
    List<ToDo> findByQFlag(boolean flag);
}
