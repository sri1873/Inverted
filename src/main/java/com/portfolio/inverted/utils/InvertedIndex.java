package com.portfolio.inverted.utils;

import com.portfolio.inverted.entity.Posting;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InvertedIndex {
    private final Map<String, List<Posting>> index = new HashMap<>();

    public Map<String, List<Posting>> getIndex() {
        return index;
    }

    public boolean containsTerm(String term) {
        return index.containsKey(term);
    }

    public List<Posting> getPosting(String term) {
        return index.get(term);
    }
}
