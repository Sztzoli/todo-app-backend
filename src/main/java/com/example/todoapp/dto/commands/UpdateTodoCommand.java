package com.example.todoapp.dto.commands;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTodoCommand {

    @NotBlank
    @Schema(description = "description of Todo", example = "Learn C#")
    private String description;
}
