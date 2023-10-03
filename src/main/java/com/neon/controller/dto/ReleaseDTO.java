package com.neon.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseDTO {

    @NotBlank(message = "Please provide a valid name")
    public String name;

    @NotBlank(message = "Please provide a valid description")
    private String description;

    @NotBlank(message = "Please provide a valid status")
    private String status;

    @NotNull(message = "Please provide a valid release date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate releaseDate;
}
