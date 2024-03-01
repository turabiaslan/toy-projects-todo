package com.beam.todo.repository;


import com.beam.todo.model.Priority;
import com.beam.todo.model.ToDo;
import com.beam.todo.model.User;
import com.beam.todo.service.ToDoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class ToDoRepositoryTest {



    @Autowired
    ToDoRepository toDoRepository;

    @Autowired
    ToDoService toDoService;

    @Autowired
    UserRepository userRepository;

/**
    @Test
    public void testAddToDo(){
        ToDo toDo = new ToDo()
                .setTitle("Gelecek Ay Event")
                .setStart(new Date())
                .setFlagged(true)
                .setNotes("Hallo!")
                .setUser(userRepository.findByMailAddress("turabiaslan@gmail.com"));
        toDo.setId(UUID.randomUUID().toString());
        toDoRepository.save(toDo);
    }


    @Test
    public void testIsToday() {
        List<ToDo> todo = toDoRepository.findByTitle("match");
        System.out.println(toDoService.isToday(todo.get(0).getStart()));

    }
 */

    @Test
    public void testUpcomingTodo(){
        User user = userRepository.findByMailAddress("turabiaslan@gmail.com");
        System.out.println(toDoService.upcomingToDo(user));

    }
}
