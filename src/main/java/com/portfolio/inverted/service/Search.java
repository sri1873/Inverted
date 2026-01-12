package com.portfolio.inverted.service;

import com.portfolio.inverted.dto.SearchResponse;
import com.portfolio.inverted.entity.ParsedQuery;
import com.portfolio.inverted.entity.Posting;
import com.portfolio.inverted.utils.InvertedIndex;
import com.portfolio.inverted.utils.SearchTypes;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class Search {

    private final InvertedIndex invertedIndex;
    private Scorer scorer;

    public Search(InvertedIndex invertedIndex, Scorer scorer) {
        this.invertedIndex = invertedIndex;
        this.scorer = scorer;
    }

    public List<SearchResponse> initiateSearch(ParsedQuery query) {

        switch (query.getSearchType()) {
            case BOOLEAN_AND, BOOLEAN_AND_NOT:
                return booleanAndSearch(query);
            case PHRASE_SEARCH:
                return phraseSearch(query);
            default:
                return booleanOrSearch(query);
        }
    }


    private List<SearchResponse> booleanOrSearch(ParsedQuery query) {
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

        List<SearchResponse> resultstf = scorer.tfIdf(documents);
        List<SearchResponse> resultsbm = scorer.bm25(documents);

        return resultstf;
    }

    private List<SearchResponse> booleanAndSearch(ParsedQuery query) {
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

        List<SearchResponse> resultstf = scorer.tfIdf(documents);
        List<SearchResponse> resultsbm = scorer.bm25(documents);

        return resultstf;
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

    private List<SearchResponse> phraseSearch(ParsedQuery query) {
        List<String> terms = query.getTerms();
        String anchorWord = terms.get(0);
        for (String word : terms) {
            if (invertedIndex.containsTerm(word)) {
                if (invertedIndex.getPosting(word).size() < invertedIndex.getPosting(anchorWord).size())
                    anchorWord = word;
            } else return new ArrayList<>();
        }
        int anchorIndex = -1;
        for (int i = 0; i < terms.size(); i++) {
            if (terms.get(i).equals(anchorWord)) {
                anchorIndex = i;
                break;
            }
        }
        HashSet<Integer> documents = new HashSet<>();
        for (Posting anchorPosting : invertedIndex.getPosting(anchorWord)) {
            Integer docId = anchorPosting.getDocumentId();

            for (int pos : anchorPosting.getPosition()) {
                boolean match = true;
                for (int i = 0; i < terms.size(); i++) {
                    if (i == anchorIndex) continue;
                    int requiredPos = pos + (i - anchorIndex);
                    Posting p = invertedIndex.getPosting(terms.get(i)).stream().filter(posting -> posting.getDocumentId().equals(docId)).findFirst().orElse(null);

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
        List<SearchResponse> result = new ArrayList<>();
        for (Integer id : documents) {
            SearchResponse searchResponse = SearchResponse.builder().documentId(id).build();
            result.add(searchResponse);
        }
        return result;

    }
}
