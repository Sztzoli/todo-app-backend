package com.example.todoapp.services;

import com.example.todoapp.dto.CategoryDto;
import com.example.todoapp.dto.TodoDto;
import com.example.todoapp.dto.commands.*;
import com.example.todoapp.exceptions.CategoryNotFoundException;
import com.example.todoapp.exceptions.TodoNotFoundException;
import com.example.todoapp.model.Category;
import com.example.todoapp.model.Todo;
import com.example.todoapp.repositories.CategoryRepository;
import com.example.todoapp.repositories.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class TodoService {

    private final CategoryRepository categoryRepository;
    private final TodoRepository todoRepository;
    private final ModelMapper mapper;

    public CategoryDto saveCategory( SaveCategoryCommand command) {
        log.info(String.format("save new category: %s", command.getDescription()));
        Category category = new Category(command.getDescription());
        return mapper.map(categoryRepository.save(category), CategoryDto.class);
    }

    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll().stream().map(c -> mapper.map(c, CategoryDto.class)).collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long categoryId) {
        Category category = getCategory(categoryId);
        return mapper.map(category, CategoryDto.class);
    }

    @Transactional
    public void deleteCategoryById( Long categoryId) {
        log.info(String.format("delete category by id: %d", categoryId));
        categoryRepository.deleteById(categoryId);
    }

    @Transactional
    public CategoryDto updateCategory( Long categoryId, UpdateCategoryCommand command) {
        Category category = getCategory(categoryId);
        category.setDescription(command.getDescription());
        log.info(String.format("update category by id: %d, change is : %s", categoryId, command.getDescription()));
        return mapper.map(category, CategoryDto.class);
    }

    @Transactional
    public CategoryDto addTodo( Long id, SaveTodoCommand command) {
        Category category = getCategory(id);
        Todo todo = todoRepository.save(new Todo(command.getDescription()));
        category.addTodo(todo);
        log.info(String.format("Add todo: %s, categoryId: %d", command.getDescription(), id));
        return mapper.map(category, CategoryDto.class);
    }


    @Transactional
    public TodoDto updateDoneTodo(Long categoryId, Long todoId, DoneTodoCommand command) {
        Category category = getCategory(categoryId);
        Todo todo = getTodoById(todoId, category);
        todo.setDone(command.isDone());
        log.info(String.format("update todo: %d,todo done: %s, categoryId: %d", todoId,
                command.isDone(), categoryId));
        return mapper.map(todo, TodoDto.class);
    }


    @Transactional
    public void deleteTodoById( Long categoryId, Long todoId) {
        Category category = getCategory(categoryId);
        Todo todo = getTodoById(todoId, category);
        category.getTodos().remove(todo);
        log.info(String.format("delete todo by id: %d, categoryId: %d", todoId, categoryId));
    }

    public List<TodoDto> getTodos(Long categoryId) {
        Category category = getCategory(categoryId);
        return category.getTodos().stream().map(t -> mapper.map(t, TodoDto.class)).collect(Collectors.toList());
    }


    public TodoDto getTodo(Long categoryId, Long todoId) {
        Category category = getCategory( categoryId);
        Todo todo = getTodoById(todoId, category);
        return mapper.map(todo, TodoDto.class);
    }

    @Transactional
    public TodoDto updateTodo( Long categoryId, Long todoId, UpdateTodoCommand command) {
        Category category = getCategory(categoryId);
        Todo todo = getTodoById(todoId, category);
        todo.setDescription(command.getDescription());
        log.info(String.format("update todo: %d,todo change: %s, categoryId: %d",
                todoId, command.getDescription(), categoryId));
        return mapper.map(todo, TodoDto.class);
    }

    private Category getCategory(Long id) {
        return categoryRepository.findCategoryId(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    private Todo getTodoById(Long todoId, Category category) {
        return category.getTodos().stream()
                .filter(t -> t.getId().equals(todoId)).findFirst().orElseThrow(() -> new TodoNotFoundException(todoId));
    }
}
