package com.portfolio.inverted.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import lombok.Getter;

import java.util.List;

@Data
@Getter
@Builder
public class Posting {
    private Integer documentId;
    private List<Integer> position;
}
