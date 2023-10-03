package com.neon.service;

import com.neon.controller.dto.SearchRecord;
import com.neon.entity.Release;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public interface ReleaseService {

    Release create(Release release);

    Release update(Release release);

    Release findById(String id);

    List<Release> findBySearchRecord(SearchRecord searchRecord) throws ParseException;

    void deleteById(String id);
}
