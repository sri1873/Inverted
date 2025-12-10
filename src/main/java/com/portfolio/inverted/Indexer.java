package com.portfolio.inverted;

import com.portfolio.inverted.entity.Posting;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Indexer {

//  private static Logger log= LoggerFactory.getLogger(Indexer.class);
    HashMap<String,List<Posting>>index=new HashMap<>();
    public void createIndex(List<String> document, Integer docId,Integer position) {
//      log.info("In Create Index");
        for (int i = 0; i < document.size(); i++) {
            List<Posting> postings = index.computeIfAbsent(document.get(i), k -> new ArrayList<>());
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
        for(String key: index.keySet()){
            System.out.println(key + ":"+ index.get(key));
        }
    }
}
