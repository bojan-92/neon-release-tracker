package com.neon.service;

import com.neon.entity.Release;
import com.neon.entity.Status;
import com.neon.exception.ResourceNotFoundException;
import com.neon.repository.ReleaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReleaseServiceUnitTest {

    @Mock
    private ReleaseRepository repository;

    @InjectMocks
    private ReleaseServiceImpl releaseService;

    private Release release;

    @BeforeEach
    public void setup() {
        release = Release.builder()
                .id("1abc")
                .name("ReleaseV1")
                .description("This is the Release version 1")
                .status(Status.CREATED)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build();
    }

    @Test
    void givenReleaseToAddShouldReturnAddedRelease() {
        // given
        given(repository.findById(release.getId()))
                .willReturn(Optional.empty());

        given(repository.save(release)).willReturn(release);

        // when
        Release savedRelease = releaseService.create(release);

        // then
        assertThat(savedRelease).isNotNull();
    }

    @Test
    void givenExistingReleaseToUpdateShouldReturnUpdatedRelease() {
        // given
        given(repository.findById(release.getId()))
                .willReturn(Optional.of(release));

        Release updatedRelease = Release.builder()
                .id("1abc")
                .name("ReleaseV2")
                .description("This is the Release version 2")
                .status(Status.DONE)
                .releaseDate(LocalDate.of(2023, 10, 22))
                .build();

        // when
        updatedRelease = releaseService.update(updatedRelease);

        // then
        assertThat(updatedRelease.getName()).isEqualTo("ReleaseV2");
        assertThat(updatedRelease.getDescription()).isEqualTo("This is the Release version 2");
        assertThat(updatedRelease.getStatus()).isEqualTo(Status.DONE);
        LocalDateTime time = LocalDateTime.now();
        assertThat(updatedRelease.getLastUpdatedAt().getYear()).isEqualTo(time.getYear());
        assertThat(updatedRelease.getLastUpdatedAt().getMonthValue()).isEqualTo(time.getMonthValue());
        assertThat(updatedRelease.getLastUpdatedAt().getDayOfMonth()).isEqualTo(time.getDayOfMonth());
    }

    @Test
    void givenNonExistingReleaseToUpdateShouldThrowsException() {
        // given
        given(repository.findById(release.getId()))
                .willReturn(Optional.of(release));

        Release updatedRelease = Release.builder()
                .id("1abcd")
                .name("ReleaseV2")
                .description("This is the Release version 2")
                .status(Status.DONE)
                .releaseDate(LocalDate.of(2023, 10, 22))
                .build();

        // when
        assertThrows(ResourceNotFoundException.class, () -> {
            releaseService.update(updatedRelease);
        });

        // then
        verify(repository, never()).save(any(Release.class));
    }

    @Test
    void givenExistingReleaseIdToDelete() {
        // given
        given(repository.findById(release.getId()))
                .willReturn(Optional.of(release));

        // when
        releaseService.deleteById(release.getId());

        // then
        verify(repository, times(1)).deleteById(release.getId());
    }

    @Test
    void givenNonExistingReleaseIdToDeleteShouldThrowsException() {
        // given
        Release releaseToDelete = Release.builder()
                .id("1efg")
                .name("ReleaseV5")
                .description("This is the Release version 5")
                .status(Status.DONE)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build();

        // when
        assertThrows(ResourceNotFoundException.class, () -> {
            releaseService.deleteById(releaseToDelete.getId());
        });

        // then
        verify(repository, never()).deleteById(releaseToDelete.getId());
    }

    @Test
    public void givenExistingReleaseIdShouldReturnRelease() {
        // given
        given(repository.findById(release.getId())).willReturn(Optional.of(release));

        // when
        Release foundRelease = releaseService.findById(release.getId());

        // then
        assertThat(foundRelease).isNotNull();
    }

    @Test
    public void givenNonExistingReleaseIdShouldThrowsException() {
        // given
        Release releaseToFind = Release.builder()
                .id("1efg")
                .name("ReleaseV5")
                .description("This is the Release version 5")
                .status(Status.DONE)
                .releaseDate(LocalDate.of(2023, 10, 20))
                .build();

        // when
        assertThrows(ResourceNotFoundException.class, () -> {
            releaseService.findById(releaseToFind.getId());
        });

        // then
        verify(repository, times(1)).findById(releaseToFind.getId());
    }
}
