package com.portfolio.inverted.service;

import com.portfolio.inverted.entity.FileDetails;
import com.portfolio.inverted.utils.FileIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileProcessor {
    private static Logger log = LoggerFactory.getLogger(FileProcessor.class);
    private final FileIndex fileIndex;
    private Analyser analyser;
    private Indexer indexer;

    public FileProcessor(Analyser analyser, Indexer indexer, FileIndex fileIndex) {
        this.analyser = analyser;
        this.indexer = indexer;
        this.fileIndex = fileIndex;
    }

    public void readFile(List<String> paths) throws FileNotFoundException {
        int countfordocid = 0;
        for (String path : paths) {
            FileReader file = new FileReader(path);
            Integer docId = ++countfordocid;
            try (BufferedReader reader = new BufferedReader(file)) {
                List<String> document = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    document.addAll(analyser.onlyWords(line));
                }
                addFileToFileIndex(path, docId, document.size());
                indexer.createIndex(document, docId);


            } catch (IOException e) {
                log.error("FileNotFoundException", e);
            }
        }
    }

    private void addFileToFileIndex(String path, Integer docId, int size) {
        fileIndex.addDocument(docId, FileDetails.builder().filePath(path).fileLength(size).build());
    }

}
