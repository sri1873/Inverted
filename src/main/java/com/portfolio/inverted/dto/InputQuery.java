package com.portfolio.inverted.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class InputQuery {
    private String query;
    private String notQuery;
    private String searchType;

}
