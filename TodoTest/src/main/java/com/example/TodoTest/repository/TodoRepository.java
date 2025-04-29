package com.example.TodoTest.repository;

import com.example.TodoTest.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo,Long> {
    List<Todo> findByCompletedTrue();
    List<Todo> findByCompletedFalse();

}
