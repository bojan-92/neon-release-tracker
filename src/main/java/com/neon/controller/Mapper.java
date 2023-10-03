package com.neon.controller;

import com.neon.controller.dto.ReleaseDTO;
import com.neon.entity.Release;
import com.neon.entity.Status;
import com.neon.exception.InvalidStatusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
class Mapper {
    public Release toRelease(ReleaseDTO releaseDTO) {
        checkIfStatusValid(releaseDTO.getStatus());
        return Release.builder()
                .name(releaseDTO.getName())
                .description(releaseDTO.getDescription())
                .status(Status.valueOf(releaseDTO.getStatus()))
                .releaseDate(releaseDTO.getReleaseDate())
                .build();
    }

    private void checkIfStatusValid(String status) {
        if (Arrays.stream(Status.values()).noneMatch(s -> s.name().equalsIgnoreCase(status))) {
            log.error("Invalid status value: {}", status);
            throw new InvalidStatusException("Please provide a valid status");
        }
    }
}
