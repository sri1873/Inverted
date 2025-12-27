package com.portfolio.inverted;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

@Service
public class FileReader {
    private static Logger log = LoggerFactory.getLogger(FileReader.class);

    private Analyser analyser;

    private Indexer indexer;

    public FileReader(Analyser analyser, Indexer indexer) {
        this.analyser = analyser;
        this.indexer = indexer;
    }

    public void readFile(List<String> paths) {
        for (String path : paths) {
            File file = new File(path);
            Integer docId = file.hashCode();
            try (Scanner reader = new Scanner(file)) {
                int position = 0;
                while (reader.hasNext()) {
                    List<String> document = analyser.onlyWords(reader.nextLine());
                    indexer.createIndex(document, docId, position);
                    position += document.size();
                }

            } catch (FileNotFoundException e) {
                log.error("FileNotFoundException", e);
            }
        }
        indexer.printIndex();
    }
}
