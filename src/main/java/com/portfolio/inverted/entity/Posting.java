package com.portfolio.inverted.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Set;

@Data
@Getter
@Builder
public class Posting {
    private Integer documentId;
    private Set<Integer> position;
}
