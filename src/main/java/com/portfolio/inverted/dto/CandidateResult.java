package com.portfolio.inverted.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CandidateResult {

    Integer documentId;
    Integer termFrequency;
    String term;
}
