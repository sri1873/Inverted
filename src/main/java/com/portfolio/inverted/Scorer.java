package com.portfolio.inverted;

import com.portfolio.inverted.entity.Posting;
import com.portfolio.inverted.utils.FileIndex;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class Scorer {

    private final FileIndex fileIndex;

    public Scorer(FileIndex fileIndex) {
        this.fileIndex = fileIndex;
    }

    public HashMap<Integer, Double> tfIdf(HashMap<String, List<Posting>> documents) {
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
        resSort(scores);
        return scores;
    }

    public HashMap<Integer, Double> bm25(HashMap<String, List<Posting>> documents) {
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
        return scores;
    }


    public void resSort(HashMap<Integer, Double> scores) {

    }
}
