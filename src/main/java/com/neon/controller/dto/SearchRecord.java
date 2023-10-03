package com.neon.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchRecord {
    private String name;
    private String description;
    private String status;
    private String releaseDateFrom;
    private String releaseDateTo;
    private String createdFrom;
    private String createdTo;
}
