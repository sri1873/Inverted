package com.portfolio.inverted.service;

import com.portfolio.inverted.dto.SearchResponse;
import com.portfolio.inverted.entity.Posting;
import com.portfolio.inverted.utils.FileIndex;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Scorer {

    private final FileIndex fileIndex;

    public Scorer(FileIndex fileIndex) {
        this.fileIndex = fileIndex;
    }

    public List<SearchResponse> tfIdf(HashMap<String, List<Posting>> documents) {
        HashMap<Integer, Double> scores = new HashMap<>();
        for (String term : documents.keySet()) {
            List<Posting> postings = documents.get(term);
            double df = postings.size();
            double idf = Math.log10(fileIndex.getTotalDocuments() / df);
            for (Posting p : postings) {
                double tfidf = p.getTermFrequency() * idf;
                scores.put(p.getDocumentId(), scores.getOrDefault(p.getDocumentId(), 0.0) + tfidf);
            }
        }

        List<SearchResponse> result = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : scores.entrySet()) {
            SearchResponse searchResponse = SearchResponse.builder().score(entry.getValue()).documentId(entry.getKey()).build();
            result.add(searchResponse);
        }
        return result;

    }

    public List<SearchResponse> bm25(HashMap<String, List<Posting>> documents) {
        HashMap<Integer, Double> scores = new HashMap<>();

        double k1 = 1.5;
        double b = 0.75;

        for (String term : documents.keySet()) {
            List<Posting> postings = documents.get(term);
            double df = postings.size();
            double idf = Math.log10((fileIndex.getTotalDocuments() - df + 0.5) / (df + 0.5));

            for (Posting p : postings) {
                int tf = p.getTermFrequency();
                double bm25 = idf * ((tf * (k1 + 1)) / (tf + (k1 * (1 - b + b * ((double) p.getDocumentLength() / fileIndex.getAverageDocumentLength())))));
                scores.put(p.getDocumentId(), scores.getOrDefault(p.getDocumentId(), 0.0) + bm25);
            }

        }
        List<SearchResponse> result = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : scores.entrySet()) {
            SearchResponse searchResponse = SearchResponse.builder().score(entry.getValue()).documentId(entry.getKey()).build();
            result.add(searchResponse);
        }
        return result;
    }


    public void resSort(HashMap<Integer, Double> scores) {

    }
}
