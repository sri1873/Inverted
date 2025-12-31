package com.portfolio.inverted.utils;

import com.portfolio.inverted.entity.FileDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FileIndex {

    private final Map<Integer, FileDetails> index = new HashMap<>();

    public Map<Integer, FileDetails> getIndex() {
        return index;
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
