package com.example.todoapp.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class TodoNotFoundException extends AbstractThrowableProblem {

    public TodoNotFoundException(Long todoId) {
        super(URI.create("todos/todo-not-found"),
                "Not found",
                Status.NOT_FOUND,
                String.format("Todo with id %d not found", todoId));
    }
}
