package com.example.todoapp.services;

import com.example.todoapp.dto.CategoryDto;
import com.example.todoapp.dto.commands.*;
import com.example.todoapp.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(statements = {"delete from todos", "delete from categories"})
class TodoServiceTest {

    @Autowired
    TodoService todoService;


    @Test
    void saveCategory() {
        todoService.saveCategory(new SaveCategoryCommand("Work"));
        List<CategoryDto> user1 = todoService.getCategories();
        assertEquals("Work", user1.get(0).getDescription());
    }

    @Test
    void deleteCategory() {
        CategoryDto category = todoService.saveCategory( new SaveCategoryCommand("Work"));
        todoService.deleteCategoryById( category.getId());
        List<CategoryDto> user1 = todoService.getCategories();
        assertEquals(0, user1.size());
    }

    @Test
    void updateCategory() {
        CategoryDto category = todoService.saveCategory( new SaveCategoryCommand("Work"));
        todoService.updateCategory(category.getId(), new UpdateCategoryCommand("Learn"));
        List<CategoryDto> user1 = todoService.getCategories();
        assertEquals("Learn", user1.get(0).getDescription());
    }

    @Test
    void addTodo() {
        CategoryDto category = todoService.saveCategory( new SaveCategoryCommand("Work"));
        todoService.addTodo(category.getId(), new SaveTodoCommand("Finish something"));
        assertEquals("Finish something", todoService.getTodos(category.getId()).get(0).getDescription());
    }

    @Test
    void getTodo() {
        CategoryDto category = todoService.saveCategory( new SaveCategoryCommand("Work"));
        CategoryDto categoryWithTodo = todoService.addTodo(category.getId(), new SaveTodoCommand("Finish something"));
        Long todoId = categoryWithTodo.getTodos().get(0).getId();
        assertEquals("Finish something", todoService.getTodo( category.getId(), todoId).getDescription());
    }

    @Test
    void updateTodo() {
        CategoryDto category = todoService.saveCategory( new SaveCategoryCommand("Work"));
        CategoryDto categorySaved = todoService.addTodo( category.getId(), new SaveTodoCommand("Finish something"));
        Long todoId = categorySaved.getTodos().get(0).getId();
        todoService.updateTodo( category.getId(), todoId, new UpdateTodoCommand("Done"));
        assertEquals("Done", todoService.getCategories().get(0).getTodos().get(0).getDescription());
    }

    @Test
    void finishTodo() {
        CategoryDto category = todoService.saveCategory( new SaveCategoryCommand("Work"));
        CategoryDto categorySaved = todoService.addTodo( category.getId(), new SaveTodoCommand("Finish something"));
        Long todoId = categorySaved.getTodos().get(0).getId();
        todoService.updateDoneTodo( category.getId(), todoId, new DoneTodoCommand(true));
        assertTrue(todoService.getCategories().get(0).getTodos().get(0).isDone());
    }

    @Test
    void deleteTodo() {
        CategoryDto category = todoService.saveCategory(new SaveCategoryCommand("Work"));
        CategoryDto categorySaved = todoService.addTodo( category.getId(), new SaveTodoCommand("Finish something"));
        Long todoId = categorySaved.getTodos().get(0).getId();
        todoService.deleteTodoById( categorySaved.getId(), todoId);
        assertEquals(0, todoService.getCategories().get(0).getTodos().size());
    }

}