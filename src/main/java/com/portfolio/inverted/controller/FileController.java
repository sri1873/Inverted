package com.portfolio.inverted.controller;


import com.portfolio.inverted.entity.Posting;
import com.portfolio.inverted.service.FileProcessor;
import com.portfolio.inverted.service.Indexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@RestController
public class FileController {

    @Autowired
    private FileProcessor fileProcessor;

    @Autowired
    private Indexer indexer;

    @PostMapping("/indexFile")
    public void addFileToIndex(@RequestBody List<String> paths) throws FileNotFoundException {
        fileProcessor.readFile(paths);
    }

    @GetMapping("/getLocalIndex")
    public Map<String, List<Posting>> getLocalIndex() {
        return indexer.printIndex();
    }
}
