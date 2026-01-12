package com.portfolio.inverted;

import com.portfolio.inverted.dto.InputQuery;
import com.portfolio.inverted.entity.ParsedQuery;
import com.portfolio.inverted.utils.SearchTypes;

import java.util.Arrays;
import java.util.List;

public class QueryParser {

    public ParsedQuery parseQuery(InputQuery query) {

        ParsedQuery parsedQuery = new ParsedQuery();
        parsedQuery.setSearchType(SearchTypes.valueOf(query.getSearchType()));
        parsedQuery.setTerms(Arrays.stream(query.getQuery().toLowerCase().split(" ")).toList());

        if (!query.getNotQuery().isEmpty() && (query.getSearchType().equalsIgnoreCase("BOOLEAN_OR_NOT") || query.getSearchType().equalsIgnoreCase("BOOLEAN_AND_NOT"))) {
            parsedQuery.setNotTerms(Arrays.stream(query.getNotQuery().toLowerCase().split(" ")).toList());
        }
        return parsedQuery;
    }


}
