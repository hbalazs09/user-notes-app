package com.horvath.usernotesapp.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

    private Long id;

    @NotBlank(message = "User name cannot be blank")
    @Size(max = 50, message = "User name must not exceed 50 characters")
    private String name;
}
