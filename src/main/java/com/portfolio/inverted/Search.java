package com.portfolio.inverted;

import com.portfolio.inverted.entity.ParsedQuery;
import com.portfolio.inverted.entity.Posting;
import com.portfolio.inverted.utils.InvertedIndex;
import com.portfolio.inverted.utils.SearchTypes;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Search {

    private final InvertedIndex invertedIndex;
    private Scorer scorer;

    public Search(InvertedIndex invertedIndex, Scorer scorer) {
        this.invertedIndex = invertedIndex;
        this.scorer = scorer;
    }

    public List<Integer> initiateSearch(ParsedQuery query) {

        switch (query.getSearchType()) {
            case BOOLEAN_AND:
                return booleanAndSearch(query);
            case BOOLEAN_AND_NOT:
                return booleanAndSearch(query);
            case PHRASE_SEARCH:
                return phraseSearch(query.getTerms().get(0));
            default:
                return booleanOrSearch(query);
        }
    }


    private List<Integer> booleanOrSearch(ParsedQuery query) {
        HashMap<String, List<Posting>> documents = new HashMap<>();
        HashSet<Integer> unionIds = new HashSet<>();
        List<String> terms = query.getTerms();
        for (String term : terms) {
            unionIds.addAll(invertedIndex.getPosting(term).stream().map(Posting::getDocumentId).collect(Collectors.toSet()));
        }
        if (query.getSearchType().equals(SearchTypes.BOOLEAN_OR_NOT)) {
            booleanNotSearch(query.getNotTerms(), unionIds);
        }


        for (String term : terms) {
            List<Posting> postings = invertedIndex.getPosting(term).stream().filter(p -> unionIds.contains(p.getDocumentId())).toList();
            documents.put(term, postings);
        }

        HashMap<Integer, Double> resultstf = scorer.tfIdf(documents);
        HashMap<Integer, Double> resultsbm = scorer.bm25(documents);

        System.out.println("TF-IDF");
        resultstf.entrySet().forEach(System.out::println);
        System.out.println("BM25");
        resultsbm.entrySet().forEach(System.out::println);
        return resultstf.keySet().stream().toList();
    }

    private List<Integer> booleanAndSearch(ParsedQuery query) {
        HashSet<Integer> commonIds = new HashSet<>();
        List<String> terms = query.getTerms();
        for (String term : terms) {
            Set<Integer> documentIds = invertedIndex.getPosting(term).stream().map(Posting::getDocumentId).collect(Collectors.toSet());
            if (commonIds.isEmpty()) {
                commonIds.addAll(documentIds);
            } else {
                commonIds.retainAll(documentIds);
            }
        }

        if (commonIds.isEmpty()) return new ArrayList<>();
        HashMap<String, List<Posting>> documents = new HashMap<>();

        if (query.getSearchType().equals(SearchTypes.BOOLEAN_AND_NOT)) {
            booleanNotSearch(query.getNotTerms(), commonIds);
        }
        for (String term : terms) {
            List<Posting> postings = invertedIndex.getPosting(term).stream().filter(p -> commonIds.contains(p.getDocumentId())).toList();
            documents.put(term, postings);
        }

        HashMap<Integer, Double> resultstf = scorer.tfIdf(documents);
        HashMap<Integer, Double> resultsbm = scorer.bm25(documents);

        System.out.println("TF-IDF");
        resultstf.entrySet().forEach(System.out::println);
        System.out.println("BM25");
        resultsbm.entrySet().forEach(System.out::println);
        return resultstf.keySet().stream().toList();
    }

    private Set<Integer> booleanNotSearch(List<String> terms, Set<Integer> resultIds) {
        HashSet<Integer> notIds = new HashSet<>();
        for (String term : terms) {
            Set<Integer> documentIds = invertedIndex.getPosting(term).stream().map(Posting::getDocumentId).collect(Collectors.toSet());
            notIds.addAll(documentIds);
        }
        resultIds.removeIf(notIds::contains);
        return resultIds;
    }

    private List<Integer> phraseSearch(String query) {
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
            if (words[i].equals(anchorWord)) {
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
                    Posting p = invertedIndex.getPosting(words[i]).stream().filter(posting -> posting.getDocumentId().equals(docId)).findFirst().orElse(null);

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
