package com.portfolio.inverted.controller;

import com.portfolio.inverted.dto.InputQuery;
import com.portfolio.inverted.dto.SearchResponse;
import com.portfolio.inverted.entity.ParsedQuery;
import com.portfolio.inverted.service.QueryParser;
import com.portfolio.inverted.service.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    @Autowired
    private QueryParser queryParser;
    @Autowired
    private Search search;

    @GetMapping("/search")
    public List<SearchResponse> search(InputQuery inputQuery) {
        ParsedQuery parsedQuery = queryParser.parseQuery(inputQuery);
        return search.initiateSearch(parsedQuery);
    }
}
