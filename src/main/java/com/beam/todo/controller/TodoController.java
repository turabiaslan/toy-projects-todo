package com.beam.todo.controller;


import com.beam.todo.ToDoDto.ToDoDto;
import com.beam.todo.model.Priority;
import com.beam.todo.model.ToDo;
import com.beam.todo.model.User;
import com.beam.todo.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

import static com.beam.todo.controller.UserController.SESSION_USER;

@RestController
@RequiredArgsConstructor
@RequestMapping("to-do")
public class TodoController {

    private final ToDoService toDoService;


    @PostMapping("add-to-do")
    public void addToDo(@RequestBody ToDo todo, HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER);
        if (user != null) {
            toDoService.addToDo(todo.getTitle(), todo.getNotes(), user, todo.getStart(), todo.getLocation(), todo.getPriority(), todo.isCompleted(), todo.isFlagged());
        }
    }


    @GetMapping("show-today")
    public List<ToDoDto> showToday(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER);
        if (user != null) {
            return toDoService.showToday(user);
        }else {
            return null;
        }
    }

    @GetMapping("show-month")
    public List<ToDoDto> showMonth(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER);
        if (user != null) {
            return toDoService.showMonth(user);
        }else {
            return null;
        }
    }

    @GetMapping("show-flagged")
    public List<ToDoDto> showFlagged(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER);
        if (user != null) {
            return toDoService.showFlagged(user);
        } else {
            return null;
        }
    }


    @GetMapping("show-all")
    public List<ToDoDto> showAll(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER);
        if (user != null) {
            return toDoService.showAllToDo(user);
        } else {
            return null;
        }
    }

    @GetMapping("show-month-with-priority")
    public List<ToDoDto> showMonthWithPriority(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER);
        if (user != null) {
            return toDoService.showMonthWithPriority(user);
        } else {
            return null;
        }
    }

    @GetMapping("mark-completed")
    public void markAsCompleted(String id, HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER);
        if (user != null) {
            toDoService.markCompleted(id);
        }
    }

    @GetMapping("show-completed")
    public List<ToDoDto> showCompleted(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER);
        if (user != null) {
            return toDoService.showCompleted(user);
        } else
            return null;

    }

    @GetMapping("search/{param}")
    public List<ToDoDto> searchToDo(@PathVariable String param,
                                    HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER);
        if (user != null) {
            return toDoService.search(param);
        } else {
            return null;
        }
    }


}
