package com.portfolio.inverted;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
@Service
public class FileReader {
    private Analyser analyser=new Analyser();

    private Indexer indexer=new Indexer();

    public void readFile(List<String> paths){
        for(String path:paths) {
            File file = new File(path);
            Integer docId = file.hashCode();
            try (Scanner reader = new Scanner(file)) {
                Integer position = 0;
                while (reader.hasNext()) {
                    List<String> document = analyser.onlyWords(reader.nextLine());
                    indexer.createIndex(document, docId,position);
                    position+=document.size();
                }

            } catch (FileNotFoundException e) {
                System.out.println("FileNotFoundException");
                e.printStackTrace();
            }
        }
        indexer.printIndex();
    }
}
