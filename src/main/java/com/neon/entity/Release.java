package com.neon.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
@ToString
@Document("releases")
public class Release {

    @Id
    private String id;
    private String name;
    private String description;
    private Status status;
    private LocalDate releaseDate;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
}
