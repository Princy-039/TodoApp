package com.example.TodoTest.service;

import com.example.TodoTest.model.Todo;
import com.example.TodoTest.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TodoService {
    @Autowired
    private TodoRepository repo;

    public Todo createTodo(Todo todo) {
        if(todo.getDescription()==null||todo.getDescription().trim().isEmpty()){
            throw new IllegalArgumentException("Description is empty");
        }
        if(todo.getDeadline().before(new Date())) {
            throw new IllegalArgumentException("Deadline cannot be past");
        }
        return repo.save(todo);
    }

    public Todo updateTodo(Todo todo) {
        if (todo.getId() == null || !repo.findById(todo.getId()).isPresent()) {
            throw new IllegalArgumentException("Invalid id");
        }
        if (todo.getDescription()==null||todo.getDescription().trim().isEmpty()){
            throw new IllegalArgumentException("Description is empty");
        }
        if(todo.getDeadline().before(new Date())) {
            throw new IllegalArgumentException("Deadline cannot be past");
        }
        return repo.save(todo);
    }


    public Todo markAsCompletedTodo(Long id, Boolean completed) {
        if (completed == null) {
            throw new IllegalArgumentException("Completed value is missing");
        }

        Todo todo = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Todo not found with id: " + id));

        todo.setCompleted(completed);
        return repo.save(todo);
    }



    public void deleteTodo(Todo todo) {
        if (todo.getId() == null || !repo.findById(todo.getId()).isPresent()) {
            throw new IllegalArgumentException("Invalid id");
        }
        repo.deleteById(todo.getId());

    }

    public List<Todo> getUpcomingTodo() {
        return repo.findByCompletedFalse();
    }

    public List<Todo> getCompletedTodo() {
        return repo.findByCompletedTrue();
    }

    public List<Todo> getAllTodo(){
        return repo.findAll();
    }


    public Todo getTodoById(Long id) {
        return repo.findById(id).orElse(null);
    }

}
