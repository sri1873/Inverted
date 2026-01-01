package com.portfolio.inverted.utils;

import com.portfolio.inverted.entity.FileDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FileIndex {

    private final Map<Integer, FileDetails> index = new HashMap<>();
    private double averageDocumentLength = -1;

    public Map<Integer, FileDetails> getIndex() {
        return index;
    }

    public void addDocument(int docId, FileDetails file) {
        if (!index.containsKey(docId)) {
            index.put(docId, file);
            averageDocumentLength += file.getFileLength();
        }
    }


    public double getAverageDocumentLength() {
        return averageDocumentLength / index.size();
    }

    public Integer getTotalDocuments() {
        return index.size();
    }

    public boolean containsFile(Integer documentId) {
        return index.containsKey(documentId);
    }

    public FileDetails getFileDetails(Integer documentId) {
        return index.get(documentId);
    }
}
