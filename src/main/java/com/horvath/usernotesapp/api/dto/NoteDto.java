package com.horvath.usernotesapp.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NoteDto {

    private Long id;

    @NotBlank(message = "Note text cannot be blank")
    @Size(max = 1000, message = "Note text must not exceed 1000 characters")
    private String text;

    @NotNull(message = "Note completed field must be set")
    private Boolean completed;

}
