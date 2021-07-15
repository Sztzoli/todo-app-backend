package com.example.todoapp.controllers;

import com.example.todoapp.dto.CategoryDto;
import com.example.todoapp.dto.TodoDto;
import com.example.todoapp.dto.commands.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from todos", "delete from categories"})
public class TodoControllerRestTemplateIT {

    @Autowired
    TestRestTemplate template;

    @Test
    void createCategory() {
        CategoryDto category = template
                .postForObject("/api/todos/category",
                        new SaveCategoryCommand("Work"),
                        CategoryDto.class);

        assertEquals("Work", category.getDescription());
    }

    @Test
    void createCategoryValid() {
        Problem problem = template
                .postForObject("/api/todos/category",
                        new SaveCategoryCommand(""),
                        Problem.class);

        assertEquals(Status.BAD_REQUEST, problem.getStatus());
        assertEquals(URI.create("https://zalando.github.io/problem/constraint-violation"), problem.getType());
    }

    @Test
    void updateCategory() {
        CategoryDto category = template
                .postForObject("/api/todos/category",
                        new SaveCategoryCommand("Work"),
                        CategoryDto.class);
        System.out.println(category.getId());
        template
                .put("/api/todos/category/" + category.getId(),
                        new UpdateCategoryCommand("Not Work"));
        CategoryDto categoryUpdated = template
                .getForObject("/api/todos/category/" + category.getId(),
                        CategoryDto.class);

        assertEquals("Not Work", categoryUpdated.getDescription());
    }

    @Test
    void updateCategoryValid() {
        CategoryDto category = template

                .postForObject("/api/todos/category",
                        new SaveCategoryCommand("Work"),
                        CategoryDto.class);
        Problem problem = template

                .exchange("/api/todos/category/" + category.getId(),
                        HttpMethod.PUT,
                        new HttpEntity<>(new UpdateCategoryCommand("")),
                        Problem.class).getBody();

        assertEquals(Status.BAD_REQUEST, problem.getStatus());
        assertEquals("Constraint Violation", problem.getTitle());
    }

    @Test
    void deleteCategory() {
        CategoryDto category = template
                .postForObject("/api/todos/category",
                        new SaveCategoryCommand("Work"),
                        CategoryDto.class);

        template

                .delete("/api/todos/category/" + category.getId());

        List<CategoryDto> categories = template

                .exchange("/api/todos/category",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<CategoryDto>>() {
                        }).getBody();


        assertEquals(0, categories.size());
    }

    @Test
    void notFoundCategory() {
        Problem problem = template

                .getForObject("/api/todos/category/" + 1,
                        Problem.class);

        assertEquals(Status.NOT_FOUND, problem.getStatus());
        assertEquals("Not found", problem.getTitle());
    }

    @Test
    void notFoundTodo() {
        CategoryDto category = template

                .postForObject("/api/todos/category",
                        new SaveCategoryCommand("Work"),
                        CategoryDto.class);
        Problem problem = template.getForObject(
                String.format("/api/todos/category/%d/todos/%d", category.getId(), 1),
                Problem.class);

        assertEquals(Status.NOT_FOUND, problem.getStatus());
        assertEquals(URI.create("todos/todo-not-found"), problem.getType());
    }

    @Test
    void addTodo() {
        CategoryDto category = template

                .postForObject("/api/todos/category",
                        new SaveCategoryCommand("Work"),
                        CategoryDto.class);
        CategoryDto categoryAddTodo = template

                .postForObject("/api/todos/category/" + category.getId(),
                        new SaveTodoCommand("Learn Java"),
                        CategoryDto.class);

        assertEquals("Learn Java", categoryAddTodo.getTodos().get(0).getDescription());
    }

    @Test
    void updateTodoDescription() {
        CategoryDto category = template
                .postForObject("/api/todos/category",
                        new SaveCategoryCommand("Work"),
                        CategoryDto.class);
        CategoryDto categoryAddTodo = template
                .postForObject("/api/todos/category/" + category.getId(),
                        new SaveTodoCommand("Learn Java"),
                        CategoryDto.class);
        Long todoId = categoryAddTodo.getTodos().get(0).getId();
        template
                .put(String.format("/api/todos/category/%d/todos/%d/change-name",
                        categoryAddTodo.getId(), todoId),
                        new UpdateTodoCommand("Learn C#"));
        TodoDto todo = template.getForObject(
                String.format("/api/todos/category/%d/todos/%d", categoryAddTodo.getId(), todoId),
                TodoDto.class
        );

        assertEquals("Learn C#", todo.getDescription());

    }

    @Test
    void getTodo() {
        CategoryDto category = template
                .postForObject("/api/todos/category",
                        new SaveCategoryCommand("Work"),
                        CategoryDto.class);
        CategoryDto categoryAddTodo = template
                .postForObject("/api/todos/category/" + category.getId(),
                        new SaveTodoCommand("Learn Java"),
                        CategoryDto.class);
        Long todoId = categoryAddTodo.getTodos().get(0).getId();
        TodoDto todo = template.getForObject(
                String.format("/api/todos/category/%d/todos/%d", categoryAddTodo.getId(), todoId),
                TodoDto.class
        );
        assertEquals("Learn Java", todo.getDescription());
    }


    @Test
    void UpdateTodoIsDone() {
        CategoryDto category = template
                .postForObject("/api/todos/category",
                        new SaveCategoryCommand("Work"),
                        CategoryDto.class);
        CategoryDto categoryAddTodo = template
                .postForObject("/api/todos/category/" + category.getId(),
                        new SaveTodoCommand("Learn Java"),
                        CategoryDto.class);
        Long todoId = categoryAddTodo.getTodos().get(0).getId();
        template
                .put(String.format("/api/todos/category/%d/todos/%d/change-done",
                        categoryAddTodo.getId(), todoId),
                        new DoneTodoCommand(true));
        TodoDto todo = template.getForObject(
                String.format("/api/todos/category/%d/todos/%d", categoryAddTodo.getId(), todoId),
                TodoDto.class
        );

        assertEquals(true, todo.isDone());
    }

    @Test
    void deleteTodo() {
        CategoryDto category = template
                .postForObject("/api/todos/category",
                        new SaveCategoryCommand("Work"),
                        CategoryDto.class);
        CategoryDto categoryAddTodo = template
                .postForObject("/api/todos/category/" + category.getId(),
                        new SaveTodoCommand("Learn Java"),
                        CategoryDto.class);
        Long todoId = categoryAddTodo.getTodos().get(0).getId();
        template.delete(
                String.format("/api/todos/category/%d/todos/%d", categoryAddTodo.getId(), todoId)
        );
        List<TodoDto> todos = template.exchange(
                String.format("/api/todos/category/%d/todos", categoryAddTodo.getId()),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TodoDto>>() {
                }
        ).getBody();

        assertEquals(0, todos.size());
    }
}
