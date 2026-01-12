package com.portfolio.inverted.entity;

import com.portfolio.inverted.utils.SearchTypes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParsedQuery {
    private List<String> terms;

    private List<String> notTerms;
    private SearchTypes searchType;
}

