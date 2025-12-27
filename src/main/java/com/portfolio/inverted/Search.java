package com.portfolio.inverted;

import com.portfolio.inverted.entity.Posting;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class Search {

    private final InvertedIndex invertedIndex;

    public Search(InvertedIndex invertedIndex) {
        this.invertedIndex = invertedIndex;
    }


    public List<Integer> search(String query) {
        HashSet<Integer> documents = new HashSet<>();
        String[] words = query.toLowerCase().split(" ");
        for (String word : words) {
            if (invertedIndex.containsTerm(word)) {
                List<Posting> postings = invertedIndex.getPosting(word);
                for (Posting p : postings) {
                    documents.add(p.getDocumentId());
                }
            }
        }
        return documents.stream().toList();
    }

    public List<Integer> phraseSearch(String query) {
        String[] words = query.toLowerCase().split(" ");
        String anchorWord = words[0];
        for (String word : words) {
            if (invertedIndex.containsTerm(word)) {
                if (invertedIndex.getPosting(word).size() < invertedIndex.getPosting(anchorWord).size())
                    anchorWord = word;
            } else return new ArrayList<>();
        }
        int anchorIndex = -1;
        for (int i = 0; i < words.length; i++) {
            if (words[i] == anchorWord) {
                anchorIndex = i;
                break;
            }
        }
        HashSet<Integer> documents = new HashSet<>();
        for (Posting anchorPosting : invertedIndex.getPosting(anchorWord)) {
            Integer docId = anchorPosting.getDocumentId();

            for (int pos : anchorPosting.getPosition()) {
                boolean match = true;
                for (int i = 0; i < words.length; i++) {
                    if (i == anchorIndex) continue;
                    int requiredPos = pos + (i - anchorIndex);
                    Posting p = invertedIndex.getPosting(words[i])
                            .stream()
                            .filter(posting -> posting.getDocumentId().equals(docId))
                            .findFirst().orElse(null);

                    if (p == null || !p.getPosition().contains(requiredPos)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    documents.add(docId);
                    break;
                }
            }

        }

        return documents.stream().toList();
    }
}
