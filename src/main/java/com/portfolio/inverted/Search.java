package com.portfolio.inverted;

import com.portfolio.inverted.entity.Posting;
import com.portfolio.inverted.utils.InvertedIndex;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Component
public class Search {

    private final InvertedIndex invertedIndex;
    private Scorer scorer;

    public Search(InvertedIndex invertedIndex, Scorer scorer) {
        this.invertedIndex = invertedIndex;
        this.scorer=scorer;
    }

    public List<Integer> termSearch(String query) {
        HashMap<String, List<Posting>> documents = new HashMap<>();
        String[] words = query.toLowerCase().split(" ");

        for (String word : words) {
            if (invertedIndex.containsTerm(word)) {
                documents.put(word, invertedIndex.getPosting(word));
            }
        }

        HashMap<Integer, Double> results = scorer.tfIdf(documents);

        results.entrySet().forEach(System.out::println);
        return results.keySet().stream().toList();
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
