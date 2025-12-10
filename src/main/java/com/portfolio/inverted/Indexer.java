package com.portfolio.inverted;

import com.portfolio.inverted.entity.Posting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Indexer {

  private static Logger log= LoggerFactory.getLogger(Indexer.class);
    HashMap<String,List<Posting>>index=new HashMap<>();
    public void createIndex(List<String> document, Integer docId,Integer position) {
        for (String term : document) {
            List<Posting> postings = index.computeIfAbsent(term, k -> new ArrayList<>());
            Posting posting = postings.stream().filter(p -> p.getDocumentId().equals(docId)).findFirst().orElse(null);
            if (posting == null) {
                posting = Posting.builder().documentId(docId).position(new ArrayList<>()).build();
                postings.add(posting);
            }
            posting.getPosition().add(position);
            position++;
        }
    }

    public void printIndex(){
        log.info("PRINT INDEX");
        for(Map.Entry<String, List<Posting>> entry: index.entrySet()){
            log.info(entry.getKey() + ":"+ entry.getValue());
        }
    }
}
