package com.example.TodoTest;

import com.example.TodoTest.model.Todo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;

import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTodo() throws Exception {
        Todo todo = new Todo();
        todo.setId(45L);
        todo.setDescription("helloworld");
        todo.setCompleted(false);
        todo.setDeadline(new Date(System.currentTimeMillis() + 86400000));

        mockMvc.perform(post("/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("helloworld"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void getAllTodo() throws Exception {
        Todo todo = new Todo();
        todo.setId(46L);
        todo.setDescription("helloworld");
        todo.setCompleted(false);
        todo.setDeadline(new Date(System.currentTimeMillis() + 86400000));

        mockMvc.perform(post("/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("helloworld"))
                .andExpect(jsonPath("$.completed").value(false));

        mockMvc.perform(get("/getalltodo")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("helloworld"))
                .andExpect(jsonPath("$[0].completed").value(false));

    }

    @Test
    void update_todo() throws Exception {
        Todo todo = new Todo();
        todo.setId(47L);
        todo.setDescription("helloworld");
        todo.setCompleted(false);
        todo.setDeadline(new Date(System.currentTimeMillis() + 86400000));

        mockMvc.perform(post("/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("helloworld"))
                .andExpect(jsonPath("$.completed").value(false));

        todo.setDescription("hithere");

        mockMvc.perform(put("/update/{id}",todo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("hithere"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void markascompleted_todo() throws Exception {
        Todo todo = new Todo();
        todo.setId(48L);
        todo.setDescription("helloworld");
        todo.setCompleted(false);
        todo.setDeadline(new Date(System.currentTimeMillis() + 86400000));

        mockMvc.perform(post("/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("helloworld"))
                .andExpect(jsonPath("$.completed").value(false));

        todo.setCompleted(true);

        mockMvc.perform(patch("/mark/{id}",todo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("helloworld"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void delete_todo() throws Exception {
        Todo todo = new Todo();
        todo.setId(49L);
        todo.setDescription("helloworld");
        todo.setCompleted(false);
        todo.setDeadline(new Date(System.currentTimeMillis() + 86400000));

        mockMvc.perform(post("/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("helloworld"))
                .andExpect(jsonPath("$.completed").value(false));

        mockMvc.perform(delete("/delete/{id}", todo.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted"));
    }

    @Test
    void getCompletedTodo() throws Exception {
        Todo todo = new Todo();
        todo.setId(48L);
        todo.setDescription("helloworld");
        todo.setCompleted(true);
        todo.setDeadline(new Date(System.currentTimeMillis() + 86400000));

        mockMvc.perform(post("/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("helloworld"))
                .andExpect(jsonPath("$.completed").value(true));


        mockMvc.perform(get("/completedtodo")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("helloworld"))
                .andExpect(jsonPath("$[0].completed").value(true));
    }

    @Test
    void getUpcomingTodo() throws Exception {
        Todo todo = new Todo();
        todo.setId(47L);
        todo.setDescription("helloworld");
        todo.setCompleted(false);  // Ensure completed is false
        todo.setDeadline(new Date(System.currentTimeMillis() + 86400000));  // Set deadline to 1 day ahead

        mockMvc.perform(post("/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("helloworld"))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.deadline").exists()) ;// Check that deadline exists

        mockMvc.perform(get("/upcomingtodo")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("helloworld"))
                .andExpect(jsonPath("$[0].completed").value(false));

    }

}
