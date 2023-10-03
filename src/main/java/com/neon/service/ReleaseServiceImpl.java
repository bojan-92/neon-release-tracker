package com.neon.service;

import com.neon.controller.dto.SearchRecord;
import com.neon.entity.Release;
import com.neon.exception.InvalidStatusException;
import com.neon.exception.ResourceNotFoundException;
import com.neon.repository.ReleaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReleaseServiceImpl implements ReleaseService {

    private final ReleaseRepository repository;
    private final MongoTemplate mongoTemplate;

    public ReleaseServiceImpl(MongoTemplate mongoTemplate, ReleaseRepository repository) {
        this.mongoTemplate = mongoTemplate;
        this.repository = repository;
    }

    @Override
    public Release create(Release release) throws InvalidStatusException {
        release.setCreatedAt(LocalDateTime.now());
        release.setLastUpdatedAt(LocalDateTime.now());
        Release createdRelease = repository.save(release);

        log.info("Release: {} successfully created", createdRelease);
        return createdRelease;
    }

    @Override
    public Release update(Release release) {
        Optional<Release> optionalRelease = repository.findById(release.getId());
        if (optionalRelease.isPresent()) {
            Release updatedRelease = optionalRelease.get();
            updatedRelease.setName(release.getName());
            updatedRelease.setDescription(release.getDescription());
            updatedRelease.setStatus(release.getStatus());
            updatedRelease.setReleaseDate(release.getReleaseDate());
            updatedRelease.setLastUpdatedAt(LocalDateTime.now());
            repository.save(updatedRelease);

            log.info("Release: {} successfully updated", release);
            return updatedRelease;
        } else {
            log.error("Release with id: {} not found", release.getId());
            throw new ResourceNotFoundException("Release with id: " + release.getId() + " not found");
        }
    }

    @Override
    public Release findById(String id) {
        Optional<Release> optionalRelease = repository.findById(id);
        if (optionalRelease.isEmpty()) {
            log.error("Release with id: {} not found", id);
            throw new ResourceNotFoundException("Release with id: " + id + " not found");
        }
        log.info("Release found: {}", optionalRelease.get());
        return optionalRelease.get();
    }

    @Override
    public List<Release> findBySearchRecord(SearchRecord searchRecord) {
        Criteria criteria = new Criteria();
        if (searchRecord.getName() != null) {
            criteria = criteria.and("name").is(searchRecord.getName());
        }
        if (searchRecord.getDescription() != null) {
            criteria = criteria.and("description").is(searchRecord.getDescription());
        }
        if (searchRecord.getStatus() != null) {
            criteria = criteria.and("status").is(searchRecord.getStatus());
        }
        criteria = createCriteriaForDateFields(searchRecord, criteria);

        Query query = new Query(criteria);
        return mongoTemplate.find(query, Release.class);
    }

    @Override
    public void deleteById(String id) {
        Optional<Release> optionalRelease = repository.findById(id);
        if (optionalRelease.isEmpty()) {
            log.error("Release with id: {} not found", id);
            throw new ResourceNotFoundException("Release with id: " + id + " not found");
        }
        repository.deleteById(id);
        log.info("Release with id: {} successfully deleted", id);
    }

    private Criteria createCriteriaForDateFields(SearchRecord searchRecord, Criteria criteria) {
        if (searchRecord.getReleaseDateFrom() != null && searchRecord.getReleaseDateTo() != null) {
            criteria = criteria.andOperator(new Criteria().orOperator(
                    Criteria.
                            where("releaseDate")
                            .gte(formatStringToDate(searchRecord.getReleaseDateFrom()))
                            .lte(formatStringToDate(searchRecord.getReleaseDateTo()))));
        } else if (searchRecord.getReleaseDateFrom() != null) {
            criteria = criteria.and("releaseDate").gte(formatStringToDate(searchRecord.getReleaseDateFrom()));
        } else if (searchRecord.getReleaseDateTo() != null) {
            criteria = criteria.and("releaseDate").lte(formatStringToDate(searchRecord.getReleaseDateTo()));
        }
        if (searchRecord.getCreatedFrom() != null && searchRecord.getCreatedTo() != null) {
            criteria = criteria.andOperator(new Criteria().orOperator(
                    Criteria
                            .where("createdAt")
                            .gte(formatStringToDate(searchRecord.getCreatedFrom()))
                            .lte(formatStringToDate(searchRecord.getCreatedTo()))));
        } else if (searchRecord.getCreatedFrom() != null) {
            criteria = criteria.and("createdAt").gte(formatStringToDate(searchRecord.getCreatedFrom()));
        } else if (searchRecord.getCreatedTo() != null) {
            criteria = criteria.and("createdAt").lte(formatStringToDate(searchRecord.getCreatedTo()));
        }
        return criteria;
    }

    private Date formatStringToDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
