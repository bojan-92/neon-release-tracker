package com.neon.repository;

import com.neon.controller.dto.SearchRecord;
import com.neon.entity.Release;
import com.neon.entity.Status;
import com.neon.exception.ResourceNotFoundException;
import com.neon.service.ReleaseService;
import com.neon.service.ReleaseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers
@ContextConfiguration(classes = {MongoDBTestContainerConfig.class, ReleaseServiceImpl.class})
@RunWith(SpringRunner.class)
class ReleaseRepositoryIntegrationTest {

    @Autowired
    ReleaseRepository repository;

    @Autowired
    ReleaseService releaseService;

    @Test
    void givenUserExists_whenFindById_thenGetUser() {
        Release release = Release.builder()
                .id("1abc")
                .name("ReleaseV1")
                .description("This is the Release version 1")
                .status(Status.CREATED)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build();
        repository.save(release);

        Release releaseFound = releaseService.findById("1abc");
        assertNotNull(releaseFound);
        assertEquals("ReleaseV1", releaseFound.getName());
    }

    @Test
    void givenUserNotExists_whenFindById_thenThrowsResourceNotFoundException() {
        Release release = Release.builder()
                .id("1abc")
                .name("ReleaseV1")
                .description("This is the Release version 1")
                .status(Status.CREATED)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build();
        repository.save(release);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> releaseService.findById("2efg")
        );
        assertTrue(thrown.getMessage().contains("Release with id: 2efg not found"));
    }

    @Test
    void givenSearchRecord_whenFindBySearchRecord_thenGet2Users() throws ParseException {
        repository.save(Release.builder()
                .id("1abc")
                .name("ReleaseV1")
                .description("This is the Release version 1")
                .status(Status.CREATED)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build());

        repository.save(Release.builder()
                .id("1efg")
                .name("ReleaseV1")
                .description("This is the Release version 2")
                .status(Status.DONE)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build());

        repository.save(Release.builder()
                .id("1ghd")
                .name("ReleaseV2")
                .description("This is the Release version 3")
                .status(Status.ON_DEV)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build());

        List<Release> releasesFound = releaseService.findBySearchRecord(SearchRecord.builder().name("ReleaseV1").build());

        assertEquals(releasesFound.size(), 2);
    }

    @Test
    void givenSearchRecord_whenFindBySearchRecord_thenGet1User() throws ParseException {
        repository.save(Release.builder()
                .id("1abc")
                .name("ReleaseV1")
                .description("This is the Release version 1")
                .status(Status.CREATED)
                .releaseDate(LocalDate.of(2023, 8, 10))
                .build());

        repository.save(Release.builder()
                .id("1efg")
                .name("ReleaseV2")
                .description("This is the Release version 2")
                .status(Status.DONE)
                .releaseDate(LocalDate.of(2023, 9, 20))
                .build());

        repository.save(Release.builder()
                .id("1ghd")
                .name("ReleaseV2")
                .description("This is the Release version 3")
                .status(Status.ON_DEV)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build());

        List<Release> releasesFound = releaseService.findBySearchRecord(SearchRecord.builder()
                .name("ReleaseV2")
                .status(Status.DONE.name())
                .releaseDateFrom("2023-08-30")
                .releaseDateTo("2023-10-21")
                .build());

        assertEquals(releasesFound.size(), 1);
    }

    @Test
    void givenSearchRecord_whenFindBySearchRecord_thenReturnEmptyList() throws ParseException {
        repository.save(Release.builder()
                .id("1abc")
                .name("ReleaseV1")
                .description("This is the Release version 1")
                .status(Status.CREATED)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build());

        repository.save(Release.builder()
                .id("1efg")
                .name("ReleaseV2")
                .description("This is the Release version 2")
                .status(Status.DONE)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build());

        repository.save(Release.builder()
                .id("1ghd")
                .name("ReleaseV3")
                .description("This is the Release version 3")
                .status(Status.ON_DEV)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build());

        List<Release> releasesFound = releaseService.findBySearchRecord(SearchRecord.builder()
                .name("ReleaseV2")
                .status(Status.QA_DONE_ON_STAGING.name())
                .releaseDateFrom("2023-08-30")
                .releaseDateTo("2023-10-21")
                .build());

        assertEquals(releasesFound.size(), 0);
    }

    @Test
    void should_create_release_when_provided_Release_details() {
        Release createdRelease = Release.builder()
                .id("1abc")
                .name("ReleaseV1")
                .description("This is the Release version 1")
                .status(Status.CREATED)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build();

        releaseService.create(createdRelease);
        Release releaseFound = releaseService.findById("1abc");
        assertTrue(createdRelease.getId().equalsIgnoreCase(releaseFound.getId()));
    }

    @Test
    void should_update_release_when_provided_update_Release_details() {
        Release createdRelease = Release.builder()
                .id("1abc")
                .name("ReleaseV1")
                .description("This is the Release version 1")
                .status(Status.CREATED)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build();

        repository.save(createdRelease);

        Release releaseToUpdate = Release.builder()
                .id("1abc")
                .name("ReleaseV2")
                .description("This is the Release version 2")
                .status(Status.QA_DONE_ON_DEV)
                .releaseDate(LocalDate.of(2023, 12, 1))
                .build();

        Release updatededRelease = releaseService.update(releaseToUpdate);

        assertNotNull(updatededRelease);
        assertEquals(updatededRelease.getId(), createdRelease.getId());
        assertEquals(updatededRelease.getName(), "ReleaseV2");
        assertEquals(updatededRelease.getDescription(), "This is the Release version 2");
        assertEquals(updatededRelease.getStatus(), Status.QA_DONE_ON_DEV);
    }

    @Test
    void should_not_update_release_when_provided_wrong_Release_id() {
        Release createdRelease = Release.builder()
                .id("1abc")
                .name("ReleaseV1")
                .description("This is the Release version 1")
                .status(Status.CREATED)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build();

        repository.save(createdRelease);

        Release releaseToUpdate = Release.builder()
                .id("2efg")
                .name("ReleaseV2")
                .description("This is the Release version 2")
                .status(Status.QA_DONE_ON_DEV)
                .releaseDate(LocalDate.of(2023, 12, 1))
                .build();

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> releaseService.update(releaseToUpdate)
        );
        assertTrue(thrown.getMessage().contains("Release with id: 2efg not found"));
    }

    @Test
    void should_delete_release_when_provided_Release_id() {
        Release createdRelease = Release.builder()
                .id("1abc")
                .name("ReleaseV1")
                .description("This is the Release version 1")
                .status(Status.CREATED)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build();
        repository.save(createdRelease);

        releaseService.deleteById(createdRelease.getId());
        Optional<Release> deletedRelease = repository.findById(createdRelease.getId());

        assertTrue(deletedRelease.isEmpty());
    }

    @Test
    void should_not_delete_release_when_provided_wrong_Release_id() {
        Release createdRelease = Release.builder()
                .id("1abc")
                .name("ReleaseV1")
                .description("This is the Release version 1")
                .status(Status.CREATED)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build();
        repository.save(createdRelease);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> releaseService.deleteById("2efg")
        );
        assertTrue(thrown.getMessage().contains("Release with id: 2efg not found"));
    }
}
