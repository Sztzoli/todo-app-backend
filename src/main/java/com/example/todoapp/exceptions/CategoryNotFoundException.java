package com.example.todoapp.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class CategoryNotFoundException extends AbstractThrowableProblem {

    public CategoryNotFoundException(Long categoryId) {
        super(URI.create("todos/category-not-found"),
                "Not found",
                Status.NOT_FOUND,
                String.format("Category with id %d not found", categoryId));
    }
}
