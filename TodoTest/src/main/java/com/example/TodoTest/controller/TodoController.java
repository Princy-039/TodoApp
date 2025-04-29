package com.example.TodoTest.controller;

import com.example.TodoTest.model.Todo;
import com.example.TodoTest.service.TodoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sound.midi.MidiSystem;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class TodoController {
    @Autowired
    private TodoService service;
    @PostMapping("/add")
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo){
        return new ResponseEntity<>(service.createTodo(todo), HttpStatus.OK);
    }
    @GetMapping("/getalltodo")
    public ResponseEntity<List<Todo>>getAll(){
        return new ResponseEntity<>(service.getAllTodo(),HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public  ResponseEntity<Todo>updateTodo(@PathVariable long id,@RequestBody Todo todo){
        Todo updatedTodo =service.updateTodo(todo);
        if (updatedTodo!=null)
            return new ResponseEntity<>(updatedTodo,HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PatchMapping("/mark/{id}")
    public ResponseEntity<Todo> markCompleted(@PathVariable Long id, @RequestBody Map<String, Boolean> completedMap) {
        try {
            Todo updatedTodo = service.markAsCompletedTodo(id, completedMap.get("completed"));
            return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String>deleteTodo(@PathVariable Long id){
        Todo deletedTodo=service.getTodoById(id);
        if (deletedTodo!=null){
            service.deleteTodo(deletedTodo);
            return new ResponseEntity<>("Deleted",HttpStatus.OK);}
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/upcomingtodo")
    public ResponseEntity<List<Todo>>upcomingTodo(){
        return new ResponseEntity<>(service.getUpcomingTodo(),HttpStatus.OK);
    }

    @GetMapping("/completedtodo")
    public ResponseEntity<List<Todo>>completedTodo(){
        return new ResponseEntity<>(service.getCompletedTodo(),HttpStatus.OK);
    }
}
