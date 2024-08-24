package com.rutaaprendizaje.webflux.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SaveTechnologyRequest {

    @NotBlank(message = "Name cannot be blank")
    @Length(max = 50, message = "Name must have less than 51 characters")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    @Length(max = 90, message = "Description must have less than 91 characters")
    private String description;
}
