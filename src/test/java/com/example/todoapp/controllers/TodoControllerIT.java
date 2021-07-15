package com.example.todoapp.controllers;

import com.example.todoapp.dto.CategoryDto;
import com.example.todoapp.dto.TodoDto;
import com.example.todoapp.dto.commands.*;
import com.example.todoapp.services.TodoService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(statements = {"delete from todos", "delete from categories"})
class TodoControllerIT {

    @Autowired
    TodoController todoController;

    @Test
    void createCategory() {
        todoController.createCategory( new SaveCategoryCommand("Work"));
        CategoryDto category = todoController.getCategories().get(0);
        assertEquals("Work", category.getDescription());
    }

    @Test
    void updateCategory() {
        CategoryDto category = todoController.createCategory(new SaveCategoryCommand("Work"));
        CategoryDto categoryUpdated = todoController.updateCategory( category.getId(), new UpdateCategoryCommand("Not Work"));
        assertEquals("Not Work", categoryUpdated.getDescription());
    }

    @Test
    void deleteCategory() {
        CategoryDto category = todoController.createCategory( new SaveCategoryCommand("Work"));
        todoController.deleteCategory( category.getId());
        assertEquals(0, todoController.getCategories().size());
    }

    @Test
    void addTodo() {
        CategoryDto category = todoController.createCategory( new SaveCategoryCommand("Work"));
        CategoryDto categoryAddTodo =
                todoController.addTodo( category.getId(), new SaveTodoCommand("Learn Java"));
        TodoDto todo = todoController.getTodos( categoryAddTodo.getId()).get(0);
        assertEquals("Learn Java", todo.getDescription());
    }

    @Test
    void getTodo() {
        CategoryDto category = todoController.createCategory(new SaveCategoryCommand("Work"));
        CategoryDto categoryAddTodo =
                todoController.addTodo( category.getId(), new SaveTodoCommand("Learn Java"));
        Long todoId = categoryAddTodo.getTodos().get(0).getId();
        TodoDto todo = todoController.getTodo( categoryAddTodo.getId(), todoId);
        assertEquals("Learn Java", todo.getDescription());
    }

    @Test
    void updateTodoDescription() {
        CategoryDto category = todoController.createCategory(new SaveCategoryCommand("Work"));
        CategoryDto categoryAddTodo =
                todoController.addTodo( category.getId(), new SaveTodoCommand("Learn Java"));
        Long todoId = categoryAddTodo.getTodos().get(0).getId();
        todoController
                .updateTodoDescription(
                        categoryAddTodo.getId(),
                        todoId,
                        new UpdateTodoCommand("Learn C#"));
        assertEquals("Learn C#", todoController
                .getTodos( categoryAddTodo.getId()).get(0).getDescription());
    }

    @Test
    void UpdateTodoIsDone() {
        CategoryDto category = todoController.createCategory( new SaveCategoryCommand("Work"));
        CategoryDto categoryAddTodo =
                todoController.addTodo( category.getId(), new SaveTodoCommand("Learn Java"));
        Long todoId = categoryAddTodo.getTodos().get(0).getId();
        todoController
                .updateTodoIsDone(
                        categoryAddTodo.getId(),
                        todoId,
                        new DoneTodoCommand(true));
        assertEquals(true, todoController
                .getTodos( categoryAddTodo.getId()).get(0).isDone());
    }

    @Test
    void deleteTodo() {
        CategoryDto category = todoController.createCategory( new SaveCategoryCommand("Work"));
        CategoryDto categoryAddTodo =
                todoController.addTodo( category.getId(), new SaveTodoCommand("Learn Java"));
        Long todoId = categoryAddTodo.getTodos().get(0).getId();
        todoController.deleteTodo(categoryAddTodo.getId(), todoId);
        assertEquals(0, todoController.getTodos( categoryAddTodo.getId()).size());
    }
}