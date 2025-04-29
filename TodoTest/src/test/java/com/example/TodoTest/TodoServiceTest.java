package com.example.TodoTest;

import com.example.TodoTest.model.Todo;
import com.example.TodoTest.repository.TodoRepository;
import com.example.TodoTest.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TodoServiceTest {
    @Mock
    private TodoRepository repo;

    @InjectMocks
    private TodoService service;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }
// description is empty
    @Test
    void createtodo_withoutdescription(){
        Todo todo =new Todo();
        todo.setDescription("");
        todo.setDeadline(new Date());

        Exception exception=assertThrows(IllegalArgumentException.class,()->{service.createTodo(todo);});

        assertEquals("Description is empty",exception.getMessage());
    }
    //deadline

    @Test
    void createtodo_deadlinebefore(){
        Todo todo =new Todo();
        todo.setDescription("hithere");
        Calendar cal= Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH,-1);
        todo.setDeadline(cal.getTime());
        Exception exception=assertThrows(IllegalArgumentException.class,()->{service.createTodo(todo);});
        assertEquals("Deadline cannot be past",exception.getMessage());
    }

    @Test
    void updatetodo_idvalid(){
        Todo todo =new Todo();
        todo.setId(999L);
        when(repo.findById(999L)).thenReturn(Optional.empty());
        Exception exception=assertThrows(IllegalArgumentException.class,()->{service.updateTodo(todo);});
        assertEquals("Invalid id",exception.getMessage());
    }

    // Invalid input (description is empty and deadline is in the past)

    @Test
    void updatetodo_invalidinput(){
        Todo todo =new Todo();
        todo.setDescription("hithere");
        todo.setId(999L);
        Calendar cal= Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH,-1);
        todo.setDeadline(cal.getTime());
        Exception exception=assertThrows(IllegalArgumentException.class,()->{service.updateTodo(todo);});
        assertEquals("Invalid id",exception.getMessage());
    }


    @Test
    void markAsCompleted() {
        Todo todo = new Todo();
        todo.setId(999L);
        todo.setDescription("hithere");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        todo.setDeadline(cal.getTime());

        Mockito.when(repo.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.markAsCompletedTodo(999L, true);
        });

        assertEquals("Todo not found with id: 999", exception.getMessage());
    }


    @Test
    void delete_todo(){
        Todo todo =new Todo();
        todo.setDescription("hithere");
        todo.setId(999L);
        Calendar cal= Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH,-1);
        todo.setDeadline(cal.getTime());
        Exception exception=assertThrows(IllegalArgumentException.class,()->{service.deleteTodo(todo);});
        assertEquals("Invalid id",exception.getMessage());
    }

    @Test
    void upcoming_todo() {
        Todo todo1 = new Todo();
        todo1.setDescription("todo1");
        todo1.setId(998L);
        todo1.setDeadline(new Date());
        todo1.setCompleted(false);

        Todo todo2 = new Todo();
        todo2.setDescription("todo2");
        todo2.setId(997L);
        todo2.setDeadline(new Date());
        todo2.setCompleted(false);

        List<Todo> todoList = Arrays.asList(todo1, todo2);
        when(repo.findByCompletedFalse()).thenReturn(todoList);
        List<Todo> upcomingTodo = service.getUpcomingTodo();
        assertEquals(2,upcomingTodo.size());
        assertNotNull(upcomingTodo);
        assertTrue(upcomingTodo.stream().allMatch(todo -> !todo.getCompleted()));
    }

    @Test
    void completed_todo() {
        Todo todo1 = new Todo();
        todo1.setDescription("todo1");
        todo1.setId(998L);
        todo1.setDeadline(new Date());
        todo1.setCompleted(true);

        Todo todo2 = new Todo();
        todo2.setDescription("todo2");
        todo2.setId(997L);
        todo2.setDeadline(new Date());
        todo2.setCompleted(true);

        List<Todo> todoList = Arrays.asList(todo1, todo2);
        when(repo.findByCompletedTrue()).thenReturn(todoList);
        List<Todo> completedTodo = service.getCompletedTodo();
        assertEquals(2,completedTodo.size());
        assertNotNull(completedTodo);
        assertTrue(completedTodo.stream().allMatch(todo -> todo.getCompleted()));
    }
}
