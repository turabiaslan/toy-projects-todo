package com.beam.todo.service;


import com.beam.todo.ToDoDto.ToDoDto;
import com.beam.todo.model.Priority;
import com.beam.todo.model.ToDo;
import com.beam.todo.model.User;
import com.beam.todo.repository.ToDoRepository;
import com.mongodb.lang.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ToDoService {

    private final ToDoRepository toDoRepository;


    public void addToDo(String title, String notes, User user, Date start,
                        @Nullable String location,
                        @Nullable Priority priority,
                        boolean completed,
                        boolean flagged) {
        if (title == null || title.isEmpty()) {
            System.out.println("Title cannot be null or empty!");
        }
        ToDo toDo = new ToDo()
                .setTitle(title)
                .setNotes(notes)
                .setStart(start)
                .setCompleted(completed)
                .setLocation(location)
                .setPriority(priority)
                .setFlagged(flagged)
                .setUser(user);
        toDo.setId(UUID.randomUUID().toString());
        toDoRepository.save(toDo);
    }



    public List<ToDo> upcomingToDo(User user) {
        Date today = new Date();
        List<ToDo> upcoming = new ArrayList<>();
        toDoRepository.findByUser(user).forEach(t -> {
            if (!t.isCompleted()) {
                upcoming.add(t);
            }
        });
        return upcoming;
    }

    public List<ToDoDto> toDoListToToDoDTo(List<ToDo> toDoList) {
        List<ToDoDto> toDoDtoList = new ArrayList<>();
        toDoList.forEach(toDo -> toDoDtoList.add(ToDoDto.toDoToToDoDto(toDo)));
        return toDoDtoList;
    }


    public List<ToDoDto> showAllToDo(User user) {
        List<ToDo> toDoList = upcomingToDo(user);
        return toDoListToToDoDTo(toDoList);
    }

    public boolean isSameMonth(Date date) {
        Calendar sameMonth = Calendar.getInstance();
        Calendar specifiedDate = Calendar.getInstance();
        specifiedDate.setTime(date);
        return sameMonth.get(Calendar.MONTH) == specifiedDate.get(Calendar.MONTH) && sameMonth.get(Calendar.YEAR) == specifiedDate.get(Calendar.YEAR);
    }


    public boolean isToday(Date date) {
        Calendar today = Calendar.getInstance();
        Calendar specifiedDate = Calendar.getInstance();
        specifiedDate.setTime(date);
        return today.get(Calendar.DAY_OF_MONTH) == specifiedDate.get(Calendar.DAY_OF_MONTH) && today.get(Calendar.MONTH) == specifiedDate.get(Calendar.MONTH) && today.get(Calendar.YEAR) == specifiedDate.get(Calendar.YEAR);
    }

    public List<ToDoDto> showToday(User user) {
        List<ToDo> todayToDo = new ArrayList<>();
        upcomingToDo(user).forEach(t -> {
            if (isToday(t.getStart()) && !t.isCompleted()) {
                todayToDo.add(t);
            }
        });
        return toDoListToToDoDTo(todayToDo);
    }

    public List<ToDoDto> showFlagged(User user) {
        List<ToDo> flagged = new ArrayList<>();
        upcomingToDo(user).forEach(t -> {
            if (t.isFlagged()) {
                flagged.add(t);
            }
        });
        return toDoListToToDoDTo(flagged);
    }

    public List<ToDoDto> showMonth(User user) {
        List<ToDo> monthlyToDo = new ArrayList<>();
        upcomingToDo(user).forEach(t -> {
            if (isSameMonth(t.getStart()) && !t.isCompleted()) {
                monthlyToDo.add(t);
            }
        });
        return toDoListToToDoDTo(monthlyToDo);
    }


    public List<ToDoDto> showMonthWithPriority(User user) {
        List<ToDoDto> toDoDtos = showMonth(user);
        Collections.sort(toDoDtos, Comparator.comparingInt(o -> o.getPriority().ordinal()));
        return toDoDtos;
    }

    public void markCompleted(String id) {
        ToDo toDo = toDoRepository.findById(id).get();
        if (toDo.isCompleted()) {
            toDo.setCompleted(false);
        } else {
            toDo.setCompleted(true);
        }
        toDoRepository.save(toDo);
    }

    public List<ToDoDto> search(String searchParam) {
        List<ToDo> searchedTodos = new ArrayList<>();
        searchedTodos.addAll(toDoRepository.findTitleContains(searchParam));
        searchedTodos.addAll(toDoRepository.findNotesContains(searchParam));
        return toDoListToToDoDTo(searchedTodos);
    }


    public List<ToDoDto> showCompleted(User user) {
        List<ToDo> result = new ArrayList<>();
        List<ToDo> completedTodos = toDoRepository.findByUser(user);
        completedTodos.forEach(c -> {
            if (c.isCompleted()) {
                result.add(c);
            };
        });
        return toDoListToToDoDTo(result);
    }
}