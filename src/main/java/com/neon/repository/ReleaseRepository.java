package com.neon.repository;

import com.neon.entity.Release;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReleaseRepository extends MongoRepository<Release, String> {

}
