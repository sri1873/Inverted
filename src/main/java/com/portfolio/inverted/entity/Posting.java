package com.portfolio.inverted.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Set;

@Data
@Getter
@Builder
public class Posting implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer documentId;
    private Set<Integer> position;
    private Integer termFrequency;
    private Integer documentLength;
}
