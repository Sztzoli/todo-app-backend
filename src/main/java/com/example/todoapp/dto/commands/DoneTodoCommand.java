package com.example.todoapp.dto.commands;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoneTodoCommand {

    @NotNull
    @Schema(description = "status of Todo", example = "true")
    private boolean done;
}
