package com.neon.controller;

import com.neon.controller.dto.ReleaseDTO;
import com.neon.controller.dto.SearchRecord;
import com.neon.entity.Release;
import com.neon.service.ReleaseService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

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

    @GetMapping("/{id}")
    private ResponseEntity<Release> getRelease(@PathVariable String id) {
        return new ResponseEntity<>(releaseService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    private ResponseEntity<Release> createRelease(@Valid @RequestBody ReleaseDTO releaseDto) {
        Release release = mapper.toRelease(releaseDto);
        return new ResponseEntity<>(releaseService.create(release), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    private ResponseEntity<Release> updateRelease(@PathVariable String id, @Valid @RequestBody ReleaseDTO releaseDTO) {
        Release release = mapper.toRelease(releaseDTO);
        release.setId(id);
        return new ResponseEntity<>(releaseService.update(release), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteRelease(@PathVariable String id) {
        releaseService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
