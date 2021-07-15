package com.example.todoapp.controllers;

import com.example.todoapp.dto.CategoryDto;
import com.example.todoapp.dto.TodoDto;
import com.example.todoapp.dto.commands.*;
import com.example.todoapp.services.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("${todos.url}")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
@Tag(name = "Operations on categories and todos")
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/category")
    @Operation(summary = "List categories by username")
    public List<CategoryDto> getCategories() {
        return todoService.getCategories();
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "receive category by categoryId")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public CategoryDto getCategoryById(
            @PathVariable Long categoryId
    ) {
        return todoService.getCategoryById(categoryId);
    }

    @PostMapping("/category")
    @Operation(summary = "create category")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(
            @Valid @RequestBody SaveCategoryCommand command) {
        return todoService.saveCategory(command);
    }

    @PutMapping("/category/{categoryId}")
    @Operation(summary = "update category by categoryId")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public CategoryDto updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody UpdateCategoryCommand command
    ) {
        return todoService.updateCategory(categoryId, command);
    }

    @DeleteMapping("/category/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "delete category by categoryId")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public void deleteCategory(
            @PathVariable Long categoryId) {
        todoService.deleteCategoryById(categoryId);
    }

    @PostMapping("/category/{categoryId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add Todo to category by categoryId")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public CategoryDto addTodo(
            @PathVariable Long categoryId,
            @Valid @RequestBody SaveTodoCommand command
    ) {
        return todoService.addTodo(categoryId, command);
    }

    @GetMapping("/category/{categoryId}/todos")
    @Operation(summary = "recieve all Todos from category by categoryId")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public List<TodoDto> getTodos(
            @PathVariable Long categoryId
    ) {
        return todoService.getTodos( categoryId);
    }

    @GetMapping("/category/{categoryId}/todos/{todoId}")
    @Operation(summary = "recieve Todo from category by categoryId and todoId")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @ApiResponse(responseCode = "404", description = "Todo not found")
    public TodoDto getTodo(
            @PathVariable Long categoryId,
            @PathVariable Long todoId
    ) {
        return todoService.getTodo( categoryId, todoId);
    }

    @PutMapping("/category/{categoryId}/todos/{todoId}/change-name")
    @Operation(summary = "update Todo description from category by categoryId and todoId")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @ApiResponse(responseCode = "404", description = "Todo not found")
    public TodoDto updateTodoDescription(
            @PathVariable Long categoryId,
            @PathVariable Long todoId,
            @Valid @RequestBody UpdateTodoCommand command
    ) {
        return todoService.updateTodo(categoryId, todoId, command);
    }

    @PutMapping("/category/{categoryId}/todos/{todoId}/change-done")
    @Operation(summary = "update Todo done from category by categoryId and todoId")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @ApiResponse(responseCode = "404", description = "Todo not found")
    public TodoDto updateTodoIsDone(
            @PathVariable Long categoryId,
            @PathVariable Long todoId,
            @Valid @RequestBody DoneTodoCommand command
    ) {
        return todoService.updateDoneTodo(categoryId, todoId, command);
    }

    @DeleteMapping("/category/{categoryId}/todos/{todoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "delete Todo from category by categoryId and todoId")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @ApiResponse(responseCode = "404", description = "Todo not found")
    public void deleteTodo(
                       @PathVariable Long categoryId,
            @PathVariable Long todoId
    ) {

        todoService.deleteTodoById(categoryId, todoId);
    }

}
