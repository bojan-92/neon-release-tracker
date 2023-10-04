package com.neon.controller;

import com.neon.controller.dto.ReleaseDTO;
import com.neon.controller.dto.SearchRecord;
import com.neon.entity.Release;
import com.neon.service.ReleaseService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@OpenAPIDefinition(info = @Info(title = "Releases API", version = "1.0", description = "Releases Information"))
@RestController
@RequestMapping("/v1/releases")
@Slf4j
public class ReleaseTrackerController {

    private final ReleaseService releaseService;
    private final Mapper mapper;

    public ReleaseTrackerController(ReleaseService releaseService, Mapper mapper) {
        this.releaseService = releaseService;
        this.mapper = mapper;
    }

    @Operation(summary = "Get a list of Releases by search params")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Releases",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid search data supplied")
    })
    @GetMapping
    private ResponseEntity<List<Release>> getReleases(@RequestParam(required = false) String name,
                                                      @RequestParam(required = false) String description,
                                                      @RequestParam(required = false) String status,
                                                      @RequestParam(required = false) String releaseDateFrom,
                                                      @RequestParam(required = false) String releaseDateTo,
                                                      @RequestParam(required = false) String createdFrom,
                                                      @RequestParam(required = false) String createdTo) throws ParseException {
        return new ResponseEntity<>(releaseService.findBySearchRecord(SearchRecord.builder()
                .name(name)
                .description(description)
                .status(status)
                .releaseDateFrom(releaseDateFrom)
                .releaseDateTo(releaseDateTo)
                .createdFrom(createdFrom)
                .createdTo(createdTo)
                .build()), HttpStatus.OK);
    }

    @Operation(summary = "Get a Release by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Release",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Release.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Release not found")
    })
    @GetMapping("/{id}")
    private ResponseEntity<Release> getRelease(@Parameter(description = "id of Release to be searched") @PathVariable String id) {
        return new ResponseEntity<>(releaseService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "Create a new Release")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Release successfully created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Release.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data supplied")
    })
    @PostMapping
    private ResponseEntity<Release> createRelease(@Valid @RequestBody ReleaseDTO releaseDto) {
        Release release = mapper.toRelease(releaseDto);
        return new ResponseEntity<>(releaseService.create(release), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing Release")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Release successfully updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Release.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data supplied"),
            @ApiResponse(responseCode = "404", description = "Release not found")
    })
    @PutMapping("/{id}")
    private ResponseEntity<Release> updateRelease(@PathVariable String id, @Valid @RequestBody ReleaseDTO releaseDTO) {
        Release release = mapper.toRelease(releaseDTO);
        release.setId(id);
        return new ResponseEntity<>(releaseService.update(release), HttpStatus.OK);
    }

    @Operation(summary = "Delete an existing Release")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Release successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Release not found")
    })
    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteRelease(@PathVariable String id) {
        releaseService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
