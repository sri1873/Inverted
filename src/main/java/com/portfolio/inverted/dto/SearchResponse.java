package com.portfolio.inverted.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SearchResponse {
    private Integer documentId;
    private String title;
    private Double score;
    private Integer position;
}
