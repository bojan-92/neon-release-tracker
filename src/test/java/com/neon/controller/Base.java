package com.neon.controller;

import com.neon.NeonReleaseTrackerApplication;
import com.neon.controller.dto.ReleaseDTO;
import com.neon.repository.ReleaseRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {NeonReleaseTrackerApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class Base {

    public static final String BASIC_ENDPOINT = "/v1/releases";

    @LocalServerPort
    int port;

    @Autowired
    public ReleaseRepository repository;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    @AfterEach
    public void clearData() {
        repository.deleteAll();
    }

    public Map<String, Object> getRequestBody(ReleaseDTO releaseDTO) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", releaseDTO.getName());
        requestBody.put("description", releaseDTO.getDescription());
        requestBody.put("status", releaseDTO.getStatus());
        requestBody.put("releaseDate", releaseDTO.getReleaseDate());
        return requestBody;
    }
}
