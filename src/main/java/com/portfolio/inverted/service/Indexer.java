package com.portfolio.inverted.service;

import com.portfolio.inverted.entity.Posting;
import com.portfolio.inverted.utils.InvertedIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
public class Indexer {

    private static Logger log = LoggerFactory.getLogger(Indexer.class);

    private final InvertedIndex invertedIndex;

    public Indexer(InvertedIndex invertedIndex) {
        this.invertedIndex = invertedIndex;
    }

    public void createIndex(List<String> document, Integer docId) {
        Map<String, List<Posting>> index = invertedIndex.getIndex();
        Integer position = 0;
        for (String term : document) {
            List<Posting> postings = index.computeIfAbsent(term, k -> new ArrayList<>());
            Posting posting = postings.stream().filter(p -> p.getDocumentId().equals(docId)).findFirst().orElse(null);
            if (posting == null) {
                posting = Posting.builder().termFrequency(0).documentLength(document.size()).documentId(docId).position(new HashSet<>()).build();
                postings.add(posting);
            }
            posting.getPosition().add(position);
            posting.setTermFrequency(posting.getTermFrequency() + 1);
            position++;
        }
    }

    public void printIndex() {
        log.info("PRINT INDEX");
        for (Map.Entry<String, List<Posting>> entry : invertedIndex.getIndex().entrySet()) {
            log.info(entry.getKey() + ":" + entry.getValue());
        }
    }

}
